/*
 * Created On:  6-Oct-07 3:14:38 PM
 */
package com.thinkparity.desdemona.model.ticket;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b>thinkParity Desdemona Model Ticket 1080 Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Ticket1080Test extends TicketTestCase {

    /** Test name. */
    private static final String NAME = "Test ticket 1080.";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create Ticket1080Test.
     *
     */
    public Ticket1080Test() {
        super(NAME);
    }

    /**
     * Test publishing the welcome package on two separate threads.
     * 
     */
    public void testTicket1080() {
        TEST_LOGGER.logTraceId();
        TEST_LOGGER.logInfo("Test accepting/deleting a contact invitation simultaneously within two threads.");
        setUpTicket1080ForEMail();
        try {
            test(new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContainerModel(datum.publishAsOne).publishWelcome();
                }
            }, new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                @Override
                public void run() {
                    datum.getContainerModel(datum.publishAsTwo).publishWelcome();
                }
            });
        } finally {
            tearDownTicket1080();
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
    private void setUpTicket1080ForEMail() {
        final String publishAsOneUsername = datum.newUniqueUsername();
        datum.createProfile(publishAsOneUsername);
        datum.publishAsOne = datum.login(publishAsOneUsername);
        datum.verifyEMail(datum.publishAsOne);

        final String publishAsTwoUsername = datum.newUniqueUsername();
        datum.createProfile(publishAsTwoUsername);
        datum.publishAsTwo = datum.login(publishAsTwoUsername);
        datum.verifyEMail(datum.publishAsTwo);

        datum.completeOne = datum.completeTwo = Boolean.FALSE;
        datum.failOne = datum.failTwo = null;
    }

    /**
     * Tear down the test.
     * 
     */
    private void tearDownTicket1080() {
        datum.failOne = datum.failTwo = null;
        datum.completeOne = datum.completeTwo = Boolean.FALSE;
        datum.publishAsTwo = datum.publishAsTwo = null;
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
        }, "Ticket1080Test-One");
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
        }, "Ticket1080Test-Two");
        threadOne.start();
        threadTwo.start();
        synchronized (wait) {
            try {
                wait.wait();
                if (datum.completeOne && datum.completeTwo) {
                    if (null == datum.failOne) {
                        if (null == datum.failTwo) {
                        } else {
                            fail(datum.failTwo, "Could not publish welcome package.");
                        }
                    } else {
                        fail(datum.failOne, "Could not publish welcome package.");
                    }
                } else {
                    wait.wait();
                    if (null == datum.failOne) {
                        if (null == datum.failTwo) {
                        } else {
                            fail(datum.failTwo, "Could not publish welcome package.");
                        }
                    } else {
                        fail(datum.failOne, "Could not publish welcome package.");
                    }
                }
            } catch (final InterruptedException ix) {
                fail(ix, "Could not wait for the work to complete.");
            }
        }
    }

    /** <b>Title:</b>Ticket Test Fixture<br> */
    private class Fixture extends TicketTestCase.Fixture {
        private Boolean completeOne;
        private Boolean completeTwo;
        private Exception failOne;
        private Exception failTwo;
        private AuthToken publishAsOne;
        private AuthToken publishAsTwo;
    }
}
