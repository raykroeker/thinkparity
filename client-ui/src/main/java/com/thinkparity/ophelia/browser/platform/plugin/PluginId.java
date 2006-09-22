/*
 * Created On: Sep 21, 2006 10:55:16 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public enum PluginId {

    ARCHIVE, CORE;

    /**
     * Obtain the plugin id.
     * 
     * @return The plugin id.
     */
    String toId() {
        return new StringBuffer("com.thinkparity.ophelia.browser.plugin.")
                .append(name().toLowerCase())
                .toString();
    }
}
