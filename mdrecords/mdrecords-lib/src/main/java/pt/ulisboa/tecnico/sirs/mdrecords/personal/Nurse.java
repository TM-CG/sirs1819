package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import pt.ist.fenixframework.FenixFramework;

import javax.crypto.SecretKey;

import org.joda.time.DateTime;

/** Class for describing Nurse entity */
public class Nurse extends Nurse_Base {
    
    
    public Nurse(SecretKey serverKey, String name, DateTime birthday, long identification) throws InvalidPersonException {
        super.checkArguments(name, birthday, identification);
        nurseAlreadyExists(identification);
        
        setName(serverKey, name);
        setBirthday(serverKey, birthday);
        setIdentification(identification);

        SNS.getInstance().addNurse(this);
    }

    public Nurse(SecretKey serverKey, String name, DateTime birthday, long identification, String certificateFileName) throws InvalidPersonException {
        super.checkArguments(name, birthday, identification);
        nurseAlreadyExists(identification);

        setName(serverKey, name);
        setBirthday(serverKey, birthday);
        setIdentification(identification);
        setCertificateFileName(certificateFileName);

        SNS.getInstance().addNurse(this);
    }

    public Nurse(SecretKey serverKey, String name, long identification) throws InvalidPersonException{
        super.checkArguments(name, identification);
        nurseAlreadyExists(identification);

        setName(serverKey, name);
        setIdentification(identification);

        SNS.getInstance().addNurse(this);
    }

    public Nurse(SecretKey serverKey, String name, long identification, String certificateFileName) throws InvalidPersonException{
        super.checkArguments(name, identification);
        nurseAlreadyExists(identification);

        setName(serverKey, name);
        setIdentification(identification);
        setCertificateFileName(certificateFileName);

        SNS.getInstance().addNurse(this);
    }
    
    /**
     * Checks if a nurse already exists.
     * @param identification of the nurse
     * @throws InvalidPersonException if the such nurse exists with same id on the system.
     */
    private void nurseAlreadyExists(long identification) throws InvalidPersonException {
        for (Nurse nurse: SNS.getInstance().getNurseSet()) {
            if (nurse.getIdentification() == identification)
            throw new InvalidPersonException("Nurse: This nurse already exists!");
        }
    }

    public void delete() {
		setSns(null);
		deleteDomainObject();
	}
}