/**
 * Created On: Mar 6, 2007 4:23:29 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.platform;

import org.jdesktop.jdic.desktop.DesktopException;

import com.thinkparity.codebase.Application;
import com.thinkparity.codebase.model.util.http.Link;
import com.thinkparity.codebase.model.util.http.LinkFactory;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.util.jdic.DesktopUtil;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ResetPassword extends AbstractAction {

    /** Create ResetPassword. */
    public ResetPassword(final Platform platform) {
        super(ActionId.PLATFORM_RESET_PASSWORD);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Link passwordLink = LinkFactory.getInstance(Application.OPHELIA, BrowserPlatform.getInstance().getEnvironment()).create("password");    
        try {
            DesktopUtil.browse(passwordLink.toString());
        } catch (final DesktopException dx) {
            throw new BrowserException("Cannot open Reset Password web page", dx);
        }
    }
}
