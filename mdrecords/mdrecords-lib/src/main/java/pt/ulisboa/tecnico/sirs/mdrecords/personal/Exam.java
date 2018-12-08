package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;

/**
 * A class for describing patient exams data.
 */
public class Exam extends Exam_Base {
    
    public Exam() {
        super();
    }

    public Exam(long personalId, long patientId, DateTime timeStamp, String speciality, String description) throws InvalidRecordException {
        checkArguments(personalId, patientId, timeStamp, speciality, description);

        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(timeStamp);
        setSpeciality(speciality);
        setDescription(description);

    }

    public Exam(long personalId, long patientId, DateTime timeStamp, String speciality, String description,
                String examName) throws InvalidRecordException {
        checkArguments(personalId, patientId, timeStamp, speciality, description);

        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(timeStamp);
        setSpeciality(speciality);
        setDescription(description);

        setExamName(examName);

    }
    
}
