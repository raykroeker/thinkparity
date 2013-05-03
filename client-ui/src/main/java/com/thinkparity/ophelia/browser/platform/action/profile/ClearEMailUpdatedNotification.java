/**
 * Created On: 12-Oct-07 10:36:16 PM
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
public class ClearEMailUpdatedNotification extends AbstractAction {

    /** The system application. */
    private final SystemApplication system;

    /**
     * Create ClearEMailUpdatedNotification.
     *
     */
    public ClearEMailUpdatedNotification(final SystemApplication system) {
        super(ActionId.PROFILE_CLEAR_EMAIL_UPDATED_NOTIFICATION);
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
                system.clearEMailUpdatedNotification();
            }
        });
    }
}
