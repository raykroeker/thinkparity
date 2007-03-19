/*
 * Created On: Oct 17, 2005
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.thinkparity.codebase.Constants;
import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.Assertion;
import com.thinkparity.codebase.config.ConfigFactory;
import com.thinkparity.codebase.event.EventListener;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.util.jta.Transaction;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.Constants.DirectoryNames;
import com.thinkparity.ophelia.model.Constants.FileNames;
import com.thinkparity.ophelia.model.util.ShutdownHook;
import com.thinkparity.ophelia.model.util.xmpp.XMPPSession;
import com.thinkparity.ophelia.model.util.xmpp.XMPPSessionImpl;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceException;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class WorkspaceImpl implements Workspace {

    /** The workspace default <code>ByteBuffer</code>. */
    private ByteBuffer defaultBuffer;

    /** A list of event listeners for each of the implementation classes. */
    private ListenersImpl listenersImpl;

    /** An apache logger. */
    private final Log4JWrapper logger;

    /** The operating <code>Mode</code>. */
    private final Mode mode;

    /** The persistence manager. */
    private PersistenceManagerImpl persistenceManagerImpl;

    /** The workspace session data. */
    private Map<String, Object> sessionData;

    /** A list of shutdown hooks to execute upon jvm termination. */
    private List<ShutdownHook> shutdownHooks;

    /** The workspace file system. */
    private final FileSystem workspace;

    /** The xmpp session. */
    private XMPPSessionImpl xmppSessionImpl;

    /** Create WorkspaceImpl. */
	public WorkspaceImpl(final File workspace) {
        super();
        this.logger = new Log4JWrapper(getClass());
        this.mode = Mode.valueOf(System.getProperty("thinkparity.mode"));
        this.workspace = initRoot(workspace);
        bootstrapLog4J();
	}

    /**
     * Add a model's event listener.
     * 
     * @param <T>
     *            A thinkParity event listener type.
     * @param impl
     *            A thinkParity model impl.
     * @param listener
     *            A thinkParity event listener.
     */
    public <T extends EventListener> boolean addListener(
            final Model impl, final T listener) {
        return listenersImpl.add(impl, listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#addShutdownHook(com.thinkparity.ophelia.model.util.ShutdownHook)
     *
     */
    public Boolean addShutdownHook(final ShutdownHook shutdownHook) {
        if (shutdownHooks.contains(shutdownHook)) {
            return Boolean.FALSE;
        }
        final boolean modified = shutdownHooks.add(shutdownHook);
        Collections.sort(shutdownHooks);
        return Boolean.valueOf(modified);
    }

    /**
     * Begin the workspace initialization.
     *
     */
    public void beginInitialize() {
        persistenceManagerImpl.beginInitialize();
    }

    /**
     * Close the workspace.
     *
     */
    public void close() {
        listenersImpl.stop();
        listenersImpl = null;

        persistenceManagerImpl.stop();
        persistenceManagerImpl = null;

        sessionData.clear();
        sessionData = null;

        xmppSessionImpl = null;

        FileUtil.deleteTree(initChild(DirectoryNames.Workspace.TEMP));

        // run the workspace shutdown hooks
        logger.logTrace("Workspace {0} is closing.", getName());
        for(final ShutdownHook shutdownHook : shutdownHooks) {
            logger.logTrace("Workspace {0} priority {1} hook {2} running.",
                    getName(), shutdownHook.getPriority(),
                    shutdownHook.getName());
            try {
                shutdownHook.run();
            } catch (final Throwable t) {
                logger.logFatal(t, MessageFormat.format(
                    "A fatal error occured running shutdown hook {2} priority {1} in workspace {0}.",
                        getName(), shutdownHook.getPriority(), shutdownHook.getName()));
            }
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#createTempDirectory()
     */
    public File createTempDirectory() throws IOException {
        final StackTraceElement caller = StackUtil.getCaller();
        final String tempFileSuffix = new StringBuffer(".")
                .append(StringUtil.searchAndReplace(caller.getClassName(), ".", "_"))
                .append("#").append(caller.getMethodName())
                .toString();
        return createTempDirectory(tempFileSuffix);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#createTempDirectory(java.lang.String)
     */
    public File createTempDirectory(final String suffix) throws IOException {
        final File temp = initChild(DirectoryNames.Workspace.TEMP);
        final File tempDirectory = new File(temp, suffix);
        Assert.assertTrue(tempDirectory.mkdir(),
                "Could not create temp directory {0}.", tempDirectory);
        return tempDirectory;
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#createTempFile()
     */
    public File createTempFile() throws IOException {
        final StackTraceElement caller = StackUtil.getCaller();
        final String tempFileSuffix = new StringBuffer(".")
                .append(StringUtil.searchAndReplace(caller.getClassName(), ".", "_"))
                .append("#").append(caller.getMethodName())
                .toString();
        return createTempFile(tempFileSuffix);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#createTempFile(java.lang.String)
     */
    public File createTempFile(final String suffix) throws IOException {
        final File temp = initChild(DirectoryNames.Workspace.TEMP);
        return File.createTempFile(Constants.File.TEMP_FILE_PREFIX, suffix, temp);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (null == obj)
            return false;
        if (this == obj)
            return true;
        if (WorkspaceImpl.class.isAssignableFrom(obj.getClass()))
            return ((WorkspaceImpl) obj).workspace.equals(workspace);
        else
            return false;
    }

    /**
     * Finish the workspace initialization.
     *
     */
    public void finishInitialize() {
        persistenceManagerImpl.finishInitialize();
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getAttribute(java.lang.String)
     *
     */
    public Object getAttribute(final String name) {
        return sessionData.get(name);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getAttributeNames()
     *
     */
    public Iterable<String> getAttributeNames() {
        return Collections.unmodifiableSet(sessionData.keySet());
    }            

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getDataDirectory()
     */
    public File getDataDirectory() {
        return initChild(DirectoryNames.Workspace.DATA);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getDataSource()
     *
     */
    public DataSource getDataSource() {
        return persistenceManagerImpl.getDataSource();
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getDefaultBuffer()
     *
     */
    public ByteBuffer getDefaultBuffer() {
        if (null == defaultBuffer) {
            defaultBuffer = ByteBuffer.allocate(getDefaultBufferSize());
        }
        return defaultBuffer;
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getDefaultBufferSize()
     *
     */
    public Integer getDefaultBufferSize() {
        // BUFFER - 2MB - WorkspaceImpl#getDefaultBufferSize()
        return 1024 * 1024 * 2;
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getIndexDirectory()
     */
    public File getIndexDirectory() {
        return initChild(DirectoryNames.Workspace.INDEX);
    }

	/**
     * Obtain the model's event listeners.
     * 
     * @param <T>
     *            A thinkParity event listener type.
     * @return A list of typed thinkParity event listeners.
     */
    public <T extends EventListener> List<T> getListeners(final Workspace workspace,
            final Model<T> impl) {
        return listenersImpl.get(impl);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getLog4JDirectory()
     *
     */
    public File getLogDirectory() {
        return initChild(DirectoryNames.Workspace.LOG);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getLogFile()
     */
    public File getLogFile() {
        final File loggingDirectory = initChild(DirectoryNames.Workspace.LOG);
        return new File(loggingDirectory, FileNames.Workspace.Logging.LOGFILE);
    }

    /**
     * Obtain the workspace name.
     * 
     * @return A name <code>String</code>.
     */
    public String getName() {
        return workspace.getRoot().getName();
    }

    /**
     * Obtain the shutdownHooks
     *
     * @return The List<ShutdownHook>.
     */
    public List<ShutdownHook> getShutdownHooks() {
        return Collections.unmodifiableList(shutdownHooks);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getTransaction()
     *
     */
    public Transaction getTransaction() {
        return persistenceManagerImpl.getTransaction();
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getWorkspaceDirectory()
     */
    public File getWorkspaceDirectory() {
        return workspace.getRoot();
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getXMPPSession()
     */
    public XMPPSession getXMPPSession() {
        return xmppSessionImpl;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return workspace.hashCode();
    }

    /**
     * Determine if the workspace is initialized.
     * 
     * @return True if it has already been initialized.
     */
    public Boolean isInitialized() {
        return persistenceManagerImpl.isInitialized();
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#isSetAttribute(java.lang.String)
     *
     */
    public Boolean isSetAttribute(final String name) {
        return sessionData.containsKey(name);
    }

    /**
     * Open the workspace.
     * 
     */
    public void open() {
        listenersImpl = new ListenersImpl(this);
        listenersImpl.start();

        persistenceManagerImpl = new PersistenceManagerImpl(this);
        persistenceManagerImpl.start();

        sessionData = new Hashtable<String, Object>();

        xmppSessionImpl = new XMPPSessionImpl(XMPPSessionDebugger.class);

        shutdownHooks = new ArrayList<ShutdownHook>();
        
        FileUtil.deleteTree(initChild(DirectoryNames.Workspace.TEMP));
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#removeAttribute(java.lang.String)
     *
     */
    public void removeAttribute(final String name) {
        sessionData.remove(name);
    }

    /**
     * Remove a model's event listener.
     * 
     * @param <T>
     *            A thinkParity event listener type.
     * @param listener
     *            A thinkParity event listener.
     */
    public <T extends EventListener> boolean removeListener(
            final Model impl, final T listener) {
        return listenersImpl.remove(impl, listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#setAttribute(java.lang.String, java.lang.Object)
     *
     */
    public void setAttribute(final String name, final Object value) {
        sessionData.put(name, value);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(workspace)
                .toString();
    }

    /**
     * Translate an error into a parity unchecked error.
     * 
     * @param t
     *            An error.
     */
    RuntimeException translateError(final Throwable t) {
        if (WorkspaceException.class.isAssignableFrom(t.getClass())) {
            return (WorkspaceException) t;
        } else if (Assertion.class.isAssignableFrom(t.getClass())) {
            return (Assertion) t;
        } else {
            final Object errorId = getErrorId(t);
            logger.logError(t, "{0}", errorId);
            return new WorkspaceException(errorId.toString(), t);
        }
    }

    /**
     * Initialize logging. Check the operating mode. If in development or
     * testing mode; enable the sql and xmpp debuggers.
     * 
     */
    private void bootstrapLog4J() {
        /* HACK if the logging root is set; we know we are being run within the
         * thinkParity server and need not configure log4j */
        final String loggingRootProperty = System.getProperty("thinkparity.logging.root");
        final boolean desktop = null == loggingRootProperty;
        if (desktop) {
            final Properties logging = bootstrapLog4JConfig(mode);
            final File loggingRoot = bootstrapLog4JRoot(mode);
            // console appender
            logging.setProperty("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
            logging.setProperty("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
            logging.setProperty("log4j.appender.CONSOLE.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
            // default appender
            logging.setProperty("log4j.appender.DEFAULT", "org.apache.log4j.RollingFileAppender");
            logging.setProperty("log4j.appender.DEFAULT.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
            logging.setProperty("log4j.appender.DEFAULT.layout", "org.apache.log4j.PatternLayout");
            logging.setProperty("log4j.appender.DEFAULT.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
            logging.setProperty("log4j.appender.DEFAULT.File",
                    MessageFormat.format("{0}{1}{2}", loggingRoot,
                            File.separatorChar, "thinkParity.log"));
            // metrics appender
            logging.setProperty("log4j.appender.METRIX_DEBUGGER", "org.apache.log4j.RollingFileAppender");
            logging.setProperty("log4j.appender.METRIX_DEBUGGER.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
            logging.setProperty("log4j.appender.METRIX_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
            logging.setProperty("log4j.appender.METRIX_DEBUGGER.layout.ConversionPattern", Constants.Log4J.METRICS_LAYOUT_CONVERSION_PATTERN);
            logging.setProperty("log4j.appender.METRIX_DEBUGGER.File",
                    MessageFormat.format("{0}{1}{2}", loggingRoot,
                            File.separatorChar, "thinkParity Metrics.log"));
            // sql appender
            logging.setProperty("log4j.appender.SQL_DEBUGGER", "org.apache.log4j.RollingFileAppender");
            logging.setProperty("log4j.appender.SQL_DEBUGGER.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
            logging.setProperty("log4j.appender.SQL_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
            logging.setProperty("log4j.appender.SQL_DEBUGGER.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
            logging.setProperty("log4j.appender.SQL_DEBUGGER.File",
                    MessageFormat.format("{0}{1}{2}", loggingRoot,
                            File.separatorChar, "thinkParity SQL.log"));
            // xa appender
            logging.setProperty("log4j.appender.XA_DEBUGGER", "org.apache.log4j.RollingFileAppender");
            logging.setProperty("log4j.appender.XA_DEBUGGER.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
            logging.setProperty("log4j.appender.XA_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
            logging.setProperty("log4j.appender.XA_DEBUGGER.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
            logging.setProperty("log4j.appender.XA_DEBUGGER.File",
                    MessageFormat.format("{0}{1}{2}", loggingRoot,
                            File.separatorChar, "thinkParity XA.log"));
            // xmpp appender
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
                if (desktop)
                    logging.setProperty("log4j.rootLogger", "WARN, DEFAULT");
    
                logging.setProperty("log4j.logger.com.thinkparity.ophelia", "WARN, DEFAULT");
                logging.setProperty("log4j.additivity.com.thinkparity.ophelia", "false");

                logging.setProperty("log4j.logger.METRIX_DEBUGGER", "NONE");
                logging.setProperty("log4j.additivity.METRIX_DEBUGGER", "false");

                logging.setProperty("log4j.logger.SQL_DEBUGGER", "NONE");
                logging.setProperty("log4j.additivity.SQL_DEBUGGER", "false");

                logging.setProperty("log4j.logger.XA_DEBUGGER", "NONE");
                logging.setProperty("log4j.additivity.XA_DEBUGGER", "false");

                logging.setProperty("log4j.logger.XMPP_DEBUGGER", "NONE");
                logging.setProperty("log4j.additivity.XMPP_DEBUGGER", "false");
                break;
            case DEVELOPMENT:
                if (desktop)
                    logging.setProperty("log4j.rootLogger", "INFO, CONSOLE, DEFAULT");
    
                logging.setProperty("log4j.logger.com.thinkparity.ophelia", "INFO, CONSOLE, DEFAULT");
                logging.setProperty("log4j.additivity.com.thinkparity.ophelia", "false");
    
                logging.setProperty("log4j.logger.METRIX_DEBUGGER", "TRACE, METRIX_DEBUGGER");
                logging.setProperty("log4j.additivity.METRIX_DEBUGGER", "false");

                logging.setProperty("log4j.logger.SQL_DEBUGGER", "DEBUG, SQL_DEBUGGER");
                logging.setProperty("log4j.additivity.SQL_DEBUGGER", "false");
    
                logging.setProperty("log4j.logger.XA_DEBUGGER", "TRACE, XA_DEBUGGER");
                logging.setProperty("log4j.additivity.XA_DEBUGGER", "false");

                logging.setProperty("log4j.logger.XMPP_DEBUGGER", "DEBUG, XMPP_DEBUGGER");
                logging.setProperty("log4j.additivity.XMPP_DEBUGGER", "false");
                break;
            case TESTING:
                if (desktop)
                    logging.setProperty("log4j.rootLogger", "INFO, DEFAULT");
    
                logging.setProperty("log4j.logger.com.thinkparity.ophelia", "INFO, DEFAULT");
                logging.setProperty("log4j.additivity.com.thinkparity.ophelia", "false");

                logging.setProperty("log4j.logger.METRIX_DEBUGGER", "DEBUG, METRIX_DEBUGGER");
                logging.setProperty("log4j.additivity.METRIX_DEBUGGER", "false");

                logging.setProperty("log4j.logger.SQL_DEBUGGER", "DEBUG, SQL_DEBUGGER");
                logging.setProperty("log4j.additivity.SQL_DEBUGGER", "false");

                logging.setProperty("log4j.logger.XA_DEBUGGER", "INFO, XA_DEBUGGER");
                logging.setProperty("log4j.additivity.XA_DEBUGGER", "false");

                logging.setProperty("log4j.logger.XMPP_DEBUGGER", "DEBUG, XMPP_DEBUGGER");
                logging.setProperty("log4j.additivity.XMPP_DEBUGGER", "false");
                break;
            default:
                throw Assert.createUnreachable("Unknown operating mode.");
            }
            // renderers
            logging.setProperty(
                    "log4j.renderer.com.thinkparity.codebase.jabber.JabberId",
                    "com.thinkparity.codebase.log4j.or.JabberIdRenderer");
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
                    "log4j.renderer.com.thinkparity.codebase.model.document.Document",
                    "com.thinkparity.codebase.model.util.logging.or.DocumentRenderer");
            logging.setProperty(
                    "log4j.renderer.com.thinkparity.codebase.model.document.DocumentVersion",
                    "com.thinkparity.codebase.model.util.logging.or.DocumentVersionRenderer");
            logging.setProperty(
                    "log4j.renderer.com.thinkparity.codebase.model.user.User",
                    "com.thinkparity.codebase.model.util.logging.or.UserRenderer");
            logging.setProperty(
                    "log4j.renderer.org.jivesoftware.smack.packet.Packet",
                    "com.thinkparity.ophelia.model.util.logging.or.PacketRenderer");
            LogManager.resetConfiguration();
            PropertyConfigurator.configure(logging);
            new Log4JWrapper("DEFAULT").logInfo("{0} - {1}", "thinkParity", "1.0");
            new Log4JWrapper("METRIX_DEBUGGER").logInfo("{0} - {1}", "thinkParity", "1.0");
            new Log4JWrapper("SQL_DEBUGGER").logInfo("{0} - {1}", "thinkParity", "1.0");
            new Log4JWrapper("XA_DEBUGGER").logInfo("{0} - {1}", "thinkParity", "1.0");
            new Log4JWrapper("XMPP_DEBUGGER").logInfo("{0} - {1}", "thinkParity", "1.0");
        }
    }

    /**
     * Create a logging configuration for the operating mode.
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
     * Bootstrap the log4j root directory. If a thinkparity.logging.root exists
     * use it as the logging root directory; otherwise use workspace/logs.
     * 
     * 
     * @param mode
     *            The thinkParity operating <code>Mode</code>.
     * @return The logging root directory <code>File</code>.
     */
    private File bootstrapLog4JRoot(final Mode mode) {
        final String loggingRootProperty = System.getProperty("thinkparity.logging.root");
        if (null == loggingRootProperty) {
            return new File(workspace.getRoot(), "logs");
        } else {
            final File loggingRoot = new File(loggingRootProperty);
            Assert.assertTrue(loggingRoot.exists() && loggingRoot.isDirectory()
                    && loggingRoot.canRead() && loggingRoot.canWrite(),
                    "Specified logging root {0} is not valid.", loggingRoot);
            return loggingRoot;
        }
    }

    /**
     * Obtain an error id.
     * 
     * @return An error id.
     */
    private Object getErrorId(final Throwable t) {
        return MessageFormat.format("[{0}] [{1}] - [{2}]",
                    StackUtil.getFrameClassName(2).toUpperCase(),
                    StackUtil.getFrameMethodName(2).toUpperCase(),
                    t.getMessage());
    }

    /**
	 * Initialize an immediate child of the workspace. This will create the
	 * child (directory) if it does not already exist.
	 * 
	 * @param workspaceDirectory
	 *            The workspace root directory.
	 * @param child
	 *            The name of a child directory of the workspace.
	 * @return The child directory of the workspace.
	 */
	private File initChild(final String child) {
		final File childFile = new File(workspace.getRoot(), child);
		if (!childFile.exists())
			Assert.assertTrue(
                    MessageFormat.format("CANNOT INIT CHILD:  {0}", child),
					childFile.mkdir());
		return childFile;
	}

    /**
	 * Initialize the workspace root. This will obtain a handle to the workspace
	 * root directory, and create it if it does not already exist; then convert
	 * it to a URL.
	 * 
	 * @return The workspace directory.
	 */
	private FileSystem initRoot(final File workspace) {
		if (!workspace.exists())
			Assert.assertTrue(workspace.mkdirs(),
                    "Cannot initialize workspace {0}.", workspace);
        return new FileSystem(workspace);
	}
}
