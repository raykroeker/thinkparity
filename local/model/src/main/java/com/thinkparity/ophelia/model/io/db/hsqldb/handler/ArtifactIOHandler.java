/*
 * Feb 9, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import java.util.*;

import javax.sql.DataSource;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.artifact.ArtifactVersionFlag;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public final class ArtifactIOHandler extends AbstractIOHandler implements
		com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler {

    /** Sql query to delete the artifact. */
	private static final String DELETE_FROM_ARTIFACT =
		new StringBuffer("delete from ARTIFACT ")
		.append("where ARTIFACT_ID=?")
		.toString();

    /**
	 * Sql to delete the artifact version.
	 * 
	 */
	private static final String DELETE_FROM_ARTIFACT_VERSION = 
		new StringBuffer("delete from ARTIFACT_VERSION ")
		.append("where ARTIFACT_ID=? and ARTIFACT_VERSION_ID=?")
		.toString();

    /**
	 * Sql to delete the artifact version meta data.
	 * 
	 */
	private static final String DELETE_FROM_ARTIFACT_VERSION_META_DATA =
		new StringBuffer("delete from ARTIFACT_VERSION_META_DATA ")
		.append("where ARTIFACT_ID=? and ARTIFACT_VERSION_ID=?")
		.toString();

    /**
     * Sql to read the earliest version id.
     */
    private static final String GET_EARLIEST_VERSION_ID =
        new StringBuffer("select MIN(ARTIFACT_VERSION_ID) EARLIEST_VERSION_ID ")
        .append("from ARTIFACT_VERSION ")
        .append("where ARTIFACT_ID=?")
        .toString();

    /**
     * Sql to read the latest version id.
     */
    private static final String GET_LATEST_VERSION_ID =
		new StringBuffer("select MAX(ARTIFACT_VERSION_ID) LATEST_VERSION_ID ")
		.append("from ARTIFACT_VERSION ")
		.append("where ARTIFACT_ID=?")
		.toString();

    /**
	 * Sql query used to create an artifact version.
	 * 
	 */
	private static final String INSERT_ARTIFACT_VERSION =
		new StringBuffer("insert into ARTIFACT_VERSION ")
		.append("(ARTIFACT_ID,ARTIFACT_VERSION_ID,ARTIFACT_NAME,ARTIFACT_TYPE,")
		.append("ARTIFACT_UNIQUE_ID,NAME,COMMENT,CREATED_BY,CREATED_ON,UPDATED_BY,")
		.append("UPDATED_ON,FLAGS) ")
		.append("values (?,?,?,?,?,?,?,?,?,?,?,?)")
		.toString();

    private static final String INSERT_ARTIFACT_VERSION_META_DATA =
		new StringBuffer("insert into ARTIFACT_VERSION_META_DATA ")
		.append("(ARTIFACT_ID,ARTIFACT_VERSION_ID,META_DATA_KEY,META_DATA_VALUE) ")
		.append("values (?,?,?,?)")
		.toString();

    /**
	 * Sql to extract the artifact version meta data.
	 * 
	 */
	private static final String SELECT_VERSION_META_DATA_BY_ARTIFACT_ID_BY_VERSION_ID =
		new StringBuffer("select META_DATA_KEY,META_DATA_VALUE ")
		.append("from ARTIFACT_VERSION_META_DATA ")
		.append("where ARTIFACT_ID=? and ARTIFACT_VERSION_ID=?")
		.toString();

    /** Sql to create an artifact. */
    private static final String SQL_CREATE =
        new StringBuffer("insert into ARTIFACT ")
        .append("(ARTIFACT_NAME,ARTIFACT_STATE_ID,ARTIFACT_TYPE_ID,")
        .append("ARTIFACT_UNIQUE_ID,CREATED_BY,CREATED_ON,UPDATED_BY,")
        .append("UPDATED_ON,FLAGS) ")
        .append("values (?,?,?,?,?,?,?,?,?)")
        .toString();

    /** Sql to create a team member relationship. */
    private static final String SQL_CREATE_TEAM_REL =
        new StringBuffer("insert into ARTIFACT_TEAM_REL ")
        .append("(ARTIFACT_ID,USER_ID) ")
        .append("values (?,?)")
        .toString();

	/** Sql to delete a team member relationship. */
    private static final String SQL_DELETE_TEAM_REL_BY_ARTIFACT =
        new StringBuffer("delete from ARTIFACT_TEAM_REL ")
        .append("where ARTIFACT_ID=?")
        .toString();

    /** Sql to delete a team member relationship. */
    private static final String SQL_DELETE_TEAM_REL_BY_ARTIFACT_BY_USER =
        new StringBuffer("delete from ARTIFACT_TEAM_REL ")
        .append("where ARTIFACT_ID=? and USER_ID=?")
        .toString();

    /** Sql to determine if any artifact version exists. */
    private static final String SQL_DOES_ANY_VERSION_EXIST =
            new StringBuffer("select COUNT(*) \"COUNT\" ")
            .append("from ARTIFACT A ")
            .append("inner join ARTIFACT_VERSION AV on A.ARTIFACT_ID=AV.ARTIFACT_ID ")
            .append("where AV.ARTIFACT_ID=? and ARTIFACT_VERSION_ID>=1")
            .toString();

	/** Sql to determine existance by id. */
    private static final String SQL_DOES_EXIST_BY_ID =
        new StringBuffer("select COUNT(ARTIFACT_ID) \"COUNT\" ")
        .append("from ARTIFACT A ")
        .append("where A.ARTIFACT_ID=?")
        .toString();

	/** Sql to determine existance by id. */
    private static final String SQL_DOES_EXIST_BY_UNIQUE_ID =
        new StringBuffer("select COUNT(ARTIFACT_UNIQUE_ID) \"COUNT\" ")
        .append("from ARTIFACT A ")
        .append("where A.ARTIFACT_UNIQUE_ID=?")
        .toString();

	/** Sql to determine if an artifact version exists. */
    private static final String SQL_DOES_VERSION_EXIST =
            new StringBuffer("select COUNT(*) \"COUNT\" ")
            .append("from ARTIFACT A ")
            .append("inner join ARTIFACT_VERSION AV on A.ARTIFACT_ID=AV.ARTIFACT_ID ")
            .append("where AV.ARTIFACT_ID=? and ARTIFACT_VERSION_ID=?")
            .toString();

	/** Sql to read the earliest version id. */
    private static final String SQL_READ_EARLIEST_VERSION_ID =
            new StringBuffer("select min(ARTIFACT_VERSION_ID) EARLIEST_VERSION_ID ")
            .append("from ARTIFACT A ")
            .append("inner join ARTIFACT_VERSION AV on A.ARTIFACT_ID=AV.ARTIFACT_ID ")
            .append("where A.ARTIFACT_ID=?")
            .toString();

    /** Sql to read flags. */
	private static final String SQL_READ_FLAGS =
		new StringBuilder("select A.FLAGS ")
		.append("from ARTIFACT A ")
		.append("where A.ARTIFACT_ID=?")
		.toString();

    private static final String SQL_READ_ID =
        new StringBuffer("select A.ARTIFACT_ID ")
        .append("from ARTIFACT A ")
        .append("where A.ARTIFACT_UNIQUE_ID=?")
        .toString();

    /** Sql to read the latest version id. */
    private static final String SQL_READ_LATEST_VERSION_ID =
            new StringBuffer("select max(ARTIFACT_VERSION_ID) LATEST_VERSION_ID ")
            .append("from ARTIFACT A ")
            .append("inner join ARTIFACT_VERSION AV on A.ARTIFACT_ID=AV.ARTIFACT_ID ")
            .append("where A.ARTIFACT_ID=?")
            .toString();

    /** Sql to read the next version id. */
    private static final String SQL_READ_NEXT_VERSION_ID =
            new StringBuffer("select ARTIFACT_VERSION_ID NEXT_VERSION_ID ")
            .append("from ARTIFACT A ")
            .append("inner join ARTIFACT_VERSION AV on A.ARTIFACT_ID=AV.ARTIFACT_ID ")
            .append("where A.ARTIFACT_ID=? and AV.ARTIFACT_VERSION_ID>? ")
            .append("order by AV.ARTIFACT_VERSION_ID asc")
            .toString();

	/** Sql to read the previous version id. */
    private static final String SQL_READ_PREVIOUS_VERSION_ID =
            new StringBuffer("select ARTIFACT_VERSION_ID PREVIOUS_VERSION_ID ")
            .append("from ARTIFACT A ")
            .append("inner join ARTIFACT_VERSION AV on A.ARTIFACT_ID=AV.ARTIFACT_ID ")
            .append("where A.ARTIFACT_ID=? and AV.ARTIFACT_VERSION_ID<? ")
            .append("order by AV.ARTIFACT_VERSION_ID desc")
            .toString();

    /** Sql to read the artifact state. */
    private static final String SQL_READ_STATE =
            new StringBuffer("select A.ARTIFACT_STATE_ID ")
            .append("from ARTIFACT A ")
            .append("where A.ARTIFACT_ID=?")
            .toString();
    
    /**
     * Sql to read the team relationship.
     * 
     * @see ArtifactIOHandler#SQL_READ_TEAM_REL_BY_ARTIFACT_BY_USER
     */
    private static final String SQL_READ_TEAM_REL =
        new StringBuffer("select U.NAME,U.JABBER_ID,")
        .append("U.USER_ID,U.ORGANIZATION,U.TITLE,U.FLAGS,ATR.ARTIFACT_ID ")
        .append("from ARTIFACT_TEAM_REL ATR ")
        .append("inner join PARITY_USER U on ATR.USER_ID = U.USER_ID ")
        .append("where ATR.ARTIFACT_ID=? ")
        .append("order by U.JABBER_ID asc")
        .toString();
    
    /**
     * Sql to read the team relationship.
     * 
     * @see ArtifactIOHandler#SQL_READ_TEAM_REL
     */
    private static final String SQL_READ_TEAM_REL_BY_ARTIFACT_BY_USER =
		new StringBuffer("select U.NAME,U.JABBER_ID,")
        .append("U.USER_ID,U.ORGANIZATION,U.TITLE,U.FLAGS,ATR.ARTIFACT_ID ")
        .append("from ARTIFACT_TEAM_REL ATR ")
        .append("inner join PARITY_USER U on ATR.USER_ID = U.USER_ID ")
        .append("where ATR.ARTIFACT_ID=? ")
        .append("and ATR.USER_ID=?")
        .append("order by U.JABBER_ID asc")
        .toString();
    
    /** Sql to count the team relationship rows for an artifact. */
    private static final String SQL_READ_TEAM_REL_COUNT =
            new StringBuffer("select count(USER_ID) TEAM_SIZE ")
            .append("from ARTIFACT_TEAM_REL ")
            .append("where ARTIFACT_ID=?")
            .toString();
    
	/** Sql to read the artifact type. */
    private static final String SQL_READ_TYPE =
            new StringBuffer("select A.ARTIFACT_TYPE_ID ")
            .append("from ARTIFACT A ")
            .append("where A.ARTIFACT_ID=?")
            .toString();

    /** Sql to read an artifact unique id. */
    private static final String SQL_READ_UNIQUE_ID =
            new StringBuffer("select A.ARTIFACT_UNIQUE_ID ")
            .append("from ARTIFACT A ")
            .append("Where A.ARTIFACT_ID=?")
            .toString();

    /** Sql to read artifact version flags. */
    private static final String SQL_READ_VERSION_FLAGS =
        new StringBuilder("select FLAGS ")
        .append("from ARTIFACT_VERSION AV ")
        .append("where AV.ARTIFACT_ID=? and AV.ARTIFACT_VERSION_ID=?")
        .toString();

    /** Sql to read all artifact version seen flags count. */
    private static final String SQL_READ_VERSION_SEEN_FLAG_COUNT =
        new StringBuilder("select count(ARTIFACT_ID) \"SEEN_COUNT\" ")
        .append("from ARTIFACT_VERSION AV ")
        .append("where AV.FLAGS>=?")
        .toString();

    /** Sql to restore an artifact. */
    private static final String SQL_RESTORE =
        new StringBuffer("insert into ARTIFACT ")
        .append("(ARTIFACT_NAME,ARTIFACT_STATE_ID,ARTIFACT_TYPE_ID,")
        .append("ARTIFACT_UNIQUE_ID,CREATED_BY,CREATED_ON,UPDATED_BY,")
        .append("UPDATED_ON) ")
        .append("values (?,?,?,?,?,?,?,?)")
        .toString();

    /** Update the artifact flags. */
    private static final String SQL_UPDATE_FLAGS =
        new StringBuilder("update ARTIFACT ")
        .append("set FLAGS=? where ARTIFACT_ID=?")
        .toString();

    private static final String SQL_UPDATE_STATE =
		new StringBuffer("update ARTIFACT set ARTIFACT_STATE_ID=?,")
		.append("UPDATED_ON=CURRENT_TIMESTAMP ")
		.append("where ARTIFACT_ID=?")
		.toString();

    /** Update the artifact version flags. */
    private static final String SQL_UPDATE_VERSION_FLAGS =
        new StringBuilder("update ARTIFACT_VERSION ")
        .append("set FLAGS=? where ARTIFACT_ID=? and ARTIFACT_VERSION_ID=?")
        .toString();

	/** The user io interface. */
    private final UserIOHandler userIO;

	/**
     * Create ArtifactIOHandler.
     * 
     * @param dataSource
     *            An sql <code>DataSource</code>.
     */
	public ArtifactIOHandler(final DataSource dataSource) {
        super(dataSource);
        this.userIO = new UserIOHandler(dataSource);
	}

	/**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#createTeamRel(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public void createTeamRel(final Long artifactId, final Long userId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_TEAM_REL);
            session.setLong(1, artifactId);
            session.setLong(2, userId);
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not create team relationship.");
        } finally {
            session.close();
        }
    }

	/**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#deleteTeamRel(java.lang.Long)
     * 
     */
    public void deleteTeamRel(final Long artifactId) throws HypersonicException {
        final Session session = openSession();
        try {
            final Integer rowCount = readTeamRelCount(session, artifactId);

            session.prepareStatement(SQL_DELETE_TEAM_REL_BY_ARTIFACT);
            session.setLong(1, artifactId);
            if(rowCount != session.executeUpdate())
                throw new HypersonicException("[LMODEL] [ARTIFACT] [IO] [DELETE TEAM REL]");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#deleteTeamRel(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public void deleteTeamRel(final Long artifactId, final Long userId)
            throws HypersonicException {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_TEAM_REL_BY_ARTIFACT_BY_USER);
            session.setLong(1, artifactId);
            session.setLong(2, userId);
            if(1 != session.executeUpdate())
                throw new HypersonicException("[LMODEL] [ARTIFACT] [IO] [DELETE TEAM REL]");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#doesExist(java.lang.Long)
     *
     */
    public Boolean doesExist(final Long artifactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_BY_ID);
            session.setLong(1, artifactId);
            session.executeQuery();
            session.nextResult();
            if (0 == session.getInteger("COUNT")) {
                return Boolean.FALSE;
            } else if (1 == session.getInteger("COUNT")) {
                return Boolean.TRUE;
            } else {
                throw new HypersonicException("Could not determine artifact existance.");
            }
        } finally {
            session.close();
        }
    }

    
	/**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#doesExist(java.util.UUID)
     *
     */
    public Boolean doesExist(final UUID uniqueId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_BY_UNIQUE_ID);
            session.setUniqueId(1, uniqueId);
            session.executeQuery();
            session.nextResult();
            if (0 == session.getInteger("COUNT")) {
                return Boolean.FALSE;
            } else if (1 == session.getInteger("COUNT")) {
                return Boolean.TRUE;
            } else {
                throw new HypersonicException("Could not determine artifact existance.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#doesVersionExist(java.lang.Long)
     *
     */
    public Boolean doesVersionExist(final Long artifactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DOES_ANY_VERSION_EXIST);
            session.setLong(1, artifactId);
            session.executeQuery();

            session.nextResult();
            if (0 == session.getInteger("COUNT")) {
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#doesVersionExist(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public Boolean doesVersionExist(final Long artifactId, final Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DOES_VERSION_EXIST);
            session.setLong(1, artifactId);
            session.setLong(2, versionId);
            session.executeQuery();

            session.nextResult();
            if (0 == session.getInteger("COUNT")) {
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#isVersionSeenFlagApplied(com.thinkparity.codebase.model.artifact.Artifact)
     *
     */
    public Boolean isVersionSeenFlagApplied(final Artifact artifact) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_VERSION_SEEN_FLAG_COUNT);
            session.setInt(1, ArtifactVersionFlag.SEEN.getId());
            session.executeQuery();
            if (session.nextResult()) {
                final int seenCount = session.getInteger("SEEN_COUNT");
                if (0 == seenCount) {
                    return Boolean.FALSE;
                } else if (1 < seenCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine seen flag application.");
                }
            } else {
                throw new HypersonicException("Could not determine seen flag application.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#readLatestVersionId(java.lang.Long)
     */
    public Long readEarliestVersionId(final Long artifactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_EARLIEST_VERSION_ID);
            session.setLong(1, artifactId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getLong("EARLIEST_VERSION_ID");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#readFlags(com.thinkparity.codebase.model.artifact.ArtifactVersion)
     *
     */
    public List<ArtifactVersionFlag> readFlags(final ArtifactVersion version) {
        final Session session = openSession();
        try {
            return readFlags(session, version);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#readFlags(java.lang.Long)
     * 
     */
	public List<ArtifactFlag> readFlags(final Long artifactId) {
		final Session session = openSession();
		try {
            return readFlags(session, artifactId);
		} finally {
            session.close();
		}
	}

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#readId(java.util.UUID)
     */
    public Long readId(final UUID uniqueId) throws HypersonicException {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_ID);
            session.setUniqueId(1, uniqueId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getLong("ARTIFACT_ID");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#readLatestVersionId(java.lang.Long)
     */
    public Long readLatestVersionId(final Long artifactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_LATEST_VERSION_ID);
            session.setLong(1, artifactId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getLong("LATEST_VERSION_ID");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#readNextVersionId(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public Long readNextVersionId(final Long artifactId, final Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_NEXT_VERSION_ID);
            session.setLong(1, artifactId);
            session.setLong(2, versionId);
            
            session.executeQuery();
            if (session.nextResult()) {
                return session.getLong("NEXT_VERSION_ID");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#readPreviousVersionId(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public Long readPreviousVersionId(final Long artifactId, final Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_PREVIOUS_VERSION_ID);
            session.setLong(1, artifactId);
            session.setLong(2, versionId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getLong("PREVIOUS_VERSION_ID");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#readState(java.lang.Long)
     * 
     */
    public ArtifactState readState(final Long artifactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_STATE);
            session.setLong(1, artifactId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getStateFromInteger("ARTIFACT_STATE_ID");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

	/**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#readTeamIds(java.lang.Long)
     * 
     */
    public Set<User> readTeamRel(final Long artifactId) throws HypersonicException {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_TEAM_REL);
            session.setLong(1, artifactId);
            session.executeQuery();

            final Set<User> team = new HashSet<User>();
            while (session.nextResult()) {
                team.add(userIO.extractUser(session));
            }
            return team;
        } finally { 
            session.close();
        }
    }

    
    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#readTeamRel(java.lang.Long, java.lang.Long)
     */
    public TeamMember readTeamRel(final Long artifactId, final Long userId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_TEAM_REL_BY_ARTIFACT_BY_USER);
            session.setLong(1, artifactId);
            session.setLong(2, userId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractTeamMember(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * Read the team for an artifact.
     * 
     * @param artifactId
     *            An artifact id.
     * @return The team.
     * @throws HypersonicException
     */
    public List<TeamMember> readTeamRel2(final Long artifactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_TEAM_REL);
            session.setLong(1, artifactId);
            session.executeQuery();

            final List<TeamMember> team = new ArrayList<TeamMember>();
            while (session.nextResult()) {
                team.add(extractTeamMember(session));
            }
            return team;
        } finally {
            session.close();
        }

    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#readType(java.lang.Long)
     */
    public ArtifactType readType(final Long artifactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_TYPE);
            session.setLong(1, artifactId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getTypeFromInteger("ARTIFACT_TYPE_ID");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#readUniqueId(java.lang.Long)
     */
    public UUID readUniqueId(final Long artifactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_UNIQUE_ID);
            session.setLong(1, artifactId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getUniqueId("ARTIFACT_UNIQUE_ID");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#updateFlags(com.thinkparity.codebase.model.artifact.ArtifactVersion, java.util.List)
     *
     */
    public void updateFlags(final ArtifactVersion version,
            final List<ArtifactVersionFlag> flags) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_VERSION_FLAGS);
            session.setArtifactVersionFlags(1, flags);
            session.setLong(2, version.getArtifactId());
            session.setLong(3, version.getVersionId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not update artifact version flags.");
        } finally {
            session.close();
        }
    }

	/**
	 * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#setFlags(java.lang.Long,
	 *      java.util.List)
	 * 
	 */
	public void updateFlags(final Long artifactId,
			final List<ArtifactFlag> flags) throws HypersonicException {
		final Session session = openSession();
		try {
            session.prepareStatement(SQL_UPDATE_FLAGS);
		    session.setArtifactFlags(1, flags);
            session.setLong(2, artifactId);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Cannot update artifact flags.");
		} finally {
            session.close();
        }
	}

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler#updateState(java.lang.Long, com.thinkparity.codebase.model.artifact.ArtifactState)
     */
    public void updateState(final Long artifactId, final ArtifactState state) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_STATE);
            session.setStateAsInteger(1, state);
            session.setLong(2, artifactId);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not update state.");
        } finally {
            session.close();
        }
    }

	/**
     * Obtain the earliest version id for the given artifact.
     * 
     * @param session
     *            The database session.
     * @param artifactId
     *            The artifact id.
     * @return The earliest version id.
     */
    protected Long getEarliestVersionId(final Session session,
            final Long artifactId) {
        session.prepareStatement(GET_EARLIEST_VERSION_ID);
        session.setLong(1, artifactId);
        session.executeQuery();
        if(session.nextResult()) { return session.getLong("EARLIEST_VERSION_ID"); }
        else { return null; }
    }

    /**
	 * Obtain the latest version id for the given artifact.
	 * 
	 * @param session
	 *            The database session.
	 * @param artifactId
	 *            The artifact id.
	 * @return The latest version id.
	 */
	protected Long getLatestVersionId(final Session session,
			final Long artifactId) {
		session.prepareStatement(GET_LATEST_VERSION_ID);
		session.setLong(1, artifactId);
		session.executeQuery();
		if(session.nextResult()) { return session.getLong("LATEST_VERSION_ID"); }
		else { return null; }
	}

	/**
	 * Create the artifact.
	 * 
	 * @param session
	 *            The database session.
	 * @param artifact
	 *            The artifact.
	 * @throws HypersonicException
	 */
	void create(final Session session, final Artifact artifact)
			throws HypersonicException {
		session.prepareStatement(SQL_CREATE);
		session.setString(1, artifact.getName());
		session.setStateAsInteger(2, artifact.getState());
		session.setTypeAsInteger(3, artifact.getType());
		session.setUniqueId(4, artifact.getUniqueId());
		session.setLong(5, readLocalId(artifact.getCreatedBy()));
		session.setCalendar(6, artifact.getCreatedOn());
		session.setLong(7, readLocalId(artifact.getUpdatedBy()));
		session.setCalendar(8, artifact.getUpdatedOn());
        session.setArtifactFlags(9, artifact.getFlags());
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not create artifact.");

		artifact.setId(session.getIdentity("ARTIFACT"));
	}

	/**
	 * Create a specific artifact version.
	 * 
	 * @param session
	 *            The database session.
	 * @param version
	 *            The artifact version.
	 * @throws HypersonicException
	 */
	void createVersion(final Session session, final ArtifactVersion version) {
		session.prepareStatement(INSERT_ARTIFACT_VERSION);
		session.setLong(1, version.getArtifactId());
		session.setLong(2, version.getVersionId());
		session.setString(3, version.getArtifactName());
		session.setTypeAsString(4, version.getArtifactType());
		session.setUniqueId(5, version.getArtifactUniqueId());
		session.setString(6, version.getName());
        session.setString(7, version.getComment());
		session.setLong(8, readLocalId(version.getCreatedBy()));
		session.setCalendar(9, version.getCreatedOn());
		session.setLong(10, readLocalId(version.getUpdatedBy()));
		session.setCalendar(11, version.getUpdatedOn());
        session.setArtifactVersionFlags(12, version.getFlags());
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not create version.");

		setVersionMetaData(session, version.getArtifactId(), version
                .getVersionId(), version.getMetaData());
	}

	/**
	 * Delete an artifact.
	 * 
	 * @param session
	 *            The database session.
	 * @param artifactId
	 *            The artifact id.
	 * @throws HypersonicException
	 */
	void delete(final Session session, final Long artifactId)
			throws HypersonicException {
		session.prepareStatement(DELETE_FROM_ARTIFACT);
		session.setLong(1, artifactId);
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not delete.");
	}

	/**
	 * Delete an artifact version.
	 * 
	 * @param session
	 *            The database session.
	 * @param artifactId
	 *            The artifact id.
	 * @param versionId
	 *            The version id.
	 * @throws HypersonicException
	 */
	void deleteVersion(final Session session, final Long artifactId,
			final Long versionId) throws HypersonicException {
		session.prepareStatement(DELETE_FROM_ARTIFACT_VERSION_META_DATA);
		session.setLong(1, artifactId);
		session.setLong(2, versionId);
		session.executeUpdate();

		session.prepareStatement(DELETE_FROM_ARTIFACT_VERSION);
		session.setLong(1, artifactId);
		session.setLong(2, versionId);
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not delete version.");
	}

	/**
     * Extract a team member from the session.
     * 
     * @param session
     *            The database session.
     * @return A team member.
     */
    TeamMember extractTeamMember(final Session session) {
        final TeamMember teamMember = new TeamMember();
        teamMember.setArtifactId(session.getLong("ARTIFACT_ID"));
        teamMember.setFlags(session.getUserFlags("FLAGS"));
        teamMember.setId(session.getQualifiedUsername("JABBER_ID"));
        teamMember.setLocalId(session.getLong("USER_ID"));
        teamMember.setName(session.getString("NAME"));
        teamMember.setOrganization(session.getString("ORGANIZATION"));
        teamMember.setTitle(session.getString("TITLE"));
        return teamMember;
    }

    /**
	 * Obtain the artifact version meta data.
	 * 
	 * @param session
	 *            The database session.
	 * @param artifactId
	 *            The artifact id.
	 * @param versionId
	 *            The artifact version id.
	 * @return The artifact version meta data.
	 * @throws HypersonicException
	 */
	Properties getVersionMetaData(final Session session, final Long artifactId,
			final Long versionId) throws HypersonicException {
		session.prepareStatement(SELECT_VERSION_META_DATA_BY_ARTIFACT_ID_BY_VERSION_ID);
		session.setLong(1, artifactId);
		session.setLong(2, versionId);
		session.executeQuery();

		final Properties metaData = new Properties();
		while(session.nextResult()) {
			metaData.setProperty(
					session.getString("META_DATA_KEY"),
                    session.getString("META_DATA_VALUE"));
		}
		return metaData;
	}

    void restore(final Session session, final Artifact artifact) {
        session.prepareStatement(SQL_RESTORE);
        session.setString(1, artifact.getName());
        session.setStateAsInteger(2, artifact.getState());
        session.setTypeAsInteger(3, artifact.getType());
        session.setUniqueId(4, artifact.getUniqueId());
        session.setLong(5, readLocalId(artifact.getCreatedBy()));
        session.setCalendar(6, artifact.getCreatedOn());
        session.setLong(7, readLocalId(artifact.getUpdatedBy()));
        session.setCalendar(8, artifact.getUpdatedOn());
        if(1 != session.executeUpdate())
            throw new HypersonicException("Could not restore.");

        artifact.setId(session.getIdentity("ARTIFACT"));
    }

    /**
     * Read the artifact version's flags.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param version
     *            An <code>ArtifactVersion</code>.
     * @return A <code>List<ArtifactVersionFlag></code>.
     */
    private List<ArtifactVersionFlag> readFlags(final Session session,
            final ArtifactVersion version) {
        session.prepareStatement(SQL_READ_VERSION_FLAGS);
        session.setLong(1, version.getArtifactId());
        session.setLong(2, version.getVersionId());
        session.executeQuery();
        if (session.nextResult()) {
            return session.getArtifactVersionFlags("FLAGS");
        } else {
            return Collections.emptyList();
        }
    }

    /**
	 * Obtain the artifact's flags.
	 * 
	 * @param session
	 *            The database session.
	 * @param artifactId
	 *            The artifact id.
	 * @return The artifact's flags.
	 * @throws HypersonicException
	 */
	private List<ArtifactFlag> readFlags(final Session session,
			final Long artifactId) {
		session.prepareStatement(SQL_READ_FLAGS);
		session.setLong(1, artifactId);
		session.executeQuery();
		if (session.nextResult()) {
            return session.getArtifactFlags("FLAGS");
        } else {
            return Collections.emptyList();
        }
	}

    /**
     * Read the local user id for the jabber id.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A local user id <code>Long</code>.
     */
    private Long readLocalId(final JabberId userId) {
        final Session session = openSession();
        try {
            return userIO.readLocalId(session, userId);
        } finally {
            session.close();
        }
    }

    /**
     * Obtain the number of team members for the artifact.
     *
     * @param session
     *      A db session.
     * @param artifactId
     *      An artifact id.
     * @return The number of team rel rows.
     */
    private Integer readTeamRelCount(final Session session,
            final Long artifactId) throws HypersonicException {
        session.prepareStatement(SQL_READ_TEAM_REL_COUNT);
        session.setLong(1, artifactId);
        session.executeQuery();
        session.nextResult();
        return session.getInteger("TEAM_SIZE");
    }

    /**
	 * Set the artifact version's meta data.
	 * 
	 * @param session
	 *            The database session.
	 * @param artifactId
	 *            The artifact id.
	 * @param versionId
	 *            The artifact version id.
	 * @param versionMetaData
	 *            The artifact version meta data.
	 */
	private void setVersionMetaData(final Session session, final Long artifactId, final Long versionId,
			final Properties versionMetaData) {
		session.prepareStatement(INSERT_ARTIFACT_VERSION_META_DATA);
		session.setLong(1, artifactId);
		session.setLong(2, versionId);
		for(final Object key : versionMetaData.keySet()) {
			session.setString(3, (String) key);
			session.setString(4, versionMetaData.getProperty((String) key));
			if(1 != session.executeUpdate())
				throw new HypersonicException("Could not create meta data.");
		}
	}
}
