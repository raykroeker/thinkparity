/*
 * Generated On: Jul 19 06 03:25:41 PM
 */
package com.thinkparity.desdemona.model.profile;

import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.profile.ProfileVCard;
import com.thinkparity.codebase.model.profile.SecurityCredentials;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.session.Credentials;
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
     * Add an email to a user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     */
    void addEMail(EMail email);

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
     * Determine whether or not an e-mail address is available.
     * 
     * @param email
     *            An <code>EMail</code> address.
     * @return True if the e-mail address is available.
     */
    Boolean isEmailAvailable(EMail email);

    /**
     * Read a profile.
     * 
     * @return A <code>Profile</code>.
     */
    Profile read();

    /**
     * Read all emails addresses for a model user.
     * 
     * @return A <code>List</code> of <code>ProfileEMail</code>s.
     */
    List<ProfileEMail> readEMails();

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
     * Remove an email from a user's profile.
     * 
     * @param email
     *            An <code>EMail</code>.
     */
    void removeEMail(EMail email);

    /**
     * Update a model user's profile.
     * 
     * @param vcard
     *            A <code>ProfileVCard</code>.
     */
    void update(ProfileVCard vcard);

    /**
     * Update a model user's password.
     * 
     * @param credentials
     *            The existing <code>Credentials</code>.
     * @param password
     *            The new password <code>String</code>.
     */
    void updatePassword(Credentials credentials, String newPassword);

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
