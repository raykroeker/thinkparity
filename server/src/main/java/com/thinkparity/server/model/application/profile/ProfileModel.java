/*
 * Generated On: Jul 19 06 03:25:41 PM
 */
package com.thinkparity.desdemona.model.profile;

import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.profile.*;
import com.thinkparity.codebase.model.profile.payment.PaymentInfo;
import com.thinkparity.codebase.model.profile.payment.PaymentPlanCredentials;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.util.Token;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Profile Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.10
 */
public interface ProfileModel {

    /**
     * Create a profile.
     * 
     * @param product
     *            The <code>Product</code> the user is running.
     * @param release
     *            The <code>Release</code> the user is running.
     * @param usernameReservation
     *            A <code>UsernameReservation</code>.
     * @param emailReservation
     *            A <code>EMailReservation</code>.
     * @param credentials
     *            A <code>Credentials</code>.
     * @param profile
     *            A <code>Profile</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @param securityCredentials
     *            A <code>SecurityCredentials</code>.
     */
    void create(Product product, Release release,
            UsernameReservation usernameReservation,
            EMailReservation emailReservation, Credentials credentials,
            Profile profile, EMail email,
            SecurityCredentials securityCredentials);

    /**
     * Create a profile.
     * 
     * @param product
     *            The <code>Product</code> the user is running.
     * @param release
     *            The <code>Release</code> the user is running.
     * @param usernameReservation
     *            A <code>UsernameReservation</code>.
     * @param emailReservation
     *            A <code>EMailReservation</code>.
     * @param credentials
     *            A <code>Credentials</code>.
     * @param profile
     *            A <code>Profile</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @param securityCredentials
     *            A <code>SecurityCredentials</code>.
     * @param paymentInfo
     *            A <code>PaymentInfo</code>.
     */
    void create(Product product, Release release,
            UsernameReservation usernameReservation,
            EMailReservation emailReservation, Credentials credentials,
            Profile profile, EMail email,
            SecurityCredentials securityCredentials, PaymentInfo paymentInfo);

    /**
     * Create a profile.
     * 
     * @param product
     *            The <code>Product</code> the user is running.
     * @param release
     *            The <code>Release</code> the user is running.
     * @param usernameReservation
     *            A <code>UsernameReservation</code>.
     * @param emailReservation
     *            A <code>EMailReservation</code>.
     * @param credentials
     *            A <code>Credentials</code>.
     * @param profile
     *            A <code>Profile</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @param securityCredentials
     *            A <code>SecurityCredentials</code>.
     * @param paymentPlanCredentials
     *            A <code>PaymentPlanCredentials</code>.
     * @throws InvalidCredentialsException
     *             if the payment plan credentials are invalid
     */
    void create(Product product, Release release,
            UsernameReservation usernameReservation,
            EMailReservation emailReservation, Credentials credentials,
            Profile profile, EMail email,
            SecurityCredentials securityCredentials,
            PaymentPlanCredentials paymentPlanCredentials)
            throws InvalidCredentialsException;

    /**
     * Create an e-mail address reservation.
     * 
     * @param email
     *            An <code>EMail</code> address.
     * @return An <code>EMailReservation</code>.
     */
    EMailReservation createEMailReservation(EMail email);

    /**
     * Create a user's token.
     * 
     * @return A user's <code>Token</code>.
     */
    Token createToken();

    /**
     * Create a username reservation.
     * 
     * @param username
     *            A username <code>String</code>.
     * @return A <code>UsernameReservation</code>.
     */
    UsernameReservation createUsernameReservation(String username);

    /**
     * Determine if payment is accessible.
     * 
     * @return True if payment is accessible.
     */
    Boolean isAccessiblePaymentInfo();

    /**
     * Determine whether or not an e-mail address is available.
     * 
     * @param email
     *            An <code>EMail</code> address.
     * @return True if the e-mail address is available.
     */
    Boolean isEmailAvailable(EMail email);

    /**
     * Determine if payment is required.
     * 
     * @return True if payment is required.
     */
    Boolean isRequiredPaymentInfo();

    /**
     * Determine if the profile's payment has been set.
     * 
     * @return True if the payment info is set.
     */
    Boolean isSetPaymentInfo();

    /**
     * Process the invoice queue. Iterate all payment plans and generate
     * required invoices.
     * 
     */
    void processInvoiceQueue();

    /**
     * Process the pending payment queue.  Iterate all invoices and generate
     * payments.
     * 
     */
    void processPaymentQueue();

    /**
     * Read a profile.
     * 
     * @return A <code>Profile</code>.
     */
    Profile read();

    /**
     * Read a profile's e-mail address.
     * 
     * @return A <code>ProfileEMail</code>.
     */
    ProfileEMail readEMail();

    /**
     * Read all features for a user.
     * 
     * @return A <code>List&lt;Feature&gt</code>.
     */
    List<Feature> readFeatures();

    /**
     * Read a user's token.
     * 
     * @return A user's <code>Token</code>.
     */
    Token readToken();

    /**
     * Update an e-mail address.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @throws EMailReferenceException
     *             if the e-mail cannot be updated
     */
    void update(ProfileVCard vcard);

    /**
     * Update a profile.
     * 
     * @param profile
     *            A <code>Profile</code>.
     * @param paymentInfo
     *            A <code>PaymentInfo</code>.
     */
    void update(Profile profile, PaymentInfo paymentInfo);

    /**
     * Update a profile's e-mail address.
     * 
     * @param email
     *            An <code>EMail</code> address.
     */
    void updateEMail(EMail email) throws EMailIntegrityException;

    /**
     * Update a model user's password.
     * 
     * @param credentials
     *            The existing <code>Credentials</code>.
     * @param password
     *            The new password <code>String</code>.
     * @throws InvalidCredentialsException
     *             if the credentials are invalid
     */
    void updatePassword(Credentials credentials, String newPassword)
            throws InvalidCredentialsException;

    /**
     * Update payment info for the profile.
     * 
     * @param profile
     *            A <code>Profile</code>.
     * @param paymentInfo
     *            A <code>PaymentInfo</code>.
     */
    void updatePaymentInfo(PaymentInfo paymentInfo);

    /**
     * Update the model user's product release.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     */
    void updateProductRelease(Product product, Release release);

    /**
     * Verify an email in a model user's profile. This includes generation of
     * incoming e-mail invitations for all outgoing e-mail invitations for the
     * e-mail address.
     * 
     * @param email
     *            An <code>EMail</code> address.
     * @param key
     *            A verification code <code>String</code>.
     */
    void verifyEMail(EMail email, String key);
}
