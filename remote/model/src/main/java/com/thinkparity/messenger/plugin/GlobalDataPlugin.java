/*
 * Nov 25, 2005
 */
package com.thinkparity.messenger.plugin;

import java.io.File;

import org.jivesoftware.messenger.IQRouter;
import org.jivesoftware.messenger.XMPPServer;
import org.jivesoftware.messenger.container.Plugin;
import org.jivesoftware.messenger.container.PluginManager;
import org.jivesoftware.util.Log;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class GlobalDataPlugin implements Plugin {

	/**
	 * Singleton instance of the global data handler.
	 */
	private static final IQGlobalDataHandler singleton;

	static {
		singleton = new IQGlobalDataHandler();
	}

	/**
	 * Handle to the xmpp server's iq router.
	 */
	private final IQRouter iqRouter;

	/**
	 * Create a GlobalDataPlugin.
	 */
	public GlobalDataPlugin() {
		super();
		Log.info(GlobalDataPluginConstants.GD_PLUGIN_NAME);
		this.iqRouter = XMPPServer.getInstance().getIQRouter();
	}

	/**
	 * @see org.jivesoftware.messenger.container.Plugin#destroyPlugin()
	 */
	public void destroyPlugin() {
		Log.info("destroyPlugin()");
		iqRouter.removeHandler(singleton);
	}

	/**
	 * @see org.jivesoftware.messenger.container.Plugin#initializePlugin(org.jivesoftware.messenger.container.PluginManager, java.io.File)
	 */
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		Log.info("initializePlugin(PluginManager,File)");
		iqRouter.addHandler(singleton);
	}
}
