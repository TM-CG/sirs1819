package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;
import pt.ist.fenixframework.FenixFramework;

import javax.crypto.SecretKey;

public class Report extends Report_Base {
    
    public Report() {
        super();
    }

    public Report(SecretKey serverKey, long personalId, long patientId, DateTime timeStamp, String speciality, String description, String digest) throws InvalidRecordException {
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
    public ReportView getView(SecretKey serverKey) {
        return new ReportView(getPersonalId(), getPatientId(), getTimeStamp(serverKey), getSpeciality(serverKey), getDescription(serverKey));
    }
    
}
