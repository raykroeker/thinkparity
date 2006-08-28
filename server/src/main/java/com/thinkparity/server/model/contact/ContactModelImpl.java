/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.contact;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jivesoftware.messenger.auth.UnauthorizedException;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.util.smtp.MessageFactory;
import com.thinkparity.model.util.smtp.TransportManager;

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

    /** A contact event generator. */
    private final ContactEventGenerator eventGenerator;

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
        this.eventGenerator = new ContactEventGenerator();
		this.invitationSql = new InvitationSql();
	}

    /**
     * Accept the invitation. Create the contact relationships and notify the
     * invitor.
     * 
     * @param invitedAs
     *            The original invitation e-mail address.
     * @param invitedBy
     *            The invitor.
     * @param acceptedBy
     *            The invitee.
     * @param acceptedOn
     *            When the acceptance was made.
     */
    void acceptInvitation(final JabberId invitedBy, final JabberId acceptedBy,
            final Calendar acceptedOn) {
		logApiId();
        logVariable("invitedBy", invitedBy);
        logVariable("acceptedBy", acceptedBy);
        logVariable("acceptedOn", acceptedOn);
		try {
			contactSql.create(acceptedBy, invitedBy, session.getJabberId());
			contactSql.create(invitedBy, acceptedBy, session.getJabberId());

            final IQ notification =
                eventGenerator.generateInvitationAccepted(acceptedBy, acceptedOn);
            send(invitedBy, notification);
		}
		catch(final Throwable t) {
            throw translateError(t);
		}
	}

    Invitation createInvitation(final JabberId to) throws ParityServerModelException {
        logApiId();
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

    /**
     * Decline the invitation. Send the invitee a notifiaction.
     * 
     * @param invitedBy
     *            The invitor.
     * @param declinedBy
     *            The invitee.
     * @param declinedOn
     *            When the acceptance was made.
     */
	void declineInvitation(final String invitedAs, final JabberId invitedBy,
            final JabberId declinedBy, final Calendar declinedOn) {
        logApiId();
        logVariable("invitedBy", invitedBy);
        logVariable("invitedAs", invitedAs);
        logVariable("declinedBy", declinedBy);
        logVariable("declinedOn", declinedOn);
        try {
            final IQ notification = eventGenerator.generateInvitationDeclined(
                    invitedAs, declinedBy, declinedOn);
            send(invitedBy, notification);
        } catch (final Throwable t) {
            throw translateError(t);
        }
	}

    /**
     * TODO create controller 0.25; delete contact data 0.25; delete distributed
     * contact 0.25
     */
    void delete(final JabberId jabberId) {}

    /**
     * TODO create controller 0.25; read e-mail invitation data 0.25; delete
     * e-mail invitation data 0.25;
     */
	void deleteInvitation(final JabberId from)
			throws ParityServerModelException {
        logApiId();
		logger.debug(from);
		try { invitationSql.delete(from, session.getJabberId()); }
		catch(final SQLException sqlx) {
			logger.error("Could not delete inviataion:  " + from, sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
	}

    /**
     * TODO read user for e-mail 0.5; create e-mail invitation data 0.5; create
     * invitation data 0.5; create distributed invitation 0.5
     */
	void invite(final EMail email, final Calendar invitedOn) {
        logApiId();
        logVariable("email", email);
        logVariable("invitedOn", invitedOn);
        try {
            final UserModel userModel = getUserModel();
            final User invitee = userModel.readUser(email);
            if (null == invitee) {
                final MimeMessage mimeMessage = MessageFactory.createMimeMessage();
                try {
                    final User user = getUserModel().readUser(session.getJabberId());
                    createInvitation(mimeMessage, email, user);
                    addRecipient(mimeMessage, email);
                }
                catch(final MessagingException mx) { throw translateError(mx); }
                TransportManager.deliver(mimeMessage);
            } else {
                final IQ notification =
                    eventGenerator.generateInvitationExtended(
                            email, session.getJabberId(), invitedOn);
                send(invitee.getId(), notification);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	Contact readContact(final JabberId contactId) {
        logApiId();
		logVariable("contactId", contactId);
		try {
		    final User user = getUserModel().readUser(contactId);
            final Element vCardElement = user.getVCard();

            final Contact contact = new Contact();
            contact.setId(user.getId());
            contact.setName((String) vCardElement.element("FN").getData());
            contact.setOrganization((String) vCardElement.element("ORG").element("ORGNAME").getData());
            contact.setVCard(user.getVCard());
            return contact;
	    } catch (final Throwable t) {
            throw translateError(t);
        }
	}

    List<Contact> readContacts() throws ParityServerModelException {
        logApiId();
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
        logApiId();
		logger.debug(from);
		try { return invitationSql.read(from, session.getJabberId()); }
		catch(final SQLException sqlx) {
			logger.error("Could not read invitation:  " + from, sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
	}

	private void createInvitation(final MimeMessage mimeMessage,
            final EMail email, final User inviter) throws MessagingException {
        final InvitationText text = new InvitationText(Locale.getDefault(), email, inviter);
	    mimeMessage.setSubject(text.getSubject());

        final MimeBodyPart invitationBody = new MimeBodyPart();
        invitationBody.setContent(text.getBody(), text.getBodyType());

        final Multipart invitation = new MimeMultipart();
        invitation.addBodyPart(invitationBody);
        mimeMessage.setContent(invitation);
    }
}
