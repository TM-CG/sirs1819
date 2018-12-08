package pt.ulisboa.tecnico.sirs.mdrecords.personal;

public class InvalidRecordException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidRecordException() {
        super();
    }

    public InvalidRecordException(String message) {
        super(message);
    }

}