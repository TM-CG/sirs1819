package pt.ulisboa.tecnico.sirs.medicalrecords.personal;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.sirs.medicalrecords.personal.exception.*;
import org.joda.time.DateTime;

/** Class for describing Nurse entity */
public class Nurse extends Nurse_Base {
    
    
    public Nurse(String name, DateTime birthday, long identification) throws InvalidPersonException {
        super.checkArguments(name, birthday, identification);
        nurseAlreadyExists(identification);
        
        setName(name);
        setBirthday(birthday);
        setIdentification(identification);

        FenixFramework.getDomainRoot().getSNS().addNurse(this);
    }
    
    public Nurse(String name, long identification) throws InvalidPersonException{
        super.checkArguments(name, identification);
        nurseAlreadyExists(identification);

        setName(name);
        setIdentification(identification);

        FenixFramework.getDomainRoot().getSNS().addNurse(this);
    }
    
    /**
     * Checks if a doctor already exists.
     * @param identification of the doctor
     * @throws InvalidPersonException if the such doctor exists with same id on the system.
     */
    private void nurseAlreadyExists(long identification) throws InvalidPersonException {
        for (Nurse nurse: FenixFramework.getDomainRoot().getSNS().getNurseSet()) {
            if (nurse.getIdentification() == this.getIdentification())
                return;
        }
        throw new InvalidPersonException("Doctor: This doctor already exists!");
    }
}