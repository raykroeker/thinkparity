/*
 * Created On: Oct 17, 2005
 * $Id$
 */
package com.thinkparity.model.parity.model.workspace;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.Constants.Directories;
import com.thinkparity.model.Constants.DirectoryNames;
import com.thinkparity.model.Constants.Files;

/**
 * 
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
class WorkspaceHelper {

    /** The workspace root directory. */
    private final File workspaceDirectory;

    /** The workspace preferences helper. */
    private final PreferencesHelper workspacePreferencesHelper;

    /** The workspace temp directory. */
    private final File workspaceTempDirectory;

    /** Create WorkspaceHelper. */
	WorkspaceHelper() {
        super();
        this.workspaceDirectory = initRoot();
        this.workspacePreferencesHelper = new PreferencesHelper(workspaceDirectory);
        this.workspaceTempDirectory =
            new File(workspaceDirectory, DirectoryNames.Workspace.TEMP);
	}

    /**
     * Delete the workspace temp directory.
     *
     */
    void deleteTempDirectory() {
        if(workspaceTempDirectory.exists())
            FileUtil.deleteTree(workspaceTempDirectory);
    }

    /**
	 * Open the parity workspace.
	 * 
	 * @return An interface with which the client can interact with the
	 *         workspace.
	 */
	Workspace openWorkspace() {
	    // data directory
	    final File dataDirectory = initChild(workspaceDirectory, DirectoryNames.Workspace.DATA);
        // index directory
        final File indexDirectory = initChild(workspaceDirectory, DirectoryNames.Workspace.INDEX);
        // workspace
		final Workspace workspace = new Workspace() {
            public File createTempFile() throws IOException {
                initChild(workspaceDirectory, DirectoryNames.Workspace.TEMP);
                final StackTraceElement caller = StackUtil.getCaller();
                final String tempFileSuffix = new StringBuffer(".")
                    .append(StringUtil.searchAndReplace(caller.getClassName(), ".", "_"))
                    .append("#").append(caller.getMethodName())
                    .toString();
                return File.createTempFile(Files.TEMP_FILE_PREFIX,
                        tempFileSuffix, workspaceTempDirectory);
            }

			/**
             * @see com.thinkparity.model.parity.model.workspace.Workspace#createTempFile(com.thinkparity.model.parity.model.document.Document)
             */
            public File createTempFile(final String suffix) throws IOException {
                initChild(workspaceDirectory, DirectoryNames.Workspace.TEMP);
                return File.createTempFile(Files.TEMP_FILE_PREFIX, suffix,
                        workspaceTempDirectory);
            }

            /**
			 * @see com.thinkparity.model.parity.model.workspace.Workspace#getDataDirectory()
			 * 
			 */
			public File getDataDirectory() { return dataDirectory; }

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Workspace#getIndexDirectory()
			 * 
			 */
			public File getIndexDirectory() { return indexDirectory; }

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Workspace#getPreferences()
			 */
			public Preferences getPreferences() {
                return workspacePreferencesHelper.getPreferences();
			}

			/**
             * @see com.thinkparity.model.parity.model.workspace.Workspace#getWorkspaceDirectory()
             */
            public File getWorkspaceDirectory() { return workspaceDirectory; }
		};
		deleteTempDirectory();
        return workspace;
	}

    /**
     * Save the workspace preferences.
     *
     */
	void savePreferences() {
        workspacePreferencesHelper.savePreferences();
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
                    MessageFormat.format("[CANNOT CREATE WORKSPACE CHILD {0}]", child),
					childFile.mkdir());
		return childFile;
	}

	/**
	 * Initialize the workspace root. This will obtain a handle to the workspace
	 * root directory, and create it if it does not already exist; then convert
	 * it to a URL.
	 * 
	 * @return The workspace directory.
	 */
	private File initRoot() {
		if(!Directories.WORKSPACE.exists())
			Assert.assertTrue("[WORKSPACE ROOT CANNOT BE CREATED]",
					Directories.WORKSPACE.mkdirs());
		return Directories.WORKSPACE;
	}
}
