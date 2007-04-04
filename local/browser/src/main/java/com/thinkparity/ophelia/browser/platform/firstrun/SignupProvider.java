/**
 * Created On: Apr 3, 2007 3:06:03 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

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
     * Read a reservation for a username, if possible.
     * 
     * @param username
     *            A username <code>String</code>.
     * @return The <code>Reservation</code>.
     */
    public Reservation readReservation(final String username) {
        Reservation reservation = null;
        try {
            reservation = profileModel.createReservation(username);
        } catch (final Throwable t) {
            reservation = null;
        }
        return reservation;
    }
}
