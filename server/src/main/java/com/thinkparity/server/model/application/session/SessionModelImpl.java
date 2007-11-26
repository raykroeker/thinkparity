/*
 * Created On:  29-May-07 8:10:04 PM
 */
package com.thinkparity.desdemona.model.session;

import java.io.File;
import java.util.Calendar;
import java.util.Properties;

import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.session.Configuration;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.codec.MD5Util;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants;
import com.thinkparity.desdemona.model.admin.message.MessageBus;
import com.thinkparity.desdemona.model.io.sql.SessionSql;
import com.thinkparity.desdemona.model.io.sql.UserSql;
import com.thinkparity.desdemona.model.migrator.InternalMigratorModel;

import com.thinkparity.desdemona.util.DateTimeProvider;

import com.thinkparity.service.AuthToken;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SessionModelImpl extends AbstractModelImpl implements
        SessionModel, InternalSessionModel {

    /** A message bus. */
    private MessageBus messageBus;

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
    public AuthToken login(final Credentials credentials)
            throws InvalidCredentialsException {
        validate(credentials);
        try {
            final User user = userSql.read(encryptPassword(credentials));
            if (null == user) {
                throw new InvalidCredentialsException();
            } else {
                sessionSql.delete(user.getLocalId());
                final String sessionId = newSessionId();
                final Session session = newSession(sessionId, user);
                sessionSql.create(user.getLocalId(), sessionId, session);
                deliverMessage("session/created", session, user);
                logger.logInfo("Session {0} created for user {1}.",
                        session.getId(), user.getSimpleUsername());
                return newAuthToken(session);
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
                deliverMessage("session/expired", sessionId);
                logger.logInfo("Session {0} expired.", sessionId);
            } else {
                deliverMessage("session/destroyed", session);
                logger.logInfo("Session {0} destroyed.", sessionId);
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
     * @see com.thinkparity.desdemona.model.session.SessionModel#readConfiguration(com.thinkparity.service.AuthToken, com.thinkparity.codebase.model.migrator.Product)
     *
     */
    @Override
    public Configuration readConfiguration(final AuthToken authToken) {
        try {
            /* HACK - SessionModelImpl#readConfiguration(AuthToken) - hard-coded product */
            final InternalMigratorModel migratorModel = getMigratorModel();
            final Product product = migratorModel.readProduct(Constants.Product.Ophelia.PRODUCT_NAME);
            final Properties properties = migratorModel.readProductConfiguration(product);
            final Configuration configuration = new Configuration();
            for (final String propertyName : properties.stringPropertyNames()) {
                configuration.setProperty(propertyName, properties.getProperty(propertyName));
            }
            return configuration;
        } catch (final Exception x) {
            throw panic(x);
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
                deliverMessage("session/expired", sessionId);
                logger.logInfo("Session {0} expired.", sessionId);
                return null;
            } else {
                final Session session = newSession(sessionId, getUserModel().read(userId));
                deliverMessage("session/extended", session);
                logger.logInfo("Session {0} extended.", session.getId());
                sessionSql.updateExpiry(sessionId, newSessionExpiry());
                return session;
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
        messageBus = MessageBus.getInstance(user, "/com/thinkparity/session");
        sessionSql = new SessionSql();
        userSql = new UserSql();
    }

    /**
     * Deliver a message on the message bus.
     * 
     * @param message
     *            A <code>String</code>.
     * @param session
     *            A <code>Session</code>.
     * @param user
     *            A <code>User</code>.
     */
    private void deliverMessage(final String message, final Session session) {
        deliverMessage(message, session.getId());
    }

    /**
     * Deliver a message on the message bus.
     * 
     * @param message
     *            A <code>String</code>.
     * @param session
     *            A <code>Session</code>.
     * @param user
     *            A <code>User</code>.
     */
    private void deliverMessage(final String message, final Session session,
            final User user) {
        deliverMessage(message, session.getId(), user.getSimpleUsername());
    }

    /**
     * Deliver a message on the message bus.
     * 
     * @param message
     *            A <code>String</code>.
     * @param session
     *            A <code>Session</code>.
     * @param user
     *            A <code>User</code>.
     */
    private void deliverMessage(final String message, final String sessionId) {
        messageBus.setString("sessionId", sessionId);
        messageBus.deliver(message);
    }

    /**
     * Deliver a message on the message bus.
     * 
     * @param message
     *            A <code>String</code>.
     * @param session
     *            A <code>Session</code>.
     * @param user
     *            A <code>User</code>.
     */
    private void deliverMessage(final String message, final String sessionId,
            final String username) {
        messageBus.setString("sessionId", sessionId);
        messageBus.setString("username", username);
        messageBus.deliver(message);
    }

    /**
     * Create a new authentication token for a session.
     * 
     * @param session
     *            A <code>Session</code>.
     * @return An <code>AuthToken</code>.
     */
    private AuthToken newAuthToken(final Session session) {
        final AuthToken authToken = new AuthToken();
        authToken.setClientId(null);
        authToken.setExpiresOn(session.getExpiresOn().getTime());
        authToken.setSessionId(session.getId());
        return authToken;
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
        long expiresOnMillis = expiresOn.getTimeInMillis();
        expiresOnMillis += 25 * 1000 * 60;
        expiresOn.setTimeInMillis(expiresOnMillis);
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

    /**
     * Perform a cursory validation of the credentials.
     * 
     * @param credentials
     *            A set of <code>Credentials</code>.
     * @throws InvalidCredentialsException
     */
    private void validate(final Credentials credentials)
            throws InvalidCredentialsException {
        if (null == credentials) {
            logger.logDebug("Null credentials.");
            throw new InvalidCredentialsException();
        }
        if (null == credentials.getPassword()) {
            logger.logDebug("Null credentials password.");
            throw new InvalidCredentialsException();
        }
        if (null == credentials.getUsername()) {
            logger.logDebug("Null credentials username.");
            throw new InvalidCredentialsException();
        }
    }
}
