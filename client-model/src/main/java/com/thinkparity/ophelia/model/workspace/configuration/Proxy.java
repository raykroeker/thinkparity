/*
 * Created On:  21-Dec-07 11:40:55 AM
 */
package com.thinkparity.ophelia.model.workspace.configuration;

/**
 * <b>Title:</b>thinkParity Common Proxy<br>
 * <b>Description:</b>Represents the proxy for the workspace configuration.
 * Currently accepts *only* proxies of type http.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Proxy {

    /** The host name. */
    private String host;

    /** The port. */
    private Integer port;

    /** The type. */
    private ProxyType type;

    /**
     * Create Proxy.
     *
     */
    public Proxy() {
        super();
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
     * Obtain the type.
     *
     * @return A <code>ProxyType</code>.
     */
    public ProxyType getType() {
        return type;
    }

    /**
     * Set the host.
     *
     * @param host
     *      A <code>String</code>.
     */
    public void setHost(final String hostName) {
        this.host = hostName;
    }

    /**
     * Set the port.
     *
     * @param address
     *      A <code>NetworkAddress</code>.
     */
    public void setPort(final Integer port) {
        this.port = port;
    }

    /**
     * Set the protocol.
     *
     * @param type
     *		A <code>ProxyType</code>.
     */
    public void setType(final ProxyType type) {
        this.type = type;
    }
}
