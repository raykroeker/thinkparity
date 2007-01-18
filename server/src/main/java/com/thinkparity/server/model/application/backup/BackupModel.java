/*
 * Generated On: Oct 04 06 10:14:14 AM
 */
package com.thinkparity.desdemona.model.backup;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;

import com.thinkparity.desdemona.model.AbstractModel;
import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b>thinkParity Backup Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
public class BackupModel extends AbstractModel<BackupModelImpl> {

    /**
     * Obtain a thinkParity Backup interface.
     * 
     * @return A thinkParity Backup interface.
     */
    public static InternalBackupModel getInternalModel(final Context context,
            final Session session) {
        return new InternalBackupModel(context, session);
    }

    /**
     * Obtain a thinkParity Backup interface.
     * 
     * @return A thinkParity Backup interface.
     */
    public static BackupModel getModel(final Session session) {
        return new BackupModel(session);
    }

    /**
	 * Create BackupModel.
	 *
	 * @param workspace
	 *		The thinkParity workspace.
	 */
	protected BackupModel(final Session session) {
		super(new BackupModelImpl(session));
	}

    /**
     * Archive an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
    public void archive(final JabberId userId, final UUID uniqueId) {
        synchronized (getImplLock()) {
            getImpl().archive(userId, uniqueId);
        }
    }

    /**
     * Delete an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
    public void delete(final JabberId userId, final UUID uniqueId) {
        synchronized (getImplLock()) {
            getImpl().delete(userId, uniqueId);
        }
    }

    /**
     * Open a document version's input stream.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A document unique id <code>UUID</code>.
     * @param versionId
     *            A document version id <code>Long</code>.
     * @return An <code>InputStream</code>.
     */
    public void createStream(final JabberId userId, final String streamId,
            final UUID uniqueId, final Long versionId) {
        synchronized (getImplLock()) {
            getImpl().createStream(userId, streamId, uniqueId, versionId);
        }
    }

	/**
     * Read the team for a backup artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A <code>List&lt;JabberId&gt;</code>.
     */
    public List<JabberId> readTeam(final JabberId userId, final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readTeam(userId, uniqueId);
        }
    }

    /**
     * Restore an artifact. This will simply remove the archived flag within the
     * backup.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>Long</code>.
     */
    public void restore(final JabberId userId, final UUID uniqueId) {
        synchronized (getImplLock()) {
            getImpl().restore(userId, uniqueId);
        }
    }
}
