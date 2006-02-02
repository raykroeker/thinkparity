/*
 * Nov 10, 2005
 */
package com.thinkparity.model.parity.model.project;

import com.thinkparity.model.ModelTestCase;

/**
 * Test the project model getInbox api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class GetInboxTest extends ModelTestCase {

	/**
	 * Create a GetInboxTest.
	 */
	public GetInboxTest() { super("testGetInbox"); }

	/**
	 * Test the project model getInbox api.
	 */
	public void testGetInbox() {
		try {
			final Project inbox = getProjectModel().getInbox();
			assertNotNull(inbox);
		}
		catch(Throwable t) { fail(createFailMessage(t)); }
	}
}
