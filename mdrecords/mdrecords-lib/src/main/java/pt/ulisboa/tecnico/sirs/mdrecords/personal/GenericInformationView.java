package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;

public class GenericInformationView extends RecordView {

    public GenericInformationView() {super();}

    public GenericInformationView(long personalId, long patientId, DateTime timeStamp, String speciality, String description) {
        super(personalId, patientId, timeStamp, speciality, description);
    }
}