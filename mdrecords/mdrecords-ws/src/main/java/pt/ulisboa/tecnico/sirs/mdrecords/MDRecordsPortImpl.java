package pt.ulisboa.tecnico.sirs.mdrecords;

import pt.ulisboa.tecnico.sirs.mdrecords.personal.SNS;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.Patient;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.Record;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;

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

    public String requestInformation(String requestType, String requestObject,String myType, Long myId, Long requestWhomId) throws BadRequestInformation_Exception{
       return "";
    }

    public String addRelation(String myType, Long myId, Long patientId) throws BadAddRelation_Exception {
        return RequestHelper.addFollowingRelation(myType, myId, patientId);
    }

    public String addIdentity(String type, String name, Long identification, XMLGregorianCalendar birthday){
        try{
            return RequestHelper.createIdentity(secretKey, type, name, identification, this.convert(birthday));
        }catch (BadAddRelationException e){
            throwBadAddRelationException(e.getMessage());

        }
    }

    public DateTime convert(final XMLGregorianCalendar xmlgc) {
        return new DateTime(xmlgc.toGregorianCalendar().getTime());
    }

    private void throwBadAddRelationException(final String message)
            throws BadAddRelation_Exception {
        BadAddRelation faultInfo = new BadAddRelation();
        faultInfo.setMessage(message);
        throw new BadAddRelation_Exception(message, faultInfo);
    }
 }