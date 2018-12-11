package pt.ulisboa.tecnico.sirs.mdrecords;

public class BadAddIdentityException extends Exception {
	private static final long serialVersionUID = 1L;

	public BadAddIdentityException() {
	}

	public BadAddIdentityException(String message) {
		super(message);
	}

	public BadAddIdentityException(Throwable cause) {
		super(cause);
	}

	public BadAddIdentityException(String message, Throwable cause) {
		super(message, cause);
	}

}