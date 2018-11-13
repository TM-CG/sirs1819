package pt.ulisboa.tecnico.sirs.medicalrecords.personal;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.sirs.medicalrecords.personal.exception.*;
import org.joda.time.DateTime;

/** Class for describing Doctor entity */
public class Doctor extends Doctor_Base {
    
    
    public Doctor(String name, DateTime birthday, long identification) throws InvalidPersonException {
        super.checkArguments(name, birthday, identification);
        doctorAlreadyExists(identification);
        
        setName(name);
        setBirthday(birthday);
        setIdentification(identification);

        FenixFramework.getDomainRoot().addDoctor(this);
    }
    
    public Doctor(String name, long identification) throws InvalidPersonException{
        super.checkArguments(name, identification);
        doctorAlreadyExists(identification);

        setName(name);
        setIdentification(identification);

        FenixFramework.getDomainRoot().addDoctor(this);
    }
    
    /**
     * Checks if a doctor already exists.
     * @param identification of the doctor
     * @throws InvalidPersonException if the such doctor exists with same id on the system.
     */
    private void doctorAlreadyExists(long identification) throws InvalidPersonException {
        for (Doctor doctor: FenixFramework.getDomainRoot().getDoctorSet()) {
            if (doctor.getIdentification() == this.getIdentification())
                return;
        }
        throw new InvalidPersonException("Doctor: This doctor already exists!");
    }
}