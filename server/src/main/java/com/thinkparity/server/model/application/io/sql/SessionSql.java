/*
 * Created On:  9-Mar-07 3:27:59 PM
 */
package com.thinkparity.desdemona.model.io.sql;

import java.util.Calendar;

import javax.sql.DataSource;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Email SQL Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SessionSql extends AbstractSql {

    /** Sql to authenticate a user by email/password. */
    private static final String SQL_AUTHENTICATE_BY_EMAIL =
        new StringBuilder("select U.USER_ID,U.USERNAME ")
        .append("from TPSD_USER U ")
        .append("inner join TPSD_USER_EMAIL UE on UE.USER_ID=U.USER_ID ")
        .append("inner join TPSD_EMAIL E on E.EMAIL_ID=UE.EMAIL_ID ")
        .append("where E.EMAIL=? and U.PASSWORD=?")
        .toString();

    /** Sql to authenticate a user by username/password. */
    private static final String SQL_AUTHENTICATE_BY_USERNAME =
        new StringBuilder("select U.USER_ID,U.USERNAME ")
        .append("from TPSD_USER U ")
        .append("where U.USERNAME=? and U.PASSWORD=?")
        .toString();

    /** Sql to create a session. */
    private static final String SQL_CREATE_SESSION =
        new StringBuilder("insert into TPSD_SESSION ")
        .append("(USER_ID,TOKEN,CREATED_ON,EXPIRES_ON) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to delete a session by its primary key. */
    private static final String SQL_DELETE_SESSION_PK =
        new StringBuilder("delete from TPSD_SESSION ")
        .append("where TOKEN=?")
        .toString();

    /** Sql to delete a session by its unique key. */
    private static final String SQL_DELETE_SESSION_UK =
        new StringBuilder("delete from TPSD_SESSION ")
        .append("where USER_ID=?")
        .toString();

    /** Sql to expire all sessions. */
    private static final String SQL_EXPIRE_SESSIONS =
        new StringBuilder("delete from TPSD_SESSION ")
        .append("where EXPIRES_ON < ?")
        .toString();

    /** Sql to read a session id by the user id unique key. */
    private static final String SQL_READ_SESSION_ID_UK =
        new StringBuilder("select SESSION_ID ")
        .append("from TPSD_SESSION S")
        .append("inner join TPSD_USER U on U.USER_ID=S.USER_ID ")
        .append("where U.USER_ID=?")
        .toString();

    /** Sql to read a user id. */
    private static final String SQL_READ_USER_ID_PK =
        new StringBuilder("select U.USER_ID ")
        .append("from TPSD_SESSION S ")
        .append("inner join TPSD_USER U on U.USER_ID=S.USER_ID ")
        .append("where S.TOKEN=?")
        .toString();

    /** Sql to update the expiry date by pk. */
    private static final String SQL_UPDATE_EXPIRY_PK =
        new StringBuilder("update TPSD_SESSION ")
        .append("set EXPIRES_ON=? ")
        .append("where TOKEN=?")
        .toString();

    /** An instance of user sql. */
    private final UserSql userSql;

    /**
     * Create SessionSql.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     */
    public SessionSql(final DataSource dataSource) {
        super(dataSource);
        this.userSql = new UserSql(dataSource);
    }

    /**
     * Authenticate a user by the credential's e-mail address.
     * 
     * @param credentials
     *            A <code>Credentials</code>.
     * @return A <code>User</code>.
     */
    public User authenticateEMail(final Credentials credentials) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_AUTHENTICATE_BY_EMAIL);
            session.setEMail(1, credentials.getEMail());
            session.setEncryptedString(2, credentials.getPassword());
            session.executeQuery();
            if (session.nextResult()) {
                return userSql.extract(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Authenticate a user by the credential's username.
     * 
     * @param credentials
     *            A <code>Credentials</code>.
     * @return A <code>User</code>.
     */
    public User authenticateUsername(final Credentials credentials) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_AUTHENTICATE_BY_USERNAME);
            session.setString(1, credentials.getUsername());
            session.setEncryptedString(2, credentials.getPassword());
            session.executeQuery();
            if (session.nextResult()) {
                return userSql.extract(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Create a session.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     * @param session
     *            A <code>Session</code>.
     */
    public void create(final Long userId, final String sessionId,
            final Session session) {
        final HypersonicSession dbSession = openSession();
        try {
            dbSession.prepareStatement(SQL_CREATE_SESSION);
            dbSession.setLong(1, userId);
            dbSession.setString(2, sessionId);
            dbSession.setCalendar(3, session.getCreatedOn());
            dbSession.setCalendar(4, session.getExpiresOn());
            if (1 != dbSession.executeUpdate())
                throw panic("Could not create session.");
            dbSession.commit();
        } catch (final Throwable t) {
            throw translateError(dbSession, t);
        } finally {
            dbSession.close();
        }
    }

    /**
     * Delete a user's session.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     */
    public void delete(final Long userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_SESSION_UK);
            session.setLong(1, userId);
            final int rowsDeleted = session.executeUpdate();
            if (0 != rowsDeleted && 1 != rowsDeleted)
                throw panic("Could not delete session.");
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Delete a session.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     */
    public void delete(final String sessionId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_SESSION_PK);
            session.setString(1, sessionId);
            final int rowsDeleted = session.executeUpdate();
            if (0 != rowsDeleted && 1 != rowsDeleted)
                throw panic("Could not delete session.");
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read a session id.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @param expireOn
     *            A session epiry <code>Calendar</code>.
     * @return A session id <code>String</code>.
     */
    public String readSessionId(final Long userId, final Calendar expireOn) {
        final HypersonicSession session = openSession();
        try {
            expire(session, expireOn);

            session.prepareStatement(SQL_READ_SESSION_ID_UK);
            session.setLong(1, userId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getString("SESSION_ID");
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read a session user id.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     * @return A user id <code>Long</code>.
     */
    public Long readUserId(final String sessionId, final Calendar expireOn) {
        final HypersonicSession session = openSession();
        try {
            expire(session, expireOn);

            session.prepareStatement(SQL_READ_USER_ID_PK);
            session.setString(1, sessionId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getLong("USER_ID");
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Update the session expiry date.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     * @param expiresOn
     *            An expires on <code>Calendar</code>.
     */
    public void updateExpiry(final String sessionId, final Calendar expiresOn) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_EXPIRY_PK);
            session.setCalendar(1, expiresOn);
            session.setString(2, sessionId);
            if (1 != session.executeUpdate())
                throw panic("Could not update expiry.");
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Delete expired sessions.
     * 
     * @param expireOn
     *            An expire on <code>Calendar</code>.
     */
    private void expire(final HypersonicSession session, final Calendar expireOn) {
        session.prepareStatement(SQL_EXPIRE_SESSIONS);
        session.setCalendar(1, expireOn);
        session.executeUpdate();
        session.commit();
    }
}
