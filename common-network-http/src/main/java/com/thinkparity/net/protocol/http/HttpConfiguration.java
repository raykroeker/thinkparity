/*
 * Created On:  14-Dec-07 1:10:29 PM
 */
package com.thinkparity.net.protocol.http;

import java.util.Hashtable;
import java.util.Map;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HttpConfiguration {

    /** The default configuration. */
    private static final Map<String, Object> DEFAULTS;

    /** A log4j wrapper. */
    private static final Log4JWrapper LOGGER;

    /** The per-http configuration. */
    private static Map<Http, Map<String, Object>> perHttp;

    /** The root configuration. */
    private static Map<String, Object> root;

    static {
        DEFAULTS = newConfigurationMap();
        DEFAULTS.put(Names.Http.CONNECTION_MANAGER_CLASS, SimpleHttpConnectionManager.class);
        DEFAULTS.put(Names.Http.ConnectionManager.MAX_TOTAL_CONNECTIONS, Integer.valueOf(3));
        DEFAULTS.put(Names.Http.ConnectionManager.SO_TIMEOUT, Integer.valueOf(7 * 1000));
        DEFAULTS.put(Names.Http.ConnectionManager.TCP_NO_DELAY, Boolean.FALSE);
        LOGGER = new Log4JWrapper("com.thinkparity.net.protocol.http.configuration");
    }

    /**
     * Ensure the top-level configuration map contains the key.
     * 
     * @param <T>
     *            A key type.
     * @param map
     *            A <code>Map<T, Map<String, Object>></code>.
     * @param key
     *            A <code>T</code>.
     */
    private static <T extends Object> void ensureEntry(
            final Map<T, Map<String, Object>> map, final T key) {
        if (!map.containsKey(key)) {
            map.put(key, new Hashtable<String, Object>());
        }
    }

    /**
     * Instantiate an empty configuration map.
     * 
     * @return A <code>Map<String, Object></code>.
     */
    private static Map<String, Object> newConfigurationMap() {
        return new Hashtable<String, Object>(12, 0.75F);
    }

    /**
     * Create a scoped configuration map and set the type's configuration.
     * 
     * @param <T>
     *            A scope type.
     * @param type
     *            A <code>T</code>
     * @return A <code>Map<T, Map<String, Object>></code>.
     */
    private static <T extends Object> Map<T, Map<String, Object>> newConfigurationMap(
            final T type) {
        final Map<T, Map<String, Object>> map =
            new Hashtable<T, Map<String, Object>>();
        map.put(type, newConfigurationMap());
        return map;
    }

    /**
     * Create HttpConfiguration.
     *
     */
    HttpConfiguration() {
        super();
    }

    /**
     * Clear the proxy credentials.
     * 
     */
    public void clearProxyCredentials() {
        clear(Names.Http.PROXY_CREDENTIALS);
    }

    /**
     * Clear the proxy credentials.
     * 
     * @param http
     *            An <code>Http</code>.
     */
    public void clearProxyCredentials(final Http http) {
        clear(http, Names.Http.PROXY_CREDENTIALS);
    }

    /**
     * Clear the proxy host.
     * 
     */
    public void clearProxyHost() {
        clear(Names.Http.PROXY_HOST);
    }

    /**
     * Clear the proxy host.
     * 
     * @param http
     *            An <code>Http</code>.
     */
    public void clearProxyHost(final Http http) {
        clear(http, Names.Http.PROXY_HOST);
    }

    /**
     * Obtain a connection manager.
     * 
     * @param http
     *            An <code>Http</code>.
     * @return A <code>HttpConnectionManager</code>.
     */
    public HttpConnectionManager getConnectionManager(final Http http) {
        return (HttpConnectionManager) get(http, Names.Http.CONNECTION_MANAGER);
    }

    /**
     * Obtain a connection manager class.
     * 
     * @param http
     *            An <code>Http</code>.
     * @return A <code>Class<?></code>.
     */
    public Class<?> getConnectionManagerClass(final Http http) {
        return (Class<?>) get(http, Names.Http.CONNECTION_MANAGER_CLASS);
    }

    /**
     * Obtain the maximum total number of connections.
     * 
     * @param http
     *            An <code>Http</code>.
     * @return An <code>Integer</code>.
     */
    public Integer getMaxTotalConnections(final Http http) {
        return (Integer) get(http, Names.Http.ConnectionManager.MAX_TOTAL_CONNECTIONS);
    }

    /**
     * Obtain the proxy host.
     * 
     * @param http
     *            An <code>Http</code>.
     * @return A set of <code>Credentials</code>.
     */
    public Credentials getProxyCredentials(final Http http) {
        return (Credentials) get(http, Names.Http.PROXY_CREDENTIALS);
    }

    /**
     * Obtain the proxy host.
     * 
     * @param http
     *            An <code>Http</code>.
     * @return A <code>ProxyHost</code>.
     */
    public ProxyHost getProxyHost(final Http http) {
        return (ProxyHost) get(http, Names.Http.PROXY_HOST);
    }

    /**
     * Obtain a socket timeout.
     * 
     * @param http
     *            An <code>Http</code>.
     * @return An <code>Integer</code>.
     */
    public Integer getSoTimeout(final Http http) {
        return (Integer) get(http, Names.Http.ConnectionManager.SO_TIMEOUT);
    }

    /**
     * Obtain a tcp no delay flag.
     * 
     * @param http
     *            An <code>Http</code>.
     * @return A <code>Boolean</code>.
     */
    public Boolean getTcpNoDelay(final Http http) {
        return (Boolean) get(http, Names.Http.ConnectionManager.TCP_NO_DELAY);
    }

    /**
     * Determine whether or not the configuration applied to http is dirty.
     * 
     * @param http
     *            An instance of <code>Http</code>.
     * @return A <code>Boolean</code>.
     */
    public Boolean isDirty(final Http http) {
        Long configurationTimestamp = (Long) get(http, Names.TIMESTAMP);
        Long clientTimestamp = http.getClientTimestamp();
        LOGGER.logInfo("Client timestamp:\"{0}\".", null == clientTimestamp
                ? "null" : clientTimestamp.toString());
        /* a client has not yet been instantiated; therefore the configuration
         * cannot be dirty */
        if (null == clientTimestamp) {
            return Boolean.FALSE;
        } else {
            /* the client was instantiated prior to the most recent per http
             * configuration change; therefore the configuration is dirty */
            if (clientTimestamp.longValue() < configurationTimestamp.longValue()) {
                return Boolean.TRUE;
            } else {
                configurationTimestamp = (Long) get(Names.TIMESTAMP);
                if (null == configurationTimestamp) {
                    return Boolean.FALSE;
                } else {
                    /* a client was instantiated prior to the most recent global
                     * configuration change; therefore the configuration is
                     * dirty */
                    if (http.getClientTimestamp().longValue() < configurationTimestamp.longValue()) {
                        return Boolean.TRUE;
                    } else {
                        return Boolean.FALSE;
                    }
                }
            }
        }
    }

    /**
     * Set the connection manager.
     * 
     * @param http
     *            An <code>Http</code>.
     * @param connectionManager
     *            An <code>HttpConnectionManager</code>.
     */
    public void setConnectionManager(final Http http, final HttpConnectionManager connectionManager) {
        set(http, Names.Http.CONNECTION_MANAGER, connectionManager);
    }

    /**
     * Set the connection manager class.
     * 
     * @param http
     *            An <code>Http</code>.
     * @param connectionManagerClass
     *            An <code>HttpConnectionManager</code>.
     */
    public void setConnectionManagerClass(final Http http,
            final Class<?> connectionManagerClass) {
        set(http, Names.Http.CONNECTION_MANAGER_CLASS, connectionManagerClass);
    }

    /**
     * Set max total connections.
     * 
     * @param http
     *            An <code>Http</code>.
     * @param maximum
     *            An <code>Integer</code>.
     */
    public void setMaxTotalConnections(final Http http, final Integer maximum) {
        set(http, Names.Http.ConnectionManager.MAX_TOTAL_CONNECTIONS, maximum);
    }
    
    /**
     * Set the proxy credentials.
     * 
     * @param http
     *            An <code>Http</code>.
     * @param credentials
     *            A set of <code>Credentials</code>.
     */
    public void setProxyCredentials(final Credentials credentials) {
        set(Names.Http.PROXY_CREDENTIALS, credentials);
    }

    /**
     * Set the proxy credentials.
     * 
     * @param http
     *            An <code>Http</code>.
     * @param credentials
     *            A set of <code>Credentials</code>.
     */
    public void setProxyCredentials(final Http http, final Credentials credentials) {
        set(http, Names.Http.PROXY_CREDENTIALS, credentials);
    }

    /**
     * Set the proxy host.
     * 
     * @param http
     *            An <code>Http</code>.
     * @param proxyHost
     *            A <code>ProxyHost</code>.
     */
    public void setProxyHost(final Http http, final ProxyHost proxyHost) {
        set(http, Names.Http.PROXY_HOST, proxyHost);
    }

    /**
     * Set the proxy host.
     * 
     * @param http
     *            An <code>Http</code>.
     * @param proxyHost
     *            A <code>ProxyHost</code>.
     */
    public void setProxyHost(final ProxyHost proxyHost) {
        set(Names.Http.PROXY_HOST, proxyHost);
    }

    /**
     * Set socket timeout.
     * 
     * @param http
     *            An <code>Http</code>.
     * @param maximum
     *            An <code>Integer</code>.
     */
    public void setSoTimeout(final Http http, final Integer soTimeout) {
        set(http, Names.Http.ConnectionManager.SO_TIMEOUT, soTimeout);
    }

    /**
     * Set tcp no delay.
     * 
     * @param http
     *            An <code>Http</code>.
     * @param maximum
     *            An <code>Integer</code>.
     */
    public void setTcpNoDelay(final Http http, final Boolean noDelay) {
        set(http, Names.Http.ConnectionManager.TCP_NO_DELAY, noDelay);
    }

    /**
     * Clear a per-http configuration.
     * 
     * @param http
     *            A <code>Http</code>.
     * @param name
     *            A <code>String</code>.
     */
    private synchronized void clear(final Http http, final String name) {
        if (null == perHttp) {
            perHttp = newConfigurationMap(http);
        }
        ensureEntry(perHttp, http);
        final Long timestamp = Long.valueOf(System.currentTimeMillis());
        LOGGER.logInfo("{0} - Clear \"{1}\" for {2}.", timestamp.toString(), name, http);
        perHttp.get(http).put(Names.TIMESTAMP, timestamp);
        perHttp.get(http).remove(name);
    }

    /**
     * Clear a root configuration.
     * 
     * @param name
     *            A <code>String</code>.
     */
    private synchronized void clear(final String name) {
        if (null == root) {
            root = newConfigurationMap();
        }
        final Long timestamp = Long.valueOf(System.currentTimeMillis());
        LOGGER.logInfo("{0} - Clear \"{1}\".", timestamp.toString(), name);
        root.put(Names.TIMESTAMP, timestamp);
        root.remove(name);
    }

    /**
     * Obtain an http configuration.
     * 
     * @param http
     *            A <code>Http</code>.
     * @param name
     *            A name <code>String</code>.
     * @return An <code>Object</code>.
     */
    private synchronized Object get(final Http http, final String name) {
        if (null == perHttp) {
            return get(name);
        } else {
            if (perHttp.containsKey(http)) {
                final Map<String, Object> values = perHttp.get(http);
                if (values.containsKey(name)) {
                    LOGGER.logInfo("Get \"{0}\":\"{1}\" for {2}.", name,
                            values.get(name), http);
                    return values.get(name);
                } else {
                    return get(name);
                }
            } else {
                return get(name);
            }
        }
    }

    /**
     * Obtain a configuration value. Check the root configuration first;
     * otherwise use the defaults.
     * 
     * @param name
     *            A name <code>String</code>.
     * @return An <code>Object</code>.
     */
    private synchronized Object get(final String name) {
        if (null == root) {
            LOGGER.logInfo("Get \"{0}\":\"{1}\".", name, DEFAULTS.get(name));
            return DEFAULTS.get(name);
        } else {
            if (root.containsKey(name)) {
                LOGGER.logInfo("Get \"{0}\":\"{1}\".", name, root.get(name));
                return root.get(name);
            } else {
                LOGGER.logInfo("Get \"{0}\":\"{1}\".", name, DEFAULTS.get(name));
                return DEFAULTS.get(name);
            }
        }
    }

    /**
     * Set a per-http configuration.
     * 
     * @param address
     *            A <code>NetworkAddress</code>.
     * @param name
     *            A <code>String</code>.
     * @param value
     *            An <code>Object</code>.
     */
    private synchronized void set(final Http http, final String name,
            final Object value) {
        if (null == perHttp) {
            perHttp = newConfigurationMap(http);
        }
        ensureEntry(perHttp, http);
        final Long timestamp = Long.valueOf(System.currentTimeMillis());
        LOGGER.logInfo("{0} - Set \"{1}\":\"{2}\" for {3}.", timestamp.toString(), name, value, http);
        perHttp.get(http).put(Names.TIMESTAMP, timestamp);
        perHttp.get(http).put(name, value);
    }

    /**
     * Set a root configuration.
     * 
     * @param name
     *            A <code>String</code>.
     * @param value
     *            An <code>Object</code>.
     */
    private synchronized void set(final String name, final Object value) {
        if (null == root) {
            root = newConfigurationMap();
        }
        final Long timestamp = Long.valueOf(System.currentTimeMillis());
        LOGGER.logInfo("{0} - Set \"{1}\":\"{2}\".", timestamp.toString(), name, value);
        root.put(Names.TIMESTAMP, timestamp);
        root.put(name, value);
    }

    /** <b>Title:</b>Http Configuration Names<br> */
    private static final class Names {
        private static final String TIMESTAMP = "timestamp";
        /** <b>Title:</b>Per Http Configuration Names<br> */
        private static final class Http {
            private static final String CONNECTION_MANAGER = "http.connectionmanager";
            private static final String CONNECTION_MANAGER_CLASS = "http.connectionmanagerclass";
            private static final String PROXY_CREDENTIALS = "http.proxycredentials";
            private static final String PROXY_HOST = "http.proxyhost";
            /** <b>Title:</b>Per Connection Manager Configuration Names<br> */
            private static final class ConnectionManager {
                private static final String MAX_TOTAL_CONNECTIONS = "http.connectionmanager.maxtotalconnections";
                private static final String SO_TIMEOUT = "http.connectionmanager.sotimeout";
                private static final String TCP_NO_DELAY = "http.connectionmanager.tcpnodelay";
            }
        }
    }
}
