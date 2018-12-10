package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;


import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.util.Base64;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/** Singleton Class for describing the SNS */
public class SNS extends SNS_Base {

    private SNS() {
        setRoot(FenixFramework.getDomainRoot());
    }

    public static SNS getInstance() {
        if (FenixFramework.getDomainRoot().getSns() == null) {
			return createSNS();
		}
		return FenixFramework.getDomainRoot().getSns();
    }

    @Atomic(mode = TxMode.WRITE)
	private static SNS createSNS() {
		return new SNS();
	}
   
    /**
     * Returns a Doctor object from his identification number.
     * @param identification of the doctor
     * @return a object if doctor is found or null otherwise.
     */
    public Doctor getDoctorById(long identification) {
        for (Doctor doctor : getDoctorSet()) {
            if (doctor.getIdentification() == identification)
                return doctor;
        }
        return null;
    }

    /**
     * Returns a Patient object from his identification number.
     * @param identification of the patient
     * @return a object if patient is found or null otherwise.
     */
    public Patient getPatientById(long identification) {
        for (Patient patient : getPatientSet()) {
            if (patient.getIdentification() == identification)
                return patient;
        }
        return null;
    }
    /**
     * Returns a Nurse object from his identification number.
     * @param identification of the nurse
     * @return a object if nurse is found or null otherwise.
     */
    public Nurse getNurseById(long identification) {
        for (Nurse nurse : getNurseSet()) {
            if (nurse.getIdentification() == identification)
                return nurse;
        }
        return null;
    }

    public Administrative getAdministrativeById(long identification){
        for (Administrative administrative : getAdministrativeSet()){
            if(administrative.getIdentification() == identification)
                return administrative;
        }
        return null;
    }


    public void delete() {
		setRoot(null);

		clearAll();

		deleteDomainObject();
    }
    
    private void clearAll() {
		for (Doctor doctor : getDoctorSet()) {
			doctor.delete();
        }
        
        for (Patient patient : getPatientSet()) {
			patient.delete();
        }
        
        for (Nurse nurse : getNurseSet()) {
			nurse.delete();
		}

        for (Administrative administrative: getAdministrativeSet()) {
            administrative.delete();
        }

	}

	public static String encrypt(SecretKey serverKey, String data) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException,
            UnsupportedEncodingException, InvalidAlgorithmParameterException {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        //Check with Martinez
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, serverKey, ivspec);

        byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));

        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(SecretKey serverKey, String encryptedData) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException,
            InvalidAlgorithmParameterException {

        byte[] encrypted = Base64.getDecoder().decode(encryptedData);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        //Check with Martinez
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, serverKey, ivspec);
        String data = new String(cipher.doFinal(encrypted));

        return data;
    }

    public Record readRecord(SecretKey serverKey, Long personalId, Long patientId, String recordType){
        SNS sns = SNS.getInstance();
        Record newestRecord = null;
        boolean isFirst = true;
        //switch(recordType){

            if(recordType == "Record"){
                    if(sns.getDoctorById(personalId).getPatientSet().contains(sns.getPatientById(patientId))){
                        for (Record record : sns.getRecordSet()) {
                            

                            if(recordType.equals(record.getClass().getSimpleName()) && 
                            personalId.equals(record.getPersonalId()) && patientId.equals(record.getPatientId())){
                                if(isFirst){
                                    newestRecord = record;
                                    isFirst = false;
                                }
                                else {
                                    if(record.getTimeStamp(serverKey).isBefore(newestRecord.getTimeStamp(serverKey))){
                                        newestRecord = record;
                                    } 
                                }
                            }
                        }
                    }
                    //nurse case
                    else if(getNurseSet().contains(personalId)){
                        for (Record record: getRecordSet()){
                            if(recordType == record.getClass().getName() && personalId == record.getPersonalId()
                            && patientId == record.getPatientId()){
                                if(isFirst){
                                    newestRecord = record;
                                    isFirst = false;
                                }
                                else {
                                    if(record.getTimeStamp(serverKey).isBefore(newestRecord.getTimeStamp(serverKey))){
                                        newestRecord = record;
                                    }  
                                }
                            }
                            
                        }
                    }
                    //patient case
                    else if(patientId == personalId){
                        for (Record record: getRecordSet()){
                            if(record.getPatientId() == patientId){
                                if(isFirst){
                                    newestRecord = record;
                                    isFirst = false;
                                }
                                else {
                                    if(record.getTimeStamp(serverKey).isBefore(newestRecord.getTimeStamp(serverKey))){
                                        newestRecord = record;
                                    }  
                                }
                            }
                        }
                    }   
                    return newestRecord;
                
                
                
            }
            else {
                return null;
            }   
            
            //TODO if we have time
            /*case "genericInformation":
                switch(personalId){
                    //doctor case
                    case getDoctorSet().contains(patientId):
                        break;
                    //nurse case
                    case getNurseSet().contains(patientId):
                        break;
                    //patient case
                    case patientId == personalId:
                        break;
                }
                break;
            case "medication":
                switch(personalId){
                    //doctor case
                    case getDoctorSet().contains(patientId):
                        break;
                    //nurse case
                    case getNurseSet().contains(patientId):
                        break;
                    //patient case
                    case patientId == personalId:
                        break;
                }
                break;
            case "exam":
                switch(personalId){
                    //doctor case
                    case getDoctorSet().contains(patientId):
                        break;
                    //nurse case
                    case getNurseSet().contains(patientId):
                        break;
                    //patient case
                    case patientId == personalId:
                        break;
                }
                break;
            */
        }
        
    }

