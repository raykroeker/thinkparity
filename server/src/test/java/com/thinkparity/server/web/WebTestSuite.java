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
        suite.addTestSuite(com.thinkparity.desdemona.model.admin.derby.ArchiveTest.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.admin.report.GenerateTest.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.admin.message.EnqueueTest.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.contact.CreateInvitationTest.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.contact.AcceptInvitationTest.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.contact.DeclineInvitationTest.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.contact.DeleteInvitationTest.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.profile.CreateTest.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.profile.UpdatePasswordTest.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.profile.VerifyEMailTest.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.session.LoginTest.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.session.ReadConfigurationTest.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.session.ReadCustomConfigurationTest.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.ticket.Ticket1000Test.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.ticket.Ticket1000ComplexTest.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.ticket.Ticket1008Test.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.ticket.Ticket1028Test.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.ticket.Ticket1067Test.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.ticket.Ticket1080Test.class);
        suite.addTestSuite(com.thinkparity.desdemona.model.ticket.Ticket1222Test.class);
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
