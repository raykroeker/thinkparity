/*
 * Created On:  21-Dec-07 11:37:58 AM
 */
package com.thinkparity.ophelia.model.workspace.configuration;

import com.thinkparity.ophelia.model.events.ConfigurationListener;

/**
 * <b>Title:</b>thinkParity Ophelia Model Workspace Configuration<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Configuration {

    /**
     * Add an event listener.
     * 
     * @param listener
     *            A <code>ConfigurationListener</code>.
     */
    void addListener(ConfigurationListener listener);

    /**
     * Delete proxy configuration.
     * 
     */
    void deleteProxyConfiguration();

    /**
     * Determine if the proxy configuration is set.
     * 
     * @return A <code>Boolean</code>.
     */
    Boolean isSetProxyConfiguration();

    /**
     * Read the proxy configuration.
     * 
     * @return A <code>ProxyConfiguration</code>.
     */
    ProxyConfiguration readProxyConfiguration();

    /**
     * Remove an event listener.
     * 
     * @param listener
     *            A <code>ConfigurationListener</code>.
     */
    void removeListener(ConfigurationListener listener);
    
    /**
     * Update the proxy configuration.
     * 
     * @param configuration
     *            A <code>ProxyConfiguration</code>.
     */
    void updateProxyConfiguration(ProxyConfiguration configuration);
}
