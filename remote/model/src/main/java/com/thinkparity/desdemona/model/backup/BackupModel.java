/*
 * Generated On: Oct 04 06 10:14:14 AM
 */
package com.thinkparity.desdemona.model.backup;

import java.io.InputStream;
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
    public InputStream openDocumentVersionStream(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().openDocumentVersionStream(userId, uniqueId, versionId);
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
}
