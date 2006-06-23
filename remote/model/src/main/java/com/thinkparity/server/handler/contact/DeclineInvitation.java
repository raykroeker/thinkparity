/*
 * Feb 28, 2006
 */
package com.thinkparity.server.handler.contact;

import org.jivesoftware.messenger.auth.UnauthorizedException;

import org.xmpp.packet.IQ;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.handler.IQAction;
import com.thinkparity.server.handler.IQHandler;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.contact.ContactModel;
import com.thinkparity.server.model.contact.Invitation;
import com.thinkparity.server.model.session.Session;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DeclineInvitation extends IQHandler {

	/**
	 * Create a InviteContact.
	 * 
	 */
	public DeclineInvitation() { super(IQAction.DECLINECONTACTINVITATION); }

	/**
	 * @see com.thinkparity.server.handler.IQHandler#handleIQ(org.xmpp.packet.IQ,
	 *      com.thinkparity.server.model.session.Session)
	 * 
	 */
	public IQ handleIQ(final IQ iq, final Session session)
			throws ParityServerModelException, UnauthorizedException {
		logger.info("[RMODEL] [CONTACT] [DECLINE INVITATION]");
		final JabberId jabberId = extractJabberId(iq);
		final ContactModel contactModel = getContactModel(session);
		final Invitation invitation = contactModel.readInvitation(jabberId);
		contactModel.declineInvitation(invitation.getFrom(), invitation.getTo());
		return createResult(iq);
	}
}
