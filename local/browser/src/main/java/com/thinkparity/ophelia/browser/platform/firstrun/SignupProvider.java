/**
 * Created On: Apr 3, 2007 3:06:03 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.profile.Reservation;

import com.thinkparity.ophelia.model.profile.ProfileModel;

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
     * Determine whether or not an e-mail address is available.
     * 
     * @param email
     *            An <code>EMail</code>.
     * @return True if the address is not in use.
     */
    public Boolean readIsEmailAvailable(final EMail email) {
        return profileModel.isAvailable(email);
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
     * @return The <code>Reservation</code>.
     */
    public Reservation readReservation(final String username) {
        return profileModel.createReservation(username);
    }
}
