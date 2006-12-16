/*
 * Created On: Nov 25, 2005
 */
package com.thinkparity.desdemona.wildfire;

import java.io.File;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.thinkparity.codebase.Constants;
import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.config.ConfigFactory;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.desdemona.model.Version;
import com.thinkparity.desdemona.model.archive.ArchiveModel;
import com.thinkparity.desdemona.model.stream.StreamModel;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.XPP3Reader;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.JiveProperties;
import org.jivesoftware.wildfire.IQRouter;
import org.jivesoftware.wildfire.XMPPServer;
import org.jivesoftware.wildfire.XMPPServerListener;
import org.jivesoftware.wildfire.container.Plugin;
import org.jivesoftware.wildfire.container.PluginManager;
import org.jivesoftware.wildfire.handler.IQHandler;


/**
 * <b>Title:</b>thinkParity Jive Plugin<br>
 * <b>Description:</b>The thinkParity Jive Server Plugin<br>
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.30
 */
public class WildfirePlugin implements Plugin, XMPPServerListener {

	/** An apache logger. */
    protected Log4JWrapper logger;

    /** The plugin's handlers. */
    private final List<IQHandler> handlers;

    /** The wildfire router. */
	private final IQRouter router;

	/** Create WildfirePlugin. */
	public WildfirePlugin() {
		super();
        this.handlers = new LinkedList<IQHandler>();
		this.router = XMPPServer.getInstance().getIQRouter();
        XMPPServer.getInstance().addServerListener(this);
	}

    /**
     * @see org.jivesoftware.wildfire.container.Plugin#destroyPlugin()
     * 
     */
	public void destroyPlugin() {
        stopArchive();
        stopStream();
	    destroyHandlers();
		destroyLogging();
	}

    /**
     * @see org.jivesoftware.wildfire.container.Plugin#initializePlugin(org.jivesoftware.wildfire.container.PluginManager, java.io.File)
     * 
	 */
	public void initializePlugin(final PluginManager manager,
            final File pluginDirectory) {
	    final JiveProperties jiveProperties = JiveProperties.getInstance();
	    final Mode mode = Mode.valueOf((String) jiveProperties.get("thinkparity.mode"));
        System.setProperty("thinkparity.mode", mode.toString());
        bootstrapLog4J(mode);
        logger = new Log4JWrapper();
		logger.logInfo("{0}:{1}", "xmpp.auth.anonymous", jiveProperties.get("xmpp.auth.anonymous"));
		logger.logInfo("{0}:{1}", "xmpp.domain", jiveProperties.get("xmpp.domain"));
		logger.logInfo("{0}:{1}", "xmpp.server.socket.port", jiveProperties.get("xmpp.server.socket.port"));
		logger.logInfo("{0}:{1}", "xmpp.socket.plain.port", jiveProperties.get("xmpp.socket.plain.port"));
		logger.logInfo("{0}:{1}", "xmpp.socket.ssl.active", jiveProperties.get("xmpp.socket.ssl.active"));
		logger.logInfo("{0}:{1}", "xmpp.socket.ssl.keypass", jiveProperties.get("xmpp.socket.ssl.keypass"));
		logger.logInfo("{0}:{1}", "xmpp.socket.ssl.port", jiveProperties.get("xmpp.socket.ssl.port"));
        logger.logInfo("{0}:{1}", "thinkparity.environment", jiveProperties.get("thinkparity.environment"));
        logger.logInfo("{0}:{1}", "thinkparity.mode", jiveProperties.get("thinkparity.mode"));
		initializeHandlers(pluginDirectory);
		startStream();
        startArchive();
        final String message = getStartupMessage();
        new Log4JWrapper(getClass()).logInfo(message);
        System.out.println(message);
	}

    /**
     * @see org.jivesoftware.wildfire.XMPPServerListener#serverStarted()
     */
    public void serverStarted() {
        startArchive();
    }

	/**
     * @see org.jivesoftware.wildfire.XMPPServerListener#serverStopping()
     */
    public void serverStopping() {
        stopArchive();
    }

    /**
	 * Initialize logging.
	 * 
	 * @param pluginDirectory
	 *            The plugin directory.
	 */
	private void bootstrapLog4J(final Mode mode) {
        final Properties logging = bootstrapLog4JConfig(mode);
        final File loggingRoot = bootstrapLog4JRoot();
        // desdemona console
        logging.setProperty("log4j.appender.DESDEMONA_CONSOLE", "org.apache.log4j.ConsoleAppender");
        logging.setProperty("log4j.appender.DESDEMONA_CONSOLE.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.DESDEMONA_CONSOLE.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
        // ophelia console
        logging.setProperty("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
        logging.setProperty("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.CONSOLE.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
        // desdemona default file appender
        logging.setProperty("log4j.appender.DESDEMONA_DEFAULT", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.DESDEMONA_DEFAULT.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
        logging.setProperty("log4j.appender.DESDEMONA_DEFAULT.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.DESDEMONA_DEFAULT.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
        logging.setProperty("log4j.appender.DESDEMONA_DEFAULT.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity Server.log"));
        // ophelia default file appender
        logging.setProperty("log4j.appender.DEFAULT", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.DEFAULT.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
        logging.setProperty("log4j.appender.DEFAULT.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.DEFAULT.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
        logging.setProperty("log4j.appender.DEFAULT.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity.log"));
        // desdemona sql appender
        logging.setProperty("log4j.appender.DESDEMONA_SQL_DEBUGGER", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.DESDEMONA_SQL_DEBUGGER.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
        logging.setProperty("log4j.appender.DESDEMONA_SQL_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.DESDEMONA_SQL_DEBUGGER.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
        logging.setProperty("log4j.appender.DESDEMONA_SQL_DEBUGGER.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity Server SQL.log"));
        // ophelia sql appender
        logging.setProperty("log4j.appender.SQL_DEBUGGER", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.SQL_DEBUGGER.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
        logging.setProperty("log4j.appender.SQL_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.SQL_DEBUGGER.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
        logging.setProperty("log4j.appender.SQL_DEBUGGER.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity SQL.log"));
        // desdemona xmpp appender
        logging.setProperty("log4j.appender.DESDEMONA_XMPP_DEBUGGER", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.DESDEMONA_XMPP_DEBUGGER.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
        logging.setProperty("log4j.appender.DESDEMONA_XMPP_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.DESDEMONA_XMPP_DEBUGGER.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
        logging.setProperty("log4j.appender.DESDEMONA_XMPP_DEBUGGER.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity Server XMPP.log"));
        // ophelia xmpp appender
        logging.setProperty("log4j.appender.XMPP_DEBUGGER", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.XMPP_DEBUGGER.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
        logging.setProperty("log4j.appender.XMPP_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.XMPP_DEBUGGER.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
        logging.setProperty("log4j.appender.XMPP_DEBUGGER.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity XMPP.log"));
        // loggers
        switch (mode) {
        case DEMO:
        case PRODUCTION:
            // root
            logging.setProperty("log4j.rootLogger", "WARN, DESDEMONA_DEFAULT");

            // desdemona
            logging.setProperty("log4j.logger.com.thinkparity.desdemona", "WARN, DESDEMONA_DEFAULT");
            logging.setProperty("log4j.additivity.com.thinkparity.desdemona", "false");
            // desdemona sql
            logging.setProperty("log4j.logger.DESDEMONA_SQL_DEBUGGER", "NONE");
            logging.setProperty("log4j.additivity.DESDEMONA_SQL_DEBUGGER", "false");
            // desdemona xmpp
            logging.setProperty("log4j.logger.DESDEMONA_XMPP_DEBUGGER", "NONE");
            logging.setProperty("log4j.additivity.DESDEMONA_XMPP_DEBUGGER", "false");

            // ophelia
            logging.setProperty("log4j.logger.com.thinkparity.ophelia", "WARN, DEFAULT");
            logging.setProperty("log4j.additivity.com.thinkparity.ophelia", "false");
            // ophelia sql
            logging.setProperty("log4j.logger.SQL_DEBUGGER", "NONE");
            logging.setProperty("log4j.additivity.SQL_DEBUGGER", "false");
            // ophelia xmpp
            logging.setProperty("log4j.logger.XMPP_DEBUGGER", "NONE");
            logging.setProperty("log4j.additivity.XMPP_DEBUGGER", "false");
            break;
        case DEVELOPMENT:
            // root
            logging.setProperty("log4j.rootLogger", "INFO, DESDEMONA_CONSOLE, DESDEMONA_DEFAULT");

            // desdemona
            logging.setProperty("log4j.logger.com.thinkparity.desdemona", "INFO, DESDEMONA_CONSOLE, DESDEMONA_DEFAULT");
            logging.setProperty("log4j.additivity.com.thinkparity.desdemona", "false");
            // desdmona sql
            logging.setProperty("log4j.logger.DESDEMONA_SQL_DEBUGGER", "DEBUG, DESDEMONA_SQL_DEBUGGER");
            logging.setProperty("log4j.additivity.DESDEMONA_SQL_DEBUGGER", "false");
            // desdemona xmpp
            logging.setProperty("log4j.logger.DESDEMONA_XMPP_DEBUGGER", "DEBUG, DESDEMONA_XMPP_DEBUGGER");
            logging.setProperty("log4j.additivity.DESDEMONA_XMPP_DEBUGGER", "false");

            // ophelia
            logging.setProperty("log4j.logger.com.thinkparity.ophelia", "INFO, CONSOLE, DEFAULT");
            logging.setProperty("log4j.additivity.com.thinkparity.ophelia", "false");
            // ophelia sql
            logging.setProperty("log4j.logger.SQL_DEBUGGER", "DEBUG, SQL_DEBUGGER");
            logging.setProperty("log4j.additivity.SQL_DEBUGGER", "false");
            // ophelia xmpp
            logging.setProperty("log4j.logger.XMPP_DEBUGGER", "DEBUG, XMPP_DEBUGGER");
            logging.setProperty("log4j.additivity.XMPP_DEBUGGER", "false");
            break;
        case TESTING:
            // root
            logging.setProperty("log4j.rootLogger", "INFO, DESDEMONA_DEFAULT");

            // desdemona
            logging.setProperty("log4j.logger.com.thinkparity.desdemona", "INFO, DESDEMONA_DEFAULT");
            logging.setProperty("log4j.additivity.com.thinkparity.desdemona", "false");
            // desdemona sql
            logging.setProperty("log4j.logger.DESDEMONA_SQL_DEBUGGER", "DEBUG, DESDEMONA_SQL_DEBUGGER");
            logging.setProperty("log4j.additivity.DESDEMONA_SQL_DEBUGGER", "false");
            // desdmona xmpp
            logging.setProperty("log4j.logger.DESDEMONA_XMPP_DEBUGGER", "DEBUG, DESDEMONA_XMPP_DEBUGGER");
            logging.setProperty("log4j.additivity.DESDEMONA_XMPP_DEBUGGER", "false");

            // ophelia
            logging.setProperty("log4j.logger.com.thinkparity.ophelia", "INFO, DEFAULT");
            logging.setProperty("log4j.additivity.com.thinkparity.ophelia", "false");
            // ophelia sql
            logging.setProperty("log4j.logger.SQL_DEBUGGER", "DEBUG, SQL_DEBUGGER");
            logging.setProperty("log4j.additivity.SQL_DEBUGGER", "false");
            // ophelia xmpp
            logging.setProperty("log4j.logger.XMPP_DEBUGGER", "DEBUG, XMPP_DEBUGGER");
            logging.setProperty("log4j.additivity.XMPP_DEBUGGER", "false");
            break;
        default:
            throw Assert.createUnreachable("Unknown operating mode.");
        }
        // common renderers
        logging.setProperty(
                "log4j.renderer.java.util.Calendar",
                "com.thinkparity.codebase.log4j.or.CalendarRenderer");
        logging.setProperty(
                "log4j.renderer.com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent",
                "com.thinkparity.codebase.model.util.logging.or.XMPPEventRenderer");
        logging.setProperty(
                "log4j.renderer.com.thinkparity.codebase.model.container.Container",
                "com.thinkparity.codebase.model.util.logging.or.ContainerRenderer");
        logging.setProperty(
                "log4j.renderer.com.thinkparity.codebase.jabber.JabberId",
                "com.thinkparity.codebase.log4j.or.JabberIdRenderer");
        logging.setProperty(
                "log4j.renderer.com.thinkparity.codebase.model.document.Document",
                "com.thinkparity.codebase.model.util.logging.or.DocumentRenderer");
        logging.setProperty(
                "log4j.renderer.com.thinkparity.codebase.model.document.DocumentVersion",
                "com.thinkparity.codebase.model.util.logging.or.DocumentVersionRenderer");
        logging.setProperty(
                "log4j.renderer.com.thinkparity.codebase.model.user.User",
                "com.thinkparity.codebase.model.util.logging.or.UserRenderer");
        // desdemona renderers
        logging.setProperty(
                "log4j.renderer.com.thinkparity.codebase.model.artifact.Artifact",
                "com.thinkparity.desdemona.util.logging.or.ArtifactRenderer");
        logging.setProperty(
                "log4j.renderer.com.thinkparity.codebase.model.profile.Profile",
                "com.thinkparity.desdemona.util.logging.or.ProfileObjectRenderer");
        logging.setProperty(
                "log4j.renderer.com.thinkparity.codebase.model.user.User",
                "com.thinkparity.desdemona.util.logging.or.UserRenderer");
        logging.setProperty(
                "log4j.renderer.com.thinkparity.desdemona.model.artifact.ArtifactSubscription",
                "com.thinkparity.desdemona.util.logging.or.ArtifactSubscriptionRenderer");
        logging.setProperty(
                "log4j.renderer.org.dom4j.Element",
                "com.thinkparity.desdemona.util.logging.or.ElementRenderer");
        logging.setProperty(
                "log4j.renderer.org.jivesoftware.wildfire.IQHandlerInfo",
                "com.thinkparity.desdemona.util.logging.or.IQHandlerInfoRenderer");
        logging.setProperty(
                "log4j.renderer.org.xmpp.packet.IQ",
                "com.thinkparity.codebase.xmpp.IQRenderer");
        logging.setProperty(
                "log4j.renderer.org.xmpp.packet.JID",
                "com.thinkparity.desdemona.util.logging.or.JIDRenderer");
        // ophelia renderers
        logging.setProperty(
                "log4j.renderer.org.jivesoftware.smack.packet.Packet",
                "com.thinkparity.ophelia.model.util.logging.or.PacketRenderer");

        System.setProperty("log4j.defaultInitOverride", "true");
        PropertyConfigurator.configure(logging);
    }

	/**
     * Bootstrap the log4j configuration based upon the operating mode.
     * 
     * @param mode
     *            A thinkParity <code>Mode</code>.
     * @return A log4j configuration <code>Properties</code>.
     */
    private Properties bootstrapLog4JConfig(final Mode mode) {
        switch (mode) {
        case DEMO:
        case PRODUCTION:
        case TESTING:
            return new Properties();
        case DEVELOPMENT:
            return ConfigFactory.newInstance("log4j.properties");
        default:
            throw Assert.createUnreachable("Unknown operating mode.");
        }
    }

	/**
     * Bootstrap the log4j root directory based upon the operating mode.
     * 
     * @return A log4j root directory.
     */
    private File bootstrapLog4JRoot() {
        final File loggingRoot = new File(JiveGlobals.getHomeDirectory(), "logs");
        System.setProperty("thinkparity.logging.root", loggingRoot.getAbsolutePath());
        return loggingRoot;
    }

    /**
	 * Destroy the iq dispatcher.
	 * 
	 */
	private void destroyHandlers() {
	    synchronized (handlers) {
            for (IQHandler handler : handlers) { 
                router.removeHandler(handler);
                handler = null;
            }
        }
	}

    /**
	 * Destroy the logging framework.
	 *
	 */
	private void destroyLogging() { LogManager.shutdown(); }

	private String getStartupMessage() {
        return MessageFormat.format("{0} - {1} - {2} is online.",
                Version.getName(), Version.getMode(), Version.getBuildId());
    }

    /**
     * Intialize the controller and add it to the route table.
     * 
     * @param handlerName
     *            The handler name.

     * @param controllerName
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
	 */
    private void initializeHandler(final String handlerName)
            throws ClassNotFoundException, IllegalAccessException,
            InstantiationException {
        handlers.add((IQHandler) Class.forName(handlerName).newInstance());
        final IQHandler controller = handlers.get(handlers.size() - 1);
        router.addHandler(controller);
        logger.logInfo("{0} has been initialized.",
                controller.getInfo().getNamespace());
    }

    /**
	 * Initialize the iq dispatcher.
	 * 
	 */
    private void initializeHandlers(final File pluginDirectory) {
        try {
            final Document document = new XPP3Reader().read(new File(pluginDirectory, "wildfire-handlers.xml"));
            final Element rootElement = document.getRootElement();
            final List elements = rootElement.elements("handler");
            for (final Object element : elements) {
                initializeHandler(((Element) element).getText());
            }
        } catch(final Throwable t) {
            logger.logFatal("Handler could not be initialized.", t);
        }
    }
        
    /**
     * Start the archive service.
     *
     */
    private void startArchive() {
        ArchiveModel.getModel().start();
    }

    /**
     * Start the stream service.
     *
     */
    private void startStream() {
        StreamModel.getModel().start();
    }

    /**
     * Stop the archive service.
     *
     */
    private void stopArchive() {
        ArchiveModel.getModel().stop();
    }

    /**
     * Stop the stream service.
     *
     */
    private void stopStream() {
        StreamModel.getModel().stop();
    }
}
