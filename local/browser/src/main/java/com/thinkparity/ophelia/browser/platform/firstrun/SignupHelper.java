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
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.model.Constants.Product;
import com.thinkparity.ophelia.model.profile.ReservationExpiredException;

import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.firstrun.SignupData.DataKey;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class SignupHelper {

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
     * Create a new profile.
     */
    public void createProfile() throws ReservationExpiredException {
        final UsernameReservation usernameReservation = (UsernameReservation) ((Data) input).get(SignupData.DataKey.USERNAME_RESERVATION);
        final EMailReservation emailReservation = (EMailReservation) ((Data) input).get(SignupData.DataKey.EMAIL_RESERVATION);
        final Credentials credentials = (Credentials) ((Data) input).get(SignupData.DataKey.CREDENTIALS);
        final Profile profile = (Profile) ((Data) input).get(SignupData.DataKey.PROFILE);
        final EMail email = (EMail) ((Data) input).get(SignupData.DataKey.EMAIL);
        final String securityQuestion = (String) ((Data) input).get(SignupData.DataKey.SECURITY_QUESTION);
        final String securityAnswer = (String) ((Data) input).get(SignupData.DataKey.SECURITY_ANSWER);
        profile.setFeatures(extractFeatures());
        createProfile(usernameReservation, emailReservation, credentials,
                profile, email, securityQuestion, securityAnswer);
    }

    /**
     * Create a new profile.
     * 
     * @param usernameReservation
     *            A <code>UsernameReservation</code>.
     * @param emailReservation
     *            An <code>EMailReservation</code>.
     * @param credentials
     *            A set of user <code>Credentials</code>.
     * @param profile
     *            A <code>Profile</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @param securityQuestion
     *            A security question <code>String</code>.
     * @param securityAnswer
     *            A security answer <code>String</code>.
     * @throws ReservationExpiredException
     */
    private void createProfile(final UsernameReservation usernameReservation,
            final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final String securityQuestion,
            final String securityAnswer) throws ReservationExpiredException {
        signupProvider.createProfile(usernameReservation,
                emailReservation, credentials, profile, email,
                securityQuestion, securityAnswer);
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
            break;
        case STANDARD:
            for (final Feature feature : allFeatures)
                if (feature.getName().equals(Product.Features.CORE))
                    features.add(feature);
            break;
        case PREMIUM:
            for (final Feature feature : allFeatures)
                features.add(feature);
            break;
        }
        return features;
    }
}
