package pt.ulisboa.tecnico.sirs.mdrecords;

public class BadRecordException extends Exception {
	private static final long serialVersionUID = 1L;

	public BadRecordException() {
	}

	public BadRecordException(String message) {
		super(message);
	}

	public BadRecordException(Throwable cause) {
		super(cause);
	}

	public BadRecordException(String message, Throwable cause) {
		super(message, cause);
	}

}