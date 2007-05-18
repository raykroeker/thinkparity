/**
 * Created On: 18-May-07 10:02:23 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ApplyFlagSeen extends AbstractBrowserAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create ApplyFlagSeen.
     * 
     * @param browser
     *            The <code>Browser</code>.
     */
    public ApplyFlagSeen(final Browser browser) {
        super(ActionId.CONTAINER_APPLY_FLAG_SEEN);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        if (!getContainerModel().hasBeenSeen(containerId)) {
            getContainerModel().applyFlagSeen(containerId);
            browser.reloadStatusAvatar();
        }
    }

    /** The data key. */
    public enum DataKey { CONTAINER_ID }
}
