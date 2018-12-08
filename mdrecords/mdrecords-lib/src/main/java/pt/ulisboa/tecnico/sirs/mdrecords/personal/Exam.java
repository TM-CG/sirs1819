package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;

/**
 * A class for describing patient exams data.
 */
public class Exam extends Exam_Base {
    
    public Exam() {
        super();
    }

    public Exam(long personalId, long patientId, DateTime timeStamp, String speciality, String description) {
        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(timeStamp);
        setSpeciality(speciality);
        setDescription(description);

    }

    public Exam(long personalId, long patientId, DateTime timeStamp, String speciality, String description,
                String examName) {
        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(timeStamp);
        setSpeciality(speciality);
        setDescription(description);

        setExamName(examName);

    }
    
}
