package pt.ulisboa.tecnico.sirs.mdrecords.personal;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.omg.CORBA.DynAnyPackage.Invalid;
import pt.ist.fenixframework.FenixFramework;

import org.joda.time.DateTime;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.sirs.kerby.SecurityHelper;

import javax.crypto.*;
import javax.xml.bind.DatatypeConverter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;


/**
 * A class for describing changes made by the staff on client data
 */
public class Record extends Record_Base {
    
    public Record() {
        super();
    }

    public Record(SecretKey serverKey, long personalId, long patientId, DateTime timeStamp, String speciality, String description, String digest) throws InvalidRecordException {
        checkArguments(personalId, patientId, timeStamp, speciality, description);

        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(serverKey, timeStamp);
        setSpeciality(serverKey, speciality);
        setDescription(serverKey, description);


        try {
            checkIncomingDigest(serverKey, personalId);

        } catch (IllegalBlockSizeException e) {
            throw new InvalidRecordException(e.getMessage());
        } catch (InvalidKeyException e) {
            throw new InvalidRecordException(e.getMessage());
        } catch (BadPaddingException e) {
            throw new InvalidRecordException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidRecordException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            throw new InvalidRecordException(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new InvalidRecordException(e.getMessage());
        } catch (CertificateException e) {
            throw new InvalidRecordException(e.getMessage());
        }

        setDigest(digest);

        FenixFramework.getDomainRoot().getSns().addRecord(this);

    }

    protected void checkIncomingDigest(SecretKey serverKey, long personalId) throws IllegalBlockSizeException,
            InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidRecordException, FileNotFoundException, CertificateException {
        //Get User public key
        PublicKey userKey = CertificateHelper.readPublicKey(new Long(personalId).toString());

        if (checkAuthenticity(serverKey, userKey) == false) {
            throw new InvalidRecordException("RECORD: Invalid digest!");
        }

    }

        protected void checkArguments(long personalId, long patientId, DateTime timeStamp, String speciality, String description)
    throws InvalidRecordException {
        if (personalId < 0)
            throw new InvalidRecordException("Invalid Personal ID in Record!");
        if (patientId < 0)
            throw new InvalidRecordException("Invalid Patient ID in Record!");
        if (timeStamp.isAfterNow())
            throw new InvalidRecordException("Invalid timeStamp ID in Record, it is from future!");
    }

    public String toString(SecretKey serverKey) {
        String res = "<Record ";

        res += getPersonalId() + ", ";
        res += getPatientId() + ", ";
        res += getTimeStamp(serverKey) + ", ";
        res += "\"" + getSpeciality(serverKey) + "\", ";
        res += "\"" + getDescription(serverKey) + "\"";

        res = ">";
        return res;
    }

    /**
     * Calculates a digest
     * @return a base64 textual representation of the digest
     * @throws NoSuchAlgorithmException
     */
    public byte[] calcDigest(SecretKey serverKey) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] digestBytes = digest.digest(toString(serverKey).getBytes());

        return digestBytes;
    }

    /**
     * Given a public key allows to check the authenticity of a given record
     * @param publicKey of the author
     * @return true if authenticity is correct and false otherwise
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public boolean checkAuthenticity(SecretKey serverKey, Key publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        if (getDigest() == null || getDigest().equals(""))
            return false;

        /**Starts by calculating the digest**/
       byte[] digest = calcDigest(serverKey);

       /** Deciphers the stored digest using the author's public key**/
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        byte[] cipheredDigest = DatatypeConverter.parseBase64Binary(getDigest());

        byte[] storedDigestBytes = cipher.doFinal(cipheredDigest);

        /** Check if both digests are the same **/
        return Arrays.equals(digest, storedDigestBytes);


    }

    public RecordView getView(SecretKey serverKey) {
        return new RecordView(getPersonalId(), getPatientId(), getTimeStamp(serverKey), getSpeciality(serverKey), getDescription(serverKey));
    }

    /**
     * Setter encapsulating database encryption
     * @param serverKey
     * @param timeStamp
     */
    public void setTimeStamp(SecretKey serverKey, DateTime timeStamp) {
        try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
            String str = timeStamp.toString(fmt);
            String encryptedTimeStamp= SNS.encrypt(serverKey, str);
            super.setTimeStamp(encryptedTimeStamp);

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    /**
     * Secure getter for timeStamp
     * @param serverKey
     * @return
     */
    public DateTime getTimeStamp(SecretKey serverKey) {
        String timeStamp = super.getTimeStamp();
        try {
            String strTimeStamp = SNS.decrypt(serverKey, timeStamp);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
            return formatter.parseDateTime(strTimeStamp);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Secure setter for TimeStamp
     * @param serverKey
     * @param speciality
     */
    public void setSpeciality(SecretKey serverKey, String speciality) {
        try {
            String encryptedSpeciality = SNS.encrypt(serverKey, speciality);
            super.setSpeciality(encryptedSpeciality);

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    /**
     * Secure getter for speciality
     * @param serverKey
     * @return
     */
    public String getSpeciality(SecretKey serverKey) {
        String speciality = super.getSpeciality();
        try {
            return SNS.decrypt(serverKey, speciality);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Secure setter for description
     * @param serverKey
     * @param description
     */
    public void setDescription(SecretKey serverKey, String description) {
        try {
            String encryptedDescription = SNS.encrypt(serverKey, description);
            super.setDescription(encryptedDescription);

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    /**
     * Secure getter to Description
     * @param serverKey
     * @return
     */
    public String getDescription(SecretKey serverKey) {
        String description = super.getDescription();
        try {
            return SNS.decrypt(serverKey, description);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

}
