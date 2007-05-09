/**
 * Created On: Jan 25, 2007 3:13:40 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser;

import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.profile.Update;
import com.thinkparity.ophelia.browser.platform.action.profile.UpdatePassword;

/**
 * <b>Title:</b>thinkParity OpheliaUI Browser Popup Delegate<br>
 * <b>Description:</b>A popup delegate for creating a global popup.<br>
 * 
 * @author robert@thinkparity.com
 * @version 1.1.2.8
 */
public final class BrowserPopupDelegate extends DefaultBrowserPopupDelegate {

    /**
     * Create BrowserPopupDelegate.
     * 
     */
    public BrowserPopupDelegate() {
        super();
    }

    /**
     * Show the browser popup menu.
     * 
     */
    public void showForBrowser() {
        add(ActionId.CONTAINER_CREATE, Data.emptyData());

        if (isOnline()) {
            addSeparator();

            final Data updateProfile = new Data(1);
            updateProfile.set(Update.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
            add(ActionId.PROFILE_UPDATE, updateProfile);

            final Data updatePassword = new Data(1);
            updatePassword.set(UpdatePassword.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
            add(ActionId.PROFILE_UPDATE_PASSWORD, updatePassword);
        }
        show();
    }

    /**
     * Determine whether or not the platform is online.
     * 
     * @return True if the platform is online.
     */
    private boolean isOnline() {
        return BrowserPlatform.getInstance().isOnline();
    }
}
