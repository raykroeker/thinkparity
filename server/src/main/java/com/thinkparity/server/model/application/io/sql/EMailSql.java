/*
 * Created On:  9-Mar-07 3:27:59 PM
 */
package com.thinkparity.desdemona.model.io.sql;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Email SQL Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class EMailSql extends AbstractSql {

    /** Sql to create an e-mail address. */
    private static final String SQL_CREATE_EMAIL =
        new StringBuilder("insert into TPSD_EMAIL ")
        .append("(EMAIL) ")
        .append("values (?)")
        .toString();

    /** Sql to read an e-mail address id. */
    private static final String SQL_READ_EMAIL_ID =
        new StringBuilder("select E.EMAIL_ID ")
        .append("from TPSD_EMAIL E ")
        .append("where E.EMAIL=?")
        .toString();

    /**
     * Create EmailSql.
     *
     */
    EMailSql() {
        super();
    }

    /**
     * Lazily create an e-mail address. If the e-mail exists return the id
     * otherwise create the e-mail and return the id.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param email
     *            An <code>EMail</code>.
     * @return An e-mail address id <code>Long</code>.
     */
    Long readLazyCreate(final HypersonicSession session, final EMail email) {
        final Long emailId = readEMailId(session, email);
        if (null == emailId) {
            return createEMail(session, email);
        } else {
            return emailId;
        }
    }

    /**
     * Create e-mail address.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param email
     *            An <code>EMail</code>.
     * @return An e-mail address id <code>Long</code>.
     */
    private Long createEMail(final HypersonicSession session, final EMail email) {
        session.prepareStatement(SQL_CREATE_EMAIL);
        session.setString(1, email.toString());
        if (1 != session.executeUpdate()) 
            throw panic("Could not create e-mail address {0}.", email);

        return session.getIdentity("TPSD_EMAIL");
    }

    /**
     * Read an e-mail address id.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param email
     *            An <code>EMail</code>.
     * @return An e-mail address id <code>Long</code>.
     */
    private Long readEMailId(final HypersonicSession session, final EMail email) {
        session.prepareStatement(SQL_READ_EMAIL_ID);
        session.setString(1, email.toString());
        session.executeQuery();
        if (session.nextResult()) {
            return session.getLong("EMAIL_ID");
        } else {
            return null;
        }
    }
}
