/*
 * Created On: Sep 21, 2006 3:42:20 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.util.Locale;
import java.util.ResourceBundle;

import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.model.events.SessionAdapter;

import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabListExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabPanelExtension;

/**
 * <b>Title:</b>thinkParity Browser Platform Plugin Services<br>
 * <b>Description:</b>The plugin services are made available to each plugin and
 * extension and provide access to the browser platform itself ie logging; model
 * generation etc.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PluginServices {

    /** The connection. */
    private Connection connection;

    /** The plugin's resource bundle. */
    private final PluginLoader loader;

    /** The plugin's <code>Log4JWrapper</code>. */
    private final Log4JWrapper logger;

    /** The plugin's model factory. */
    private final PluginModelFactory modelFactory;

    /** The plugin wrapper. */
    private final PluginWrapper wrapper;

    /**
     * Create PluginServices.
     * 
     */
    PluginServices(final Platform platform, final PluginWrapper wrapper) {
        super();
        this.loader = new PluginRegistry().getLoader(wrapper);
        this.logger = new Log4JWrapper(getClass());
        this.modelFactory = new PluginModelFactory(platform);
        this.modelFactory.getSessionModel().addListener(new SessionAdapter() {
            @Override
            public void sessionEstablished() {
                connection = Connection.ONLINE;
            }
            @Override
            public void sessionTerminated() {
                connection = Connection.OFFLINE;
            }
        });
        this.wrapper = wrapper;
    }

    /**
     * Obtain the resource bundle for the plugin.
     * 
     * @param baseName
     *            The resource bundle's base name.
     * @return A resource bundle.
     */
    public ResourceBundle getBundle(final String baseName) {
        return loader.loadBundle(baseName, Locale.getDefault());
    }

    /**
     * Obtain the connection.
     * 
     * @return A <code>Connection</code>.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Obtain the model factory.
     * 
     * @return A <code>PluginModelFactory</code>.
     */
    public PluginModelFactory getModelFactory() {
        return modelFactory;
    }

    /**
     * Translate an error into a plugin error.
     * 
     * @param cause
     *            The cause <code>Throwable</code> of the error.
     * @return A <code>PluginException</code>.
     */
    public PluginException translateError(final Throwable t) {
        if (PluginException.class.isAssignableFrom(t.getClass())) {
            return (PluginException) t;
        } else {
            final String errorId = new ErrorHelper().getErrorId(t);
            logger.logError(t, "{0}", errorId);
            return PluginException.translate(errorId, t);
        }
    }

    /**
     * Obtain an action extension for the plugin with the given name.
     * 
     * @param name
     *            An extension name.
     * @return A <code>ActionExtension</code>.
     */
    ActionExtension getActionExtension(final String name) {
        return (ActionExtension) wrapper.getExtension(name);
    }

    /**
     * Obtain a tab extension for the plugin with the given name.
     * 
     * @param name
     *            An extension name.
     * @return A <code>TabExtension</code>.
     */
    TabListExtension getTabListExtension(final String name) {
        return (TabListExtension) wrapper.getExtension(name);
    }

    /**
     * Obtain a tab extension for the plugin with the given name.
     * 
     * @param name
     *            An extension name.
     * @return A <code>TabExtension</code>.
     */
    TabPanelExtension getTabPanelExtension(final String name) {
        return (TabPanelExtension) wrapper.getExtension(name);
    }
}
