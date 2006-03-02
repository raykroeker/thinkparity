/*
 * Feb 28, 2006
 */
package com.thinkparity.server.handler.contact;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.server.JabberId;
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
public class AcceptInvitation extends IQHandler {

	/**
	 * Create a InviteContact.
	 * 
	 */
	public AcceptInvitation() { super(IQAction.ACCEPTCONTACTINVITATION); }

	/**
	 * @see com.thinkparity.server.handler.IQHandler#handleIQ(org.xmpp.packet.IQ,
	 *      com.thinkparity.server.model.session.Session)
	 * 
	 */
	public IQ handleIQ(final IQ iq, final Session session)
			throws ParityServerModelException, UnauthorizedException {
		final JabberId jabberId = extractJabberId(iq);
		final ContactModel contactModel = getContactModel(session);
		final Invitation invitation = contactModel.readInvitation(jabberId);
		contactModel.acceptInvitation(invitation.getFrom(), invitation.getTo());
		return createResult(iq);
	}
}
