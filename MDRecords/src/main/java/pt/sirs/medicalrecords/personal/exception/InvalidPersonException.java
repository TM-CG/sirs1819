package pt.sirs.medicalrecords.personal.exception;

public class InvalidPersonException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidPersonException() {
        super();
    }

    public InvalidPersonException(String message) {
        super(message);
    }

}