/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.index;

import com.thinkparity.model.parity.model.ModelTestCase;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
abstract class IndexTestCase extends ModelTestCase {

	/**
	 * Create a IndexTestCase.
	 * 
	 * @param name
	 *            The test name.
	 */
	protected IndexTestCase(final String name) { super(name); }

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();

		login();
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		logout();

		super.tearDown();
	}
}
