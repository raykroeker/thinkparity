/*
 * Created On:  2-Aug-07 1:56:55 PM
 */
package com.thinkparity.desdemona.service.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.thinkparity.desdemona.DesdemonaTestCase;

/**
 * <b>Title:</b>thinkParity Desdemona Wildfire Util Persistence Manager Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PersistenceServiceTest extends DesdemonaTestCase {

    /** Test datum. */
    private Fixture datum;

    /**
     * Create PersistenceManagerTest.
     *
     */
    public PersistenceServiceTest() {
        super("Persistence manager test");
    }

    /**
     * Test obtain data source.
     * 
     */
    public void testGetDataSource() {
        final DataSource dataSource = datum.ps.getDataSource();
        assertNotNull("Data source is null.", dataSource);
    }

    /**
     * Test obtain connection.
     * 
     */
    public void testGetDataSourceConnection() {
        final DataSource dataSource = datum.ps.getDataSource();
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
        final DataSource dataSource = datum.ps.getDataSource();
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
        System.setProperty("thinkparity.app.datasource-driver", "org.apache.derby.jdbc.ClientDriver");
        System.setProperty("thinkparity.app.datasource-password", "sa"); 
        System.setProperty("thinkparity.app.datasource-url", "jdbc:derby://localhost:9001/desdemona"); 
        System.setProperty("thinkparity.app.datasource-user", "sa");
        final int[] maxConnections = { 3, 10, 25, 100, 1000, 10000, 100000 }; 
        TEST_LOGGER.logInfo("thinkparity.app.datasource-driver:{0}", System.getProperty("thinkparity.app.datasource-driver"));
        TEST_LOGGER.logInfo("thinkparity.app.datasource-password:{0}", System.getProperty("thinkparity.app.datasource-password")); 
        TEST_LOGGER.logInfo("thinkparity.app.datasource-url:{0}", System.getProperty("thinkparity.app.datasource-url")); 
        TEST_LOGGER.logInfo("thinkparity.app.datasource-user:{0}", System.getProperty("thinkparity.app.datasource-user"));
        final StringBuilder buffer = new StringBuilder(maxConnections.length * 8);
        for (final int maxConnection : maxConnections) {
            if (0 < buffer.length()) {
                buffer.append(',');
            }
            buffer.append(maxConnection);
        }
        TEST_LOGGER.logInfo("maxConnections:{0}", maxConnections);
        datum = new Fixture(PersistenceService.getInstance(), maxConnections);
        final Properties properties = new Properties();
        final long sleep = Long.MAX_VALUE;
        properties.setProperty("thinkparity.persistence.derbyarchiversleep",
                String.valueOf(sleep));
        datum.ps.start(properties);
    }

    /**
     * @see com.thinkparity.desdemona.DesdemonaTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum.ps.stop();
        datum = null;

        super.tearDown();
    }

    private class Fixture {

        private final int[] maxConnections;

        private final PersistenceService ps;

        private Fixture(final PersistenceService ps, final int[] maxConnections) {
            super();
            this.ps = ps;
            this.maxConnections = maxConnections;
        }
    }
}
