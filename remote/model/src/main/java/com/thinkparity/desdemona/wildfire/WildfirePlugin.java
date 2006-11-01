/*
 * Created On: Nov 25, 2005
 */
package com.thinkparity.desdemona.wildfire;

import java.io.File;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.desdemona.model.Version;
import com.thinkparity.desdemona.model.archive.ArchiveModel;
import com.thinkparity.desdemona.model.stream.StreamModel;

import org.apache.log4j.LogManager;
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
		initializeLogging();
		final JiveProperties jiveProperties = JiveProperties.getInstance();
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
        final String message = MessageFormat.format("{0} - {1} - {2}",
                Version.getName(), Version.getMode(), Version.getBuildId());
		logger.logInfo(message);
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
	 * Initialize logging.
	 * 
	 * @param pluginDirectory
	 *            The plugin directory.
	 */
	private void initializeLogging() {
        final File logDirectory = new File(JiveGlobals.getHomeDirectory(), "logs");
        System.setProperty("thinkparity.log4j.file",
                new File(logDirectory, "desdemona.log").getAbsolutePath());
        logger = new Log4JWrapper();
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
