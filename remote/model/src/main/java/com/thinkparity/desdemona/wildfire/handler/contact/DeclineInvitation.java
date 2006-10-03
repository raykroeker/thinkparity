/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import java.util.Calendar;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.model.contact.ContactModel;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DeclineInvitation extends AbstractHandler {

	/** Create DeclineInvitation. */
	public DeclineInvitation() { super("contact:declineinvitation"); }

	/**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        declineInvitation(readJabberId("declinedBy"),
                readJabberId("invitedBy"), readEMail("invitedAs"),
                readCalendar("executedOn"));
    }

    /**
     * @see ContactModel#declineInvitation(JabberId, JabberId, EMail, Calendar)
     * 
     */
    private void declineInvitation(final JabberId userId,
            final JabberId invitedBy, final EMail invitedAs,
            final Calendar declinedOn) {
        getContactModel().declineInvitation(userId, invitedBy, invitedAs, declinedOn);
    }
}
