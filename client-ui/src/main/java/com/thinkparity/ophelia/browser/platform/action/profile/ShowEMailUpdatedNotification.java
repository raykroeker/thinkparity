/**
 * Created On: 12-Oct-07 11:58:54 PM
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
public class ShowEMailUpdatedNotification extends AbstractAction {

    /** The system application. */
    private final SystemApplication system;

    /**
     * Create ShowEMailUpdatedNotification.
     *
     */
    public ShowEMailUpdatedNotification(final SystemApplication system) {
        super(ActionId.PROFILE_SHOW_EMAIL_UPDATED_NOTIFICATION);
        this.system = system;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     *
     */
    @Override
    protected void invoke(final Data data) {
        if (Boolean.FALSE == getProfileModel().readEMail().isVerified()) {
            SwingUtil.ensureDispatchThread(new Runnable() {
                public void run() {
                    system.showEMailUpdatedNotification();
                }
            });
        }
    }
}
