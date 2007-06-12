/*
 * Created On:  3-Jun-07 4:13:56 PM
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.model.Constants.JivePropertyNames;
import com.thinkparity.desdemona.util.DesdemonaProperties;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class StreamService {

    /** A singleton instance of the <code>StreamService</code>. */
    private static final StreamService SINGLETON;

    static {
        SINGLETON = new StreamService();
    }

    /**
     * Obtain an instance of the stream service.
     * 
     * @return An instance of <code>StreamService</code>.
     */
    public static StreamService getInstance() {
        return SINGLETON;
    }

    /** The <code>Environment</code>. */
    private final Environment environment;

    /** A <code>Log4JWrapper</code>. */
    private final Log4JWrapper logger;

    /** An instance of <code>DesdemonaProperties</code>. */
    private final DesdemonaProperties properties;

    /** The stream server. */
    private StreamServer server;

    /**
     * Create StreamService.
     *
     */
    private StreamService() {
        super();
        this.logger = new Log4JWrapper(getClass());
        this.properties = DesdemonaProperties.getInstance();

        this.environment = Environment.valueOf(properties.getProperty("thinkparity.environment"));
    }

    /**
     * Start the stream service.
     *
     */
    public void start() {
        logger.logTraceId();
        logger.logInfo("Starting stream service.");
        synchronized (this) {
            startImpl();
        }
        logger.logInfo("Stream service started.");
    }

    /**
     * Stop the stream service.
     *
     */
    public void stop() {
        logger.logTraceId();
        logger.logInfo("Stopping stream service.");
        synchronized (this) {
            stopImpl();
        }
        logger.logInfo("Stream service stopped.");
    }

    /**
     * Authenticate a stream session.
     * 
     * @param sessionId
     *            A stream session id <code>String</code>.
     * @return A <code>StreamSession</code>.
     */
    StreamSession authenticate(final String sessionId) {
        final ServerSession session = server.authenticate(sessionId);
        if (null == session) {
            return null;
        } else {
            return session.getClientSession();
        }
    }

    /**
     * Destroy a stream session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     */
    void destroy(final StreamSession session) {
        server.destroy(session);
    }

    /**
     * Destroy a stream.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    void destroy(final StreamSession session, final String streamId) {
        server.destroy(session, streamId);
    }

    /**
     * Initialize a session.
     * 
     * @param session
     *            A <code>ServerSession</code>.
     */
    void initialize(final ServerSession session) {
        server.initialize(session);
    }

    /**
     * Intialize a stream.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    void initialize(final StreamSession session, final String streamId) {
        server.initialize(session, streamId);
    }

    /**
     * Start stream service implementation.
     *
     */
    private void startImpl() {
        logger.logTraceId();
        try {
            server = new StreamServer(new File(properties.getProperty(
                    JivePropertyNames.THINKPARITY_STREAM_ROOT)), environment);
            server.start();
        } catch (final Throwable t) {
            server = null;
            throw new StreamException(t);
        }
    }

    /**
     * Stop stream service implementation.
     *
     */
    private void stopImpl() {
        logger.logTraceId();
        try {
            server.stop(Boolean.TRUE);
        } catch (final Throwable t) {
            throw new StreamException(t);
        } finally {
            server = null;
        }
    }
}
