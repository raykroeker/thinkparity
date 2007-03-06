/*
 * Created On: Jul 7, 2006 2:20:07 PM
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.ContactVCard;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.contact.ContactInvitation;
import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;
import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class ContactIOHandler extends AbstractIOHandler implements
        com.thinkparity.ophelia.model.io.handler.ContactIOHandler {

    /** Sql to create a contact. */
    private static final String SQL_CREATE =
            new StringBuffer("insert into CONTACT ")
            .append("(CONTACT_ID,CONTACT_VCARD) ")
            .append("values (?,?)")
            .toString();

    /** Sql to create contact email relationship. */
    private static final String SQL_CREATE_EMAIL_REL =
            new StringBuffer("insert into CONTACT_EMAIL_REL ")
            .append("(CONTACT_ID,EMAIL_ID) ")
            .append("values (?,?)")
            .toString();

    /** Sql to create an incoming invitation. */
    private static final String SQL_CREATE_INCOMING_INVITATION =
            new StringBuffer("insert into CONTACT_INVITATION_INCOMING ")
            .append("(CONTACT_INVITATION_ID,INVITED_AS,INVITED_BY) ")
            .append("values (?,?,?)")
            .toString();

    /** Sql to create the common invitation data. */
    private static final String SQL_CREATE_INVITATION =
            new StringBuffer("insert into CONTACT_INVITATION ")
            .append("(CREATED_BY,CREATED_ON) ")
            .append("values (?,?)")
            .toString();

    /** Sql to create an outgoing invitation. */
    private static final String SQL_CREATE_OUTGOING_INVITATION =
            new StringBuffer("insert into CONTACT_INVITATION_OUTGOING ")
            .append("(CONTACT_INVITATION_ID,EMAIL_ID) ")
            .append("values (?,?)")
            .toString();

    /** Sql to delete a contact. */
    private static final String SQL_DELETE =
            new StringBuffer("delete ")
            .append("from CONTACT ")
            .append("where CONTACT_ID=?")
            .toString();

    /** Sql to delete contact email relationship. */
    private static final String SQL_DELETE_EMAIL_REL =
            new StringBuffer("delete ")
            .append("from CONTACT_EMAIL_REL ")
            .append("where CONTACT_ID=? and EMAIL_ID=?")
            .toString();

    /** Sql to delete an incoming invitation. */
    private static final String SQL_DELETE_INCOMING_INVITATION =
            new StringBuffer("delete from CONTACT_INVITATION_INCOMING ")
            .append("where CONTACT_INVITATION_ID=?")
            .toString();

    /** Sql to delete common contact invitation data. */
    private static final String SQL_DELETE_INVITATION =
            new StringBuffer("delete from CONTACT_INVITATION ")
            .append("where CONTACT_INVITATION_ID=?")
            .toString();

    /** Sql to delete an outgoing contact invitation. */
    private static final String SQL_DELETE_OUTGOING_INVITATION =
            new StringBuffer("Delete from CONTACT_INVITATION_OUTGOING ")
            .append("where CONTACT_INVITATION_ID=?")
            .toString();

    /** Sql to read contacts. */
    private static final String SQL_READ =
            new StringBuffer("select C.CONTACT_ID,C.CONTACT_VCARD,U.USER_ID,")
            .append("U.JABBER_ID,U.NAME,U.ORGANIZATION,U.TITLE ")
            .append("from CONTACT C ")
            .append("inner join PARITY_USER U on C.CONTACT_ID=U.USER_ID ")
            .toString();

    /** Sql to read a contact by its id. */
    private static final String SQL_READ_BY_ID =
            new StringBuffer(SQL_READ)
            .append("where U.JABBER_ID=?")
            .toString();

    /** Sql to read contact e-mail addresses. */
    private static final String SQL_READ_EMAIL =
            new StringBuffer("select E.EMAIL ")
            .append("from CONTACT C ")
            .append("inner join CONTACT_EMAIL_REL CER on C.CONTACT_ID=CER.CONTACT_ID ")
            .append("inner join EMAIL E on CER.EMAIL_ID=E.EMAIL_ID ")
            .append("where C.CONTACT_ID=?")
            .toString();

    /** Sql to read an incoming contact invitation. */
    private static final String SQL_READ_INCOMING_INVITATION =
            new StringBuffer("select CI.CREATED_BY,CI.CREATED_ON,")
            .append("UI.JABBER_ID,CI.CONTACT_INVITATION_ID,E.EMAIL ")
            .append("from CONTACT_INVITATION CI ")
            .append("inner join CONTACT_INVITATION_INCOMING CII on ")
            .append("CI.CONTACT_INVITATION_ID=CII.CONTACT_INVITATION_ID ")
            .append("inner join PARITY_USER UI on CII.INVITED_BY=UI.USER_ID ")
            .append("inner join EMAIL E on CII.INVITED_AS=E.EMAIL_ID ")
            .append("inner join PARITY_USER U on CI.CREATED_BY=U.USER_ID ")
            .append("where CII.CONTACT_INVITATION_ID=?")
            .toString();

    /** Sql to read an incoming contact invitation. */
    private static final String SQL_READ_INCOMING_INVITATION_ID =
            new StringBuffer("select CI.CONTACT_INVITATION_ID ")
            .append("from CONTACT_INVITATION CI ")
            .append("inner join CONTACT_INVITATION_INCOMING CII on ")
            .append("CI.CONTACT_INVITATION_ID=CII.CONTACT_INVITATION_ID ")
            .append("inner join PARITY_USER U on CII.INVITED_BY=U.USER_ID ")
            .append("where U.JABBER_ID=?")
            .toString();

    /** Sql to read all incoming contact invitation. */
    private static final String SQL_READ_INCOMING_INVITATIONS =
            new StringBuffer("select CI.CREATED_BY,CI.CREATED_ON,")
            .append("UI.JABBER_ID,CI.CONTACT_INVITATION_ID,E.EMAIL ")
            .append("from CONTACT_INVITATION CI ")
            .append("inner join CONTACT_INVITATION_INCOMING CII on ")
            .append("CI.CONTACT_INVITATION_ID=CII.CONTACT_INVITATION_ID ")
            .append("inner join PARITY_USER UI on CII.INVITED_BY=UI.USER_ID ")
            .append("inner join EMAIL E on CII.INVITED_AS=E.EMAIL_ID ")
            .append("inner join PARITY_USER U on CI.CREATED_BY=U.USER_ID ")
            .toString();
   
    /** Sql to read an outgoing contact invitation. */
    private static final String SQL_READ_OUTGOING_INVITATION_BY_EMAIL =
            new StringBuffer("select U.JABBER_ID,CI.CREATED_BY,CI.CREATED_ON,")
            .append("E.EMAIL,CI.CONTACT_INVITATION_ID ")
            .append("from CONTACT_INVITATION CI ")
            .append("inner join CONTACT_INVITATION_OUTGOING CIO on ")
            .append("CI.CONTACT_INVITATION_ID=CIO.CONTACT_INVITATION_ID ")
            .append("inner join EMAIL E on CIO.EMAIL_ID=E.EMAIL_ID ")
            .append("inner join PARITY_USER U on CI.CREATED_BY=U.USER_ID ")
            .append("where E.EMAIL=?")
            .toString();
   
    /** Sql to read an outgoing invitation by its id. */
    private static final String SQL_READ_OUTGOING_INVITATION_BY_ID =
            new StringBuffer("select U.JABBER_ID,CI.CREATED_BY,CI.CREATED_ON,")
            .append("E.EMAIL,CI.CONTACT_INVITATION_ID ")
            .append("from CONTACT_INVITATION CI ")
            .append("inner join CONTACT_INVITATION_OUTGOING CIO on ")
            .append("CI.CONTACT_INVITATION_ID=CIO.CONTACT_INVITATION_ID ")
            .append("inner join EMAIL E on CIO.EMAIL_ID=E.EMAIL_ID ")
            .append("inner join PARITY_USER U on CI.CREATED_BY=U.USER_ID ")
            .append("where CI.CONTACT_INVITATION_ID=?")
            .toString();

    /** Sql to read all outgoing contact invitations. */
    private static final String SQL_READ_OUTGOING_INVITATIONS =
            new StringBuffer("select U.JABBER_ID,CI.CREATED_BY,CI.CREATED_ON,")
            .append("E.EMAIL,CI.CONTACT_INVITATION_ID ")
            .append("from CONTACT_INVITATION CI ")
            .append("inner join CONTACT_INVITATION_OUTGOING CIO on ")
            .append("CI.CONTACT_INVITATION_ID=CIO.CONTACT_INVITATION_ID ")
            .append("inner join EMAIL E on CIO.EMAIL_ID=E.EMAIL_ID ")
            .append("inner join PARITY_USER U on CI.CREATED_BY=U.USER_ID")
            .toString();

    /** Sql to update a contact. */
    private static final String SQL_UPDATE =
            new StringBuffer("update CONTACT ")
            .append("set CONTACT_VCARD=? ")
            .append("where CONTACT_ID=?")
            .toString();

    /** The email db io. */
    private final EmailIOHandler emailIO;

    /** A user db io. */
    private final UserIOHandler userIO;

    /**
     * Create ContactIOHandler.
     * 
     * @param dataSource
     *            An sql <code>DataSource</code>.
     */
    public ContactIOHandler(final DataSource dataSource) {
        super(dataSource);
        this.emailIO = new EmailIOHandler(dataSource);
        this.userIO = new UserIOHandler(dataSource);
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#create(com.thinkparity.codebase.model.contact.Contact)
     * 
     */
    public void create(final Contact contact) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_CREATE);
            session.setLong(1, contact.getLocalId());
            session.setVCard(2, contact.getVCard());
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not create contact.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#createEmail(java.lang.Long, com.thinkparity.codebase.email.EMail)
     */
    public void createEmail(final Long contactId, final EMail email) {
        final Session session = openSession();
        try {
            final Long emailId = emailIO.create(session, email);
            session.prepareStatement(SQL_CREATE_EMAIL_REL);
            session.setLong(1, contactId);
            session.setLong(2, emailId);
            if (1 != session.executeUpdate())
                throw new HypersonicException("COULD NOT CREATE EMAIL");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#createIncomingInvitation(com.thinkparity.ophelia.model.contact.IncomingInvitation)
     * 
     */
    public void createIncomingInvitation(final IncomingInvitation incoming,
            final User user) {
        final Session session = openSession();
        try {
            final Long emailId = emailIO.readId(session, incoming.getInvitedAs());
            createInvitation(session, incoming);

            session.prepareStatement(SQL_CREATE_INCOMING_INVITATION);
            session.setLong(1, incoming.getId());
            session.setLong(2, emailId);
            session.setLong(3, user.getLocalId());
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not create incoming invitation.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#createOutgoingInvitation(com.thinkparity.ophelia.model.contact.OutgoingInvitation)
     * 
     */
    public void createOutgoingInvitation(final OutgoingInvitation outgoing) {
        final Session session = openSession();
        try {
            createInvitation(session, outgoing);
            final Long emailId = emailIO.create(session, outgoing.getEmail());

            session.prepareStatement(SQL_CREATE_OUTGOING_INVITATION);
            session.setLong(1, outgoing.getId());
            session.setLong(2, emailId);
            if(1 != session.executeUpdate())
                throw new HypersonicException("COuld not create outgoing invitation.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#delete(java.lang.Long)
     */
    public void delete(final Long contactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE);
            session.setLong(1, contactId);
            if (1 != session.executeUpdate())
                throw new HypersonicException("COULD NOT DELETE CONTACT");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#deleteEmail(java.lang.Long, com.thinkparity.codebase.email.EMail)
     */
    public void deleteEmail(final Long contactId, final EMail email) {
        final Session session = openSession();
        try {
            final Long emailId = emailIO.readId(session, email);
            session.prepareStatement(SQL_DELETE_EMAIL_REL);
            session.setLong(1, contactId);
            session.setLong(2, emailId);
            if (1 != session.executeUpdate())
                throw new HypersonicException("COULD NOT DELETE EMAIL");
            emailIO.delete(session, emailId);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#deleteEMail(com.thinkparity.codebase.email.EMail)
     * 
     */
    public void deleteEMail(final EMail email) {
        final Session session = openSession();
        try {
            final Long emailId = emailIO.readId(session, email);
            emailIO.delete(session, emailId);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#deleteIncomingInvitation(java.lang.Long)
     */
    public void deleteIncomingInvitation(final Long invitationId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_INCOMING_INVITATION);
            session.setLong(1, invitationId);
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not deleting incoming invitation.");
            deleteInvitation(session, invitationId);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#deleteOutgoingInvitation(java.lang.Long)
     */
    public void deleteOutgoingInvitation(final Long invitationId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_OUTGOING_INVITATION);
            session.setLong(1, invitationId);
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not delete outgoing invitation.");
            deleteInvitation(session, invitationId);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#read()
     */
    public List<Contact> read() {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ);
            session.executeQuery();
            final List<Contact> contacts = new ArrayList<Contact>();
            while(session.nextResult()) {
                contacts.add(extractContact(session));
            }
            return contacts;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#read(com.thinkparity.codebase.jabber.JabberId)
     */
    public Contact read(final JabberId contactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_ID);
            session.setQualifiedUsername(1, contactId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractContact(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * Read a list of e-mail addresses for a contact.
     * 
     * @param contactId
     *            A contact id.
     * @return A list of e-mail addresses.
     */
    public List<EMail> readEmails(final Long contactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_EMAIL);
            session.setLong(1, contactId);
            session.executeQuery();
            final List<EMail> emails = new ArrayList<EMail>();
            while (session.nextResult()) {
                emails.add(emailIO.extractEMail(session));
            }
            return emails;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readIncomingInvitation(java.lang.Long)
     */
    public IncomingInvitation readIncomingInvitation(Long invitationId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_INCOMING_INVITATION);
            session.setLong(1, invitationId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractIncomingInvitation(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readIncomingInvitation(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public IncomingInvitation readIncomingInvitation(final JabberId invitedBy) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_INCOMING_INVITATION_ID);
            session.setQualifiedUsername(1, invitedBy);
            session.executeQuery();
            if (session.nextResult()) {
                return readIncomingInvitation(
                        session.getLong("CONTACT_INVITATION_ID"));
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readIncomingInvitations()
     */
    public List<IncomingInvitation> readIncomingInvitations() {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_INCOMING_INVITATIONS);
            session.executeQuery();
            final List<IncomingInvitation> invitations = new ArrayList<IncomingInvitation>();
            while(session.nextResult()) {
                invitations.add(extractIncomingInvitation(session));
            }
            return invitations;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readInvitations()
     * 
     */
    public List<ContactInvitation> readInvitations() {
        throw Assert
                .createNotYetImplemented("ContactIOHandler#readInvitations");
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readInvitation(java.lang.String)
     * 
     */
    public OutgoingInvitation readOutgoingInvitation(final EMail email) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_OUTGOING_INVITATION_BY_EMAIL);
            session.setEMail(1, email);
            session.executeQuery();
            if(session.nextResult()) { return extractOutgoingInvitation(session); }
            else { return null; }
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readOutgoingInvitation(java.lang.Long)
     */
    public OutgoingInvitation readOutgoingInvitation(Long invitationId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_OUTGOING_INVITATION_BY_ID);
            session.setLong(1, invitationId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractOutgoingInvitation(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readOutgoingInvitations()
     */
    public List<OutgoingInvitation> readOutgoingInvitations() {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_OUTGOING_INVITATIONS);
            session.executeQuery();
            final List<OutgoingInvitation> invitations = new ArrayList<OutgoingInvitation>();
            while(session.nextResult()) {
                invitations.add(extractOutgoingInvitation(session));
            }
            return invitations;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#update(com.thinkparity.codebase.jabber.JabberId, com.thinkparity.codebase.model.contact.Contact)
     */
    public void update(final Contact contact) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE);
            session.setVCard(1, contact.getVCard());
            session.setLong(2, contact.getLocalId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("COULD NOT UPDATE CONTACT");

            userIO.update(session, contact);
        } finally {
            session.close();
        }
    }

    /**
     * Extract a contact from the database session.
     * 
     * @param session
     *            A database session.
     * @return A contact.
     */
    Contact extractContact(final Session session) {
        final Contact contact = new Contact();
        contact.setId(session.getQualifiedUsername("JABBER_ID"));
        contact.setLocalId(session.getLong("USER_ID"));
        contact.setVCard(session.getVCard("CONTACT_VCARD", new ContactVCard()));
        contact.setName(session.getString("NAME"));
        contact.setOrganization(session.getString("ORGANIZATION"));
        contact.setTitle(session.getString("TITLE"));
        contact.addAllEmails(readEmails(contact.getLocalId()));

        contact.setFlags(userIO.readFlags(contact.getLocalId()));
        return contact;
    }

    /**
     * Extract an incoming contact invitation from the session.
     * 
     * @param session
     *            A database session.
     * @return An incoming contact invitation.
     */
    IncomingInvitation extractIncomingInvitation(final Session session) {
        final IncomingInvitation incoming = new IncomingInvitation();
        extractInvitation(session, incoming);
        incoming.setInvitedAs(emailIO.extractEMail(session));
        incoming.setInvitedBy(session.getQualifiedUsername("JABBER_ID"));
        return incoming;
    }

    /**
     * Extract a contact invitation from the session.
     * 
     * @param session
     *            A database session.
     * @return A contact invitation.
     */
    OutgoingInvitation extractOutgoingInvitation(final Session session) {
        final OutgoingInvitation outgoing = new OutgoingInvitation();
        extractInvitation(session, outgoing);
        outgoing.setEmail(emailIO.extractEMail(session));
        return outgoing;
    }

    /**
     * Create the common invivation data.
     * 
     * @param session
     *            A database session.
     * @param invitation
     *            An invitation.
     */
    private void createInvitation(final Session session,
            final ContactInvitation invitation) {
        session.prepareStatement(SQL_CREATE_INVITATION);
        session.setLong(1, invitation.getCreatedBy());
        session.setCalendar(2, invitation.getCreatedOn());
        if(1 != session.executeUpdate())
            throw new HypersonicException("COULD NOT CREATE INVITATION");
        invitation.setId(session.getIdentity("CONTACT_INVITATION"));
    }

    /**
     * Delete the common invitation data.
     * 
     * @param session
     *            A database session.
     * @param invitationId
     *            An invitation id.
     */
    private void deleteInvitation(final Session session, final Long invitationId) {
        session.prepareStatement(SQL_DELETE_INVITATION);
        session.setLong(1, invitationId);
        if(1 != session.executeUpdate())
            throw new HypersonicException("Could not delete invitation.");
    }

    /**
     * Extract common invitation data from the session into the invitation.
     * 
     * @param session
     *            A database session.
     * @param invitation
     *            An invitation.
     */
    private void extractInvitation(final Session session,
            final ContactInvitation invitation) {
        invitation.setCreatedBy(session.getLong("CREATED_BY"));
        invitation.setCreatedOn(session.getCalendar("CREATED_ON"));
        invitation.setId(session.getLong("CONTACT_INVITATION_ID"));
    }
}
