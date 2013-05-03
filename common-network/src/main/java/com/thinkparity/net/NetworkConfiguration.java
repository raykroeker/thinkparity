/*
 * Created On:  19-Aug-07 7:50:01 PM
 */
package com.thinkparity.net;

import java.util.Hashtable;
import java.util.Map;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.net.log4j.or.NetworkAddressRenderer;
import com.thinkparity.net.log4j.or.NetworkConnectionRenderer;
import com.thinkparity.net.log4j.or.NetworkProtocolRenderer;
import com.thinkparity.net.log4j.or.NetworkProxyRenderer;
import com.thinkparity.net.log4j.or.NetworkRenderer;

/**
 * <b>Title:</b>thinkParity Network Configuration<br>
 * <b>Description:</b>A network configuration; allows for a hierarchy of
 * configuration as well as provides default configuration.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NetworkConfiguration {

    /** The default configuration. */
    private static final Map<String, Object> DEFAULTS;

    /** A log4j wrapper. */
    private static final Log4JWrapper LOGGER;

    /** The per-address configuration. */
    private static Map<NetworkAddress, Map<String, Object>> perAddress;

    /** The per-connection configuration. */
    private static Map<NetworkConnection, Map<String, Object>> perConnection;

    /** The per-protocol configuration. */
    private static Map<NetworkProtocol, Map<String, Object>> perProtocol;

    /** The root configuration. */
    private static Map<String, Object> root;

    static {
        DEFAULTS = newConfigurationMap();
        DEFAULTS.put(Names.Network.Connection.CONNECT_TIMEOUT, 2250);
        DEFAULTS.put(Names.Network.Connection.SO_LINGER, -1);
        DEFAULTS.put(Names.Network.Connection.SO_TIMEOUT, 0);
        DEFAULTS.put(Names.Network.Connection.TCP_NODELAY, Boolean.TRUE);
        DEFAULTS.put(Names.Network.Address.CACHE_TTL, Integer.MAX_VALUE);
        DEFAULTS.put(Names.Network.Address.LOOKUP_TIMEOUT, 3500);
        LOGGER = new Log4JWrapper("com.thinkparity.net.configuration");
        LOGGER.setRenderer(Network.class, NetworkRenderer.class);
        LOGGER.setRenderer(NetworkAddress.class, NetworkAddressRenderer.class);
        LOGGER.setRenderer(NetworkConnection.class, NetworkConnectionRenderer.class);
        LOGGER.setRenderer(NetworkProtocol.class, NetworkProtocolRenderer.class);
        LOGGER.setRenderer(NetworkProxy.class, NetworkProxyRenderer.class);
        LOGGER.configureRenderers();
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
     * Create a configuration map.
     * 
     * @return A <code>Map<String, Object></code>.
     */
    private static Map<String, Object> newConfigurationMap() {
        return new Hashtable<String, Object>(2, 1.0F);
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
     * Create NetworkConfiguration.
     *
     */
    NetworkConfiguration() {
        super();
    }

    /**
     * Obtain the address cache life time in ms.
     * 
     * @return An <code>Integer</code>.
     */
    public Integer getAddressCacheTTL() {
        return (Integer) get(Names.Network.Address.CACHE_TTL);
    }

    /**
     * Obtain the address cache life time in ms.
     * 
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return An <code>Integer</code>.
     */
    public Integer getAddressCacheTTL(final NetworkAddress address) {
        return (Integer) get(address, Names.Network.Address.CACHE_TTL);
    }
    
    /**
     * Obtain the address lookup timeout in ms.
     * 
     * @return An <code>Integer</code>.
     */
    public Integer getAddressLookupTimeout() {
        return (Integer) get(Names.Network.Address.LOOKUP_TIMEOUT);
    }

    /**
     * Obtain the connect timeout.
     * 
     * @return An <code>Integer</code>.
     */
    public Integer getConnectTimeout() {
        return (Integer) get(Names.Network.Connection.CONNECT_TIMEOUT);
    }
    
    /**
     * Obtain the connect timeout.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @return An <code>Integer</code>.
     */
    public Integer getConnectTimeout(final NetworkProtocol protocol) {
        return (Integer) get(protocol,
                Names.Network.Connection.CONNECT_TIMEOUT);
    }

    /**
     * Obtain the connect timeout.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddresss</code>.
     * @return An <code>Integer</code>.
     */
    public Integer getConnectTimeout(final NetworkProtocol protocol,
            final NetworkAddress address) {
        return (Integer) get(protocol, address,
                Names.Network.Connection.CONNECT_TIMEOUT);
    }

    /**
     * Obtain the connect timeout.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddresss</code>.
     * @param connection
     *            A <code>NetworkConnection</code>.
     * @return An <code>Integer</code>.
     */
    public Integer getConnectTimeout(final NetworkProtocol protocol,
            final NetworkAddress address, final NetworkConnection connection) {
        return (Integer) get(protocol, address, connection,
                Names.Network.Connection.CONNECT_TIMEOUT);
    }

    /**
     * Set the connection proxy.
     * 
     * @param connection
     *            A <code>NetworkConnection</code>.
     * @param proxy
     *            A <code>NetworkProxy</code>.
     */
    public NetworkProxy getProxy(final NetworkConnection connection) {
        return (NetworkProxy) get(connection, Names.Network.Connection.PROXY);
    }

    /**
     * Obtain the socket linger duration.
     * 
     * @return An <code>Integer</code>.
     */
    public Integer getSoLinger() {
        return (Integer) get(Names.Network.Connection.SO_LINGER);
    }

    /**
     * Obtain the socket linger duration.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @return An <code>Integer</code>.
     */
    public Integer getSoLinger(final NetworkProtocol protocol) {
        return (Integer) get(protocol, Names.Network.Connection.SO_LINGER);
    }

    /**
     * Obtain the socket linger duration.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddresss</code>.
     * @return An <code>Integer</code>.
     */
    public Integer getSoLinger(final NetworkProtocol protocol,
            final NetworkAddress address) {
        return (Integer) get(protocol, address,
                Names.Network.Connection.SO_LINGER);
    }

    /**
     * Obtain the socket linger duration.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddresss</code>.
     * @param proxy
     *            A <code>NetworkProxy</code>.
     * @param connection
     *            A <code>NetworkConnection</code>.
     * @return An <code>Integer</code>.
     */
    public Integer getSoLinger(final NetworkProtocol protocol,
            final NetworkAddress address, final NetworkConnection connection) {
        return (Integer) get(protocol, address, connection,
                Names.Network.Connection.SO_LINGER);
    }

    /**
     * Obtain the socket timeout.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddresss</code>.
     * @param proxy
     *            A <code>NetworkProxy</code>.
     * @param connection
     *            A <code>NetworkConnection</code>.
     * @return An <code>Integer</code>.
     */
    public Integer getSoTimeout() {
        return (Integer) get(Names.Network.Connection.SO_TIMEOUT);
    }

    /**
     * Obtain the socket timeout.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @return An <code>Integer</code>.
     */
    public Integer getSoTimeout(final NetworkProtocol protocol) {
        return (Integer) get(protocol, Names.Network.Connection.SO_TIMEOUT);
    }

    /**
     * Obtain the socket timeout.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddresss</code>.
     * @param proxy
     *            A <code>NetworkProxy</code>.
     * @return An <code>Integer</code>.
     */
    public Integer getSoTimeout(final NetworkProtocol protocol,
            final NetworkAddress address) {
        return (Integer) get(protocol, address,
                Names.Network.Connection.SO_TIMEOUT);
    }

    /**
     * Obtain the socket timeout.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddresss</code>.
     * @param proxy
     *            A <code>NetworkProxy</code>.
     * @param connection
     *            A <code>NetworkConnection</code>.
     * @return An <code>Integer</code>.
     */
    public Integer getSoTimeout(final NetworkProtocol protocol,
            final NetworkAddress address, final NetworkConnection connection) {
        return (Integer) get(protocol, address, connection,
                Names.Network.Connection.SO_TIMEOUT);
    }

    /**
     * Obtain the tcp no delay flag.
     * 
     * @return A <code>Boolean</code>.
     */
    public Boolean getTcpNoDelay() {
        return (Boolean) get(Names.Network.Connection.TCP_NODELAY);
    }

    /**
     * Obtain the tcp no delay flag.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @return A <code>Boolean</code>.
     */
    public Boolean getTcpNoDelay(final NetworkProtocol protocol) {
        return (Boolean) get(protocol, Names.Network.Connection.TCP_NODELAY);
    }

    /**
     * Obtain the tcp no delay flag.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddresss</code>.
     * @return A <code>Boolean</code>.
     */
    public Boolean getTcpNoDelay(final NetworkProtocol protocol,
            final NetworkAddress address) {
        return (Boolean) get(protocol, address,
                Names.Network.Connection.TCP_NODELAY);
    }

    /**
     * Obtain the tcp no delay flag.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddresss</code>.
     * @param connection
     *            A <code>NetworkConnection</code>.
     * @return A <code>Boolean</code>.
     */
    public Boolean getTcpNoDelay(final NetworkProtocol protocol,
            final NetworkAddress address, final NetworkConnection connection) {
        return (Boolean) get(protocol, address, connection,
                Names.Network.Connection.TCP_NODELAY);
    }

    /**
     * Set the address cache life time in ms.
     * 
     * @param ttl
     *            An <code>Integer</code>.
     */
    public void setAddressCacheTTL(final Integer ttl) {
        set(Names.Network.Address.CACHE_TTL, ttl);
    }

    /**
     * Set the address cache life time in ms.
     * 
     * @param address
     *            A <code>NetworkAddress</code>.
     * @param ttl
     *            An <code>Integer</code>.
     */
    public void setAddressCacheTTL(final NetworkAddress address,
            final Integer ttl) {
        set(address, Names.Network.Address.CACHE_TTL, ttl);
    }

    /**
     * Obtain the address lookup timeout in ms.
     * 
     * @return An <code>Integer</code>.
     */
    public void setAddressLookupTimeout(final Integer timeout) {
        set(Names.Network.Address.LOOKUP_TIMEOUT, timeout);
    }

    /**
     * Set the connect timeout.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddresss</code>.
     * @param connection
     *            A <code>NetworkConnection</code>.
     * @param connectTimeout
     *            An <code>Integer</code>.
     */
    public void setConnectTimeout(final Integer connectTimeout) {
        set(Names.Network.Connection.CONNECT_TIMEOUT, connectTimeout);
    }

    /**
     * Set the connect timeout.
     * 
     * @param address
     *            A <code>NetworkAddresss</code>.
     * @param connectTimeout
     *            An <code>Integer</code>.
     */
    public void setConnectTimeout(final NetworkAddress address,
            final Integer connectTimeout) {
        set(address, Names.Network.Connection.CONNECT_TIMEOUT, connectTimeout);
    }

    /**
     * Set the connect timeout.
     * 
     * @param connection
     *            A <code>NetworkConnection</code>.
     * @param connectTimeout
     *            An <code>Integer</code>.
     */
    public void setConnectTimeout(final NetworkConnection connection,
            final Integer connectTimeout) {
        set(connection, Names.Network.Connection.CONNECT_TIMEOUT, connectTimeout);
    }

    /**
     * Set the connect timeout.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param connectTimeout
     *            An <code>Integer</code>.
     */
    public void setConnectTimeout(final NetworkProtocol protocol,
            final Integer connectTimeout) {
        set(protocol, Names.Network.Connection.CONNECT_TIMEOUT, connectTimeout);
    }

    /**
     * Set the address proxy.
     * 
     * @param address
     *            A <code>NetworkConnection</code>.
     * @param proxy
     *            A <code>NetworkProxy</code>.
     */
    public void setProxy(final NetworkAddress address, final NetworkProxy proxy) {
        set(address, Names.Network.Connection.PROXY, proxy);
    }

    /**
     * Set the connection proxy.
     * 
     * @param connection
     *            A <code>NetworkConnection</code>.
     * @param proxy
     *            A <code>NetworkProxy</code>.
     */
    public void setProxy(final NetworkConnection connection,
            final NetworkProxy proxy) {
        set(connection, Names.Network.Connection.PROXY, proxy);
    }

    /**
     * Set the socket timeout.
     * 
     * @param timeout
     *            An <code>Integer</code>.
     */
    public void setSoTimeout(final Integer timeout) {
        set(Names.Network.Connection.SO_TIMEOUT, timeout);
    }

    /**
     * Set the socket timeout.
     * 
     * @param address
     *            A <code>NetworkAddresss</code>.
     * @param timeout
     *            An <code>Integer</code>.
     */
    public void setSoTimeout(final NetworkAddress address, final Integer timeout) {
        set(address, Names.Network.Connection.SO_TIMEOUT, timeout);
    }

    /**
     * Set the socket timeout.
     * 
     * @param connection
     *            A <code>NetworkConnection</code>.
     * @param timeout
     *            An <code>Integer</code>.
     */
    public void setSoTimeout(final NetworkConnection connection,
            final Integer timeout) {
        set(connection, Names.Network.Connection.SO_TIMEOUT, timeout);
    }

    /**
     * Set the socket timeout.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param timeout
     *            An <code>Integer</code>.
     */
    public void setSoTimeout(final NetworkProtocol protocol, final Integer timeout) {
        set(protocol, Names.Network.Connection.SO_TIMEOUT, timeout);
    }

    /**
     * Obtain a address configuration.
     * 
     * @param address
     *            A <code>NetworkAddress</code>.
     * @param name
     *            A name <code>String</code>.
     * @return An <code>Object</code>.
     */
    private synchronized Object get(final NetworkAddress address,
            final String name) {
        if (null == perAddress) {
            return get(name);
        } else {
            if (perAddress.containsKey(address)) {
                final Map<String, Object> values = perAddress.get(address);
                if (values.containsKey(name)) {
                    LOGGER.logInfo("Get \"{0}\":\"{1}\" for {2}.", name,
                            values.get(name), address);
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
     * Obtain a connection configuration.
     * 
     * @param connection
     *            A <code>NetworkConnection</code>.
     * @param name
     *            A name <code>String</code>.
     * @return An <code>Object</code>.
     */
    private synchronized Object get(final NetworkConnection connection,
            final String name) {
        if (null == perConnection) {
            return get(name);
        } else {
            if (perConnection.containsKey(connection)) {
                final Map<String, Object> values = perConnection.get(connection);
                if (values.containsKey(name)) {
                    LOGGER.logInfo("Get \"{0}\":\"{1}\" for {2}.", name,
                            values.get(name), connection);
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
     * Obtain a connection; proxy; address; protocol configuration.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddress</code>.
     * @param proxy
     *            A <code>NetworkProxy</code>.
     * @param connection
     *            A <code>NetworkConnection</code>.
     * @param name
     *            A name <code>String</code>.
     * @return An <code>Object</code>.
     */
    private synchronized Object get(final NetworkProtocol protocol,
            final NetworkAddress address, final NetworkConnection connection,
            final String name) {
        if (null == perConnection) {
            return get(protocol, address, name);
        } else {
            if (perConnection.containsKey(connection)) {
                final Map<String, Object> values = perConnection.get(connection);
                if (values.containsKey(name)) {
                    LOGGER.logInfo("Get \"{0}\":\"{1}\" for {2}.", name,
                            values.get(name), connection);
                    return values.get(name);
                } else {
                    return get(protocol, address, name);
                }
            } else {
                return get(protocol, address, name);
            }
        }
    }

    /**
     * Obtain an address; protocol configuration.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddress</code>.
     * @param name
     *            A name <code>String</code>.
     * @return An <code>Object</code>.
     */
    private synchronized Object get(final NetworkProtocol protocol,
            final NetworkAddress address, final String name) {
        if (null == perAddress) {
            return get(protocol, name);
        } else {
            if (perAddress.containsKey(address)) {
                final Map<String, Object> values = perAddress.get(address);
                if (values.containsKey(name)) {
                    LOGGER.logInfo("Get \"{0}\":\"{1}\" for {2}.", name,
                            values.get(name), address);
                    return values.get(name);
                } else {
                    return get(protocol, name);
                }
            } else {
                return get(protocol, name);
            }
        }
    }

    /**
     * Obtain a protocol configuration.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param name
     *            A name <code>String</code>.
     * @return An <code>Object</code>.
     */
    private synchronized Object get(final NetworkProtocol protocol,
            final String name) {
        if (null == perProtocol) {
            return get(name);
        } else {
            if (perProtocol.containsKey(protocol)) {
                final Map<String, Object> values = perProtocol.get(protocol);
                if (values.containsKey(name)) {
                    LOGGER.logInfo("Get \"{0}\":\"{1}\" for {2}.", name,
                            values.get(name), protocol);
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
     * Set a per-address configuration.
     * 
     * @param address
     *            A <code>NetworkAddress</code>.
     * @param name
     *            A <code>String</code>.
     * @param value
     *            An <code>Object</code>.
     */
    private synchronized void set(final NetworkAddress address,
            final String name, final Object value) {
        if (null == perAddress) {
            perAddress = newConfigurationMap(address);
        }
        ensureEntry(perAddress, address);
        LOGGER.logInfo("Set \"{0}\":\"{1}\" for {2}.", name, value, address);
        perAddress.get(address).put(name, value);
    }

    /**
     * Set a connection configuration.
     * 
     * @param connection
     *            A <code>NetworkConnection</code>.
     * @param name
     *            A <code>String</code>.
     * @param value
     *            An <code>Object</code>.
     */
    private synchronized void set(final NetworkConnection connection,
            final String name, final Object value) {
        if (null == perConnection) {
            perConnection = newConfigurationMap(connection);
        }
        ensureEntry(perConnection, connection);
        LOGGER.logInfo("Set \"{0}\":\"{1}\" for {2}.", name, value, connection);
        perConnection.get(connection).put(name, value);
    }

    /**
     * Set a protocol configuration.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param name
     *            A <code>String</code>.
     * @param value
     *            An <code>Object</code>.
     */
    private synchronized void set(final NetworkProtocol protocol,
            final String name, final Object value) {
        if (null == perProtocol) {
            perProtocol = newConfigurationMap(protocol);
        }
        ensureEntry(perProtocol, protocol);
        LOGGER.logInfo("Set \"{0}\":\"{1}\" for {2}.", name, value, protocol);
        perProtocol.get(protocol).put(name, value);
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

    /** <b>Title:</b>Network Configuration Entry Names<br> */
    private static final class Names {
        /** <b>Title:</b>Network Configuration Entry Names:  Network<br> */
        private static final class Network {
            /** <b>Title:</b>Network Configuration Entry Names:  Network:  Address<br> */
            private static final class Address {
                private static final String CACHE_TTL = "network.address.cache.ttl";
                private static final String LOOKUP_TIMEOUT = "network.address.lookup.timeout";
            }
            /** <b>Title:</b>Network Configuration Entry Names:  Network:  Connection<br> */
            private static final class Connection {
                private static final String CONNECT_TIMEOUT = "network.connection.connecttimeout";
                private static final String PROXY = "network.connection.proxy";
                private static final String SO_LINGER = "network.connection.solinger";
                private static final String SO_TIMEOUT = "network.connection.sotimeout";
                private static final String TCP_NODELAY = "network.connection.tcpnodelay";
            }
        }
    }
}
