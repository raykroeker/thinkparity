/*
 * Created On: Nov 25, 2005
 */
package com.thinkparity.desdemona.wildfire;

import java.io.File;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

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
		initializeHandlers(pluginDirectory);
		startStream();
        startArchive();
        final String message = getStartupMessage();
        new Log4JWrapper(getClass()).logInfo(message);
        new Log4JWrapper("DESDEMONA_DEFAULT").logInfo(message);
        new Log4JWrapper("DESDEMONA_SQL_DEBUGGER").logInfo(message);
        new Log4JWrapper("DESDEMONA_XMPP_DEBUGGER").logInfo(message);
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
        final File loggingRoot = bootstrapLog4JRoot(mode);
        // console appender
        logging.setProperty("log4j.appender.DESDEMONA_CONSOLE", "org.apache.log4j.ConsoleAppender");
        logging.setProperty("log4j.appender.DESDEMONA_CONSOLE.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.DESDEMONA_CONSOLE.layout.ConversionPattern", "%d{ISO8601} %t %p %m%n");
        // default appender
        logging.setProperty("log4j.appender.DESDEMONA_DEFAULT", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.DESDEMONA_DEFAULT.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.DESDEMONA_DEFAULT.layout.ConversionPattern", "%d{ISO8601} %t %p %m%n");
        logging.setProperty("log4j.appender.DESDEMONA_DEFAULT.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity Server.log"));
        // sql appender
        logging.setProperty("log4j.appender.DESDEMONA_SQL_DEBUGGER", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.DESDEMONA_SQL_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.DESDEMONA_SQL_DEBUGGER.layout.ConversionPattern", "%d{ISO8601} %t %m%n");
        logging.setProperty("log4j.appender.DESDEMONA_SQL_DEBUGGER.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity Server SQL.log"));
        // xmpp appender
        logging.setProperty("log4j.appender.DESDEMONA_XMPP_DEBUGGER", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.DESDEMONA_XMPP_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.DESDEMONA_XMPP_DEBUGGER.layout.ConversionPattern", "%d{ISO8601} %t %m%n");
        logging.setProperty("log4j.appender.DESDEMONA_XMPP_DEBUGGER.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity Server XMPP.log"));
        // loggers
        switch (mode) {
        case DEMO:
        case PRODUCTION:
            logging.setProperty("log4j.rootLogger", "WARN, DESDEMONA_DEFAULT");
            logging.setProperty("log4j.logger.com.thinkparity.desdemona", "WARN, DESDEMONA_DEFAULT");
            logging.setProperty("log4j.additivity.com.thinkparity.desdemona", "false");
            logging.setProperty("log4j.logger.DESDEMONA_SQL_DEBUGGER", "NONE");
            logging.setProperty("log4j.additivity.DESDEMONA_SQL_DEBUGGER", "false");
            logging.setProperty("log4j.logger.DESDEMONA_XMPP_DEBUGGER", "NONE");
            logging.setProperty("log4j.additivity.DESDEMONA_XMPP_DEBUGGER", "false");
            break;
        case DEVELOPMENT:
            logging.setProperty("log4j.rootLogger", "INFO, DESDEMONA_CONSOLE, DESDEMONA_DEFAULT");
            logging.setProperty("log4j.logger.com.thinkparity.desdemona", "INFO, DESDEMONA_CONSOLE, DESDEMONA_DEFAULT");
            logging.setProperty("log4j.additivity.com.thinkparity.desdemona", "false");
            logging.setProperty("log4j.logger.DESDEMONA_SQL_DEBUGGER", "DEBUG, DESDEMONA_SQL_DEBUGGER");
            logging.setProperty("log4j.additivity.DESDEMONA_SQL_DEBUGGER", "false");
            logging.setProperty("log4j.logger.DESDEMONA_XMPP_DEBUGGER", "DEBUG, DESDEMONA_XMPP_DEBUGGER");
            logging.setProperty("log4j.additivity.DESDEMONA_XMPP_DEBUGGER", "false");
            break;
        case TESTING:
            logging.setProperty("log4j.rootLogger", "INFO, DESDEMONA_DEFAULT");
            logging.setProperty("log4j.logger.com.thinkparity.desdemona", "INFO, DESDEMONA_DEFAULT");
            logging.setProperty("log4j.additivity.com.thinkparity.desdemona", "false");
            logging.setProperty("log4j.logger.DESDEMONA_SQL_DEBUGGER", "DEBUG, DESDEMONA_SQL_DEBUGGER");
            logging.setProperty("log4j.additivity.DESDEMONA_SQL_DEBUGGER", "false");
            logging.setProperty("log4j.logger.DESDEMONA_XMPP_DEBUGGER", "DEBUG, DESDEMONA_XMPP_DEBUGGER");
            logging.setProperty("log4j.additivity.DESDEMONA_XMPP_DEBUGGER", "false");
            break;
        default:
            throw Assert.createUnreachable("Unknown operating mode.");
        }
        // renderers
        logging.setProperty("log4j.renderer.com.thinkparity.codebase.model.artifact.Artifact", "com.thinkparity.desdemona.util.logging.or.ArtifactRenderer");
        logging.setProperty("log4j.renderer.com.thinkparity.codebase.model.profile.Profile", "com.thinkparity.desdemona.util.logging.or.ProfileObjectRenderer");
        logging.setProperty("log4j.renderer.com.thinkparity.codebase.model.user.User", "com.thinkparity.desdemona.util.logging.or.UserRenderer");
        logging.setProperty("log4j.renderer.com.thinkparity.desdemona.model.artifact.ArtifactSubscription", "com.thinkparity.desdemona.util.logging.or.ArtifactSubscriptionRenderer");
        logging.setProperty("log4j.renderer.org.dom4j.Element", "com.thinkparity.desdemona.util.logging.or.ElementRenderer");
        logging.setProperty("log4j.renderer.org.jivesoftware.wildfire.IQHandlerInfo", "com.thinkparity.desdemona.util.logging.or.IQHandlerInfoRenderer");
        logging.setProperty("log4j.renderer.org.xmpp.packet.IQ", "com.thinkparity.codebase.xmpp.IQRenderer");
        logging.setProperty("log4j.renderer.org.xmpp.packet.JID", "com.thinkparity.desdemona.util.logging.or.JIDRenderer");

        LogManager.resetConfiguration();
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
     * @param mode
     *            A thinkParity operating <code>Mode</code>.
     * @return A log4j root directory.
     */
    private File bootstrapLog4JRoot(final Mode mode) {
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
        return MessageFormat.format("{0} - {1} - {2}",
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
        logger.logInfo("{0}", controller.getInfo().getNamespace());
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
