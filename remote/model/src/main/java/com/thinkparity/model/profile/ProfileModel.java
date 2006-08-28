/*
 * Generated On: Jul 19 06 03:25:41 PM
 */
package com.thinkparity.model.profile;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.model.AbstractModel;
import com.thinkparity.server.model.ParityServerModelException;
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
     * Add an email to a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     */
    public void addEmail(final JabberId userId, final EMail email) {
        synchronized (implLock) {
            impl.addEmail(userId, email);
        }
    }

    /**
     * Read a profile.
     * 
     * @param jabberId
     *            A jabber id.
     * @return A profile.
     */
    public Profile read(final JabberId jabberId) throws ParityServerModelException {
        synchronized(implLock) { return impl.read(jabberId); }
    }

    public List<ProfileEMail> readEMails(final JabberId userId) {
        synchronized (implLock) {
            return impl.readEMails(userId);
        }
    }

    /**
     * Remove an email from a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     */
    public void removeEmail(final JabberId userId, final EMail email) {
        synchronized (implLock) {
            impl.removeEmail(userId, email);
        }
    }

    /**
     * Verify an email in a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     * @param key
     *            A verification code <code>String</code>.
     */
    public void verifyEmail(final JabberId userId, final EMail email,
            final String key) {
        synchronized (implLock) {
            impl.verifyEmail(userId, email, key);
        }
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
