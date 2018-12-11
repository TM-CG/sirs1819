package pt.ulisboa.tecnico.sirs.mdrecords;

public class BadAddIdentity extends Exception {
	private static final long serialVersionUID = 1L;

	public BadAddIdentity() {
	}

	public BadAddIdentity(String message) {
		super(message);
	}

	public BadAddIdentity(Throwable cause) {
		super(cause);
	}

	public BadAddIdentity(String message, Throwable cause) {
		super(message, cause);
	}

}