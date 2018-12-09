package pt.ulisboa.tecnico.sirs.mdrecords.personal;
import pt.ist.fenixframework.FenixFramework;

import org.joda.time.DateTime;

/**
 * A class for describing changes made by the staff on client data
 */
public class Record extends Record_Base {
    
    public Record() {
        super();
    }

    public Record(long personalId, long patientId, DateTime timeStamp, String speciality, String description) throws InvalidRecordException {
        checkArguments(personalId, patientId, timeStamp, speciality, description);

        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(timeStamp);
        setSpeciality(speciality);
        setDescription(description);

        FenixFramework.getDomainRoot().getSns().addRecord(this);

    }

    protected void checkArguments(long personalId, long patientId, DateTime timeStamp, String speciality, String description)
    throws InvalidRecordException {
        if (personalId < 0)
            throw new InvalidRecordException("Invalid Personal ID in Record!");
        if (patientId < 0)
            throw new InvalidRecordException("Invalid Patient ID in Record!");
        if (timeStamp.isAfterNow())
            throw new InvalidRecordException("Invalid timeStamp ID in Record, it is from future!");
    }
    
}
