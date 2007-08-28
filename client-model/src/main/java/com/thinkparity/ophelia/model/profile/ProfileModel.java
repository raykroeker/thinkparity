/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.ophelia.model.profile;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityConcurrency;
import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.profile.SecurityCredentials;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.concurrent.Lock;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.annotation.ThinkParityOnline;
import com.thinkparity.ophelia.model.events.ProfileListener;

/**
 * <b>Title:</b>thinkParity Profile Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.26
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface ProfileModel {

    /**
     * Add an email to the profile.
     * 
     * @param email
     *            An <code>EMail</code>.
     */
    @ThinkParityOnline
	public void addEmail(final EMail email);

    /**
     * Add a profile event listener to the model.
     *
     * @param listener
     *      An instance of <code>ProfileListener</code>.
     */
    @ThinkParityTransaction(TransactionType.NEVER)
    public void addListener(final ProfileListener listener);

    public void create(final UsernameReservation usernameReservation,
            final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final SecurityCredentials securityCredentials)
            throws ReservationExpiredException;

    public EMailReservation createEMailReservation(final EMail email);

    public UsernameReservation createUsernameReservation(final String username);

    /**
     * Determine whether or not an e-mail address is available.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @return True if the address is not in use.
     */
    @ThinkParityOnline
    public Boolean isAvailable(final EMail email);

    /**
     * Determine if the backup feature is available.
     * 
     * @return True if backup is available for the profile.
     */
    public Boolean isBackupEnabled();

    /**
     * Determine if the core feature is available.
     * 
     * @return True if core is available for the profile.
     */
    public Boolean isCoreEnabled();

    /**
     * Determine whether or not the invite user interface is enabled.
     * 
     * @return True if the invite user interface is enabled.
     */
    public Boolean isInviteAvailable(final User user);

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
    @ThinkParityConcurrency(Lock.LOCAL_READ)
    public Profile read();

    /**
     * Read the profile backup statistics.
     * 
     * @return The <code>BackupStatistics</code>.
     */
    public BackupStatistics readBackupStatistics();

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
     * Read a list of available features.
     * 
     * @return A <code>List</code> of <code>Feature</code>s.
     */
    public List<Feature> readFeatures();

    /**
     * Read the profile statistics.
     * 
     * @return The <code>Statistics</code>.
     */
    public Statistics readStatistics();

    /**
     * Remove an email.
     * 
     * @param emailId
     *            An email id <code>Long</code>.
     */
    @ThinkParityOnline
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
     * Search for profiles.
     * 
     * @param expression
     *            A search expression <code>String</code>.
     * @return A <code>List</code> of <code>JabberId</code>s.
     */
    public List<JabberId> search(final String expression);

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
     * @param credentials
     *            The current <code>Credentials</code>.
     * @param newPassword
     *            The new password <code>String</code>.
     * @throws InvalidCredentialsException
     *             if the existing credentials are not valid
     */
    public void updatePassword(final Credentials credentials,
            final String newPassword) throws InvalidCredentialsException;

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
    @ThinkParityOnline
    public void verifyEmail(final Long emailId, final String key);

    /**
     * Read the profile's username.
     * 
     * @return A <code>String</code>.
     */
    String readUsername();
}
