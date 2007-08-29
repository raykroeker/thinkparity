/*
 * Created On:  20-Aug-07 3:19:39 PM
 */
package com.thinkparity.network;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class NetworkUtil {

    /**
     * Create an address.
     * 
     * @param host
     *            A <code>String</code>.
     * @param port
     *            A <code>Integer</code>.
     * @return A <code>NetworkAddress</code>.
     */
    static NetworkAddress newAddress(final String host, final Integer port) {
        return new NetworkAddress(host, port);
    }

    /**
     * Create a proxy.
     * 
     * @param proxy
     *            A <code>NetworkProxy</code>.
     * @return A <code>Proxy</code>.
     */
    static Proxy newProxy(final NetworkProxy proxy) {
        if (NetworkProxy.NO_PROXY == proxy) {
            return Proxy.NO_PROXY;
        } else {
            final Proxy.Type type;
            switch (proxy.getType()) {
            case HTTP:
                type = Proxy.Type.HTTP;
                break;
            case SOCKS:
                type = Proxy.Type.SOCKS;
                break;
            default:
                throw new IllegalArgumentException("Cannot determine proxy type.");
            }            
            return new Proxy(type, newSocketAddress(proxy.getAddress()));
        }
    }

    /**
     * Create a proxy.
     * 
     * @param proxy
     *            A <code>Proxy</code>.
     * @return A <code>NetworkProxy</code>.
     */
    static NetworkProxy newProxy(final Proxy proxy) {
        if (Proxy.NO_PROXY == proxy) {
            return NetworkProxy.NO_PROXY;
        } else {
            final NetworkProxy.Type type;
            switch (proxy.type()) {
            case HTTP:
                type = NetworkProxy.Type.HTTP;
                break;
            case SOCKS:
                type = NetworkProxy.Type.SOCKS;
                break;
            default:
                throw new IllegalArgumentException("Unexpected proxy type.");
            }
            return new NetworkProxy(type, newAddress(
                    ((InetSocketAddress) proxy.address()).getHostName(),
                    ((InetSocketAddress) proxy.address()).getPort()));
        }
    }

    /**
     * Create a socket address.
     * 
     * @param address
     *            A <code>NetworkAddress</code>.
     * @return A <code>SocketAddress</code>.
     */
    static SocketAddress newSocketAddress(final NetworkAddress address) {
        return new InetSocketAddress(address.getHost(), address.getPort());
    }

    /**
     * Create NetworkUtil.
     *
     */
    private NetworkUtil() {
        super();
    }
}
