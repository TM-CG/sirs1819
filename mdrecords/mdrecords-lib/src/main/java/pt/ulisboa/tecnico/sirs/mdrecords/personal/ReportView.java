package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;

public class ReportView extends RecordView {

    public ReportView() {super();}

    public ReportView(long personalId, long patientId, DateTime timeStamp, String speciality, String description) {
        super(personalId, patientId, timeStamp, speciality, description);
    }
}
