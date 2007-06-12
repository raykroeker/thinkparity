/*
 * Created On:  4-Jun-07 9:12:19 AM
 */
package com.thinkparity.desdemona.model.queue.notification;

import com.thinkparity.codebase.net.ServerSocketFactory;

import com.thinkparity.desdemona.model.Constants.Security.KeyStore;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class SecureNotificationSocketServer extends NotificationSocketServer {

    /** The secure <code>javax.net.ServerSocketFactory</code>. */
    private static final javax.net.ServerSocketFactory SERVER_SOCKET_FACTORY;

    static {
        final char[] keyStorePassword = KeyStore.SERVER_PASS;
        final String keyStorePath = KeyStore.SERVER_PATH;
        try {
            SERVER_SOCKET_FACTORY = ServerSocketFactory.getSecureInstance(
                    keyStorePath, keyStorePassword, keyStorePath, keyStorePassword);
        } catch (final Exception x) {
            throw new RuntimeException(x);
        }
    }

    /**
     * Create SecureNotificationSocketServer.
     * 
     * @param server
     *            The notification server.
     * @param host
     *            The host to listen on.
     * @param port
     *            The port to listen on.
     */
    SecureNotificationSocketServer(final NotificationServer server,
            final String host, final Integer port) {
        super(server, host, port, SERVER_SOCKET_FACTORY);
    }
}
