/**
 * Created On: 4-Sep-07 1:59:31 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action;

import com.thinkparity.ophelia.browser.platform.BrowserPlatform;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.util.ModelFactory;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public final class ActionVisibility {

    private static final ActionVisibility SINGLETON;

    static {
        SINGLETON = new ActionVisibility();
    }

    private final ModelFactory modelFactory;

    /**
     * Create ActionVisibility.
     * 
     */
    private ActionVisibility() {
        super();
        final Platform platform = BrowserPlatform.getInstance();
        this.modelFactory = platform.getModelFactory();
    }

    public Boolean isVisible(final ActionId actionId, final Data data) {
        return Boolean.TRUE;
    }
}
