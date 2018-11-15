package pt.ulisboa.tecnico.sirs.mdrecords;

import pt.ulisboa.tecnico.sirs.mdrecords.personal.SNS;

import java.security.Key;

public class MDRecordsApp{

    public static void main(String[] args) {

        String uddiURL = null;
        String wsName = null;
        String wsURL = null;

    }
    public MDRecordsApp(){
        SNS.getInstance();
    }

    private MDRecordsPortType portType;


    public boolean authenticateUser(Key key, long id){
        return true;
    }



}
