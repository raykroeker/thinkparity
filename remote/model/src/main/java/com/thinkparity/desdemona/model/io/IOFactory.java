/*
 * Created On:  7-Oct-07 12:33:44 PM
 */
package com.thinkparity.desdemona.model.io;

import javax.sql.DataSource;

import com.thinkparity.desdemona.model.io.sql.*;

/**
 * <b>Title:</b>thinkParity Desdemona Model IO Factory<br>
 * <b>Description:</b>An interface defined by the io service and used by the
 * model components.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface IOFactory {

    /**
     * Instantiate a contact io handler.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     * @return A <code>ContactSql</code>.
     */
    ContactSql newContactIO(final DataSource dataSource);

    /**
     * Instantiate a derby io handler.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     * @return A <code>DerbySql</code>.
     */
    DerbySql newDerbyIO(final DataSource dataSource);

    /**
     * Instantiate a message io handler.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     * @return A <code>MessageSql</code>.
     */
    MessageSql newMessageIO(final DataSource dataSource);

    /**
     * Instantate a payment io handler.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     * @return A <code>PaymentSql</code>.
     */
    PaymentSql newPaymentIO(final DataSource dataSource);

    /**
     * Instantiate a queue io handler.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     * @return A <code>QueueSql</code>.
     */
    QueueSql newQueueIO(final DataSource dataSource);

    /**
     * Instantiate a report io handler.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     * @return A <code>ReportSql</code>.
     */
    ReportSql newReportIO(final DataSource dataSource);

    /**
     * Instantiate a session io handler.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     * @return A <code>SessionSql</code>.
     */
    SessionSql newSessionIO(DataSource dataSource);

    /**
     * Instantiate a user io handler.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     * @return A <code>UserSql</code>.
     */
    UserSql newUserIO(final DataSource dataSource);
}
