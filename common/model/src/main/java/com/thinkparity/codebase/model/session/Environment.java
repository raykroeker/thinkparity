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

    /** The demo environment. */
    DEMO("thinkparity.dyndns.org", 5230, XMPPProtocol.XMPP,
            "thinkparity.dyndns.org", 6004, StreamProtocol.TCP),

    /** A localhost demo environment. */
    DEMO_LOCALHOST("localhost", 5230, XMPPProtocol.XMPP,
            "thinkparity.dyndns.org", 6004, StreamProtocol.TCP),

    /** A localhost development environment. */
    DEVELOPMENT_LOCALHOST("localhost", 5226, XMPPProtocol.XMPP,
            "thinkparity.dyndns.org", 6002, StreamProtocol.TCP),

    /** Raymond's development environment. */
    DEVELOPMENT_RAYMOND("thinkparity.dyndns.org", 5226, XMPPProtocol.XMPP,
            "thinkparity.dyndns.org", 6002, StreamProtocol.TCP),

    /** Robert's development environment. */
    DEVELOPMENT_ROBERT("thinkparity.dyndns.org", 5228, XMPPProtocol.XMPP,
            "thinkparity.dyndns.org", 6003, StreamProtocol.TCP),

    /** Production environment. */
    PRODUCTION("thinkparity.dyndns.org", 5222, XMPPProtocol.XMPP,
            "thinkparity.dyndns.org", 6000, StreamProtocol.TCP),

    /** Testing environment. */
    TESTING("thinkparity.dyndns.org", 5224, XMPPProtocol.XMPP,
            "thinkparity.dyndns.org", 6001, StreamProtocol.TCP),

    /** A localhost testing environment. */
    TESTING_LOCALHOST("localhost", 5224, XMPPProtocol.XMPP,
            "thinkparity.dyndns.org", 6001, StreamProtocol.TCP);

    /** The stream server host. */
    private transient String streamHost;

    /** The stream server port. */
    private transient Integer streamPort;

    /** The stream server protocol. */
    private transient StreamProtocol streamProtocol;

    /** The server host. */
    private transient String xmppHost;

    /** The server port. */
    private transient Integer xmppPort;

    /** The server protocol. */
    private transient XMPPProtocol xmppProtocol;

    /**
     * Create Environment.
     * 
     * @param xmppHost
     *            The xmpp server host.
     * @param xmppPort
     *            The xmpp server port.
     * @param xmppProtocol
     *            The xmpp server protocol.
     * @param streamHost
     *            The stream server host.
     * @param streamPort
     *            The stream server port
     * @param streamProtocol
     *            The stream server protocol.
     */
    private Environment(final String xmppHost, final Integer xmppPort,
            final XMPPProtocol xmppProtocol, final String streamHost,
            final Integer streamPort, final StreamProtocol streamProtocol) {
        setXMPPHost(xmppHost);
        setXMPPPort(xmppPort);
        setXMPPProtocol(xmppProtocol);
        setStreamHost(streamHost);
        setStreamPort(streamPort);
        setStreamProtocol(streamProtocol);
    }

    /**
     * Obtain the streamHost
     * 
     * @return The stream host <code>String</code>.
     */
    public String getStreamHost() {
        return streamHost;
    }

    /**
     * Obtain the streamPort
     *
     * @return The Integer.
     */
    public Integer getStreamPort() {
        return streamPort;
    }

    /**
     * Obtain the streamProtocol
     *
     * @return The StreamProtocol.
     */
    public StreamProtocol getStreamProtocol() {
        return streamProtocol;
    }

    /**
     * Obtain the xmppHost
     *
     * @return The String.
     */
    public String getXMPPHost() {
        return xmppHost;
    }

    /**
     * Obtain the xmppPort
     *
     * @return The port.
     */
    public Integer getXMPPPort() {
        return xmppPort;
    }

    /**
     * Obtain the xmppProtocol
     *
     * @return The XMPPProtocol.
     */
    public XMPPProtocol getXMPPProtocol() {
        return xmppProtocol;
    }

    /**
     * Determine whether or not the environment is reachable.
     * 
     * @return True if the environment is reachable; false otherwise.
     */
    public Boolean isReachable() {
        return NetworkUtil.isTargetReachable(xmppHost, xmppPort);
    }

    /**
     * Set streamHost.
     * 
     * @param streamHost
     *            The stream host <code>String</code>.
     */
    public void setStreamHost(final String streamHost) {
        this.streamHost = streamHost;
    }

    /**
     * Set streamPort.
     * 
     * @param streamPort
     *            The stream port <code>Integer</code>.
     */
    public void setStreamPort(final Integer streamPort) {
        this.streamPort = streamPort;
    }

    /**
     * Set streamProtocol.
     *
     * @param streamProtocol The StreamProtocol.
     */
    public void setStreamProtocol(final StreamProtocol streamProtocol) {
        this.streamProtocol = streamProtocol;
    }

    /**
     * Set xmppHost.
     *
     * @param xmppHost The String.
     */
    public void setXMPPHost(final String serverHost) {
        this.xmppHost = serverHost;
    }

    /**
     * Set xmppPort.
     *
     * @param xmppPort The port.
     */
    public void setXMPPPort(final Integer serverPort) {
        this.xmppPort = serverPort;
    }

    /**
     * Set xmppProtocol.
     *
     * @param xmppProtocol The XMPPProtocol.
     */
    public void setXMPPProtocol(final XMPPProtocol serverProtocol) {
        this.xmppProtocol = serverProtocol;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(xmppHost)
                .append("/").append(xmppPort)
                .append("/").append(streamHost)
                .append("/").append(streamPort)
                .toString();
    }

    /** The protocol used by the stream server. */
    public enum StreamProtocol { TCP }

    /** The protocol used by the xmpp server. */
    public enum XMPPProtocol { XMPP, XMPPS }
}
