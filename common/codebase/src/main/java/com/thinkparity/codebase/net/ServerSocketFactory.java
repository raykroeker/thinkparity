/*
 * Created On:  30-Oct-06 8:54:07 AM
 */
package com.thinkparity.codebase.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.thinkparity.codebase.ResourceUtil;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServerSocketFactory extends javax.net.ServerSocketFactory {

    /**
     * Obtain a server socket factory.
     * 
     * @return A <code>javax.net.ServerSocketFactory</code>.
     */
    public final static javax.net.ServerSocketFactory getInstance() {
        return new ServerSocketFactory(javax.net.ServerSocketFactory.getDefault());
    }

    /**
     * Create an instance of a secure server socket factory.
     * 
     * @param keyStorePath
     *            The key store path <code>String</code>.
     * @param keyStorePassword
     *            The key store password <code>char[]</code>.
     * @return A <code>javax.net.ServerSocketFactory</code>.
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws KeyStoreException
     * @throws KeyManagementException
     * @throws IOException
     * @throws CertificateException
     */
    public final static javax.net.ServerSocketFactory getSecureInstance(
            final String keyStorePath, final char[] keyStorePassword,
            final String trustedStorePath, final char[] trustedStorePassword)
            throws NoSuchProviderException, NoSuchAlgorithmException,
            UnrecoverableKeyException, KeyStoreException,
            KeyManagementException, IOException, CertificateException {
        InputStream stream;
        // load the key store
        final KeyStore keyStore = KeyStore.getInstance("JKS");
        stream = ResourceUtil.getInputStream(keyStorePath);
        try {
            keyStore.load(stream, keyStorePassword);
        } finally {
            stream.close();
        }
        // create a key manager factory
        final KeyManagerFactory keyManagerFactory =
            KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keyStorePassword);

        // load the trusted store
        final KeyStore trustedStore = KeyStore.getInstance("JKS");
        stream = ResourceUtil.getInputStream(trustedStorePath);
        try {
            trustedStore.load(stream, trustedStorePassword);
        } finally {
            stream.close();
        }
        // create a trust manager factory
        final TrustManagerFactory trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustedStore);

        // create a context
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(),
                trustManagerFactory.getTrustManagers(), new SecureRandom());

        // create the factory
        return new ServerSocketFactory(sslContext.getServerSocketFactory());
    }

    /** The wrapped instance of <code>SSLServerSocketFactory</code>. */
    private final javax.net.ServerSocketFactory factory;

    /**
     * Create SSLServerSocketFactory.
     *
     */
    private ServerSocketFactory(final javax.net.ServerSocketFactory factory) {
        super();
        this.factory = factory;
    }

    /**
     * @see javax.net.ServerSocketFactory#createServerSocket(int)
     *
     */
    @Override
    public ServerSocket createServerSocket(final int port) throws IOException {
        return factory.createServerSocket(port);
    }

    /**
     * @see javax.net.ServerSocketFactory#createServerSocket(int, int)
     *
     */
    @Override
    public ServerSocket createServerSocket(final int port, final int backlog)
            throws IOException {
        return factory.createServerSocket(port, backlog);
    }

    /**
     * @see javax.net.ServerSocketFactory#createServerSocket(int, int, java.net.InetAddress)
     *
     */
    @Override
    public ServerSocket createServerSocket(final int port, final int backlog,
            final InetAddress ifAddress) throws IOException {
        return factory.createServerSocket(port, backlog, ifAddress);
    }
}