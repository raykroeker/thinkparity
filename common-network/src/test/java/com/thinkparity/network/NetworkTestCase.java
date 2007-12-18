/*
 * Created On:  20-Aug-07 8:55:50 AM
 */
package com.thinkparity.network;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.text.MessageFormat;
import java.util.List;

import com.thinkparity.codebase.junitx.TestCase;

/**
 * <b>Title:</b>thinkParity Network Test Case<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class NetworkTestCase extends TestCase {

    /**
     * Create NetworkTestCase.
     * 
     * @param name
     *            A test name <code>String</code>.
     */
    public NetworkTestCase(final String name) {
        super(name);
    }

    /**
     * Obtain the network connect address.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @return A <code>NetworkAddress</code>.
     */
    protected final NetworkAddress getNetworkConnectAddress(
            final NetworkProtocol protocol) {
        if (protocol.isSecure()) {
            return new NetworkAddress("google.ca", 443);
        } else {
            return new NetworkAddress("google.ca", 80);
        }
    }

    /**
     * Obtain the network connect address.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>NetworkProxy</code>.
     */
    protected final NetworkProxy newProxy(final NetworkProtocol protocol,
            final NetworkAddress address) {
        final List<Proxy> proxyList = ProxySelector.getDefault().select(newURI(protocol, address));
        if (proxyList.isEmpty()) {
            return null;
        } else {
            final Proxy proxy = proxyList.get(0);
            if (Proxy.NO_PROXY == proxy || Proxy.Type.DIRECT == proxy.type()) {
                return null;
            } else {
                return new NetworkProxy(newNetworkAddress(proxy));
            }
        }
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Instantiate a network address for a proxy.
     * 
     * @param proxy
     *            A <code>Proxy</code>.
     * @return A <code>NetworkAddress</code>.
     */
    private NetworkAddress newNetworkAddress(final Proxy proxy) {
        final InetSocketAddress address = (InetSocketAddress) proxy.address();
        return new NetworkAddress(address.getHostName(), address.getPort());
    }

    /**
     * Instantiate a uri for a protocol/address.
     * 
     * @param protocol
     *            A <code>NetworkProtocol</code>.
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>URI</code>.
     */
    private URI newURI(final NetworkProtocol protocol, final NetworkAddress address) {
        final String uri = MessageFormat.format("{0}://{1}:{2}",
                protocol.getName(), address.getHost(),
                String.valueOf(address.getPort()));
        return URI.create(uri);
    }

    /** <b>Title:</b>Network Test Case Fixture<br> */
    protected abstract class Fixture {}
}
