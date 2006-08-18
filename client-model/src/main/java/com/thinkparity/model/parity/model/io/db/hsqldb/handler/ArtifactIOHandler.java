/*
 * Feb 9, 2006
 */
package com.thinkparity.model.parity.model.io.db.hsqldb.handler;

import java.util.*;

import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.artifact.ArtifactRemoteInfo;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.model.parity.model.io.db.hsqldb.Session;
import com.thinkparity.model.parity.model.user.TeamMember;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactIOHandler extends AbstractIOHandler implements
		com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler {

    /** Sql query to delete the artifact. */
	private static final String DELETE_FROM_ARTIFACT =
		new StringBuffer("delete from ARTIFACT ")
		.append("where ARTIFACT_ID=?")
		.toString();

    /**
	 * Sql query to delete artifact flags for an artifact.
	 * 
	 */
	private static final String DELETE_FROM_ARTIFACT_FLAG_REL =
		new StringBuffer("delete from ARTIFACT_FLAG_REL ")
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

    private static final String GET_LATEST_VERSION_ID =
		new StringBuffer("select MAX(ARTIFACT_VERSION_ID) LATEST_VERSION_ID ")
		.append("from ARTIFACT_VERSION ")
		.append("where ARTIFACT_ID=?")
		.toString();

    /**
	 * Sql to create the artifact.
	 * 
	 */
	private static final String INSERT_ARTIFACT =
		new StringBuffer("insert into ARTIFACT ")
		.append("(ARTIFACT_NAME,ARTIFACT_STATE_ID,ARTIFACT_TYPE_ID,")
		.append("ARTIFACT_UNIQUE_ID,CREATED_BY,CREATED_ON,UPDATED_BY,")
		.append("UPDATED_ON) ")
		.append("values (?,?,?,?,?,?,?,?)")
		.toString();

	/**
	 * Sql to insert an artifact flag relationship.
	 * 
	 */
	private static final String INSERT_ARTIFACT_FLAG_REL =
		new StringBuffer("insert into ARTIFACT_FLAG_REL ")
		.append("(ARTIFACT_ID,ARTIFACT_FLAG_ID) ")
		.append("values (?,?)")
		.toString();

	/**
	 * Sql query used to create an artifact version.
	 * 
	 */
	private static final String INSERT_ARTIFACT_VERSION =
		new StringBuffer("insert into ARTIFACT_VERSION ")
		.append("(ARTIFACT_ID,ARTIFACT_VERSION_ID,ARTIFACT_NAME,ARTIFACT_TYPE,")
		.append("ARTIFACT_UNIQUE_ID,CREATED_BY,CREATED_ON,UPDATED_BY,")
		.append("UPDATED_ON) ")
		.append("values (?,?,?,?,?,?,?,?,?)")
		.toString();

	private static final String INSERT_ARTIFACT_VERSION_META_DATA =
		new StringBuffer("insert into ARTIFACT_VERSION_META_DATA ")
		.append("(ARTIFACT_ID,ARTIFACT_VERSION_ID,KEY,VALUE) ")
		.append("values (?,?,?,?)")
		.toString();

	/**
	 * Sql to get the artifact's flag.
	 * 
	 */
	private static final String SELECT_ARTIFACT_FLAG_REL_BY_ARTIFACT_ID =
		new StringBuffer("select ARTIFACT_ID,ARTIFACT_FLAG_ID ")
		.append("from ARTIFACT_FLAG_REL ")
		.append("where ARTIFACT_ID=?")
		.toString();

	private static final String SELECT_PREVIOUS_ARTIFACT_VERSION_ID =
		new StringBuffer("select MAX(ARTIFACT_VERSION_ID) PREVIOUS_VERSION_ID ")
		.append("from ARTIFACT_VERSION ")
		.append("where ARTIFACT_ID=?")
		.toString();

	/**
	 * Sql to extract the artifact version meta data.
	 * 
	 */
	private static final String SELECT_VERSION_META_DATA_BY_ARTIFACT_ID_BY_VERSION_ID =
		new StringBuffer("select KEY,VALUE ")
		.append("from ARTIFACT_VERSION_META_DATA ")
		.append("where ARTIFACT_ID=? and ARTIFACT_VERSION_ID=?")
		.toString();

	/**
	 * Sql to create the remote info.
	 * 
	 */
	private static final String SQL_CREATE_REMOTE_INFO =
		new StringBuffer("insert into ARTIFACT_REMOTE_INFO ")
		.append("(ARTIFACT_ID,UPDATED_BY,UPDATED_ON) ")
		.append("values (?,?,?)")
		.toString();

	/** Sql to create a team member relationship. */
    private static final String SQL_CREATE_TEAM_REL =
        new StringBuffer("insert into ARTIFACT_TEAM_REL ")
        .append("(ARTIFACT_ID,USER_ID) ")
        .append("values (?,?)")
        .toString();

	/**
	 * Sql to delete the remote info.
	 * 
	 */
	private static final String SQL_DELETE_REMOTE_INFO =
		new StringBuffer("delete from ARTIFACT_REMOTE_INFO ")
		.append("where ARTIFACT_ID=?")
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
	
	/** Sql to determine if an artifact version exists. */
    private static final String SQL_DOES_VERSION_EXIST =
            new StringBuffer("select COUNT(*) \"COUNT\" ")
            .append("from ARTIFACT A ")
            .append("inner join ARTIFACT_VERSION AV on A.ARTIFACT_ID=AV.ARTIFACT_ID ")
            .append("where ARTIFACT_ID=? and ARTIFACT_VERSION_ID=?")
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
        .append("U.USER_ID,U.ORGANIZATION,ATR.ARTIFACT_ID ")
        .append("from ARTIFACT_TEAM_REL ATR ")
        .append("inner join USER U on ATR.USER_ID = U.USER_ID ")
        .append("where ATR.ARTIFACT_ID=?")
        .toString();

    /**
     * Sql to read the team relationship.
     * 
     * @see ArtifactIOHandler#SQL_READ_TEAM_REL
     */
    private static final String SQL_READ_TEAM_REL_BY_ARTIFACT_BY_USER =
            new StringBuffer(SQL_READ_TEAM_REL)
            .append(" and ATR.USER_ID=?")
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

	/**
	 * Sql to update the remote info of an artifact.
	 * 
	 */
	private static final String SQL_UPDATE_REMOTE_INFO =
		new StringBuffer("update ARTIFACT_REMOTE_INFO ")
		.append("set UPDATED_BY=?,UPDATED_ON=? ")
		.append("where ARTIFACT_ID=?")
		.toString();

	private static final String SQL_UPDATE_STATE =
		new StringBuffer("update ARTIFACT set ARTIFACT_STATE_ID=?,")
		.append("UPDATED_ON=CURRENT_TIMESTAMP ")
		.append("where ARTIFACT_ID=?")
		.toString();

	/**
     * Obtain a log4j api id.
     * 
     * @param api
     *            The api.
     * @return A log4j api id.
     */
    protected static StringBuffer getApiId(final String api) {
        return getIOId("[ARTIFACT]").append(" ").append(api);
    }

	/**
     * Obtain a log4j error id.
     * 
     * @param api
     *            The api.
     * @param error
     *            The error.
     * @return A log4j error id.
     */
    protected static String getErrorId(final String api, final String error) {
        return getApiId(api).append(" ").append(error).toString();
    }

    /** The user io interface. */
    private final UserIOHandler userIO;

	/**
	 * Create a ArtifactIOHandler.
	 */
	public ArtifactIOHandler() {
        super();
        this.userIO = new UserIOHandler();
	}

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler#createRemoteInfo(java.lang.Long,
     *      com.thinkparity.model.xmpp.JabberId, java.util.Calendar)
     * 
     */
	public void createRemoteInfo(final Long artifactId,
			final JabberId updatedBy, final Calendar updatedOn)
			throws HypersonicException {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_CREATE_REMOTE_INFO);
			session.setLong(1, artifactId);
			session.setQualifiedUsername(2, updatedBy);
			session.setCalendar(3, updatedOn);
			if(1 != session.executeUpdate())
				throw new HypersonicException("Could not create remote info.");

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler#createTeamRel(java.lang.Long,
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
                throw new HypersonicException(getErrorId("[CREATE TEAM REL]", "[COULD NOT CREATE TEAM RELATIONSHIP]"));

            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler#deleteRemoteInfo(java.lang.Long)
     * 
     */
	public void deleteRemoteInfo(final Long artifactId) throws HypersonicException {
		final Session session = openSession();
		try {
            deleteRemoteInfo(session, artifactId);
			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
     * @see com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler#deleteTeamRel(java.lang.Long)
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

            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

	/**
     * @see com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler#deleteTeamRel(java.lang.Long,
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

            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler#doesVersionExist(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public Boolean doesVersionExist(Long artifactId, Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DOES_VERSION_EXIST);
            session.setLong(1, artifactId);
            session.setLong(2, versionId);
            session.executeQuery();

            session.nextResult();
            if(0 == session.getInteger("COUNT")) { return Boolean.FALSE; }
            else { return Boolean.TRUE; }
        }
        finally { session.close(); }
    }

    /**
	 * Obtain the artifact's flags.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return The artifact's flags.
	 * @throws HypersonicException
	 */
	public List<ArtifactFlag> getFlags(final Long artifactId)
			throws HypersonicException {
		final Session session = openSession();
		try { return getFlags(session, artifactId); }
		finally { session.close(); }
	}

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler#readId(java.util.UUID)
     */
    public Long readId(final UUID uniqueId) throws HypersonicException {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_ID);
            session.setUniqueId(1, uniqueId);
            session.executeQuery();
            if(session.nextResult()) { return session.getLong("ARTIFACT_ID"); }
            else { return null; }
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler#readLatestVersionId(java.lang.Long)
     */
    public Long readLatestVersionId(final Long artifactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_LATEST_VERSION_ID);
            session.setLong(1, artifactId);
            session.executeQuery();
            if(session.nextResult()) { return session.getLong("LATEST_VERSION_ID"); }
            else { return null; }
        }
        finally { session.close(); }
    }

	/**
     * @see com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler#readState(java.lang.Long)
     * 
     */
    public ArtifactState readState(final Long artifactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_STATE);
            session.setLong(1, artifactId);
            session.executeQuery();
            if(session.nextResult()) { return session.getStateFromInteger("ARTIFACT_STATE_ID"); }
            else { return null; }
        }
        finally { session.close(); }
    }

    
    /**
     * @see com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler#readTeam(java.lang.Long)
     * 
     */
    public Set<User> readTeamRel(final Long artifactId) throws HypersonicException {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_TEAM_REL);
            session.setLong(1, artifactId);
            session.executeQuery();

            final Set<User> team = new HashSet<User>();
            while(session.nextResult()) { team.add(extractUser(session)); }
            return team;
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler#readTeamRel(java.lang.Long, java.lang.Long)
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
            while(session.nextResult()) { team.add(extractTeamMember(session)); }
            return team;
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }

    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler#readType(java.lang.Long)
     */
    public ArtifactType readType(final Long artifactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_TYPE);
            session.setLong(1, artifactId);
            session.executeQuery();
            if(session.nextResult()) { return session.getTypeFromInteger("ARTIFACT_TYPE_ID"); }
            else { return null; }
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler#readUniqueId(java.lang.Long)
     */
    public UUID readUniqueId(final Long artifactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_UNIQUE_ID);
            session.setLong(1, artifactId);
            session.executeQuery();
            if(session.nextResult()) { return session.getUniqueId("ARTIFACT_UNIQUE_ID"); }
            else { return null; }
        }
        finally { session.close(); }
    }

	/**
	 * @see com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler#setFlags(java.lang.Long,
	 *      java.util.List)
	 * 
	 */
	public void updateFlags(final Long artifactId,
			final List<ArtifactFlag> flags) throws HypersonicException {
		final Session session = openSession();
		try {
			setFlags(session, artifactId, flags);
			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
     * @see com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler#updateRemoteInfo(java.lang.Long,
     *      com.thinkparity.model.xmpp.JabberId, java.util.Calendar)
     * 
     */
	public void updateRemoteInfo(final Long artifactId,
			final JabberId updatedBy, final Calendar updatedOn)
			throws HypersonicException {
		final Session session = openSession();
		try {
			session.prepareStatement(SQL_UPDATE_REMOTE_INFO);
			session.setQualifiedUsername(1, updatedBy);
			session.setCalendar(2, updatedOn);
			session.setLong(3, artifactId);

			if(1 != session.executeUpdate())
				throw new HypersonicException("Could not update remote info.");

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
	}

	/**
     * @see com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler#updateState(java.lang.Long, com.thinkparity.model.parity.model.artifact.ArtifactState)
     */
    public void updateState(final Long artifactId, final ArtifactState state) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_STATE);
            session.setStateAsInteger(1, state);
            session.setLong(2, artifactId);
            if(1 != session.executeUpdate())
                throw new HypersonicException(getErrorId("[UPDATE STATE]", "[CANNOT UPDATE STATE]"));
            session.commit();
        }
        catch(final HypersonicException hx) {
            session.rollback();
            throw hx;
        }
        finally { session.close(); }
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
	 * Obtain the next version id for the given artifact.
	 * 
	 * @param session
	 *            The database session.
	 * @param artifactId
	 *            The artifact id.
	 * @return The next version id.
	 */
	protected Long getNextVersionId(final Session session,
			final Long artifactId) {
		session.prepareStatement(SELECT_PREVIOUS_ARTIFACT_VERSION_ID);
		session.setLong(1, artifactId);
		session.executeQuery();
		if(session.nextResult()) {
			return session.getLong("PREVIOUS_VERSION_ID") + 1L;
		}
		else { return 1L; }
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
		session.prepareStatement(INSERT_ARTIFACT);
		session.setString(1, artifact.getName());
		session.setStateAsInteger(2, artifact.getState());
		session.setTypeAsInteger(3, artifact.getType());
		session.setUniqueId(4, artifact.getUniqueId());
		session.setString(5, artifact.getCreatedBy());
		session.setCalendar(6, artifact.getCreatedOn());
		session.setString(7, artifact.getUpdatedBy());
		session.setCalendar(8, artifact.getUpdatedOn());
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not create.");

		artifact.setId(session.getIdentity());

		deleteFlags(session, artifact.getId());
		insertFlags(session, artifact.getId(), artifact.getFlags());
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
		session.setString(3, version.getName());
		session.setTypeAsString(4, version.getArtifactType());
		session.setUniqueId(5, version.getArtifactUniqueId());
		session.setString(6, version.getCreatedBy());
		session.setCalendar(7, version.getCreatedOn());
		session.setString(8, version.getUpdatedBy());
		session.setCalendar(9, version.getUpdatedOn());
		if(1 != session.executeUpdate())
			throw new HypersonicException(getErrorId("[CREATE VERSION]", "[COULD NOT CREATE VERSION]"));

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
		deleteFlags(session, artifactId);

		session.prepareStatement(DELETE_FROM_ARTIFACT);
		session.setLong(1, artifactId);
		if(1 != session.executeUpdate())
			throw new HypersonicException("Could not delete.");
	}

	/**
     * Delete the artifact remote info.
     * 
     * @param session
     *            A database session.
     * @param artifactId
     *            The artifact id.
     */
    void deleteRemoteInfo(final Session session, final Long artifactId) {
        session.prepareStatement(SQL_DELETE_REMOTE_INFO);
        session.setLong(1, artifactId);
        if(1 != session.executeUpdate())
            throw new HypersonicException(getErrorId("[DELETE REMOTE INFO]", "[COULD NOT DELETE REMOTE INFO]"));
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
     * Extract the artifact remote info from the session. The required column
     * names are:
     * <ul>
     * <li>REMOTE_UPDATED_BY
     * <li>REMOTE_UPDATED_ON
     * 
     * @param session
     *            The database session.
     * @return The artifact remote info.
     */
	ArtifactRemoteInfo extractRemoteInfo(final Session session) {
        final ArtifactRemoteInfo remoteInfo = new ArtifactRemoteInfo();
        remoteInfo.setUpdatedBy(session.getQualifiedUsername("REMOTE_UPDATED_BY"));
        remoteInfo.setUpdatedOn(session.getCalendar("REMOTE_UPDATED_ON"));
        return remoteInfo;
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
        teamMember.setId(session.getQualifiedUsername("JABBER_ID"));
        teamMember.setLocalId(session.getLong("USER_ID"));
        teamMember.setName(session.getString("NAME"));
        teamMember.setOrganization(session.getString("ORGANIZATION"));
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
					session.getString("KEY"), session.getString("VALUE"));
		}
		return metaData;
	}

    /**
	 * Set the flags for the artifact.
	 * 
	 * @param session
	 *            The database session.
	 * @param artifactId
	 *            The artifact id.
	 * @param flags
	 *            The flags.
	 * @throws HypersonicException
	 */
	void setFlags(final Session session, final Long artifactId,
			final Collection<ArtifactFlag> flags) throws HypersonicException {
		deleteFlags(session, artifactId);
		insertFlags(session, artifactId, flags);
	}

    /**
	 * Delete all artifact flags for an artifact.
	 * 
	 * @param session
	 *            The databse session.
	 * @param artifactId
	 *            The artifact id.
	 */
	private void deleteFlags(final Session session, final Long artifactId) {
		session.prepareStatement(DELETE_FROM_ARTIFACT_FLAG_REL);
		session.setLong(1, artifactId);
		session.executeUpdate();
	}

    /**
     * Extract a user from the session.
     * 
     * @param session
     *            The database session.
     * @return A user.
     * @see UserIOHandler#extractUser(Session)
     */
    private User extractUser(final Session session) {
        return userIO.extractUser(session);
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
	private List<ArtifactFlag> getFlags(final Session session,
			final Long artifactId) {
		session.prepareStatement(SELECT_ARTIFACT_FLAG_REL_BY_ARTIFACT_ID);
		session.setLong(1, artifactId);
		session.executeQuery();

		final List<ArtifactFlag> flags = new LinkedList<ArtifactFlag>();
		while(session.nextResult()) {
			flags.add(session.getFlagFromInteger("ARTIFACT_FLAG_ID"));
		}
		return flags;
	}

    /**
	 * Insert the artifact flag relationships for all of the flags.
	 * 
	 * @param session
	 *            The datbase session.
	 * @param artifactId
	 *            The artifact id.
	 * @param flags
	 *            The artifact flags.
	 */
	private void insertFlags(final Session session, final Long artifactId,
			final Collection<ArtifactFlag> flags) {
		session.prepareStatement(INSERT_ARTIFACT_FLAG_REL);
		for(final ArtifactFlag flag : flags) {
			session.setLong(1, artifactId);
			session.setFlagAsInteger(2, flag);
			if(1 != session.executeUpdate())
				throw new HypersonicException("Could not insert flag.");
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
