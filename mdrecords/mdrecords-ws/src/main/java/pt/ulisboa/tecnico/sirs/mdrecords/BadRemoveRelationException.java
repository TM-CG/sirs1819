package pt.ulisboa.tecnico.sirs.mdrecords;

public class BadRemoveRelationException extends Exception {
	private static final long serialVersionUID = 1L;

	public BadRemoveRelationException() {
	}

	public BadRemoveRelationException(String message) {
		super(message);
	}

	public BadRemoveRelationException(Throwable cause) {
		super(cause);
	}

	public BadRemoveRelationException(String message, Throwable cause) {
		super(message, cause);
	}

}