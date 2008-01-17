/*
 * Created On:  4-Jun-07 11:25:54 AM
 */
package com.thinkparity.codebase.model.queue.notification;

import java.nio.charset.Charset;

import com.thinkparity.common.StringUtil;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NotificationSession {

    /** The charset to use when reading/writing a notification. */
    private Charset charset;

    /** The session id. */
    private String id;

    /** The notification session serverHost. */
    private String serverHost;

    /** The notification session serverPort. */
    private Integer serverPort;

    /**
     * Create NotificationSession.
     *
     */
    public NotificationSession() {
        super();
    }

    /**
     * Obtain the charset.
     * 
     * @return A <code>Charset</code>.
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * Obtain id.
     *
     * @return A String.
     */
    public String getId() {
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
     * Set the character set.
     * 
     * @param charset
     *            A <code>Charset</code>.
     */
    public void setCharset(final Charset charset) {
        this.charset = charset;
    }

    /**
     * Set id.
     *
     * @param id
     *		A String.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Set serverHost.
     *
     * @param serverHost
     *		A String.
     */
    public void setServerHost(final String host) {
        this.serverHost = host;
    }

    /**
     * Set serverPort.
     *
     * @param serverPort
     *		A Integer.
     */
    public void setServerPort(final Integer port) {
        this.serverPort = port;
    }

    /**
     * @see java.lang.Object#toString()
     *
     */
    @Override
    public String toString() {
        return StringUtil.toString(NotificationSession.class, "id", id,
                "serverHost", serverHost, "serverPort", serverPort,
                "charset", charset);
    }
}
