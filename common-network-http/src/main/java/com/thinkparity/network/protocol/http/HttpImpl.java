/*
 * Created On:  14-Dec-07 1:09:46 PM
 */
package com.thinkparity.network.protocol.http;

import java.lang.reflect.InvocationTargetException;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.network.protocol.http.socket.SecureSocketFactory;
import com.thinkparity.network.protocol.http.socket.SocketFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class HttpImpl {

    static {
        Protocol.registerProtocol("http", new Protocol("http", SocketFactory.getInstance(), 80));
        Protocol.registerProtocol("https", new Protocol("https", (ProtocolSocketFactory) SecureSocketFactory.getInstance(), 443));
    }

    /** The http configuration. */
    private HttpConfiguration configuration;

    /** An http reference. */
    private final Http http;

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /**
     * Create HttpImpl.
     * 
     * @param logger
     *            A <code>Log4JWrapper</code>.
     */
    HttpImpl(final Http http, final Log4JWrapper logger) {
        super();
        this.http = http;
        this.logger = logger;
    }

    /**
     * Obtain the configuration.
     * 
     * @return A <code>HttpConfiguration</code>.
     */
    HttpConfiguration getConfiguration() {
        logger.logTraceId();
        if (null == configuration) {
            configuration = new HttpConfiguration();
        }
        return configuration;
    }

    /**
     * Instantiate an http client.
     * 
     * @return A <code>HttpClient</code>.
     * @throws HttpException
     *             if the http client cannot be instantiated
     */
    HttpClient newClient() throws HttpException {
        logger.logTraceId();
        final HttpClient httpClient = new HttpClient();
        setHttpClientOptions(httpClient);
        return httpClient;
    }

    /**
     * Instantiate a connection manager.
     * 
     * @param connectionManagerClass
     *            A <code>Class<?></code>.
     * @return An <code>HttpConnectionManager</code>.
     * @throws HttpException
     *             if the connection manager cannot be instantiated
     */
    private HttpConnectionManager newConnectionManager(
            final Class<?> connectionManagerClass) throws HttpException {
        try {
            return (HttpConnectionManager) connectionManagerClass.getConstructor(new Class<?>[] {}).newInstance(new Object[] {});
        } catch (final NoSuchMethodException nsmx) {
            throw new HttpException(nsmx);
        } catch (final InvocationTargetException itx) {
            throw new HttpException(itx);
        } catch (final InstantiationException ix) {
            throw new HttpException(ix);
        } catch (final IllegalAccessException iax) {
            throw new HttpException(iax);
        }
    }

    /**
     * Set the http client options.
     * 
     * @param httpClient
     *            A <code>HttpClient</code>.
     * @throws HttpException
     *             if the http client's connection manager is not set; and
     *             cannot be instantiated
     */
    private void setHttpClientOptions(final HttpClient httpClient) throws HttpException {
        HttpConnectionManager connectionManager = getConfiguration().getConnectionManager(http);
        if (null == connectionManager) {
            final Class<?> connectionManagerClass = getConfiguration().getConnectionManagerClass(http);
            logger.logDebug("connectionManagerClass:{0}", connectionManagerClass);
            connectionManager = newConnectionManager(connectionManagerClass);
        }
        logger.logDebug("connectionManager:{0}", connectionManager);
        httpClient.setHttpConnectionManager(connectionManager);
        final Integer maxTotalConnections = getConfiguration().getMaxTotalConnections(http);
        logger.logDebug("maxTotalConnections:{0}", maxTotalConnections);
        connectionManager.getParams().setMaxTotalConnections(maxTotalConnections);
        final Integer soTimeout = getConfiguration().getSoTimeout(http);
        logger.logDebug("soTimeout:{0}", soTimeout);
        connectionManager.getParams().setSoTimeout(soTimeout);
        final Boolean tcpNoDelay = getConfiguration().getTcpNoDelay(http);
        logger.logDebug("tcpNoDelay:{0}", tcpNoDelay);
        connectionManager.getParams().setTcpNoDelay(tcpNoDelay);
        final ProxyHost proxyHost = getConfiguration().getProxyHost(http);
        logger.logDebug("proxyHost:{0}", proxyHost);
        httpClient.getHostConfiguration().setProxyHost(proxyHost);
    }
}
