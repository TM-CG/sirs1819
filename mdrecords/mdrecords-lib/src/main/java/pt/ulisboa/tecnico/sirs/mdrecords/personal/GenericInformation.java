package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;
import pt.ist.fenixframework.FenixFramework;

import javax.crypto.SecretKey;

/**
 * A class for describing Generic Medical Information of patients (e.g. allergies)
 */
public class GenericInformation extends GenericInformation_Base {
    
    public GenericInformation() {
        super();
    }

    public GenericInformation(SecretKey serverKey, long personalId, long patientId, DateTime timeStamp, String speciality, String description, String digest) throws InvalidRecordException {
        checkArguments(personalId, patientId, timeStamp, speciality, description);

        setPersonalId(personalId);
        setPatientId(patientId);
        setTimeStamp(serverKey, timeStamp);
        setSpeciality(serverKey, speciality);
        setDescription(serverKey, description);

        setDigest(digest);

        FenixFramework.getDomainRoot().getSns().addRecord(this);

    }

    @Override
    public GenericInformationView getView(SecretKey serverKey) {
        return new GenericInformationView(getPersonalId(), getPatientId(), getTimeStamp(serverKey), getSpeciality(serverKey), getDescription(serverKey));
    }

}
