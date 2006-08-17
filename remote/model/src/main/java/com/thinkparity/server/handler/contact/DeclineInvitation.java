/*
 * Feb 28, 2006
 */
package com.thinkparity.server.handler.contact;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.ParityServerConstants.Xml;
import com.thinkparity.server.handler.AbstractController;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DeclineInvitation extends AbstractController {

	/**
	 * Create a InviteContact.
	 * 
	 */
	public DeclineInvitation() { super("contact:declineinvitation"); }

	/**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        declineInvitation(
                readString(Xml.Contact.INVITED_AS),
                readJabberId(Xml.Contact.INVITED_BY),
                readJabberId(Xml.Contact.DECLINED_BY),
                readCalendar(Xml.All.EXECUTED_ON));
    }

    /**
     * Decline an invitation.
     * 
     * @param invitedAs
     *            The original invitation e-mail address.
     * @param invitedBy
     *            Invited by.
     * @param declinedBy
     *            Declined by.
     * @param declinedOn
     *            Declined on.
     */
    private void declineInvitation(final String invitedAs,
            final JabberId invitedBy, final JabberId declinedBy,
            final Calendar declinedOn) {
        getContactModel().declineInvitation(invitedAs, invitedBy, declinedBy, declinedOn);
    }
}
