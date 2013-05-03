/*
 * Created On:  16-Aug-07 11:25:45 AM
 */
package com.thinkparity.net;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * <b>Title:</b>thinkParity Network<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Network {

    /** The implementation. */
    private final NetworkImpl impl;

    /** A network logger. */
    private final Log4JWrapper logger;

    /**
     * Create Network.
     *
     */
    public Network() {
        super();
        this.logger = new Log4JWrapper("com.thinkparity.net");
        this.impl = new NetworkImpl(logger);
    }

    /**
     * Disconnect all connections.
     * 
     */
    public void disconnect() {
        impl.disconnect();
    }

    /**
     * Obtain the network configuration.
     * 
     * @return A <code>NetworkConfiguration</code>.
     */
    public NetworkConfiguration getConfiguration() {
        return impl.getConfiguration();
    }

    /**
     * Create a network connection for the protocol and address.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>NetworkConnection</code>.
     */
    public NetworkConnection newConnection(final NetworkProtocol protocol,
            final NetworkAddress address) {
        return impl.newConnnection(protocol, address);
    }
}
