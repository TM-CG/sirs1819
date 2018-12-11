package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;
import pt.ist.fenixframework.FenixFramework;

import javax.crypto.SecretKey;

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

        setDrugName(drugName);
        setDosage(dosage);

        FenixFramework.getDomainRoot().getSns().addRecord(this);
    }

    @Override
    public MedicationView getView(SecretKey serverKey) {
        return new MedicationView(getPersonalId(), getPatientId(), getTimeStamp(serverKey), getSpeciality(serverKey), getDescription(serverKey), getDrugName(), getDosage());
    }
    
}
