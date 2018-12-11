package pt.ulisboa.tecnico.sirs.mdrecords;

import pt.ist.fenixframework.Atomic;
import pt.ulisboa.tecnico.sirs.kerby.SecurityHelper;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;
import pt.ulisboa.tecnico.sirs.mdrecords.ws.handler.KerberosServerHandler;

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
