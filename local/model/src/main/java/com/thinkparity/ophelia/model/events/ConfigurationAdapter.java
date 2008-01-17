/*
 * Created On:  23-Dec-07 1:31:58 PM
 */
package com.thinkparity.ophelia.model.events;

/**
 * <b>Title:</b>thinkParity Ophelia Model Workspace Configuration Listener
 * Adapter<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ConfigurationAdapter implements ConfigurationListener {

    /**
     * Create ConfigurationAdapter.
     *
     */
    public ConfigurationAdapter() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.model.events.ConfigurationListener#configurationDeleted(com.thinkparity.ophelia.model.events.ConfigurationEvent)
     * 
     */
    @Override
    public void configurationDeleted(final ConfigurationEvent event) {
    }

    /**
     * @see com.thinkparity.ophelia.model.events.ConfigurationListener#configurationUpdated(com.thinkparity.ophelia.model.events.ConfigurationEvent)
     * 
     */
    @Override
    public void configurationUpdated(final ConfigurationEvent event) {
    }
}
