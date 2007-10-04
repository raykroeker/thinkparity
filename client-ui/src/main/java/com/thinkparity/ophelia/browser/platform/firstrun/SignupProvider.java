/**
 * Created On: Apr 3, 2007 3:06:03 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.SecurityCredentials;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.profile.payment.PaymentInfo;
import com.thinkparity.codebase.model.profile.payment.PaymentPlanCredentials;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.profile.ReservationExpiredException;

import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;

/**
 * <b>Title:</b>thinkParity Ophelia UI Platform First Run Provider<br>
 * <b>Description:</b>A facade into the model for the first run wizard.<br>
 * 
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public final class SignupProvider extends ContentProvider {

    /**
     * Create SignupProvider.
     * 
     * @param profileModel
     *            An instance of <code>ProfileModel</code>.
     */
    public SignupProvider(final ProfileModel profileModel) {
        super(profileModel);
    }

    /**
     * Create an e-mail address reservation.
     * 
     * @param email
     *            An <code>EMail</code> address.
     * @return An <code>EMailReservation</code> or null if the reservation cannot be
     *         created.
     */
    public EMailReservation createEMailReservation(final EMail email) {
        return profileModel.createEMailReservation(email);
    }

    /**
     * Create a profile.
     * 
     * @param usernameReservation
     *            A <code>UsernameReservation</code>.
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
    public void createProfile(final UsernameReservation usernameReservation,
            final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final SecurityCredentials securityCredentials)
            throws ReservationExpiredException {
        profileModel.create(usernameReservation, emailReservation, credentials,
                profile, email, securityCredentials);
    }

    /**
     * Create a profile.
     * 
     * @param usernameReservation
     *            A <code>UsernameReservation</code>.
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
    public void createProfile(final UsernameReservation usernameReservation,
            final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final SecurityCredentials securityCredentials,
            final PaymentInfo paymentInfo) throws ReservationExpiredException {
        profileModel.create(usernameReservation, emailReservation, credentials,
                profile, email, securityCredentials, paymentInfo);
    }

    /**
     * Create a profile.
     * 
     * @param usernameReservation
     *            A <code>UsernameReservation</code>.
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
     * @param paymentPlanCredentials
     *            A <code>PaymentPlanCredentials</code>.
     * @throws ReservationExpiredException
     *             if either the username/e-mail reservation have expired
     * @throws InvalidCredentialsException
     *             if the plan credentials are invalid
     */
    public void createProfile(final UsernameReservation usernameReservation,
            final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final SecurityCredentials securityCredentials,
            final PaymentPlanCredentials paymentPlanCredentials)
            throws ReservationExpiredException, InvalidCredentialsException {
        profileModel.create(usernameReservation, emailReservation, credentials,
                profile, email, securityCredentials, paymentPlanCredentials);
    }

    /**
     * Create a username reservation.
     * 
     * @param username
     *            A username <code>String</code>.
     * @return A <code>UsernameReservation</code> or null if the reservation
     *         cannot be created.
     */
    public UsernameReservation createUsernameReservation(final String username) {
        return profileModel.createUsernameReservation(username);
    }

    /**
     * Determine if the payment info is required.
     * 
     * @return True if the payement info is required.
     */
    public Boolean isRequiredPaymentInfo() {
        return profileModel.isRequiredPaymentInfo();
    }

    /**
     * Determine if the payment info is set.
     * 
     * @return True if the payement info is set.
     */
    public Boolean isSetPaymentInfo() {
        return profileModel.isSetPaymentInfo();
    }

    /**
     * Read the available features.
     * 
     * @return A <code>List</code> of <code>Feature</code>s.
     */
    public List<Feature> readFeatures() {
        return profileModel.readFeatures();
    }
}
