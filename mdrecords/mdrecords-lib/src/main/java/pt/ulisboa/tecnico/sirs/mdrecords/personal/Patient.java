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

    public Patient(SecretKey serverKey, String name, DateTime birthday, long identification, String certificateFileName) throws InvalidPersonException{
        super.checkArguments(name, birthday, identification);
        PatientAlreadyExists(identification);

        setName(serverKey, name);
        setBirthday(serverKey, birthday);
        setIdentification(identification);
        setCertificateFileName(certificateFileName);

        FenixFramework.getDomainRoot().getSns().addPatient(this);
    }

    public Patient(SecretKey serverKey, String name, long identification) throws InvalidPersonException{
        super.checkArguments(name, identification);
        PatientAlreadyExists(identification);

        setName(serverKey, name);
        setIdentification(identification);

        FenixFramework.getDomainRoot().getSns().addPatient(this);
    }

    public Patient(SecretKey serverKey, String name, long identification, String certificateFileName) throws InvalidPersonException{
        super.checkArguments(name, identification);
        PatientAlreadyExists(identification);

        setName(serverKey, name);
        setIdentification(identification);
        setCertificateFileName(certificateFileName);

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

    public void addReport(SecretKey secretKey, long personalId, String speciality, String description, String digest) {
        try {
            this.addReport(new Report(secretKey, personalId, this.getIdentification(), new DateTime(), speciality, description, digest));
        } catch (InvalidRecordException e) {
            System.out.println("Invalid data on Records");
            System.out.println(e.getMessage());
        }
    }

    public void addMedication(SecretKey secretKey, long personalId, String speciality, String description, String digest, String drug, float usage) {
        try {
            this.addMedication(new Medication(secretKey, personalId, this.getIdentification(), new DateTime(), speciality, description, digest, drug, usage));
        } catch (InvalidRecordException e) {
            System.out.println("Invalid data on Medication");
            System.out.println(e.getMessage());
        }
    }

    public void addGeneric(SecretKey secretKey, long personalId, String speciality, String description, String digest) {
        try {
            this.addGenericinformation(new GenericInformation(secretKey, personalId, this.getIdentification(), new DateTime(), speciality, description, digest));
        } catch (InvalidRecordException e) {
            System.out.println("Invalid data on Generic Information");
            System.out.println(e.getMessage());
        }
    }

    public void addExam(SecretKey secretKey, long personalId, String speciality, String description, String digest, String examName){
        try {
            this.addExam(new Exam(secretKey, personalId, this.getIdentification(), new DateTime(), speciality, description, digest, examName));
        } catch (InvalidRecordException e) {
            System.out.println("Invalid data on Exam");
            System.out.println(e.getMessage());
        }
    }

    public RecordView getRecord(SecretKey serverKey, String recordType){
        if(recordType.equals("Report")){
            Report lastReport = null;
            DateTime lastTime = null;
            for(Report rep : this.getReportSet()){
                if(lastTime == null || rep.getTimeStamp(serverKey).isAfter(lastTime)){
                    lastReport = rep;
                    lastTime = lastReport.getTimeStamp(serverKey);
                }
            }
            if(lastReport == null){
                System.out.println("Patient has no Reports");
                return null;
            }
            return lastReport.getView(serverKey);
        }
        else if(recordType.equals("Medication")){
            Medication lastMedication = null;
            DateTime lastTime = null;
            for(Medication med: this.getMedicationSet()){
                if(lastTime == null || med.getTimeStamp(serverKey).isAfter(lastTime)){
                    lastMedication = med;
                    lastTime = lastMedication.getTimeStamp(serverKey);
                }
            }
            if(lastMedication == null){
                System.out.println("Patient has no Medication");
                return null;
            }
            return lastMedication.getView(serverKey);
        }
        else if(recordType.equals("Generic")){
            GenericInformation generic = null;
            DateTime lastTime = null;
            for(GenericInformation gen : this.getGenericinformationSet()){
                if(lastTime == null || gen.getTimeStamp(serverKey).isAfter(lastTime)){
                    generic = gen;
                    lastTime = generic.getTimeStamp(serverKey);
                }
            }
            if(generic == null){
                System.out.println("Patient has no Generic Information");
                return null;
            }
            return generic.getView(serverKey);
        }
        else if(recordType.equals("Exam")){
            Exam lastExam  = null;
            DateTime lastTime = null;
            for(Exam exam : this.getExamSet()){
                if(lastTime == null || exam.getTimeStamp(serverKey).isAfter(lastTime)){
                    lastExam = exam;
                    lastTime = lastExam.getTimeStamp(serverKey);
                }
            }
            if(lastExam == null){
                System.out.println("Patient has no exams");
                return null;
            }
            return lastExam.getView(serverKey);
        }
        return null;
    }

    /** Deletes Patient from the SNS */
    public void delete() {
        setSns(null);
		deleteDomainObject();
	}


}

