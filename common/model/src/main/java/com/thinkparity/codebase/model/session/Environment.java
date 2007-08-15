/*
 * Created On: Aug 5, 2006 10:16:01 AM
 */
package com.thinkparity.codebase.model.session;


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
    DEVELOPMENT("thinkparity.dyndns.org", 10001),

    /** A localhost development environment. */
    DEVELOPMENT_LOCALHOST("localhost", 10001),

    /** Testing environment. */
    DEVELOPMENT_TESTING("thinkparity.dyndns.org", 10002),

    /** A localhost testing environment. */
    DEVELOPMENT_TESTING_LOCALHOST("localhost", 10002),

    /** Production environment. */
    PRODUCTION("thinkparity.net", 443);

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
