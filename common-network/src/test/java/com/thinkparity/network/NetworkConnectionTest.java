/*
 * Created On:  14-Dec-07 6:58:53 PM
 */
package com.thinkparity.network;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NetworkConnectionTest extends NetworkTestCase {

    /**
     * Create NetworkTest.
     *
     */
    public NetworkConnectionTest() {
        super("Network connection");
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

        protocol = NetworkProtocol.getProtocol("socket");
        address = getNetworkConnectAddress(protocol);
        connection = network.newConnection(protocol, address);
        assertNotNull("Network connection is null.", connection);
        try {
            connection.connect();
        } catch (final NetworkException nx) {
            fail(nx, "Failed to connect.");
        }
        assertTrue("Network connection does not report connected.",
                connection.isConnected());
    }

    /**
     * Test connecting to a secure protocol.
     * 
     */
    public void testConnectSecure() {
        final Network network = new Network();
        assertNotNull("Network is null.", network);

        NetworkProtocol protocol;
        NetworkAddress address;
        NetworkConnection connection;

        protocol = NetworkProtocol.getSecureProtocol("socket");
        address = getNetworkConnectAddress(protocol);
        connection = network.newConnection(protocol, address);
        assertNotNull("Network connection is null.", connection);
        try {
            connection.connect();
        } catch (final NetworkException nx) {
            fail(nx, "Failed to connect.");
        }
        assertTrue("Network connection does not report connected.",
                connection.isConnected());
    }

    /**
     * @see com.thinkparity.network.NetworkTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see com.thinkparity.network.NetworkTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
