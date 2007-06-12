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
    DEMO("thinkparity.dyndns.org", 446),

    /** A localhost demo environment. */
    DEMO_LOCALHOST("localhost", 446),

    /** The development environment. */
    DEVELOPMENT("thinkparity.dyndns.org", 8445),

    /** A localhost development environment. */
    DEVELOPMENT_LOCALHOST("localhost", 8445),

    /** Production environment. */
    PRODUCTION("thinkparity.net", 443),

    /** Testing environment. */
    TESTING("thinkparity.dyndns.org", 444),

    /** A localhost testing environment. */
    TESTING_LOCALHOST("localhost", 444);

    /** The web-services host. */
    private final transient String serviceHost;

    /** The web-services port. */
    private final transient Integer servicePort;

    /**
     * Create Environment.
     * 
     * @param serviceHost
     *            The web-services server host <code>String</code>.
     * @param servicePort
     *            The web-services server port <code>Integer</code>.
     */
    private Environment(final String serviceHost, final Integer servicePort) {
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
    }

    /**
     * Obtain the web-services host.
     * 
     * @return A host-name <code>String</code>.
     */
    public String getServiceHost() {
        return serviceHost;
    }

    /**
     * Obtain the web-services port.
     * 
     * @return A port <code>Integer</code>.
     */
    public Integer getServicePort() {
        return servicePort;
    }

    /**
     * Determine whether or not all services within the environment are
     * reachable.
     * 
     * @return True if the environment is reachable; false otherwise.
     */
    public Boolean isReachable() {
        return isServiceReachable();
    }

    /**
     * Determine whether or not the xmpp service within the environment is
     * reachable.
     * 
     * @return True if it is reachable.
     */
    public Boolean isServiceReachable() {
        return NetworkUtil.isTargetReachable(serviceHost, servicePort);
    }

    /**
     * @see java.lang.Object#toString()
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder(64)
                .append(getClass().getName()).append("//")
                .append(serviceHost)
                .append("/").append(servicePort)
                .toString();
    }
}
