/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;


import com.thinkparity.desdemona.model.contact.ContactModel;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AcceptInvitation extends AbstractHandler {

	/** Create AcceptInvitation. */
	public AcceptInvitation() { super("contact:acceptinvitation"); }

	/**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
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
