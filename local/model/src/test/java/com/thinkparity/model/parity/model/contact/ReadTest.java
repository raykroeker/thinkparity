/*
 * Created On: Nov 10, 2005
 * $Id$
 */
package com.thinkparity.model.parity.model.contact;

import java.util.List;

import com.thinkparity.model.xmpp.contact.Contact;

/**
 * Test the session model getRosterEntries api.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReadTest extends ContactTestCase {

    /** Test name. */
    private static final String NAME = "[TEST READ]";

	/** Test datum. */
	private Fixture datum;

	/** Create ReadTest. */
	public ReadTest() { super(NAME); }

	/**
	 * Test the session model getRosterEntries api.
	 */
	public void testRead() {
		final List<Contact> contacts = datum.cModel.read();

		assertNotNull(NAME, contacts);
		assertEquals(NAME + " [CONTACTS SIZE DOES NOT MATCH EXPECTATION]",
                datum.eSize.intValue(), contacts.size());
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		login();
        final ContactModel cModel = getContactModel();
		datum = new Fixture(cModel, 3);
	}

	/**
	 * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		datum = null;
		logout();
        super.tearDown();
	}

	/** Test datum definition. */
	private class Fixture {
		private final ContactModel cModel;
		private final Integer eSize;
		private Fixture(final ContactModel cModel, final Integer eSize) {
			this.cModel = cModel;
			this.eSize = eSize;
		}
	}
}
