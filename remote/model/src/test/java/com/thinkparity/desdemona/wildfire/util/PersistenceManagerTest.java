/*
 * Created On:  2-Aug-07 1:56:55 PM
 */
package com.thinkparity.desdemona.wildfire.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.thinkparity.desdemona.DesdemonaTestCase;

/**
 * <b>Title:</b>thinkParity Desdemona Wildfire Util Persistence Manager Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PersistenceManagerTest extends DesdemonaTestCase {

    /** Test datum. */
    private Fixture datum;

    /**
     * Create PersistenceManagerTest.
     *
     */
    public PersistenceManagerTest() {
        super("Persistence manager test");
    }

    /**
     * Test obtain data source.
     * 
     */
    public void testGetDataSource() {
        final DataSource dataSource = datum.pm.getDataSource();
        assertNotNull("Data source is null.", dataSource);
    }

    /**
     * Test obtain connection.
     * 
     */
    public void testGetDataSourceConnection() {
        final DataSource dataSource = datum.pm.getDataSource();
        assertNotNull("Data source is null.", dataSource);
        try {
            final Connection connection = dataSource.getConnection();
            assertNotNull("Data source connection is null.", connection);
        } catch (final SQLException sqlx) {
            fail(sqlx, "Cannot obtain data source connection.");
        }
    }

    /**
     * Test obtain connection within a loop.
     * 
     */
    public void testGetDataSourceConnections() {
        final DataSource dataSource = datum.pm.getDataSource();
        assertNotNull("Data source is null.", dataSource);
        Connection connection;
        for (int i = 0; i < datum.maxConnections.length; i++) {
            for (int j = 0; j < datum.maxConnections[i]; j++) {
                try {
                    connection = dataSource.getConnection();
                    try {
                        assertNotNull("Data source connection [" + i + "," + j + "] is null.", connection);
                    } finally {
                        connection.close();
                    }
                } catch (final SQLException sqlx) {
                    fail(sqlx, "Cannot obtain data source connection [" + i + "," + j + "].");
                }
            }
        }
    }

    /**
     * @see com.thinkparity.desdemona.DesdemonaTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // persistence manager config
        System.setProperty("thinkparity.datasource-driver", "org.apache.derby.jdbc.ClientDriver");
        System.setProperty("thinkparity.datasource-password", "sa"); 
        System.setProperty("thinkparity.datasource-url", "jdbc:derby://localhost:9001/desdemona"); 
        System.setProperty("thinkparity.datasource-user", "sa");
        final int[] maxConnections = { 3, 10, 25, 100, 1000, 10000, 100000 }; 
        TEST_LOGGER.logInfo("thinkparity.datasource-driver:{0}", System.getProperty("thinkparity.datasource-driver"));
        TEST_LOGGER.logInfo("thinkparity.datasource-password:{0}", System.getProperty("thinkparity.datasource-password")); 
        TEST_LOGGER.logInfo("thinkparity.datasource-url:{0}", System.getProperty("thinkparity.datasource-url")); 
        TEST_LOGGER.logInfo("thinkparity.datasource-user:{0}", System.getProperty("thinkparity.datasource-user"));
        final StringBuilder buffer = new StringBuilder(maxConnections.length * 8);
        for (final int maxConnection : maxConnections) {
            if (0 < buffer.length()) {
                buffer.append(',');
            }
            buffer.append(maxConnection);
        }
        TEST_LOGGER.logInfo("maxConnections:{0}", maxConnections);
        datum = new Fixture(PersistenceManager.getInstance(), maxConnections);
        datum.pm.start();
    }

    /**
     * @see com.thinkparity.desdemona.DesdemonaTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum.pm.stop();
        datum = null;

        super.tearDown();
    }

    private class Fixture {

        private final int[] maxConnections;

        private final PersistenceManager pm;

        private Fixture(final PersistenceManager pm, final int[] maxConnections) {
            super();
            this.pm = pm;
            this.maxConnections = maxConnections;
        }
    }
}
