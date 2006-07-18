/*
 * Created On: Jul 17, 2006 9:26:33 PM
 */
package com.thinkparity.model.parity.model.io.db.hsqldb.handler;

import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.model.parity.model.io.db.hsqldb.Session;


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

    private static StringBuffer getApiId(final String api) {
        return getIOId("[EMAIL]").append(" ").append(api);
    }

    private static String getErrorId(final String api, final String error) {
        return getApiId(api).append(" ").append(error).toString();
    }

    /** Create EmailIOHandler. */
    EmailIOHandler() { super(); }

    /**
     * Create an e-mail address.
     * 
     * @param session
     *            The database session.
     * @param email
     *            The e-mail address.
     * @return The e-mail id.
     */
    public Long create(final Session session, final String email) {
        session.prepareStatement(SQL_CREATE);
        session.setString(1, email);
        if(1 != session.executeUpdate())
            throw new HypersonicException(getErrorId("[CREATE]", "[COULD NOT CREATE E-MAIL"));
        return session.getIdentity();
    }
}
