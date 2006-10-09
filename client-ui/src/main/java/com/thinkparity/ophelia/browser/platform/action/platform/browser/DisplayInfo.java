/**
 * Created On: 24-Aug-06 1:06:51 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.platform.browser;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class DisplayInfo extends AbstractAction {
    
    /** The browser application. */
    private final Browser browser;
    
    /**
     * Create a Create.
     * 
     * @param browser
     *            The browser application.
     */
    public DisplayInfo(final Browser browser) {
        super(ActionId.PLATFORM_BROWSER_DISPLAY_INFO);
        this.browser = browser;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        // Launch help about dialog
        browser.displayInfoDialog();              
    }
}
