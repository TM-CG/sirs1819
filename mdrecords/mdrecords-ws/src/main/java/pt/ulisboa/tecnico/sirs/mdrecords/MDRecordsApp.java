package pt.ulisboa.tecnico.sirs.mdrecords;

import pt.ist.fenixframework.Atomic;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.*;

import java.security.Key;

public class MDRecordsApp{

    private static SNS sns;

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void main(String[] args) throws Exception {

        sns = getSNSInstance();

        String uddiURL = null;
        String wsName = null;
        String wsURL = null;

        Doctor doctor = new Doctor("Vítor Nunes", 123456789);
    }

    @Atomic(mode = Atomic.TxMode.READ)
    private static SNS getSNSInstance() {
        return SNS.getInstance();
    }
    
    private MDRecordsPortType portType;


    public boolean authenticateUser(Key key, long id){
        return true;
    }



}
