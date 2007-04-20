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
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.profile.ReservationExpiredException;

import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class SignupProvider extends ContentProvider {

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
     * Create a profile (account).
     * 
     * @param reservation
     *            A <code>Reservation</code>.
     * @param credentials
     *            A <code>Credentials</code>.
     * @param profile
     *            A <code>Profile</code>.
     * @param email
     *            An <code>EMail</code>.
     * @throws ReservationExpiredException
     */
    public void createProfile(final UsernameReservation usernameReservation,
            final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email) throws ReservationExpiredException {
        // NOCOMMIT - Hardcoded security question/answer.
    	profileModel.create(usernameReservation, emailReservation, credentials,
                profile, email, "What is my username?",
                profile.getSimpleUsername());
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
     * Read the available features.
     * 
     * @return A <code>List</code> of <code>Feature</code>s.
     */
    public List<Feature> readFeatures() {
        return profileModel.readFeatures();
    }
}
