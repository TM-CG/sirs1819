package pt.sirs.MedicalRecords.Personal;

import pt.sirs.MedicalRecords.Personal.Exception.*;
import java.util.Date;

/**
 * A class for describing Person abstract data
 */
public abstract class Person {

    private String _name;

    private Date _birthday;

    /** Citizen Card number identification **/
    private long _identification;

    //private PublicKey _publicKey;

    public Person(String name, Date birthday, long identification) throws InvalidPersonException{
        checkArguments(name, birthday, identification);
        this._name           = name;
        this._birthday       = birthday;
        this._identification = identification;
    }

    public Person(String name, long identification) throws InvalidPersonException{
        checkArguments(name, identification);
        this._name           = name;
        this._identification = identification;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public Date getBirthday() {
        return _birthday;
    }

    public void setBirthday(Date _birthday) {
        this._birthday = _birthday;
    }

    public long getIdentification() {
        return _identification;
    }

    public void setIdentification(long _identification) {
        this._identification = _identification;
    }

    private void checkArguments(String name, Date birthday, long identification) throws InvalidPersonException{
        checkArguments(name, identification);
        //check if birthday is in the future
        if (birthday == null) {
            throw new InvalidPersonException("Invalid Person's Birthday: date cannot be null");
        }
        if (birthday.after(new Date())) {
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
