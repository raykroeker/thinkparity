/*
 * Created On:  3-Jul-07 12:24:55 PM
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.system.SystemApplication;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>Clear Incoming User Invitation Notifications<br>
 * <b>Description:</b>Clear all displayed notifications pertaining to an
 * invitation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ClearIncomingUserInvitationNotifications extends AbstractAction {

    /** The system application. */
    private final SystemApplication system;

    /**
     * Create ClearInvitationNotifications.
     *
     */
    public ClearIncomingUserInvitationNotifications(final SystemApplication system) {
        super(ActionId.CONTACT_CLEAR_INCOMING_USER_INVITATION_NOTIFICATIONS);
        this.system = system;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     *
     */
    @Override
    protected void invoke(final Data data) {
        final Long invitationId = (Long) data.get(DataKey.INVITATION_ID);
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                system.clearInvitationNotifications(invitationId);
            }
        });
    }

    /** <b>Title:</b>Data Keys<br> */
    public enum DataKey { INVITATION_ID }
}
