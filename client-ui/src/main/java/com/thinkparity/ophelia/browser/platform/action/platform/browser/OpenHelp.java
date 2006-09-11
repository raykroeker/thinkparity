/**
 * Created On: 24-Aug-06 1:06:35 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.platform.browser;


import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class OpenHelp extends AbstractAction {
    
    /**
     * Create a Create.
     * 
     * @param browser
     *            The browser application.
     */
    public OpenHelp(final Browser browser) {
        super(ActionId.PLATFORM_BROWSER_OPEN_HELP);
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        Assert.assertNotYetImplemented("OpenHelp");               
    }
}
