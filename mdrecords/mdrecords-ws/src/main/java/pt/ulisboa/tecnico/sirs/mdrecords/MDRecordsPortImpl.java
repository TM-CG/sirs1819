package pt.ulisboa.tecnico.sirs.mdrecords;

import pt.ulisboa.tecnico.sirs.mdrecords.personal.SNS;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.Patient;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.Record;
import pt.ulisboa.tecnico.sirs.mdrecords.ws.handler.KerberosServerHandler;

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

    public String readRecord(Long patientId, Long personalId, String recordType) throws BadReadRecord_Exception{
       
        if(!recordType.equals("Record")){
            return "At the moment the only recordType available at the system is 'Report'";
        }

        System.out.println("PORT IMPL KEY: " + KerberosServerHandler.serverKey);

        //TODO: Need server encryption key
        Record record = SNS.getInstance().readRecord(KerberosServerHandler.serverKey, patientId, personalId, recordType);
        if(record.equals(null)){
            return "No records available";
        }
        //Cleans the key
        KerberosServerHandler.serverKey = null;
        return record.getDescription();
    }

 }