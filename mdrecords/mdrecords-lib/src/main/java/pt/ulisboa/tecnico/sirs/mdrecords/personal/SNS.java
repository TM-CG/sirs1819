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

    @Atomic(mode = TxMode.WRITE)
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
    @Atomic(mode = TxMode.READ)
    public Doctor getDoctorById(long identification) {
        for (Doctor doctor : SNS.getInstance().getDoctorSet()) {
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
    @Atomic(mode = TxMode.READ)
    public Patient getPatientById(long identification) {
        for (Patient patient : SNS.getInstance().getPatientSet()) {
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
    @Atomic(mode = TxMode.READ)
    public Nurse getNurseById(long identification) {
        for (Nurse nurse : SNS.getInstance().getNurseSet()) {
            if (nurse.getIdentification() == identification)
                return nurse;
        }
        return null;
    }

    @Atomic(mode = TxMode.READ)
    public Administrative getAdministrativeById(long identification){
        for (Administrative administrative : SNS.getInstance().getAdministrativeSet()){
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
		for (Doctor doctor : SNS.getInstance().getDoctorSet()) {
			doctor.delete();
        }
        
        for (Patient patient : SNS.getInstance().getPatientSet()) {
			patient.delete();
        }
        
        for (Nurse nurse : SNS.getInstance().getNurseSet()) {
			nurse.delete();
		}

        for (Administrative administrative: SNS.getInstance().getAdministrativeSet()) {
            administrative.delete();
        }

	}

	public static String encrypt(SecretKey serverKey, String data) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException,
            UnsupportedEncodingException, InvalidAlgorithmParameterException {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

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


        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, serverKey, ivspec);
        String data = new String(cipher.doFinal(encrypted));

        return data;
    }

    }

