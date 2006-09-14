/*
 * Created On: Aug 6, 2005
 */
package com.thinkparity.ophelia.model.workspace;

import java.io.File;
import java.io.IOException;

import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;
import com.thinkparity.ophelia.model.util.xmpp.XMPPSession;

/**
 * Workspace
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Workspace {

    /**
     * Create a temporary file within the workspace. The workspace will attempt
     * to delete the file when thinkParity shuts down. If the jvm abrubpty
     * exists the file will be deleted the next time the workspace is opened.
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
	 * Obtain the index directory.
	 * 
	 * @return The index directory <code>File</code.
	 */
	public File getIndexDirectory();

    /**
     * Obtain the preferences for the workspace.
     * 
     * @return The workspace <code>Preferences</code>.
     */
	public Preferences getPreferences();

    /**
     * Obtain the database session manager for the workspace.
     * 
     * @return A session manager.
     */
    public SessionManager getSessionManager();

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
