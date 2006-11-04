/*
 * Created On: Oct 17, 2005
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.Assertion;
import com.thinkparity.codebase.event.EventListener;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.Constants.DirectoryNames;
import com.thinkparity.ophelia.model.Constants.FileNames;
import com.thinkparity.ophelia.model.Constants.Files;
import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;
import com.thinkparity.ophelia.model.util.ShutdownHook;
import com.thinkparity.ophelia.model.util.xmpp.XMPPSession;
import com.thinkparity.ophelia.model.util.xmpp.XMPPSessionImpl;
import com.thinkparity.ophelia.model.workspace.Preferences;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceException;

import com.thinkparity.ophelia.browser.Version;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class WorkspaceImpl implements Workspace {

    /** A list of event listeners for each of the implementation classes. */
    private ListenersImpl listenersImpl;

    /** An apache logger. */
    private final Logger logger;

    /** The operating <code>Mode</code>. */
    private final Mode mode;

    /** The workspace preferences. */
    private PreferencesImpl preferencesImpl;

    /** The workspace database session manager. */
    private SessionManagerImpl sessionManagerImpl;

    /** A list of shutdown hooks to execute upon jvm termination. */
    private List<ShutdownHook> shutdownHooks;

    /** The workspace file system. */
    private final FileSystem workspace;

    /** The xmpp session. */
    private XMPPSessionImpl xmppSessionImpl;

    /** Create WorkspaceImpl. */
	public WorkspaceImpl(final File workspace) {
        super();
        this.logger = Logger.getLogger(getClass());
        this.mode = Mode.valueOf(System.getProperty("thinkparity.mode"));
        this.workspace = initRoot(workspace);
        bootstrapLogging();
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
            final AbstractModelImpl impl, final T listener) {
        return listenersImpl.add(impl, listener);
    }

    /**
     * Add a shutdown hook.
     * 
     * @param hook
     *            A runnable.
     */
    public boolean addShutdownHook(final ShutdownHook shutdownHook) {
        if (shutdownHooks.contains(shutdownHook)) {
            return false;
        }
        final boolean modified = shutdownHooks.add(shutdownHook);
        Collections.sort(shutdownHooks);
        return modified;
    }

    /**
     * Close the workspace.
     *
     */
    public void close() {
        listenersImpl.stop();
        listenersImpl = null;

        preferencesImpl.save();
        preferencesImpl = null;

        sessionManagerImpl.stop();
        sessionManagerImpl = null;

        xmppSessionImpl = null;

        FileUtil.deleteTree(initChild(DirectoryNames.Workspace.TEMP));
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
        return File.createTempFile(Files.TEMP_FILE_PREFIX, suffix, temp);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (null != obj && obj instanceof WorkspaceImpl) {
            return ((WorkspaceImpl) obj).workspace.equals(workspace);
        }
        return false;
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getDataDirectory()
     */
    public File getDataDirectory() {
        return initChild(DirectoryNames.Workspace.DATA);
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
            final AbstractModelImpl<T> impl) {
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
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getPreferences()
     * 
     */
    public Preferences getPreferences() {
        return preferencesImpl;
    }

	/**
     * @see com.thinkparity.ophelia.model.workspace.Workspace#getSessionManager()
     */
    public SessionManager getSessionManager() {
        return sessionManagerImpl;
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
     * Initialize the workspace.
     *
     */
    public void initialize() {
        preferencesImpl.initialize();
    }

    /**
     * Determine if the workspace has been initialized.
     * 
     * @return True if the workspace has been initialized.
     */
    public Boolean isInitialized() {
        return preferencesImpl.isInitialized();
    }

    /**
     * Open the workspace.
     * 
     */
    public void open() {
        listenersImpl = new ListenersImpl(this);
        listenersImpl.start();

        preferencesImpl = new PreferencesImpl(this);
        preferencesImpl.load();

        sessionManagerImpl = new SessionManagerImpl(this);
        sessionManagerImpl.start();

        xmppSessionImpl = new XMPPSessionImpl(XMPPSessionDebugger.class);

        shutdownHooks = new ArrayList<ShutdownHook>();
        
        FileUtil.deleteTree(initChild(DirectoryNames.Workspace.TEMP));
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
            final AbstractModelImpl impl, final T listener) {
        return listenersImpl.remove(impl, listener);
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
            logger.error(errorId, t);
            return new WorkspaceException(errorId.toString(), t);
        }
    }

    /**
     * Initialize logging. Check the operating mode. If in development or
     * testing mode; enable the sql and xmpp debuggers.
     * 
     */
    private void bootstrapLogging() {
        if ((Mode.DEVELOPMENT == mode || Mode.TESTING == mode)) {
            final Properties logging = new Properties();
            // sql debugger
            logging.setProperty("log4j.logger.SQL_DEBUGGER", "DEBUG, SQL_DEBUGGER");
            logging.setProperty("log4j.appender.SQL_DEBUGGER", "org.apache.log4j.RollingFileAppender");
            logging.setProperty("log4j.appender.SQL_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
            logging.setProperty("log4j.appender.SQL_DEBUGGER.layout.ConversionPattern", "%d %m%n");
            logging.setProperty("log4j.appender.SQL_DEBUGGER.File",
                    MessageFormat.format("{0}{1}logs{1}{2}",
                            workspace.getRoot().getAbsolutePath(),
                            File.separatorChar, "thinkParity SQL.log"));
            // xmpp debugger
            logging.setProperty("log4j.logger.XMPP_DEBUGGER", "DEBUG, XMPP_DEBUGGER");
            logging.setProperty("log4j.appender.XMPP_DEBUGGER", "org.apache.log4j.RollingFileAppender");
            logging.setProperty("log4j.appender.XMPP_DEBUGGER.layout", "org.apache.log4j.PatternLayout");
            logging.setProperty("log4j.appender.XMPP_DEBUGGER.layout.ConversionPattern", "%d %m%n");
            logging.setProperty("log4j.appender.XMPP_DEBUGGER.File",
                    MessageFormat.format("{0}{1}logs{1}{2}",
                            workspace.getRoot().getAbsolutePath(),
                            File.separatorChar, "thinkParity XMPP.log"));
            PropertyConfigurator.configure(logging);
            new Log4JWrapper("SQL_DEBUGGER").logInfo("{0} - {1} - {2}",
                    Version.getName(), Version.getMode(), Version.getBuildId());
            new Log4JWrapper("XMPP_DEBUGGER").logInfo("{0} - {1} - {2}",
                    Version.getName(), Version.getMode(), Version.getBuildId());

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
			Assert.assertTrue(
                    MessageFormat.format("CANNOT INIT ROOT:  {0}", workspace),
                    workspace.mkdirs());
        return new FileSystem(workspace);
	}

}
