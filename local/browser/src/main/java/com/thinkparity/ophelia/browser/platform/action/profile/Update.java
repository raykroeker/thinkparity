/*
 * Created On: Aug 24, 2006 8:53:41 AM
 */
package com.thinkparity.ophelia.browser.platform.action.profile;

import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.model.profile.ProfileModel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Update extends AbstractAction {

    /** The thinkParity browser application. */
    private final Browser browser;

    /**
     * Create Update.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public Update(final Browser browser) {
        super(ActionId.PROFILE_UPDATE);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Boolean displayAvatar = (Boolean) data.get(DataKey.DISPLAY_AVATAR);
        if (displayAvatar) {
            browser.displayUpdateProfileDialog();
        } else {
            final ProfileModel profileModel = getProfileModel();
            final Profile profile = profileModel.read();

            // update profile
            profile.setAddress((String) data.get(DataKey.ADDRESS));
            profile.setCity((String) data.get(DataKey.CITY));
            profile.setCountry((String) data.get(DataKey.COUNTRY));
            profile.setMobilePhone((String) data.get(DataKey.MOBILE_PHONE));
            profile.setName((String) data.get(DataKey.NAME));
            profile.setOrganization((String) data.get(DataKey.ORGANIZATION));
            profile.setOrganizationCountry(profile.getCountry());
            profile.setPhone((String) data.get(DataKey.PHONE));
            profile.setPostalCode((String) data.get(DataKey.POSTAL_CODE));
            profile.setProvince((String) data.get(DataKey.PROVINCE));
            profile.setTitle((String) data.get(DataKey.TITLE));
            profileModel.update(profile);
        }
    }

    /** Data keys. */
    public enum DataKey {
        ADDRESS, CITY, COUNTRY, DISPLAY_AVATAR, MOBILE_PHONE, NAME,
        ORGANIZATION, PHONE, POSTAL_CODE, PROVINCE, TITLE
    }
}
