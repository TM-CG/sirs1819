package pt.ulisboa.tecnico.sirs;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sirs.mdrecords.BadRecordRequest_Exception;
import pt.ulisboa.tecnico.sirs.mdrecords.*;
//import pt.ulisboa.tecnico.sirs.ws.uddi.UDDINaming;

public class MDRecordsClient implements MDRecordsPortType {

    /** WS service */
	MDRecordsService service = null;

	/** WS port (port type is the interface, port is the implementation) */
	MDRecordsPortType port = null;

    /** UDDI server URL */
	private String uddiURL = null;

	/** WS name */
	private String wsName = null;

	/** WS end point address */
	private String wsURL = null; // default value is defined inside WSDL

	public String getWsURL() {
		return wsURL;
	}

	/** output option **/
	private boolean verbose = false;

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
    }
    
    /** constructor with provided web service URL */
	public MDRecordsClient(String wsURL) throws MDRecordsClientException {
		this.wsURL = wsURL;
		createStub();
	}

	/** constructor with provided UDDI location and name */
	public MDRecordsClient(String uddiURL, String wsName) throws MDRecordsClientException {
		this.uddiURL = uddiURL;
		this.wsName = wsName;
		//uddiLookup();
		createStub();
    }
    
    /** UDDI lookup */
	/*private void uddiLookup() throws MDRecordsClientException {
		try {
			if (verbose)
				System.out.printf("Contacting UDDI at %s%n", uddiURL);
			UDDINaming uddiNaming = new UDDINaming(uddiURL);

			if (verbose)
				System.out.printf("Looking for '%s'%n", wsName);
			wsURL = uddiNaming.lookup(wsName);

		} catch (Exception e) {
			String msg = String.format("Client failed lookup on UDDI at %s!", uddiURL);
			throw new MDRecordsClientException(msg, e);
		}

		if (wsURL == null) {
			String msg = String.format("Service with name %s not found on UDDI at %s", wsName, uddiURL);
			throw new MDRecordsClientException(msg);
		}
	}*/

	/** Stub creation and configuration */
	private void createStub() {
		if (verbose)
			System.out.println("Creating stub ...");
		service = new MDRecordsService();
		port = service.getMDRecordsPort();

		if (wsURL != null) {
			if (verbose)
				System.out.println("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, wsURL);
		}
    }
    
    // remote invocation methods ----------------------------------------------

    public String readRecord(Long patientId, Long personalId, String recordType)
            throws BadReadRecord_Exception {
        return port.readRecord(patientId, personalId, recordType);
	}
    
}