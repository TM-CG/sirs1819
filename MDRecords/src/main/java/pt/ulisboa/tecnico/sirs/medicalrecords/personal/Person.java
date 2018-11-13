package pt.ulisboa.tecnico.sirs.medicalrecords.personal;

import pt.ulisboa.tecnico.sirs.medicalrecords.personal.exception.*;
import org.joda.time.DateTime;

public abstract class Person extends Person_Base {
    /**
     * Checks if the Person's Arguments are correct
     * @param name of the person
     * @param birthday of the person
     * @param identification of the person
     * @throws InvalidPersonException if some argument is not null
     */
    protected void checkArguments(String name, DateTime birthday, long identification) throws InvalidPersonException{
        checkArguments(name, identification);
        //check if birthday is in the future
        if (birthday == null) {
            throw new InvalidPersonException("Invalid Person's Birthday: date cannot be null");
        }
        if (birthday.isAfterNow()) {
            throw new InvalidPersonException("Invalid Person's Birthday: date cannot be in the future");
        }

    }

    protected void checkArguments(String name, long identification) throws InvalidPersonException{
        if (name == null)
            throw new InvalidPersonException("Invalid Person's Name: name cannot be null");
        if (identification <= 0)
            throw new InvalidPersonException("Invalid Person's Identification: identification needs to be a valid number");
        
                                                                                                                            
    }
    
}
