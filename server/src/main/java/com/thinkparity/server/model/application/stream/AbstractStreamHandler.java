/*
 * Created On:  21-Dec-06 10:38:48 AM
 */
package com.thinkparity.desdemona.model.stream;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.stream.StreamSession;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class AbstractStreamHandler {

    /** An apache logger wrapper. */
    protected static final Log4JWrapper LOGGER;

    static {
        LOGGER = new Log4JWrapper(AbstractStreamHandler.class);
    }

    /** The stream id <code>String</code>. */
    protected final String streamId;

    /** The size <code>Long</code> of the stream. */
    protected final Long streamSize;

    /**
     * The offset <code>Long</code> at which point to resume reading/writing
     * the stream.
     */
    protected final Long streamOffset;

    /** The <code>StreamServer</code>. */
    protected final StreamServer streamServer;

    /** The <code>StreamSession</code>. */
    protected final StreamSession streamSession;

    /**
     * 
     * Create AbstractStreamHandler.
     *
     * @param streamServer
     * @param streamSession
     * @param streamId
     * @param streamOffset
     * @param streamSize
     */
    protected AbstractStreamHandler(final StreamServer streamServer,
            final StreamSession streamSession, final String streamId,
            final Long streamOffset, final Long streamSize) {
        super();
        this.streamServer = streamServer;
        this.streamSession = streamSession;
        this.streamId = streamId;
        this.streamOffset = streamOffset;
        this.streamSize = streamSize;
    }
}
