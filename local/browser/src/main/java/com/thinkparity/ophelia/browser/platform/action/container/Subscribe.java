/**
 * Created On: 13-Sep-06 2:37:05 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Subscribe extends AbstractBrowserAction {
    
    /**
     * Create Subscribe.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public Subscribe(final Browser browser) {
        super(ActionId.CONTAINER_SUBSCRIBE);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        Assert.assertNotYetImplemented("Subscribe");     
    }

}
