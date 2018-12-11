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
 * A class for describing Medication of patients.
 */
public class Medication extends Medication_Base {
    
    public Medication() {
        super();
    }

    public Medication(SecretKey serverKey, long personalId, long patientId, DateTime timeStamp, String speciality, String description) throws InvalidRecordException {
        checkArguments(personalId, patientId, timeStamp, speciality, description);

        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(serverKey, timeStamp);
        setSpeciality(serverKey, speciality);
        setDescription(serverKey, description);

        FenixFramework.getDomainRoot().getSns().addRecord(this);

    }

    public Medication(SecretKey serverKey, long personalId, long patientId, DateTime timeStamp, String speciality, String description,
                      String drugName, float dosage) throws InvalidRecordException {
        checkArguments(personalId, patientId, timeStamp, speciality, description);

        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(serverKey, timeStamp);
        setSpeciality(serverKey, speciality);
        setDescription(serverKey, description);

        setDrugName(serverKey, drugName);
        setDosage(serverKey, dosage);

        FenixFramework.getDomainRoot().getSns().addRecord(this);
    }

    /**
     * Secure setter for medication drug name
     * @param serverKey
     * @param drugName
     */
    public void setDrugName(SecretKey serverKey, String drugName) {
        try {
            String encryptedDrugName = SNS.encrypt(serverKey, drugName);
            super.setDrugName(encryptedDrugName);

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
     * Secure getter for medication drug name
     * @param serverKey
     * @return
     */
    public String getDrugName(SecretKey serverKey) {
        String drugName = super.getDrugName();
        try {
            return SNS.decrypt(serverKey, drugName);
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
     * Secure setter for medication dosage
     * @param serverKey
     * @param dosage
     */
    public void setDosage(SecretKey serverKey, float dosage) {
        try {
            Float val = new Float(dosage);
            String encryptedDosage = SNS.encrypt(serverKey, val.toString());
            super.setDosage(encryptedDosage);

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
     * Secure getter for medication dosage
     * @param serverKey
     * @return
     */
    public float getDosage(SecretKey serverKey) {
        String encryptedDosage = super.getDosage();
        try {
            String dosage = SNS.decrypt(serverKey, encryptedDosage);
            return Float.parseFloat(dosage);
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
        return 0;
    }

    @Override
    public MedicationView getView(SecretKey serverKey) {
        return new MedicationView(getPersonalId(), getPatientId(), getTimeStamp(serverKey), getSpeciality(serverKey), getDescription(serverKey), getDrugName(serverKey), getDosage(serverKey));
    }
    
}
