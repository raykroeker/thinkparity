/**
 * Created On: 13-Sep-06 3:02:20 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Rename  extends AbstractBrowserAction {
    
    /** The browser application. */
    private final Browser browser;
    
    /**
     * Create Unsubscribe.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public Rename(final Browser browser) {
        super(ActionId.CONTAINER_RENAME);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final String containerName = (String) data.get(DataKey.CONTAINER_NAME);

        if (null == containerName) {
            final Container container = getContainerModel().read(containerId);
            browser.displayRenameContainerDialog(containerId, container.getName());
        }
        else {
            getContainerModel().rename(containerId, containerName);
        }    
    }

    /** The data keys. */
    public enum DataKey { CONTAINER_ID, CONTAINER_NAME }
}
