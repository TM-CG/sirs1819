package pt.ulisboa.tecnico.sirs;

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

        MDRecordsClient recordsClient = new MDRecordsClient(mdrecordsServerPath);
        
        System.out.println("Let me invoke readRecord");
        Long patientId = new Long(123456788);
        Long personalId = new Long(123456789);
        String response = recordsClient.readRecord(patientId, personalId, "Record");
        
        System.out.println("RESPONSE: " + response);

    }
}
