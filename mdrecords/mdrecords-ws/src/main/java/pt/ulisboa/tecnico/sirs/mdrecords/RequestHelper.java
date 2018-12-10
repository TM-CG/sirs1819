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
                            checkFollowingStatus(myId, requestWhomId))){
                return patient.getRecord(requestObject);
            }
        }
        else if(myType.equals("Nurse")){
            Nurse myself = sns.getNurseById(myId);
            Patient patient = sns.getPatientById(requestWhomId);

            if(XACMLHelper.checkPersonPermission("Nurse",requestObject,requestType) ||
                    XACMLHelper.checkPersonPermission("Nurse", requestObject, requestType,
                            checkFollowingStatus(myId, requestWhomId))){
                return patient.getRecord(requestObject);
            }
        }
        else if(myType.equals("Patient")){
            Patient myself = sns.getPatientById(myId);
            Patient patient = sns.getPatientById(requestWhomId);

            if(XACMLHelper.checkPersonPermission("Patient",requestObject,requestType) ||
                    XACMLHelper.checkPersonPermission("Patient", requestObject, requestType,
                            checkFollowingStatus(myId, requestWhomId))){
                return patient.getRecord(requestObject);
            }
        }
        /*else if(myType.equals("Administrative")){
            Administrative myself = sns.getPatientById(myId);
            Patient patient = sns.getPatientById(requestWhomId);

            if(XACMLHelper.checkPersonPermission("Administrative",requestObject,requestType) ||
                    XACMLHelper.checkPersonPermission("Administrative", requestObject, requestType,
                            checkFollowingStatus(myId, requestWhomId))){
                return patient.getRecord(requestObject);
            }
        }*/
        return null;
    }

    /******************************************* CHECK FOLOWING STATUS ************************************************/


    /******************************************* ADD FOLOWING RELATION ************************************************/




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
        /*try{
            new Patient(secretKey, name, birthday, identification);
        }catch(InvalidPersonException e){
            System.out.println("Patient Information invalid.");
            System.out.println(e.getMessage());
        }*/
    }


}
