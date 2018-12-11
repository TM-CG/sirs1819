package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;
import pt.ist.fenixframework.FenixFramework;

import javax.crypto.SecretKey;

/** Class for describing Administrative personal staff */
public class Administrative extends Administrative_Base {

    public Administrative(SecretKey serverKey, String name, DateTime birthday, long identification) throws InvalidPersonException {
        checkArguments(name, birthday, identification);
        administrativeAlreadyExists(identification);

        setName(serverKey, name);
        setBirthday(serverKey, birthday);
        setIdentification(identification);

        SNS.getInstance().addAdministrative(this);
    }

    public Administrative(SecretKey serverKey, String name, DateTime birthday, long identification, String certificateFileName) throws InvalidPersonException {
        checkArguments(name, birthday, identification);
        administrativeAlreadyExists(identification);

        setName(serverKey, name);
        setBirthday(serverKey, birthday);
        setIdentification(identification);
        setCertificateFileName(certificateFileName);

        SNS.getInstance().addAdministrative(this);
    }

    /**
     * Checks if a Administrative person already exists
     * @param identification
     * @throws InvalidPersonException
     */
    private void administrativeAlreadyExists(long identification) throws InvalidPersonException {
        for (Administrative administrative: SNS.getInstance().getAdministrativeSet()) {
            if (administrative.getIdentification() == identification)
                throw new InvalidPersonException("Administrative: This person already exists!");
        }
    }

    /** Deletes Doctor from the SNS */
    public void delete() {
        setSns(null);

        deleteDomainObject();
    }
    
}
