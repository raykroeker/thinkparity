/*
 * Created On:  30-Oct-06 11:00:58 AM
 */
package com.thinkparity.codebase.net;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
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

    /** The client keystore password. */
    private static final char[] KEYSTORE_PASSWORD;

    /** The client keystore path. */
    private static final String KEYSTORE_PATH;

    static {
        LOGGER = new Log4JWrapper(SocketFactory.class);
        LOGGER.logVariable("java.net.useSystemProxies",
                System.getProperty("java.net.useSystemProxies"));

        KEYSTORE_PATH = "security/client_keystore";
        KEYSTORE_PASSWORD = "password".toCharArray();
    }

    /**
     * Obtain an instance of a socket factory.
     * 
     * @return A <code>javax.net.SocketFactory</code>.
     */
    public static final javax.net.SocketFactory getInstance() {
        return new SocketFactoryImpl();
    }

    /**
     * Create an instance of a secure server socket factory.
     * 
     * @return A <code>javax.net.ServerSocketFactory</code>.
     */
    public static final javax.net.SocketFactory getSecureInstance() {
        try {
            InputStream stream;
            // load the key store
            final KeyStore keyStore = KeyStore.getInstance("JKS");
            stream = ResourceUtil.getInputStream(KEYSTORE_PATH);
            try {
                keyStore.load(stream, KEYSTORE_PASSWORD);
            } finally {
                stream.close();
            }
            // create a key manager factory
            final KeyManagerFactory keyManagerFactory =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, KEYSTORE_PASSWORD);
    
            // load the trusted store
            final KeyStore trustedStore = KeyStore.getInstance("JKS");
            stream = ResourceUtil.getInputStream(KEYSTORE_PATH);
            try {
                trustedStore.load(stream, KEYSTORE_PASSWORD);
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
        } catch (final KeyStoreException ksx) {
            throw new IllegalArgumentException("Cannot initialize secure socket factory.", ksx);
        } catch (final CertificateException cx) {
            throw new IllegalArgumentException("Cannot initialize secure socket factory.", cx);
        } catch (final NoSuchAlgorithmException nsax) {
            throw new IllegalArgumentException("Cannot initialize secure socket factory.", nsax);
        } catch (final UnrecoverableKeyException ukx) {
            throw new IllegalArgumentException("Cannot initialize secure socket factory.", ukx);
        } catch (final KeyManagementException kmx) {
            throw new IllegalArgumentException("Cannot initialize secure socket factory.", kmx);
        } catch (final IOException iox) {
            throw new IllegalArgumentException("Cannot initialize secure socket factory.", iox);
        }
    }
}
