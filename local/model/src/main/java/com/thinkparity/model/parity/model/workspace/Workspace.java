/*
 * Created On: Aug 6, 2005
 */
package com.thinkparity.model.parity.model.workspace;

import java.io.File;
import java.io.IOException;

/**
 * Workspace
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Workspace {

    /**
     * Create a temporary file within the workspace.
     * 
     * @return A new file.
     */
    public File createTempFile() throws IOException;

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
