package pt.ulisboa.tecnico.sirs;

import java.util.Random;

import pt.ulisboa.tecnico.sirs.kerby.cli.KerbyClient;
import pt.ulisboa.tecnico.sirs.kerby.SessionKeyAndTicketView;

public class RecordsClient {
    public static void main(String[] args) throws Exception {
        String kerbyPath;
        String user;
        String mdrecordsServerPath;

        //If no arguments were given just use the default one
        if (args.length == 0) {
            user = "12345678";
            kerbyPath = "http://localhost:8888/kerby";
            mdrecordsServerPath = "http://localhost:8889/mdrecords";
        }
        else {
            user = args[0];
            kerbyPath = args[1];
            mdrecordsServerPath = args[2];
        }
        
        
        System.out.println("Connecting at kerby server at: " + kerbyPath + " using user: " + user);
        KerbyClient client = new KerbyClient(kerbyPath);

        //Request temp session key
        //SessionKeyAndTicketView result = client.requestTicket(user, "69696969",
        //        new Random().nextLong(), 60 /* seconds */);
                
        //System.out.println("SessionKey: " + result.getSessionKey());

        MDRecordsClient recordsClient = new MDRecordsClient(mdrecordsServerPath);
        
        System.out.println("Let me invoke requestRecord.");
        String response = recordsClient.requestRecord(new Long(1234), new Long(45456), "hahaha");
        
        System.out.println("RESPONSE: " + response);

    }
}
