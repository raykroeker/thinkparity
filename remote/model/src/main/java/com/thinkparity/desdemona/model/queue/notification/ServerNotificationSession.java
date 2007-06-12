/*
 * Created On:  4-Jun-07 1:32:55 PM
 */
package com.thinkparity.desdemona.model.queue.notification;

import java.nio.charset.Charset;

import com.thinkparity.codebase.model.queue.notification.NotificationSession;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServerNotificationSession {

    /** The notification socket delegate. */
    private NotificationSocketDelegate delegate;

    /** The shared client/server notification session. */
    private final NotificationSession sharedSession;

    /**
     * Create ServerNotificationSession.
     *
     */
    public ServerNotificationSession() {
        super();
        this.sharedSession = new NotificationSession();
    }

    /**
     * Obtain charset.
     *
     * @return A Charset.
     */
    public Charset getCharset() {
        return sharedSession.getCharset();
    }

    /**
     * Obtain the client session.
     * 
     * @return A <code>NotificationSession</code>.
     */
    public NotificationSession getClientSession() {
        final NotificationSession clientSession = new NotificationSession();
        clientSession.setCharset(sharedSession.getCharset());
        clientSession.setId(sharedSession.getId());
        clientSession.setServerHost(sharedSession.getServerHost());
        clientSession.setServerPort(sharedSession.getServerPort());
        return clientSession;
    }

    /**
     * Obtain id.
     *
     * @return A String.
     */
    public String getId() {
        return sharedSession.getId();
    }

    /**
     * Obtain serverHost.
     *
     * @return A String.
     */
    public String getServerHost() {
        return sharedSession.getServerHost();
    }

    /**
     * Obtain serverPort.
     *
     * @return A Integer.
     */
    public Integer getServerPort() {
        return sharedSession.getServerPort();
    }

    /**
     * Set charset.
     *
     * @param charset
     *		A Charset.
     */
    public void setCharset(final Charset charset) {
        this.sharedSession.setCharset(charset);
    }

    /**
     * Set id.
     *
     * @param id
     *		A String.
     */
    public void setId(final String id) {
        this.sharedSession.setId(id);
    }

    /**
     * Set serverHost.
     *
     * @param serverHost
     *		A String.
     */
    public void setServerHost(final String serverHost) {
        this.sharedSession.setServerHost(serverHost);
    }

    /**
     * Set serverPort.
     *
     * @param serverPort
     *		A Integer.
     */
    public void setServerPort(final Integer serverPort) {
        this.sharedSession.setServerPort(serverPort);
    }

    /**
     * Obtain delegate.
     *
     * @return A NotificationSocketDelegate.
     */
    NotificationSocketDelegate getDelegate() {
        return delegate;
    }

    /**
     * Set delegate.
     *
     * @param delegate
     *		A NotificationSocketDelegate.
     */
    void setDelegate(final NotificationSocketDelegate delegate) {
        this.delegate = delegate;
    }
}
