package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * A class for describing basic RSA certificates operations.
 */
public class CertificateHelper {
    /**
     * Reads a private key from file
     * @param privateKeyPath the filename of the PEM file in certificates directory
     * @param keyPassword the password needed to access the private password
     * @return PrivateKey object to be used to encrypt/decrypt data.
     * @throws IOException
     */
    public static PrivateKey readPrivateKey(String privateKeyPath, String keyPassword) throws IOException {

        FileReader fileReader = new FileReader(privateKeyPath);
        PEMParser keyReader = new PEMParser(fileReader);

        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PEMDecryptorProvider decryptionProv = new JcePEMDecryptorProviderBuilder().build(keyPassword.toCharArray());

        Object keyPair = keyReader.readObject();
        PrivateKeyInfo keyInfo;

        if (keyPair instanceof PEMEncryptedKeyPair) {
            PEMKeyPair decryptedKeyPair = ((PEMEncryptedKeyPair) keyPair).decryptKeyPair(decryptionProv);
            keyInfo = decryptedKeyPair.getPrivateKeyInfo();
        } else {
            keyInfo = ((PEMKeyPair) keyPair).getPrivateKeyInfo();
        }

        keyReader.close();
        return converter.getPrivateKey(keyInfo);
    }

    /**
     * Reads a public key from certificate
     * @param publicKeyFile the certificate file name
     * @return PublicKey object
     * @throws FileNotFoundException
     * @throws CertificateException
     */
    public static PublicKey readPublicKey(String publicKeyFile) throws FileNotFoundException, CertificateException {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        URL caURL = loader.getResource("ca");
        String path_ca = caURL.getPath() + "/root_ca.pem";
        File ca = new File(path_ca);
        FileInputStream istream = new FileInputStream(ca);

        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        X509Certificate rootCA = (X509Certificate) fact.generateCertificate(istream);

        URL url = loader.getResource("certificates");
        String path = url.getPath();


        FileInputStream is= new FileInputStream(path + "/" + publicKeyFile + ".cert.pem");

        X509Certificate cer = (X509Certificate) fact.generateCertificate(is);
        if(verifySignature(cer, rootCA)) {
            String subject = cer.getSubjectDN().getName();
            String[] subjectInfo = subject.split(",", 2);
            String[] commonName = subjectInfo[0].split("=", 2);

            return cer.getPublicKey();

        }else{
            System.out.println("Certificate not valid");
            return null;
        }


    }

    /**
     * Verifies a signature of a certificate
     * @param certificate to be validated
     * @param issuingCertificate the root that certifies the certificate
     * @return true if it is valid and false otherwise
     */
    public static boolean verifySignature(X509Certificate certificate, X509Certificate issuingCertificate) {
        X500Principal subject = certificate.getSubjectX500Principal();
        X500Principal expectedIssuerSubject = certificate.getIssuerX500Principal();
        X500Principal issuerSubject = issuingCertificate.getSubjectX500Principal();
        PublicKey publicKeyForSignature = issuingCertificate.getPublicKey();

        try {
            certificate.verify(publicKeyForSignature);
            return true;
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Encrypts bytes using a RSA Private Key. Typically a signature of a digests.
     * @param key PrivateKey of the client
     * @param plaintext bytes to be encrypted
     * @return encrypted bytes
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] encrypt(PrivateKey key, byte[] plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(plaintext);
    }

    /**
     * Decrypts bytes using a RSA Public Key. Typically to check the authorship of a signature of a digest.
     * @param key Public Key of the author.
     * @param ciphertext The encrypted bytes to be decrypted. Typically a digest.
     * @return decrypted plaintext
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] decrypt(PublicKey key, byte[] ciphertext) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(ciphertext);
    }

    /**
     * Creates a Digest given a Record View
     * @param privateKeyPath
     * @param record
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchPaddingException
     */
    public static String createRecordDigest(String privateKeyPath, RecordView record) throws NoSuchAlgorithmException,
            IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        byte[] digest = record.calcDigest();

        PrivateKey privateKey = readPrivateKey(privateKeyPath, "");

        byte[] digestcipher = encrypt(privateKey, digest);

        return DatatypeConverter.printBase64Binary(digestcipher);
    }
}
