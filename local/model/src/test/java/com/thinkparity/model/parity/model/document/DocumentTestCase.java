/*
 * Feb 14, 2006
 */
package com.thinkparity.model.parity.model.document;

import com.thinkparity.model.parity.model.ModelTestCase;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class DocumentTestCase extends ModelTestCase {

	/**
	 * Create a DocumentTestCase.
	 * 
	 * @param name
	 *            The test name.
	 */
	public DocumentTestCase(final String name) { super(name); }

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
