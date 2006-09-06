/*
 * Created On: Nov 29, 2005
 */
package com.thinkparity.model.contact;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jivesoftware.messenger.vcard.VCardManager;
import org.jivesoftware.messenger.vcard.VCardProvider;

import org.dom4j.Element;
import org.xmpp.packet.IQ;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.AbstractModelImpl;
import com.thinkparity.model.Constants.VCardFields;
import com.thinkparity.model.io.sql.ContactSql;
import com.thinkparity.model.io.sql.InvitationSql;
import com.thinkparity.model.io.sql.UserSql;
import com.thinkparity.model.session.Session;
import com.thinkparity.model.user.User;
import com.thinkparity.model.user.UserModel;
import com.thinkparity.model.util.smtp.MessageFactory;
import com.thinkparity.model.util.smtp.TransportManager;
import com.thinkparity.model.xmpp.IQWriter;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.10
 */
class ContactModelImpl extends AbstractModelImpl {

	/** The thinkParity contact io. */
	private final ContactSql contactSql;

    /** The thinkParity invitation io. */
	private final InvitationSql invitationSql;

    /** The thinkParity user io. */
    private final UserSql userSql;

    /** A jive vcard provider. */
    private final VCardProvider vcardProvider;

    /**
	 * Create ContactModelImpl.
	 * 
     * @param session
     *            The user's session.
	 */
	ContactModelImpl(final Session session) {
		super(session);
		this.contactSql = new ContactSql();
		this.invitationSql = new InvitationSql();
        this.userSql = new UserSql();
        this.vcardProvider = VCardManager.getProvider();
	}

    /**
     * Accept the invitation. Create the contact relationships and notify the
     * invitor.
     * 
     * @param userId
     *            The user id <code>JabberId</code>.
     * @param invitedAs
     *            The original invitation e-mail address.
     * @param invitedBy
     *            The invitor.
     * @param acceptedBy
     *            The invitee.
     * @param acceptedOn
     *            When the acceptance was made.
     */
    void acceptInvitation(final JabberId userId, final JabberId invitedBy,
            final Calendar acceptedOn) {
		logApiId();
		logVariable("userId", userId);
        logVariable("invitedBy", invitedBy);
        logVariable("acceptedOn", acceptedOn);
		try {
            assertIsAuthenticatedUser(userId);
            // delete the invitation
		    invitationSql.delete(invitedBy, userId);
		    // create the contact relationships
            contactSql.create(userId, invitedBy, userId);
			contactSql.create(invitedBy, userId, userId);

			final IQWriter notification = createIQWriter("invitationaccepted");
            notification.writeJabberId("acceptedBy", userId);
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
     * Decline an invitation. Delete the invitation and send a notification
     * (which will delete that invitation).
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param invitedBy
     *            Who invited the user.
     * @param invitedAs
     *            To which <code>EMail</code> the invitation was sent.
     * @param declinedOn
     *            When the invitation was declined.
     */
	void declineInvitation(final JabberId userId, final JabberId invitedBy,
            final EMail invitedAs, final Calendar declinedOn) {
        logApiId();
        logVariable("userId", userId);
        logVariable("invitedBy", invitedBy);
        logVariable("invitedAs", invitedAs);
        logVariable("declinedOn", declinedOn);
        try {
            // delete the invitation
            invitationSql.delete(invitedBy, userId);
            // send notification
            final IQWriter notification = createIQWriter("invitationdeclined");
            notification.writeEMail("invitedAs", invitedAs);
            notification.writeJabberId("declinedBy", userId);
            notification.writeCalendar("declinedOn", declinedOn);
            final IQ notificationIQ = notification.getIQ();
            notificationIQ.setTo(invitedBy.getJID());
            send(invitedBy, notificationIQ);
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
     * Delete a contact invitation.
     * 
     * @param userId
     *            A user id.
     * @param invitedAs
     *            The <code>EMail</code> the invitation was sent to.
     * @param deletedOn
     *            The deletion date\time <code>Calendar<code>.
     */
	void deleteInvitation(final JabberId userId, final EMail invitedAs,
            final Calendar deletedOn) {
        logApiId();
        logVariable("userId", userId);
        logVariable("invitedAs", invitedAs);
        logVariable("deletedOn", deletedOn);
		try {
		    final UserModel userModel = getUserModel();
            final User invitedAsUser = userModel.readUser(invitedAs);
            if (null == invitedAsUser) {
                // delete remote data
                invitationSql.deleteEmail(userId, invitedAs);
            } else {
                // delete remote data
                invitationSql.delete(userId, invitedAsUser.getId());
                // send notification
                final IQWriter notification = createIQWriter("contact:invitationdeleted");
                notification.writeEMail("invitedAs", invitedAs);
                notification.writeJabberId("deletedBy", userId);
                notification.writeCalendar("deletedOn", deletedOn);
                final IQ notificationIQ = notification.getIQ();
                notificationIQ.setTo(invitedAsUser.getId().getJID());
                send(invitedAsUser.getId(), notificationIQ);
            }
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
                // create remote data
                invitationSql.createEmail(userId, extendTo);
                // extend the invitation via SMTP
                final MimeMessage mimeMessage = MessageFactory.createMimeMessage();
                createInvitation(mimeMessage, extendTo, userModel.readUser(userId));
                addRecipient(mimeMessage, extendTo);
                TransportManager.deliver(mimeMessage);
            } else {
                // create remote data
                invitationSql.create(userId, extendToUser.getId());
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

	List<Contact> read(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        assertIsAuthenticatedUser(userId);
		try {
			final List<JabberId> contactIds = contactSql.readIds(userId);
			final List<Contact> contacts = new LinkedList<Contact>();
			for (final JabberId contactId : contactIds) {
				contacts.add(read(userId, contactId));
			}
			return contacts;
		} catch (final Throwable t) {
			throw translateError(t);
		}
	}

    Contact read(final JabberId userId, final JabberId contactId) {
        logApiId();
        logVariable("userId", userId);
		logVariable("contactId", contactId);
        assertIsAuthenticatedUser(userId);
		try {
		    final User user = getUserModel().readUser(contactId);

            final Element vcard = vcardProvider.loadVCard(contactId.getUsername());
            final Contact contact = new Contact();
            contact.setId(user.getId());
            contact.setName((String) vcard.element("FN").getData());
            final Element orgElement = vcard.element("ORG");
            if (null != orgElement) {
                contact.setOrganization((String) orgElement.element("ORGNAME").getData());
            }
            final Element titleElement = vcard.element(VCardFields.TITLE);
            if (null != titleElement) {
                contact.setTitle((String) titleElement.getData());
            }
            contact.setVCard(vcard);
            contact.addAllEmails(userSql.readEmails(contactId, Boolean.TRUE));
            return contact;
	    } catch (final Throwable t) {
            throw translateError(t);
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
