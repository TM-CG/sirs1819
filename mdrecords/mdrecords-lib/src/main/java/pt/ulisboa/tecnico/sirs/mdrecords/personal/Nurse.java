package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.exception.*;
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
     * Checks if a nurse already exists.
     * @param identification of the nurse
     * @throws InvalidPersonException if the such nurse exists with same id on the system.
     */
    private void nurseAlreadyExists(long identification) throws InvalidPersonException {
        for (Nurse nurse: FenixFramework.getDomainRoot().getSNS().getNurseSet()) {
            if (nurse.getIdentification() == this.getIdentification())
            throw new InvalidPersonException("Nurse: This nurse already exists!");
        }
        return;
    }

    public void delete() {
		setSNS(null);
		deleteDomainObject();
	}
}