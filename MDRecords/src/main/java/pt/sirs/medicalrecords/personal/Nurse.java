package pt.sirs.medicalrecords.personal;
import pt.sirs.medicalrecords.personal.exception.*;
import org.joda.time.DateTime;

public class Nurse extends Nurse_Base{
    public Nurse(){
        super();
    }

    public Nurse(String name, DateTime birthday, long identification) throws InvalidPersonException{
        super(name, birthday, identification);
    }

    public Nurse(String name, long identification) throws InvalidPersonException{
        super(name, identification);
    }
}