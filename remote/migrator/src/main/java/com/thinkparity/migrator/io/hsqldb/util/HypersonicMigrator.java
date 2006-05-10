/*
 * Feb 8, 2006
 */
package com.thinkparity.migrator.io.hsqldb.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.LoggerFactory;
import com.thinkparity.migrator.Version;
import com.thinkparity.migrator.io.hsqldb.HypersonicException;
import com.thinkparity.migrator.io.hsqldb.HypersonicSession;
import com.thinkparity.migrator.io.hsqldb.HypersonicSessionManager;
import com.thinkparity.migrator.io.hsqldb.Table;
import com.thinkparity.migrator.io.md.MetaDataType;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class HypersonicMigrator {

	private static final Config CONFIG;

	private static final String[] CREATE_SCHEMA_SQL;

	private static final String INSERT_SEED_LIBRARY_TYPE;

    private static final String INSERT_SEED_META_DATA_TYPE;

    private static final String INSERT_SEED_VERSION;

	private static final String READ_META_DATA_VERSION;

	static {
		// sql statements
		CONFIG = ConfigFactory.newInstance("io/hsqldb/hypersonicMigrator.properties");

		CREATE_SCHEMA_SQL = new String[] {
                CONFIG.getProperty("CreateMetaDataType"),
                CONFIG.getProperty("CreateMetaData"),
                CONFIG.getProperty("CreateLibraryType"),
				CONFIG.getProperty("CreateLibrary"),
				CONFIG.getProperty("CreateLibraryBytes"),
                CONFIG.getProperty("CreateRelease"),
                CONFIG.getProperty("CreateReleaseLibraryRel")
		};

		INSERT_SEED_LIBRARY_TYPE = CONFIG.getProperty("InsertSeedLibraryType");

        INSERT_SEED_META_DATA_TYPE = CONFIG.getProperty("InsertSeedMetaDataType");
		
		INSERT_SEED_VERSION = CONFIG.getProperty("InsertSeedVersion");

		READ_META_DATA_VERSION = CONFIG.getProperty("ReadMetaDataVersion");
	}

	protected final Logger logger;

	/**
	 * Create a HypersonicMigrator.
	 * 
	 */
	HypersonicMigrator() {
		super();
		this.logger = LoggerFactory.getLogger(getClass());
	}

	void migrate() throws HypersonicException {
		final String actualVersionId = Version.getBuildId();
		final String expectedVersionId = getExpectedVersionId();
		if(null == expectedVersionId) { initializeSchema(); }
		else if(actualVersionId.equals(expectedVersionId)) {
			migrateSchema(expectedVersionId, actualVersionId);
		}
	}

	private void createSchema(final HypersonicSession session) {
		for(final String sql : CREATE_SCHEMA_SQL) {
			session.execute(sql);
		}
	}

	private String getExpectedVersionId() {
		final List<Table> tables = HypersonicSessionManager.listTables();
		Boolean isTableFound = Boolean.FALSE;
		for(final Table t : tables) {
			if(t.getName().equals("META_DATA")) {
				isTableFound = Boolean.TRUE;
				break;
			}
		}
		if(!isTableFound) { return null; }
		else {
			final HypersonicSession session = HypersonicSessionManager.openSession();
			try {
				session.prepareStatement(READ_META_DATA_VERSION);
				session.setLong(1, 1000L);
				session.setTypeAsInteger(2, MetaDataType.STRING);
				session.setString(3, "VERSION");
				session.executeQuery();
				session.nextResult();
				return session.getString("VALUE");
			}
			finally { session.close(); }
		}
	}

	/**
	 * Initialize the parity model schema.
	 * 
	 */
	private void initializeSchema() {
		final HypersonicSession session = HypersonicSessionManager.openSession();
		try {
			createSchema(session);
			insertSeedData(session);
			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			logger.fatal("Could not create parity schema.", hx);
			throw hx;
		}
		finally { session.close(); }
	}

	private void insertSeedData(final HypersonicSession session) {
		session.prepareStatement(INSERT_SEED_META_DATA_TYPE);
		for(final MetaDataType mdt : MetaDataType.values()) {
			session.setTypeAsInteger(1, mdt);
			session.setTypeAsString(2, mdt);
			if(1 != session.executeUpdate())
				throw new HypersonicException(
						"Could not insert meta data seed data:  " + mdt);
		}

		session.prepareStatement(INSERT_SEED_VERSION);
		session.setTypeAsInteger(1, MetaDataType.STRING);
		session.setString(2, "VERSION");
		session.setString(3, Version.getBuildId());
		if(1 != session.executeUpdate())
			throw new HypersonicException(
					"Could not insert version seed.");

        session.prepareStatement(INSERT_SEED_LIBRARY_TYPE);
        for(final Library.Type t : Library.Type.values()) {
            session.setTypeAsInteger(1, t);
            session.setTypeAsString(2, t);
            if(1 != session.executeUpdate())
                throw new HypersonicException(
                        "[RMIGRATOR] [IO] [INSERT SEED DATA] [LIBRARY TYPE]");
        }
	}

	private void migrateSchema(final String fromVersionId,
			final String toVersionId) {}
}
