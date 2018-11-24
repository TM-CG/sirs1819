package pt.ulisboa.tecnico.sirs.medicalrecords.personal;

import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.sirs.mdrecords.personal.SNS;

//** Abstract class for RollBackTests in database */
public abstract class RollbackTestAbstractClass {
	@Before
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void setUp() throws Exception {
		try {
			FenixFramework.getTransactionManager().begin(false);
			SNS sns = SNS.getInstance();
			populate4Test();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		try {
			FenixFramework.getTransactionManager().rollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract void populate4Test();

}