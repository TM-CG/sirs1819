package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;

public class MedicationView extends RecordView {

    protected String drugName;
    protected float dosage;

    public MedicationView() {super();}

    public MedicationView(long personalId, long patientId, DateTime timeStamp, String speciality, String description) {
        super(personalId, patientId, timeStamp, speciality, description);
    }

    public MedicationView(long personalId, long patientId, DateTime timeStamp, String speciality, String description, String drugName, float dosage) {
        super(personalId, patientId, timeStamp, speciality, description);

        this.drugName = drugName;
        this.dosage = dosage;
    }
}