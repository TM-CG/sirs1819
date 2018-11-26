package pt.ulisboa.tecnico.sirs.mdrecords.ws.handler;

import pt.ulisboa.tecnico.sirs.kerby.*;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

/**
 * This SOAPHandler outputs the contents of the message context for inbound and
 * outbound messages.
 */
public class KerberosServerHandler implements SOAPHandler<SOAPMessageContext> {

    public static final String PASSWORD_MDRECORDS = "z5Pqwhi";

    public static final String CLASS_NAME = KerberosServerHandler.class.getSimpleName();



    //
    // Handler interface implementation
    //

    /**
     * Gets the header blocks that can be processed by this Handler instance. If
     * null, processes all.
     */
    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    /**
     * The handleMessage method is invoked for normal processing of inbound and
     * outbound messages.
     */
    @Override
    public boolean handleMessage(SOAPMessageContext smc) {
        Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound) {



        } else {
            // inbound message
            try {

                Key serverKey = SecurityHelper.generateKeyFromPassword(PASSWORD_MDRECORDS);

                // get SOAP envelope header
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();
                SOAPHeader sh = se.getHeader();

                if (sh == null) {
                    System.out.println("Header not found.");
                    return true;
                }

                Name name = se.createName("myTicket", "t", "http://ticket");
                Iterator it = sh.getChildElements(name);
                // check header element
                if (!it.hasNext()) {
                    return true;
                }
                SOAPElement element = (SOAPElement) it.next();

                // get header element value
                CipherClerk cipherClerk = new CipherClerk();
                CipheredView ticketCipher = cipherClerk.cipherFromXMLBytes(DatatypeConverter.parseBase64Binary(element.getValue()));
                Ticket ticket = new Ticket(ticketCipher, serverKey);
                ticket.validate();
                Key sessionKey = ticket.getKeyXY();

                smc.put("user", ticket.getX());
                smc.setScope("user", Scope.HANDLER);

                smc.put("sessionKey", sessionKey);
                // set property scope to application so that server class can
                // access property
                smc.setScope("sessionKey", Scope.HANDLER);


                //second element from header
                name = se.createName("myAuth", "a", "http://auth");
                it = sh.getChildElements(name);
                if (!it.hasNext()) {
                    System.out.printf("Auth element %s not found.%n", "myAuth");
                    return true;
                }
                element = (SOAPElement) it.next();

                CipheredView authView = cipherClerk.cipherFromXMLBytes(DatatypeConverter.parseBase64Binary(element.getValue()));
                Auth auth = new Auth(authView, sessionKey);
                auth.validate();
                System.out.printf("%s got '%s'%n", CLASS_NAME, auth);

                smc.put("auth", auth);


            } catch (SOAPException | JAXBException e) {
            } catch (KerbyException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
        return true;

    }

    /**
     * The handleFault method is invoked for fault message processing.
     */
    @Override
    public boolean handleFault(SOAPMessageContext smc) {
        return true;
    }

    /**
     * Called at the conclusion of a message exchange pattern just prior to the
     * JAX-WS runtime dispatching a message, fault or exception.
     */
    @Override
    public void close(MessageContext messageContext) {
        // nothing to clean up
    }
}


