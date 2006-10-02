/*
 * Created On: Nov 25, 2005
 */
package com.thinkparity.desdemona.wildfire;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.wildfire.IQRouter;
import org.jivesoftware.wildfire.XMPPServer;
import org.jivesoftware.wildfire.XMPPServerListener;
import org.jivesoftware.wildfire.container.Plugin;
import org.jivesoftware.wildfire.container.PluginManager;
import org.jivesoftware.wildfire.handler.IQHandler;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.XPP3Reader;
import org.xmlpull.v1.XmlPullParserException;

import com.thinkparity.desdemona.model.Version;
import com.thinkparity.desdemona.model.archive.ArchiveModel;


/**
 * <b>Title:</b>thinkParity Jive Plugin<br>
 * <b>Description:</b>The thinkParity Jive Server Plugin<br>
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.30
 */
public class WildfirePlugin implements Plugin, XMPPServerListener {

	/** An apache logger. */
    protected Logger logger;

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
//        stopArchive();
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
		initializeHandlers(pluginDirectory);
//        startArchive();
        final String message = MessageFormat.format("{0} - {1} - {2}",
                Version.getName(), Version.getMode(), Version.getBuildId());
		logger.info(message);
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
        logger.info(MessageFormat.format("[{0}]",
                controller.getInfo().getNamespace()));
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
        } catch(final ClassNotFoundException cnfx) {
            logger.fatal(cnfx);
        } catch(final DocumentException dx) {
            logger.fatal(dx);
        } catch (final FileNotFoundException fnfx) {
            logger.fatal(fnfx);
        } catch (final IllegalAccessException iax) {
            logger.fatal(iax);
        } catch (final InstantiationException ix) {
            logger.fatal(ix);
        } catch (final IOException iox) {
            logger.fatal(iox);
        }catch (final XmlPullParserException xppx) {
            logger.fatal(xppx);
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
        logger = Logger.getLogger(getClass());
	}

    /**
     * Start the archive service.
     *
     */
    private void startArchive() {
        ArchiveModel.getModel().start();
    }

    /**
     * Stop the archive service.
     *
     */
    private void stopArchive() {
        ArchiveModel.getModel().stop();
    }
}
