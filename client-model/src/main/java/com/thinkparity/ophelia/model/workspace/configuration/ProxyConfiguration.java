/*
 * Created On:  22-Dec-07 5:35:07 PM
 */
package com.thinkparity.ophelia.model.workspace.configuration;


/**
 * <b>Title:</b>thinkParity Ophelia Workspace Proxy Configuration<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ProxyConfiguration {

    /** The http proxy. */
    private Proxy http;

    /** The http proxy credentials. */
    private ProxyCredentials httpCredentials;

    /** The socks proxy. */
    private Proxy socks;

    /**
     * Create ProxyConfiguration.
     *
     */
    public ProxyConfiguration() {
        super();
    }

    /**
     * Obtain the http.
     *
     * @return A <code>Proxy</code>.
     */
    public Proxy getHttp() {
        return http;
    }

    /**
     * Obtain the httpCredentials.
     *
     * @return A <code>ProxyCredentials</code>.
     */
    public ProxyCredentials getHttpCredentials() {
        return httpCredentials;
    }

    /**
     * Obtain the socks.
     *
     * @return A <code>Proxy</code>.
     */
    public Proxy getSocks() {
        return socks;
    }

    /**
     * Determine if the http credentials are set.
     * 
     * @return A <code>Boolean</code>.
     */
    public Boolean isSetHttpCredentials() {
        return null != httpCredentials;
    }

    /**
     * Set the http.
     *
     * @param http
     *		A <code>Proxy</code>.
     */
    public void setHttp(final Proxy http) {
        this.http = http;
    }

    /**
     * Set the httpCredentials.
     *
     * @param httpCredentials
     *		A <code>ProxyCredentials</code>.
     */
    public void setHttpCredentials(final ProxyCredentials httpCredentials) {
        this.httpCredentials = httpCredentials;
    }

    /**
     * Set the socks.
     *
     * @param socks
     *		A <code>Proxy</code>.
     */
    public void setSocks(final Proxy socks) {
        this.socks = socks;
    }
}
