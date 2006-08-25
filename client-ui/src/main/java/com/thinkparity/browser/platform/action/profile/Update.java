/*
 * Created On: Aug 24, 2006 8:53:41 AM
 */
package com.thinkparity.browser.platform.action.profile;

import java.util.List;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.profile.ProfileModel;

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
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Boolean displayAvatar = (Boolean) data.get(DataKey.DISPLAY_AVATAR);
        if (displayAvatar) {
            browser.displayUpdateProfileDialog();
        } else {
            final List<EMail> emails = data.getList(DataKey.EMAILS);
            final String name = (String) data.get(DataKey.NAME);
            final String organization = (String) data.get(DataKey.ORGANIZATION);
            final String title = (String) data.get(DataKey.TITLE);
    
            final ProfileModel profileModel = getProfileModel();
            final Profile profile = profileModel.read();
            profile.clearEmails();
            profile.addAllEmails(emails);
            profile.setName(name);
            profile.setOrganization(organization);
            profile.setTitle(title);
            profileModel.update(profile);
        }
    }

    /** Data keys. */
    public enum DataKey { DISPLAY_AVATAR, EMAILS, NAME, ORGANIZATION, TITLE }
}
