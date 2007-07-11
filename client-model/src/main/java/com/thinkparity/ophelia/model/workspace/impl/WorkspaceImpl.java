/*
 * Created On: Oct 17, 2005
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
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
import com.thinkparity.ophelia.model.Constants.Release;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.util.ShutdownHook;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceException;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

/**
 * <b>Title:</b>thinkParity OpheliaModel Workspace Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.37
 */
public final class WorkspaceImpl implements Workspace {

    /** The workspace <code>ByteBuffer</code>. */
    private ByteBuffer buffer;

    /** The workspace buffer <code>byte[]</code>. */
    private byte[] bufferArray;

    /** The workspace buffer synchronization lock <code>Object</code>. */
    private Object bufferLock;

    /** A list of event listeners for each of the implementation classes. */
    private ListenersImpl listenersImpl;

    /** An apache logger. */
    private final Log4JWrapper logger;

    /** The persistence manager. */
    private PersistenceManagerImpl persistenceManagerImpl;

    /** The workspace session data. */
    private Map<String, Object> sessionData;

    /** A list of shutdown hooks to execute upon jvm termination. */
    private List<ShutdownHook> shutdownHooks;

    /** The workspace file system. */
    private final FileSystem workspace;

    /**
     * Create WorkspaceImpl.
     * 
     * @param workspace
     *            A workspace directory <code>File</code>.
     */
	public WorkspaceImpl(final File workspace) {
        super();
        this.logger = new Log4JWrapper(getClass());
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
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getDefaultBuffer()
     *
     */
    public ByteBuffer getBuffer() {
        if (null == buffer) {
            // BUFFER - WorkspaceImpl#getBuffer() - 2MB
            buffer = ByteBuffer.allocateDirect(getBufferSize());
        }
        return buffer;
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getBufferArray()
     *
     */
    public byte[] getBufferArray() {
        if (null == bufferArray) {
            // BUFFER - WorkspaceImpl#getBufferArray() - 2MB
            bufferArray = new byte[getBufferSize()];
        }
        return bufferArray;
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getBufferLock()
     *
     */
    public Object getBufferLock() {
        if (null == bufferLock) {
            bufferLock = new Object();
        }
        return bufferLock;
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getBufferSize()
     * 
     */
    public Integer getBufferSize() {
        // BUFFER - 2MB - WorkspaceImpl#getBufferSize()
        return 1024 * 1024 * 2;
    }            

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getCharset()
     *
     */
    public Charset getCharset() {
        return StringUtil.Charset.UTF_8.getCharset();
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
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getDownloadDirectory()
     *
     */
    public File getDownloadDirectory() {
        return initChild(DirectoryNames.Workspace.DOWNLOAD);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getIndexDirectory()
     * 
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
     * @see java.lang.Object#hashCode()
     * 
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
        try {
            final Session session = persistenceManagerImpl.openSession();
            final Boolean isInitialized;
            try {
                // check for the existence of the META_DATA table
                session.openMetaData();
                session.getMetaDataTables("META_DATA");
                if (session.nextResult()) {
                    session.getMetaDataTables();
                    final String sql =
                        new StringBuffer("select META_DATA_VALUE ")
                        .append("from META_DATA ")
                        .append("where META_DATA_KEY=?")
                        .toString();
                    session.prepareStatement(sql);
                    session.setString(1, "thinkparity.workspace-initialized");
                    session.executeQuery();
                    if (session.nextResult()) {
                        isInitialized = Boolean.valueOf(
                                session.getString("META_DATA_VALUE"));
                    } else {
                        isInitialized = Boolean.FALSE;
                    }
                } else {
                    isInitialized = Boolean.FALSE;
                }
            } finally {
                session.close();
            }
            return isInitialized;
        } catch (final Throwable t) {
            throw new WorkspaceException("Cannot determine workspace initialization.", t);
        }
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
    public Object setAttribute(final String name, final Object value) {
        return sessionData.put(name, value);
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
     * testing mode; enable the sql and service debuggers.
     * 
     */
    private void bootstrapLog4J() {
        final Properties logging = ConfigFactory.newInstance("log4j.properties");
        final File loggingRoot = getLogDirectory();
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
        // metrics additivity
        logging.setProperty("log4j.additivity.METRIX_DEBUGGER", "false");
        // sql appender
        logging.setProperty("log4j.appender.SQL_DEBUGGER", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.SQL_DEBUGGER.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
        logging.setProperty("log4j.appender.SQL_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.SQL_DEBUGGER.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
        logging.setProperty("log4j.appender.SQL_DEBUGGER.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity SQL.log"));
        // sql additivity
        logging.setProperty("log4j.additivity.SQL_DEBUGGER", "false");
        // xa appender
        logging.setProperty("log4j.appender.XA_DEBUGGER", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.XA_DEBUGGER.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
        logging.setProperty("log4j.appender.XA_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.XA_DEBUGGER.layout.ConversionPattern", Constants.Log4J.LAYOUT_CONVERSION_PATTERN);
        logging.setProperty("log4j.appender.XA_DEBUGGER.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity XA.log"));
        // xa additivity
        logging.setProperty("log4j.additivity.XA_DEBUGGER", "false");
        // service appender
        logging.setProperty("log4j.appender.SERVICE_DEBUGGER", "org.apache.log4j.RollingFileAppender");
        logging.setProperty("log4j.appender.SERVICE_DEBUGGER.MaxFileSize", Constants.Log4J.MAX_FILE_SIZE);
        logging.setProperty("log4j.appender.SERVICE_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
        logging.setProperty("log4j.appender.SERVICE_DEBUGGER.layout.ConversionPattern", Constants.Log4J.SERVICE_CONVERSION_PATTERN);
        logging.setProperty("log4j.appender.SERVICE_DEBUGGER.File",
                MessageFormat.format("{0}{1}{2}", loggingRoot,
                        File.separatorChar, "thinkParity Service.log"));
        // service additivity
        logging.setProperty("log4j.additivity.SERVICE_DEBUGGER", "false");
        // loggers
        logging.setProperty("log4j.additivity.com.thinkparity.ophelia", "false");
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
                "log4j.renderer.com.thinkparity.codebase.model.artifact.Artifact",
                "com.thinkparity.codebase.model.util.logging.or.ArtifactRenderer");
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
                "log4j.renderer.com.thinkparity.codebase.model.migrator.Release",
                "com.thinkparity.codebase.model.util.logging.or.ReleaseRenderer");
        logging.setProperty(
                "log4j.renderer.com.thinkparity.codebase.model.user.User",
                "com.thinkparity.codebase.model.util.logging.or.UserRenderer");
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(logging);
        new Log4JWrapper("DEFAULT").logInfo("{0} - {1}", "thinkParity", Release.NAME);
        new Log4JWrapper("METRIX_DEBUGGER").logInfo("{0} - {1}", "thinkParity", Release.NAME);
        new Log4JWrapper("SQL_DEBUGGER").logInfo("{0} - {1}", "thinkParity", Release.NAME);
        new Log4JWrapper("XA_DEBUGGER").logInfo("{0} - {1}", "thinkParity", Release.NAME);
        new Log4JWrapper("SERVICE_DEBUGGER").logInfo("{0} - {1}", "thinkParity", Release.NAME);
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
