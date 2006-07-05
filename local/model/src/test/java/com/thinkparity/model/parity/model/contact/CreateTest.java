/*
 * Created On: Jun 27, 2006 4:45:55 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.contact;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * <b>Title:</b>thinkParity Contact Create Test<br>
 * <b>Description:</b>A test of the contact's create api.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class CreateTest extends ContactTestCase {

    /** The test name. */
    private static final String NAME = "[LMODEL] [CONTACT] [CREATE TEST]";

    /** Test datum. */
    private Fixture datum;

    /** Create CreateTest. */
    public CreateTest() { super(NAME); }

    /**
     * Test the create api.
     *
     */
    public void testCreate() {
        final Contact contact = datum.cModel.create(datum.emailAddress);
        assertNotNull(NAME, contact);
    }

    /**
     * @see com.thinkparity.model.parity.model.contact.ContactTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        final ContactModel cModel = getContactModel();

        login();
        datum = new Fixture(cModel, ModelTestUser.getX().getEmailAddress());
    }

    /**
     * @see com.thinkparity.model.parity.model.contact.ContactTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        datum = null;
        logout();
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture {
        private final ContactModel cModel;
        private final String emailAddress;
        private Fixture(final ContactModel cModel, final String emailAddress) {
            this.cModel = cModel;
            this.emailAddress = emailAddress;
        }
    }
}
