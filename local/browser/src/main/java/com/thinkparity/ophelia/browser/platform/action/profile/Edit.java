/**
 * Created On: 6-Dec-06 11:33:09 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.profile;

import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.model.profile.ProfileModel;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Edit extends AbstractAction {
    
    /** The thinkParity browser application. */
    private final Browser browser;
    
    /**
     * Create Edit.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public Edit(final Browser browser) {
        super(ActionId.PROFILE_EDIT);
        this.browser = browser;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Boolean displayAvatar = (Boolean) data.get(DataKey.DISPLAY_AVATAR);
        if (displayAvatar) {
            browser.displayEditProfileDialog();
        } else {
            final ProfileModel profileModel = getProfileModel();
            final Profile profile = profileModel.read();

            // update profile
            final String name = (String) data.get(DataKey.NAME);
            if (null != name) {          
                final String organization = (String) data.get(DataKey.ORGANIZATION);
                final String title = (String) data.get(DataKey.TITLE);
                final String officePhone = (String) data.get(DataKey.OFFICE_PHONE);
                final String mobilePhone = (String) data.get(DataKey.MOBILE_PHONE);
                final String address = (String) data.get(DataKey.ADDRESS);
                profile.setName(name);
                profile.setOrganization(organization);
                profile.setTitle(title);
                profile.setPhone(officePhone);
                profile.setMobilePhone(mobilePhone);
                profile.setOrganizationAddress(address);
                profileModel.update(profile);
            }
            
            // update password
            final String password = (String) data.get(DataKey.PASSWORD);
            final String newPassword = (String) data.get(DataKey.NEW_PASSWORD);
            if (null != password) {
                // TODO Add a check for password complexity.
                profileModel.updatePassword(password, newPassword);
            }
        }
    }

    /** Data keys. */
    public enum DataKey {
        ADDRESS, DISPLAY_AVATAR, MOBILE_PHONE, NAME, NEW_PASSWORD,
        NEW_PASSWORD_CONFIRM, OFFICE_PHONE, ORGANIZATION, PASSWORD, TITLE
    }
}
