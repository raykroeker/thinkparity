/*
 * Feb 8, 2006
 */
package com.thinkparity.model.parity.model.io.db.hsqldb.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;

import com.thinkparity.model.Version;
import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.io.db.HypersonicException;
import com.thinkparity.model.parity.model.io.db.hsqldb.Session;
import com.thinkparity.model.parity.model.io.db.hsqldb.SessionManager;
import com.thinkparity.model.parity.model.io.db.hsqldb.Table;
import com.thinkparity.model.parity.model.io.db.hsqldb.SessionManager.Property;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class HypersonicMigrator {

	private static final Config CONFIG;

	private static final String[] CREATE_SCHEMA_SQL;

	private static final String INSERT_SEED_ARTIFACT_FLAG;

	private static final String INSERT_SEED_ARTIFACT_TYPE;

	static {
		// sql statements
		CONFIG = ConfigFactory.newInstance(HypersonicMigrator.class);

		CREATE_SCHEMA_SQL = new String[] {
				CONFIG.getProperty("CreateArtifactType"),
				CONFIG.getProperty("CreateArtifactFlag"),
				CONFIG.getProperty("CreateArtifact"),
				CONFIG.getProperty("CreateArtifactFlagRel"),
				CONFIG.getProperty("CreateArtifactVersion"),
				CONFIG.getProperty("CreateArtifactVersionMetaData"),
				CONFIG.getProperty("CreateDocument"),
				CONFIG.getProperty("CreateDocumentVersion"),
				CONFIG.getProperty("CreateMetaData")
		};
		
		INSERT_SEED_ARTIFACT_FLAG = CONFIG.getProperty("InsertSeedArtifactFlag");

		INSERT_SEED_ARTIFACT_TYPE = CONFIG.getProperty("InsertSeedArtifactType");
	}

	protected final Logger logger;

	/**
	 * Create a HypersonicMigrator.
	 * 
	 */
	HypersonicMigrator() {
		super();
		this.logger = ModelLoggerFactory.getLogger(getClass());
	}

	void migrate() throws HypersonicException {
		final String actualVersionId = Version.getBuildId();
		final String expectedVersionId = getExpectedVersionId();
		if(null == expectedVersionId) { initializeSchema(); }
		else if(actualVersionId.equals(expectedVersionId)) {
			migrateSchema(expectedVersionId, actualVersionId);
		}
	}

	private void createSchema(final Session session) {
		for(final String sql : CREATE_SCHEMA_SQL) {
			session.execute(sql);
		}
	}

	private String getExpectedVersionId() {
		final List<Table> tables = SessionManager.listTables();
		Boolean isTableFound = Boolean.FALSE;
		for(final Table t : tables) {
			if(t.getName().equals("META_DATA")) {
				isTableFound = Boolean.TRUE;
				break;
			}
		}
		if(!isTableFound) { return null; }
		else { return SessionManager.getProperty(Property.VERSION_ID); }
	}

	/**
	 * Initialize the parity model schema.
	 * 
	 */
	private void initializeSchema() {
		final Session session = SessionManager.openSession();
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

	private void insertSeedData(final Session session) {
		SessionManager.setProperty(Property.VERSION_ID, Version.getBuildId());

		session.prepareStatement(INSERT_SEED_ARTIFACT_FLAG);
		for(final ArtifactFlag af : ArtifactFlag.values()) {
			session.setInt(1, af.getId());
			session.setString(2, af.toString());
			if(1 != session.executeUpdate())
				throw new HypersonicException(
						"Could not insert artifact flag seed data:  " + af);
		}

		session.prepareStatement(INSERT_SEED_ARTIFACT_TYPE);
		for(final ArtifactType at : ArtifactType.values()) {
			session.setInt(1, at.getId());
			session.setString(2, at.toString());
			if(1 != session.executeUpdate())
				throw new HypersonicException(
						"Could not insert artifact type seed data:  " + at);
		}
	}

	private void migrateSchema(final String fromVersionId,
			final String toVersionId) {}
}
