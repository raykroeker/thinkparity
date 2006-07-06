/*
 * Created On: Feb 14, 2006
 */
package com.thinkparity.model.parity.model.document;

import com.thinkparity.model.parity.model.ModelTestCase;

/**
 * <b>Title:</b>thinkParity Document Test Case<br>
 * <b>Description:</b>An abstraction of a document test case. Within the setup
 * for this abstraction a user's session is established.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
abstract class DocumentTestCase extends ModelTestCase {

	/**
	 * Create DocumentTestCase.
	 * 
	 * @param name
	 *            The test name.
	 */
	protected DocumentTestCase(final String name) { super(name); }

    /**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
	 * 
	 */
    @Override
	protected void setUp() throws Exception {
		super.setUp();
		login();
	}

    /**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 * 
	 */
    @Override
	protected void tearDown() throws Exception {
		logout();
		super.tearDown();
	}
}
