package pt.ulisboa.tecnico.sirs;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;

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

        DateTime dateTime = new DateTime();

        final GregorianCalendar calendar = new GregorianCalendar(dateTime.getZone().toTimeZone());
        calendar.setTimeInMillis(dateTime.getMillis());
        XMLGregorianCalendar birthday = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);

        recordsClient.addIdentity("Doctor","Vítor Nunes", new Long(123456789), birthday);


    }
}
