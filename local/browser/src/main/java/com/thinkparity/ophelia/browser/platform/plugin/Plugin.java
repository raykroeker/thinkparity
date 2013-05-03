/*
 * Created On: Sep 20, 2006 12:40:56 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

/**
 * <b>Title:</b>thinkParity Browser Platform Plugin<br>
 * <b>Description:</b>A plugin interface. Each plugin wanting to extend the
 * thinkParity platfrom must implement this interface.<br>
 * <br>
 * A plugin's lifecycle is as follows:
 * 
 * <pre>
 *  PLATFORM
 *   |-&gt;start
 *   |-&gt;PLUGIN
 *   |---&gt;initialize
 *   |---&gt;start
 *   |-&gt;APPLICATION
 *   |---&gt;start
 *   |---&gt;end
 *   |-&gt;PLUGIN
 *   |---&gt;end
 *   |-&gt;end
 * </pre>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Plugin {

    /**
     * End the plugin.
     *
     */
    public void end();

    /**
     * Internal initialization of the plugin.
     *
     */
    public void initialize(final PluginServices services);

    /**
     * Start the plugin.
     *
     */
    public void start();
}
