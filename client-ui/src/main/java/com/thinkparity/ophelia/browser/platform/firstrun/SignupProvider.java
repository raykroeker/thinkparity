/**
 * Created On: Apr 3, 2007 3:06:03 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.Reservation;
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
    public void createProfile(final Reservation reservation,
			final Credentials credentials, final Profile profile,
			final EMail email) throws ReservationExpiredException {
    	profileModel.create(reservation, credentials, profile, email);
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
     * Read a reservation for a username, if possible.
     * 
     * @param username
     *            A username <code>String</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @return The <code>Reservation</code>.
     */
    public Reservation createReservation(final String username,
            final EMail email) {
        return profileModel.createReservation(username, email);
    }
}
