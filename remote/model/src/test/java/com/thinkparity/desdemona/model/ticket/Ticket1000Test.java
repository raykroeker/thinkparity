/*
 * Created On:  6-Oct-07 3:14:38 PM
 */
package com.thinkparity.desdemona.model.ticket;

import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Desdemona Model Ticket 1000 Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Ticket1000Test extends TicketTestCase {

    private static final String NAME = "Test ticket ";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket1000Test.
     *
     */
    public Ticket1000Test() {
        super(NAME);
    }

    /**
     * Test accepting/deleting a contact invitation simultaneously within two threads.
     * 
     */
    public void testTicket1000AcceptDeleteEMail() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test accepting/deleting a contact invitation simultaneously within two threads.");
        setUpTicket1000ForEMail();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).acceptInvitation(
                            datum.incomingEMail, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).deleteInvitation(
                            datum.outgoingEMail, datum.now());
                }
            });
        } finally {
            tearDownTicket1000();
        }
    }

    /**
     * Test accepting/deleting a contact invitation simultaneously within two threads.
     * 
     */
    public void testTicket1000AcceptDeleteUser() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test accepting/deleting a contact user invitation simultaneously within two threads.");
        setUpTicket1000ForUser();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).acceptInvitation(
                            datum.incomingUser, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).deleteInvitation(
                            datum.outgoingUser, datum.now());
                }
            });
        } finally {
            tearDownTicket1000();
        }
    }
    /**
     * Test declining/deleting a contact invitation simultaneously within two threads.
     * 
     */
    public void testTicket1000DeclineDeleteEMail() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test declining/deleting a contact invitation simultaneously within two threads.");
        setUpTicket1000ForEMail();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).declineInvitation(
                            datum.incomingEMail, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).deleteInvitation(
                            datum.outgoingEMail, datum.now());
                }
            });
        } finally {
            tearDownTicket1000();
        }
    }

    /**
     * Test declining/deleting a contact user invitation simultaneously within two threads.
     * 
     */
    public void testTicket1000DeclineDeleteUser() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test declining/deleting a contact user invitation simultaneously within two threads.");
        setUpTicket1000ForUser();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).declineInvitation(
                            datum.incomingUser, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).deleteInvitation(
                            datum.outgoingUser, datum.now());
                }
            });
        } finally {
            tearDownTicket1000();
        }
    }

    /**
     * Test deleting/accepting a contact invitation simultaneously within two threads.
     * 
     */
    public void testTicket1000DeleteAcceptEMail() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test deleting/accepting a contact invitation simultaneously within two threads.");
        setUpTicket1000ForEMail();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).deleteInvitation(
                            datum.outgoingEMail, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).acceptInvitation(
                            datum.incomingEMail, datum.now());
                }
            });
        } finally {
            tearDownTicket1000();
        }
    }

    /**
     * Test deleting/accepting a contact user invitation simultaneously within two threads.
     * 
     */
    public void testTicket1000DeleteAcceptUser() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test deleting/accepting a contact user invitation simultaneously within two threads.");
        setUpTicket1000ForUser();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).deleteInvitation(
                            datum.outgoingUser, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).acceptInvitation(
                            datum.incomingUser, datum.now());
                }
            });
        } finally {
            tearDownTicket1000();
        }
    }

    /**
     * Test deleting/declining a contact invitation simultaneously within two threads.
     * 
     */
    public void testTicket1000DeleteDeclineEMail() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test deleting/declining a contact invitation simultaneously within two threads.");
        setUpTicket1000ForEMail();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).deleteInvitation(
                            datum.outgoingEMail, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).declineInvitation(
                            datum.incomingEMail, datum.now());
                }
            });
        } finally {
            tearDownTicket1000();
        }
    }

    /**
     * Test deleting/declining a contact invitation simultaneously within two threads.
     * 
     */
    public void testTicket1000DeleteDeclineUser() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test deleting/declining a contact user invitation simultaneously within two threads.");
        setUpTicket1000ForUser();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).deleteInvitation(
                            datum.outgoingUser, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).declineInvitation(
                            datum.incomingUser, datum.now());
                }
            });
        } finally {
            tearDownTicket1000();
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.ticket.TicketTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        datum = new Fixture();
    }

    /**
     * @see com.thinkparity.desdemona.model.ticket.TicketTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;

        super.tearDown();
    }
    /**
     * Set up the test.
     * 
     */
    private void setUpTicket1000ForEMail() {
        final String acceptAsOneUsername = datum.newUniqueUsername();
        final Profile acceptAsOneProfile = datum.createProfile(acceptAsOneUsername);
        datum.acceptAsOne = datum.login(acceptAsOneUsername);
        datum.verifyEMail(datum.acceptAsOne);

        final String acceptAsTwoUsername = datum.newUniqueUsername();
        datum.createProfile(acceptAsTwoUsername);
        datum.acceptAsTwo = datum.login(acceptAsTwoUsername);
        datum.verifyEMail(datum.acceptAsTwo);

        datum.outgoingEMail = new OutgoingEMailInvitation();
        datum.outgoingEMail.setCreatedBy(acceptAsOneProfile);
        datum.outgoingEMail.setCreatedOn(datum.now());
        datum.outgoingEMail.setInvitationEMail(datum.getProfileModel(datum.acceptAsTwo).readEMail().getEmail());
        datum.getContactModel(datum.acceptAsOne).createInvitation(datum.outgoingEMail);

        datum.incomingEMail = datum.getContactModel(datum.acceptAsTwo).readIncomingEMailInvitations().get(0);
        assertNotNull("Incoming e-mail invitation is null.", datum.incomingEMail);

        datum.completeOne = datum.completeTwo = Boolean.FALSE;
        datum.failOne = datum.failTwo = null;
    }

    /**
     * Set up the test.
     * 
     */
    private void setUpTicket1000ForUser() {
        final String acceptAsOneUsername = datum.newUniqueUsername();
        final Profile acceptAsOneProfile = datum.createProfile(acceptAsOneUsername);
        datum.acceptAsOne = datum.login(acceptAsOneUsername);
        datum.verifyEMail(datum.acceptAsOne);

        final String acceptAsTwoUsername = datum.newUniqueUsername();
        final Profile acceptAsTwoProfile = datum.createProfile(acceptAsTwoUsername);
        datum.acceptAsTwo = datum.login(acceptAsTwoUsername);
        datum.verifyEMail(datum.acceptAsTwo);

        datum.outgoingUser = new OutgoingUserInvitation();
        datum.outgoingUser.setCreatedBy(acceptAsOneProfile);
        datum.outgoingUser.setCreatedOn(datum.now());
        datum.outgoingUser.setInvitationUser(acceptAsTwoProfile);
        datum.getContactModel(datum.acceptAsOne).createInvitation(datum.outgoingUser);

        datum.incomingUser = datum.getContactModel(datum.acceptAsTwo).readIncomingUserInvitations().get(0);
        assertNotNull("Incoming user invitation is null.", datum.incomingUser);

        datum.completeOne = datum.completeTwo = Boolean.FALSE;
        datum.failOne = datum.failTwo = null;
    }

    /**
     * Tear down the test.
     * 
     */
    private void tearDownTicket1000() {
        datum.acceptAsOne = datum.acceptAsTwo = null;
        datum.incomingEMail = null;
        datum.incomingUser = null;
        datum.outgoingEMail = null;
        datum.outgoingUser = null;
    }

    /**
     * Test issuing two runnables simultaneously.
     * 
     * @param one
     *            A <code>Runnable</code>.
     * @param two
     *            A <code>Runnable</code>.
     */
    private void test(final Runnable one, final Runnable two) {
        final Object wait = new Object();
        final Thread threadOne = datum.newThread(new Runnable() {
            /**
             * @see java.lang.Runnable#run()
             *
             */
            @Override
            public void run() {
                TEST_LOGGER.logInfo("Start thread one.");
                try {
                    one.run();
                } catch (final Exception x) {
                    datum.failOne = x;
                } finally {
                    datum.completeOne = Boolean.TRUE;
                    synchronized (wait) {
                        wait.notify();
                    }
                }
            }
        }, "Ticket1000Test-One");
        final Thread threadTwo = datum.newThread(new Runnable() {
            /**
             * @see java.lang.Runnable#run()
             *
             */
            @Override
            public void run() {
                TEST_LOGGER.logInfo("Start thread two.");
                try {
                    two.run();
                } catch (final Exception x) {
                    datum.failTwo = x;
                } finally {
                    datum.completeTwo = Boolean.TRUE;
                    synchronized (wait) {
                        wait.notify();
                    }
                }
            }
        }, "Ticket1000Test-Two");
        threadOne.start();
        threadTwo.start();
        synchronized (wait) {
            try {
                wait.wait();
                if (datum.completeOne && datum.completeTwo) {
                    if (null == datum.failOne) {
                        if (null == datum.failTwo) {
                        } else {
                            fail(datum.failTwo, "Could not accept invitation.");
                        }
                    } else {
                        fail(datum.failOne, "Could not delete invitation.");
                    }
                } else {
                    wait.wait();
                    if (null == datum.failOne) {
                        if (null == datum.failTwo) {
                        } else {
                            fail(datum.failTwo, "Could not accept invitation.");
                        }
                    } else {
                        fail(datum.failOne, "Could not delete invitation.");
                    }
                }
            } catch (final InterruptedException ix) {
                fail(ix, "Could not wait for the work to complete.");
            }
        }
    }

    /** <b>Title:</b>Ticket Test Fixture<br> */
    private class Fixture extends TicketTestCase.Fixture {
        private AuthToken acceptAsOne;
        private AuthToken acceptAsTwo;
        private Boolean completeOne;
        private Boolean completeTwo;
        private Exception failOne;
        private Exception failTwo;
        private IncomingEMailInvitation incomingEMail;
        private IncomingUserInvitation incomingUser;
        private OutgoingEMailInvitation outgoingEMail;
        private OutgoingUserInvitation outgoingUser;
    }
}
