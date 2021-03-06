/**
 * Created On: Feb 9, 2007 4:56:44 PM
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
public class Collapse extends AbstractBrowserAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create a Collapse.
     * 
     * @param browser
     *            The browser application.
     */
    public Collapse(final Browser browser) {
        super(ActionId.CONTAINER_COLLAPSE);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        browser.collapseContainer(containerId);  
    }

    /** The data keys. */
    public enum DataKey { CONTAINER_ID }
}
