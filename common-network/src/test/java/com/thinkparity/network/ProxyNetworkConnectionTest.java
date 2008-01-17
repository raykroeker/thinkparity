/*
 * Created On:  14-Dec-07 6:58:53 PM
 */
package com.thinkparity.network;

import com.thinkparity.net.Network;
import com.thinkparity.net.NetworkAddress;
import com.thinkparity.net.NetworkConnection;
import com.thinkparity.net.NetworkException;
import com.thinkparity.net.NetworkProtocol;
import com.thinkparity.net.NetworkProxy;

/**
 * <b>Title:</b>thinkParity Network Proxy Connection Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ProxyNetworkConnectionTest extends NetworkTestCase {

    /**
     * Create ProxyNetworkTest.
     *
     */
    public ProxyNetworkConnectionTest() {
        super("Proxy network connection");
    }

    /**
     * Test connecting.
     * 
     */
    public void testConnect() {
        final Network network = new Network();
        assertNotNull("Network is null.", network);

        NetworkProtocol protocol;
        NetworkAddress address;
        NetworkConnection connection;
        NetworkProxy proxy;

        protocol = NetworkProtocol.getProtocol("socket");
        address = getNetworkConnectAddress(protocol);
        proxy = newProxy(protocol, address);
        assertNotNull("Network proxy is null.", proxy);
        connection = network.newConnection(protocol, address);
        assertNotNull("Network connection is null.", connection);
        try {
            connection.connect(proxy);
        } catch (final NetworkException nx) {
            fail(nx, "Failed to connect.");
        }
        assertTrue("Network connection does not report connected.",
                connection.isConnected());
    }

    /**
     * Test connecting.
     * 
     */
    public void testConnectNullProxy() {
        final Network network = new Network();
        assertNotNull("Network is null.", network);

        NetworkProtocol protocol;
        NetworkAddress address;
        NetworkConnection connection;
        NetworkProxy proxy;

        protocol = NetworkProtocol.getProtocol("socket");
        address = getNetworkConnectAddress(protocol);
        proxy = null;
        assertNull("Network proxy is not null.", proxy);
        connection = network.newConnection(protocol, address);
        assertNotNull("Network connection is null.", connection);
        try {
            connection.connect(proxy);
        } catch (final NetworkException nx) {
            fail(nx, "Failed to connect.");
        }
        assertTrue("Network connection does not report connected.",
                connection.isConnected());
    }

    /**
     * @see com.thinkparity.net.NetworkTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see com.thinkparity.net.NetworkTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
