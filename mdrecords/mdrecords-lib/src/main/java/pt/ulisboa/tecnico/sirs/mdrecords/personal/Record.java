package pt.ulisboa.tecnico.sirs.mdrecords.personal;
import pt.ist.fenixframework.FenixFramework;

import org.joda.time.DateTime;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.sirs.kerby.SecurityHelper;

import javax.crypto.*;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


/**
 * A class for describing changes made by the staff on client data
 */
public class Record extends Record_Base {
    
    public Record() {
        super();
    }

    public Record(long personalId, long patientId, DateTime timeStamp, String speciality, String description) throws InvalidRecordException {
        checkArguments(personalId, patientId, timeStamp, speciality, description);

        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(timeStamp);
        setSpeciality(speciality);
        setDescription(description);

        FenixFramework.getDomainRoot().getSns().addRecord(this);

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

    @Override
    public String toString() {
        String res = "<Record ";

        res += getPersonalId() + ", ";
        res += getPatientId() + ", ";
        res += getTimeStamp() + ", ";
        res += "\"" + getSpeciality() + "\", ";
        res += "\"" + getDescription() + "\"";

        res = ">";
        return res;
    }

    /**
     * Calculates a digest
     * @return a base64 textual representation of the digest
     * @throws NoSuchAlgorithmException
     */
    public byte[] calcDigest() throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] digestBytes = digest.digest(toString().getBytes());

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
    public boolean checkAuthenticity(Key publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        if (getDigest() == null || getDigest().equals(""))
            return false;

        /**Starts by calculating the digest**/
       byte[] digest = calcDigest();

       /** Deciphers the stored digest using the author's public key**/
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        byte[] cipheredDigest = DatatypeConverter.parseBase64Binary(getDigest());

        byte[] storedDigestBytes = cipher.doFinal(cipheredDigest);

        /** Check if both digests are the same **/
        return Arrays.equals(digest, storedDigestBytes);


    }

    public RecordView getView() {
        return new RecordView(getPersonalId(), getPatientId(), getTimeStamp(), getSpeciality(), getDescription());
    }

}
