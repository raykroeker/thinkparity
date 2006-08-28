/**
 * Created On: 24-Aug-06 1:06:51 PM
 * $Id$
 */
package com.thinkparity.browser.platform.action.platform.browser;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class DisplayInfo extends AbstractAction {
    
    /**
     * Create a Create.
     * 
     * @param browser
     *            The browser application.
     */
    public DisplayInfo(final Browser browser) {
        super(ActionId.PLATFORM_BROWSER_DISPLAY_INFO);
    }
    
    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        Assert.assertNotYetImplemented("OpenHelp About");                
    }
}
