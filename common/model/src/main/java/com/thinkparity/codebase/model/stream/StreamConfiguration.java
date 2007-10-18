/*
 * Created On:  18-Oct-07 12:49:37 PM
 */
package com.thinkparity.codebase.model.stream;

import java.util.Hashtable;
import java.util.Map;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import org.apache.commons.httpclient.HttpClient;

/**
 * <b>Title:</b>thinkParity Stream Configuration<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class StreamConfiguration {

    /** The default configuration. */
    private static final Map<String, Object> DEFAULTS;

    /** A log4j wrapper. */
    private static final Log4JWrapper LOGGER;

    /** The root configuration. */
    private static Map<String, Object> root;

    static {
        DEFAULTS = newConfigurationMap();
        DEFAULTS.put(Names.Http.CLIENT, newDefaultHttpClient());
        LOGGER = new Log4JWrapper("com.thinkparity.stream");
    }

    /**
     * Instantiate a new empty configuration map.
     * 
     * @return A <code>Map<String, Object></code>.
     */
    private static Map<String, Object> newConfigurationMap() {
        return new Hashtable<String, Object>(1, 1.0F);
    }

    /**
     * Instantiate a default http client.
     * 
     * @return A <code>HttpClient</code>.
     */
    private static HttpClient newDefaultHttpClient() {
        final HttpClient httpClient = new HttpClient();
        httpClient.setHttpConnectionManager(new com.thinkparity.codebase.model.stream.httpclient.HttpConnectionManager());
        httpClient.getHttpConnectionManager().getParams().setMaxTotalConnections(3);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(7 * 1000);
        return httpClient;
    }

    /**
     * Create StreamConfiguration.
     *
     */
    public StreamConfiguration() {
        super();
    }

    /**
     * Set the http connection manager.
     * 
     * @param hcm
     *            A <code>Class<?></code>.
     */
    public HttpClient getHttpClient() {
        return (HttpClient) get(Names.Http.CLIENT);
    }

    /**
     * Set the http client.
     * 
     * @param httpClient
     *            A <code>HttpClient</code>.
     */
    public void setHttpClient(final HttpClient httpClient) {
        set(Names.Http.CLIENT, httpClient);
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
     * Set a root configuration value.
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
        LOGGER.logInfo("Set \"{0}\":\"{1}\"", name, value);
        root.put(name, value);
    }

    /** <b>Title:</b>Stream Configuration Entry Names<br> */
    private static final class Names {
        /** <b>Title:</b>Stream Configuration Entry Names:  Http<br> */
        private static final class Http {
            private static final String CLIENT = "stream.connection.client";
        }
    }
}
