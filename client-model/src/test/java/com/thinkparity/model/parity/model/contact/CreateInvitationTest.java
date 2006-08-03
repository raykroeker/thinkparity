/*
 * Created On: Jun 27, 2006 4:45:55 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.contact;


/**
 * <b>Title:</b>thinkParity Contact Create Test<br>
 * <b>Description:</b>A test of the contact's create api.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class CreateInvitationTest extends ContactTestCase {

    /** The test name. */
    private static final String NAME = "[LMODEL] [CONTACT] [CREATE INVITATION TEST]";

    /** Test datum. */
    private Fixture datum;

    /** Create CreateTest. */
    public CreateInvitationTest() { super(NAME); }

    /**
     * Test the create api.
     *
     */
    public void testCreateInvitation() {
        final ContactInvitation invitation = datum.cModel.createInvitation(datum.emailAddress);
        assertNotNull(NAME, invitation);
    }

    /**
     * @see com.thinkparity.model.parity.model.contact.ContactTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        login();
        final ContactModel cModel = getContactModel();
        final String randomEmailAddress = "raykroeker@gmail.com";
                //System.currentTimeMillis() + jUnitX.getEmailAddress();
        datum = new Fixture(cModel, randomEmailAddress);
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
