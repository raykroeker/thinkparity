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
 * <b>Title:</b>thinkParity Ophelia Model IO Session Open/Close Concurrent Test<br>
 * <b>Description:</b>Test the open/close of the database session when the
 * maximum number of concurrent sessions are open.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class OpenCloseConcurrentTest extends ModelTestCase {

    /** Test name. */
    private static final String NAME = "Open close concurrent test.";

    /** Test datum. */
    private Fixture datum;

    /** Logger. */
    private final Log4JWrapper logger;

    /**
     * Create OpenCloseConcurrentTest.
     *
     */
    public OpenCloseConcurrentTest() {
        super(NAME);
        this.logger = new Log4JWrapper(getClass());
    }

    /**
     * Test opening then closing a session with a number of already open
     * sessions.
     * 
     */
    public void testOpenCloseSession() {
        logger.logApiId();
        logger.logTrace("Entry");

        Metrics.begin("OpenClose#openBaseline");
        final Session session = datum.sessionManager.openSession();
        Metrics.end("OpenClose#openBaseline");

        Metrics.begin("OpenClose#closeBaseline");
        session.close();
        Metrics.end("OpenClose#closeBaseline");

        openConcurrent();
        try {
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
        } finally {
            closeConcurrent();
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

        openConcurrent();
        try {
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
        } finally {
            closeConcurrent();
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
     * Close all concurrent sessions.
     *
     */
    private void closeConcurrent() {
        for (int i = 0; i < Constants.CONCURRENT; i++) {
            datum.concurrent.get(i).close();
        }
    }

    /**
     * Open all concurrent sessions.
     *
     */
    private void openConcurrent() {
        for (int i = 0; i < Constants.CONCURRENT; i++) {
            datum.concurrent.add(datum.sessionManager.openSession());
        }
        logger.logDebug("{0} open connections.", datum.concurrent.size());
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

    /** <b>Title:</b>Open/Close Concurrent Fixture<br> */
    private class Fixture {

        /** A list of open/concurrent sessions. */
        private final List<Session> concurrent;

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

            this.concurrent = new ArrayList<Session>(Constants.CONCURRENT);
        }
    }
}
