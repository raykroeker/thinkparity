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
     * Validate the remote migrator's hypersonic schema.
     * 
     * @throws HypersonicException
     */
	public static void validate() throws HypersonicException {
        new HypersonicValidator().doValidate();
    }

    /** The database directory. */
	private final File dbDirectory;

	/** The database file. */
    private final File dbFile;

	/**
	 * Create a HypersonicValidator.
	 */
	private HypersonicValidator() {
		super();
        this.dbFile = new File(System.getProperty("hsqldb.file"));
		this.dbDirectory = dbFile.getParentFile();
	}

    private void doValidate() {
		validateDatabaseDirectory();
		validateSession();
		new HypersonicMigrator().migrate();
	}

	private void validateDatabaseDirectory() {
		if(!dbDirectory.exists())
			if(!dbDirectory.mkdir())
				throw new HypersonicException("[RMIGRATOR] [IO] [HYPERSONIC VALIDATOR] [VALIDATE DB DIRECTORY] [CANNOT CREATE DATABASE DIRECTORY]");
	}

	private void validateSession() {
		final HypersonicSession session = HypersonicSessionManager.openSession();
		session.close();
	}
}
