/*
 * Created On:  9-Oct-07 1:01:07 PM
 */
package com.thinkparity.desdemona.model.io.sql;

import java.io.File;

import javax.sql.DataSource;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DerbySql extends AbstractSql {

    /** DERBY - DerbySql#SQL_BACKUP - Sql to call the backup stored procedure. */
    private static final String SQL_BACKUP =
        new StringBuilder("call syscs_util.syscs_backup_database(?)")
        .toString();

    /**
     * Create DerbySql.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     */
    public DerbySql(final DataSource dataSource) {
        super(dataSource, Boolean.TRUE);
    }

    /**
     * Backup the database to the backup root.
     * 
     * @param backupRoot
     *            A <code>File</code>.
     */
    public void backup(final File backupRoot) {
        final HypersonicSession session = openSession();
        try {
            session.prepareCall(SQL_BACKUP);
            session.setCallableString(1, backupRoot.getAbsolutePath());
            session.executeCall();
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }
}
