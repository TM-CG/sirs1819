package pt.ulisboa.tecnico.sirs;

public class MDRecordsClientException extends Exception {

	private static final long serialVersionUID = 1L;

	public MDRecordsClientException() {
	}

	public MDRecordsClientException(String message) {
		super(message);
	}

	public MDRecordsClientException(Throwable cause) {
		super(cause);
	}

	public MDRecordsClientException(String message, Throwable cause) {
		super(message, cause);
	}

}