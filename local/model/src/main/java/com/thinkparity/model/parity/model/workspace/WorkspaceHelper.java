/*
 * Oct 17, 2005
 */
package com.thinkparity.model.parity.model.workspace;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.ZipUtil;
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
	 * 
	 */
	WorkspaceHelper() { super(); }

	/**
	 * Open the parity workspace.
	 * 
	 * @return An interface with which the client can interact with the
	 *         workspace.
	 */
	Workspace openWorkspace() {
		final URL workspaceRootURL = initRoot();
		final File workspaceRoot = new File(workspaceRootURL.getFile());

		return new Workspace() {
			/**
			 * URL to the data directory within the workspace.
			 * 
			 */
			final URL dataURL = initChild(workspaceRootURL, "data");

			/**
			 * File the index resides in.
			 * 
			 */
			final File indexDirectory = initChild(workspaceRoot, "index");

			/**
			 * URL to the log file directory within the workspace.
			 * 
			 */
			final URL loggerURL = initChild(workspaceRootURL, "logs");

			/**
			 * Workspace preferences.
			 */
			final Preferences preferences =
				new PreferencesHelper(workspaceRoot).getPreferences();

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Workspace#getDataDirectory()
			 * 
			 */
			public File getDataDirectory() {
				return new File(dataURL.getFile());
			}

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Workspace#getDataURL()
			 */
			public URL getDataURL() { return dataURL; }

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Workspace#getIndexDirectory()
			 * 
			 */
			public File getIndexDirectory() { return indexDirectory; }

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Workspace#getLogArchive()
			 */
			public File getLogArchive() {
				final File logFileDirectory = new File(loggerURL.getFile());
				final File logFileArchive = new File(workspaceRootURL.getFile(), "Logs.zip");
				if(logFileArchive.exists()) {
					Assert.assertTrue(
							"Cannot delete log file archive.",
							logFileArchive.delete());
				}
				try { ZipUtil.createZipFile(logFileArchive, logFileDirectory); }
				catch(final IOException iox) { throw new RuntimeException(iox); }
				return logFileArchive;
			}

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Workspace#getLoggerURL()
			 */
			public URL getLoggerURL() { return loggerURL; }

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Workspace#getPreferences()
			 */
			public Preferences getPreferences() { return preferences; }

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
	 * @param workspaceDirectory
	 *            The workspace root directory.
	 * @param child
	 *            The name of a child directory of the workspace.
	 * @return The child directory of the workspace.
	 */
	private File initChild(final File workspaceDirectory, final String child) {
		final File childFile = new File(workspaceDirectory, child);
		if(!childFile.exists())
			Assert.assertTrue(
					"Cannot create workspace sub-directory:  " + child,
					childFile.mkdir());
		return childFile;
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
	private URL initChild(final URL workspaceURL, final String child) {
		try { return initChild(new File(workspaceURL.getFile()), child).toURL(); }
		catch(MalformedURLException murlx) {
			murlx.printStackTrace();
			return null;
		}
	}

	/**
	 * Initialize the workspace root. This will obtain a handle to the workspace
	 * root directory, and create it if it does not already exist; then convert
	 * it to a URL.
	 * 
	 * @return The workspace root url.
	 */
	private URL initRoot() {
		final File workspaceDirectory;
		switch(OSUtil.getOS()) {
		case WINDOWS_2000:
		case WINDOWS_XP:
			workspaceDirectory = initRootWin32();
			break;
		case LINUX:
			workspaceDirectory = initRootLinux();
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

	/**
	 * Obtain the workspace root directory for a linux environment.
	 * 
	 * @return The workspace root directory for a linux environment.
	 */
	private File initRootLinux() {
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
	private File initRootWin32() {
		final StringBuffer win32Path = new StringBuffer()
			.append(SystemUtil.getenv("APPDATA"))
			.append(File.separatorChar).append("Parity Software")
			.append(File.separatorChar).append("Parity");
		return new File(win32Path.toString());
	}
}
