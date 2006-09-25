/*
 * Created On: Sep 21, 2006 8:13:12 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.util.ResourceBundle;

/**
 * <b>Title:</b>A thinkParity Browser Platfrom Plugin Extension<br>
 * <b>Description:</b>A plugin extension is a predefined extension point into
 * the browser platform.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface PluginExtension {

    /**
     * Obtain the extensions localization.
     * 
     * @return A <code>ResourceBundle</code>.
     */
    public ResourceBundle getLocalization();

    /**
     * Obtain the extension's localization context.
     * 
     * @return A localization context <code>String</code>.
     */
    public String getLocalizationContext();

    /**
     * Internal initialization of the extension.
     *
     */
    public void initialize(final PluginServices services);
}
