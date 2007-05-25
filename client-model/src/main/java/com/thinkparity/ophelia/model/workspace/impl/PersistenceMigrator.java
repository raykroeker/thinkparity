/*
 * Created On: Feb 8, 2006
 */
package com.thinkparity.ophelia.model.workspace.impl;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;

import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.artifact.ArtifactType;

import com.thinkparity.ophelia.model.Constants;
import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;
import com.thinkparity.ophelia.model.io.md.MetaDataType;

import org.apache.log4j.Logger;

/**
 * <b>Title:</b>thinkParity Workspace Persistence Migrator<br>
 * <b>Description:</b>Migrates the workspace persistence schema/data from one
 * version to the next.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PersistenceMigrator {

    private static final Config CONFIG;

    private static final String[] CREATE_SCHEMA_SQL;

    private static final String INSERT_SEED_ARTIFACT_STATE;

    private static final String INSERT_SEED_ARTIFACT_TYPE;

    private static final String INSERT_SEED_META_DATA_TYPE;

    private static final String INSERT_SEED_VERSION;

    private static final String READ_META_DATA_RELEASE_ID;

    static {
        // sql statements
        CONFIG = ConfigFactory.newInstance("database/create.properties");

        CREATE_SCHEMA_SQL = new String[] {
                CONFIG.getProperty("CreateMetaDataType"),
                CONFIG.getProperty("CreateMetaData"),
                CONFIG.getProperty("CreateEmail"),
                CONFIG.getProperty("CreateUser"),
                CONFIG.getProperty("CreateProfile"),
                CONFIG.getProperty("CreateProfileEmailRel"),
                CONFIG.getProperty("CreateProfileFeature"),
                CONFIG.getProperty("CreateContact"),
                CONFIG.getProperty("CreateContactEmailRel"),
                CONFIG.getProperty("CreateContactInvitation"),
                CONFIG.getProperty("CreateContactInvitationIncomingEMail"),
                CONFIG.getProperty("CreateContactInvitationIncomingUser"),
                CONFIG.getProperty("CreateContactInvitationOutgoingEMail"),
                CONFIG.getProperty("CreateContactInvitationOutgoingUser"),
                CONFIG.getProperty("CreateArtifactState"),
                CONFIG.getProperty("CreateArtifactType"),
                CONFIG.getProperty("CreateArtifact"),
                CONFIG.getProperty("CreateArtifactTeamRel"),
                CONFIG.getProperty("CreateArtifactVersion"),
                CONFIG.getProperty("CreateArtifactVersionMetaData"),
                CONFIG.getProperty("CreateConfiguration"),
                CONFIG.getProperty("CreateContainer"),
                CONFIG.getProperty("CreateContainerVersion"),
                CONFIG.getProperty("CreateContainerVersionArtifactVersionRel"),
                CONFIG.getProperty("CreateContainerVersionDelta"),
                CONFIG.getProperty("CreateContainerVersionArtifactVersionDelta"),
                CONFIG.getProperty("CreateContainerVersionPublishedTo"),
                CONFIG.getProperty("CreateContainerVersionPublishedToInvitation"),
                CONFIG.getProperty("CreateContainerDraft"),
                CONFIG.getProperty("CreateContainerDraftArtifactRel"),
                CONFIG.getProperty("CreateDocument"),
                CONFIG.getProperty("CreateDocumentVersion"),
                CONFIG.getProperty("CreateContainerDraftDocument"),
                CONFIG.getProperty("CreateMigrator"),
                CONFIG.getProperty("CreateIndexUserName"),
                CONFIG.getProperty("CreateIndexUserOrganization"),
                CONFIG.getProperty("CreateIndexEmail")
        };

        INSERT_SEED_META_DATA_TYPE = CONFIG.getProperty("InsertSeedMetaDataType");
        
        INSERT_SEED_VERSION = CONFIG.getProperty("InsertSeedVersion");

        INSERT_SEED_ARTIFACT_STATE = CONFIG.getProperty("InsertSeedArtifactState");

        INSERT_SEED_ARTIFACT_TYPE = CONFIG.getProperty("InsertSeedArtifactType");

        READ_META_DATA_RELEASE_ID = CONFIG.getProperty("ReadMetaDataReleaseId");
    }

    protected final Logger logger;

    /** A database session manager. */
    private final SessionManager sessionManager;

    /**
     * Create PersistenceMigrator.
     * 
     * @param sessionManager
     *            A <code>SessionManager</code>.
     */
    PersistenceMigrator(final SessionManager sessionManager) {
        super();
        this.logger = Logger.getLogger(getClass());
        this.sessionManager = sessionManager;
    }

    void migrate() throws HypersonicException {
        final String schemaVersionId = getSchemaVersionId();
        if (null == schemaVersionId) {
            initializeSchema();
        } else {
            final String softwareVersionId = "";
            migrateSchema(schemaVersionId, softwareVersionId);
        }
    }

    private void createSchema(final Session session) {
        for(final String sql : CREATE_SCHEMA_SQL) {
            session.execute(sql);
        }
    }

    private String getSchemaVersionId() {
        final Session session = openSession();
        session.openMetaData();
        try {
            session.getMetaDataTables();
            boolean found = false;
            while (session.nextResult()) {
                if (session.getString("TABLE_NAME").equals("META_DATA")) {
                    found = true;
                    break;
                }
            }
            if (found) {
                session.prepareStatement(READ_META_DATA_RELEASE_ID);
                session.setLong(1, Constants.MetaData.RELEASE_ID_PK);
                session.setTypeAsInteger(2, MetaDataType.STRING);
                session.setString(3, Constants.MetaData.RELEASE_ID_KEY);
                session.executeQuery();
                if (session.nextResult()) {
                    return session.getString("META_DATA_VALUE");
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * Initialize the parity model schema.
     * 
     */
    private void initializeSchema() {
        final Session session = openSession();
        try {
            createSchema(session);
            insertSeedData(session);
        } finally {
            session.close();
        }
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
        session.setString(2, Constants.MetaData.RELEASE_ID_KEY);
        session.setString(3, Constants.Release.NAME);
        if(1 != session.executeUpdate())
            throw new HypersonicException(
                    "Could not insert version seed.");

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
    }

    private void migrateSchema(final String fromVersionId,
            final String toVersionId) {}

    /**
     * Open the hypersonic session.
     * 
     * @return The hypersonic session.
     */
    private Session openSession() {
        return sessionManager.openSession(StackUtil.getCaller());
    }
}
