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
public final class NetworkTest extends NetworkTestCase {

    /**
     * Create NetworkTest.
     *
     */
    public NetworkTest() {
        super("Network");
    }

    /**
     * Test obtaining network configuration.
     * 
     */
    public void testGetConfiguration() {
        final Network network = new Network();
        assertNotNull("Network is null.", network);
        final NetworkConfiguration config = network.getConfiguration();
        assertNotNull("Network configuration is null.", config);
    }

    /**
     * Test instantiating a network.
     * 
     */
    public void testNew() {
        final Network network = new Network();
        assertNotNull("Network is null.", network);
    }

    /**
     * Test instantiating a connection.
     * 
     */
    public void testNewConnection() {
        final Network network = new Network();
        assertNotNull("Network is null.", network);

        NetworkProtocol protocol;
        NetworkAddress address;
        NetworkConnection connection;

        protocol = NetworkProtocol.getProtocol("socket");
        address = getNetworkConnectAddress(protocol);
        connection = network.newConnection(protocol, address);
        assertNotNull("Network connection is null.", connection);
    }

    /**
     * Test instantiating a secure connection.
     * 
     */
    public void testNewSecureConnection() {
        final Network network = new Network();
        assertNotNull("Network is null.", network);

        NetworkProtocol protocol;
        NetworkAddress address;
        NetworkConnection connection;

        protocol = NetworkProtocol.getSecureProtocol("socket");
        address = getNetworkConnectAddress(protocol);
        connection = network.newConnection(protocol, address);
        assertNotNull("Secure network connection is null.", connection);
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
