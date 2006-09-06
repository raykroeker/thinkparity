/*
 * Created On: Nov 25, 2005
 */
package com.thinkparity.wildfire;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.jivesoftware.messenger.IQRouter;
import org.jivesoftware.messenger.XMPPServer;
import org.jivesoftware.messenger.container.Plugin;
import org.jivesoftware.messenger.container.PluginManager;
import org.jivesoftware.messenger.handler.IQHandler;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.XPP3Reader;
import org.xmlpull.v1.XmlPullParserException;

import com.thinkparity.model.Version;
import com.thinkparity.model.archive.ArchiveModel;


/**
 * <b>Title:</b>thinkParity Jive Plugin<br>
 * <b>Description:</b>The thinkParity Jive Server Plugin<br>
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.30
 */
public class JivePlugin implements Plugin {

	/** An apache logger. */
    protected Logger logger;

	/** The plugin's handlers. */
    private final List<IQHandler> handlers;

    /** The jive router. */
	private final IQRouter router;

	/** Create JivePlugin. */
	public JivePlugin() {
		super();
        this.handlers = new LinkedList<IQHandler>();
		this.router = XMPPServer.getInstance().getIQRouter();
	}

	/**
	 * @see org.jivesoftware.messenger.container.Plugin#destroyPlugin()
	 */
	public void destroyPlugin() {
        destroyArchive();
	    destroyHandlers();
		destroyLogging();
	}

	/**
	 * @see org.jivesoftware.messenger.container.Plugin#initializePlugin(org.jivesoftware.messenger.container.PluginManager, java.io.File)
	 */
	public void initializePlugin(final PluginManager manager,
            final File pluginDirectory) {
		initializeLogging(pluginDirectory);
		initializeHandlers(pluginDirectory);
        initializeArchive();
		logger.info(Version.toInfo());
	}

	/**
     * Stop the archive service.
     *
     */
    private void destroyArchive() {
        ArchiveModel.getModel().stop();
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
     * Start the archive service.
     *
     */
    private void initializeArchive() {
        ArchiveModel.getModel().start();
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
        logger.info(MessageFormat.format("[{0}]",
                controller.getInfo().getNamespace()));
    }

    /**
	 * Initialize the iq dispatcher.
	 * 
	 */
    private void initializeHandlers(final File pluginDirectory) {
        try {
            final Document document = new XPP3Reader().read(new File(pluginDirectory, "jive-handlers.xml"));
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
	private void initializeLogging(final File pluginDirectory) {
        System.setProperty("thinkparity.log4j.file",
                new File(pluginDirectory, "desdemona.log4j").getAbsolutePath());
        logger = Logger.getLogger(getClass());
	}
}
