/*
 * Created On: Aug 29, 2006 3:00:18 PM
 */
package com.thinkparity.server.handler.contact;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.handler.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DeleteInvitation extends AbstractController {

    /** Create DeleteInvitation. */
    public DeleteInvitation() { super("contact:deleteinvitation"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logApiId();
        deleteInvitation(readJabberId("userId"), readJabberId("invitedUserId"));
    }

    /**
     * Delete an extended invitation.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedUserId
     *            The invited user's id <code>JabberId</code>.
     */
    private void deleteInvitation(final JabberId userId, final JabberId invitedUserId) {
        getContactModel().deleteInvitation(userId, invitedUserId);
    }
}
