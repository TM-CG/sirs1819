package pt.ulisboa.tecnico.sirs.mdrecords;

import org.joda.time.DateTime;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.*;
import pt.ist.fenixframework.FenixFramework;


import javax.crypto.SecretKey;
import java.io.IOException;

public class RequestHelper {

    /*******************************************REQUEST INFORMATION ***************************************************/

    public static RecordView requestInformation(SecretKey secretKey, String requestObject,
                                                String myType, long myId, long requestWhomId) throws IOException, BadRequestInformationException {

        SNS sns = FenixFramework.getDomainRoot().getSns();

        if(myType.equals("Doctor")){
            Doctor myself = sns.getDoctorById(myId);

            if(myself == null){
                throw new BadRequestInformationException("Doctor does not exist.");
            }

            Patient patient = sns.getPatientById(requestWhomId);
            if(patient == null){
                throw new BadRequestInformationException("Patient does not exist");
            }

            if(XACMLHelper.checkPersonPermission("Doctor",requestObject,"read") ||
                    XACMLHelper.checkPersonPermission("Doctor", requestObject, "read",
                            checkFollowingStatus(myself, patient))){
                return patient.getRecord(secretKey, requestObject);
            }
            else
                throw new BadRequestInformationException("Operation not authorized.");
        }
        else if(myType.equals("Nurse")){
            Nurse myself = sns.getNurseById(myId);
            if(myself == null){
                throw new BadRequestInformationException("Nurse does not exist.");
            }

            Patient patient = sns.getPatientById(requestWhomId);
            if(patient == null){
                throw new BadRequestInformationException("Patient does not exist");
            }

            if(XACMLHelper.checkPersonPermission("Nurse",requestObject,"read") ||
                    XACMLHelper.checkPersonPermission("Nurse", requestObject, "read",
                            checkFollowingStatus(myself, patient))){
                return patient.getRecord(secretKey, requestObject);
            }
            else
                throw new BadRequestInformationException("Operation not authorized.");
        }
        else if(myType.equals("Patient")){
            Patient myself = sns.getPatientById(myId);
            if(myself == null)
                throw new BadRequestInformationException("Patient does not exist");

            Patient patient = sns.getPatientById(requestWhomId);
            if(patient == null)
                throw new BadRequestInformationException("Patient does not exist");

            if(XACMLHelper.checkPersonPermission("Patient",requestObject,"read") ||
                    XACMLHelper.checkPersonPermission("Patient", requestObject, "read",
                            checkFollowingStatus(myself, patient))){
                return patient.getRecord(secretKey, requestObject);
            }
            else
                throw new BadRequestInformationException("Operation not authorized.");
        }
        else if(myType.equals("Administrative")) {
            Administrative myself = sns.getAdministrativeById(myId);
            Patient patient = sns.getPatientById(requestWhomId);
            if(patient == null){
                throw new BadRequestInformationException("Administrative does not exist");
            }

            if (XACMLHelper.checkPersonPermission("Administrative", requestObject, "read"))
                return patient.getRecord(secretKey, requestObject);
            else
                throw new BadRequestInformationException("Operation not authorized.");
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

    public static String addFollowingRelation(String myType, long myId, long patientId) throws BadAddRelationException{
        SNS sns = FenixFramework.getDomainRoot().getSns();

        if(myType.equals("Doctor")){
            Doctor doc = sns.getDoctorById(myId);
            Patient patient = sns.getPatientById(patientId);
            if(doc != null && patient != null){
                addFolowingRelation(doc, patient);
                return "Doctor: " + myId + " now follows patient: " + patient + ".";
            }
            else
                throw new BadAddRelationException("One of the subjects does not exist.");
        }
       else if(myType.equals("Nurse")){
            Nurse nurse = sns.getNurseById(myId);
            Patient patient = sns.getPatientById(patientId);
            if(nurse != null && patient != null){
                addFolowingRelation(nurse, patient);
                return "Doctor: " + myId + " now follows patient: " + patient + ".";
            }
            else
                throw new BadAddRelationException("One of the subjects does not exist.");
        }

        return null;
    }

    private static void addFolowingRelation(Doctor doc, Patient patient){
            patient.addDoctor(doc);
    }

    private static void addFolowingRelation(Nurse nurse, Patient patient){
        patient.addNurse(nurse);
    }


    /***********************************************CREATORS***********************************************************/
    public static String createIdentity(String type, SecretKey secretKey, String name, DateTime birthday, long identification) throws BadAddIdentityException{
        try{
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
        }catch (InvalidPersonException e){
            throw new BadAddIdentityException(e.getMessage());
        }
        return "Add opertation on " +  type + ": " + identification + "was sucesseful.";

    }

    private static void createDoctor(SecretKey secretKey, String name, DateTime birthday, long identification) throws InvalidPersonException{
        new Doctor(secretKey, name, birthday, identification);
    }

    private static void createNurse(SecretKey secretKey, String name, DateTime birthday, long identification) throws InvalidPersonException{
        new Nurse(secretKey, name, birthday, identification);
    }

    private static void createPatient(SecretKey secretKey, String name, DateTime birthday, long identification) throws InvalidPersonException{
        new Patient(secretKey, name, birthday, identification);

    }

    private static void createAdministrative(SecretKey secretKey, String name, DateTime birthday, long identification) throws InvalidPersonException{
        new Patient(secretKey, name, birthday, identification);
    }


}
