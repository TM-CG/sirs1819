package pt.ulisboa.tecnico.sirs.mdrecords.ws.handler;

import pt.ulisboa.tecnico.sirs.kerby.*;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

public class ServerAuthHandler implements SOAPHandler<SOAPMessageContext> {
    /**To synchronize client and server clocks**/
    private static final int DELTA = 60;

    private HashSet<Long> timeStamps = new HashSet<Long>();
    
    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    /**
     * Returns the string content of an header tag
     * @param se SOAPEnvelope
     * @param sh SOAPHeader
     * @param localName attribute local name
     * @param prefix the attribute prefix
     * @param uri the tag universal resource identifier
     * @return the string content of that tag
     * @throws SOAPException
     */
    private String getHeaderAttribute(SOAPEnvelope se, SOAPHeader sh, String localName, String prefix, String uri) throws SOAPException {
        Name name = se.createName(localName, prefix, uri);
        Iterator it = sh.getChildElements(name);
        if (!it.hasNext()) {
            System.out.println("Header element has not found!");
            return null;
        }

        SOAPElement element = (SOAPElement) it.next();
        String result = element.getValue();
        return result;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outboundElement = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        try{
            // === INBOUND ===
            if(!outboundElement.booleanValue()){

                Key sessionKey = (Key)context.get("sessionKey");
                Date nowDate = (Date)context.get("date");
                long now = nowDate.getTime();
                String clientName = (String) context.get("clientName");

                // get SOAP envelope
                SOAPMessage msg = context.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();

                // get header
                SOAPHeader sh = se.getHeader();

                //textual representation of the header
                String authenticator = getHeaderAttribute(se, sh, "auth", "d", "http://ws.binas.org/");

                CipherClerk cipher = new CipherClerk();
                CipheredView cv = null;
                try {
                    //parse received string to bytes before decipher
                    byte[] parsedHeader = parseBase64Binary(authenticator);
                    cv = cipher.cipherFromXMLBytes(parsedHeader);
                } catch (JAXBException e) {
                    throw new RuntimeException("AUTH HANDLER: Cannot parse receive string to bytes!");
                }


                //Decipher ciphered view of auth using sessionkey
                Auth auth = new Auth(cv, sessionKey);

                //Check auth validation
                auth.validate();
                
                RequestTime requestTime = new RequestTime(auth.getTimeRequest());
                requestTime.validate();
                
                //Checks if timestamp from auth was already declared

                Calendar timeStamp = Calendar.getInstance();
                timeStamp.setTime(auth.getTimeRequest());

                
                if(timeStamps.contains(timeStamp.getTimeInMillis())) {
                    throw new RuntimeException("AUTH HANDLER: TimeStamp expired!");
                }
                timeStamps.add(timeStamp.getTimeInMillis());
                
                //if client name doesnt match then abort and send fault message
                if (!auth.getX().equals(clientName)) {
                    throw new RuntimeException("AUTH HANDLER: Client name does not match session key info!");
                }

                //Compare received timestamp with current date time
                Calendar calendar = Calendar.getInstance();
                //Clone two calendar to calc limit after and limit before
                Calendar c1 = (Calendar) calendar.clone();
                Calendar c2 = (Calendar) calendar.clone();
                //Calc after and before
                c1.add(Calendar.SECOND,  - DELTA);
                c2.add(Calendar.SECOND,  2 * DELTA);

                //if timestamp not in interval: maxBefore < timestamp < maxAfter
                if (!(timeStamp.compareTo(c1) > 0 && timeStamp.compareTo(c2) < 0))
                    throw new RuntimeException("AUTH_HANDLER: TimeStamp does not match! Message from the past or the future!");
                
                
                context.put("timeRequest", auth.getTimeRequest());
                
                //Returns a node list with all "email" nodes
                SOAPBody body = se.getBody();
                NodeList nodes = body.getElementsByTagName("email");
                
                //Since there's only one "email" node, retrieve it from list
                Node node = nodes.item(0);
                
                if(node != null) {
                	//Get email
                	String requestEmail = node.getTextContent();
                
                	if(!clientName.equals(requestEmail))
                        throw new RuntimeException("AUTH HANDLER: E-mail does not match session key client!");
                	

                }
            } else { // === OUTBOUND ===
            	Date timeRequest = (Date)context.get("timeRequest");
            	Key sessionKey = (Key)context.get("sessionKey");
            	
            	//TimeRequest is encripted with session key
            	RequestTime requestTime = new RequestTime(timeRequest);
            	CipheredView cipheredRequestTime = null;
            	
            	try {
            		cipheredRequestTime = requestTime.cipher(sessionKey);
            		
            	} catch(KerbyException e) {
            		throw new RuntimeException("SERVERAUTH_HANDLER: Error during cipher of timeRequest using session key!");
            	}
            	
            	//Convert timerequest to xml bytes
            	CipherClerk cipher = new CipherClerk();
            	
            	byte[] req = null;
            	try {
            		req = cipher.cipherToXMLBytes(cipheredRequestTime, "timeRequest");
            	} catch (JAXBException e) {
            		throw new RuntimeException("SERVERAUTH_HANDLER: Problem parsing timeRequest to XML bytes!");
            	}
            	
            	//Add timeRequest to header
            	try {
                    // get SOAP envelope
                    SOAPMessage msg = context.getMessage();
                    SOAPPart sp = msg.getSOAPPart();
                    SOAPEnvelope se = sp.getEnvelope();

                    // add header
                    SOAPHeader sh = se.getHeader();
                    if (sh == null)
                        sh = se.addHeader();

                    Name name = se.createName("timeRequest", "d", "http://ws.binas.org/");
                    SOAPHeaderElement element = sh.addHeaderElement(name);
                    String binTimeRequest = printBase64Binary(req);
                    element.addTextNode(binTimeRequest);

                } catch (SOAPException e) {
                    throw new RuntimeException("SERVERAUTH_HANDLER: SOAP Error when add timeRequest to the header!");
                }
            	
            }

        } catch (SOAPException e) {
            throw new RuntimeException("AUTH HANDLER: SOAP message error!");
        } catch (KerbyException e) {
            throw new RuntimeException("AUTH HANDLER: Kerby exception!");
        }

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return false;
    }

    @Override
    public void close(MessageContext context) {

    }
}
