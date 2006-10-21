/**
 * Created On: 20-Oct-06 4:53:31 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class RemoveBookmark extends AbstractAction {
    
    /** The browser application. */
    private final Browser browser;

    /**
     * Remove bookmark.
     * 
     * @param browser
     *            The browser application.
     */
    public RemoveBookmark(final Browser browser) {
        super(ActionId.CONTAINER_REMOVE_BOOKMARK);
        this.browser = browser;
    }
        
    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        getContainerModel().removeBookmark(containerId);
    }
    
    public enum DataKey { CONTAINER_ID }
}
