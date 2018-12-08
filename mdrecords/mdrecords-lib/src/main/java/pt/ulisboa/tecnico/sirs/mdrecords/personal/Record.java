package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;

/**
 * A class for describing changes made by the staff on client data
 */
public class Record extends Record_Base {
    
    public Record() {
        super();
    }

    public Record(long personalId, long patientId, DateTime timeStamp, String speciality, String description) {
        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(timeStamp);
        setSpeciality(speciality);
        setDescription(description);

    }
    
}
