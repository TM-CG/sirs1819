package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;

import javax.crypto.SecretKey;

public class MedicationView extends RecordView {

    public String drugName;
    public float dosage;

    public MedicationView() {super();}

    public MedicationView(long personalId, long patientId, DateTime timeStamp, String speciality, String description) {
        super(personalId, patientId, timeStamp, speciality, description);
    }

    public MedicationView(long personalId, long patientId, DateTime timeStamp, String speciality, String description, String drugName, float dosage) {
        super(personalId, patientId, timeStamp, speciality, description);

        this.drugName = drugName;
        this.dosage = dosage;
    }

    public String toString() {
        String res = "<Medication ";

        res += this.personalId + ", ";
        res += this.patientId + ", ";
        res += this.timeStamp + ", ";
        res += "\"" + this.speciality + "\", ";
        res += "\"" + this.description + "\"";
        res += "\"" + this.drugName + "\"";
        res += "\"" + this.dosage + "\"";

        res += ">";
        return res;
    }
}