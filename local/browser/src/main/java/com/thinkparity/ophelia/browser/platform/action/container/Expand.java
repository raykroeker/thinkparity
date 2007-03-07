/**
 * Created On: Jan 24, 2007 12:04:53 PM
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
public class Expand extends AbstractBrowserAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create a Expand.
     * 
     * @param browser
     *            The browser application.
     */
    public Expand(final Browser browser) {
        super(ActionId.CONTAINER_EXPAND);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Boolean archiveTab = (Boolean) data.get(DataKey.ARCHIVE_TAB);
        browser.expandContainer(containerId, archiveTab);  
    }

    /** The data keys. */
    public enum DataKey { CONTAINER_ID, ARCHIVE_TAB }
}
