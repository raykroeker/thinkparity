/*
 * Created On:  16-Aug-07 12:00:25 PM
 */
package com.thinkparity.net;

/**
 * <b>Title:</b>thinkParity Network Proxy<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class NetworkProxy {

    /** The proxy network address. */
    private NetworkAddress address;

    /**
     * Create NetworkProxy.
     * 
     */
    public NetworkProxy() {
        this(null);
    }

    /**
     * Create NetworkProxy.
     * 
     * @param type
     *            A <code>Type</code>.
     * @param address
     *            A <code>NetworkAddress</code>.
     */
    public NetworkProxy(final NetworkAddress address) {
        super();
        setAddress(address);
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
     * Set the address.
     *
     * @param address
     *		A <code>NetworkAddress</code>.
     */
    public void setAddress(final NetworkAddress address) {
        this.address = address;
    }
}
