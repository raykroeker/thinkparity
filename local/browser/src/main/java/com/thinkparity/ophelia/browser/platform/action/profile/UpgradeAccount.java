/**
 * Created On: 27-Jul-07 4:56:31 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.profile;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class UpgradeAccount extends AbstractBrowserAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create UpgradeAccount.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public UpgradeAccount(final Browser browser) {
        super(ActionId.PROFILE_UPGRADE_ACCOUNT);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Boolean displayAvatar = (Boolean) data.get(DataKey.DISPLAY_AVATAR);
        if (displayAvatar) {
            browser.displayUpgradeAccountDialog();
        } else {
            // TODO
        }
    }

    public enum DataKey { DISPLAY_AVATAR }
}
