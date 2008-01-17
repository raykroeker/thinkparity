/*
 * Created On:  21-Dec-07 11:54:05 AM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.model.events.ConfigurationEvent;
import com.thinkparity.ophelia.model.events.ConfigurationListener;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.configuration.Configuration;
import com.thinkparity.ophelia.model.workspace.configuration.Proxy;
import com.thinkparity.ophelia.model.workspace.configuration.ProxyConfiguration;
import com.thinkparity.ophelia.model.workspace.configuration.ProxyConstraints;
import com.thinkparity.ophelia.model.workspace.configuration.ProxyCredentials;
import com.thinkparity.ophelia.model.workspace.configuration.ProxyType;

/**
 * <b>Title:</b>thinkParity Ophelia Model Workspace Configuration
 * Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class ConfigurationImpl implements Configuration {

    /** A log4j wrapper. */
    protected static final Log4JWrapper logger;

    /** A list of event listeners. */
    private static final List<ConfigurationListener> listenerList;

    static {
        listenerList = new ArrayList<ConfigurationListener>(7);
        logger = new Log4JWrapper(ConfigurationImpl.class);
    }

    /**
     * Determine if the configuration listener is registered.
     * 
     * @param listener
     *            A <code>ConfigurationListener</code>.
     * @return A <code>boolean</code>.
     */
    private static boolean containsListener(final ConfigurationListener listener) {
        return -1 < indexOfListener(listener);
    }

    /**
     * Obtain the index of the configuration listener.
     * 
     * @param listener
     *            A <code>ConfigurationListener</code>.
     * @return An <code>int</code>.
     */
    private static int indexOfListener(final ConfigurationListener listener) {
        int i = 0;
        for (final Iterator<ConfigurationListener> iListener =
            listenerList.iterator(); iListener.hasNext(); ) {
            if (iListener.next().equals(listener)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * Notify all listeners.
     * 
     * @param notifier
     *            An <code>EventNotifier<ConfigurationListener></code>.
     */
    private static void notifyListeners(final Workspace workspace,
            final EventNotifier<ConfigurationListener> notifier) {
        final List<ConfigurationListener> cloneList = new ArrayList<ConfigurationListener>(listenerList.size());
        cloneList.addAll(listenerList);
        workspace.newThread("TPS-OpheliaModel-NotifyListeners", new Runnable() {
    
            /**
             * @see java.lang.Runnable#run()
             *
             */
            @Override
            public void run() {
                for (final ConfigurationListener listener : cloneList) {
                    try {
                        notifier.notifyListener(listener);
                    } catch (final Throwable t) {
                        logger.logWarning(t, "Cannot notify configuration listener.");
                    }
                }
            }
        }).start();
    }

    /**
     * Wrap the listener in a weak reference.
     * 
     * @param listener
     *            A <code>ConfigurationListener</code>.
     * @return A <code>WeakReference<ConfigurationListener></code>.
     */
    private static ConfigurationListener wrapListener(
            final ConfigurationListener listener) {
        return listener;
    }

    /** A reference to the workspace. */
    private final Workspace workspace;

    /**
     * Create ConfigurationImpl.
     * 
     * @param workspace
     *            A <code>Workspace</code>.
     */
    protected ConfigurationImpl(final Workspace workspace) {
        super();
        this.workspace = workspace;
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.configuration.Configuration#addListener(com.thinkparity.ophelia.model.events.ConfigurationListener)
     *
     */
    @Override
    public void addListener(final ConfigurationListener listener) {
        if (containsListener(listener)) {
            return;
        } else {
            listenerList.add(wrapListener(listener));
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.configuration.Configuration#deleteProxyConfiguration()
     *
     */
    @Override
    public void deleteProxyConfiguration() {
        unsetHttpProxy();
        unsetSocksProxy();
        notifyDeleted();
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.configuration.Configuration#isSetProxyConfiguration()
     *
     */
    @Override
    public Boolean isSetProxyConfiguration() {
        return isSet(Keys.Proxy.Http.NETWORK_ADDRESS_HOST,
                Keys.Proxy.Http.NETWORK_ADDRESS_PORT,
                Keys.Proxy.Socks.NETWORK_ADDRESS_HOST,
                Keys.Proxy.Socks.NETWORK_ADDRESS_PORT);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.configuration.Configuration#readProxyConfiguration()
     *
     */
    @Override
    public ProxyConfiguration readProxyConfiguration() {
        if (isSetProxyConfiguration()) {
            final ProxyConfiguration configuration = new ProxyConfiguration();
            configuration.setHttp(getHttpProxy());
            configuration.setHttpCredentials(getHttpProxyCredentials());
            configuration.setSocks(getSocksProxy());
            return configuration;
        } else {
            return null;
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.configuration.Configuration#removeListener(com.thinkparity.ophelia.model.events.ConfigurationListener)
     *
     */
    @Override
    public void removeListener(final ConfigurationListener listener) {
        if (containsListener(listener)) {
            listenerList.remove(indexOfListener(listener));
        } else {
            return;
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.configuration.Configuration#updateProxyConfiguration(com.thinkparity.ophelia.model.workspace.configuration.ProxyConfiguration)
     *
     */
    @Override
    public void updateProxyConfiguration(final ProxyConfiguration configuration) {
        validate(configuration);
        unsetHttpProxy();
        setHttpProxy(configuration.getHttp(), configuration.getHttpCredentials());
        unsetSocksProxy();
        setSocksProxy(configuration.getSocks());
        notifyUpdated(configuration);
    }

    /**
     * Obtain the configuration for the http proxy.
     * 
     * @return A <code>Proxy</code>.
     */
    protected final Proxy getHttpProxy() {
        final Proxy http = new Proxy();
        http.setHost(getString(Keys.Proxy.Http.NETWORK_ADDRESS_HOST));
        http.setPort(getInteger(Keys.Proxy.Http.NETWORK_ADDRESS_PORT));
        http.setType(ProxyType.HTTP);
        return http;
    }

    /**
     * Obtain the configuration for the http proxy credentials.
     * 
     * @return A <code>Proxy</code>.
     */
    protected final ProxyCredentials getHttpProxyCredentials() {
        if (isSetHttpProxyCredentials()) {
            final ProxyCredentials credentials = new ProxyCredentials();
            credentials.setPassword(getString(Keys.Proxy.Http.Credentials.PASSWORD));
            credentials.setUsername(getString(Keys.Proxy.Http.Credentials.USERNAME));
            return credentials;
        } else {
            return null;
        }
    }

    /**
     * Read an integer value.
     * 
     * @param key
     *            A <code>String</code>.
     * @return An <code>Integer</code>.
     */
    protected abstract Integer getInteger(String key);

    /**
     * Obtain the configuration for the socks proxy.
     * 
     * @return A <code>Proxy</code>.
     */
    protected final Proxy getSocksProxy() {
        final Proxy http = new Proxy();
        http.setHost(getString(Keys.Proxy.Socks.NETWORK_ADDRESS_HOST));
        http.setPort(getInteger(Keys.Proxy.Socks.NETWORK_ADDRESS_PORT));
        http.setType(ProxyType.SOCKS);
        return http;
    }

    /**
     * Read a string value.
     * 
     * @param key
     *            A <code>String</code>.
     * @return A <code>String</code>.
     */
    protected abstract String getString(final String key);

    /**
     * Determine if a value is set.
     * 
     * @param key
     *            A <code>String</code>.
     * @return A <code>Boolean</code>.
     */
    protected final Boolean isSet(final String... keyArray) {
        for (final String key : keyArray) {
            if (!isSet(key)) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     * Determine if a value is set.
     * 
     * @param key
     *            A <code>String</code>.
     * @return A <code>Boolean</code>.
     */
    protected abstract Boolean isSet(String key);

    /**
     * Determine whether or not the http proxy credentials are set.
     * 
     * @return A <code>Boolean</code>.
     */
    protected final Boolean isSetHttpProxyCredentials() {
        return isSet(Keys.Proxy.Http.Credentials.PASSWORD,
                Keys.Proxy.Http.Credentials.USERNAME);
    }

    /**
     * Notify the configuration was deleted.
     * 
     */
    protected final void notifyDeleted() {
        notifyListeners(workspace, new EventNotifier<ConfigurationListener>() {
            public void notifyListener(final ConfigurationListener listener) {
                listener.configurationDeleted(new ConfigurationEvent());
            }
        });
    }

    /**
     * Notify the configuration was updated.
     * 
     */
    protected final void notifyUpdated(final ProxyConfiguration proxyConfiguration) {
        notifyListeners(workspace, new EventNotifier<ConfigurationListener>() {
            public void notifyListener(final ConfigurationListener listener) {
                listener.configurationUpdated(new ConfigurationEvent(proxyConfiguration));
            }
        });
    }

    /**
     * Set an integer key/value pair.
     * 
     * @param key
     *            A <code>String</code>.
     * @param value
     *            A <code>String</code>.
     */
    protected abstract void set(String key, Integer value);

    /**
     * Set a string key/value pair.
     * 
     * @param key
     *            A <code>String</code>.
     * @param value
     *            A <code>String</code>.
     */
    protected abstract void set(String key, String value);

    /**
     * Set the http proxy.
     * 
     * @param proxy
     *            A <code>Proxy</code>.
     * @param credentials
     *            A set of <code>ProxyCredentials</code>.
     */
    protected final void setHttpProxy(final Proxy proxy,
            final ProxyCredentials credentials) {
        set(Keys.Proxy.Http.NETWORK_ADDRESS_HOST, proxy.getHost());
        set(Keys.Proxy.Http.NETWORK_ADDRESS_PORT, proxy.getPort());
        unset(Keys.Proxy.Http.Credentials.PASSWORD);
        unset(Keys.Proxy.Http.Credentials.USERNAME);
        if (null == credentials) {
            logger.logInfo("Clearing proxy credentials.");
        } else {
            logger.logInfo("Setting proxy credentials.");
            set(Keys.Proxy.Http.Credentials.PASSWORD, credentials.getPassword());
            set(Keys.Proxy.Http.Credentials.USERNAME, credentials.getUsername());
        }
    }

    /**
     * Set the socks proxy.
     * 
     * @param proxy
     *            A <code>Proxy</code>.
     */
    protected final void setSocksProxy(final Proxy proxy) {
        set(Keys.Proxy.Socks.NETWORK_ADDRESS_HOST, proxy.getHost());
        set(Keys.Proxy.Socks.NETWORK_ADDRESS_PORT, proxy.getPort());
    }

    /**
     * Delete a value.
     * 
     * @param key
     *            A <code>String</code>.
     */
    protected abstract void unset(String key);

    /**
     * Unset the http proxy configuration.
     * 
     */
    protected final void unsetHttpProxy() {
        unset(Keys.Proxy.Http.NETWORK_ADDRESS_HOST);
        unset(Keys.Proxy.Http.NETWORK_ADDRESS_PORT);
        unset(Keys.Proxy.Http.Credentials.PASSWORD);
        unset(Keys.Proxy.Http.Credentials.USERNAME);
    }

    /**
     * Unset the socks proxy configuration.
     * 
     */
    protected final void unsetSocksProxy() {
        unset(Keys.Proxy.Socks.NETWORK_ADDRESS_HOST);
        unset(Keys.Proxy.Socks.NETWORK_ADDRESS_PORT);
    }

    /**
     * Validate the proxy configuration.
     * 
     * @param configuration
     *            A <code>ProxyConfiguration</code>.
     */
    protected final void validate(final ProxyConfiguration configuration) {
        final ProxyConstraints constraints = ProxyConstraints.getInstance();
        constraints.getHttpProxyHost().validate(configuration.getHttp().getHost());
        constraints.getHttpProxyPort().validate(configuration.getHttp().getPort());
        if (configuration.isSetHttpCredentials()) {
            constraints.getHttpProxyPassword().validate(configuration.getHttpCredentials().getPassword());
            constraints.getHttpProxyUsername().validate(configuration.getHttpCredentials().getUsername());
        }
        constraints.getSocksProxyHost().validate(configuration.getSocks().getHost());
        constraints.getSocksProxyPort().validate(configuration.getSocks().getPort());
    }

    /** <b>Title:</b>Configuration Keys<br> */
    private static final class Keys {
        private static final class Proxy {
            private static final class Http {
                private static final String NETWORK_ADDRESS_HOST = "Proxy.Http.NetworkAddress.Host";
                private static final String NETWORK_ADDRESS_PORT = "Proxy.Http.NetworkAddress.Port";
                private static final class Credentials {
                    private static final String PASSWORD = "Proxy.Http.Credentials.Password";
                    private static final String USERNAME = "Proxy.Http.Credentials.Username";
                }
            }
            private static final class Socks {
                private static final String NETWORK_ADDRESS_HOST = "Proxy.Socks.NetworkAddress.Host";
                private static final String NETWORK_ADDRESS_PORT = "Proxy.Socks.NetworkAddress.Port";
            }
        }
    }
}
