/*
 * Created On: Sun Oct 22 2006 10:28 PDT
 */
package com.thinkparity.codebase.model.stream;

import java.nio.charset.Charset;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class StreamSession {

    /** The size of the transfer buffer. */
    private Integer bufferSize;

    /** The character set to use. */
    private Charset charset;

    /** The session id <code>String</code>. */
    private String id;

    /** The streaming sesssion server host. */
    private String serverHost;

    /** The streaming sesssion server port. */
    private Integer serverPort;

    /**
     * Create StreamSession.
     *
     */
    public StreamSession() {
        super();
    }

    /**
     * Obtain the buffer size.
     * 
     * @return A size <code>Integer</code>.
     */
    public final Integer getBufferSize() {
        return bufferSize;
    }

    /**
     * Obtain the character set.
     * 
     * @return A <code>Charset</code>.
     */
    public final Charset getCharset() {
        return charset;
    }

    /**
     * Obtain the session id.
     * 
     * @return A session id <code>String</code>.
     */
    public final String getId() {
        return id;
    }

    /**
     * Obtain serverHost.
     *
     * @return A String.
     */
    public String getServerHost() {
        return serverHost;
    }

    /**
     * Obtain serverPort.
     *
     * @return A Integer.
     */
    public Integer getServerPort() {
        return serverPort;
    }

    /**
     * Set the buffer size.
     * 
     * @param bufferSize
     *            A size <code>Integer</code>.
     */
    public final void setBufferSize(final Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    /**
     * Set the character set.
     * 
     * @param charset
     *            A <code>Charset</code>.
     */
    public final void setCharset(final Charset charset) {
        this.charset = charset;
    }

    /**
     * Set the session id.
     * 
     * @param id
     *            A session id <code>String</code>.
     */
    public final void setId(final String id) {
        this.id = id;
    }

    /**
     * Set serverHost.
     *
     * @param serverHost
     *		A String.
     */
    public void setServerHost(final String serverHost) {
        this.serverHost = serverHost;
    }

    /**
     * Set serverPort.
     *
     * @param serverPort
     *		A Integer.
     */
    public void setServerPort(final Integer serverPort) {
        this.serverPort = serverPort;
    }
}