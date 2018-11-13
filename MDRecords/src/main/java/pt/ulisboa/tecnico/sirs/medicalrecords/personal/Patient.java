package pt.ulisboa.tecnico.sirs.medicalrecords.personal;

import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.sirs.medicalrecords.personal.exception.InvalidPersonException;

public class Patient extends Patient_Base {
    

    public Patient(String name, DateTime birthday, long identification) throws InvalidPersonException{
        super.checkArguments(name, birthday, identification);
        setName(name);
        setBirthday(birthday);
        setIdentification(identification);
        FenixFramework.getDomainRoot().getSNS().addPatient(this);
    }

    public Patient(String name, long identification) throws InvalidPersonException{
        super.checkArguments(name, identification);
        setName(name);
        setIdentification(identification);
        FenixFramework.getDomainRoot().getSNS().addPatient(this);
    }
}
