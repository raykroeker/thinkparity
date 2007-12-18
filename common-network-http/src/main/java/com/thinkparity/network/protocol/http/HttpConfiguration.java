/*
 * Created On:  14-Dec-07 1:10:29 PM
 */
package com.thinkparity.network.protocol.http;

import java.util.Hashtable;
import java.util.Map;

import com.thinkparity.codebase.log4j.Log4JWrapper;

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
        LOGGER = new Log4JWrapper("com.thinkparity.network.protocol.http.configuration");
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
        LOGGER.logInfo("Set \"{0}\":\"{1}\" for {2}.", name, value, http);
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
        LOGGER.logInfo("Set \"{0}\":\"{1}\".", name, value);
        root.put(name, value);
    }

    /** <b>Title:</b>Http Configuration Names<br> */
    private static final class Names {
        /** <b>Title:</b>Per Http Configuration Names<br> */
        private static final class Http {
            private static final String CONNECTION_MANAGER = "http.connectionmanager";
            private static final String CONNECTION_MANAGER_CLASS = "http.connectionmanagerclass";
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
