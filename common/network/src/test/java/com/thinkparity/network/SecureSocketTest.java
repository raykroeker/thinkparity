/*
 * Created On:  20-Aug-07 8:57:12 AM
 */
package com.thinkparity.network;

import java.util.Arrays;



/**
 * <b>Title:</b>thinkParity Network Socket Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SecureSocketTest extends NetworkTestCase {

    /** Test name. */
    private static final String NAME = "Network socket test";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create SocketTest.
     *
     */
    public SecureSocketTest() {
        super(NAME);
    }

    /**
     * Test connecting.
     * 
     */
    public void testConnect() {
        TEST_LOGGER.logApiId();
        TEST_LOGGER.logVariable("protocol.getName()", datum.protocol.getName());
        final NetworkAddress address = newClientAddress();
        TEST_LOGGER.logVariable("address.getHost()", address.getHost());
        TEST_LOGGER.logVariable("address.getPort()", address.getPort());
        final NetworkConnection connection = datum.network.newConnection(
                datum.protocol, address);
        assertNotNull(connection);
        assertFalse("Connected.", connection.isConnected());
        try {
            connection.connect();
        } catch (final NetworkException nx) {
            fail(nx, "Could not connect network.");
        }
        try {
            assertTrue("Not connected.", connection.isConnected());
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Test creating a new connection.
     * 
     */
    public void testNewConnection() {
        final NetworkAddress address = newClientAddress();
        final NetworkConnection connection = datum.network.newConnection(
                datum.protocol, address);
        assertNotNull(connection);
        assertFalse("Connected.", connection.isConnected());
    }

    /**
     * Test reading from a connection.
     * 
     */
    public void testRead() {
        final NetworkAddress address = newClientAddress();
        final NetworkConnection connection = datum.network.newConnection(
                datum.protocol, address);
        assertNotNull(connection);
        assertFalse("Connected.", connection.isConnected());
        try {
            connection.connect();
        } catch (final NetworkException nx) {
            fail(nx, "Connect failed.");
        }
        assertTrue("Not connected.", connection.isConnected());
        try {
            final byte[] writeBytes = (getName() + '\n').getBytes();
            connection.write(writeBytes);
            final byte[] readBytes = new byte[writeBytes.length];
            final int length = connection.read(readBytes);
            assertEquals("Read failed.", writeBytes.length, length);
            assertTrue("Read failed", Arrays.equals(writeBytes, readBytes));
        } catch (final NetworkException nx) {
            fail(nx, "Network error.");
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Test reading from a connection.
     * 
     */
    public void testWrite() {
        final NetworkAddress address = newClientAddress();
        final NetworkConnection connection = datum.network.newConnection(
                datum.protocol, address);
        assertNotNull(connection);
        assertFalse("Connected.", connection.isConnected());
        try {
            connection.connect();
        } catch (final NetworkException nx) {
            fail(nx, "Connect failed.");
        }
        assertTrue("Not connected.", connection.isConnected());
        try {
            connection.write((getName() + '\n').getBytes());
        } catch (final NetworkException nx) {
            fail(nx, "Network error.");
        } finally {
            connection.disconnect();
        }
    }

    /**
     * @see com.thinkparity.network.NetworkTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        if (false == SecureSocketTestServer.isRunning()) {
            SecureSocketTestServer.start(newServerAddress());
        }
        datum = new Fixture(new Network(), NetworkProtocol.getSecureProtocol("socket"));
    }

    /**
     * @see com.thinkparity.network.NetworkTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;
        super.tearDown();
    }

    /**
     * Create a new client address.
     * 
     * @return A <code>NetworkAddress</code>.
     */
    private NetworkAddress newClientAddress() {
        return new NetworkAddress("192.168.1.5", 8080);
    }

    /**
     * Create a new server address.
     * 
     * @return A <code>NetworkAddress</code>.
     */
    private NetworkAddress newServerAddress() {
        return new NetworkAddress("192.168.1.5", 8080);
    }

    /** <b>Title:</b>Socket Test Fixture<br> */
    private class Fixture extends NetworkTestCase.Fixture {
        private final Network network;
        private final NetworkProtocol protocol;
        private Fixture(final Network network, final NetworkProtocol protocol) {
            super();
            this.network = network;
            this.protocol = protocol;
        }
    }
}
