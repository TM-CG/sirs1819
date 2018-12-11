package pt.ulisboa.tecnico.sirs.mdrecords;

import pt.ulisboa.tecnico.sirs.mdrecords.personal.RecordView;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.SNS;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.Patient;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.Record;

import javax.crypto.SecretKey;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;
import pt.ulisboa.tecnico.sirs.mdrecords.ws.handler.KerberosServerHandler;

import java.io.IOException;

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

    public String requestInformation(String requestType, String requestObject,String myType, Long myId, Long patientid) throws BadRequestInformation_Exception {
       try{
           RecordView recordView = RequestHelper.requestInformation(KerberosServerHandler.serverKey,requestObject, myType, myId, patientid);
           return recordView.description;
       }catch (BadRequestInformationException e){
           throwBadRequestInformation(e.getMessage());
       } catch (IOException e) {
           throwBadRequestInformation(e.getMessage());
       }
       return null;
    }

    public String addRelation(String myType, Long myId, Long patientId) throws BadAddRelation_Exception {
        try {
            return RequestHelper.addFollowingRelation(myType, myId, patientId);
        } catch (BadAddRelationException e) {
            throwBadAddRelationException(e.getMessage());
        }
        return null;
    }


    public String addIdentity(String type, String name, Long identification, XMLGregorianCalendar birthday) throws BadAddIdentity_Exception{
        try{
            return RequestHelper.createIdentity(type, KerberosServerHandler.serverKey, name, this.convert(birthday), identification);
        }catch (BadAddIdentityException e){
            throwBadAddIdentityException(e.getMessage());

        }
      return null;
    }

    public String addReport(String myType, Long personalId, Long patientId, String speciality, String description) throws BadAddReport_Exception{
        try{
            return RequestHelper.addReport(KerberosServerHandler.serverKey, myType, personalId, patientId, speciality, description);
        }catch (BadRecordException e){
            throwBadAddReportException(e.getMessage());
        }catch (IOException e) {
            throwBadAddReportException(e.getMessage());
        }
        return null;
    }

    public String addMedication(String myType, Long personalId, Long patientId, String speciality, String description, String drug, Float usage) throws BadAddMedication_Exception{
        try{
            return RequestHelper.addMedication(KerberosServerHandler.serverKey, myType, personalId, patientId, speciality, description, drug, usage);
        }catch (BadRecordException e){
            throwBadAddMedicationException(e.getMessage());
        }catch (IOException e) {
            throwBadAddMedicationException(e.getMessage());
        }
        return null;
    }

    public String addGeneric(String myType, Long personalId, Long patientId, String speciality, String description) throws BadAddGeneric_Exception{
        try{
            return RequestHelper.addGeneric(KerberosServerHandler.serverKey, myType, personalId, patientId, speciality, description);
        }catch (BadRecordException e){
            throwBadAddGenericException(e.getMessage());
        }catch (IOException e) {
            throwBadAddGenericException(e.getMessage());
        }
        return null;
    }

    public String addExam(String myType, Long personalId, Long patientId, String speciality, String description) throws BadAddExam_Exception{
        try{
            return RequestHelper.addReport(KerberosServerHandler.serverKey, myType, personalId, patientId, speciality, description);
        }catch (BadRecordException e){
            throwBadAddExamException(e.getMessage());
        }catch (IOException e) {
            throwBadAddExamException(e.getMessage());
        }
        return null;
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

    private void throwBadRequestInformation (final String message)
            throws BadRequestInformation_Exception {
        BadRequestInformation faultInfo = new BadRequestInformation();
        faultInfo.setMessage(message);
        throw new BadRequestInformation_Exception(message, faultInfo);
    }

    private void throwBadAddIdentityException(final String message)
            throws BadAddIdentity_Exception {
        BadAddIdentity faultInfo = new BadAddIdentity();
        faultInfo.setMessage(message);
        throw new BadAddIdentity_Exception(message, faultInfo);
    }

    private void throwBadAddReportException(final String message)
            throws BadAddReport_Exception {
        BadAddReport faultInfo = new BadAddIdentity();
        faultInfo.setMessage(message);
        throw new BadAddReport_Exception(message, faultInfo);
    }

    private void throwBadAddMedicationException(final String message)
            throws BadAddMedication_Exception {
        BadAddMedication faultInfo = new BadAddIdentity();
        faultInfo.setMessage(message);
        throw new BadAddMedication_Exception(message, faultInfo);
    }

    private void throwBadAddGenericException(final String message)
            throws BadAddGeneric_Exception {
        BadAddGeneric faultInfo = new BadAddIdentity();
        faultInfo.setMessage(message);
        throw new BadAddGeneric_Exception(message, faultInfo);
    }

    private void throwBadAddExamException(final String message)
            throws BadAddExam_Exception {
        BadAddExam faultInfo = new BadAddIdentity();
        faultInfo.setMessage(message);
        throw new BadAddExam_Exception(message, faultInfo);
    }

}