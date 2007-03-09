/*
 * Created On: Aug 6, 2005
 */
package com.thinkparity.ophelia.model.workspace;

import java.io.File;
import java.io.IOException;

import javax.sql.DataSource;

import com.thinkparity.codebase.model.util.jta.Transaction;

import com.thinkparity.ophelia.model.util.ShutdownHook;
import com.thinkparity.ophelia.model.util.xmpp.XMPPSession;

/**
 * <b>Title:</b>thinkParity Workspace<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
public interface Workspace {

    /**
     * Add a shutdown hook to the workspace.
     * 
     * @param shutdownHook
     *            A <code>ShutdownHook</code>.
     */
    public Boolean addShutdownHook(final ShutdownHook shutdownHook);

    /**
     * Create a temporary directory within the workspace. The workspace will
     * attempt to delete the directory when thinkParity shuts down. If the jvm
     * abrubptly exits; the directory will be deleted the next time the
     * workspace is opened.
     * 
     * @return A temporary directory <code>File</code>.
     * @throws IOException
     */
    public File createTempDirectory() throws IOException;

    /**
     * Create a temporary directory within the workspace. The workspace will
     * attempt to delete the directory when thinkParity shuts down. If the jvm
     * abrubptly exits; the directory will be deleted the next time the
     * workspace is opened.
     * 
     * @param suffix
     *            The suffix string to be used in generating the directory's
     *            name.
     * @return A temporary directory <code>File</code>.
     * @throws IOException
     */
    public File createTempDirectory(final String suffix) throws IOException;

    /**
     * Create a temporary file within the workspace. The workspace will attempt
     * to delete the file when thinkParity shuts down. If the jvm abrubpty
     * exits the file will be deleted the next time the workspace is opened.
     * 
     * @return A <code>File</code>.
     */
    public File createTempFile() throws IOException;

    /**
     * Create a temporary file within the workspace. The workspace will attempt
     * to create a temporary file represents the artifact.
     * 
     * @param suffix
     *            The suffix string to be used in generating the file's name.
     * @return A <code>File</code>.
     * @throws IOException
     */
    public File createTempFile(final String suffix) throws IOException;

    /**
     * Obtain the data directory.
     * 
     * @return The data directory <code>File</code>.
     */
	public File getDataDirectory();

    /**
     * Obtain the data source for the workspace.
     * 
     * @return A <code>DataSource</code>.
     */
    public DataSource getDataSource();

    /**
     * Obtain the default buffer size.
     * 
     * @return A buffer size <code>Integer</code>.
     */
    public Integer getDefaultBufferSize();

    /**
	 * Obtain the index directory.
	 * 
	 * @return The index directory <code>File</code.
	 */
	public File getIndexDirectory();

	/**
     * Obtain the log directory.
     * 
     * @return A directory <code>File</code>.
     */
    public File getLogDirectory();

    /**
     * Obtain the log file for the workspace.
     * 
     * @return A log <code>File</code>.
     */
    public File getLogFile();

    /**
     * Obtain a transaction.
     * 
     * @return A <code>Transaction</code>.
     */
    public Transaction getTransaction();

    /**
     * Obtain the workspace directory.
     * 
     * @return The workspace directory <code>File</code>.
     */
    public File getWorkspaceDirectory();

    /**
     * Obtain the xmpp session for the workspace.
     * 
     * @return An <code>XMPPSession</code>.
     */
    public XMPPSession getXMPPSession();
}
