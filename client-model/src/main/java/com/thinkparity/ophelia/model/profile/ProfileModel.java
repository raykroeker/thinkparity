/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.ophelia.model.profile;

import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.events.ProfileListener;

/**
 * <b>Title:</b>thinkParity Profile Model<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface ProfileModel {

	/**
     * Add an email to the profile.
     * 
     * @param email
     *            An <code>EMail</code>.
     */
	public void addEmail(final EMail email);

    /**
     * Add a profile event listener to the model.
     *
     * @param listener
     *      An instance of <code>ProfileListener</code>.
     */
    @ThinkParityTransaction(TransactionType.NEVER)
    public void addListener(final ProfileListener listener);

    /**
     * Determine whether or not an e-mail address is available.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @return True if the address is not in use.
     */
    public Boolean isAvailable(final EMail email);

    /**
     * Determine if sign up is available.
     * 
     * @return True if sign up is available.
     */
    public Boolean isSignUpAvailable();

    /**
     * Read the logged in user's profile.
     * 
     * @return A profile.
     */
    public Profile read();

    /**
     * Read a profile email.
     * 
     * @param emailId
     *            An email id <code>Long</code>.
     * @return A <code>ProfileEmail</code>.
     */
    public ProfileEMail readEmail(final Long emailId);

    /**
     * Read a list of profile email addresses.
     * 
     * @return A list of email addresses.
     */
    public List<ProfileEMail> readEmails();

	/**
     * Read the security question.
     * 
     * @return A security question.
     */
    public String readSecurityQuestion();

    /**
     * Remove an email.
     * 
     * @param emailId
     *            An email id <code>Long</code>.
     */
	public void removeEmail(final Long emailId);

    /**
     * Remove a profile event listener from the model.
     *
     * @param listener
     *      An instance of <code>ProfileListener</code>.
     */
    @ThinkParityTransaction(TransactionType.NEVER)
    public void removeListener(final ProfileListener listener);

    /**
     * Reset the user's password.
     * 
     * @param securityAnswer
     *            The security question answer <code>String</code>.
     */
	public void resetPassword(final String securityAnswer);

    /**
     * Update the logged in user's profile.
     * 
     * @param profile
     *            A profile.
     */
    public void update(final Profile profile);

    /**
     * Update the profile password.
     * 
     * @param password
     *            The current password <code>String</code>.
     * @param newPassword
     *            The new password <code>String</code>.
     * @throws InvalidCredentialsException
     *             if the password does not match the existing password
     */
    public void updatePassword(final String password, final String newPassword)
            throws InvalidCredentialsException;

    /**
     * Determine the validity of the credentials.
     * 
     * @param credentials
     *            The <code>Credentials</code> to validate.
     */
    public void validateCredentials(final Credentials credentials)
            throws InvalidCredentialsException;

    /**
     * Verify an email.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @param key
     *            A verification key <code>String</code>.
     */
    public void verifyEmail(final Long emailId, final String key);
}
