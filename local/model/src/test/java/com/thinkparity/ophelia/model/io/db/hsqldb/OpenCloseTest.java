/*
 * Created On:  20-Jul-07 9:51:06 AM
 */
package com.thinkparity.ophelia.model.io.db.hsqldb;

import java.util.ArrayList;
import java.util.List;

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

    /** Number of iterations to open/close the session. */
    private static final int ITERATIONS = 10000;

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
        long now;

        now = System.currentTimeMillis();
        final Session session = datum.sessionManager.openSession();
        datum.baseline.openDuration = System.currentTimeMillis() - now;

        now = System.currentTimeMillis();
        session.close();
        datum.baseline.closeDuration = System.currentTimeMillis() - now;

        for (int i = 0; i < ITERATIONS; i++) {
            now = System.currentTimeMillis();
            final Session session2 = datum.sessionManager.openSession();
            datum.metrics.get(i).openDuration = System.currentTimeMillis() - now;

            now = System.currentTimeMillis();
            session2.close();
            datum.metrics.get(i).closeDuration = System.currentTimeMillis() - now;
        }
        logMetrics("OpenClose");
        logger.logTrace("Exit");
    }

    /**
     * Test opening; running a query; then closing a session.
     *
     */
    public void testOpenQueryCloseSession() {
        logger.logApiId();
        logger.logTrace("Entry");
        long now;

        now = System.currentTimeMillis();
        final Session session = datum.sessionManager.openSession();
        datum.baseline.openDuration = System.currentTimeMillis() - now;
        try {
            runQuery(session);
        } finally {
            now = System.currentTimeMillis();
            session.close();
            datum.baseline.closeDuration = System.currentTimeMillis() - now;
        }

        for (int i = 0; i < ITERATIONS; i++) {
            now = System.currentTimeMillis();
            final Session session2 = datum.sessionManager.openSession();
            datum.metrics.get(i).openDuration = System.currentTimeMillis() - now;
            try {
                runQuery(session2);
            } finally {
                now = System.currentTimeMillis();
                session2.close();
                datum.metrics.get(i).closeDuration = System.currentTimeMillis() - now;
            }
        }
        logMetrics("OpenQueryClose");
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
     * Log the collected metrics.
     * 
     * @param name
     *            A log name.
     */
    private void logMetrics(final String name) {
        logger.logDebug("{0};Baseline;Open:{1}ms;Close:{2}ms",
                name, datum.baseline.openDuration, datum.baseline.closeDuration);
        for (int i = 0; i < ITERATIONS; i++) {
            logger.logDebug("{0};Iteration_{1};Open:{2}ms;Close:{3}ms", name, i,
                    datum.metrics.get(i).openDuration, datum.metrics.get(i).closeDuration);
        }
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

        /** A baseline metric. */
        private final Metric baseline;

        /** The iteration metrics. */
        private final List<Metric> metrics;

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

            this.baseline = new Metric();
            this.metrics = new ArrayList<Metric>(ITERATIONS);
            for (int i = 0; i < ITERATIONS; i++) {
                metrics.add(new Metric());
            }
        }
    }

    /** <b>Title:</b>Open/Close Test Metric<br> */
    private class Metric {

        /** Duration of close session. */
        private long closeDuration;

        /** Duration of open session. */
        private long openDuration;
    }
}
