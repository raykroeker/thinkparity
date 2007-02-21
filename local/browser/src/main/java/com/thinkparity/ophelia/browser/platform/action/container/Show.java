/**
 * Created On: Feb 21, 2007 2:09:21 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.MainTitleAvatar;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Show extends AbstractAction {

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
        browser.runPlatformBrowserRestore();
        browser.selectTab(MainTitleAvatar.TabId.CONTAINER);
        browser.expandContainer(containerId, Boolean.FALSE);
    }

    /** The data keys. */
    public enum DataKey { CONTAINER_ID }
}
