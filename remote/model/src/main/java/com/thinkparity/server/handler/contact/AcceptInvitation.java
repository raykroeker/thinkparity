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
                readJabberId(Xml.Contact.INVITED_BY),
                readJabberId(Xml.Contact.ACCEPTED_BY),
                readCalendar(Xml.All.EXECUTED_ON));
    }

    /**
     * Accept an invitation.
     * 
     * @param invitedBy
     *            The original invitation creator.
     * @param acceptedBy
     *            The invitation acceptor.
     * @param acceptedOn
     *            When the invitation was accepted.
     */
    private void acceptInvitation(final JabberId invitedBy,
            final JabberId acceptedBy, final Calendar acceptedOn) {
        getContactModel().acceptInvitation(invitedBy, acceptedBy, acceptedOn);
    }
}
