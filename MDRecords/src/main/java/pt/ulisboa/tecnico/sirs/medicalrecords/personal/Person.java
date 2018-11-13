package pt.ulisboa.tecnico.sirs.medicalrecords.personal;

import pt.ulisboa.tecnico.sirs.medicalrecords.personal.exception.*;
import org.joda.time.DateTime;

public abstract class Person extends Person_Base {
<<<<<<< HEAD:MDRecords/src/main/java/pt/ulisboa/tecnico/sirs/medicalrecords/personal/Person.java

=======
    
>>>>>>> 25c1289e0e35d67dafa99c945c1359735085ab57:MDRecords/src/main/java/pt/sirs/medicalrecords/personal/Person.java
    public Person() {
        super();
    }

    public Person(String name, DateTime birthday, long identification) throws InvalidPersonException{
        checkArguments(name, birthday, identification);
        setName(name);
        setBirthday(birthday);
        setIdentification(identification);
    }

    public Person(String name, long identification) throws InvalidPersonException{
        checkArguments(name, identification);
        setName(name);
        setIdentification(identification);
    }

    private void checkArguments(String name, DateTime birthday, long identification) throws InvalidPersonException{
        checkArguments(name, identification);
        //check if birthday is in the future
        if (birthday == null) {
            throw new InvalidPersonException("Invalid Person's Birthday: date cannot be null");
        }
        if (birthday.isAfterNow()) {
            throw new InvalidPersonException("Invalid Person's Birthday: date cannot be in the future");
        }

    }

    private void checkArguments(String name, long identification) throws InvalidPersonException{
        if (name == null)
            throw new InvalidPersonException("Invalid Person's Name: name cannot be null");
        if (identification <= 0)
            throw new InvalidPersonException("Invalid Person's Identification: identification needs to be a valid number");
        //TODO VERIFICAR SE A IDENTICACAO E UNICA
                                                                                                                            
    }
    
}
