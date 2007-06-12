/*
 * Generated On: Oct 22 06 10:33:07 AM
 */
package com.thinkparity.desdemona.model.stream;

import java.nio.charset.Charset;

import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.util.codec.MD5Util;

import com.thinkparity.ophelia.model.util.UUIDGenerator;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.util.DesdemonaProperties;

/**
 * <b>Title:</b>thinkParity Stream Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
public final class StreamModelImpl extends AbstractModelImpl implements
        StreamModel, InternalStreamModel {

    /** The character set used by the stream server. */
    static final Charset CHARSET;

    static {
        CHARSET = Charset.forName("ISO-8859-1");
    }

    /** The <code>StreamService</code>. */
    private StreamService service;

    /**
     * Create StreamModelImpl.
     *
     */
    public StreamModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamModel#create(java.lang.String)
     * 
     */
    public String create(final String sessionId) {
        try {
            final StreamSession session = service.authenticate(sessionId);
            final String streamId = newStreamId();
            service.initialize(session, streamId);
            return streamId;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamModel#createSession()
     * 
     */
    public StreamSession createSession() {
        try {
            final ServerSession session = newSession(newStreamSessionId());
            service.initialize(session);
            return session.getClientSession();
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamModel#delete(java.lang.String,
     *      java.lang.String)
     * 
     */
    public void delete(final String sessionId, final String streamId) {
        try {
            final StreamSession session = service.authenticate(sessionId);
            service.destroy(session, streamId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamModel#deleteSession(java.lang.String)
     *
     */
    public void deleteSession(final String sessionId) {
        try {
            final StreamSession session = service.authenticate(sessionId);
            service.destroy(session);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamModel#readSession(java.lang.String)
     * 
     */
    public StreamSession readSession(final String sessionId) {
        try {
            return service.authenticate(sessionId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#initialize()
     *
     */
    @Override
    protected void initialize() {
        properties = DesdemonaProperties.getInstance();
        service = StreamService.getInstance();
    }

    /**
     * Build a server session.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param inetAddress
     *            An <code>InetAddress</code>.
     * @return A new server stream session.
     */
    private ServerSession newSession(final String streamSessionId) {
        final ServerSession session = new ServerSession();
        session.setCharset(CHARSET);
        session.setBufferSize(getBufferSize("stream-session"));
        session.setId(streamSessionId);
        session.setInetAddress(null);
        session.setServerHost(properties.getProperty("thinkparity.stream-host"));
        session.setServerPort(Integer.valueOf(properties.getProperty("thinkparity.stream-port")));
        return session;
    }

    /** The desdemona properties. */
    private DesdemonaProperties properties;

    /**
     * Build a stream id.
     * 
     * @return A stream id <code>String</code>.
     */
    private String newStreamId() {
        /*
         * NOTE A stream id is a UUID
         */
        final StringBuffer hashString = new StringBuffer()
            .append(UUIDGenerator.nextUUID());
        return MD5Util.md5Hex(hashString.toString());
    }

    /**
     * Create an instance of a stream session id for the model user.
     * 
     * @return A stream session id <code>String</code>.
     */
    private String newStreamSessionId() {
        return buildUserTimestampId(user.getId());
    }
}
