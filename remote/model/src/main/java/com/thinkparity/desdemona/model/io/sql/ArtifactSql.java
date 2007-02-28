/*
 * Nov 30, 2005
 */
package com.thinkparity.desdemona.model.io.sql;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.artifact.RemoteArtifact;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Artifact SQL<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArtifactSql extends AbstractSql {

    /** Sql to create an artifact. */
    private static final String SQL_CREATE =
        new StringBuffer("insert into PARITYARTIFACT ")
        .append("(ARTIFACTUUID,ARTIFACTKEYHOLDER,CREATEDBY,CREATEDON,")
        .append("UPDATEDBY,UPDATEDON) ")
		.append("values (?,?,?,?,?,?)")
		.toString();

    /** Sql to create a team member relationship. */
    private static final String SQL_CREATE_TEAM_REL =
        new StringBuffer("insert into ARTIFACT_TEAM_REL ")
        .append("(ARTIFACT_ID,USER_ID) ")
        .append("values (?,?)")
        .toString();

    /** Sql to delete an artifact. */
	private static final String SQL_DELETE =
		new StringBuffer("delete from PARITYARTIFACT ")
		.append("where ARTIFACTID=?")
		.toString();


    /** Sql to delete a team member relationship. */
    private static final String SQL_DELETE_TEAM_REL =
        new StringBuffer("delete from ARTIFACT_TEAM_REL ")
        .append("where ARTIFACT_ID=? and USER_ID=?")
        .toString();

    /** Sql to read an artifact. */
	private static final String SQL_READ =
	    new StringBuffer("select ARTIFACTID,ARTIFACTUUID,ARTIFACTKEYHOLDER,")
        .append("CREATEDON,UPDATEDON ")
        .append("from PARITYARTIFACT PA ")
        .append("where PA.ARTIFACTUUID=?")
        .toString();

	/** Sql to read the artifact id. */
    private static final String SQL_READ_ARTIFACT_ID =
        new StringBuffer("select ARTIFACTID ")
        .append("from PARITYARTIFACT PA ")
        .append("where PA.ARTIFACTUUID=?")
        .toString();

	/** Sql to read all draft unique ids for a user. */
    private static final String SQL_READ_DRAFT_ARTIFACT_IDS =
        new StringBuffer("select PA.ARTIFACTUUID ")
        .append("from parityArtifact PA ")
        .append("where PA.ARTIFACTKEYHOLDER=? ")
        .append("order by PA.ARTIFACTID asc")
        .toString();

    /** Sql to read the artifact key holder. */
    private static final String SQL_READ_KEY_HOLDER =
            new StringBuffer("select ARTIFACTKEYHOLDER ")
            .append("from PARITYARTIFACT ")
            .append("where ARTIFACTUUID=?")
            .toString();

    /** Sql to read the team member relationship. */
    private static final String SQL_READ_TEAM_REL_BY_ARTIFACT_BY_USER =
        new StringBuffer("select U.USERNAME,U.USER_ID,ATR.ARTIFACT_ID ")
        .append("from ARTIFACT_TEAM_REL ATR ")
        .append("inner join PARITY_USER U on ATR.USER_ID = U.USER_ID ")
        .append("where ATR.ARTIFACT_ID=? ")
        .append("and ATR.USER_ID=?")
        .append("order by U.USERNAME asc")
        .toString();

	/** Sql to read the team relationship. */
    private static final String SQL_READ_TEAM_REL_BY_ARTIFACT_ID =
        new StringBuffer("select U.USERNAME,U.USER_ID,ATR.ARTIFACT_ID ")
        .append("from ARTIFACT_TEAM_REL ATR ")
        .append("inner join PARITY_USER U on ATR.USER_ID = U.USER_ID ")
        .append("where ATR.ARTIFACT_ID=? ")
        .append("order by U.USERNAME asc")
        .toString();

    /** Sql to read the team member relationship. */
    private static final String SQL_READ_TEAM_REL_COUNT_BY_ARTIFACT_ID =
        new StringBuffer("select COUNT(U.USER_ID) \"TEAM_REL_COUNT\" ")
        .append("from ARTIFACT_TEAM_REL ATR ")
        .append("inner join PARITY_USER U on ATR.USER_ID = U.USER_ID ")
        .append("where ATR.ARTIFACT_ID=?")
        .toString();

	/** Sql to update the keyholder. */
	private static final String SQL_UPDATE_KEYHOLDER =
		new StringBuffer("update PARITYARTIFACT ")
		.append("set ARTIFACTKEYHOLDER=?,UPDATEDON=current_timestamp,")
		.append("UPDATEDBY=? ")
		.append("where ARTIFACTID=?")
		.toString();

    /**
     * Create ArtifactSql.
     *
	 */
	public ArtifactSql() {
        super();
	}

	/**
     * Create an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @param keyHolder
     *            An artifact key holder user id <code>JabberId</code>.
     * @param createdBy
     *            The creation user id <code>JabberId</code>.
     * @param createdOn
     *            The creation date <code>Calendar</code>.
     * @return The artifact id <code>Long</code>.
     */
	public Long create(final UUID uniqueId, final JabberId keyHolder,
            final JabberId createdBy, final Calendar createdOn) {
		final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SQL_CREATE);
			session.setUniqueId(1, uniqueId);
			session.setString(2, keyHolder.getUsername());
            session.setString(3, createdBy.getUsername());
            session.setCalendar(4, createdOn);
            session.setString(5, createdBy.getUsername());
            session.setCalendar(6, createdOn);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create artifact {0}.", uniqueId);
            session.commit();

			return session.getIdentity();
        } catch (final Throwable t) {
            throw translateError(session, t);
		} finally {
            session.close();
		}
	}

    /**
     * Create a team relationship.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    public void createTeamRel(final Long artifactId, final Long userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_TEAM_REL);
            session.setLong(1, artifactId);
            session.setLong(2, userId);
            if(1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not create team relationship for {0}:{1}.",
                        artifactId, userId);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Delete an artifact.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     */
	public void delete(final Long artifactId) {
	    final HypersonicSession session = openSession();
        try {
			session.prepareStatement(SQL_DELETE);
			session.setLong(1, artifactId);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Cannot delete artifact {0}.", artifactId);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
		} finally {
            session.close();
		}
	}

    /**
     * Delete an artifact team relationship.
     * 
     * @param artifactId
     *            The artifact id.
     * @param userId
     *            The user id.
     */
	public void deleteTeamRel(final Long artifactId, final Long userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_TEAM_REL);
            session.setLong(1, artifactId);
            session.setLong(2, userId);
            if(1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not delete team relationship for {0}:{1}.",
                        artifactId, userId);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return An <code>Artifact</code>.
     */
    public Artifact read(final UUID uniqueId) {
        final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SQL_READ);
			session.setString(1, uniqueId.toString());
			session.executeQuery();
			if (session.nextResult()) {
                return extract(session);
			} else {
                return null;
			}
		} finally {
            session.close();
		}
	}

    /**
     * Read the artifact id for a unique id.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return An artifact id <code>Long</code>.
     */
    public Long readArtifactId(final UUID uniqueId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_ARTIFACT_ID);
            session.setUniqueId(1, uniqueId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getLong("ARTIFACTID");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * Read the artifact ids for a user that is the current key holder.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List</code> of artifact id <code>Long</code>s.
     */
    public List<UUID> readDraftUniqueIds(final JabberId userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_DRAFT_ARTIFACT_IDS);
            session.setString(1, userId.getUsername());
            session.executeQuery();
            final List<UUID> uniqueIds = new ArrayList<UUID>();
            while (session.nextResult()) {
                uniqueIds.add(session.getUniqueId("ARTIFACTUUID"));
            }
            return uniqueIds;
        } finally {
            session.close();
        }
    }

    /**
     * Read the current key holder.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A user id <code>JabberId</code>.
     */
    public JabberId readKeyHolder(final UUID uniqueId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_KEY_HOLDER);
            session.setString(1, uniqueId.toString());
            session.executeQuery();
            if (session.nextResult()) {
                return JabberIdBuilder.parseUsername(
                        session.getString("ARTIFACTKEYHOLDER"));
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
    public List<TeamMember> readTeamRel(final Long artifactId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_TEAM_REL_BY_ARTIFACT_ID);
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
     * Read a team member for an artifact.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param userId
     *            A user id <code>Long</code>.
     * @return A <code>TeamMember</code>.
     */
    public TeamMember readTeamRel(final Long artifactId, final Long userId) {
        final HypersonicSession session = openSession();
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
     * Read a count of the team members for an artifact.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @return A team member count <code>Integer</code>.
     */
    public Integer readTeamRelCount(final Long artifactId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_TEAM_REL_COUNT_BY_ARTIFACT_ID);
            session.setLong(1, artifactId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getInteger("TEAM_REL_COUNT");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

	/**
     * Update an artifact key holder.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param keyHolder
     *            A user id <code>JabberId</code>.
     * @param updatedBy
     *            An updated by user id <code>JabberId</code>.
     */
    public void updateKeyHolder(final Long artifactId, final JabberId keyHolder,
            final JabberId updatedBy) {
        updateKeyHolder(artifactId, keyHolder.getUsername(), updatedBy);
    }

    /**
     * Update an artifact key holder.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param keyHolder
     *            A user id <code>String</code>.
     * @param updatedBy
     *            An updated by user id <code>JabberId</code>.
     */
    public void updateKeyHolder(final Long artifactId,
            final String keyHolder, final JabberId updatedBy) {
		final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SQL_UPDATE_KEYHOLDER);
            session.setString(1, keyHolder);
			session.setString(2, updatedBy.getUsername());
			session.setLong(3, artifactId);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not update artifact key holder for {0}:{1}",
                        artifactId, keyHolder);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
		} finally {
            session.close();
		}
	}

    /**
     * Update an artifact key holder.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param keyHolder
     *            A <code>User</code>.
     * @param updatedBy
     *            An updated by user id <code>JabberId</code>.
     */
	public void updateKeyHolder(final Long artifactId, final User keyHolder,
            final JabberId updatedBy) {
        updateKeyHolder(artifactId, keyHolder.getId(), updatedBy);
    }

    /**
     * Extract an artifact from a database session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return An <code>Artifact</code>.
     */
	Artifact extract(final HypersonicSession session) {
        final Artifact artifact = new RemoteArtifact();
        artifact.setId(session.getLong("ARTIFACTID"));
        artifact.setUniqueId(UUID.fromString(session.getString("ARTIFACTUUID")));
        artifact.setCreatedOn(session.getCalendar("CREATEDON"));
        artifact.setUpdatedOn(session.getCalendar("UPDATEDON"));
        return artifact;
	}

    /**
     * Extract a team member from the session.
     * 
     * @param session
     *            The database session.
     * @return A team member.
     */
    TeamMember extractTeamMember(final HypersonicSession session) {
        final TeamMember teamMember = new TeamMember();
        teamMember.setArtifactId(session.getLong("ARTIFACT_ID"));
        teamMember.setId(JabberIdBuilder.parseUsername(session.getString("USERNAME")));
        teamMember.setLocalId(session.getLong("USER_ID"));
        return teamMember;
    }
}
