/*
 * Created On:  23-Dec-07 1:27:44 PM
 */
package com.thinkparity.ophelia.model.events;

import com.thinkparity.codebase.event.EventListener;

/**
 * <b>Title:</b>thinkParity Ophelia Model Workspace Configuration Listener<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ConfigurationListener extends EventListener {

    /**
     * Fired when the workspace configuration is deleted.
     * 
     * @param event
     *            A <code>ConfigurationEvent</code>.
     */
    void configurationDeleted(ConfigurationEvent event);

    /**
     * Fired when the workspace configuration is updated.
     * 
     * @param event
     *            A <code>ConfigurationEvent</code>.
     */
    void configurationUpdated(ConfigurationEvent event);
}
