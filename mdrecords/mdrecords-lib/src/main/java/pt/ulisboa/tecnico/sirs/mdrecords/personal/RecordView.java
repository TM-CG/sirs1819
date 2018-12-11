package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;

import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A View of the Record.
 */
public class RecordView {

    private long personalId;
    private long patientId;
    private DateTime timeStamp;
    private String speciality;
    private String description;

    public RecordView(SecretKey secretKey, Report report){
        this.patientId = report.getPatientId();
        this.timeStamp = report.getTimeStamp(secretKey);
        this.speciality = report.getSpeciality(secretKey);
        this.description = report.getDescription(secretKey);
    }

    public RecordView(SecretKey secretKey, Medication medication){
        this.patientId = medication.getPatientId();
        this.timeStamp = medication.getTimeStamp(secretKey);
        this.speciality = medication.getSpeciality(secretKey);
        this.description = medication.getDescription(secretKey);
    }

    public RecordView(SecretKey secretKey, GenericInformation generic){
        this.patientId = generic.getPatientId();
        this.timeStamp = generic.getTimeStamp(secretKey);
        this.speciality = generic.getSpeciality(secretKey);
        this.description = generic.getDescription(secretKey);
    }

    public RecordView(SecretKey secretKey, Exam exam){
        this.patientId = exam.getPatientId();
        this.timeStamp = exam.getTimeStamp(secretKey);
        this.speciality = exam.getSpeciality(secretKey);
        this.description = exam.getDescription(secretKey);
    }

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
