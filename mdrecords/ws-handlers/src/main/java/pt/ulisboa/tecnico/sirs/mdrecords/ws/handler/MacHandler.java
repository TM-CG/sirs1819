package pt.ulisboa.tecnico.sirs.mdrecords.ws.handler;



import javax.crypto.Mac;
import javax.xml.bind.DatatypeConverter;

import org.w3c.dom.Document;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * This SOAPHandler will be used to intercept interactions between binas-ws-cli and
 * binas-ws SOAP messages.
 */

public class MacHandler implements SOAPHandler<SOAPMessageContext> {

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

        Key sessionKey = (Key) smc.get("sessionKey");
        try {
            SOAPMessage msg = smc.getMessage();
            SOAPPart sp = msg.getSOAPPart();
            SOAPEnvelope se = sp.getEnvelope();
            SOAPBody sb = se.getBody();

            DOMSource domSource = new DOMSource(sb);
            StringWriter stringResult = new StringWriter();
            TransformerFactory.newInstance().newTransformer().transform(domSource, new StreamResult(stringResult));
            String bodyStr = stringResult.toString();

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(sessionKey);

            SOAPHeader sh = se.getHeader();
            if (sh == null)
                sh = se.addHeader();

            byte[] msgToDigest = DatatypeConverter.parseBase64Binary(bodyStr);
            byte[] digest = mac.doFinal(msgToDigest);

            if (outbound) { //if message is outbound -> add mac to soap envelope

                Name name = se.createName("mac", "m", "http://mac");
                SOAPHeaderElement element = sh.addHeaderElement(name);

                // add header element value
                element.addTextNode(DatatypeConverter.printBase64Binary(digest));
            } else { // if message is inbound -> validate integrity

                  // get hash
                Name name = se.createName("mac", "m", "http://mac");
                Iterator it = sh.getChildElements(name);

                // check header element
                if (!it.hasNext()) {
                    throw new RuntimeException();
                }

                SOAPElement element = (SOAPElement) it.next();

                byte[] expected = DatatypeConverter.parseBase64Binary(element.getValue());

                if (Arrays.equals(expected, digest)) {
                    return true;
                } else {
                    throw new RuntimeException("Message was modified");
                }
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException | SOAPException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return true;
    }

    /** The handleFault method is invoked for fault message processing. */
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

    private static byte[] SOAPMessageToByteArray(SOAPMessage msg) throws Exception {
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        byte[] msgByteArray = null;

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        Source source = msg.getSOAPPart().getContent();
        Result result = new StreamResult(byteOutStream);
        transformer.transform(source, result);

        msgByteArray = byteOutStream.toByteArray();
        return msgByteArray;
    }

}