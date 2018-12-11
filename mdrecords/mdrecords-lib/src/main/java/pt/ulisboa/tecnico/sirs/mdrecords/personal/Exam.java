package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;
import pt.ist.fenixframework.FenixFramework;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * A class for describing patient exams data.
 */
public class Exam extends Exam_Base {
    
    public Exam() {
        super();
    }

    public Exam(SecretKey serverKey, long personalId, long patientId, DateTime timeStamp, String speciality, String description, String digest) throws InvalidRecordException {
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

    public Exam(SecretKey serverKey, long personalId, long patientId, DateTime timeStamp, String speciality, String description,
                String digest, String examName) throws InvalidRecordException {
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

        setExamName(serverKey, examName);

        FenixFramework.getDomainRoot().getSns().addRecord(this);

    }

    /**
     * Secure setter for examName
     * @param serverKey
     * @param examName
     */
    public void setExamName(SecretKey serverKey, String examName) {
        try {
            String encryptedExamName = SNS.encrypt(serverKey, examName);
            super.setExamName(encryptedExamName);

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
     * Secure getter for examName
     * @param serverKey
     * @return
     */
    public String getExamName(SecretKey serverKey) {
        String encryptedExamName = super.getExamName();
        try {
            return SNS.decrypt(serverKey, encryptedExamName);
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

    @Override
    public ExamView getView(SecretKey serverKey) {
        return new ExamView(getPersonalId(), getPatientId(), getTimeStamp(serverKey), getSpeciality(serverKey), getDescription(serverKey), getExamName(serverKey));
    }

    @Override
    public String toString(SecretKey serverKey) {
        String res = "<Medication ";

        res += getPersonalId() + ", ";
        res += getPatientId() + ", ";
        res += getTimeStamp(serverKey) + ", ";
        res += "\"" + getSpeciality(serverKey) + "\", ";
        res += "\"" + getDescription(serverKey) + "\"";
        res += "\"" + getExamName(serverKey) + "\"";

        res = ">";
        return res;
    }
}
