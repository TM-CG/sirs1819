package pt.ulisboa.tecnico.sirs.kerby;

import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class KerbyManager {
	
	private static final int MIN_TICKET_DURATION = 10;
	private static final int MAX_TICKET_DURATION = 300;
	private static Set<UserNouncePair> previousNounces = Collections.synchronizedSet(new HashSet<UserNouncePair>());
	private static ConcurrentHashMap<String, PublicKey> knownKeys = new ConcurrentHashMap<String, PublicKey>();
	private static String salt;
	
	// Singleton -------------------------------------------------------------
	private KerbyManager() {
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final KerbyManager INSTANCE = new KerbyManager();
	}

	public static synchronized KerbyManager getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public SessionKeyAndTicketView requestTicket(String client, String server, long nounce, int ticketDuration) 
			throws BadTicketRequestException {
		
		/* Validate parameters */
		if(client == null || client.trim().isEmpty())
			throw new BadTicketRequestException("Null Client.");
		if(knownKeys.get(client) == null)
			throw new BadTicketRequestException("Unknown Client.");
		if(server == null || server.trim().isEmpty())
			throw new BadTicketRequestException("Null Server.");
		if(knownKeys.get(server) == null)
			throw new BadTicketRequestException("Unknown Server.");
		if(ticketDuration < MIN_TICKET_DURATION || ticketDuration > MAX_TICKET_DURATION)
			throw new BadTicketRequestException("Invalid Ticked Duration.");
		
		UserNouncePair userNounce = new UserNouncePair(client, nounce);
		if(previousNounces.contains(userNounce))
			throw new BadTicketRequestException("Repeated Nounce, possible Replay Attack.");
		
		
		try {
			/* Get Previously Generated Client and Server Keys */
			Key clientKey = knownKeys.get(client);
			Key serverKey = knownKeys.get(server);

			/* Generate a new key for Client-Server communication */
			Key clientServerKey = SecurityHelper.generateKey();
			
			/* Create and Cipher the Ticket */
			Ticket ticket = createTicket(client, server, ticketDuration, clientServerKey);

            CipheredView cipheredTicket = ticket.cipher(serverKey);
            System.out.println("allgucci2");

			/* Create and Cipher the Session Key */
			SessionKey sessionKey = new SessionKey(clientServerKey, nounce);
            System.out.println("allgucci3");

            CipheredView cipheredSessionKey = sessionKey.cipher(clientKey);
            System.out.println("allgucci4");


            /* Create SessionKeyAndTicketView */
			SessionKeyAndTicketView response = new SessionKeyAndTicketView();
			response.setTicket(cipheredTicket);
			response.setSessionKey(cipheredSessionKey);
			
			/* Store UserNouncePair */
			previousNounces.add(userNounce);
			
			return response;
			
		} catch (NoSuchAlgorithmException e) {
			throw new BadTicketRequestException("Error generating shared key.");
		} catch (KerbyException e) {
			throw new BadTicketRequestException("Error while ciphering.");
		}
	}
	
	// Helpers -------------------------------------------------------------
	
	public void initSalt(String saltFilename) throws Exception {
		InputStream inputStream = KerbyManager.class.getResourceAsStream(saltFilename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line = reader.readLine();
		if(line != null && !line.trim().isEmpty())
			salt = line;
	}

	public void initKeysCert() throws Exception{

		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		URL caURL = loader.getResource("ca");
		String path_ca = caURL.getPath() + "/root_ca.pem";
		File ca = new File(path_ca);
        FileInputStream istream = new FileInputStream(ca);

        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        X509Certificate rootCA = (X509Certificate) fact.generateCertificate(istream);

		URL url = loader.getResource("certificates");
		String path = url.getPath();

		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles){
			if(file.isFile()){

                FileInputStream is= new FileInputStream(file);

				X509Certificate cer = (X509Certificate) fact.generateCertificate(is);
				if(verifySignature(cer, rootCA)) {
                    PublicKey key = cer.getPublicKey();

                    String subject = cer.getSubjectDN().getName();
                    String[] subjectInfo = subject.split(",", 2);
                    String[] commonName = subjectInfo[0].split("=", 2);

                    System.out.println(commonName[1]);
                    knownKeys.put(commonName[1], key);
                }else{
                    System.out.println("Certificate not valid");
                }
			}
		}
		System.out.println("Numero de chaves inseridas: " + knownKeys.size());
	}

	public static boolean verifySignature(X509Certificate certificate, X509Certificate issuingCertificate) {
		X500Principal subject = certificate.getSubjectX500Principal();
		X500Principal expectedIssuerSubject = certificate.getIssuerX500Principal();
		X500Principal issuerSubject = issuingCertificate.getSubjectX500Principal();
		PublicKey publicKeyForSignature = issuingCertificate.getPublicKey();

		try {
			certificate.verify(publicKeyForSignature);
			return true;
		} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException |
				NoSuchProviderException | SignatureException e) {
		}

		return false;
	}

	/** Reads Passwords from the given file, generates all keys and stores them in memory. */
	/*public void initKeys(String passwordFilename) throws Exception {
		InputStream inputStream = KerbyManager.class.getResourceAsStream(passwordFilename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while((line = reader.readLine()) != null) {
			line = line.trim();
			if(line.startsWith("#") || !line.contains(","))
				continue;
			String[] values = line.split(",");
			Key key;
			if(salt == null) {
				key = SecurityHelper.generateKeyFromPassword(values[1]);
			} else {
				key = SecurityHelper.generateKeyFromPassword(values[1], salt);
			}
			knownKeys.put(values[0], key);
		}
	}*/
	
	private Ticket createTicket(String client, String server, int ticketDuration, Key clientServerKey) {
		final Calendar calendar = Calendar.getInstance();
		final Date t1 = calendar.getTime();
		calendar.add(Calendar.SECOND, ticketDuration);
		final Date t2 = calendar.getTime();
		Ticket ticket = new Ticket(client, server, t1, t2, clientServerKey);
		return ticket;
	}
	
}
