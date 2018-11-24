package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.exception.*;
import org.joda.time.DateTime;

import javax.crypto.SecretKey;

/** Class for describing Doctor entity */
public class Doctor extends Doctor_Base {

    public Doctor(SecretKey serverKey, String name, DateTime birthday, long identification) throws InvalidPersonException {
        checkArguments(name, birthday, identification);
        doctorAlreadyExists(identification);

        setName(serverKey, name);
        setBirthday(serverKey, birthday);
        setIdentification(identification);

        FenixFramework.getDomainRoot().getSns().addDoctor(this);
    }

    public Doctor(SecretKey serverKey, String name, long identification) throws InvalidPersonException{
        checkArguments(name, identification);
        doctorAlreadyExists(identification);

        setName(serverKey, name);
        setIdentification(identification);

        FenixFramework.getDomainRoot().getSns().addDoctor(this);
    }
    
    /**
     * Checks if a doctor already exists.
     * @param identification of the doctor
     * @throws InvalidPersonException if the such doctor exists with same id on the system.
     */
    private void doctorAlreadyExists(long identification) throws InvalidPersonException {
        for (Doctor doctor: FenixFramework.getDomainRoot().getSns().getDoctorSet()) {
            if (doctor.getIdentification() == identification)
                throw new InvalidPersonException("Doctor: This doctor already exists!");
        }
    }

    /** Deletes Doctor from the SNS */
    public void delete() {
		setSns(null);

		deleteDomainObject();
	}
}