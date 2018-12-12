package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;
import pt.ist.fenixframework.FenixFramework;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

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

        try {
            checkIncomingDigest(serverKey, personalId);

        } catch (IllegalBlockSizeException e) {
            throw new InvalidRecordException(e.getMessage());
        } catch (InvalidKeyException e) {
            throw new InvalidRecordException(e.getMessage());
        } catch (BadPaddingException e) {
            throw new InvalidRecordException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidRecordException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            throw new InvalidRecordException(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new InvalidRecordException(e.getMessage());
        } catch (CertificateException e) {
            throw new InvalidRecordException(e.getMessage());
        }

    }

    @Override
    public ReportView getView(SecretKey serverKey) {
        return new ReportView(getPersonalId(), getPatientId(), getTimeStamp(serverKey), getSpeciality(serverKey), getDescription(serverKey));
    }
    
}
