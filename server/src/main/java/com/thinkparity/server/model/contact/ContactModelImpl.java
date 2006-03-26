/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.contact;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.server.JabberId;
import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.ParityErrorTranslator;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.io.sql.contact.ContactSql;
import com.thinkparity.server.model.io.sql.contact.InvitationSql;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.model.user.User;
import com.thinkparity.server.model.user.UserModel;
import com.thinkparity.server.org.xmpp.packet.contact.IQInviteContact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.5
 */
class ContactModelImpl extends AbstractModelImpl {

	/**
	 * Contact sql interface.
	 * 
	 */
	private final ContactSql contactSql;

	/**
	 * Invitation sql interface.
	 * 
	 */
	private final InvitationSql invitationSql;

	/**
	 * Create a ArtifactModelImpl.
	 * 
	 */
	ContactModelImpl(final Session session) {
		super(session);
		this.contactSql = new ContactSql();
		this.invitationSql = new InvitationSql();
	}

	void acceptInvitation(final JabberId from, final JabberId to)
			throws ParityServerModelException {
		logger.info("acceptInvitation(JabberId,JabberId)");
		logger.debug(from);
		logger.debug(to);
		try {
			final Invitation invitation = invitationSql.read(from, to);
			Assert.assertNotNull("Cannot accept a null invitation.", invitation);

			contactSql.create(from, to, session.getJabberId());
			contactSql.create(to, from, session.getJabberId());

			invitationSql.delete(from, to);
		}
		catch(final SQLException sqlx) {
			logger.error("could not accept invitation:  " + from + ", " + to, sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
	}

	Invitation createInvitation(final JabberId to) throws ParityServerModelException {
		logger.info("createInvitation(JabberId)");
		logger.debug(to);
		try {
			final Invitation invitation = invitationSql.read(session.getJabberId(), to);
			// if an invitation already exists we do nothing
			if(null == invitation) {
				invitationSql.create(session.getJabberId(), to, session.getJabberId());

				// send the invitation
				final IQ iq = new IQInviteContact(session.getJabberId());
				iq.setTo(to.getJID());
				iq.setFrom(session.getJID());
				send(to, iq);
				return invitationSql.read(session.getJabberId(), to);
			}
			else { return invitation; }
		}
		catch(final SQLException sqlx) {
			logger.error("Could not create invitation:  " + to, sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
		catch(final UnauthorizedException ux) {
			logger.error("Could not create invitation:  " + to, ux);
			throw ParityErrorTranslator.translate(ux);
		}
	}

	void declineInvitation(final JabberId from, final JabberId to)
		throws ParityServerModelException {
		logger.info("declineInvitation(JabberId,JabberId)");
		logger.debug(from);
		logger.debug(to);
		try {
			final Invitation invitation = invitationSql.read(from, to);
			Assert.assertNotNull("Cannot decline a null invitation.", invitation);

			invitationSql.delete(from, to);
		}
		catch(final SQLException sqlx) {
			logger.error("could not decline invitation:  " + from + ", " + to, sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
	}

	void deleteInvitation(final JabberId from)
			throws ParityServerModelException {
		logger.info("deleteInvitation(JabberId)");
		logger.debug(from);
		try { invitationSql.delete(from, session.getJabberId()); }
		catch(final SQLException sqlx) {
			logger.error("Could not delete inviataion:  " + from, sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
	}

	List<Contact> readContacts() throws ParityServerModelException {
		logger.info("readContacts()");
		try {
			final UserModel userModel = getUserModel();
			final List<JabberId> contactIds = contactSql.read(session.getJabberId());
			final List<User> users = userModel.readUsers(contactIds);
			final List<Contact> contacts = new LinkedList<Contact>();
			Contact contact;
			for(final User user : users) {
				contact = new Contact();
				contact.setId(user.getId());
				contact.setVCard(user.getVCard());
				contacts.add(contact);
			}
			return contacts;
		}
		catch(final SQLException sqlx) {
			logger.error("Could not read contacts:  " + session.getJabberId(), sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
	}

	Invitation readInvitation(final JabberId from)
			throws ParityServerModelException {
		logger.info("readInvitation(JabberId)");
		logger.debug(from);
		try { return invitationSql.read(from, session.getJabberId()); }
		catch(final SQLException sqlx) {
			logger.error("Could not read invitation:  " + from, sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
	}
}
