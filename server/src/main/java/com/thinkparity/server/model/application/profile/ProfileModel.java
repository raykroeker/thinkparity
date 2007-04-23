/*
 * Generated On: Jul 19 06 03:25:41 PM
 */
package com.thinkparity.desdemona.model.profile;

import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.TemporaryCredentials;
import com.thinkparity.codebase.model.util.Token;

import com.thinkparity.desdemona.model.annotation.ThinkParityAuthenticate;
import com.thinkparity.desdemona.util.AuthenticationType;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Profile Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.10
 */
public interface ProfileModel {

    /**
     * Add an email to a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     */
    @ThinkParityAuthenticate(AuthenticationType.USER)
    public void addEmail(final JabberId userId, final EMail email);

	@ThinkParityAuthenticate(AuthenticationType.SYSTEM)
    public void create(final JabberId userId,
            final UsernameReservation usernameReservation,
            final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final String securityQuestion,
            final String securityAnswer);

    public TemporaryCredentials createCredentials(final String profileKey,
            final String securityAnswer, final Calendar createdOn);

    @ThinkParityAuthenticate(AuthenticationType.SYSTEM)
    public EMailReservation createEMailReservation(final JabberId userId,
            final EMail email, final Calendar reservedOn);

    /**
     * Create a user's token.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A user's <code>Token</code>.
     */
    @ThinkParityAuthenticate(AuthenticationType.USER)
    public Token createToken(final JabberId userId);

    @ThinkParityAuthenticate(AuthenticationType.SYSTEM)
    public UsernameReservation createUsernameReservation(final JabberId userId,
            final String username, final Calendar reservedOn);

    /**
     * Determine whether or not an e-mail address is available.
     * 
     * @param userId
     *            A user Id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @return True if the e-mail address is available.
     */
    @ThinkParityAuthenticate(AuthenticationType.USER)
    public Boolean isEmailAvailable(final JabberId userId, final EMail email);

    /**
     * Read a profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>Profile</code>.
     */
    @ThinkParityAuthenticate(AuthenticationType.USER)
    public Profile read(final JabberId userId);

    /**
     * Read all emails addresses for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List</code> of <code>ProfileEMail</code>s.
     */
    @ThinkParityAuthenticate(AuthenticationType.USER)
    public List<ProfileEMail> readEMails(final JabberId userId);

    /**
     * Read all features for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Feature&gt</code>.
     */
    @ThinkParityAuthenticate(AuthenticationType.SYSTEM)
    public List<Feature> readFeatures(final JabberId userId);

    /**
     * Read a user's security question.
     * 
     * @param profileKey
     *            A profile key <code>String</code>.
     * @return A users's security question <code>String</code>.
     */
    public String readSecurityQuestion(final String profileKey);

    /**
     * Read a user's token.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A user's <code>Token</code>.
     */
    public Token readToken(final JabberId userId);

    /**
     * Remove an email from a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     */
    @ThinkParityAuthenticate(AuthenticationType.USER)
    public void removeEmail(final JabberId userId, final EMail email);

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
    @ThinkParityAuthenticate(AuthenticationType.USER)
    public void update(final JabberId userId, final ProfileVCard vcard);

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
    @ThinkParityAuthenticate(AuthenticationType.USER)
    public void updatePassword(final JabberId userId,
            final Credentials credentials, final String newPassword);

    /**
     * Update a user's password.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param credentials
     *            A set of <code>TemporaryCredentials</code>.
     * @param newPassword
     *            The new password <code>String</code>.
     */
    public void updatePassword(final JabberId userId,
            final TemporaryCredentials credentials, final String newPassword);

    /**
     * Verify an email in a user's profile. This includes generation of incoming
     * e-mail invitations for all outgoing e-mail invitations for the e-mail
     * address.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @param key
     *            A verification code <code>String</code>.
     */
    @ThinkParityAuthenticate(AuthenticationType.USER)
    public void verifyEmail(final JabberId userId, final EMail email,
            final String key);
}
