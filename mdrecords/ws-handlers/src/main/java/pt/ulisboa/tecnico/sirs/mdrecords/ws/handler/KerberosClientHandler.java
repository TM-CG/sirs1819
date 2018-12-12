package pt.ulisboa.tecnico.sirs.mdrecords.ws.handler;

import java.security.Key;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.w3c.dom.NodeList;
import pt.ulisboa.tecnico.sirs.kerby.*;
import pt.ulisboa.tecnico.sirs.kerby.cli.KerbyClient;
import pt.ulisboa.tecnico.sirs.kerby.cli.KerbyClientException;
import sun.misc.Request;

import javax.crypto.Cipher;
import javax.management.openmbean.InvalidKeyException;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBException;
import javax.xml.soap.*;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;

import java.util.Iterator;
import java.util.Set;
import java.util.Date;

/**
 * This SOAPHandler will be used to intercept interactions between client and kerberos server.
 */

public class KerberosClientHandler implements SOAPHandler<SOAPMessageContext> {

    private static String currentUser = "12345678";
    private static String currentPassword = "Zd8hqDu23t";

    private static final String SERVER_NAME = "mdrecords.pt";

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

    public static void setCurrentUser(String user){
        currentUser = user;
    }

    public static void setCurrentPassword(String password) { currentPassword = password; }

    public boolean processMessage(SOAPMessageContext smc) {
        Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if(outbound){
            try{

                // get SOAP envelope
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();

                Key clientKey = SecurityHelper.generateKeyFromPassword(currentPassword);

                KerbyClient client = new KerbyClient("http://localhost:8888/kerby");

                SecureRandom randomGenerator = new SecureRandom();
                long nounce = randomGenerator.nextLong();
                SessionKeyAndTicketView result = client.requestTicket(currentUser, SERVER_NAME , nounce, 60);

                CipherClerk cipherClerk = new CipherClerk();

                CipheredView cipheredSessionKey = result.getSessionKey();
                CipheredView cipheredTicket = result.getTicket();

                cipherClerk.cipherToXMLBytes(cipheredTicket, "ticket");
                SessionKey sessionKey = new SessionKey(cipheredSessionKey, clientKey);

                // add header
                SOAPHeader sh = se.getHeader();
                if (sh == null)
                    sh = se.addHeader();

                // add header element (ticket) (name, namespace prefix, namespace)
                Name name = se.createName("myTicket", "t", "http://ticket");
                SOAPHeaderElement element = sh.addHeaderElement(name);
                // add header element value
                element.addTextNode(DatatypeConverter.printBase64Binary(cipherClerk.cipherToXMLBytes(cipheredTicket, "ticket")));

                // Create Authenticator
                Auth auth = new Auth(currentUser, new Date());

                // Put auth on messagecontext so that ClientHAndler can later compare with treq sent in response
                smc.put("auth", auth);

                CipheredView cipheredAuth = auth.cipher(sessionKey.getKeyXY());
                name = se.createName("myAuth", "a", "http://auth");
                element = sh.addHeaderElement(name);

                // add header element value
                element.addTextNode(DatatypeConverter.printBase64Binary(cipherClerk.cipherToXMLBytes(cipheredAuth, "auth")));

                //Put sessionKey on messageContext so MacHandler can use it to digest the message
                smc.put("sessionKey", sessionKey.getKeyXY());
                smc.put("alreadyHaveSessionKey", true);

            } catch (NoSuchAlgorithmException | InvalidKeySpecException | BadTicketRequest_Exception
                    | KerbyClientException | SOAPException | JAXBException | KerbyException e) {
                e.printStackTrace();
            }
            return true;
        } else { // inbound message
            try {
                // get SOAP envelope
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();

                SOAPHeader sh = se.getHeader();
                if (sh == null)
                    sh = se.addHeader();

                Name name = se.createName("myTreq", "treq", "http://treq");
                Iterator it = sh.getChildElements(name);

                smc.put("alreadyHaveSessionKey", true);

                // check header element
                if (!it.hasNext()) {
                    return true;
                }
                SOAPElement element = (SOAPElement) it.next();
                //get header element value
                CipherClerk cipherClerk = new CipherClerk();
                CipheredView treqCipher = cipherClerk.cipherFromXMLBytes(DatatypeConverter.parseBase64Binary(element.getValue()));
                RequestTime treq = new RequestTime(treqCipher, (Key) smc.get("sessionKey"));

                Auth auth = (Auth) smc.get("auth");

                if(treq.getTimeRequest().equals(auth.getTimeRequest())){
                    return true;
                } else {
                    throw new RuntimeException("treq from server response does not match client request treq");
                }




            } catch (SOAPException | JAXBException | KerbyException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}