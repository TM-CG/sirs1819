package pt.ulisboa.tecnico.sirs.mdrecords;

public class BadAddRelationException extends Exception {
	private static final long serialVersionUID = 1L;

	public BadAddRelationException() {
	}

	public BadAddRelationException(String message) {
		super(message);
	}

	public BadAddRelationException(Throwable cause) {
		super(cause);
	}

	public BadAddRelationException(String message, Throwable cause) {
		super(message, cause);
	}

}