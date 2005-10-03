/*
 * Jul 23, 2005
 */
package com.thinkparity.model.parity.model.workspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;

import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.util.SystemUtil;

/**
 * WorkspaceModelImpl
 * @author raykroeker@gmail.com
 * @version 1.1
 * @see com.thinkparity.model.parity.model.workspace.WorkspaceModel
 */
class WorkspaceModelImpl extends AbstractModelImpl {

	/**
	 * Cached workspace.
	 */
	private static Workspace cachedWorkspace;

	/**
	 * PreferencesImpl
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class PreferencesImpl implements Preferences {

		/**
		 * PreferencesImpl
		 * Represents the interface to both the user preferences as well as the
		 * system preferences for parity.
		 * @param preferencesURL
		 * @throws IOException
		 * @throws InvalidPreferencesFormatException
		 */
		private PreferencesImpl(final URL systemPreferencesURL, final URL userPreferencesURL) throws IOException,
				InvalidPreferencesFormatException {
			super();
			java.util.prefs.Preferences.importPreferences(new FileInputStream(
					new File(systemPreferencesURL.getFile())));
			java.util.prefs.Preferences.importPreferences(new FileInputStream(
					new File(userPreferencesURL.getFile())));
		}

		/**
		 * @see com.thinkparity.model.parity.model.workspace.Preferences#getLocale()
		 */
		public Locale getLocale() { return Locale.getDefault(); }

		/**
		 * @see com.thinkparity.model.parity.model.workspace.Preferences#getServerHost()
		 */
		public String getServerHost() { return "mob-002.raykroeker.com"; }

		/**
		 * @see com.thinkparity.model.parity.model.workspace.Preferences#getServerPort()
		 */
		public Integer getServerPort() { return 5222; }

		/**
		 * @see com.thinkparity.model.parity.model.workspace.Preferences#getUsername()
		 */
		public String getUsername() { return "system"; }

		/**
		 * @see com.thinkparity.model.parity.model.workspace.Preferences#isSetLocale()
		 */
		public Boolean isSetLocale() { return Boolean.TRUE; }

		/**
		 * @see com.thinkparity.model.parity.model.workspace.Preferences#isSetUsername()
		 */
		public Boolean isSetUsername() { return Boolean.TRUE; }

		/**
		 * @see com.thinkparity.model.parity.model.workspace.Preferences#setLocale(Locale)
		 */
		public void setLocale(Locale locale) {}

		/**
		 * @see com.thinkparity.model.parity.model.workspace.Preferences#setUsername(String)
		 */
		public void setUsername(String username) {}
	}

	/**
	 * WorkspaceImpl
	 * Implementation of the workspace.
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class WorkspaceImpl implements Workspace {

		/**
		 * Parity workspace data directory url.
		 */
		private URL dataURL;

		/**
		 * Parity workspace log directory url.
		 */
		private URL loggerURL;

		/**
		 * Parity workspace preferences directory url.
		 */
		private URL preferencesURL;

		/**
		 * Parity workspace directory url.
		 */
		private URL workspaceURL;

		/**
		 * Create a WorkspaceImpl.
		 * @param dataURL - URL
		 * @param loggerURL - URL
		 * @param preferencesURL - URL
		 * @param workspaceURL - URL
		 */
		private WorkspaceImpl(final URL dataURL, final URL loggerURL,
				final URL preferencesURL, final URL workspaceURL) {
			super();
			this.dataURL = dataURL;
			this.loggerURL = loggerURL;
			this.preferencesURL = preferencesURL;
			this.workspaceURL = workspaceURL;
		}

		/**
		 * Create a URL reference to a preference of a given name.  If the 
		 * preference file does not exist it is created.
		 * @param defaultPreferences Preferences
		 * @param preferenceName String
		 * @return URL
		 */
		private URL createPreferencesURL(
				final java.util.prefs.Preferences defaultPreferences,
				final String preferenceName) throws BackingStoreException,
				IOException, MalformedURLException {
			final File preferenceFile = new File(preferencesURL.getFile(),
					preferenceName + ".xml");
			if(!preferenceFile.exists()) {
				Assert.assertTrue("Cannot create preference file:  "
						+ preferenceName + ".", preferenceFile
						.createNewFile());
				defaultPreferences.exportNode(new FileOutputStream(preferenceFile));
			}
			return preferenceFile.toURL();
		}

		/**
		 * @see com.thinkparity.model.parity.model.workspace.Workspace#getParityTree()
		 */
		public URL getDataURL() { return dataURL; }

		/**
		 * @see com.thinkparity.model.parity.model.workspace.Workspace#getLoggerURL()
		 */
		public URL getLoggerURL() { return loggerURL; }

		/**
		 * @see com.thinkparity.model.parity.model.workspace.Workspace#getPreferencesURL()
		 */
		public Preferences getPreferences() {
			try {
				return new PreferencesImpl(
						createPreferencesURL(
								java.util.prefs.Preferences.systemRoot(),
								"system"),
						createPreferencesURL(
								java.util.prefs.Preferences.userRoot(),
								"user"));
			}
			catch(BackingStoreException bsx) { bsx.printStackTrace(); }
			catch(InvalidPreferencesFormatException ipfx) { ipfx.printStackTrace(); }
			catch(MalformedURLException murlx) { murlx.printStackTrace(); }
			catch(IOException iox) { iox.printStackTrace(); }
			return null;
		}

		/**
		 * @see com.thinkparity.model.parity.model.workspace.Workspace#getWorkspaceURL()
		 */
		public URL getWorkspaceURL() { return workspaceURL; }
	}

	/**
	 * Create a WorkspaceModelImpl
	 */
	WorkspaceModelImpl() { super(); }

	/**
	 * Create the workspace artifact from scratch.
	 * @return <code>Workspace</code>
	 */
	private Workspace createWorkspace() {
		try {
			final URL workspaceURL = getWorkspaceURL();
			return new WorkspaceImpl(
					getWorkspaceChildURL(workspaceURL, ".data"),
					getWorkspaceChildURL(workspaceURL, ".log"),
					getWorkspaceChildURL(workspaceURL, ".prefs"),
					workspaceURL);
		}
		catch(MalformedURLException murlx) {
			throw new RuntimeException("Could not initialize parity workspace.");
		}
	}

	/**
	 * Create a child directory of the workspace.
	 * @param workspaceURL <code>java.net.URL</code>
	 * @param childName <code>java.lang.String</code>
	 * @return <code>java.net.URL</code>
	 * @throws MalformedURLException
	 */
	private URL getWorkspaceChildURL(final URL workspaceURL, final String childName)
			throws MalformedURLException {
		final File dataDirectory = new File(workspaceURL.getFile(), childName);
		if(!dataDirectory.exists())
			Assert.assertTrue("Cannot create workspace sub-directory:  "
					+ childName, dataDirectory.mkdir());
		return dataDirectory.toURL();
	}

	/**
	 * Obtain the workspace url for the given operating system.
	 * @param os <code>OS</code>
	 * @return <code>java.net.URL</code>
	 */
	private File getWorkspaceDirectory(final OS os) {
		switch(os) {
		case WINDOWS_XP:
			return getWorkspaceDirectory_Win32();
		case LINUX:
			return getWorkspaceDirectory_Linux();
		}
		throw Assert
				.createNotYetImplemented("Windows xp is the only supported operating system.");
	}

	private File getWorkspaceDirectory_Linux() {
		final StringBuffer linuxWorkspaceURL =
			new StringBuffer(SystemUtil.getenv("HOME"))
				.append(File.separatorChar).append(".Parity Software")
				.append(File.separatorChar).append("Parity");
		final File linuxWorkspace = new File(linuxWorkspaceURL.toString());
		return linuxWorkspace;
	}

	/**
	 * Obtain the workspace directory's file for a win32 environment.
	 * @return <code>java.io.File</code>
	 * @throws MalformedURLException
	 */
	private File getWorkspaceDirectory_Win32() {
		// application data directory
		// TODO:  Move the corporation name and the product name to an external
		// configuration
		final StringBuffer win32WorkspaceURL = new StringBuffer(SystemUtil
				.getenv("APPDATA")).append(File.separatorChar).append(
				"Parity Software").append(File.separatorChar).append("Parity");
		final File win32Workspace = new File(win32WorkspaceURL.toString());
		return win32Workspace;
	}

	/**
	 * Obtain the workspace url. Will create the directory if it does not exist.
	 * 
	 * @return <code>java.net.URL</code>
	 */
	private URL getWorkspaceURL() {
		final File workspaceDirectory = getWorkspaceDirectory(OSUtil.getOS());
		if(!workspaceDirectory.exists())
			Assert.assertTrue("Cannot create workspace directory.",
					workspaceDirectory.mkdirs());
		try { return workspaceDirectory.toURL(); }
		catch(MalformedURLException murlx) { return null; }
	}

	/**
	 * Obtain the workspace for the parity model software. After initial
	 * creation the workspace is cached in order to facilitate subsequent
	 * requests.
	 * 
	 * @return <code>Workspace</code>
	 */
	Workspace getWorkspace() {
		if(null == WorkspaceModelImpl.cachedWorkspace) {
			WorkspaceModelImpl.cachedWorkspace = createWorkspace();
		}
		return WorkspaceModelImpl.cachedWorkspace;
	}
}
