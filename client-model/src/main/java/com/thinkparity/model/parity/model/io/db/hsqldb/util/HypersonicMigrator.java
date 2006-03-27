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
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.model.parity.model.io.db.hsqldb.Session;
import com.thinkparity.model.parity.model.io.db.hsqldb.SessionManager;
import com.thinkparity.model.parity.model.io.db.hsqldb.Table;
import com.thinkparity.model.parity.model.io.md.MetaDataType;
import com.thinkparity.model.parity.model.message.system.SystemMessageType;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class HypersonicMigrator {

	private static final Config CONFIG;

	private static final String[] CREATE_SCHEMA_SQL;

	private static final String INSERT_SEED_ARTIFACT_AUDIT_TYPE;

	private static final String INSERT_SEED_ARTIFACT_FLAG;

	private static final String INSERT_SEED_ARTIFACT_STATE;

	private static final String INSERT_SEED_ARTIFACT_TYPE;

	private static final String INSERT_SEED_META_DATA_TYPE;

	private static final String INSERT_SEED_SYSTEM_MESSAGE_TYPE;

	private static final String INSERT_SEED_VERSION;

	private static final String READ_META_DATA_VERSION;

	static {
		// sql statements
		CONFIG = ConfigFactory.newInstance(HypersonicMigrator.class);

		CREATE_SCHEMA_SQL = new String[] {
				CONFIG.getProperty("CreateMetaDataType"),
				CONFIG.getProperty("CreateMetaData"),
				CONFIG.getProperty("CreateArtifactState"),
				CONFIG.getProperty("CreateArtifactType"),
				CONFIG.getProperty("CreateArtifact"),
				CONFIG.getProperty("CreateArtifactFlag"),
				CONFIG.getProperty("CreateArtifactFlagRel"),
				CONFIG.getProperty("CreateArtifactRemoteInfo"),
				CONFIG.getProperty("CreateArtifactVersion"),
				CONFIG.getProperty("CreateArtifactVersionMetaData"),
				CONFIG.getProperty("CreateArtifactAuditType"),
				CONFIG.getProperty("CreateArtifactAudit"),
				CONFIG.getProperty("CreateArtifactAuditMetaData"),
				CONFIG.getProperty("CreateArtifactAuditVersion"),
				CONFIG.getProperty("CreateSystemMessageType"),
				CONFIG.getProperty("CreateSystemMessage"),
				CONFIG.getProperty("CreateSystemMessageMetaData"),
				CONFIG.getProperty("CreateDocument"),
				CONFIG.getProperty("CreateDocumentVersion")
		};

		INSERT_SEED_META_DATA_TYPE = CONFIG.getProperty("InsertSeedMetaDataType");
		
		INSERT_SEED_VERSION = CONFIG.getProperty("InsertSeedVersion");

		INSERT_SEED_ARTIFACT_FLAG = CONFIG.getProperty("InsertSeedArtifactFlag");

		INSERT_SEED_ARTIFACT_STATE = CONFIG.getProperty("InsertSeedArtifactState");

		INSERT_SEED_ARTIFACT_TYPE = CONFIG.getProperty("InsertSeedArtifactType");

		INSERT_SEED_ARTIFACT_AUDIT_TYPE = CONFIG.getProperty("InsertSeedArtifactAuditType");

		INSERT_SEED_SYSTEM_MESSAGE_TYPE = CONFIG.getProperty("InsertSeedSystemMessageType");

		READ_META_DATA_VERSION = CONFIG.getProperty("ReadMetaDataVersion");
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
		else {
			final Session session = SessionManager.openSession();
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

		session.prepareStatement(INSERT_SEED_ARTIFACT_FLAG);
		for(final ArtifactFlag af : ArtifactFlag.values()) {
			session.setInt(1, af.getId());
			session.setString(2, af.toString());
			if(1 != session.executeUpdate())
				throw new HypersonicException(
						"Could not insert artifact flag seed data:  " + af);
		}

		session.prepareStatement(INSERT_SEED_ARTIFACT_STATE);
		for(final ArtifactState as : ArtifactState.values()) {
			session.setInt(1, as.getId());
			session.setString(2, as.toString());
			if(1 != session.executeUpdate())
				throw new HypersonicException(
						"Could not insert artifact state seed data:  " + as);
		}

		session.prepareStatement(INSERT_SEED_ARTIFACT_TYPE);
		for(final ArtifactType at : ArtifactType.values()) {
			session.setInt(1, at.getId());
			session.setString(2, at.toString());
			if(1 != session.executeUpdate())
				throw new HypersonicException(
						"Could not insert artifact type seed data:  " + at);
		}

		session.prepareStatement(INSERT_SEED_ARTIFACT_AUDIT_TYPE);
		for(final AuditEventType aat : AuditEventType.values()) {
			session.setTypeAsInteger(1, aat);
			session.setTypeAsString(2, aat);
			if(1 != session.executeUpdate())
				throw new HypersonicException(
						"Could not insert artifact audit type seed data:  " + aat);
		}

		session.prepareStatement(INSERT_SEED_SYSTEM_MESSAGE_TYPE);
		for(final SystemMessageType smt : SystemMessageType.values()) {
			session.setTypeAsInteger(1, smt);
			session.setTypeAsString(2, smt);
			if(1 != session.executeUpdate())
				throw new HypersonicException(
						"Could not insert system message type seed data:  " + smt);
		}
	}

	private void migrateSchema(final String fromVersionId,
			final String toVersionId) {}
}
