/*
 * Oct 17, 2005
 */
package com.thinkparity.model.parity.model.workspace;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.util.SystemUtil;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 * 
 * TODO: Move the corporate name and application name referenced in the
 * directory creation to another location.
 */
class WorkspaceHelper {

	/**
	 * Create a WorkspaceHelper.
	 */
	WorkspaceHelper() { super(); }

	/**
	 * Open the parity workspace.
	 * 
	 * @return An interface with which the client can interact with the
	 *         workspace.
	 */
	Workspace openWorkspace() {
		final URL workspaceRootURL = initWorkspaceRootURL();
		final PreferencesHelper preferencesHelper = new PreferencesHelper();
		return new Workspace() {
			/**
			 * URL to the data directory within the workspace.
			 */
			final URL dataURL = initWorkspaceChild(workspaceRootURL, "data");

			/**
			 * URL to the log file directory within the workspace.
			 */
			final URL loggerURL = initWorkspaceChild(workspaceRootURL, "logs");

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Workspace#getDataURL()
			 */
			public URL getDataURL() { return dataURL; }

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Workspace#getLoggerURL()
			 */
			public URL getLoggerURL() { return loggerURL; }

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Workspace#getPreferences()
			 */
			public Preferences getPreferences() {
				return preferencesHelper.getPreferences();
			}

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Workspace#getWorkspaceURL()
			 */
			public URL getWorkspaceURL() { return workspaceRootURL; }
		};
	}

	/**
	 * Initialize an immediate child of the workspace. This will create the
	 * child (directory) if it does not already exist.
	 * 
	 * @param workspaceURL
	 *            The workspace root url.
	 * @param child
	 *            The name of a child directory of the workspace.
	 * @return The url of the child directory of the workspace.
	 */
	private URL initWorkspaceChild(final URL workspaceURL, final String child) {
		final File childFile = new File(workspaceURL.getFile(), child);
		if(!childFile.exists())
			Assert.assertTrue(
					"Cannot create workspace sub-directory:  " + child,
					childFile.mkdir());
		try { return childFile.toURL(); }
		catch(MalformedURLException murlx) {
			murlx.printStackTrace();
			return null;
		}
	}

	/**
	 * Obtain the workspace root directory for a linux environment.
	 * 
	 * @return The workspace root directory for a linux environment.
	 */
	private File initWorkspaceRoot_linux() {
		final StringBuffer linuxPath = new StringBuffer()
			.append(SystemUtil.getenv("HOME"))
			.append(File.separatorChar).append("Parity Software")
			.append(File.separatorChar).append("Parity");
		return new File(linuxPath.toString());
	}

	/**
	 * Obtain the workspace root directory for a win32 environment.
	 * 
	 * @return The workspace root directory for a win32 environment.
	 */
	private File initWorkspaceRoot_win32() {
		final StringBuffer win32Path = new StringBuffer()
			.append(SystemUtil.getenv("APPDATA"))
			.append(File.separatorChar).append("Parity Software")
			.append(File.separatorChar).append("Parity");
		return new File(win32Path.toString());
	}

	/**
	 * Initialize the workspace root. This will obtain a handle to the workspace
	 * root directory, and create it if it does not already exist; then convert
	 * it to a URL.
	 * 
	 * @return The workspace root url.
	 */
	private URL initWorkspaceRootURL() {
		final File workspaceDirectory;
		switch(OSUtil.getOS()) {
		case WINDOWS_2000:
		case WINDOWS_XP:
			workspaceDirectory = initWorkspaceRoot_win32();
			break;
		case LINUX:
			workspaceDirectory = initWorkspaceRoot_linux();
			break;
		default:
			throw Assert.createUnreachable("Unsupported os:  " + OSUtil.getOS());
		}

		if(!workspaceDirectory.exists())
			Assert.assertTrue(
					"Cannot create workspace directory.",
					workspaceDirectory.mkdirs());
		try { return workspaceDirectory.toURL(); }
		catch(MalformedURLException murlx) {
			murlx.printStackTrace();
			return null;
		}
	}
}
