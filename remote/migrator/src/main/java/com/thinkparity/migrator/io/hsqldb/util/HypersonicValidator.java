/*
 * Feb 8, 2006
 */
package com.thinkparity.migrator.io.hsqldb.util;

import java.io.File;

import com.thinkparity.migrator.io.hsqldb.HypersonicException;
import com.thinkparity.migrator.io.hsqldb.HypersonicSession;
import com.thinkparity.migrator.io.hsqldb.HypersonicSessionManager;

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
	public HypersonicValidator() {
		super();
		this.databaseDirectory = new File("C:\\", "db");
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
		final HypersonicSession session = HypersonicSessionManager.openSession();
		session.close();
	}
}
