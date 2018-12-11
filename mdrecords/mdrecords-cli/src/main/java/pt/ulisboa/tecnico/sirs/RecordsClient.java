package pt.ulisboa.tecnico.sirs;

import org.joda.time.DateTime;
import pt.ulisboa.tecnico.sirs.mdrecords.BadAddIdentity_Exception;
import pt.ulisboa.tecnico.sirs.mdrecords.BadAddRelation_Exception;
import pt.ulisboa.tecnico.sirs.mdrecords.BadRequestInformation_Exception;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.*;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class RecordsClient {
    public static void main(String[] args) throws Exception {
        String kerbyPath;
        String user;
        String type;
        long identification;
        String mdrecordsServerPath;

        //If no arguments were given just use the default one
        if (args.length == 0) {
            user = "12345678";
            identification = Long.parseLong(user);
            kerbyPath = "http://localhost:8888/kerby";
            type = "Doctor";
            mdrecordsServerPath = "http://localhost:8889/mdrecords";
        }
        else {
            user = args[0];
            type = args[1];
            identification = Long.parseLong(user);
            kerbyPath = args[2];
            mdrecordsServerPath = args[3];
        }
        
        
        System.out.println("Hello: " + user);

        MDRecordsClient recordsClient = new MDRecordsClient(mdrecordsServerPath);

        MenuUI menu = new MenuUI("Select an option");
        menu.addEntry("*DEBUG* - Create");
        menu.addEntry("*DEBUG* - Add following");
        menu.addEntry("Read Operations");
        menu.addEntry("Write Operations");
        menu.addEntry("Exit");

        String name;
        String birthday;
        long identification2;
        DateFormat format;
        Date date;
        GregorianCalendar cal;
        XMLGregorianCalendar xmlGregCal;
        MenuUI loginMenu;
        DateTime dt;

        String speciality;
        String description;
        String digest;
        String examName;
        String drugName;

        float dosage;

        int option;
        int option2;

        dt = new DateTime();
        System.out.println(dt);

        ReportView aaa = new ReportView(identification, new Long(987654321), dt , "fgfdgfd", "fgdfgvg");

        System.out.println("report: " + aaa);

        digest = CertificateHelper.createRecordDigest(new Long(identification).toString(), aaa);

        System.out.println("AAAAA: " + digest);



        do {
            option = menu.display();

            switch (option) {
                case 1:
                    MenuUI debugCreate = new MenuUI("*DEBUG* Create");
                    debugCreate.addEntry("Create Doctor");
                    debugCreate.addEntry("Create Patient");
                    debugCreate.addEntry("Create Nurse");
                    debugCreate.addEntry("Create Administrative");
                    debugCreate.addEntry("Go back");
                    option2 = debugCreate.display();

                    switch (option2) {
                        case 1:
                            name = new BoxUI("What is the doctor name?").showAndGet();
                            birthday = new BoxUI("What is the doctor birthday? (YYYY-MM-DD)").showAndGet();
                            identification = Long.parseLong(new BoxUI("What is the doctor identification?").showAndGet());

                            format = new SimpleDateFormat("yyyy-MM-dd");
                            date = format.parse(birthday);

                            cal = new GregorianCalendar();
                            cal.setTime(date);

                            xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

                            try {
                                recordsClient.addIdentity("Doctor", name, identification, xmlGregCal);
                            } catch (BadAddIdentity_Exception e) {
                                new BoxUI(e.getMessage()).show();
                            }

                            break;

                        case 2:

                            name = new BoxUI("What is the patient name?").showAndGet();
                            birthday = new BoxUI("What is the patient birthday? (YYYY-MM-DD)").showAndGet();
                            identification = Long.parseLong(new BoxUI("What is the patient identification?").showAndGet());

                            format = new SimpleDateFormat("yyyy-MM-dd");
                            date = format.parse(birthday);

                            cal = new GregorianCalendar();
                            cal.setTime(date);

                            xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

                            try {
                                recordsClient.addIdentity("Patient", name, identification, xmlGregCal);
                            } catch (BadAddIdentity_Exception e) {
                                new BoxUI(e.getMessage()).show();
                            }

                            break;

                        case 3:

                            name = new BoxUI("What is the nurse name?").showAndGet();
                            birthday = new BoxUI("What is the nurse birthday? (YYYY-MM-DD)").showAndGet();
                            identification = Long.parseLong(new BoxUI("What is the nurse identification?").showAndGet());

                            format = new SimpleDateFormat("yyyy-MM-dd");
                            date = format.parse(birthday);

                            cal = new GregorianCalendar();
                            cal.setTime(date);

                            xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

                            try {
                                recordsClient.addIdentity("Nurse", name, identification, xmlGregCal);
                            } catch (BadAddIdentity_Exception e) {
                                new BoxUI(e.getMessage()).show();
                            }

                            break;

                        case 4:

                            name = new BoxUI("What is the administrative name?").showAndGet();
                            birthday = new BoxUI("What is the administrative birthday? (YYYY-MM-DD)").showAndGet();
                            identification = Long.parseLong(new BoxUI("What is the administrative identification?").showAndGet());

                            format = new SimpleDateFormat("yyyy-MM-dd");
                            date = format.parse(birthday);

                            cal = new GregorianCalendar();
                            cal.setTime(date);

                            xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

                            try {
                                recordsClient.addIdentity("Administrative", name, identification, xmlGregCal);
                            } catch (BadAddIdentity_Exception e) {
                                new BoxUI(e.getMessage()).show();
                            }

                            break;


                        case 5:
                            break;


                    }

                    break;

                case 2:
                    MenuUI debugFollowing = new MenuUI("*DEBUG* Following");
                    debugFollowing.addEntry("Doctor follows patient");
                    debugFollowing.addEntry("Nurse follows patient");
                    debugFollowing.addEntry("Go back");

                    option2 = debugFollowing.display();

                    switch (option2) {
                        case 1:
                            identification = Long.parseLong(new BoxUI("What is the doctor identification?").showAndGet());
                            identification2 = Long.parseLong(new BoxUI("What is the patient identification?").showAndGet());

                            try {
                                recordsClient.addRelation("Doctor", identification, identification2);
                            } catch (BadAddRelation_Exception e) {
                                new BoxUI(e.getMessage()).show();
                            }

                        break;

                        case 2:
                            identification = Long.parseLong(new BoxUI("What is the nurse identification?").showAndGet());
                            identification2 = Long.parseLong(new BoxUI("What is the patient identification?").showAndGet());

                            try {
                                recordsClient.addRelation("Nurse", identification, identification2);
                            } catch (BadAddRelation_Exception e) {
                                new BoxUI(e.getMessage()).show();
                            }

                            break;

                    }

                    break;


                case 3:

                    MenuUI readMenu = new MenuUI("Read Operations");
                    readMenu.addEntry("Report");
                    readMenu.addEntry("Exam");
                    readMenu.addEntry("Generic Information");
                    readMenu.addEntry("Medication");
                    readMenu.addEntry("Go back");
                    option2 = readMenu.display();

                    switch (option2) {
                        case 1:
                            identification2 = Long.parseLong(new BoxUI("What is the patient identification?").showAndGet());

                            try {
                                recordsClient.requestInformation("read", "Report", type, identification, identification2);
                            } catch (BadRequestInformation_Exception e) {
                                new BoxUI(e.getMessage()).show();
                            }
                            break;

                        case 2:
                            identification2 = Long.parseLong(new BoxUI("What is the patient identification?").showAndGet());

                            try {
                                recordsClient.requestInformation("read", "Exam", type, identification, identification2);
                            } catch (BadRequestInformation_Exception e) {
                                new BoxUI(e.getMessage()).show();
                            }
                            break;


                        case 3:
                            identification2 = Long.parseLong(new BoxUI("What is the patient identification?").showAndGet());

                            try {
                                recordsClient.requestInformation("read", "Generic", type, identification, identification2);
                            } catch (BadRequestInformation_Exception e) {
                                new BoxUI(e.getMessage()).show();
                            }
                            break;


                        case 4:
                            identification2 = Long.parseLong(new BoxUI("What is the patient identification?").showAndGet());

                            try {
                                recordsClient.requestInformation("read", "Medication", type, identification, identification2);
                            } catch (BadRequestInformation_Exception e) {
                                new BoxUI(e.getMessage()).show();
                            }
                            break;


                        case 5: break;
                    }

                    break;

                case 4:

                    MenuUI writeMenu = new MenuUI("Write Operations");
                    writeMenu.addEntry("Report");
                    writeMenu.addEntry("Exam");
                    writeMenu.addEntry("Generic Information");
                    writeMenu.addEntry("Medication");
                    writeMenu.addEntry("Go back");
                    option2 = writeMenu.display();

                    switch(option2) {
                        case 1:

                            //public String addReport(String myType, Long personalId, Long patientId, String speciality, String description, String digest)
                            identification2 = Long.parseLong(new BoxUI("What is the patient identification?").showAndGet());
                            speciality = new BoxUI("What is the speciality?").showAndGet();
                            description = new BoxUI("What is the description?").showAndGet();

                            dt = new DateTime();

                            format = new SimpleDateFormat("yyyy-MM-dd");
                            date = format.parse(dt.toString());

                            cal = new GregorianCalendar();
                            cal.setTime(date);

                            xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

                            ReportView reportView = new ReportView(identification, identification2, dt , speciality, description);

                            digest = CertificateHelper.createRecordDigest(new Long(identification).toString(), reportView);

                            recordsClient.addReport(type, identification, identification2, speciality, description, xmlGregCal, digest);

                            break;

                        case 2:

                            //public String addReport(String myType, Long personalId, Long patientId, String speciality, String description, String digest)
                            identification2 = Long.parseLong(new BoxUI("What is the patient identification?").showAndGet());
                            speciality = new BoxUI("What is the speciality?").showAndGet();
                            description = new BoxUI("What is the description?").showAndGet();
                            examName = new BoxUI("What is the exam name?").showAndGet();

                            dt = new DateTime();

                            format = new SimpleDateFormat("yyyy-MM-dd");
                            date = format.parse(dt.toString());

                            cal = new GregorianCalendar();
                            cal.setTime(date);

                            xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

                            ExamView examView = new ExamView(identification, identification2, dt, speciality, description, examName);

                            digest = CertificateHelper.createRecordDigest(new Long(identification).toString(), examView);

                            recordsClient.addExam(type, identification, identification2, speciality, description, xmlGregCal, digest, examName);

                            break;

                        case 3:

                            //public String addReport(String myType, Long personalId, Long patientId, String speciality, String description, String digest)
                            identification2 = Long.parseLong(new BoxUI("What is the patient identification?").showAndGet());
                            speciality = new BoxUI("What is the speciality?").showAndGet();
                            description = new BoxUI("What is the description?").showAndGet();

                            dt = new DateTime();

                            format = new SimpleDateFormat("yyyy-MM-dd");
                            date = format.parse(dt.toString());

                            cal = new GregorianCalendar();
                            cal.setTime(date);

                            xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

                            GenericInformationView genericView = new GenericInformationView(identification, identification2, dt, speciality, description);

                            digest = CertificateHelper.createRecordDigest(new Long(identification).toString(), genericView);

                            recordsClient.addGeneric(type, identification, identification2, speciality, description, xmlGregCal, digest);

                            break;

                        case 4:

                            //public String addReport(String myType, Long personalId, Long patientId, String speciality, String description, String digest)
                            identification2 = Long.parseLong(new BoxUI("What is the patient identification?").showAndGet());
                            speciality = new BoxUI("What is the speciality?").showAndGet();
                            description = new BoxUI("What is the description?").showAndGet();
                            drugName = new BoxUI("What is the drug name?").showAndGet();
                            dosage = Long.parseLong(new BoxUI("How much is the dosage?").showAndGet());

                            dt = new DateTime();

                            format = new SimpleDateFormat("yyyy-MM-dd");
                            date = format.parse(dt.toString());

                            cal = new GregorianCalendar();
                            cal.setTime(date);

                            xmlGregCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);

                            MedicationView medicationView = new MedicationView(identification, identification2, dt, speciality, description, drugName, dosage);

                            digest = CertificateHelper.createRecordDigest(new Long(identification).toString(), medicationView);

                            recordsClient.addMedication(type, identification, identification2, speciality, description, xmlGregCal, digest, drugName, dosage);

                            break;

                        case 5: break;

                    }

                    break;



            }
        }while (option != 5);

    }
}
