package pt.ulisboa.tecnico.sirs;

import java.util.Random;

import pt.ulisboa.tecnico.sirs.kerby.cli.KerbyClient;
import pt.ulisboa.tecnico.sirs.kerby.SessionKeyAndTicketView;

public class RecordsClient {
    public static void main(String[] args) throws Exception {
        String kerbyPath;
        String user;

        //If no arguments were given just use the default one
        if (args.length == 0) {
            user = "vitor";
            kerbyPath = "http://localhost:8888/kerby";
        }
        else {
            user = args[0];
            kerbyPath = args[1];
        }
        
        
        System.out.println("Connecting at kerby server at: " + kerbyPath + " using user: " + user);
        KerbyClient client = new KerbyClient(kerbyPath);

        //Request temp session key
        SessionKeyAndTicketView result = client.requestTicket("alice@CXX.binas.org", "binas@CXX.binas.org",
                new Random().nextLong(), 60 /* seconds */);
                
        System.out.println("SessionKey: " + result.getSessionKey());
    }
}
