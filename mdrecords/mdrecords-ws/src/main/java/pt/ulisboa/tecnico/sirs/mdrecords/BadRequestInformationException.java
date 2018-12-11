package pt.ulisboa.tecnico.sirs.mdrecords;

public class BadRequestInformationException extends Exception {
	private static final long serialVersionUID = 1L;

	public BadRequestInformationException() {
	}

	public BadRequestInformationException(String message) {
		super(message);
	}

	public BadRequestInformationException(Throwable cause) {
		super(cause);
	}

	public BadRequestInformationException(String message, Throwable cause) {
		super(message, cause);
	}

}