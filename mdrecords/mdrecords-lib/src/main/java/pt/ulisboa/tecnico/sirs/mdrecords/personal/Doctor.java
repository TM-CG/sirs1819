package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.exception.*;
import org.joda.time.DateTime;

/** Class for describing Doctor entity */
public class Doctor extends Doctor_Base {
    
    
    public Doctor(String name, DateTime birthday, long identification) throws InvalidPersonException {
        super.checkArguments(name, birthday, identification);
        doctorAlreadyExists(identification);
        
        setName(name);
        setBirthday(birthday);
        setIdentification(identification);

        FenixFramework.getDomainRoot().getSNS().addDoctor(this);
    }
    
    public Doctor(String name, long identification) throws InvalidPersonException{
        super.checkArguments(name, identification);
        doctorAlreadyExists(identification);

        setName(name);
        setIdentification(identification);

        FenixFramework.getDomainRoot().getSNS().addDoctor(this);
    }
    
    /**
     * Checks if a doctor already exists.
     * @param identification of the doctor
     * @throws InvalidPersonException if the such doctor exists with same id on the system.
     */
    private void doctorAlreadyExists(long identification) throws InvalidPersonException {
        for (Doctor doctor: FenixFramework.getDomainRoot().getSNS().getDoctorSet()) {
            if (doctor.getIdentification() == this.getIdentification())
            throw new InvalidPersonException("Doctor: This doctor already exists!");
        }
        return;
    }

    /** Deletes Doctor from the SNS */
    public void delete() {
		setSNS(null);

		deleteDomainObject();
	}
}