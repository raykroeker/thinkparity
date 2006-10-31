/*
 * Created On: Sun Oct 22 2006 11:50 PDT
 */
package com.thinkparity.desdemona.model.stream;

import javax.net.ServerSocketFactory;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class SecureStreamSocketServer extends StreamSocketServer {

    /** The key store password <code>char[]</code>. */
    private static final char[] keyStorePassword;

    /** The <code>KeyStore</code>. */
    private static final String keyStorePath;

    /** The <code>ServerSocketFactory</code>. */
    private static final ServerSocketFactory secureSocketFactory;

    static {
        keyStorePassword = "password".toCharArray();
        keyStorePath = "security/server_keystore";
        try {
            secureSocketFactory = com.thinkparity.codebase.net.ServerSocketFactory.getSecureInstance(
                    keyStorePath, keyStorePassword, keyStorePath, keyStorePassword);
        } catch (final Exception x) {
            throw new StreamException(x);
        }
    }

    /**
     * Create SecureStreamSocketServer.
     * 
     * @param streamServer
     *            A <code>StreamServer</code>.
     * @param host
     *            A host <code>String</code>.
     * @param port
     *            A port <code>Integer</code>.
     */
    protected SecureStreamSocketServer(final StreamServer streamServer,
            final String host, final Integer port) {
        super(streamServer, host, port, secureSocketFactory);
    }
}
