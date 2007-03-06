/*
 * Generated On: Jul 19 06 03:25:41 PM
 */
package com.thinkparity.desdemona.model.profile;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.user.Token;

import com.thinkparity.desdemona.model.AbstractModel;
import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b>thinkParity Profile Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
public class ProfileModel extends AbstractModel<ProfileModelImpl> {

    /**
     * Create a Profile interface.
     * 
     * @return The Profile interface.
     */
    public static ProfileModel getModel(final Session session) {
        return new ProfileModel(session);
    }

    /**
     * Create ProfileModel.
     *
     * @param workspace
     *      The thinkParity workspace.
     */
    protected ProfileModel(final Session session) {
        super(new ProfileModelImpl(session));
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
        synchronized (getImplLock()) {
            getImpl().addEmail(userId, email);
        }
    }

    /**
     * Create a user's token.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A user's <code>Token</code>.
     */
    public Token createToken(final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().createToken(userId);
        }
    }

    public Boolean isEmailAvailable(final JabberId userId, final EMail email) {
        synchronized (getImplLock()) {
            return getImpl().isEmailAvailable(userId, email);
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
        synchronized (getImplLock()) {
            return getImpl().read(userId);
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
        synchronized (getImplLock()) {
            return getImpl().readEmails(userId);
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
        synchronized (getImplLock()) {
            return getImpl().readFeatures(userId);
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
        synchronized (getImplLock()) {
            return getImpl().readSecurityQuestion(userId);
        }
    }

    /**
     * Read a user's token.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A user's <code>Token</code>.
     */
    public Token readToken(final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().readToken(userId);
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
        synchronized (getImplLock()) {
            getImpl().removeEmail(userId, email);
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
        synchronized (getImplLock()) {
            return getImpl().resetCredentials(userId, securityAnswer);
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
    public void update(final JabberId userId, final ProfileVCard vcard) {
        synchronized (getImplLock()) {
            getImpl().update(userId, vcard);
        }
    }

    /**
     * Update a user's password.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param password
     *            The existing password <code>String</code>.
     * @param newPassword
     *            The new password <code>String</code>.
     */
    public void updatePassword(final JabberId userId, final String password,
            final String newPassword) {
        synchronized (getImplLock()) {
            getImpl().updatePassword(userId, password, newPassword);
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
        synchronized (getImplLock()) {
            getImpl().verifyEmail(userId, email, key);
        }
    }
}
