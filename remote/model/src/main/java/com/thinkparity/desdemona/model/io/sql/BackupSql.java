/*
 * Created On:  9-Mar-07 3:27:59 PM
 */
package com.thinkparity.desdemona.model.io.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Email SQL Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class BackupSql extends AbstractSql {

    /** Sql to create a user archive relationship. */
    private static final String SQL_CREATE_USER_ARCHIVE =
        new StringBuilder("insert into TPSD_BACKUP_USER_ARCHIVE ")
        .append("(USER_ID,ARTIFACT_ID) ")
        .append("values(?,?)")
        .toString();

    /** Sql to delete a user archive relationship. */
    private static final String SQL_DELETE_USER_ARCHIVE =
        new StringBuilder("delete from TPSD_BACKUP_USER_ARCHIVE ")
        .append("where USER_ID=? and ARTIFACT_ID=?")
        .toString();

    /** Sql to read an artifact archive count by the artifact. */
    private static final String SQL_READ_DOES_EXIST_ARCHIVE_ARTIFACT =
        new StringBuilder("select count(BUA.ARTIFACT_ID) \"ARCHIVE_COUNT\" ")
        .append("from TPSD_BACKUP_USER_ARCHIVE BUA ")
        .append("where BUA.ARTIFACT_ID=?")
        .toString();

    /** Sql to read an artifact archive count by the archive primary key. */
    private static final String SQL_READ_DOES_EXIST_ARCHIVE_PK =
        new StringBuilder("select count(BUA.ARTIFACT_ID) \"ARCHIVE_COUNT\" ")
        .append("from TPSD_BACKUP_USER_ARCHIVE BUA ")
        .append("where BUA.USER_ID=? and BUA.ARTIFACT_ID=?")
        .toString();

    /** Sql to read a user archive relationship. */
    private static final String SQL_READ_USER_ARCHIVE =
        new StringBuilder("select A.ARTIFACT_UNIQUE_ID ")
        .append("from TPSD_BACKUP_USER_ARCHIVE BUA ")
        .append("inner join TPSD_ARTIFACT A on A.ARTIFACT_ID=BUA.ARTIFACT_ID ")
        .append("where BUA.USER_ID=?")
        .toString();

    /**
     * Create BackupSql.
     *
     */
    public BackupSql() {
        super();
    }

    /**
     * Create a user artifact archive relationship.
     * 
     * @param user
     *            A <code>User</code>.
     * @param artifact
     *            An <code>Artifact</code>.
     */
    public void createArchive(final User user, final Artifact artifact) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_USER_ARCHIVE);
            session.setLong(1, user.getLocalId());
            session.setLong(2, artifact.getId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not restore artifact for user.",
                        artifact.getId(), user.getId());
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Delete an archive relationship between the user and an artifact.
     * 
     * @param user
     *            A <code>User</code>.
     * @param artifact
     *            An <code>Artifact</code>.
     */
    public void deleteArchive(final User user, final Artifact artifact) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_USER_ARCHIVE);
            session.setLong(1, user.getLocalId());
            session.setLong(2, artifact.getId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not restore artifact for user.",
                        artifact.getId(), user.getId());
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Determine if an archive relationship exists for a user and an artifact.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @return True if a relationship exists.
     */
    public Boolean doesExistArchive(final Artifact artifact) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_DOES_EXIST_ARCHIVE_ARTIFACT);
            session.setLong(1, artifact.getId());
            session.executeQuery();
            if (session.nextResult()) {
                final int archiveCount = session.getInteger("ARCHIVE_COUNT");
                if (0 == archiveCount) {
                    return Boolean.FALSE;
                } else if (0 < archiveCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine archive existence.");
                }
            } else {
                throw new HypersonicException("Could not determine archive existence.");
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Determine if an archive relationship exists for a user and an artifact.
     * 
     * @param user
     *            A <code>User</code>.
     * @param artifact
     *            An <code>Artifact</code>.
     * @return True if a relationship exists.
     */
    public Boolean doesExistArchive(final User user, final Artifact artifact) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_DOES_EXIST_ARCHIVE_PK);
            session.setLong(1, user.getLocalId());
            session.setLong(2, artifact.getId());
            session.executeQuery();
            if (session.nextResult()) {
                final int archiveCount = session.getInteger("ARCHIVE_COUNT");
                if (0 == archiveCount) {
                    return Boolean.FALSE;
                } else if (1 == archiveCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine archive existence.");
                }
            } else {
                throw new HypersonicException("Could not determine archive existence.");
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }
    
    /**
     * Read a list of archived artifact unique ids.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>List</code> of artifact unique id <code>UUID</code>s.
     */
    public List<UUID> readArchive(final User user) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_USER_ARCHIVE);
            session.setLong(1, user.getLocalId());
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
}
