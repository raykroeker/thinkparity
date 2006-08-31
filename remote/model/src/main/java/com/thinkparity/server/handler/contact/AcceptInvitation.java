/*
 * Feb 28, 2006
 */
package com.thinkparity.server.handler.contact;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.handler.AbstractController;
import com.thinkparity.server.model.contact.ContactModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AcceptInvitation extends AbstractController {

	/** Create AcceptInvitation. */
	public AcceptInvitation() { super("contact:acceptinvitation"); }

	/**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logApiId();
        acceptInvitation(
                readJabberId("userId"),
                readJabberId("invitedBy"),
                readCalendar("acceptedOn"));
    }

    /**
     * @see ContactModel#acceptInvitation(JabberId, JabberId, Calendar)
     */
    private void acceptInvitation(final JabberId userId,
            final JabberId invitedBy, final Calendar acceptedOn) {
        getContactModel().acceptInvitation(userId, invitedBy, acceptedOn);
    }
}
