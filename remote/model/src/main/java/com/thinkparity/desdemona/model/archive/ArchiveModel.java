/*
 * Generated On: Sep 04 06 04:29:31 PM
 */
package com.thinkparity.desdemona.model.archive;

import com.thinkparity.codebase.model.artifact.ArtifactVersion;

import com.thinkparity.desdemona.model.AbstractModel;
import com.thinkparity.desdemona.model.session.Session;


/**
 * <b>Title:</b>thinkParity Archive Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
public class ArchiveModel<T extends ArtifactVersion> extends AbstractModel {

	/**
     * Obtain a thinkParity archive interface.
     * 
     * @return A thinkParity archive interface.
     */
    public static <U extends ArtifactVersion> ArchiveModel<U> getModel() {
        return new ArchiveModel<U>();
    }

    /**
	 * Create an Archive interface.
	 * 
	 * @return The Archive interface.
	 */
	public static <U extends ArtifactVersion> ArchiveModel<U> getModel(final Session session) {
		return new ArchiveModel<U>(session);
	}

	/** The model implementation. */
	private final ArchiveModelImpl<T> impl;

	/** The model implementation synchronization lock. */
	private final Object implLock;

	/** Create ArchiveModel. */
    protected ArchiveModel() {
        super();
        this.impl = new ArchiveModelImpl<T>();
        this.implLock = new Object();
    }

    /**
	 * Create ArchiveModel.
	 *
	 * @param workspace
	 *		The thinkParity workspace.
	 */
	protected ArchiveModel(final Session session) {
		super();
		this.impl = new ArchiveModelImpl<T>(session);
		this.implLock = new Object();
	}

    /**
     * Archive an xmpp query.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param query
     *            An xmpp query <code>IQ</code>.
     */
    public void archive() {
    }

    /**
     * Backup an xmpp query.
     * 
     * @param query
     *            An xmpp query <code>IQ</code>.
     */
    public void backup(final T version) {
        synchronized (implLock) {
            impl.backup(version);
        }
    }

    /**
     * Start the archive.  This involves starting all of the archive
     * databases.
     *
     */
    public void start() {
        synchronized (implLock) {
            impl.start();
        }
    }

    /**
     * Stop the archive.  This involves stopping all of the archive
     * databases.
     *
     */
    public void stop() {
        synchronized (implLock) {
            impl.stop();
        }
    }

	/**
	 * Obtain the model implementation.
	 * 
	 * @return The model implementation.
	 */
	protected ArchiveModelImpl getImpl() { return impl; }

	/**
	 * Obtain the model implementation synchronization lock.
	 * 
	 * @return The model implementation synchrnoization lock.
	 */
	protected Object getImplLock() { return implLock; }
}
