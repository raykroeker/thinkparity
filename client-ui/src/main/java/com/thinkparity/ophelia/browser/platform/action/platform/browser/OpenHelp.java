/**
 * Created On: 24-Aug-06 1:06:35 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.platform.browser;


import com.thinkparity.codebase.Application;
import com.thinkparity.codebase.http.Link;
import com.thinkparity.codebase.http.LinkFactory;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

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
        try {
            final Link helpLink = LinkFactory.getInstance(Application.OPHELIA, BrowserPlatform.getInstance().getEnvironment()).create("help");
            String runString = "rundll32 url.dll,FileProtocolHandler " + helpLink.toString();
            Runtime.getRuntime().exec(runString);
        } catch (final Throwable t) {
            throw new BrowserException("Cannot open Help web page", t);
        }
    }
}
