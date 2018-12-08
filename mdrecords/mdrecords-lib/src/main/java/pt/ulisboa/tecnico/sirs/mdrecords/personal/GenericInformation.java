package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;

/**
 * A class for describing Generic Medical Information of patients (e.g. allergies)
 */
public class GenericInformation extends GenericInformation_Base {
    
    public GenericInformation() {
        super();
    }

    public GenericInformation(long personalId, long patientId, DateTime timeStamp, String speciality, String description) throws InvalidRecordException {
        checkArguments(personalId, patientId, timeStamp, speciality, description);

        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(timeStamp);
        setSpeciality(speciality);
        setDescription(description);
    }
    
}
