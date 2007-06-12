/**
 * Created On: 7-May-07 10:09:39 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.platform;

import com.thinkparity.codebase.model.util.http.Link;
import com.thinkparity.codebase.model.util.http.LinkFactory;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.util.jdic.DesktopUtil;

import org.jdesktop.jdic.desktop.DesktopException;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContactUs extends AbstractAction {

    /** The relative link for "contact us". */
    private static String CONTACT_US_LINK;

    static {
        CONTACT_US_LINK = "contactUs";
    }
    /** Create ContactUs. */
    public ContactUs(final Platform platform) {
        super(ActionId.PLATFORM_CONTACT_US);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Link contactUsLink = LinkFactory.getInstance().create(CONTACT_US_LINK);
        try {
            DesktopUtil.browse(contactUsLink.toString());
        } catch (final DesktopException dx) {
            throw new BrowserException("Cannot open Contact Us web page", dx);
        }
    }
}
