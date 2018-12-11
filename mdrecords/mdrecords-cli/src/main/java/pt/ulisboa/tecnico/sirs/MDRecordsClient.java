package pt.ulisboa.tecnico.sirs;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.GregorianCalendar;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;

import org.joda.time.DateTime;

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

    public String requestInformation(String requestType, String requestObject,String myType, Long myId, Long requestWhomId) throws BadRequestInformation_Exception{
        return port.requestInformation(requestType, requestObject, myType, myId, requestWhomId);
	}
	public String addRelation(String myType, Long myId, Long patientId) throws BadAddRelation_Exception {
		return port.addRelation(myType, myId, patientId);
	}
	public String addIdentity(String type, String name, Long identification, XMLGregorianCalendar birthday) throws BadAddIdentity_Exception {
		return port.addIdentity(type,name,identification,birthday);
	}

	public String addReport(String myType, Long personalId, Long patientId, String speciality, String description, XMLGregorianCalendar timestamp, String digest) throws BadAddReport_Exception {
		return port.addReport(myType, personalId, patientId, speciality, description, timestamp, digest);
	}

	public String addMedication(String myType, Long personalId, Long patientId, String speciality, String description, XMLGregorianCalendar timestamp, String digest, String drug, Float usage) throws BadAddMedication_Exception {
		return port.addMedication(myType, personalId, patientId, speciality, description, timestamp, digest, drug, usage);
	}

	public String addGeneric(String myType, Long personalId, Long patientId, String speciality, String description, XMLGregorianCalendar timestamp, String digest) throws BadAddGeneric_Exception {
		return port.addGeneric(myType, personalId, patientId, speciality, description, timestamp, digest);
	}

	public String addExam(String myType, Long personalId, Long patientId, String speciality, String description, XMLGregorianCalendar timestamp, String digest, String examName) throws BadAddExam_Exception {
		return port.addExam(myType, personalId, patientId, speciality, description, timestamp, digest, examName);
	}

	/*public XMLGregorianCalendar convert(DateTime d) throws DatatypeConfigurationException{
		final GregorianCalendar calendar = new GregorianCalendar(d.getZone().toTimeZone());
		calendar.setTimeInMillis(d.getMillis());
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
	}*/
	
	
}