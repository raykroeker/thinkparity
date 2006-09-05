/*
 * Generated On: Sep 04 06 04:29:31 PM
 */
package com.thinkparity.model.archive;

import org.xmpp.packet.IQ;

import com.thinkparity.server.model.AbstractModel;
import com.thinkparity.server.model.session.Session;

/**
 * <b>Title:</b>thinkParity Archive Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
public class ArchiveModel extends AbstractModel {

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

	/** The model implementation. */
	private final ArchiveModelImpl impl;

	/** The model implementation synchronization lock. */
	private final Object implLock;

	/** Create ArchiveModel. */
    protected ArchiveModel() {
        super();
        this.impl = new ArchiveModelImpl();
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
		this.impl = new ArchiveModelImpl(session);
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
    public void backup(final IQ query) {
        synchronized (implLock) {
            impl.backup(query);
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
