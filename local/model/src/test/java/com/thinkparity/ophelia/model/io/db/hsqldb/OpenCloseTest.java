/*
 * Created On:  20-Jul-07 9:51:06 AM
 */
package com.thinkparity.ophelia.model.io.db.hsqldb;

import javax.sql.DataSource;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.model.ModelTestCase;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Ophelia Model IO Session Open/Close Test<br>
 * <b>Description:</b>Test the open/close of the database session.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class OpenCloseTest extends ModelTestCase {

    /** Test name. */
    private static final String NAME = "Open close test.";

    /** Test datum. */
    private Fixture datum;

    /** Logger. */
    private final Log4JWrapper logger;

    /**
     * Create OpenCloseTest.
     *
     */
    public OpenCloseTest() {
        super(NAME);
        this.logger = new Log4JWrapper(getClass());
    }

    /**
     * Test opening then closing a session.
     *
     */
    public void testOpenCloseSession() {
        logger.logApiId();
        logger.logTrace("Entry");

        Metrics.begin("OpenClose#openBaseline");
        final Session baseline = datum.sessionManager.openSession();
        Metrics.end("OpenClose#openBaseline");

        Metrics.begin("OpenClose#closeBaseline");
        baseline.close();
        Metrics.end("OpenClose#closeBaseline");

        final String openContext = "OpenClose#openIteration()";
        final String closeContext = "OpenClose#closeIteration()";
        for (int i = 0; i < Constants.ITERATIONS; i++) {
            Metrics.begin(openContext);
            final Session iteration = datum.sessionManager.openSession();
            Metrics.end(openContext);

            Metrics.begin(closeContext);
            iteration.close();
            Metrics.end(closeContext);
        }
        logger.logTrace("Exit");
    }

    /**
     * Test opening; running a query; then closing a session.
     *
     */
    public void testOpenQueryCloseSession() {
        logger.logApiId();
        logger.logTrace("Entry");

        Metrics.begin("OpenQueryClose#openBaseline");
        final Session baseline = datum.sessionManager.openSession();
        Metrics.end("OpenQueryClose#openBaseline");
        try {
            runQuery(baseline);
        } finally {
            Metrics.begin("OpenQueryClose#closeBaseline");
            baseline.close();
            Metrics.end("OpenQueryClose#closeBaseline");
        }

        final String openContext = "OpenQueryClose#openIteration()";
        final String closeContext = "OpenQueryClose#closeIteration()";
        for (int i = 0; i < Constants.ITERATIONS; i++) {
            Metrics.begin(openContext);
            final Session iteration = datum.sessionManager.openSession();
            Metrics.end(openContext);
            try {
                runQuery(iteration);
            } finally {
                Metrics.begin(closeContext);
                iteration.close();
                Metrics.end(closeContext);
            }
        }
        logger.logTrace("Exit");
    }

    /**
     * @see com.thinkparity.ophelia.model.ModelTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        datum = new Fixture(OpheliaTestUser.JUNIT);
    }

    /**
     * @see com.thinkparity.ophelia.model.ModelTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;

        super.tearDown();
    }

    /**
     * Run a query on the session.
     * 
     * @param session
     *            A <code>Session</code>.
     */
    private void runQuery(final Session session) {
        session.prepareStatement("select current_timestamp from PARITY_USER");
        session.executeQuery();
        while (session.nextResult()) {}
    }

    /** <b>Title:</b>Open/Close Test Fixture<br> */
    private class Fixture {

        /** A session manager. */
        private final SessionManager sessionManager;

        /**
         * Create Fixture.
         * 
         * @param testUser
         *            An <code>OpheliaTestUser</code>.
         */
        private Fixture(final OpheliaTestUser testUser) {
            super();
            final DataSource dataSource = testUser.getWorkspace().getDataSource();
            this.sessionManager = new SessionManager(dataSource);
        }
    }
}
