package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;
import pt.ist.fenixframework.FenixFramework;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * A class for describing patient exams data.
 */
public class Exam extends Exam_Base {
    
    public Exam() {
        super();
    }

    public Exam(SecretKey serverKey, long personalId, long patientId, DateTime timeStamp, String speciality, String description) throws InvalidRecordException {
        checkArguments(personalId, patientId, timeStamp, speciality, description);

        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(serverKey, timeStamp);
        setSpeciality(serverKey, speciality);
        setDescription(serverKey, description);

        FenixFramework.getDomainRoot().getSns().addRecord(this);

    }

    public Exam(SecretKey serverKey, long personalId, long patientId, DateTime timeStamp, String speciality, String description,
                String examName) throws InvalidRecordException {
        checkArguments(personalId, patientId, timeStamp, speciality, description);

        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(serverKey, timeStamp);
        setSpeciality(serverKey, speciality);
        setDescription(serverKey, description);

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
    
}
