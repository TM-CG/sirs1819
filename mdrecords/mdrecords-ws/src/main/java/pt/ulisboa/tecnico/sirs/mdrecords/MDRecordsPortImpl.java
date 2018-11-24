package pt.ulisboa.tecnico.sirs.mdrecords;

import javax.jws.WebService;

/**
 * Medical records implementation class.
 * 
 */

 public class MDRecordsPortImpl implements MDRecordsPortType {

    // end point manager
	private MDRecordsEndpointManager endpointManager;

    public MDRecordsPortImpl(MDRecordsEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

    public String requestRecord(Long patientId, Long personalId, String operationType) throws BadRecordRequest_Exception {
        return null;
    }

 }