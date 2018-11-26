package pt.ulisboa.tecnico.sirs.mdrecords;

import pt.ulisboa.tecnico.sirs.mdrecords.personal.SNS;

import javax.jws.HandlerChain;
import javax.jws.WebService;

/**
 * Medical records implementation class.
 * 
 */
/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "pt.ulisboa.tecnico.sirs.mdrecords.MDRecordsPortType",
        wsdlLocation = "",
        name ="MDRecordsWebService",
        portName = "MDRecordsPortType",
        targetNamespace="http://mdrecords.org",
        serviceName = "MDRecordsService"
)
@HandlerChain(file = "/mdrecords-ws_handler-chain.xml")
 public class MDRecordsPortImpl implements MDRecordsPortType {

    // end point manager
	private MDRecordsEndpointManager endpointManager;

    public MDRecordsPortImpl(MDRecordsEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

    public String requestRecord(Long patientId, Long personalId, String operationType) throws BadRecordRequest_Exception {
        return "Hello! This is the requestRecord reply of patient: " + patientId + " by personal: " + personalId + " operation: " + operationType;
    }

 }