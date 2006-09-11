/*
 * Feb 8, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.util;

import java.io.File;

import com.thinkparity.codebase.StackUtil;


import com.thinkparity.ophelia.model.Constants.DirectoryNames;
import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HypersonicValidator {

	/**
	 * The database directory.
	 * 
	 */
	private final File databaseDirectory;

	/**
	 * Create a HypersonicValidator.
	 */
	public HypersonicValidator(final Workspace workspace) {
		super();
		this.databaseDirectory = new File(
				workspace.getDataDirectory(),
                DirectoryNames.Workspace.Data.DB);
	}

	public void validate() throws HypersonicException {
		validateDatabaseDirectory();
		validateSession();
		new HypersonicMigrator().migrate();
	}

	private void validateDatabaseDirectory() {
		if(!databaseDirectory.exists())
			if(!databaseDirectory.mkdir())
				throw new HypersonicException(
						"Could not create database directory:  " +
						databaseDirectory.getAbsolutePath());
	}

	private void validateSession() {
		final Session session =
            SessionManager.openSession(StackUtil.getExecutionPoint());
		session.close();
	}
}
