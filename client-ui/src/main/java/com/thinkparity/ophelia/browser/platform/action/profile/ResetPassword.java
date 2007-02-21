/*
 * Created On: Aug 29, 2006 8:38:00 AM
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
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ResetPassword extends AbstractAction {

    /** Create ResetPassword. */
    public ResetPassword(final Browser browser) {
        super(ActionId.PROFILE_RESET_PASSWORD);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        try {
            final Link helpLink = LinkFactory.getInstance(Application.OPHELIA, BrowserPlatform.getInstance().getEnvironment()).create("password");
            String runString = "rundll32 url.dll,FileProtocolHandler " + helpLink.toString();
            Runtime.getRuntime().exec(runString);
        } catch (final Throwable t) {
            throw new BrowserException("Cannot open Reset Password web page", t);
        }
    }
}
