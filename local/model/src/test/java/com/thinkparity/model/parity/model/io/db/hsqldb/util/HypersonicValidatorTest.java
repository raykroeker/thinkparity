/*
 * Feb 8, 2006
 */
package com.thinkparity.model.parity.model.io.db.hsqldb.util;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.parity.model.ModelTestCase;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HypersonicValidatorTest extends ModelTestCase {

    /** The test name. */
    private static final String NAME = "[LMODEL] [IO] [HSQLDB] [TEST VALIDATE]";

    /** The test data. */
	private List<Fixture> data;

	/**
	 * Create a HypersonicValidatorTest.
	 * 
	 */
	public HypersonicValidatorTest() { super(NAME); }

    /**
     * Test the validate api.
     *
     */
	public void testValidate() {
		try {
			for(final Fixture datum : data) {
				datum.databaseValidator.validate();
			}
		}
		catch(final Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new LinkedList<Fixture>();
		data.add(new Fixture(new HypersonicValidator(getWorkspace())));
		data.add(new Fixture(new HypersonicValidator(getWorkspace())));
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		data.clear();
		data = null;
	}

    /** Test data definition. */
	private class Fixture {
		private final HypersonicValidator databaseValidator;
		private Fixture(final HypersonicValidator databaseValidator) {
			super();
			this.databaseValidator = databaseValidator;
		}
	}
}
