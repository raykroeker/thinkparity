/*
 * Created On: Oct 17, 2005
 * $Id$
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
import com.thinkparity.model.Constants.Directories;

/**
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
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
		if(!Directories.WORKSPACE.exists())
			Assert.assertTrue(
					"[LMODEL] [WORKSPACE INIT] [CANNOT BE CREATED]",
					Directories.WORKSPACE.mkdirs());
		try { return Directories.WORKSPACE.toURL(); }
		catch(final MalformedURLException murlx) {
			murlx.printStackTrace();
			return null;
		}
	}
}
