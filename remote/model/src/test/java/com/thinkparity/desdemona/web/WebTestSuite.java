/*
 * Created On:  8-Sep-07 11:03:49 AM
 */
package com.thinkparity.desdemona.web;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <b>Title:</b>thinkParity Desdemona Web Test Suite<br>
 * <b>Description:</b>A test definition for dedemona web.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class WebTestSuite {

    /**
     * Obtain the test suite.
     * 
     * @return A <code>Test</code>.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(com.thinkparity.desdemona.model.contact.AcceptInvitationTest.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.profile.CreateTest.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.ticket.Ticket1008Test.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.ticket.Ticket1028Test.class);
        return suite;
    }

    /**
     * Create ModelTestSuite.
     *
     */
    public WebTestSuite() {
        super();
    }
}
