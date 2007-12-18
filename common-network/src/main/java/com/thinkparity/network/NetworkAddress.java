/*
 * Created On:  16-Aug-07 11:50:57 AM
 */
package com.thinkparity.network;

/**
 * <b>Title:</b>thinkParity Network Address<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class NetworkAddress {

    /** A host. */
    private String host;

    /** A port. */
    private Integer port;

    /**
     * Create NetworkAddress.
     *
     */
    public NetworkAddress() {
        this(null, null);
    }

    /**
     * Create NetworkAddress.
     * 
     * @param host
     *            A host <code>String</code>.
     * @param port
     *            A port <code>Integer</code>.
     */
    public NetworkAddress(final String host, final Integer port) {
        super();
        setHost(host);
        setPort(port);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     */
    @Override
    public boolean equals(final Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() == obj.getClass()) {
            return ((NetworkAddress) obj).host.equals(host)
                && ((NetworkAddress) obj).port.equals(port);
        }
        return false;
    }

    /**
     * Obtain the host.
     *
     * @return A <code>String</code>.
     */
    public String getHost() {
        return host;
    }

    /**
     * Obtain the port.
     *
     * @return A <code>Integer</code>.
     */
    public Integer getPort() {
        return port;
    }

    
    /**
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + ((port == null) ? 0 : port.hashCode());
        return result;
    }

    /**
     * Set the host.
     *
     * @param host
     *		A <code>String</code>.
     */
    public void setHost(final String host) {
        this.host = host;
    }

    /**
     * Set the port.
     *
     * @param port
     *		A <code>Integer</code>.
     */
    public void setPort(final Integer port) {
        this.port = port;
    }

    /**
     * @see java.lang.Object#toString()
     *
     */
    @Override
    public String toString() {
        return new StringBuilder(64)
            .append(NetworkAddress.class.getSimpleName())
            .append("$host:").append(host)
            .append(",port:").append(port)
            .toString();
    }
}
