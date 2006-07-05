/*
 * Created On: Jun 27, 2006 4:44:07 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.contact;

import java.util.List;

import com.thinkparity.model.parity.model.ModelTestCase;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * <b>Title:</b>thinkParity Contact Model Test Case<br>
 * <b>Description:</b>A thinkParity contact model test case abstraction.<br>
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
abstract class ContactTestCase extends ModelTestCase {

    /**
     * Assert that the contact; nor its required members are null.
     * 
     * @param assertion
     *            The assertion.
     * @param contact
     *            The contact.
     */
    protected static void assertNotNull(final String assertion,
            final Contact contact) {
        assertNotNull(assertion + " [CONTACT IS NULL]", (Object) contact);
        assertNotNull(assertion + " [CONTACT EMAIL IS NULL]", contact.getEmail());
        assertNotNull(assertion + " [CONTACT ID IS NULL]", contact.getId());
    }

    /**
     * Assert that the list of contacts is not null.
     * 
     * @param assertion
     *            The assertion.
     * @param contacts
     *            The list of contacts.
     */
    protected static void assertNotNull(final String assertion,
            final List<Contact> contacts) {
        assertNotNull(assertion + " [CONTACTS IS NULL]", (Object) contacts);
        for(final Contact contact : contacts) {
            assertNotNull(assertion, contact);
        }
    }

    /**
     * Create ContactTestCase.
     * 
     * @param name
     *            The test case name.
     */
    protected ContactTestCase(final String name) { super(name); }

    /**
     * @see com.thinkparity.model.parity.model.ModelTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see com.thinkparity.model.parity.model.ModelTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
