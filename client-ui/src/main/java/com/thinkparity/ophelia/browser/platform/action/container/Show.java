/**
 * Created On: Feb 21, 2007 2:09:21 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

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
        super(ActionId.CONTAINER_SHOW);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Long versionId = (Long) data.get(DataKey.VERSION_ID);
        final Boolean clearSearch = (Boolean) data.get(DataKey.CLEAR_SEARCH);

        // Restore the Browser if it is hibernating
        invoke(ActionId.PLATFORM_BROWSER_RESTORE, Data.emptyData());

        // Uniconify and move to front
        browser.iconify(Boolean.FALSE);
        browser.moveToFront();

        // Clear search
        if (clearSearch) {
            browser.clearSearch();
        }

        // Select the container tab and show the container
        browser.selectTab(MainTitleAvatar.TabId.CONTAINER);
        if (null == versionId) {
            browser.expandContainer(containerId);
        } else {
            browser.expandContainer(containerId, versionId);
        }
    }

    /** The data keys. */
    public enum DataKey { CLEAR_SEARCH, CONTAINER_ID, VERSION_ID }
}
