package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.exception.InvalidPersonException;

public class Patient extends Patient_Base {
    

    public Patient(String name, DateTime birthday, long identification) throws InvalidPersonException{
        super.checkArguments(name, birthday, identification);
        PatientAlreadyExists(identification);

        setName(name);
        setBirthday(birthday);
        setIdentification(identification);

        FenixFramework.getDomainRoot().getSNS().addPatient(this);
    }

    public Patient(String name, long identification) throws InvalidPersonException{
        super.checkArguments(name, identification);
        PatientAlreadyExists(identification);

        setName(name);
        setIdentification(identification);

        FenixFramework.getDomainRoot().getSNS().addPatient(this);
    }
        
    /**
     * Checks if a Patient already exists.
     * @param identification of the Patient
     * @throws InvalidPersonException if the such Patient exists with same id on the system.
     */
    private void PatientAlreadyExists(long identification) throws InvalidPersonException {
        for (Patient Patient: FenixFramework.getDomainRoot().getSNS().getPatientSet()) {
            if (Patient.getIdentification() == this.getIdentification())
                throw new InvalidPersonException("Patient: This patient already exists!");
        }
        return;
    }

    /** Deletes Patient from the SNS */
    public void delete() {
        setSNS(null);
		deleteDomainObject();
	}
}

