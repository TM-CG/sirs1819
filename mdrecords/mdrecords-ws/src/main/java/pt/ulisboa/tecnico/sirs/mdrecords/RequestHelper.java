package pt.ulisboa.tecnico.sirs.mdrecords;

import org.joda.time.DateTime;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.*;
import pt.ist.fenixframework.FenixFramework;


import javax.crypto.SecretKey;
import java.io.IOException;

public class RequestHelper {

    /*******************************************REQUEST INFORMATION ***************************************************/

    public static RecordView requestInformation(SecretKey secretKey, String requestType, String requestObject,
                                                String myType, long myId, long requestWhomId) throws IOException {
        SNS sns = FenixFramework.getDomainRoot().getSns();
        if(myType.equals("Doctor")){
            Doctor myself = sns.getDoctorById(myId);
            Patient patient = sns.getPatientById(requestWhomId);

            if(XACMLHelper.checkPersonPermission("Doctor",requestObject,requestType) ||
                    XACMLHelper.checkPersonPermission("Doctor", requestObject, requestType,
                            checkFollowingStatus(myself, patient))){
                return patient.getRecord(requestObject);
            }
        }
        else if(myType.equals("Nurse")){
            Nurse myself = sns.getNurseById(myId);
            Patient patient = sns.getPatientById(requestWhomId);

            if(XACMLHelper.checkPersonPermission("Nurse",requestObject,requestType) ||
                    XACMLHelper.checkPersonPermission("Nurse", requestObject, requestType,
                            checkFollowingStatus(myself, patient))){
                return patient.getRecord(requestObject);
            }
        }
        else if(myType.equals("Patient")){
            Patient myself = sns.getPatientById(myId);
            Patient patient = sns.getPatientById(requestWhomId);

            if(XACMLHelper.checkPersonPermission("Patient",requestObject,requestType) ||
                    XACMLHelper.checkPersonPermission("Patient", requestObject, requestType,
                            checkFollowingStatus(myself, patient))){
                return patient.getRecord(requestObject);
            }
        }
        else if(myType.equals("Administrative")) {
            Administrative myself = sns.getAdministrativeById(myId);
            Patient patient = sns.getPatientById(requestWhomId);

            if (XACMLHelper.checkPersonPermission("Administrative", requestObject, requestType))
                return patient.getRecord(requestObject);
        }
        return null;
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
