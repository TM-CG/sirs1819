package pt.ulisboa.tecnico.sirs.mdrecords;

import org.joda.time.DateTime;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.*;
import pt.ist.fenixframework.FenixFramework;


import javax.crypto.SecretKey;
import java.io.IOException;

public class RequestHelper {

    /*******************************************REQUEST INFORMATION ***************************************************/

    public static RecordView requestInformation(SecretKey secretKey, String requestObject,
                                                String myType, long myId, long requestWhomId) throws IOException {
        SNS sns = FenixFramework.getDomainRoot().getSns();
        if(myType.equals("Doctor")){
            Doctor myself = sns.getDoctorById(myId);
            Patient patient = sns.getPatientById(requestWhomId);

            if(XACMLHelper.checkPersonPermission("Doctor",requestObject,"read") ||
                    XACMLHelper.checkPersonPermission("Doctor", requestObject, "read",
                            checkFollowingStatus(myself, patient))){
                return patient.getRecord(secretKey, requestObject);
            }
        }
        else if(myType.equals("Nurse")){
            Nurse myself = sns.getNurseById(myId);
            Patient patient = sns.getPatientById(requestWhomId);

            if(XACMLHelper.checkPersonPermission("Nurse",requestObject,"read") ||
                    XACMLHelper.checkPersonPermission("Nurse", requestObject, "read",
                            checkFollowingStatus(myself, patient))){
                return patient.getRecord(secretKey, requestObject);
            }
        }
        else if(myType.equals("Patient")){
            Patient myself = sns.getPatientById(myId);
            Patient patient = sns.getPatientById(requestWhomId);

            if(XACMLHelper.checkPersonPermission("Patient",requestObject,"read") ||
                    XACMLHelper.checkPersonPermission("Patient", requestObject, "read",
                            checkFollowingStatus(myself, patient))){
                return patient.getRecord(secretKey, requestObject);
            }
        }
        else if(myType.equals("Administrative")) {
            Administrative myself = sns.getAdministrativeById(myId);
            Patient patient = sns.getPatientById(requestWhomId);

            if (XACMLHelper.checkPersonPermission("Administrative", requestObject, "read"))
                return patient.getRecord(secretKey, requestObject);
        }

        return null;
    }

    /****************************************** ADD METHODS ***********************************************************/
    public static String addReport(SecretKey secretKey, String myType, long personalId, long patientId, String speciality, String description) throws IOException{
        SNS sns = FenixFramework.getDomainRoot().getSns();

        if(myType.equals("Doctor")){
            Doctor myself = sns.getDoctorById(personalId);
            Patient patient = sns.getPatientById(patientId);

            if(XACMLHelper.checkPersonPermission("Doctor","Report","write") ||
                    XACMLHelper.checkPersonPermission("Doctor", "Report", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addReport(secretKey, personalId, speciality, description);
                return "Operation successful";
            }
        }
        else if(myType.equals("Nurse")){
            Nurse myself = sns.getNurseById(personalId);
            Patient patient = sns.getPatientById(patientId);

            if(XACMLHelper.checkPersonPermission("Nurse","Report","write") ||
                    XACMLHelper.checkPersonPermission("Nurse", "Report", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addReport(secretKey, personalId, speciality, description);
                return "Operation successful";
            }
        }
        else if(myType.equals("Patient")){
            Patient myself = sns.getPatientById(personalId);
            Patient patient = sns.getPatientById(patientId);

            if(XACMLHelper.checkPersonPermission("Patient","Report","write") ||
                    XACMLHelper.checkPersonPermission("Patient", "Report", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addReport(secretKey, personalId, speciality, description);
                return "Operation successful";
            }
         }
        else if(myType.equals("Administrative")) {
            Administrative myself = sns.getAdministrativeById(personalId);
            Patient patient = sns.getPatientById(patientId);

            if (XACMLHelper.checkPersonPermission("Administrative", "Report", "write")) {
                patient.addReport(secretKey, personalId, speciality, description);
                return "Operation successful";
            }
        }
        return "Operation unsuccessful";
    }

    public static String addMedication(SecretKey secretKey, String myType, long personalId, long patientId, String speciality, String description, String drug, float usage) throws IOException{
        SNS sns = FenixFramework.getDomainRoot().getSns();

        if(myType.equals("Doctor")){
            Doctor myself = sns.getDoctorById(personalId);
            Patient patient = sns.getPatientById(patientId);

            if(XACMLHelper.checkPersonPermission("Doctor","Medication","write") ||
                    XACMLHelper.checkPersonPermission("Doctor", "Medication", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addMedication(secretKey, personalId, speciality, description, drug, usage);
                return "Operation successful";
            }
        }
        else if(myType.equals("Nurse")){
            Nurse myself = sns.getNurseById(personalId);
            Patient patient = sns.getPatientById(patientId);

            if(XACMLHelper.checkPersonPermission("Nurse","Medication","write") ||
                    XACMLHelper.checkPersonPermission("Nurse", "Medication", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addMedication(secretKey, personalId, speciality, description, drug, usage);
                return "Operation successful";
            }
        }
        else if(myType.equals("Patient")){
            Patient myself = sns.getPatientById(personalId);
            Patient patient = sns.getPatientById(patientId);

            if(XACMLHelper.checkPersonPermission("Patient","Medication","write") ||
                    XACMLHelper.checkPersonPermission("Patient", "Medication", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addMedication(secretKey, personalId, speciality, description, drug, usage);
                return "Operation successful";
            }
        }
        else if(myType.equals("Administrative")) {
            Administrative myself = sns.getAdministrativeById(personalId);
            Patient patient = sns.getPatientById(patientId);

            if (XACMLHelper.checkPersonPermission("Administrative", "Medication", "write")) {
                patient.addMedication(secretKey, personalId, speciality, description, drug, usage);
                return "Operation successful";
            }
        }

        return "Operation unsuccessful";
    }

    public static String addGeneric(SecretKey secretKey, String myType, long personalId, long patientId, String speciality, String description) throws IOException{
        SNS sns = FenixFramework.getDomainRoot().getSns();

        if(myType.equals("Doctor")){
            Doctor myself = sns.getDoctorById(personalId);
            Patient patient = sns.getPatientById(patientId);

            if(XACMLHelper.checkPersonPermission("Doctor","Generic","write") ||
                    XACMLHelper.checkPersonPermission("Doctor", "Generic", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addGeneric(secretKey, personalId, speciality, description);
                return "Operation successful";
            }
        }
        else if(myType.equals("Nurse")){
            Nurse myself = sns.getNurseById(personalId);
            Patient patient = sns.getPatientById(patientId);

            if(XACMLHelper.checkPersonPermission("Nurse","Generic","write") ||
                    XACMLHelper.checkPersonPermission("Nurse", "Generic", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addGeneric(secretKey, personalId, speciality, description);
                return "Operation successful";
            }
        }
        else if(myType.equals("Patient")){
            Patient myself = sns.getPatientById(personalId);
            Patient patient = sns.getPatientById(patientId);

            if(XACMLHelper.checkPersonPermission("Patient","Generic","write") ||
                    XACMLHelper.checkPersonPermission("Patient", "Generic", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addGeneric(secretKey, personalId, speciality, description);
                return "Operation successful";
            }
        }
        else if(myType.equals("Administrative")) {
            Administrative myself = sns.getAdministrativeById(personalId);
            Patient patient = sns.getPatientById(patientId);

            if (XACMLHelper.checkPersonPermission("Administrative", "Generic", "write")) {
                patient.addGeneric(secretKey, personalId, speciality, description);
                return "Operation successful";
            }
        }

        return "Operation unsuccessful";
    }

    public static String addExam(SecretKey secretKey, String myType, long personalId, long patientId, String speciality, String description, String examName) throws IOException{
        SNS sns = FenixFramework.getDomainRoot().getSns();

        if(myType.equals("Doctor")){
            Doctor myself = sns.getDoctorById(personalId);
            Patient patient = sns.getPatientById(patientId);

            if(XACMLHelper.checkPersonPermission("Doctor","Exam","write") ||
                    XACMLHelper.checkPersonPermission("Doctor", "Exam", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addExam(secretKey, personalId, speciality, description, examName);
                return "Operation successful";
            }
        }
        else if(myType.equals("Nurse")){
            Nurse myself = sns.getNurseById(personalId);
            Patient patient = sns.getPatientById(patientId);

            if(XACMLHelper.checkPersonPermission("Nurse","Exam","write") ||
                    XACMLHelper.checkPersonPermission("Nurse", "Exam", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addExam(secretKey, personalId, speciality, description, examName);
                return "Operation successful";
            }
        }
        else if(myType.equals("Patient")){
            Patient myself = sns.getPatientById(personalId);
            Patient patient = sns.getPatientById(patientId);

            if(XACMLHelper.checkPersonPermission("Patient","Exam","write") ||
                    XACMLHelper.checkPersonPermission("Patient", "Exam", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addExam(secretKey, personalId, speciality, description, examName);
                return "Operation successful";
            }
        }
        else if(myType.equals("Administrative")) {
            Administrative myself = sns.getAdministrativeById(personalId);
            Patient patient = sns.getPatientById(patientId);

            if (XACMLHelper.checkPersonPermission("Administrative", "Exam", "write")) {
                patient.addExam(secretKey, personalId, speciality, description, examName);
                return "Operation successful";
            }
        }

        return "Operation unsuccessful";
    }


    /******************************************* CHECK FOLLOWING STATUS ************************************************/

    private static String checkFollowingStatus(Doctor doc, Patient patient){
        if(patient.getDoctorSet().contains(doc)){
            return "true";
        }
        else{
            return "false";
        }
    }

    private static String checkFollowingStatus(Nurse nurse, Patient patient){
        if(patient.getNurseSet().contains(nurse)){
            return "true";
        }
        else{
            return "false";
        }
    }

    private static String checkFollowingStatus(Patient myself, Patient patient){
        if(myself.getIdentification() == patient.getIdentification()){
            return "true";
        }
        else{
            return "false";
        }
    }



    /******************************************* ADD FOLLOWING RELATION ***********************************************/

    public static void addFollowingRelation(String myType, long myId, long patientId){
        SNS sns = FenixFramework.getDomainRoot().getSns();

        if(myType.equals("Doctor")){
            Doctor doc = sns.getDoctorById(myId);
            Patient patient = sns.getPatientById(patientId);
            addFolowingRelation(doc, patient);
        }
        else if(myType.equals("Nurse")){
            Nurse nurse = sns.getNurseById(myId);
            Patient patient = sns.getPatientById(patientId);
            addFolowingRelation(nurse, patient);
        }
    }

    private static void addFolowingRelation(Doctor doc, Patient patient){
            patient.addDoctor(doc);
    }

    private static void addFolowingRelation(Nurse nurse, Patient patient){
        patient.addNurse(nurse);
    }


    /***********************************************CREATORS***********************************************************/
    public static void createIdentity(String type, SecretKey secretKey, String name, DateTime birthday, long identification){
        if (type.equals("Doctor")){
            createDoctor(secretKey, name, birthday, identification);
        }
        else if(type.equals("Nurse")){
            createNurse(secretKey, name, birthday, identification);
        }
        else if(type.equals("Patient")){
            createPatient(secretKey, name, birthday, identification);
        }
        else if(type.equals("Administrative")){
            createAdministrative(secretKey, name, birthday, identification);
        }
    }

    private static void createDoctor(SecretKey secretKey, String name, DateTime birthday, long identification){
        try{
            new Doctor(secretKey, name, birthday, identification);
        }catch(InvalidPersonException e){
            System.out.println("Doctor information invalid.");
            System.out.println(e.getMessage());
        }
    }

    private static void createNurse(SecretKey secretKey, String name, DateTime birthday, long identification){
        try{
            new Nurse(secretKey, name, birthday, identification);
        }catch(InvalidPersonException e){
            System.out.println("Nurse information invalid.");
            System.out.println(e.getMessage());
        }
    }

    private static void createPatient(SecretKey secretKey, String name, DateTime birthday, long identification){
        try{
            new Patient(secretKey, name, birthday, identification);
        }catch(InvalidPersonException e){
            System.out.println("Patient Information invalid.");
            System.out.println(e.getMessage());
        }
    }

    private static void createAdministrative(SecretKey secretKey, String name, DateTime birthday, long identification){
        try{
            new Patient(secretKey, name, birthday, identification);
        }catch(InvalidPersonException e){
            System.out.println("Patient Information invalid.");
            System.out.println(e.getMessage());
        }
    }


}
