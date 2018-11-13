package pt.ulisboa.tecnico.sirs.medicalrecords.personal;

import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;

//** Abstract class for RollBackTests in database */
public abstract class RollbackTestAbstractClass {
	@Before
	public void setUp() throws Exception {
		try {
			FenixFramework.getTransactionManager().begin(false);
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