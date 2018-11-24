package pt.ulisboa.tecnico.sirs.mdrecords;

import pt.ist.fenixframework.Atomic;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.*;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class MDRecordsApp{

    private static SNS sns;

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void main(String[] args) throws Exception {

        sns = getSNSInstance();

        String uddiURL = null;
        String wsName = null;
        String wsURL = null;

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); //key size
        Key key = keyGen.generateKey();

        String dados = "Estes são os dados super secretos!";
        SecretKeySpec k = new SecretKeySpec(key.getEncoded(), "AES");
        String encriptados = SNS.encrypt(k, dados);

        System.out.println("Dados encriptados: " + encriptados);

        String desencriptados = SNS.decrypt(k, encriptados);

        System.out.println("Dados desencriptados: " + desencriptados);

        //Doctor doctor = new Doctor("Vítor Nunes", 123456789);
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
