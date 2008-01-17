/*
 * Created On:  23-Dec-07 1:29:11 PM
 */
package com.thinkparity.ophelia.model.events;

import com.thinkparity.ophelia.model.workspace.configuration.ProxyConfiguration;

/**
 * <b>Title:</b>thinkParity Ophelia Model Configuration Event<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ConfigurationEvent  {

    /** The proxy configuration. */
    private final ProxyConfiguration proxyConfiguration;

    /**
     * Create ConfigurationEvent.
     *
     */
    public ConfigurationEvent() {
        this(null);
    }

    /**
     * Create ConfigurationEvent.
     * 
     * @param proxyConfiguration
     *            A <code>ProxyConfiguration</code>.
     */
    public ConfigurationEvent(final ProxyConfiguration proxyConfiguration) {
        super();
        this.proxyConfiguration = proxyConfiguration;
    }

    /**
     * Obtain the proxy configuration.
     * 
     * @return A <code>ProxyConfiguration</code>.
     */
    public ProxyConfiguration getProxyConfiguration() {
        return proxyConfiguration;
    }
}
