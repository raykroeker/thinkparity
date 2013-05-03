/**
 * Created On: Feb 22, 2007 5:05:47 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.platform.browser;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Iconify extends AbstractAction {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** Create Iconify. */
    public Iconify(final Platform platform) {
        super(ActionId.PLATFORM_BROWSER_ICONIFY);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final Boolean iconify = (Boolean) data.get(DataKey.ICONIFY);
        ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER)).iconify(iconify);
    }

    /** The data keys. */
    public enum DataKey { ICONIFY }
}
