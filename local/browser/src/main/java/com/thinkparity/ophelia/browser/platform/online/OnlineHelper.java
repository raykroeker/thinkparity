/*
 * Created On: Jun 14, 2006 4:10:49 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.online;

import com.thinkparity.ophelia.model.session.SessionModel;

import com.thinkparity.ophelia.browser.platform.Platform;

/**
 * <b>Title:</b>thinkParity Ophelia Browser Platform Online Helper<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class OnlineHelper {

    /** An instance of <code>SessionModel</code>. */
    private final SessionModel model;

    /**
     * Create OnlineHelper.
     * 
     * @param platform
     *            The <code>Platform</code>.
     */
    public OnlineHelper(final Platform platform) {
        super();
        this.model = platform.getModelFactory().getSessionModel(getClass());
    }

    /**
     * Determine whether or not the platform is online.
     * 
     * @return True if the platform is online.
     */
    public Boolean isOnline() {
        return model.isOnline();
    }
}
