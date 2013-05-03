/*
 * Created On:  3-Jun-07 4:13:56 PM
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.session.Environment;

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
     * Destroy a stream.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     */
    public void destroy(final String streamId) {
        server.destroy(streamId);
    }

    /**
     * Determine a stream's existence.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @return True if a stream exists.
     */
    public Boolean doesExist(final String streamId) {
        return server.doesExist(streamId);
    }

    /**
     * Finalize the persistence of the stream.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     */
    public void finalize(final String streamId) {
        server.finalizeStream(streamId);
    }

    /**
     * Intialize a stream.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     */
    public void initialize(final String streamId) {
        server.initialize(streamId);
    }

	/**
     * Open a stream for an downstream pull.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @return A <code>ReadableByteChannel</code>.
     */
    public ReadableByteChannel openForDownstream(final String streamId)
            throws IOException {
        return server.openForDownstream(streamId);
    }

    /**
     * Open a stream for an upstream push.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @return A <code>WritableByteChannel</code>.
     */
    public WritableByteChannel openForUpstream(final String streamId)
            throws IOException {
        return server.openForUpstream(streamId);
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
