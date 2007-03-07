/**
 * Created On: Feb 21, 2007 2:37:05 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatar;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Show extends AbstractBrowserAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create a Show.
     * 
     * @param browser
     *            The browser application.
     */
    public Show(final Browser browser) {
        super(ActionId.CONTACT_SHOW);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long invitationId = (Long) data.get(DataKey.INVITATION_ID);
        // Restore the Browser if it is hibernating
        browser.runPlatformBrowserRestore();

        // Uniconify and move to front
        browser.iconify(Boolean.FALSE);
        browser.moveToFront();

        // Select the contact tab and show the invitation
        browser.selectTab(MainTitleAvatar.TabId.CONTACT);
        browser.showContactInvitation(invitationId);
    }

    /** The data keys. */
    public enum DataKey { INVITATION_ID }
}
