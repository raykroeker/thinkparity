/*
 * Aug 6, 2005
 */
package com.thinkparity.model.parity.model.workspace;

import java.net.URL;

/**
 * Workspace
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Workspace {

	/**
	 * Obtain the URL representing the data directory for the parity workspace.
	 * 
	 * @return URL
	 */
	public URL getDataURL();

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
