/**
 * Created On: Apr 4, 2007 12:19:09 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.platform;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.Reservation;
import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.model.profile.ReservationExpiredException;

import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class CreateAccount extends AbstractAction {

    /** Create CreateAccount. */
    public CreateAccount(final Platform platform) {
        super(ActionId.PLATFORM_CREATE_ACCOUNT);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Credentials credentials = (Credentials) data.get(DataKey.CREDENTIALS);
        final EMail email = (EMail) data.get(DataKey.EMAIL);
        final Profile profile = (Profile) data.get(DataKey.PROFILE);
        final Reservation reservation = (Reservation) data.get(DataKey.RESERVATION);
        final String securityQuestion = Separator.EmptyString.toString();
        final String securityAnswer = Separator.EmptyString.toString();

        try {
            getProfileModel().create(reservation, credentials, profile, email,
                    securityQuestion, securityAnswer);
        } catch (final ReservationExpiredException rex) {
            // TODO Have to figure out how to handle this error.
            //browser.displayErrorDialog("ErrorCreateAccountReservationExpired");
        }
    }
    
    /** The data keys. */
    public enum DataKey { CREDENTIALS, EMAIL, PROFILE, RESERVATION }
}
