package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import javax.crypto.SecretKey;

import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;

public class Patient extends Patient_Base {
    

    public Patient(SecretKey serverKey, String name, DateTime birthday, long identification) throws InvalidPersonException{
        super.checkArguments(name, birthday, identification);
        PatientAlreadyExists(identification);

        setName(serverKey, name);
        setBirthday(serverKey, birthday);
        setIdentification(identification);

        FenixFramework.getDomainRoot().getSns().addPatient(this);
    }

    public Patient(SecretKey serverKey, String name, long identification) throws InvalidPersonException{
        super.checkArguments(name, identification);
        PatientAlreadyExists(identification);

        setName(serverKey, name);
        setIdentification(identification);

        FenixFramework.getDomainRoot().getSns().addPatient(this);
    }
        
    /**
     * Checks if a Patient already exists.
     * @param identification of the Patient
     * @throws InvalidPersonException if the such Patient exists with same id on the system.
     */
    private void PatientAlreadyExists(long identification) throws InvalidPersonException {
        for (Patient Patient: FenixFramework.getDomainRoot().getSns().getPatientSet()) {
            if (Patient.getIdentification() == identification)
                throw new InvalidPersonException("Patient: This patient already exists!");
        }
    }

    public void addRecord(SecretKey secretKey, String type, long personalId, String speciality, String decription){
        if(type.equals("Report")){
            try{
                this.setReport(new Report(secretKey, personalId, this.getIdentification(), new DateTime(), speciality, decription));
            }catch (InvalidRecordException e){
                System.out.println("Invalid data on Records");
                System.out.println(e.getMessage());
            }
        }
        else if(type.equals("Medication")){
        }
        else if(type.equals("Generic")){}
        else if (type.equals("Exam")){}

    }

    public RecordView getRecord(SecretKey serverKey, String recordType){
        if(recordType.equals("Report")){
            return new RecordView(serverKey, this.getReport());
        }
        else if(recordType.equals("Medication")){
            return new RecordView(serverKey, this.getMedication());
        }
        else if(recordType.equals("Generic")){
            return new RecordView(serverKey, this.getGenericinformation());
        }
        else if(recordType.equals("Exam")){
            return new RecordView(serverKey, this.getExam());
        }
        return null;
    }

    /** Deletes Patient from the SNS */
    public void delete() {
        setSns(null);
		deleteDomainObject();
	}


	/***DECRYPTORS***/

}

