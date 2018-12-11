package pt.ulisboa.tecnico.sirs.mdrecords;

public class BadRequestInformation extends Exception {
	private static final long serialVersionUID = 1L;

	public BadRequestInformation() {
	}

	public BadRequestInformation(String message) {
		super(message);
	}

	public BadRequestInformation(Throwable cause) {
		super(cause);
	}

	public BadRequestInformation(String message, Throwable cause) {
		super(message, cause);
	}

}