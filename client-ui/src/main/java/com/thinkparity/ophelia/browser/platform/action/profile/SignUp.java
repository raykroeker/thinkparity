/**
 * Created On: 24-Aug-06 2:23:52 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.profile;

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
public class SignUp extends AbstractAction {

    /**
     * Create a Create.
     * 
     * @param browser
     *            The browser application.
     */
    public SignUp(final Browser browser) {
        super(ActionId.PROFILE_SIGN_UP);
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        try {
            final Link signUpLink = LinkFactory.getInstance(Application.OPHELIA, BrowserPlatform.getInstance().getEnvironment()).create("signup");
            String runString = "rundll32 url.dll,FileProtocolHandler " + signUpLink.toString();
            Runtime.getRuntime().exec(runString);
        } catch (final Throwable t) {
            throw new BrowserException("Cannot open Sign Up web page", t);
        }
    }
}
