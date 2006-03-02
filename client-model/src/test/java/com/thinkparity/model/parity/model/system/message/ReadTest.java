/*
 * Feb 25, 2006
 */
package com.thinkparity.model.parity.model.system.message;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.parity.model.message.system.SystemMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReadTest extends SystemMessageTestCase {

	private List<Fixture> data;

	/**
	 * Create a ReadTest.
	 * 
	 */
	public ReadTest() { super("Read Test"); }

	public void testRead() {
		try {
			Collection<SystemMessage> systemMessages;
			for(final Fixture datum : data) {
				systemMessages = datum.systemMessageModel.read();

				assertNotNull("System message list is null.", systemMessages);
			}
		}
		catch(final Throwable t) { fail(createFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.parity.model.system.message.SystemMessageTestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		data = new LinkedList<Fixture>();
		final SystemMessageModel systemMessageModel = getSystemMessageModel();

		data.add(new Fixture(systemMessageModel));
	}

	/**
	 * @see com.thinkparity.model.parity.model.system.message.SystemMessageTestCase#tearDown()
	 * 
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private class Fixture {
		private final SystemMessageModel systemMessageModel;
		private Fixture(final SystemMessageModel systemMessageModel) {
			this.systemMessageModel = systemMessageModel;
		}
	}
}
