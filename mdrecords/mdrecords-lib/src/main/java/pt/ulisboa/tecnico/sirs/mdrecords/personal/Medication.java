package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;

/**
 * A class for describing Medication of patients.
 */
public class Medication extends Medication_Base {
    
    public Medication() {
        super();
    }

    public Medication(long personalId, long patientId, DateTime timeStamp, String speciality, String description) {
        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(timeStamp);
        setSpeciality(speciality);
        setDescription(description);
    }

    public Medication(long personalId, long patientId, DateTime timeStamp, String speciality, String description,
                      String drugName, float dosage) {
        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(timeStamp);
        setSpeciality(speciality);
        setDescription(description);

        setDrugName(drugName);
        setDosage(dosage);
    }
    
}
