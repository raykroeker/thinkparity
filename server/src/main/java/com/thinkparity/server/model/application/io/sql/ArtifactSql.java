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
        new StringBuilder("insert into TPSD_ARTIFACT ")
        .append("(ARTIFACT_UNIQUE_ID,ARTIFACT_DRAFT_OWNER,")
        .append("ARTIFACT_LATEST_VERSION_ID,CREATED_BY,CREATED_ON,UPDATED_BY,")
        .append("UPDATED_ON) ")
		.append("values (?,?,?,?,?,?,?)")
		.toString();

    /** Sql to create a team member relationship. */
    private static final String SQL_CREATE_TEAM_REL =
        new StringBuilder("insert into TPSD_ARTIFACT_TEAM_REL ")
        .append("(ARTIFACT_ID,USER_ID) ")
        .append("values (?,?)")
        .toString();

    /** Sql to delete an artifact. */
	private static final String SQL_DELETE =
		new StringBuilder("delete from TPSD_ARTIFACT ")
		.append("where ARTIFACT_ID=?")
		.toString();

    /** Sql to delete a team member relationship. */
    private static final String SQL_DELETE_TEAM_REL =
        new StringBuilder("delete from TPSD_ARTIFACT_TEAM_REL ")
        .append("where ARTIFACT_ID=? and USER_ID=?")
        .toString();

    /** Sql to determine team membership existence. */
    private static final String SQL_DOES_EXIST_TEAM_MEMBER_PK =
        new StringBuilder("select count(ATR.ARTIFACT_ID) \"TEAM_REL_COUNT\" ")
        .append("from TPSD_ARTIFACT_TEAM_REL ATR ")
        .append("where ATR.ARTIFACT_ID=? and ATR.USER_ID=?")
        .toString();

    /** Sql to determine artifact existence by unique key. */
    private static final String SQL_DOES_EXIST_UK =
        new StringBuilder("select count(A.ARTIFACT_ID) \"ARTIFACT_COUNT\" ")
        .append("from TPSD_ARTIFACT A ")
        .append("where A.ARTIFACT_UNIQUE_ID=?")
        .toString();

    /** Sql to read an artifact. */
	private static final String SQL_READ =
	    new StringBuilder("select ARTIFACT_ID,ARTIFACT_UNIQUE_ID,CREATED_ON,")
        .append("UPDATED_ON ")
        .append("from TPSD_ARTIFACT A ")
        .append("where A.ARTIFACT_UNIQUE_ID=?")
        .toString();

	/** Sql to read the artifact id. */
    private static final String SQL_READ_ARTIFACT_ID =
        new StringBuilder("select A.ARTIFACT_ID ")
        .append("from TPSD_ARTIFACT A ")
        .append("where A.ARTIFACT_UNIQUE_ID=?")
        .toString();

	/** Sql to read the artifact draft owner. */
    private static final String SQL_READ_DRAFT_OWNER =
        new StringBuilder("select U.USERNAME \"ARTIFACT_DRAFT_OWNER\" ")
        .append("from TPSD_ARTIFACT A ")
        .append("inner join TPSD_USER U on A.ARTIFACT_DRAFT_OWNER=U.USER_ID ")
        .append("where A.ARTIFACT_UNIQUE_ID=?")
        .toString();

    /** Sql to read all draft unique ids for a user. */
    private static final String SQL_READ_DRAFT_UNIQUE_IDS =
        new StringBuilder("select A.ARTIFACT_UNIQUE_ID ")
        .append("from TPSD_ARTIFACT A ")
        .append("where A.ARTIFACT_DRAFT_OWNER=? ")
        .append("order by A.ARTIFACT_ID asc")
        .toString();

    /** Sql to read the latest version id. */
    private static final String SQL_READ_LATEST_VERSION_ID =
        new StringBuilder("select A.ARTIFACT_LATEST_VERSION_ID ")
        .append("from TPSD_ARTIFACT A ")
        .append("where A.ARTIFACT_ID=?")
        .toString();

	/** Sql to read a user's artifacts from the team. */
    private static final String SQL_READ_TEAM_ARTIFACT_UNIQUE_IDS =
        new StringBuilder("select A.ARTIFACT_UNIQUE_ID ")
        .append("from TPSD_ARTIFACT_TEAM_REL ATR ")
        .append("inner join TPSD_ARTIFACT A on A.ARTIFACT_ID=ATR.ARTIFACT_ID ")
        .append("where ATR.USER_ID=?")
        .toString();

    /** Sql to read the team member relationship. */
    private static final String SQL_READ_TEAM_REL_BY_ARTIFACT_BY_USER =
        new StringBuilder("select U.USERNAME,U.USER_ID,ATR.ARTIFACT_ID ")
        .append("from TPSD_ARTIFACT_TEAM_REL ATR ")
        .append("inner join TPSD_USER U on ATR.USER_ID=U.USER_ID ")
        .append("where ATR.ARTIFACT_ID=? ")
        .append("and ATR.USER_ID=?")
        .append("order by U.USERNAME asc")
        .toString();

    /** Sql to read the team relationship. */
    private static final String SQL_READ_TEAM_REL_BY_ARTIFACT_ID =
        new StringBuilder("select U.USERNAME,U.USER_ID,ATR.ARTIFACT_ID ")
        .append("from TPSD_ARTIFACT_TEAM_REL ATR ")
        .append("inner join TPSD_USER U on ATR.USER_ID=U.USER_ID ")
        .append("where ATR.ARTIFACT_ID=? ")
        .append("order by U.USERNAME asc")
        .toString();

    /** Sql to read the team member relationship. */
    private static final String SQL_READ_TEAM_REL_COUNT_BY_ARTIFACT_ID =
        new StringBuilder("select COUNT(U.USER_ID) \"TEAM_REL_COUNT\" ")
        .append("from TPSD_ARTIFACT_TEAM_REL ATR ")
        .append("inner join TPSD_USER U on ATR.USER_ID=U.USER_ID ")
        .append("where ATR.ARTIFACT_ID=?")
        .toString();

	/** Sql to update the draft owner. */
    private static final String SQL_UPDATE_DRAFT_OWNER =
        new StringBuilder("update TPSD_ARTIFACT ")
        .append("set ARTIFACT_DRAFT_OWNER=?,UPDATED_ON=?,UPDATED_BY=? ")
        .append("where ARTIFACT_ID=? and ARTIFACT_DRAFT_OWNER=?")
        .toString();

    /** Sql to update the latest version id. */
    private static final String SQL_UPDATE_LATEST_VERSION_ID =
        new StringBuilder("update TPSD_ARTIFACT ")
        .append("set ARTIFACT_LATEST_VERSION_ID=?,UPDATED_BY=?,UPDATED_ON=? ")
        .append("where ARTIFACT_ID=? AND ARTIFACT_DRAFT_OWNER=?")
        .toString();

    /** A user sql interface. */
    private final UserSql userSql;

    /**
     * Create ArtifactSql.
     *
	 */
	public ArtifactSql() {
        super();
        this.userSql = new UserSql();
	}
    /**
     * Create an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @param draftOwner
     *            A draft owner user id <code>JabberId</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param createdBy
     *            The creation user id <code>JabberId</code>.
     * @param createdOn
     *            The creation date <code>Calendar</code>.
     * @return The artifact id <code>Long</code>.
     */
	public Long create(final UUID uniqueId, final JabberId draftOwner,
            final Long versionId, final JabberId createdBy,
            final Calendar createdOn) {
		final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SQL_CREATE);
			session.setUniqueId(1, uniqueId);
			session.setLong(2, readLocalUserId(draftOwner));
            session.setLong(3, versionId);
            session.setLong(4, readLocalUserId(createdBy));
            session.setCalendar(5, createdOn);
            session.setLong(6, readLocalUserId(createdBy));
            session.setCalendar(7, createdOn);
            if (1 != session.executeUpdate())
                throw panic("Could not create artifact {0}.", uniqueId);
            session.commit();

			return session.getIdentity("TPSD_ARTIFACT");
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
                throw panic("Cannot delete artifact {0}.", artifactId);

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
     * Determine an artifact's existence.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return True if the artifact exists.
     */
    public Boolean doesExist(final UUID uniqueId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_UK);
            session.setUniqueId(1, uniqueId);
            session.executeQuery();
            if (session.nextResult()) {
                final int artifactCount = session.getInteger("ARTIFACT_COUNT");
                if (0 == artifactCount) {
                    return Boolean.FALSE;
                } else if (1 == artifactCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine artifact existence.");
                }
            } else {
                throw new HypersonicException("Could not determine artifact existence.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * Determine whether or not a team relationship exists.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param userId
     *            A user id <code>Long</code>.
     * @return True if a team relationship exists.
     */
    public Boolean doesExistTeamMember(final Long artifactId, final Long userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_TEAM_MEMBER_PK);
            session.setLong(1, artifactId);
            session.setLong(2, userId);
            session.executeQuery();
            if (session.nextResult()) {
                final int teamRelCount = session.getInteger("TEAM_REL_COUNT");
                if (0 == teamRelCount) {
                    return Boolean.FALSE;
                } else if (1 == teamRelCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine team relationship existence.");
                }
            } else {
                throw new HypersonicException("Could not determine team relationship existence.");
            }
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
                return session.getLong("ARTIFACT_ID");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * Read the current draft owner.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A user id <code>JabberId</code>.
     */
    public JabberId readDraftOwner(final UUID uniqueId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_DRAFT_OWNER);
            session.setString(1, uniqueId.toString());
            session.executeQuery();
            if (session.nextResult()) {
                return JabberIdBuilder.parseUsername(
                        session.getString("ARTIFACT_DRAFT_OWNER"));
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * Read the artifact ids for a user that is the current draft owner.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List</code> of artifact id <code>Long</code>s.
     */
    public List<UUID> readDraftUniqueIds(final JabberId draftOwner) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_DRAFT_UNIQUE_IDS);
            session.setLong(1, readLocalUserId(draftOwner));
            session.executeQuery();
            final List<UUID> uniqueIds = new ArrayList<UUID>();
            while (session.nextResult()) {
                uniqueIds.add(session.getUniqueId("ARTIFACT_UNIQUE_ID"));
            }
            return uniqueIds;
        } finally {
            session.close();
        }
    }

    /**
     * Read the latest version id for an artifact.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @return The latest version id <code>Long</code>.
     */
    public Long readLatestVersionId(final Long artifactId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_LATEST_VERSION_ID);
            session.setLong(1, artifactId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getLong("ARTIFACT_LATEST_VERSION_ID");
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read a team artifact unique ids.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @return A <code>List</code> of artifact unique id <code>UUID</code>s.
     */
    public List<UUID> readTeamArtifactUniqueIds(final Long userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_TEAM_ARTIFACT_UNIQUE_IDS);
            session.setLong(1, userId);
            session.executeQuery();
            final List<UUID> uniqueIds = new ArrayList<UUID>();
            while (session.nextResult()) {
                uniqueIds.add(session.getUniqueId("ARTIFACT_UNIQUE_ID"));
            }
            return uniqueIds;
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
     * Update an artifact draft owner.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param draftOwner
     *            The new draft owner <code>JabberId</code>.
     * @param updatedBy
     *            An updated by user id <code>JabberId</code>.
     */
    public void updateDraftOwner(final Artifact artifact,
            final User currentOwner, final User newOwner,
            final Calendar updatedOn) {
		final HypersonicSession session = openSession();
		try {
			session.prepareStatement(SQL_UPDATE_DRAFT_OWNER);
            session.setLong(1, newOwner.getLocalId());
            session.setCalendar(2, updatedOn);
			session.setLong(3, newOwner.getLocalId());
			session.setLong(4, artifact.getId());
            session.setLong(5, currentOwner.getLocalId());
            if (1 != session.executeUpdate()) {
                // TODO use a specific error code for this scenario
                throw new HypersonicException("Could not update draft owner.");
            }

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
		} finally {
            session.close();
		}
	}

    public void updateLatestVersionId(final Long artifactId,
            final Long versionId, final Long draftOwnerId,
            final Calendar updatedOn) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_LATEST_VERSION_ID);
            session.setLong(1, versionId);
            session.setLong(2, draftOwnerId);
            session.setCalendar(3, updatedOn);
            session.setLong(4, artifactId);
            session.setLong(5, draftOwnerId);

            if (1 != session.executeUpdate())
                throw panic("Could not update latest version.");
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
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
        artifact.setId(session.getLong("ARTIFACT_ID"));
        artifact.setUniqueId(UUID.fromString(session.getString("ARTIFACT_UNIQUE_ID")));
        artifact.setCreatedOn(session.getCalendar("CREATED_ON"));
        artifact.setUpdatedOn(session.getCalendar("UPDATED_ON"));
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

    /**
     * Read the local user id for the user id.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A local user id <code>Long</code>.
     */
    private Long readLocalUserId(final JabberId userId) {
        return userSql.readLocalUserId(userId);
    }
}
