/*
 * Created On:  30-Oct-06 11:00:58 AM
 */
package com.thinkparity.codebase.net;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * <b>Title:</b>thinkParity CommonCodebase Socket Factory<br>
 * <b>Description:</b>A client socket factory that is system proxy aware. In
 * order for system proxies to be considered, the &quot;&quot; system property
 * must have been set prior to initializing the java.net framework.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.3
 */
public final class SocketFactory {

    /** A <code>Log4JWrapper</code>. */
    static final Log4JWrapper LOGGER;

    static {
        LOGGER = new Log4JWrapper(SocketFactory.class);
        LOGGER.logVariable("java.net.useSystemProxies",
                System.getProperty("java.net.useSystemProxies"));
    }

    /**
     * Obtain an instance of a socket factory.
     * 
     * @return A <code>javax.net.SocketFactory</code>.
     */
    public static javax.net.SocketFactory getInstance() {
        return new SocketFactoryImpl();
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
    public final static javax.net.SocketFactory getSecureInstance(
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
        return new SSLSocketFactoryImpl(sslContext.getSocketFactory());
    }
}
