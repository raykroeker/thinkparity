/**
 * Created On: 24-Aug-06 1:06:35 PM
 * $Id$
 */
package com.thinkparity.browser.platform.action.help;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Help extends AbstractAction {
    
    /** The browser application. */
    private final Browser browser;

    /**
     * Create a Create.
     * 
     * @param browser
     *            The browser application.
     */
    public Help(final Browser browser) {
        super(ActionId.HELP);
        this.browser = browser;
    }
    
    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        Assert.assertNotYetImplemented("Help");               
    }
}
