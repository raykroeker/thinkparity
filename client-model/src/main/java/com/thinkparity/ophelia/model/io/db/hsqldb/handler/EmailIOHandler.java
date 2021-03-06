/*
 * Created On: Jul 17, 2006 9:26:33 PM
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import javax.sql.DataSource;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;


/**
 * @author raymond@thinkparity.com
 * @version
 */
class EmailIOHandler extends AbstractIOHandler {

    /** Sql to create an e-mail address. */
    private static final String SQL_CREATE =
            new StringBuffer("insert into EMAIL ")
            .append("(EMAIL) ")
            .append("values (?)")
            .toString();

    /** Sql to delete an e-mail address. */
    private static final String SQL_DELETE =
            new StringBuffer("delete from EMAIL ")
            .append("where EMAIL_ID=?")
            .toString();

    /** Sql to read an e-mail count by its unique key. */
    private static final String SQL_READ_COUNT_UK =
        new StringBuilder("select COUNT(E.EMAIL_ID) \"EMAIL_COUNT\" ")
        .append("from EMAIL E ")
        .append("where E.EMAIL=?")
        .toString();

    /** Sql to read an e-mail id from an e-mail address. */
    private static final String SQL_READ_ID =
            new StringBuffer("select EMAIL_ID ")
            .append("from EMAIL E ")
            .append("where E.EMAIL=?")
            .toString();

    /**
     * Create EmailIOHandler.
     * 
     * @param dataSource
     *            An sql <code>DataSource</code>.
     */
    EmailIOHandler(final DataSource dataSource) {
        super(dataSource);
    }

    /**
     * Create an e-mail address.
     * 
     * @param session
     *            The database session.
     * @param email
     *            The e-mail address.
     * @return The e-mail id.
     */
    Long create(final Session session, final EMail email) {
        session.prepareStatement(SQL_CREATE);
        session.setEMail(1, email);
        if(1 != session.executeUpdate())
            throw new HypersonicException("Could not create email.");
        return session.getIdentity("EMAIL");
    }

    void delete(final Session session, final Long emailId) {
        session.prepareStatement(SQL_DELETE);
        session.setLong(1, emailId);
        if(1 != session.executeUpdate())
            throw new HypersonicException("Could not delete email.");
    }

    Boolean doesExist(final Session session, final EMail email) {
        session.prepareStatement(SQL_READ_COUNT_UK);
        session.setEMail(1, email);
        session.executeQuery();
        session.nextResult();
        if (1 == session.getInteger("EMAIL_COUNT")) {
            return Boolean.TRUE;
        } else if (0 == session.getInteger("EMAIL_COUNT")) {
            return Boolean.FALSE;
        } else {
            throw new HypersonicException("Could not determine e-mail count.");
        }
    }

    /**
     * Extract an <code>EMail</code> from the <code>Session</code>.
     * 
     * @param session
     *            A database <code>Session</code>.
     * @return An <code>Email</code>.
     */
    EMail extractEMail(final Session session) {
        return session.getEMail("EMAIL");
    }

    Long readId(final EMail email) {
        final Session session = openSession();
        try {
            return readId(session, email);
        } finally {
            session.close();
        }
    }

    Long readId(final Session session, final EMail email) {
        session.prepareStatement(SQL_READ_ID);
        session.setEMail(1, email);
        session.executeQuery();
        if (session.nextResult()) {
            return session.getLong("EMAIL_ID");
        } else {
            return null;
        }
    }

    Long readLazyCreate(final Session session, final EMail email ){
        if (!doesExist(session, email)) {
            return create(session, email);
        } else {
            return readId(session, email);
        }
    }
}
