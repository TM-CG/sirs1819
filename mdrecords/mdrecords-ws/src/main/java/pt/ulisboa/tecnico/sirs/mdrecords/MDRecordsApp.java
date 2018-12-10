package pt.ulisboa.tecnico.sirs.mdrecords;

import pt.ist.fenixframework.Atomic;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;

import java.security.*;

import static pt.ulisboa.tecnico.sirs.mdrecords.personal.CertificateHelper.*;


public class MDRecordsApp{

    private static SNS sns;

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void main(String[] args) throws Exception {


        sns = getSNSInstance();

        String uddiURL = null;
        String wsName = null;
        String wsURL = null;

        //If no arguments were given just use the default one
        if (args.length == 0) {
            System.out.println("Missing arguments:");

            return;
        }
        else {
            wsURL = args[0];
            wsName = args[1];
        }

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); //key size
        Key key = keyGen.generateKey();
        SecretKeySpec k = new SecretKeySpec(key.getEncoded(), "AES");

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime dt = formatter.parseDateTime("01/01/2000");

        Doctor doctor = new Doctor(k,"Vítor Nunes", dt, 123456789);

        //System.out.println("O meu nome é: " + doctor.getName(k));
        //System.out.println("O meu birthday é: " + doctor.getBirthday(k).toString(formatter));*/

        Patient patient = new Patient(k, "miquelina de belém", dt, 123456788);

        //relação criada
        doctor.addPatient(patient);
        //instante atual
        DateTime ts = new DateTime();
        Record record = new Record(k, 123456789, 123456788, ts, "pichologia", "penis muy mirradito");
        Record novo = sns.readRecord(k, new Long(123456789), new Long(123456788),"Record");

        System.out.println(novo.getSpeciality(k));
        System.out.println(novo.getDescription(k));

        System.out.println("O meu nome é: " + doctor.getName(k));
        System.out.println("O meu birthday é: " + doctor.getBirthday(k).toString(formatter));

        /** ===========Begin============= RECORD DIGEST TEST ===========Begin============= **/
        /*Patient patient = new Patient(k, "Afadílio Vieira", dt, 321);

        Record record = new Record(123456789,321, new DateTime(), "Clinica geral", "É alérigico a peras");

        byte[] digest = record.calcDigest();

        System.out.println("Ó Zé olha o digest: " + digest);

        RecordView recordView = record.getView();

        record.setDigest(createRecordDigest("../../users/Vitor/vitor.key.pem", recordView));

        System.out.println("Ó Zé olha o digest encriptado: " + record.getDigest());

        System.out.println("Ó Afadílio topa-me isto: " + record.checkAuthenticity(readPublicKey("vitor.cert.pem")));*/


        /** ===========End============= RECORD DIGEST TEST ===========End============= **/


        /** ===========Begin============= SERVER DATABASE ENCRYPTION ===========Begin============= **/
        /*String dados = "Estes são os dados super secretos!";
        SecretKeySpec k = new SecretKeySpec(key.getEncoded(), "AES");
        String encriptados = SNS.encrypt(k, dados);

        System.out.println("Dados encriptados: " + encriptados);

        String desencriptados = SNS.decrypt(k, encriptados);

        System.out.println("Dados desencriptados: " + desencriptados);*/

        /** ===========End============= SERVER DATABASE ENCRYPTION ===========End============= **/

        MDRecordsEndpointManager endpoint;
        if (uddiURL == null) {
			endpoint = new MDRecordsEndpointManager(wsName, wsURL);
		}
		else {
			endpoint = new MDRecordsEndpointManager(uddiURL, wsName, wsURL);
        }
        
        System.out.println("MDRecords Server is running!");
		
		try {
			endpoint.start();

			endpoint.awaitConnections();
			
		} catch(Exception e) {
			System.out.println("Connection error");
		}
		finally {
			endpoint.stop();
		}

    }

    @Atomic(mode = Atomic.TxMode.READ)
    private static SNS getSNSInstance() {
        return SNS.getInstance();
    }
    
    public boolean authenticateUser(Key key, long id){
        return true;
    }



}
