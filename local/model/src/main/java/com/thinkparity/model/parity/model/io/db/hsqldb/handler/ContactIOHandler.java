/*
 * Created On: Jul 7, 2006 2:20:07 PM
 */
package com.thinkparity.model.parity.model.io.db.hsqldb.handler;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.contact.ContactInvitation;
import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.model.parity.model.io.db.hsqldb.Session;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class ContactIOHandler extends AbstractIOHandler implements
        com.thinkparity.model.parity.model.io.handler.ContactIOHandler {

    /** Sql to create a contact invitation. */
    private static final String SQL_CREATE_INVITATION =
            new StringBuffer("insert into CONTACT_INVITATION ")
            .append("(EMAIL,CREATED_ON,CREATED_BY) ")
            .append("values (?,?,?)")
            .toString();

    /** Sql to read contacts. */
    private static final String SQL_READ =
            new StringBuffer("select C.CONTACT_ID,U.USER_ID,U.JABBER_ID,")
            .append("U.NAME,U.ORGANIZATION ")
            .append("from CONTACT C ")
            .append("inner join USER U on C.CONTACT_ID=U.USER_ID ")
            .toString();

    /** Sql to read a contact from an e-mail. */
    private static final String SQL_READ_BY_EMAIL =
            new StringBuffer("select ")
            .append("from CONTACT ")
            .append("")
            .toString();

    /** Sql to read contact e-mail addresses. */
    private static final String SQL_READ_EMAIL =
            new StringBuffer("select CE.EMAIL")
            .append("from CONTACT C ")
            .append("inner join CONTACT_EMAIL CE on C.CONTACT_ID=CE.CONTACT_ID ")
            .append("where C.CONTACT_ID=?")
            .toString();

    /** Sql to read a contact invitation. */
    private static final String SQL_READ_INVITATION =
            new StringBuffer("select U.JABBER_ID CREATED_BY,CI.CREATED_ON,CI.EMAIL,CI.CONTACT_INVITATION_ID ")
            .append("from CONTACT_INVITATION CI ")
            .append("inner join USER U on CI.CREATED_BY = U.USER_ID ")
            .append("where CI.EMAIL=?")
            .toString();

    /** Sql to read contact invitations. */
    private static final String SQL_READ_INVITATIONS =
            new StringBuffer("select U.JABBER_ID CREATED_BY,CI.CREATED_ON,CI.EMAIL,CI.CONTACT_INVITATION_ID ")
            .append("from CONTACT_INVITATION CI ")
            .append("inner join USER U on CI.CREATED_BY = U.USER_ID")
            .toString();

    /**
     * Return the api id.
     * 
     * @param api
     *            The api.
     * @return The api id.
     */
    private static StringBuffer getApiId(final String api) {
        return getIOId("CONTACT").append(" ").append(api);
    }

    /**
     * Obtain an error id.
     * 
     * @param api
     *            The api.
     * @param error
     *            The error.
     * @return The error id.
     */
    private static String getErrorId(final String api, final String error) {
        return getApiId(api).append(" ").append(error).toString();
    }

    /** Create ContactIOHandler. */
    public ContactIOHandler() { super(); }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContactIOHandler#create()
     */
    public void create() {
        throw Assert.createNotYetImplemented("ContactIOHandler#create");
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContactIOHandler#createInvitation(com.thinkparity.model.parity.model.contact.ContactInvitation,
     *      com.thinkparity.model.xmpp.user.User)
     * 
     */
    public void createInvitation(final ContactInvitation invitation,
            final User createdBy) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_INVITATION);
            session.setString(1, invitation.getEmail());
            session.setCalendar(2, invitation.getCreatedOn());
            session.setLong(3, createdBy.getLocalId());
            if(1 != session.executeUpdate())
                throw new HypersonicException(getErrorId("[CREATE INVITATION]", "[COULD NOT CREATE INVITATION]"));
            session.commit();
        }
        catch(final HypersonicException hx) {
            session.close();
            throw hx;
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContactIOHandler#delete()
     */
    public void delete() {
        throw Assert.createNotYetImplemented("ContactIOHandler#delete");
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContactIOHandler#read()
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
     * @see com.thinkparity.model.parity.model.io.handler.ContactIOHandler#read(java.lang.String)
     */
    public Contact read(final String email) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_EMAIL);
            session.setString(1, email);
            session.executeQuery();
            if(session.nextResult()) { return extractContact(session); }
            else { return null; }
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContactIOHandler#readInvitation(java.lang.String)
     * 
     */
    public ContactInvitation readInvitation(final String email) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_INVITATION);
            session.setString(1, email);
            session.executeQuery();
            if(session.nextResult()) { return extractInvitation(session); }
            else { return null; }
        }
        finally { session.close(); }
    }

    /**
     * @see com.thinkparity.model.parity.model.io.handler.ContactIOHandler#readInvitations()
     * 
     */
    public List<ContactInvitation> readInvitations() {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_INVITATIONS);
            session.executeQuery();

            final List<ContactInvitation> invitations = new ArrayList<ContactInvitation>();
            while(session.nextResult()) {
                invitations.add(extractInvitation(session));
            }
            return invitations;
        }
        finally { session.close(); }
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
        contact.setName(session.getString("NAME"));
        contact.setOrganization(session.getString("ORGANIZATION"));
        contact.addAllEmails(readEmails(contact.getLocalId()));
        return contact;
    }

    /**
     * Extract an e-mail address from the session.
     * 
     * @param session
     *            A database session.
     * @return An e-mail address.
     */
    String extractEmail(final Session session) {
        return session.getString("EMAIL");
    }

    /**
     * Extract a contact invitation from the session.
     * 
     * @param session
     *            A database session.
     * @return A contact invitation.
     */
    ContactInvitation extractInvitation(final Session session) {
        final ContactInvitation invitation = new ContactInvitation();
        invitation.setCreatedBy(session.getQualifiedUsername("CREATED_BY"));
        invitation.setCreatedOn(session.getCalendar("CREATED_ON"));
        invitation.setEmail(session.getString("EMAIL"));
        invitation.setId(session.getLong("CONTACT_INVITATION_ID"));
        return invitation;
    }

    /**
     * Read a list of e-mail addresses for a contact.
     * 
     * @param contactId
     *            A contact id.
     * @return A list of e-mail addresses.
     */
    private List<String> readEmails(final Long contactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_EMAIL);
            session.setLong(1, contactId);
            final List<String> emails = new ArrayList<String>();
            while(session.nextResult()) { emails.add(extractEmail(session)); }
            return emails;
        }
        finally { session.close(); }
    }
}
