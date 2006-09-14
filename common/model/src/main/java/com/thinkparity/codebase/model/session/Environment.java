/*
 * Created On: Aug 5, 2006 10:16:01 AM
 */
package com.thinkparity.codebase.model.session;

import com.thinkparity.codebase.NetworkUtil;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.0
 */
public class Environment {

    /** Raymond's development environment. */
    public static final Environment DEVELOPMENT_RAYMOND;

    /** Robert's development environment. */
    public static final Environment DEVELOPMENT_ROBERT;

    /** Production environment. */
    public static final Environment PRODUCTION;

    /** Testing environment. */
    public static final Environment TESTING;

    static {
        DEVELOPMENT_RAYMOND = new Environment("thinkparity.dyndns.org", 5226, Protocol.XMPP);
        DEVELOPMENT_ROBERT = new Environment("thinkparity.dyndns.org", 5228, Protocol.XMPP);
        PRODUCTION = new Environment("thinkparity.dyndns.org", 5222, Protocol.XMPP);
        TESTING = new Environment("thinkparity.dyndns.org", 5224, Protocol.XMPP);
    }

    /** The server host. */
    private transient String serverHost;

    /** The server port. */
    private transient Integer serverPort;

    /** The server protocol. */
    private transient Protocol serverProtocol;

    /** Create Environment. */
    public Environment() { super(); }

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
        super();
        setServerHost(serverHost);
        setServerPort(serverPort);
        setServerProtocol(serverProtocol);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if(null != obj && obj instanceof Environment) {
            return ((Environment) obj).serverHost.equals(serverHost) &&
                    ((Environment) obj).serverPort.equals(serverPort);
        }
        return false;
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
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new StringBuffer(getClass().getName())
                .append(serverHost).append(serverPort)
                .toString().hashCode();
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
