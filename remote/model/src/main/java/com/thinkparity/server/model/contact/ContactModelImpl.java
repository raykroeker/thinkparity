/*
 * Created On: Nov 29, 2005
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

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.util.smtp.MessageFactory;
import com.thinkparity.model.util.smtp.TransportManager;
import com.thinkparity.model.xmpp.IQWriter;

import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.ParityErrorTranslator;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.io.sql.contact.ContactSql;
import com.thinkparity.server.model.io.sql.contact.InvitationSql;
import com.thinkparity.server.model.io.sql.user.UserSql;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.model.user.User;
import com.thinkparity.server.model.user.UserModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.10
 */
class ContactModelImpl extends AbstractModelImpl {

	/** The thinkParity contact io. */
	private final ContactSql contactSql;

    /** The thinkParity contact event generator. */
    private final ContactEventGenerator eventGenerator;

    /** The thinkParity invitation io. */
	private final InvitationSql invitationSql;

    /** The thinkParity user io. */
    private final UserSql userSql;

    /**
	 * Create ContactModelImpl.
	 * 
     * @param session
     *            The user's session.
	 */
	ContactModelImpl(final Session session) {
		super(session);
		this.contactSql = new ContactSql();
        this.eventGenerator = new ContactEventGenerator();
		this.invitationSql = new InvitationSql();
        this.userSql = new UserSql();
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

			final IQWriter notification = createIQWriter("invitationaccepted");
            notification.writeJabberId("acceptedBy", acceptedBy);
            notification.writeCalendar("acceptedOn", acceptedOn);
            final IQ notificationIQ = notification.getIQ();
            notificationIQ.setTo(invitedBy.getJID());
            send(invitedBy, notificationIQ);
		}
		catch(final Throwable t) {
            throw translateError(t);
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
     * Delete a contact for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    void delete(final JabberId userId, final JabberId contactId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("contactId", contactId);
        try {
            assertIsAuthenticatedUser(userId);
            contactSql.delete(userId, contactId);
            contactSql.delete(contactId, userId);
            
            final IQWriter notification = createIQWriter("contactdeleted");
            notification.writeJabberId("deletedBy", userId);
            notification.writeCalendar("deletedOn", currentDateTime());
            final IQ notificationIQ = notification.getIQ();
            notificationIQ.setTo(contactId.getJID());
            send(contactId, notificationIQ);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * TODO create controller 0.25; read e-mail invitation data 0.25; delete
     * e-mail invitation data 0.25;
     */
	void deleteInvitation(final JabberId userId, final JabberId invitedUserId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("invitedUserId", invitedUserId);
		try {
            invitationSql.delete(userId, invitedUserId);
		} catch(final Throwable t) {
			throw translateError(t);
		}
	}

	/**
     * Extend an invitation for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param extendedTo
     *            An <code>EMail</code> to extend the invitation to.
     * @param extendedOn
     *            The date <code>Calendar</code>.
     */
	void extendInvitation(final JabberId userId, final EMail extendTo,
            final Calendar extendedOn) {
        logApiId();
        logVariable("userId", userId);
        logVariable("extendTo", extendTo);
        logVariable("extendedOn", extendedOn);
        try {
            final UserModel userModel = getUserModel();
            final User extendToUser = userModel.readUser(extendTo);
            if (null == extendToUser) {
                // extend the invitation via SMTP
                final MimeMessage mimeMessage = MessageFactory.createMimeMessage();
                createInvitation(mimeMessage, extendTo, userModel.readUser(userId));
                addRecipient(mimeMessage, extendTo);
                TransportManager.deliver(mimeMessage);
            } else {
                // extend the invitation within thinkParity
                final IQWriter notification = createIQWriter("invitationextended");
                notification.writeEMail("extendedTo", extendTo);
                notification.writeJabberId("extendedBy", userId);
                notification.writeCalendar("extendedOn", extendedOn);
                final IQ notificationIQ = notification.getIQ();
                notificationIQ.setTo(extendToUser.getId().getJID());
                send(extendToUser.getId(), notificationIQ);
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
            contact.addAllEmails(userSql.readEmails(contactId, Boolean.TRUE));
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

    Invitation readInvitation(final JabberId from) {
        logApiId();
		logger.debug(from);
		try {
            return invitationSql.read(from, session.getJabberId());
		} catch (final Throwable t) {
			throw translateError(t);
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
