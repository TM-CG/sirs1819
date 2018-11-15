package pt.ulisboa.tecnico.sirs.mdrecords;

import pt.ulisboa.tecnico.sirs.mdrecords.personal.SNS;

import java.security.Key;

public class MDRecordsApp{

    public MDRecordsApp(){
        SNS.getInstance();
    }

   // MDRecordsEndpointManager endpoit = null;


    public boolean authenticateUser(Key key, long id){
        return true;
    }



}
