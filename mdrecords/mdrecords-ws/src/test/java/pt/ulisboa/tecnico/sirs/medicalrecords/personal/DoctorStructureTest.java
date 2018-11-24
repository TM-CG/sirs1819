package pt.ulisboa.tecnico.sirs.medicalrecords.personal;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.Doctor;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.exception.InvalidPersonException;

public class DoctorStructureTest extends RollbackTestAbstractClass {
    private Doctor doctor;

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
    private DateTime dt = formatter.parseDateTime("01/01/2000");

    public void populate4Test() {

    }

    /*@Test
    public void simpleDoctorSuccessTest() throws InvalidPersonException {
        this.doctor = new Doctor("Vítor Nunes", 12345678);

        Assert.assertEquals("Vítor Nunes", this.doctor.getName());
        Assert.assertEquals(12345678, this.doctor.getIdentification());
    }

    @Test
    public void simpleDoctorWithBirthdaySuccessTest() throws InvalidPersonException {
        this.doctor = new Doctor("Vítor Nunes", dt,12345678);

        Assert.assertEquals("Vítor Nunes", this.doctor.getName());
        Assert.assertEquals(12345678, this.doctor.getIdentification());

        Assert.assertEquals("01/01/2000", this.doctor.getBirthday().toString("dd/MM/yyyy"));
    }*/
}
