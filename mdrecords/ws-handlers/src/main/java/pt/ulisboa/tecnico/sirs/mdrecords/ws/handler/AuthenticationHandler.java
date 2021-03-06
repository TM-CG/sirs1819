package pt.ulisboa.tecnico.sirs.mdrecords.ws.handler;

import org.w3c.dom.NodeList;
import pt.ulisboa.tecnico.sirs.kerby.*;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * This SOAPHandler will be used to intercept interactions between client and server and attach a authenticator.
 */

public class AuthenticationHandler implements SOAPHandler<SOAPMessageContext> {

    public static final int MARGIN_ERROR = 5000; // time in ms
    Set<Date> timeRequests = new TreeSet<Date>();

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
        return processMessage(smc);
    }


    /** The handleFault method is invoked for fault message processing. */
    @Override
    public boolean handleFault(SOAPMessageContext smc) {
        System.out.println("AuthenticationHandler: Handling fault message..");
        return processMessage(smc);
    }


    /**
     * Called at the conclusion of a message exchange pattern just prior to the
     * JAX-WS runtime dispatching a message, fault or exception.
     */
    @Override
    public void close(MessageContext messageContext) {
        // nothing to clean up
    }


    public boolean processMessage(SOAPMessageContext smc) {
        Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        //vitor: just ignore this message. I need the session key on the context to encrypt/decrypt! It will be done
        //in a close future
        if (smc.get("alreadyHaveSessionKey") == null) {
            return true;
        }

        if(outbound) {
            try {

                // get SOAP envelope header
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();
                SOAPHeader sh = se.getHeader();

                if (sh == null) {
                    System.out.println("Header not found.");
                    return true;
                }

                Auth auth = (Auth) smc.get("auth");
                Key sessionKey = (Key) smc.get("sessionKey");

                // create treq in order to cipher and send back to client
                RequestTime treq = new RequestTime(auth.getTimeRequest());
                CipheredView cipheredTreq = treq.cipher(sessionKey);
                Name name = se.createName("myTreq", "treq", "http://treq");
                SOAPHeaderElement headerElement = sh.addHeaderElement(name);

                // add treq ciphered with sessionkey to response message header
                CipherClerk cipherClerk = new CipherClerk();
                headerElement.addTextNode(DatatypeConverter.printBase64Binary(cipherClerk.cipherToXMLBytes(cipheredTreq, "treq")));


            } catch (SOAPException | KerbyException | JAXBException e) {
                e.printStackTrace();
            }
        }else{ //inbound

            //just remove the flag for next request
            smc.remove("alreadyHaveSessionKey");

            Auth auth = (Auth) smc.get("auth");

            //protect against replay attacks
            if(!timeRequests.contains(auth.getTimeRequest())){
                timeRequests.add(auth.getTimeRequest());
            } else {
                throw new RuntimeException("Received same request twice. Ignoring...");
            }

            //check whether client request is relatively new
            Date currentTime = new Date();
            if (currentTime.getTime() - auth.getTimeRequest().getTime() < MARGIN_ERROR &&
                    currentTime.after(auth.getTimeRequest())) {
                return true;
            } else {
                throw new RuntimeException("Client request too old. Ignoring...");
            }


        }
        return true;
    }
}