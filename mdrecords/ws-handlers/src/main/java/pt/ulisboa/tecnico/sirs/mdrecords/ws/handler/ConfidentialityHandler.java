package pt.ulisboa.tecnico.sirs.mdrecords.ws.handler;

import pt.ulisboa.tecnico.sirs.kerby.CipherClerk;
import pt.ulisboa.tecnico.sirs.kerby.CipheredView;
import pt.ulisboa.tecnico.sirs.kerby.SecurityHelper;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * This SOAPHandler outputs the endpoint address of messages, if available.
 */
public class ConfidentialityHandler implements SOAPHandler<SOAPMessageContext> {
    
    /**
	 * Gets the header blocks that can be processed by this Handler instance. If
	 * null, processes all.
	 */
	@Override
	public Set<QName> getHeaders() {
		return null;
	}

	/**
	 * The handleMessage method is invoked for normal processing of in-bound and
	 * out-bound messages.
	 */
	@Override
	public boolean handleMessage(SOAPMessageContext smc) {
        Boolean outboundElement = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        //vitor: just ignore this message. I need the session key on the context to encrypt/decrypt!
        if (smc.get("sessionKey") == null) {
        	return true;
		}

			try {
				SOAPMessage soapMessage = smc.getMessage();
				SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
				SOAPBody soapBody = soapEnvelope.getBody();

				@SuppressWarnings("unchecked")
				Iterator<SOAPBodyElement> elements = soapBody.getChildElements();

				//Gets the session key from the context
				Key sessionKey = (Key) smc.get("sessionKey");

				CipherClerk cipherClerk = new CipherClerk();

				//traverse the body elements
				while (elements.hasNext()) {
					SOAPBodyElement element = elements.next();

					@SuppressWarnings("unchecked")
					Iterator<SOAPBodyElement> params = element.getChildElements();
					while (params.hasNext()) {
						SOAPBodyElement param = params.next();
						String paramValue = null;


						try {

							if (outboundElement) {
								paramValue = param.getValue();

								Cipher cipher = SecurityHelper.initCipher(sessionKey);
								byte[] cipherBytes = cipher.doFinal(paramValue.getBytes());

								String encryptedParam = DatatypeConverter.printBase64Binary(cipherBytes);

								//Replace the plaintext body argument value with the encrypted one
								//param.setValue(encryptedParam);
							}
							else {
								System.out.println("Decrypt: " + param.getValue());
								/*byte[] cipherBytes = DatatypeConverter.parseBase64Binary(param.getValue());

								Cipher cipher = SecurityHelper.initCipher(sessionKey);
								byte[] bytes = cipher.doFinal(cipherBytes);

								paramValue = new String(bytes);

								//Replace the encrypted body argument value with the plaintext one
								param.setValue(paramValue);*/

							}


								//Ciphers the content of the parameter using session key
								//And convert them to base 64 (textual representation)



						} catch (NoSuchAlgorithmException e) {
							e.printStackTrace();
						} catch (InvalidKeyException e) {
							e.printStackTrace();
						} catch (NoSuchPaddingException e) {
							e.printStackTrace();
						} catch (BadPaddingException e) {
							e.printStackTrace();
						} catch (IllegalBlockSizeException e) {
							e.printStackTrace();
						}

					}
				}

			} catch (SOAPException e) {
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
}