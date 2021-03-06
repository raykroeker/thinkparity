/*
 * Generated On: Jul 17 06 11:52:34 AM
 */
package com.thinkparity.ophelia.model.profile;

import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.EMailIntegrityException;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.profile.SecurityCredentials;
import com.thinkparity.codebase.model.profile.payment.PaymentInfo;
import com.thinkparity.codebase.model.profile.payment.PaymentPlanCredentials;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.user.User;
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
     * Add a profile event listener to the model.
     *
     * @param listener
     *      An instance of <code>ProfileListener</code>.
     */
    @ThinkParityTransaction(TransactionType.NEVER)
    public void addListener(final ProfileListener listener);

    public EMailReservation createEMailReservation(final EMail email);

    /**
     * Determine if the payment info is accessible.
     * 
     * @return True if payment info is accessible.
     */
    public Boolean isAccessiblePaymentInfo();

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
     * Determine if payment info is required.
     * 
     * @return True if payment info is required.
     */
    public Boolean isRequiredPaymentInfo();

    /**
     * Determine if payment info is set.
     * 
     * @return True if payment info is set.
     */
    public Boolean isSetPaymentInfo();

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
     * Read the profile backup statistics.
     * 
     * @return The <code>BackupStatistics</code>.
     */
    public BackupStatistics readBackupStatistics();

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
     * Sign up.
     * 
     */
    public void signUp(final PaymentInfo paymentInfo);

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
    public void verifyEMail(final Long emailId, final String key);

    /**
     * Create a profile.
     * 
     * @param emailReservation
     *            An <code>EMailReservation</code>.
     * @param credentials
     *            A set of <code>Credentials</code>.
     * @param profile
     *            A <code>Profile</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @param securityCredentials
     *            A set of <code>SecurityCredentials</code>.
     * @throws ReservationExpiredException
     */
    void create(EMailReservation emailReservation, Credentials credentials,
            Profile profile, EMail email,
            SecurityCredentials securityCredentials)
            throws ReservationExpiredException;

    /**
     * Create a profile.
     * 
     * @param emailReservation
     *            An <code>EMailReservation</code>.
     * @param credentials
     *            A set of <code>Credentials</code>.
     * @param profile
     *            A <code>Profile</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @param securityCredentials
     *            A set of <code>SecurityCredentials</code>.
     * @param paymentInfo
     *            A <code>PaymentInfo</code>.
     * @throws ReservationExpiredException
     */
    void create(EMailReservation emailReservation, Credentials credentials,
            Profile profile, EMail email,
            SecurityCredentials securityCredentials, PaymentInfo paymentInfo)
            throws ReservationExpiredException;

    /**
     * Create a profile.
     * 
     * @param emailReservation
     *            An <code>EMailReservation</code>.
     * @param credentials
     *            A set of <code>Credentials</code>.
     * @param profile
     *            A <code>Profile</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @param securityCredentials
     *            A set of <code>SecurityCredentials</code>.
     * @param paymentPlanCredentials
     *            A set of <code>PaymentPlanCredentials</code>.
     * @throws ReservationExpiredException
     * @throws InvalidCredentialsException
     */
    void create(EMailReservation emailReservation, Credentials credentials,
            Profile profile, EMail email,
            SecurityCredentials securityCredentials,
            PaymentPlanCredentials paymentPlanCredentials)
            throws ReservationExpiredException, InvalidCredentialsException;

    /**
     * Read whether or not invite is available.
     * 
     * @return True if it is available.
     */
    Boolean isInviteAvailable();

    /**
     * Read the profile's e-mail address.
     * 
     * @return A <code>ProfileEMail</code>.
     */
    ProfileEMail readEMail();

    /**
     * Read the profile's active flag.
     * 
     * @return A <code>Boolean</code>.
     */
    Boolean readIsActive();

    /**
     * Read the profile's username.
     * 
     * @return A <code>String</code>.
     */
    String readUsername();

    /**
     * Update an e-mail address.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @throws EMailIntegrityException
     *             if the e-mail's integrity cannot be maintained
     */
    void updateEMail(EMail email) throws EMailIntegrityException;

    /**
     * Update the payment info.
     * 
     * @param paymentInfo
     *            A <code>PaymentInfo</code>.
     */
    void updatePaymentInfo(PaymentInfo paymentInfo);
}
