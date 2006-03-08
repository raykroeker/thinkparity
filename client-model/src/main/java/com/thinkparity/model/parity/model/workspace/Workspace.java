/*
 * Aug 6, 2005
 */
package com.thinkparity.model.parity.model.workspace;

import java.io.File;
import java.net.URL;

/**
 * Workspace
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Workspace {

	/**
	 * Obtain the data directory.
	 * 
	 * @return The data directory.
	 */
	public File getDataDirectory();

	/**
	 * Obtain the URL representing the data directory for the parity workspace.
	 * 
	 * @return URL
	 */
	public URL getDataURL();

	/**
	 * Obtain the index directory.
	 * 
	 * @return The index directory.
	 */
	public File getIndexDirectory();

	/**
	 * Obtain a Zip archive of the workspace logs.
	 * 
	 * @return A zip archive of the workspace logs.
	 */
	public File getLogArchive();

	/**
	 * Obtain the URL representing the log directory for the parity workspace.
	 * 
	 * @return URL
	 */
	public URL getLoggerURL();

	/**
	 * Obtain the preferences for the workspace.
	 * 
	 * @return <code>Preferences</code>
	 */
	public Preferences getPreferences();

	/**
	 * Obtain the URL representing the directory for the parity workspace.
	 * 
	 * @return URL
	 */
	public URL getWorkspaceURL();
}
