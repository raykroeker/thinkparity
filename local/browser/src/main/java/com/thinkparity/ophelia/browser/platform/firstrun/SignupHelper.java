/**
 * Created On: 23-Apr-07 11:02:32 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import java.util.ArrayList;
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

import com.thinkparity.ophelia.model.profile.ReservationExpiredException;

import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.firstrun.SignupData.DataKey;

/**
 * <b>Title:</b>thinkParity Ophelia UI Platform First Run Helper<br>
 * <b>Description:</b>Common utilities for the first run wizard.<br>
 * 
 * @author robert@thinkparity.com
 * @version $Revision$
 */
final class SignupHelper {

    /** Input <code>Data</code>. */
    private final Data input;

    /** A <code>SignupProvider</code>. */
    private final SignupProvider signupProvider;

    /**
     * Create SignupHelper.
     * 
     * @param signupProvider
     *            The <code>SignupProvider</code>.
     * @param input
     *            The input <code>Data</code>.
     */
    public SignupHelper(final SignupProvider signupProvider, final Data input) {
        super();
        this.signupProvider = signupProvider;
        this.input = input;
    }

    /**
     * Create a guest profile.
     * 
     */
    public void createGuestProfile() throws ReservationExpiredException {
        final EMailReservation emailReservation = (EMailReservation) ((Data) input).get(SignupData.DataKey.EMAIL_RESERVATION);
        final Credentials credentials = (Credentials) ((Data) input).get(SignupData.DataKey.CREDENTIALS);
        final Profile profile = (Profile) ((Data) input).get(SignupData.DataKey.PROFILE);
        final EMail email = (EMail) ((Data) input).get(SignupData.DataKey.EMAIL);
        final SecurityCredentials securityCredentials = (SecurityCredentials) ((Data) input).get(SignupData.DataKey.SECURITY_CREDENTIALS);
        profile.setFeatures(extractFeatures());

        signupProvider.createProfile(emailReservation, credentials, profile,
                email, securityCredentials);
    }

    /**
     * Create a payment plan profile.
     * 
     */
    public void createPlanProfile() throws ReservationExpiredException,
            InvalidCredentialsException {
        final EMailReservation emailReservation = (EMailReservation) ((Data) input).get(SignupData.DataKey.EMAIL_RESERVATION);
        final Credentials credentials = (Credentials) ((Data) input).get(SignupData.DataKey.CREDENTIALS);
        final Profile profile = (Profile) ((Data) input).get(SignupData.DataKey.PROFILE);
        final EMail email = (EMail) ((Data) input).get(SignupData.DataKey.EMAIL);
        final SecurityCredentials securityCredentials = (SecurityCredentials) ((Data) input).get(SignupData.DataKey.SECURITY_CREDENTIALS);
        profile.setFeatures(extractFeatures());
        final PaymentPlanCredentials paymentPlanCredentials = (PaymentPlanCredentials) ((Data) input).get(SignupData.DataKey.PAYMENT_CREDENTIALS);

        signupProvider.createProfile(emailReservation, credentials, profile,
                email, securityCredentials, paymentPlanCredentials);
    }

    /**
     * Create a profile.
     * 
     */
    public void createProfile() throws ReservationExpiredException {
        final EMailReservation emailReservation = (EMailReservation) ((Data) input).get(SignupData.DataKey.EMAIL_RESERVATION);
        final Credentials credentials = (Credentials) ((Data) input).get(SignupData.DataKey.CREDENTIALS);
        final Profile profile = (Profile) ((Data) input).get(SignupData.DataKey.PROFILE);
        final EMail email = (EMail) ((Data) input).get(SignupData.DataKey.EMAIL);
        final SecurityCredentials securityCredentials = (SecurityCredentials) ((Data) input).get(SignupData.DataKey.SECURITY_CREDENTIALS);
        profile.setFeatures(extractFeatures());
        final PaymentInfo paymentInfo = (PaymentInfo) ((Data) input).get(SignupData.DataKey.PAYMENT_INFO);

        signupProvider.createProfile(emailReservation, credentials, profile,
                email, securityCredentials, paymentInfo);
    }

    /**
     * Determine if the payment info is required.
     * 
     * @return True if the payment info is requried.
     */
    public Boolean isRequiredPaymentInfo() {
        return signupProvider.isRequiredPaymentInfo();
    }

    /**
     * Determine if the payment is set.
     * 
     * @return True if the payment info is set.
     */
    public Boolean isSetPaymentInfo() {
        return signupProvider.isSetPaymentInfo();
    }

    /**
     * Extract the features.
     * 
     * @return A list of <code>Feature</code>.
     */
    private List<Feature> extractFeatures() {
        final FeatureSet featureSet = (FeatureSet) ((Data) input).get(DataKey.FEATURE_SET);
        final List<Feature> allFeatures = signupProvider.readFeatures();
        final List<Feature> features = new ArrayList<Feature>();
        switch (featureSet) {
        case FREE:
            for (final Feature feature : allFeatures) {
                if (feature.getName().equals(Feature.Name.BACKUP)) {
                    features.add(feature);
                }
            }
            break;
        case STANDARD:
            for (final Feature feature : allFeatures)
                features.add(feature);
            break;
        }
        return features;
    }
}
