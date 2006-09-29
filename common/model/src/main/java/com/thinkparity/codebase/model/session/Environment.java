/*
 * Created On: Aug 5, 2006 10:16:01 AM
 */
package com.thinkparity.codebase.model.session;

import com.thinkparity.codebase.NetworkUtil;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.0
 */
public enum Environment {

    /** A localhost development environment. */
    DEVELOPMENT_LOCALHOST("localhost", 5226, Protocol.XMPP),

    /** Raymond's development environment. */
    DEVELOPMENT_RAYMOND("thinkparity.dyndns.org", 5226, Protocol.XMPP),

    /** Robert's development environment. */
    DEVELOPMENT_ROBERT("thinkparity.dyndns.org", 5228, Protocol.XMPP),

    /** Production environment. */
    PRODUCTION("thinkparity.dyndns.org", 5222, Protocol.XMPP),

    /** Testing environment. */
    TESTING("thinkparity.dyndns.org", 5224, Protocol.XMPP),

    /** A localhost testing environment. */
    TESTING_LOCALHOST("localhost", 5224, Protocol.XMPP);

    /** The server host. */
    private transient String serverHost;

    /** The server port. */
    private transient Integer serverPort;

    /** The server protocol. */
    private transient Protocol serverProtocol;

    /**
     * Create Environment.
     * 
     * @param serverHost
     *            The server host.
     * @param serverPort
     *            The server port.
     */
    private Environment(final String serverHost, final Integer serverPort,
            final Protocol serverProtocol) {
        setServerHost(serverHost);
        setServerPort(serverPort);
        setServerProtocol(serverProtocol);
    }

    /**
     * Obtain the serverHost
     *
     * @return The String.
     */
    public String getServerHost() {
        return serverHost;
    }

    /**
     * Obtain the serverPort
     *
     * @return The port.
     */
    public Integer getServerPort() {
        return serverPort;
    }

    /**
     * Obtain the serverProtocol
     *
     * @return The Protocol.
     */
    public Protocol getServerProtocol() {
        return serverProtocol;
    }

    /**
     * Determine whether or not the environment is reachable.
     * 
     * @return True if the environment is reachable; false otherwise.
     */
    public Boolean isReachable() {
        return NetworkUtil.isTargetReachable(serverHost, serverPort);
    }

    /**
     * Set serverHost.
     *
     * @param serverHost The String.
     */
    public void setServerHost(final String serverHost) {
        this.serverHost = serverHost;
    }

    /**
     * Set serverPort.
     *
     * @param serverPort The port.
     */
    public void setServerPort(final Integer serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * Set serverProtocol.
     *
     * @param serverProtocol The Protocol.
     */
    public void setServerProtocol(Protocol serverProtocol) {
        this.serverProtocol = serverProtocol;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(serverHost)
                .append("/").append(serverPort)
                .toString();
    }

    /** The protocol used by the environment. */
    public enum Protocol { XMPP, XMPPS }
}
