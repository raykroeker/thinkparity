/*
 * Created On: Sep 21, 2006 3:42:20 PM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.log4j.Log4JHelper;

import com.thinkparity.ophelia.model.events.SessionAdapter;

import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabListExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabPanelExtension;

import org.apache.log4j.Logger;

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

    /** The plugin's logger. */
    private final Logger logger;

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
        this.logger = Logger.getLogger(getClass());
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
            @Override
            public void sessionTerminated(final Throwable cause) {
                sessionTerminated();
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
     * Log a trace statement.
     * 
     * @param tracePattern
     *            A trace pattern.
     * @param traceArguments
     *            Trace pattern arguments.
     */
    public void logTrace(final String tracePattern,
            final Object... traceArguments) {
        if (logger.isTraceEnabled()) {
            logger.warn(Log4JHelper.renderAndFormat(logger, tracePattern,
                    traceArguments));
        }
    }

    /**
     * Log a variable.
     * 
     * @param <V>
     *            A variable type.
     * @param name
     *            A variable name.
     * @param value
     *            A variable.
     * @return The variable.
     */
    public <V> V logVariable(final String name, final V value) {
        if (logger.isDebugEnabled()) {
            logger.warn(Log4JHelper.renderAndFormat(logger, "{0}:{1}", name,
                    value));
        }
        return value;
    }

    /**
     * Log a warning.
     * 
     * @param warnPattern
     *            A warning text pattern.
     * @param warnArguments
     *            Warning text pattern arguments.
     */
    public void logWarning(final String warnPattern,
            final Object... warnArguments) {
        logger.warn(Log4JHelper.renderAndFormat(logger, warnPattern,
                warnArguments));
    }

    /**
     * Log a warning.
     * 
     * @param t
     *            A throwable.
     * @param warnPattern
     *            A warning text pattern.
     * @param warnArguments
     *            Warning text pattern arguments.
     */
    public void logWarning(final Throwable t, final String warnPattern,
            final Object... warnArguments) {
        logger.warn(Log4JHelper.renderAndFormat(logger, warnPattern,
                warnArguments), t);
    }

    /**
     * Translate an error into a plugin error.
     * 
     * @param cause
     *            The cause <code>Throwable</code> of the error.
     * @return A <code>PluginException</code>.
     */
    public PluginException translateError(final Throwable cause) {
        if (PluginException.class.isAssignableFrom(cause.getClass())) {
            return (PluginException) cause;
        } else {
            final Object errorId = getErrorId(cause);
            logger.error(errorId, cause);
            return PluginException.translate(errorId.toString(), cause);
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

    /**
     * Obtain an error id.
     * 
     * @param cause
     *            The cause of the error.
     * @return The error id.
     */
    private Object getErrorId(final Throwable cause) {
        return MessageFormat.format("{0} - {2} - {3}.{4}({5}:{6})",
                cause.getLocalizedMessage(),
                StackUtil.getFrameClassName(2),
                StackUtil.getFrameMethodName(2),
                StackUtil.getFrameFileName(2),
                StackUtil.getFrameLineNumber(2));
    }
}
