package pt.ulisboa.tecnico.sirs.medicalrecords.personal;
import pt.ulisboa.tecnico.sirs.medicalrecords.personal.exception.*;
import org.joda.time.DateTime;

public class Nurse extends Nurse_Base{
    public Nurse(){
        super();
    }

    public Nurse(String name, DateTime birthday, long identification) throws InvalidPersonException{
        super.checkArguments(name, birthday, identification);
    }

    public Nurse(String name, long identification) throws InvalidPersonException{
        super.checkArguments(name, identification);
    }
}