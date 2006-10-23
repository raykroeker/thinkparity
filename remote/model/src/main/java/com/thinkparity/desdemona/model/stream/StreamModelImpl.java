/*
 * Generated On: Oct 22 06 10:33:07 AM
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.jivesoftware.util.JiveProperties;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.Direction;
import com.thinkparity.codebase.model.stream.Session;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants.JivePropertyNames;

/**
 * <b>Title:</b>thinkParity Stream Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
final class StreamModelImpl extends AbstractModelImpl {

    /** A <code>JiveProperties</code> instance. */
    private final JiveProperties jiveProperties;

    /** A thinkParity <code>StreamServer</code>. */
    private StreamServer streamServer;

    StreamModelImpl() {
        this(null);
    }

    /**
     * Create StreamModelImpl.
     *
     * @param session
     *		The user's session.
     */
    StreamModelImpl(final com.thinkparity.desdemona.model.session.Session session) {
        super(session);
        this.jiveProperties = JiveProperties.getInstance();
    }

    /**
     * Create a new session.
     * 
     * @param direction
     *            The stream's <code>Direction</code>.
     * @return A <code>Session</code>.
     */
    Session createSession(final Direction direction) {
        logApiId();
        logVariable("direction", direction);
        try {
            final Session session = new Session();
            session.setId(streamServer.createSession(direction));
            session.setEnvironment(readEnvironment());
            return session;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<InputStream> openStreams(final Session session) {
        logApiId();
        logVariable("session", session);
        try {
            return streamServer.openStreams(session.getId());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Start the stream service.
     *
     */
    void start() {
        logApiId();
        try {
            Assert.assertIsNull("Stream server has been started.", streamServer);
            final Environment environment = readEnvironment();
            streamServer = new StreamServer(
                    new File((String) jiveProperties.get(JivePropertyNames.THINKPARITY_STREAM_ROOT)),
                    environment.getStreamHost(), environment.getStreamPort());
            streamServer.start();
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Stop the stream service.
     *
     */
    void stop() {
        logApiId();
        try {
            Assert.assertNotNull("Stream server has not been started.", streamServer);
            streamServer.stop();
            streamServer = null;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }
}
