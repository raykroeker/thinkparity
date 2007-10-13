/**
 * Created On: 13-Oct-07 12:08:14 AM
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
public class ShowProfilePassivatedNotification extends AbstractAction {

    /** The system application. */
    private final SystemApplication system;

    /**
     * Create ShowProfilePassivatedNotification.
     *
     */
    public ShowProfilePassivatedNotification(final SystemApplication system) {
        super(ActionId.PROFILE_SHOW_PROFILE_PASSIVATED_NOTIFICATION);
        this.system = system;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     *
     */
    @Override
    protected void invoke(final Data data) {
        if (Boolean.FALSE == getProfileModel().readIsActive()) {
            SwingUtil.ensureDispatchThread(new Runnable() {
                public void run() {
                    system.showProfilePassivatedNotification();
                }
            });
        }
    }
}
