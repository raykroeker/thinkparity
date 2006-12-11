/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.ophelia.model.profile;

import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Profile Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 
 */
public class ProfileModel extends AbstractModel<ProfileModelImpl> {

    /**
	 * Create a Profile interface.
	 * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @param context
	 *            A thinkParity internal context.
	 * @return The internal Profile interface.
	 */
	public static InternalProfileModel getInternalModel(final Context context,
            final Environment environment, final Workspace workspace) {
		return new InternalProfileModel(context, environment, workspace);
	}

    /**
	 * Create a Profile interface.
	 * 
     * @param workspace
     *      A thinkParity <code>Workspace</code>.
	 * @return The Profile interface.
	 */
	public static ProfileModel getModel(final Environment environment,
            final Workspace workspace) {
		return new ProfileModel(environment, workspace);
	}

    /**
	 * Create ProfileModel.
	 *
	 * @param workspace
	 *		The thinkParity workspace.
	 */
	protected ProfileModel(final Environment environment,
            final Workspace workspace) {
		super(new ProfileModelImpl(environment, workspace));
	}

	/**
     * Add an email to the profile.
     * 
     * @param email
     *            An <code>EMail</code>.
     */
	public void addEmail(final EMail email) {
        synchronized (getImplLock()) {
            getImpl().addEmail(email);
        }
    }

    /**
     * Create the user's profile locally.
     *
     */
    public Profile create() {
        synchronized (getImplLock()) {
            return getImpl().create();
        }
    }

    /**
     * Determine if sign up is available.
     * 
     * @return True if sign up is available.
     */
    public Boolean isSignUpAvailable() {
        synchronized (getImplLock()) {
            return getImpl().isSignUpAvailable();
        }
    }

    /**
     * Read the logged in user's profile.
     * 
     * @return A profile.
     */
    public Profile read() {
        synchronized (getImplLock()) {
            return getImpl().read();
        }
    }

    /**
     * Read the user's credentials.
     * 
     * @return The user's credentials.
     */
    public Credentials readCredentials() {
        synchronized (getImplLock()) {
            return getImpl().readCredentials();
        }
    }

    /**
     * Read a profile email.
     * 
     * @param emailId
     *            An email id <code>Long</code>.
     * @return A <code>ProfileEmail</code>.
     */
    public ProfileEMail readEmail(final Long emailId) {
        synchronized (getImplLock()) {
            return getImpl().readEmail(emailId);
        }
    }

    /**
     * Read a list of profile email addresses.
     * 
     * @return A list of email addresses.
     */
    public List<ProfileEMail> readEmails() {
        synchronized (getImplLock()) {
            return getImpl().readEmails();
        }
    }

	/**
     * Read the security question.
     * 
     * @return A security question.
     */
    public String readSecurityQuestion() {
        synchronized (getImplLock()) {
            return getImpl().readSecurityQuestion();
        }
    }

    /**
     * Remove an email.
     * 
     * @param emailId
     *            An email id <code>Long</code>.
     */
	public void removeEmail(final Long emailId) {
        synchronized (getImplLock()) {
            getImpl().removeEmail(emailId);
        }
    }

    /**
     * Reset the user's password.
     * 
     * @param securityAnswer
     *            The security question answer <code>String</code>.
     */
	public void resetPassword(final String securityAnswer) {
        synchronized (getImplLock()) {
            getImpl().resetPassword(securityAnswer);
        }
    }

    /**
     * Update the logged in user's profile.
     * 
     * @param profile
     *            A profile.
     */
    public void update(final Profile profile) {
        synchronized (getImplLock()) {
            getImpl().update(profile);
        }
    }

    /**
     * Update the profile password.
     * 
     * @param password
     *            The current password <code>String</code>.
     * @param newPassword
     *            The new password <code>String</code>.
     */
    public void updatePassword(final String password,
            final String newPassword) {
        synchronized (getImplLock()) {
            getImpl().updatePassword(password, newPassword);
        }
    }

    /**
     * Verify an email.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @param key
     *            A verification key <code>String</code>.
     */
    public void verifyEmail(final Long emailId, final String key) {
        synchronized (getImplLock()) {
            getImpl().verifyEmail(emailId, key);
        }
    }
}
