/**
 * Created On: Mar 15, 2007 12:21:10 PM
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
public class OpenWebSite extends AbstractAction {

    /** Create OpenWebSite. */
    public OpenWebSite(final Platform platform) {
        super(ActionId.PLATFORM_OPEN_WEBSITE);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Link webSiteLink = LinkFactory.getInstance(Application.OPHELIA, BrowserPlatform.getInstance().getEnvironment()).create();    
        try {
            DesktopUtil.browse(webSiteLink.toString());
        } catch (final DesktopException dx) {
            throw new BrowserException("Cannot open main web page", dx);
        }
    }
}
