package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;
import pt.ist.fenixframework.FenixFramework;

/**
 * A class for describing Medication of patients.
 */
public class Medication extends Medication_Base {
    
    public Medication() {
        super();
    }

    public Medication(long personalId, long patientId, DateTime timeStamp, String speciality, String description) throws InvalidRecordException {
        checkArguments(personalId, patientId, timeStamp, speciality, description);

        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(timeStamp);
        setSpeciality(speciality);
        setDescription(description);

        FenixFramework.getDomainRoot().getSns().addRecord(this);
    }

    public Medication(long personalId, long patientId, DateTime timeStamp, String speciality, String description,
                      String drugName, float dosage) throws InvalidRecordException {
        checkArguments(personalId, patientId, timeStamp, speciality, description);

        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(timeStamp);
        setSpeciality(speciality);
        setDescription(description);

        setDrugName(drugName);
        setDosage(dosage);

        FenixFramework.getDomainRoot().getSns().addRecord(this);
    }
    
}
