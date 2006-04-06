/*
 * Apr 5, 2006
 */
package com.thinkparity.model.xmpp;

import java.util.HashSet;
import java.util.Set;

import com.thinkparity.model.ModelTestUser;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AcceptInvitationTest extends XMPPTestCase {

    private Set<Fixture> data;

    /**
     * Create a AcceptInvitationTest.
     * 
     */
    public AcceptInvitationTest() { super("AcceptInvitationTest"); }

    /**
     * Test the xmpp session accept invitation api.
     *
     */
    public void testAcceptInvitation() {
//        for(final Fixture datum : data) {
//            try { datum.xmppSession.acceptInvitation(datum.jabberId); }
//            catch(final SmackException sx) {
//                fail(createFailMessage(sx));
//            }
//        }
    }

    /**
     * @see com.thinkparity.model.xmpp.XMPPTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        data = new HashSet<Fixture>();

        final JabberId jabberId = ModelTestUser.getJUnitBuddy0().getJabberId();

        data.add(new Fixture(jabberId, getSession()));
    }

    /**
     * @see com.thinkparity.model.xmpp.XMPPTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        data.clear();
        data = null;
    }

    private class Fixture {
        private final JabberId jabberId;
        private final XMPPSession xmppSession;
        private Fixture(final JabberId jabberId, final XMPPSession xmppSession) {
            this.jabberId = jabberId;
            this.xmppSession = xmppSession;
        }
    }
}
