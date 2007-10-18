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
public final class Ticket1000ComplexTest extends TicketTestCase {

    private static final String NAME = "Test ticket ";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket1000Test.
     *
     */
    public Ticket1000ComplexTest() {
        super(NAME);
    }

    /**
     * Test accepting/deleting a contact invitation simultaneously within two threads.
     * 
     */
    public void testTicket1000ComplexAcceptDeleteEMailOne() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test accepting/deleting a contact invitation simultaneously within two threads.");
        setUpTicket1000Complex();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).acceptInvitation(
                            datum.incomingEMailOne, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).deleteInvitation(
                            datum.outgoingEMailTwo, datum.now());
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
    public void testTicket1000ComplexAcceptDeleteEMailTwo() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test accepting/deleting a contact invitation simultaneously within two threads.");
        setUpTicket1000Complex();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).acceptInvitation(
                            datum.incomingEMailTwo, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).deleteInvitation(
                            datum.outgoingEMailOne, datum.now());
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
    public void testTicket1000ComplexAcceptDeleteUserOne() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test accepting/deleting a contact user invitation simultaneously within two threads.");
        setUpTicket1000Complex();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).acceptInvitation(
                            datum.incomingUserOne, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).deleteInvitation(
                            datum.outgoingUserTwo, datum.now());
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
    public void testTicket1000ComplexAcceptDeleteUserTwo() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test accepting/deleting a contact user invitation simultaneously within two threads.");
        setUpTicket1000Complex();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).acceptInvitation(
                            datum.incomingUserTwo, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).deleteInvitation(
                            datum.outgoingUserOne, datum.now());
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
    public void testTicket1000ComplexDeclineDeleteEMailOne() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test declining/deleting a contact invitation simultaneously within two threads.");
        setUpTicket1000Complex();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).declineInvitation(
                            datum.incomingEMailOne, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).deleteInvitation(
                            datum.outgoingEMailTwo, datum.now());
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
    public void testTicket1000ComplexDeclineDeleteEMailTwo() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test declining/deleting a contact invitation simultaneously within two threads.");
        setUpTicket1000Complex();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).declineInvitation(
                            datum.incomingEMailTwo, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).deleteInvitation(
                            datum.outgoingEMailOne, datum.now());
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
    public void testTicket1000ComplexDeclineDeleteUserOne() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test declining/deleting a contact user invitation simultaneously within two threads.");
        setUpTicket1000Complex();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).declineInvitation(
                            datum.incomingUserOne, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).deleteInvitation(
                            datum.outgoingUserTwo, datum.now());
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
    public void testTicket1000ComplexDeclineDeleteUserTwo() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test declining/deleting a contact user invitation simultaneously within two threads.");
        setUpTicket1000Complex();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).declineInvitation(
                            datum.incomingUserTwo, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).deleteInvitation(
                            datum.outgoingUserOne, datum.now());
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
    public void testTicket1000ComplexDeleteAcceptEMailOne() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test deleting/accepting a contact invitation simultaneously within two threads.");
        setUpTicket1000Complex();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).deleteInvitation(
                            datum.outgoingEMailOne, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).acceptInvitation(
                            datum.incomingEMailTwo, datum.now());
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
    public void testTicket1000ComplexDeleteAcceptEMailTwo() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test deleting/accepting a contact invitation simultaneously within two threads.");
        setUpTicket1000Complex();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).deleteInvitation(
                            datum.outgoingEMailTwo, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).acceptInvitation(
                            datum.incomingEMailOne, datum.now());
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
    public void testTicket1000ComplexDeleteAcceptUserOne() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test deleting/accepting a contact user invitation simultaneously within two threads.");
        setUpTicket1000Complex();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).deleteInvitation(
                            datum.outgoingUserOne, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).acceptInvitation(
                            datum.incomingUserTwo, datum.now());
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
    public void testTicket1000ComplexDeleteAcceptUserTwo() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test deleting/accepting a contact user invitation simultaneously within two threads.");
        setUpTicket1000Complex();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).deleteInvitation(
                            datum.outgoingUserTwo, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).acceptInvitation(
                            datum.incomingUserOne, datum.now());
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
    public void testTicket1000ComplexDeleteDeclineEMailOne() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test deleting/declining a contact invitation simultaneously within two threads.");
        setUpTicket1000Complex();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).deleteInvitation(
                            datum.outgoingEMailOne, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).declineInvitation(
                            datum.incomingEMailTwo, datum.now());
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
    public void testTicket1000ComplexDeleteDeclineEMailTwo() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test deleting/declining a contact invitation simultaneously within two threads.");
        setUpTicket1000Complex();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).deleteInvitation(
                            datum.outgoingEMailTwo, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).declineInvitation(
                            datum.incomingEMailOne, datum.now());
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
    public void testTicket1000ComplexDeleteDeclineUserOne() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test deleting/declining a contact user invitation simultaneously within two threads.");
        setUpTicket1000Complex();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).deleteInvitation(
                            datum.outgoingUserOne, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).declineInvitation(
                            datum.incomingUserTwo, datum.now());
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
    public void testTicket1000ComplexDeleteDeclineUserTwo() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test deleting/declining a contact user invitation simultaneously within two threads.");
        setUpTicket1000Complex();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsTwo).deleteInvitation(
                            datum.outgoingUserTwo, datum.now());
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContactModel(datum.acceptAsOne).declineInvitation(
                            datum.incomingUserOne, datum.now());
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
    private void setUpTicket1000Complex() {
        final String acceptAsOneUsername = datum.newUniqueUsername();
        final Profile acceptAsOneProfile = datum.createProfile(acceptAsOneUsername);
        datum.acceptAsOne = datum.login(acceptAsOneUsername);
        datum.verifyEMail(datum.acceptAsOne);

        final String acceptAsTwoUsername = datum.newUniqueUsername();
        final Profile acceptAsTwoProfile = datum.createProfile(acceptAsTwoUsername);
        datum.acceptAsTwo = datum.login(acceptAsTwoUsername);
        datum.verifyEMail(datum.acceptAsTwo);

        datum.outgoingEMailOne = new OutgoingEMailInvitation();
        datum.outgoingEMailOne.setCreatedBy(acceptAsOneProfile);
        datum.outgoingEMailOne.setCreatedOn(datum.now());
        datum.outgoingEMailOne.setInvitationEMail(datum.getProfileModel(datum.acceptAsTwo).readEMail().getEmail());
        datum.getContactModel(datum.acceptAsOne).createInvitation(datum.outgoingEMailOne);

        datum.incomingEMailTwo = datum.getContactModel(datum.acceptAsTwo).readIncomingEMailInvitations().get(0);
        assertNotNull("Incoming e-mail invitation is null.", datum.incomingEMailTwo);

        datum.outgoingEMailTwo = new OutgoingEMailInvitation();
        datum.outgoingEMailTwo.setCreatedBy(acceptAsTwoProfile);
        datum.outgoingEMailTwo.setCreatedOn(datum.now());
        datum.outgoingEMailTwo.setInvitationEMail(datum.getProfileModel(datum.acceptAsOne).readEMail().getEmail());
        datum.getContactModel(datum.acceptAsTwo).createInvitation(datum.outgoingEMailTwo);

        datum.incomingEMailOne = datum.getContactModel(datum.acceptAsOne).readIncomingEMailInvitations().get(0);
        assertNotNull("Incoming e-mail invitation is null.", datum.incomingEMailOne);

        datum.outgoingUserOne = new OutgoingUserInvitation();
        datum.outgoingUserOne.setCreatedBy(acceptAsOneProfile);
        datum.outgoingUserOne.setCreatedOn(datum.now());
        datum.outgoingUserOne.setInvitationUser(acceptAsTwoProfile);
        datum.getContactModel(datum.acceptAsOne).createInvitation(datum.outgoingUserOne);

        datum.incomingUserTwo = datum.getContactModel(datum.acceptAsTwo).readIncomingUserInvitations().get(0);
        assertNotNull("Incoming user invitation is null.", datum.incomingUserTwo);

        datum.outgoingUserTwo = new OutgoingUserInvitation();
        datum.outgoingUserTwo.setCreatedBy(acceptAsTwoProfile);
        datum.outgoingUserTwo.setCreatedOn(datum.now());
        datum.outgoingUserTwo.setInvitationUser(acceptAsOneProfile);
        datum.getContactModel(datum.acceptAsTwo).createInvitation(datum.outgoingUserTwo);

        datum.incomingUserOne = datum.getContactModel(datum.acceptAsOne).readIncomingUserInvitations().get(0);
        assertNotNull("Incoming user invitation is null.", datum.incomingUserOne);

        datum.completeOne = datum.completeTwo = Boolean.FALSE;
        datum.failOne = datum.failTwo = null;
    }

    /**
     * Tear down the test.
     * 
     */
    private void tearDownTicket1000() {
        datum.acceptAsOne = datum.acceptAsTwo = null;
        datum.incomingEMailOne = null;
        datum.incomingEMailTwo = null;        
        datum.incomingUserOne = null;
        datum.incomingUserTwo = null;
        datum.outgoingEMailOne = null;
        datum.outgoingEMailTwo = null;
        datum.outgoingUserOne = null;
        datum.outgoingUserTwo = null;
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
        }, "Ticket1000ComplexTest-One");
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
        }, "Ticket1000ComplexTest-Two");
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
        private IncomingEMailInvitation incomingEMailOne;
        private IncomingEMailInvitation incomingEMailTwo;
        private IncomingUserInvitation incomingUserOne;
        private IncomingUserInvitation incomingUserTwo;
        private OutgoingEMailInvitation outgoingEMailOne;
        private OutgoingEMailInvitation outgoingEMailTwo;
        private OutgoingUserInvitation outgoingUserOne;
        private OutgoingUserInvitation outgoingUserTwo;
    }
}
