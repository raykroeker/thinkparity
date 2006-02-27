/*
 * Feb 25, 2006
 */
package com.thinkparity.model.parity.model.system.message;

import com.thinkparity.model.parity.model.ModelTestCase;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
abstract class SystemMessageTestCase extends ModelTestCase {

	private SystemMessageModel systemMessageModel;

	/**
	 * Create a SystemMessageTestCase.
	 * 
	 * @param name
	 *            The test name.
	 */
	protected SystemMessageTestCase(final String name) { super(name); }

	protected SystemMessageModel getSystemMessageModel() {
		if(null == systemMessageModel) {
			systemMessageModel = SystemMessageModel.getModel();
		}
		return systemMessageModel;
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception { super.setUp(); }

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception { super.tearDown(); }
}
