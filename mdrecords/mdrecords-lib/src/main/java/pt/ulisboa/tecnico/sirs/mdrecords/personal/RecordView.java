package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;

import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A View of the Record.
 */
public class RecordView {

    public long personalId;
    public long patientId;
    public DateTime timeStamp;
    public String speciality;
    public String description;

    public RecordView() {}

    public RecordView(long personalId, long patientId, DateTime timeStamp, String speciality, String description) {
        this.personalId = personalId;
        this.patientId = patientId;
        this.timeStamp = timeStamp;
        this.speciality = speciality;
        this.description = description;
    }

    public String toString() {
        String res = "<Record ";

        res += this.personalId + ", ";
        res += this.patientId + ", ";
        res += this.timeStamp + ", ";
        res += "\"" + this.speciality + "\", ";
        res += "\"" + this.description + "\"";

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
}
