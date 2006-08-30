/*
 * Feb 28, 2006
 */
package com.thinkparity.server.handler.contact;

import java.util.List;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.server.handler.IQAction;
import com.thinkparity.server.handler.IQHandler;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.contact.Contact;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.org.xmpp.packet.contact.IQReadContacts;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReadContacts extends IQHandler {

	/** Create a ReadContacts. */
	public ReadContacts() { super(IQAction.READCONTACTS); }

	/**
	 * @see com.thinkparity.server.handler.IQHandler#handleIQ(org.xmpp.packet.IQ,
	 *      com.thinkparity.server.model.session.Session)
	 * 
	 */
	public IQ handleIQ(final IQ iq, final Session session)
			throws ParityServerModelException, UnauthorizedException {
        logApiId();
		final List<Contact> contacts = getContactModel(session).readContacts();
		return createResult(iq, session, contacts);
	}

	private IQ createResult(final IQ iq, final Session session,
			final List<Contact> contacts) {
		final IQ result = new IQReadContacts(contacts);
		result.setID(iq.getID());
		result.setTo(session.getJID());
		result.setFrom(session.getJID());
		return result;
	}
}
