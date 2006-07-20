/*
 * Generated On: Jul 19 06 03:25:41 PM
 */
package com.thinkparity.model.profile;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.model.AbstractModel;
import com.thinkparity.server.model.session.Session;

/**
 * <b>Title:</b>thinkParity Profile Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
public class ProfileModel extends AbstractModel {

	/**
	 * Create a Profile interface.
	 * 
	 * @return The Profile interface.
	 */
	public static ProfileModel getModel(final Session session) {
		return new ProfileModel(session);
	}

	/** The model implementation. */
	private final ProfileModelImpl impl;

	/** The model implementation synchronization lock. */
	private final Object implLock;

	/**
	 * Create ProfileModel.
	 *
	 * @param workspace
	 *		The thinkParity workspace.
	 */
	protected ProfileModel(final Session session) {
		super();
		this.impl = new ProfileModelImpl(session);
		this.implLock = new Object();
	}

    /**
     * Read a profile.
     * 
     * @param jabberId
     *            A jabber id.
     * @return A profile.
     */
    public Profile read(final JabberId jabberId) {
        synchronized(implLock) { return impl.read(jabberId); }
    }

	/**
	 * Obtain the model implementation.
	 * 
	 * @return The model implementation.
	 */
	protected ProfileModelImpl getImpl() { return impl; }

	/**
	 * Obtain the model implementation synchronization lock.
	 * 
	 * @return The model implementation synchrnoization lock.
	 */
	protected Object getImplLock() { return implLock; }
}
