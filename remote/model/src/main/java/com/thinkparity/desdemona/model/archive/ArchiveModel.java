/*
 * Generated On: Sep 04 06 04:29:31 PM
 */
package com.thinkparity.desdemona.model.archive;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.Context;

import com.thinkparity.desdemona.model.AbstractModel;
import com.thinkparity.desdemona.model.session.Session;


/**
 * <b>Title:</b>thinkParity Archive Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
public class ArchiveModel extends AbstractModel<ArchiveModelImpl> {

    /**
     * Obtain an internal thinkParity archive interface.
     * 
     * @param context
     *            A thinkParity context.
     * @return An internal thinkParity archive interface.
     */
    public static InternalArchiveModel getInternalModel(final Context context,
            final Session session) {
        return new InternalArchiveModel(context, session);
    }

    /**
     * Obtain a thinkParity archive interface.
     * 
     * @return A thinkParity archive interface.
     */
    public static ArchiveModel getModel() {
        return new ArchiveModel();
    }

	/**
	 * Create an Archive interface.
	 * 
	 * @return The Archive interface.
	 */
	public static ArchiveModel getModel(final Session session) {
		return new ArchiveModel(session);
	}

	/** Create ArchiveModel. */
    protected ArchiveModel() {
        super(new ArchiveModelImpl());
    }

	/**
	 * Create ArchiveModel.
	 *
	 * @param workspace
	 *		The thinkParity workspace.
	 */
	protected ArchiveModel(final Session session) {
		super(new ArchiveModelImpl(session));
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
     * Restore an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
    public void restore(final JabberId userId, final UUID uniqueId) {
        synchronized (getImplLock()) {
            getImpl().restore(userId, uniqueId);
        }
    }

    /**
     * Start the archive.  This involves starting all of the archive
     * databases.
     *
     */
    public void start() {
        synchronized (getImplLock()) {
            getImpl().start();
        }
    }

    /**
     * Stop the archive.  This involves stopping all of the archive
     * databases.
     *
     */
    public void stop() {
        synchronized (getImplLock()) {
            getImpl().stop();
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
    public InputStream openDocumentVersionStream(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().openDocumentVersionStream(userId, uniqueId, versionId);
        }
    }

    public List<JabberId> readTeam(final JabberId userId, final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readTeam(userId, uniqueId);
        }
    }
}
