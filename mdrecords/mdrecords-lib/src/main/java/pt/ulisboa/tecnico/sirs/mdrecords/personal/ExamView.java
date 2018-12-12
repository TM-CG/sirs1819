package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;

import javax.crypto.SecretKey;

public class ExamView extends RecordView {

    public String examName;

    public ExamView() {super();}

    public ExamView(long personalId, long patientId, DateTime timeStamp, String speciality, String description) {
        super(personalId, patientId, timeStamp, speciality, description);
    }

    public ExamView(long personalId, long patientId, DateTime timeStamp, String speciality, String description, String examName) {
        super(personalId, patientId, timeStamp, speciality, description);

        this.examName = examName;
    }

    @Override
    public String toString() {
        String res = "<Exam ";

        res += this.personalId + ", ";
        res += this.patientId + ", ";
        res += this.timeStamp + ", ";
        res += "\"" + this.speciality + "\", ";
        res += "\"" + this.description + "\", ";
        res += "\"" + this.examName + "\"";

        res += ">";
        return res;
    }
}