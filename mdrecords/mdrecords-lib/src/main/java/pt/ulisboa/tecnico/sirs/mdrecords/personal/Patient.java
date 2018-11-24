package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import javax.crypto.SecretKey;

import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.exception.InvalidPersonException;

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

    /** Deletes Patient from the SNS */
    public void delete() {
        setSns(null);
		deleteDomainObject();
	}
}

