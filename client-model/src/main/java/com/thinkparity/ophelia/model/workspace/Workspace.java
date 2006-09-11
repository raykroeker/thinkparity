/*
 * Created On: Aug 6, 2005
 */
package com.thinkparity.ophelia.model.workspace;

import java.io.File;
import java.io.IOException;

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
     * @return A new file.
     */
    public File createTempFile() throws IOException;

    /**
     * Create a temporary file within the workspace. The workspace will attempt
     * to create a temporary file represents the artifact.
     * 
     * @param suffix
     *            The suffix string to be used in generating the file's name.
     * @return A temporary file.
     * @throws IOException
     */
    public File createTempFile(final String suffix) throws IOException;

    /**
	 * Obtain the data directory.
	 * 
	 * @return The data directory.
	 */
	public File getDataDirectory();

	/**
	 * Obtain the index directory.
	 * 
	 * @return The index directory.
	 */
	public File getIndexDirectory();

	/**
     * Obtain the preferences for the workspace.
     * 
     * @return thinkParity preferences.
     */
	public Preferences getPreferences();

	/**
     * Obtain the workspace directory.
     * 
     * @return A file.
     */
    public File getWorkspaceDirectory();
}
