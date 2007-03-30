/*
 * Created On:  9-Mar-07 3:27:59 PM
 */
package com.thinkparity.desdemona.model.io.sql;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Email SQL Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class EMailSql extends AbstractSql {

    /** Sql to create an e-mail address. */
    private static final String SQL_CREATE_EMAIL =
        new StringBuilder("insert into TPSD_EMAIL (EMAIL) values (?)")
        .toString();

    /** Sql to delete an email address. */
    private static final String SQL_DELETE_EMAIL_UK =
        new StringBuilder("delete from TPSD_EMAIL where EMAIL=?")
        .toString();

    /** Sql to count email addresses. */
    private static final String SQL_READ_COUNT_UK =
        new StringBuilder("select count(E.EMAIL_ID) \"EMAIL_COUNT\" ")
        .append("from TPSD_EMAIL E ")
        .append("where E.EMAIL=?")
        .toString();

    /** Sql to read an e-mail address id. */
    private static final String SQL_READ_EMAIL_ID =
        new StringBuilder("select E.EMAIL_ID from TPSD_EMAIL E where E.EMAIL=?")
        .toString();

    /**
     * Create EmailSql.
     *
     */
    public EMailSql() {
        super();
    }

    /**
     * Determine whether or not an e-mail address exists.
     * 
     * @param email
     *            An <code>EMail</code> address.
     * @return True if the e-mail address already exists.
     */
    public Boolean doesExist(final EMail email) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_COUNT_UK);
            session.setString(1, email.toString());
            session.executeQuery();
            if (session.nextResult()) {
                final int emailCount = session.getInteger("EMAIL_COUNT");
                if (0 == emailCount) {
                    return Boolean.FALSE;
                } else if (1 == emailCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine e-mail existence.");
                }
            } else {
                throw new HypersonicException("Could not determine e-mail existence.");
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Delete an e-mail address.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param email
     *            An <code>EMail</code> address.
     */
    void delete(final HypersonicSession session, final EMail email) {
        session.prepareStatement(SQL_DELETE_EMAIL_UK);
        session.setString(1, email.toString());
        if (1 != session.executeUpdate())
            throw panic("Could not delete e-mail address {0}.", email);
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
    Long readEMailId(final HypersonicSession session, final EMail email) {
        session.prepareStatement(SQL_READ_EMAIL_ID);
        session.setString(1, email.toString());
        session.executeQuery();
        if (session.nextResult()) {
            return session.getLong("EMAIL_ID");
        } else {
            return null;
        }
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
}
