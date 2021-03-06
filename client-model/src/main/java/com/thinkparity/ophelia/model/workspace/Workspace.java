/*
 * Created On: Aug 6, 2005
 */
package com.thinkparity.ophelia.model.workspace;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javax.sql.DataSource;

import com.thinkparity.service.ServiceFactory;

import com.thinkparity.ophelia.model.util.ShutdownHook;
import com.thinkparity.ophelia.model.util.daemon.DaemonJob;
import com.thinkparity.ophelia.model.util.daemon.DaemonSchedule;
import com.thinkparity.ophelia.model.util.service.ServiceRetryHandler;
import com.thinkparity.ophelia.model.workspace.configuration.Configuration;

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
     * Obtain a session attribute.
     * 
     * @param name
     *            An attribute name <code>String</code>.
     * @return An attribute value <code>Object</code>.
     */
    public Object getAttribute(final String name);

    /**
     * Obtain the attribute names.
     * 
     * @return An attibute name <code>Iterable</code>.
     */
    public Iterable<String> getAttributeNames();

    /**
     * Obtain the buffer.
     * 
     * @return A <code>ByteBuffer</code>.
     */
    public ByteBuffer getBuffer();

    /**
     * Obtain the buffer array.
     * 
     * @return A <code>byte[]</code>.
     */
    public byte[] getBufferArray();

    /**
     * Obtain the buffer synchronization lock.
     * 
     * @return An <code>Object</code>.
     */
    public Object getBufferLock();

    /**
     * Obtain the buffer size used by the workspace.
     * 
     * @return An <code>Integer</code> buffer size.
     */
    public Integer getBufferSize();

    /**
     * Obtain the default character-set for the workspace.
     * 
     * @return A <code>Charset</code>.
     */
    public Charset getCharset();

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
     * Obtain the download directory.
     * 
     * @return The download directory <code>File</code.
     */
    public File getDownloadDirectory();

    /**
     * Obtain the export directory.
     * 
     * @return The export directory <code>File</code>.
     */
    public File getExportDirectory();

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
     * Obtain a service factory.
     * 
     * @param retryHandler
     *            A <code>ServiceRetryHandler</code>.
     * @return A <code>ServiceFactory</code>.
     */
    public ServiceFactory getServiceFactory(
            final ServiceRetryHandler retryHandler);

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
     * Determine whether or not an attribute is set.
     * 
     * @param name
     *            An attribute name <code>String</code>.
     */
    public Boolean isSetAttribute(final String name);

	/**
     * Remove an attribute.
     * 
     * @param name
     *            An attribute name <code>String</code>.
     */
    public void removeAttribute(final String name);

    /**
     * Set a session attribute.
     * 
     * @param name
     *            An attribute name <code>String</code>.
     * @param value
     *            An attribute value <code>Object</code>.
     * @return The previous attribute value.
     */
    public Object setAttribute(final String name, final Object value);

    /**
     * Obtain the workspace configuration.
     * 
     * @return A <code>WorkspaceConfiguration</code>.
     */
    Configuration getConfiguration();

    /**
     * Create a thread; providing the name and runnable.
     * 
     * @param name
     *            A thread name <code>String</code>.
     * @param runnable
     *            A <code>Runnable</code>.
     * @return A <code>Thread</code>.
     */
    Thread newThread(String name, Runnable runnable);

    /**
     * Schedule a job.
     * 
     * @param job
     *            A <code>DaemonJob</code>.
     * @param schedule
     *            A <code>DaemonSchedule</code>.
     */
    void schedule(DaemonJob job, DaemonSchedule schedule);
}
