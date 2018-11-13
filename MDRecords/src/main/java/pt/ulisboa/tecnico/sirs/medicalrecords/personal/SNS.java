package pt.ulisboa.tecnico.sirs.medicalrecords.personal;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

/** Singleton Class for describing the SNS */
public class SNS extends SNS_Base {
    
    private SNS() {
        setRoot(FenixFramework.getDomainRoot());
    }

    public static SNS getInstance() {
        if (FenixFramework.getDomainRoot().getSNS() == null) {
			return createSNS();
		}
		return FenixFramework.getDomainRoot().getSNS();
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

	}
}
