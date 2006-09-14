/*
 * Created On: Jul 17, 2006 9:26:33 PM
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;


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

    /** Sql to read an e-mail id from an e-mail address. */
    private static final String SQL_READ_ID =
            new StringBuffer("select EMAIL_ID ")
            .append("from EMAIL E ")
            .append("where E.EMAIL=?")
            .toString();

    private static StringBuffer getApiId(final String api) {
        return getIOId("[EMAIL]").append(" ").append(api);
    }

    private static String getErrorId(final String api, final String error) {
        return getApiId(api).append(" ").append(error).toString();
    }

    /**
     * Create EmailIOHandler.
     * 
     * @param sessionManager
     *            A hypersonic <code>SessionManager</code>.
     */
    EmailIOHandler(final SessionManager sessionManager) {
        super(sessionManager);
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
            throw new HypersonicException(getErrorId("CREATE", "COULD NOT CREATE E-MAIL"));
        return session.getIdentity();
    }

    void delete(final Session session, final Long emailId) {
        session.prepareStatement(SQL_DELETE);
        session.setLong(1, emailId);
        if(1 != session.executeUpdate())
            throw new HypersonicException(getErrorId("DELETE", "COULD NOT DELETE E-MAIL"));
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
}
