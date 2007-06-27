/*
 * Created On:  29-May-07 8:10:04 PM
 */
package com.thinkparity.desdemona.model.session;

import java.io.File;
import java.util.Calendar;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.codec.MD5Util;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.io.sql.SessionSql;
import com.thinkparity.desdemona.model.io.sql.UserSql;
import com.thinkparity.desdemona.util.DateTimeProvider;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SessionModelImpl extends AbstractModelImpl implements
        SessionModel, InternalSessionModel {

    /** A session sql interface. */
    private SessionSql sessionSql;

    /** A user sql interface. */
    private UserSql userSql;

    /**
     * Create SessionModelImpl.
     *
     */
    public SessionModelImpl() {
        super();
    }

    /**
     * Create SessionModelImpl.
     *
     * @param session
     */
    public SessionModelImpl(final Session session) {
        super();
        this.userSql = new UserSql();
    }

    /**
     * @see com.thinkparity.desdemona.model.session.SessionModel#login(com.thinkparity.codebase.model.session.Credentials)
     * 
     */
    public String login(final Credentials credentials)
            throws InvalidCredentialsException {
        try {
            final User user = userSql.read(credentials);
            if (null == user) {
                throw new InvalidCredentialsException();
            } else {
                sessionSql.delete(user.getLocalId());
                final String sessionId = newSessionId();
                final Session session = newSession(sessionId, user);
                sessionSql.create(user.getLocalId(), sessionId, session);
                return sessionId;
            }
        } catch (final InvalidCredentialsException icx) {
            throw icx;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.session.SessionModel#logout(java.lang.String)
     *
     */
    public void logout(final String sessionId) {
        try {
            final Session session = readSession(sessionId);
            if (null == session) {
                logger.logWarning("Session {0} has expired.", sessionId);
            } else {
                sessionSql.delete(sessionId);
                try {
                    getTempFileSystem().deleteTree();
                } catch (final Throwable t) {
                    logger.logWarning(t, "Could not destroy session.");
                }
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.session.InternalSessionModel#read()
     *
     */
    public Session read() {
        try {
            final String sessionId = sessionSql.readSessionId(user.getLocalId(),
                    DateTimeProvider.getCurrentDateTime());
            if (null == sessionId) {
                logger.logWarning("Session for user {0} has expired.", user.getId());
                return null;
            } else {
                return newSession(sessionId, user);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.session.InternalSessionModel#lookupSession(java.lang.String)
     *
     */
    public Session readSession(final String sessionId) {
        try {
            final Long userId = sessionSql.readUserId(sessionId,
                    DateTimeProvider.getCurrentDateTime());
            if (null == userId) {
                logger.logWarning("Session {0} has expired.", sessionId);
                return null;
            } else {
                sessionSql.updateExpiry(sessionId, newSessionExpiry());
                return newSession(sessionId, getUserModel().read(userId));
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.session.SessionModel#readUser(java.lang.String)
     *
     */
    public User readUser(final String sessionId) {
        try {
            final Long userId = sessionSql.readUserId(sessionId,
                    DateTimeProvider.getCurrentDateTime());
            if (null == userId) {
                return null;
            } else {
                return getUserModel().read(userId);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#initialize()
     *
     */
    @Override
    protected void initialize() {
        sessionSql = new SessionSql();
        userSql = new UserSql();
    }

    /**
     * Create a session.
     * 
     * @param createdOn
     *            A created on <code>Calendar</code>.
     * @param expiresOn
     *            An expires on <code>Calendar</code>.
     * @param sessionId
     *            A session id <code>String</code>.
     * @param user
     *            A <code>User</code>.
     * @return An instance of a <code>Session</code>.
     */
    private Session newSession(final Calendar createdOn,
            final Calendar expiresOn, final String sessionId, final User user) {
        final Session session = new Session();
        session.setCreatedOn(createdOn);
        session.setExpiresOn(expiresOn);
        session.setId(sessionId);
        session.setInetAddress(null);
        session.setTempRoot(new File(getProperty("thinkparity.temp.root")));
        return session;
    }

    /**
     * Create a session.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     * @param user
     *            A <code>User</code>.
     * @return A <code>Session</code>.
     */
    private Session newSession(final String sessionId, final User user) {
        final Calendar createdOn = DateTimeProvider.getCurrentDateTime();
        final Calendar expiresOn = newSessionExpiry(createdOn);
        return newSession(createdOn, expiresOn, sessionId, user);
    }

    /**
     * Create a new session expiry date from the current date/time.
     * 
     * @return A <code>Calendar</code>.
     */
    private Calendar newSessionExpiry() {
        return newSessionExpiry(DateTimeProvider.getCurrentDateTime());
    }

    /**
     * Create a new session expiry date from the current date/time.
     * 
     * @param reference
     *            A reference <code>Calendar</code>.
     * @return A <code>Calendar</code>.
     */
    private Calendar newSessionExpiry(final Calendar reference) {
        final Calendar expiresOn = (Calendar) reference.clone();
        expiresOn.set(Calendar.HOUR, expiresOn.get(Calendar.MINUTE) + 25);
        return expiresOn;
    }

    /**
     * Create a new session id.
     * 
     * @return A session id <code>String</code>.
     */
    private String newSessionId() {
        return MD5Util.md5Base64(String.valueOf(currentDateTime().getTimeInMillis()));
    }
}
