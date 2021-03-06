package pt.ulisboa.tecnico.sirs.mdrecords;

import org.joda.time.DateTime;
import pt.ist.fenixframework.Atomic;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.*;


import javax.crypto.SecretKey;
import java.io.IOException;

public class RequestHelper {

    /*******************************************REQUEST INFORMATION ***************************************************/

    @Atomic(mode = Atomic.TxMode.READ)
    public static RecordView requestInformation(SecretKey secretKey, String requestObject,
                                                String myType, long myId, long requestWhomId) throws IOException, BadRequestInformationException {

        SNS sns = SNS.getInstance();

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

                try {
                    return patient.getRecord(secretKey, requestObject);
                } catch (InvalidRecordException e) {
                    throw new BadRequestInformationException(e.getMessage());
                }
            }
            else
                throw new BadRequestInformationException(myType + " with identification: " + myId + " does not have authorization to read patient: " + requestWhomId + " " +requestObject);
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
                try {
                    return patient.getRecord(secretKey, requestObject);
                } catch (InvalidRecordException e) {
                    throw new BadRequestInformationException(e.getMessage());
                }
            }
            else
                throw new BadRequestInformationException(myType + " with identification: " + myId + " does not have authorization to read patient: " + requestWhomId + " " +requestObject);
        }
        else if(myType.equals("Patient")){
            Patient myself = sns.getPatientById(myId);
            if(myself == null)
                throw new BadRequestInformationException("Patient does not exist");

            Patient patient = sns.getPatientById(requestWhomId);
            if(patient == null)
                throw new BadRequestInformationException("Patient does not exist");

            if(XACMLHelper.checkPersonPermission("Patient", requestObject, "read",
                            checkFollowingStatus(myself, patient))){
                try {
                    return patient.getRecord(secretKey, requestObject);
                } catch (InvalidRecordException e) {
                    throw new BadRequestInformationException(e.getMessage());
                }
            }
            else
                throw new BadRequestInformationException(myType + " with identification: " + myId + " does not have authorization to read patient: " + requestWhomId + " " +requestObject);
        }
        else if(myType.equals("Administrative")) {
            Administrative myself = sns.getAdministrativeById(myId);
            if(myself == null)
                throw new BadRequestInformationException("Administrative does not exist");
            Patient patient = sns.getPatientById(requestWhomId);
            if(patient == null){
                throw new BadRequestInformationException("Patient does not exist");
            }

            if (XACMLHelper.checkPersonPermission("Administrative", requestObject, "read"))
                try {
                    return patient.getRecord(secretKey, requestObject);
                } catch (InvalidRecordException e) {
                    throw new BadRequestInformationException(e.getMessage());
                }
            else
                throw new BadRequestInformationException(myType + " with identification: " + myId + " does not have authorization to read patient: " + requestWhomId + " " +requestObject);
        }

        return null;
    }

    /****************************************** ADD METHODS ***********************************************************/
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static String addReport(SecretKey secretKey, String myType, long personalId, long patientId, String speciality, String description, DateTime timeStamp, String digest) throws BadRecordException, IOException{
        SNS sns = SNS.getInstance();

        if(myType.equals("Doctor")){
            Doctor myself = sns.getDoctorById(personalId);
            if(myself == null)
                throw new BadRecordException("Doctor does not exist");

            Patient patient = sns.getPatientById(patientId);
            if(patient == null)
                throw new BadRecordException("Patient does not exist");

            if(XACMLHelper.checkPersonPermission("Doctor","Report","write") ||
                    XACMLHelper.checkPersonPermission("Doctor", "Report", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addReport(secretKey, personalId, speciality, description, timeStamp, digest);
                return "Operation successful";
            }
        }
        else if(myType.equals("Nurse")){
            Nurse myself = sns.getNurseById(personalId);
            if(myself == null)
                throw new BadRecordException("Nurse does not exist");

            Patient patient = sns.getPatientById(patientId);
            if(patient == null)
                throw new BadRecordException("Patient does not exist");

            if(XACMLHelper.checkPersonPermission("Nurse","Report","write") ||
                    XACMLHelper.checkPersonPermission("Nurse", "Report", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addReport(secretKey, personalId, speciality, description, timeStamp, digest);
                return "Operation successful";
            }
        }
        else if(myType.equals("Patient")){
            Patient myself = sns.getPatientById(personalId);
            if(myself == null)
                throw new BadRecordException("Myself does not exist");

            Patient patient = sns.getPatientById(patientId);
            if(patient == null)
                throw new BadRecordException("Patient does not exist");

            if(XACMLHelper.checkPersonPermission("Patient","Report","write") ||
                    XACMLHelper.checkPersonPermission("Patient", "Report", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addReport(secretKey, personalId, speciality, description, timeStamp, digest);
                return "Operation successful";
            }
         }
        else if(myType.equals("Administrative")) {
            Administrative myself = sns.getAdministrativeById(personalId);
            if(myself == null)
                throw new BadRecordException("Administrative does not exist");
            Patient patient = sns.getPatientById(patientId);
            if(patient == null)
                throw new BadRecordException("Patient does not exist");

            if (XACMLHelper.checkPersonPermission("Administrative", "Report", "write")) {
                patient.addReport(secretKey, personalId, speciality, description, timeStamp, digest);
                return "Operation successful";
            }
        }
        throw new BadRecordException(myType + " with identification: " + personalId + " does not have authorization to add a Report to patient: " + patientId);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static String addMedication(SecretKey secretKey, String myType, long personalId, long patientId, String speciality, String description, DateTime timeStamp, String digest, String drug, float usage) throws BadRecordException, IOException{
        SNS sns = SNS.getInstance();

        if(myType.equals("Doctor")){
            Doctor myself = sns.getDoctorById(personalId);
            if(myself == null)
                throw new BadRecordException("Doctor does not exist");

            Patient patient = sns.getPatientById(patientId);
            if(patient == null)
                throw new BadRecordException("Patient does not exist");

            if(XACMLHelper.checkPersonPermission("Doctor","Medication","write") ||
                    XACMLHelper.checkPersonPermission("Doctor", "Medication", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addMedication(secretKey, personalId, speciality, description, timeStamp, digest, drug, usage);
                return "Operation successful";
            }
        }
        else if(myType.equals("Nurse")){
            Nurse myself = sns.getNurseById(personalId);
            if(myself == null)
                throw new BadRecordException("Nurse does not exist");

            Patient patient = sns.getPatientById(patientId);
            if(patient == null)
                throw new BadRecordException("Patient does not exist");

            if(XACMLHelper.checkPersonPermission("Nurse","Medication","write") ||
                    XACMLHelper.checkPersonPermission("Nurse", "Medication", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addMedication(secretKey, personalId, speciality, description, timeStamp, digest, drug, usage);
                return "Operation successful";
            }
        }
        else if(myType.equals("Patient")){
            Patient myself = sns.getPatientById(personalId);
            if(myself == null)
                throw new BadRecordException("Myself does not exist");

            Patient patient = sns.getPatientById(patientId);
            if(patient == null)
                throw new BadRecordException("Patient does not exist");

            if(XACMLHelper.checkPersonPermission("Patient","Medication","write") ||
                    XACMLHelper.checkPersonPermission("Patient", "Medication", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addMedication(secretKey, personalId, speciality, description, timeStamp, digest, drug, usage);
                return "Operation successful";
            }
        }
        else if(myType.equals("Administrative")) {
            Administrative myself = sns.getAdministrativeById(personalId);
            if(myself == null)
                throw new BadRecordException("Administrative does not exist");

            Patient patient = sns.getPatientById(patientId);
            if(patient == null)
                throw new BadRecordException("Patient does not exist");

            if (XACMLHelper.checkPersonPermission("Administrative", "Medication", "write")) {
                patient.addMedication(secretKey, personalId, speciality, description, timeStamp, digest, drug, usage);
                return "Operation successful";
            }
        }

        throw new BadRecordException(myType + " with identification: " + personalId + " does not have authorization to add a Medication to patient: " + patientId);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static String addGeneric(SecretKey secretKey, String myType, long personalId, long patientId, String speciality, String description, DateTime timeStamp, String digest) throws BadRecordException, IOException{
        SNS sns = SNS.getInstance();

        if(myType.equals("Doctor")){
            Doctor myself = sns.getDoctorById(personalId);
            if(myself == null)
                throw new BadRecordException("Doctor does not exist");

            Patient patient = sns.getPatientById(patientId);
            if(patient == null)
                throw new BadRecordException("Patient does not exist");

            if(XACMLHelper.checkPersonPermission("Doctor","Generic","write") ||
                    XACMLHelper.checkPersonPermission("Doctor", "Generic", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addGeneric(secretKey, personalId, speciality, description, timeStamp, digest);
                return "Operation successful";
            }
        }
        else if(myType.equals("Nurse")){
            Nurse myself = sns.getNurseById(personalId);
            if(myself == null)
                throw new BadRecordException("Nurse does not exist");

            Patient patient = sns.getPatientById(patientId);
            if(patient == null)
                throw new BadRecordException("Patient does not exist");

            if(XACMLHelper.checkPersonPermission("Nurse","Generic","write") ||
                    XACMLHelper.checkPersonPermission("Nurse", "Generic", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addGeneric(secretKey, personalId, speciality, description, timeStamp, digest);
                return "Operation successful";
            }
        }
        else if(myType.equals("Patient")){
            Patient myself = sns.getPatientById(personalId);
            if(myself == null)
                throw new BadRecordException("Myself does not exist");

            Patient patient = sns.getPatientById(patientId);
            if(patient == null)
                throw new BadRecordException("Patient does not exist");

            if(XACMLHelper.checkPersonPermission("Patient","Generic","write") ||
                    XACMLHelper.checkPersonPermission("Patient", "Generic", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addGeneric(secretKey, personalId, speciality, description, timeStamp, digest);
                return "Operation successful";
            }
        }
        else if(myType.equals("Administrative")) {
            Administrative myself = sns.getAdministrativeById(personalId);
            if(myself == null)
                throw new BadRecordException("Administrative does not exist");

            Patient patient = sns.getPatientById(patientId);
            if(patient == null)
                throw new BadRecordException("Patient does not exist");

            if (XACMLHelper.checkPersonPermission("Administrative", "Generic", "write")) {
                patient.addGeneric(secretKey, personalId, speciality, description, timeStamp, digest);
                return "Operation successful";
            }
        }

        throw new BadRecordException(myType + " with identification: " + personalId + " does not have authorization to add Generic Informations to patient: " + patientId);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static String addExam(SecretKey secretKey, String myType, long personalId, long patientId, String speciality, String description, DateTime timeStamp, String digest, String examName) throws BadRecordException, IOException{
        SNS sns = SNS.getInstance();

        if(myType.equals("Doctor")){
            Doctor myself = sns.getDoctorById(personalId);
            if(myself == null)
                throw new BadRecordException("Doctor does not exist");

            Patient patient = sns.getPatientById(patientId);
            if(patient == null)
                throw new BadRecordException("Patient does not exist");

            if(XACMLHelper.checkPersonPermission("Doctor","Exam","write") ||
                    XACMLHelper.checkPersonPermission("Doctor", "Exam", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addExam(secretKey, personalId, speciality, description, timeStamp, digest, examName);
                return "Operation successful";
            }
        }
        else if(myType.equals("Nurse")){
            Nurse myself = sns.getNurseById(personalId);
            if(myself == null)
                throw new BadRecordException("Nurse does not exist");

            Patient patient = sns.getPatientById(patientId);
            if(patient == null)
                throw new BadRecordException("Patient does not exist");

            if(XACMLHelper.checkPersonPermission("Nurse","Exam","write") ||
                    XACMLHelper.checkPersonPermission("Nurse", "Exam", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addExam(secretKey, personalId, speciality, description, timeStamp, digest, examName);
                return "Operation successful";
            }
        }
        else if(myType.equals("Patient")){
            Patient myself = sns.getPatientById(personalId);
            if(myself == null)
                throw new BadRecordException("Myself does not exist");

            Patient patient = sns.getPatientById(patientId);
            if(patient == null)
                throw new BadRecordException("Patient does not exist");

            if(XACMLHelper.checkPersonPermission("Patient","Exam","write") ||
                    XACMLHelper.checkPersonPermission("Patient", "Exam", "write",
                            checkFollowingStatus(myself, patient))){
                patient.addExam(secretKey, personalId, speciality, description, timeStamp, digest, examName);
                return "Operation successful";
            }
        }
        else if(myType.equals("Administrative")) {
            Administrative myself = sns.getAdministrativeById(personalId);
            if(myself == null)
                throw new BadRecordException("Administrative does not exist");

            Patient patient = sns.getPatientById(patientId);
            if(patient == null)
                throw new BadRecordException("Patient does not exist");

            if (XACMLHelper.checkPersonPermission("Administrative", "Exam", "write")) {
                patient.addExam(secretKey, personalId, speciality, description, timeStamp, digest, examName);
                return "Operation successful";
            }
        }

        throw new BadRecordException(myType + " with identification: " + personalId + " does not have authorization to add Exams to patient: " + patientId);
    }


    /******************************************* CHECK FOLLOWING STATUS ************************************************/

    @Atomic(mode = Atomic.TxMode.READ)
    private static String checkFollowingStatus(Doctor doc, Patient patient){
        if(patient.getDoctorSet().contains(doc)){
            return "true";
        }
        else{
            return "false";
        }
    }

    @Atomic(mode = Atomic.TxMode.READ)
    private static String checkFollowingStatus(Nurse nurse, Patient patient){
        if(patient.getNurseSet().contains(nurse)){
            return "true";
        }
        else{
            return "false";
        }
    }

    @Atomic(mode = Atomic.TxMode.READ)
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
        SNS sns = SNS.getInstance();

        if(myType.equals("Doctor")){
            Doctor doc = sns.getDoctorById(myId);
            Patient patient = sns.getPatientById(patientId);
            if(doc != null && patient != null){
                addFolowingRelation(doc, patient);
                return "Doctor: " + myId + " now follows patient: " + patientId + ".";
            }
            else if (doc == null)
                throw new BadAddRelationException("Doctor does not exist.");
            else
                throw new BadAddRelationException("Patient does not exist.");

        }
       else if(myType.equals("Nurse")){
            Nurse nurse = sns.getNurseById(myId);
            Patient patient = sns.getPatientById(patientId);
            if(nurse != null && patient != null){
                addFolowingRelation(nurse, patient);
                return "Nurse: " + myId + " now follows patient: " + patientId + ".";
            }
            else if (nurse == null)
                throw new BadAddRelationException("Nurse does not exist.");
            else
                throw new BadAddRelationException("Patient does not exist.");
        }

        return null;
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    private static void addFolowingRelation(Doctor doc, Patient patient) throws BadAddRelationException{
        if(patient.getDoctorSet().contains(doc)){
            throw new BadAddRelationException("Doctor: " + doc.getIdentification() + " already follows Patient: " + patient.getIdentification() + ".");
        }
        patient.addDoctor(doc);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    private static void addFolowingRelation(Nurse nurse, Patient patient) throws BadAddRelationException{
        if(patient.getDoctorSet().contains(nurse)){
            throw new BadAddRelationException("Nurse: " + nurse.getIdentification() + " already follows Patient: " + patient.getIdentification() + ".");
        }patient.addNurse(nurse);
    }


    /******************************************* REMOVE FOLLOWING RELATION ***********************************************/
    public static String removeFollowingRelation(String myType, long myId, long patientId) throws BadRemoveRelationException{
        SNS sns = SNS.getInstance();

        if(myType.equals("Doctor")){
            Doctor doc = sns.getDoctorById(myId);
            Patient patient = sns.getPatientById(patientId);
            if(doc != null && patient != null){
                removeFolowingRelation(doc, patient);
                return "Doctor: " + myId + " now unfollows patient: " + patientId + ".";
            }
            else if (doc == null)
                throw new BadRemoveRelationException("Doctor does not exist.");
            else
                throw new BadRemoveRelationException("Patient does not exist.");

        }
        else if(myType.equals("Nurse")){
            Nurse nurse = sns.getNurseById(myId);
            Patient patient = sns.getPatientById(patientId);
            if(nurse != null && patient != null){
                removeFolowingRelation(nurse, patient);
                return "Nurse: " + myId + " now unfollows patient: " + patientId + ".";
            }
            else if (nurse == null)
                throw new BadRemoveRelationException("Nurse does not exist.");
            else
                throw new BadRemoveRelationException("Patient does not exist.");
        }

        return null;
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    private static void removeFolowingRelation(Doctor doc, Patient patient) throws BadRemoveRelationException{
        if(!patient.getDoctorSet().contains(doc)){
            throw new BadRemoveRelationException("Doctor: " + doc.getIdentification() + " does not follow Patient: " + patient.getIdentification() + ".");
        }
        patient.getDoctorSet().remove(doc);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    private static void removeFolowingRelation(Nurse nurse, Patient patient) throws BadRemoveRelationException{
        if(!patient.getNurseSet().contains(nurse)){
            throw new BadRemoveRelationException("Nurse: " + nurse.getIdentification() + " does not follow Patient: " + patient.getIdentification() + ".");
        }
        patient.getNurseSet().remove(nurse);
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
        return "Add opertation of " +  type + ": " + identification + " was sucesseful.";

    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    private static void createDoctor(SecretKey secretKey, String name, DateTime birthday, long identification) throws InvalidPersonException{
       new Doctor(secretKey, name, birthday, identification);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    private static void createNurse(SecretKey secretKey, String name, DateTime birthday, long identification) throws InvalidPersonException{
        new Nurse(secretKey, name, birthday, identification);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    private static void createPatient(SecretKey secretKey, String name, DateTime birthday, long identification) throws InvalidPersonException{
        new Patient(secretKey, name, birthday, identification);

    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    private static void createAdministrative(SecretKey secretKey, String name, DateTime birthday, long identification) throws InvalidPersonException{
        new Administrative(secretKey, name, birthday, identification);
    }


}
