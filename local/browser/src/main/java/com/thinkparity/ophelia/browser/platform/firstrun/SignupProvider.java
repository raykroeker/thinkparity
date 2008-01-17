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
import com.thinkparity.codebase.model.profile.payment.PaymentInfo;
import com.thinkparity.codebase.model.profile.payment.PaymentPlanCredentials;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.profile.ReservationExpiredException;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.configuration.ProxyConfiguration;

import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;

/**
 * <b>Title:</b>thinkParity Ophelia UI Platform First Run Provider<br>
 * <b>Description:</b>A facade into the model for the first run wizard.<br>
 * 
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public final class SignupProvider extends ContentProvider {

    /** The thinkParity <code>Workspace</code>. */
    private final Workspace workspace;

    /**
     * Create SignupProvider.
     * 
     * @param profileModel
     *            An instance of <code>ProfileModel</code>.
     * @param workspace
     *            The <code>Workspace</code>.        
     */
    public SignupProvider(final ProfileModel profileModel,
            final Workspace workspace) {
        super(profileModel);
        this.workspace = workspace;
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
    public void createProfile(final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final SecurityCredentials securityCredentials)
            throws ReservationExpiredException {
        profileModel.create(emailReservation, credentials,
                profile, email, securityCredentials);
    }

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
    public void createProfile(final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final SecurityCredentials securityCredentials,
            final PaymentInfo paymentInfo) throws ReservationExpiredException {
        profileModel.create(emailReservation, credentials,
                profile, email, securityCredentials, paymentInfo);
    }

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
     * @param paymentPlanCredentials
     *            A <code>PaymentPlanCredentials</code>.
     * @throws ReservationExpiredException
     *             if the e-mail reservation have expired
     * @throws InvalidCredentialsException
     *             if the plan credentials are invalid
     */
    public void createProfile(final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final SecurityCredentials securityCredentials,
            final PaymentPlanCredentials paymentPlanCredentials)
            throws ReservationExpiredException, InvalidCredentialsException {
        profileModel.create(emailReservation, credentials,
                profile, email, securityCredentials, paymentPlanCredentials);
    }

    /**
     * Delete the proxy configuration.
     */
    public void deleteProxyConfiguration() {
        workspace.getConfiguration().deleteProxyConfiguration();
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
     * Determine if the proxy configuration is set.
     * 
     * @return true if the proxy configuration is set.
     */
    public Boolean isSetProxyConfiguration() {
        return workspace.getConfiguration().isSetProxyConfiguration();
    }

    /**
     * Read the available features.
     * 
     * @return A <code>List</code> of <code>Feature</code>s.
     */
    public List<Feature> readFeatures() {
        return profileModel.readFeatures();
    }

    /**
     * Update the proxy configuration.
     * 
     * @param configuration
     *            A <code>ProxyConfiguration</code>.
     */
    public void updateProxyConfiguration(final ProxyConfiguration configuration) {
        workspace.getConfiguration().updateProxyConfiguration(configuration);
    }
}
