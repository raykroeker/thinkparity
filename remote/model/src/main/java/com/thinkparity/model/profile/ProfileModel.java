/*
 * Generated On: Jul 19 06 03:25:41 PM
 */
package com.thinkparity.model.profile;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.AbstractModel;
import com.thinkparity.model.session.Session;


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
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>Profile</code>.
     */
    public Profile read(final JabberId userId) {
        synchronized (implLock) {
            return impl.read(userId);
        }
    }

    /**
     * Read all emails addresses for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;EMail&gt;</code>.
     */
    public List<EMail> readEmails(final JabberId userId) {
        synchronized (implLock) {
            return impl.readEmails(userId);
        }
    }

    /**
     * Read all features for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Feature&gt</code>.
     */
    public List<Feature> readFeatures(final JabberId userId) {
        synchronized (implLock) {
            return impl.readFeatures(userId);
        }
    }

    /**
     * Read a user's security question.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A users's security question <code>String</code>.
     */
    public String readSecurityQuestion(final JabberId userId) {
        synchronized (implLock) {
            return impl.readSecurityQuestion(userId);
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
     * Reset a user's credentials.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param securityAnswer
     *            The security question answer <code>String</code>.
     * @return The user's new password.
     */
    public String resetPassword(final JabberId userId,
            final String securityAnswer) {
        synchronized (implLock) {
            return impl.resetCredentials(userId, securityAnswer);
        }
    }

    /**
     * Update a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param name
     *            A user's name <code>String</code>.
     * @param organization
     *            A user's organization <code>String</code>.
     * @param title
     *            A user's title <code>String</code>.
     */
    public void update(final JabberId userId, final String name,
            final String organization, final String title) {
        synchronized (implLock) {
            impl.update(userId, name, organization, title);
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
}
