/*
 * Created On: Jun 27, 2006 4:44:07 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.contact;

import java.util.List;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.ContactInvitation;

import com.thinkparity.ophelia.model.ModelTestCase;

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
    private static void assertNotNull(final String assertion,
            final Contact contact) {
        assertNotNull(assertion + " [CONTACT IS NULL]", (Object) contact);
        assertNotNull(assertion + " [CONTACT EMAIL IS NULL]", contact.getEmails());
        assertNotNull(assertion + " [CONTACT ID IS NULL]", contact.getId());
//        assertNotNull(assertion + " [CONTACT LOCAL ID IS NULL]", contact.getLocalId());
        assertNotNull(assertion + " [CONTACT NAME IS NULL]", contact.getName());
    }

    /**
     * Assert that the contact invitation nor its required members are null.
     * 
     * @param assertion
     *            The assertion.
     * @param invitation
     *            The invitation.
     */
    protected static void assertNotNull(final String assertion,
            final ContactInvitation invitation) {
        assertNotNull(assertion + " [CONTACT INVITATION IS NULL]", (Object) invitation);
        assertNotNull(assertion + " [CONTACT INVITATION CREATOR IS NULL]", invitation.getCreatedBy());
        assertNotNull(assertion + " [CONTACT INVITATION CREATION DATE IS NULL]", invitation.getCreatedOn());
        assertNotNull(assertion + " [CONTACT INVITATION ID IS NULL]", invitation.getId());
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
     * @see com.thinkparity.ophelia.model.ModelTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see com.thinkparity.ophelia.model.ModelTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
