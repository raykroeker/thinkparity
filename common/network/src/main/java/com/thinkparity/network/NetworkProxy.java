/*
 * Created On:  16-Aug-07 12:00:25 PM
 */
package com.thinkparity.network;

/**
 * <b>Title:</b>thinkParity Network Proxy<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class NetworkProxy {

    public static final NetworkProxy NO_PROXY;

    static {
        NO_PROXY = new NetworkProxy(Type.DIRECT);
    }

    /** The proxy network address. */
    private NetworkAddress address;

    /** The proxy type. */
    private Type type;

    /**
     * Create NetworkProxy.
     * 
     */
    public NetworkProxy() {
        this(null, null);
    }

    /**
     * Create NetworkProxy.
     * 
     * @param type
     *            A <code>Type</code>.
     * @param address
     *            A <code>NetworkAddress</code>.
     */
    public NetworkProxy(final Type type, final NetworkAddress address) {
        super();
        setType(type);
        setAddress(address);
    }

    /**
     * Create NetworkProxy.
     * 
     * @param type
     *            A <code>Type</code>.
     */
    private NetworkProxy(final Type type) {
        this(type, null);
    }

    /**
     * Obtain the address.
     *
     * @return A <code>NetworkAddress</code>.
     */
    public NetworkAddress getAddress() {
        return address;
    }

    /**
     * Obtain the type.
     *
     * @return A <code>Type</code>.
     */
    public Type getType() {
        return type;
    }

    /**
     * Set the address.
     *
     * @param address
     *		A <code>NetworkAddress</code>.
     */
    public void setAddress(final NetworkAddress address) {
        this.address = address;
    }

    /**
     * Set the type.
     *
     * @param type
     *		A <code>Type</code>.
     */
    public void setType(final Type type) {
        this.type = type;
    }

    /** <b>Title:</b>Network Proxy Type<br> */
    public enum Type { DIRECT, HTTP, SOCKS }
}
