package pt.ulisboa.tecnico.sirs.mdrecords.personal;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;

public class Person extends Person_Base {
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

    /**
     * Secure setter to encrypt data in the database
     * @param serverKey
     * @param name
     */
    public void setName(SecretKey serverKey, String name) {
        try {
            String encryptedName = SNS.encrypt(serverKey, name);
            super.setName(encryptedName);

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

     /**
     * Secure setter to encrypt data in the database
     * @param serverKey
     * @param name
     */
    public void setBirthday(SecretKey serverKey, DateTime birthday) {
        try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
            String str = birthday.toString(fmt);
            String encryptedBirthday = SNS.encrypt(serverKey, str);
            super.setBirthday(encryptedBirthday);

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    public String getName(SecretKey serverKey) {
        String name = super.getName();
        try {
            return SNS.decrypt(serverKey, name);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DateTime getBirthday(SecretKey serverKey) {
        String birthday = super.getBirthday();
        try {
            String strBirthday = SNS.decrypt(serverKey, birthday);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
            return formatter.parseDateTime(strBirthday);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
