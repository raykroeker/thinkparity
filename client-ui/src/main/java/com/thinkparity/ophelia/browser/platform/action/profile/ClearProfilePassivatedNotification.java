/**
 * Created On: 12-Oct-07 6:26:20 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.profile;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.system.SystemApplication;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public final class ClearProfilePassivatedNotification extends AbstractAction {

    /** The system application. */
    private final SystemApplication system;

    /**
     * Create ClearInvitationNotifications.
     *
     */
    public ClearProfilePassivatedNotification(final SystemApplication system) {
        super(ActionId.PROFILE_CLEAR_PROFILE_PASSIVATED_NOTIFICATION);
        this.system = system;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     *
     */
    @Override
    protected void invoke(final Data data) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                system.clearProfilePassivatedNotification();
            }
        });
    }
}
