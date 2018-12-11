package pt.ulisboa.tecnico.sirs.mdrecords.ws.handler;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * This SOAPHandler will be used to intercept interactions between client and servers
 */

public class MessageManipulatorHandler implements SOAPHandler<SOAPMessageContext> {
    public static boolean hacked = false;
    private static boolean delay = false;
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
        try{
            if(delay){
                TimeUnit.SECONDS.sleep(10);
            }
            if(!hacked){
                return true;
            }

            SOAPMessage msg = smc.getMessage();
            SOAPPart sp = msg.getSOAPPart();
            SOAPEnvelope se = sp.getEnvelope();

        if(outbound){
            Name name = se.createName("hacker");
            SOAPBody sb = se.getBody();
            SOAPElement elem = sb.addBodyElement(name);
            Name attribute = se.createName("injectedMessage");
            elem.addAttribute(attribute, "INJECTED MESSAGE");

        } else { // inbound message

           }

        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }


    /** The handleFault method is invoked for fault message processing. */
    @Override
    public boolean handleFault(SOAPMessageContext smc) {
        System.out.println("KerberosClientHandler: Handling fault message..");
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

    public static void setHacked(){
        hacked = true;
    }

    public static void setDelay(){
        delay = true;
    }
}