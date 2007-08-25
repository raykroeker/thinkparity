/*
 * Created On: Jul 7, 2006 2:20:07 PM
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.*;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;

/**
 * <b>Title:</b>thinkParity OpheliaModel Contact IO Handler Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContactIOHandler extends AbstractIOHandler implements
        com.thinkparity.ophelia.model.io.handler.ContactIOHandler {

    /** Sql to create a contact. */
    private static final String SQL_CREATE =
        new StringBuilder("insert into CONTACT ")
        .append("(CONTACT_ID,CONTACT_VCARD) ")
        .append("values (?,?)")
        .toString();

    /** Sql to create contact email relationship. */
    private static final String SQL_CREATE_EMAIL_REL =
        new StringBuilder("insert into CONTACT_EMAIL_REL ")
        .append("(CONTACT_ID,EMAIL_ID) ")
        .append("values (?,?)")
        .toString();

    /** Sql to create an incoming e-mail invitation. */
    private static final String SQL_CREATE_IEI =
        new StringBuilder("insert into CONTACT_INVITATION_INCOMING_EMAIL ")
        .append("(CONTACT_INVITATION_ID,EMAIL_ID,EXTENDED_BY_USER_ID) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to create the common invitation data. */
    private static final String SQL_CREATE_INVITATION =
        new StringBuilder("insert into CONTACT_INVITATION ")
        .append("(CREATED_BY,CREATED_ON) ")
        .append("values (?,?)")
        .toString();

    /** Sql to create an incoming user invitation. */
    private static final String SQL_CREATE_IUI =
        new StringBuilder("insert into CONTACT_INVITATION_INCOMING_USER ")
        .append("(CONTACT_INVITATION_ID,EXTENDED_BY_USER_ID) ")
        .append("values (?,?)")
        .toString();

    /** Sql to create an outgoing invitation. */
    private static final String SQL_CREATE_OEI =
        new StringBuilder("insert into CONTACT_INVITATION_OUTGOING_EMAIL ")
        .append("(CONTACT_INVITATION_ID,EMAIL_ID) ")
        .append("values (?,?)")
        .toString();

    /** Sql to create an outgoing invitation. */
    private static final String SQL_CREATE_OUI =
        new StringBuilder("insert into CONTACT_INVITATION_OUTGOING_USER ")
        .append("(CONTACT_INVITATION_ID,USER_ID) ")
        .append("values (?,?)")
        .toString();

    /** Sql to delete a contact. */
    private static final String SQL_DELETE =
        new StringBuilder("delete from CONTACT where CONTACT_ID=?")
        .toString();

    /** Sql to delete e-mail relationship entries. */
    private static final String SQL_DELETE_EMAIL_REL =
        new StringBuilder("delete from CONTACT_EMAIL_REL ")
        .append("where CONTACT_ID=?")
        .toString();

    /** Sql to delete an incomining e-mail invitation. */
    private static final String SQL_DELETE_IEI =
        new StringBuilder("delete from CONTACT_INVITATION_INCOMING_EMAIL ")
        .append("where CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to delete common contact invitation data. */
    private static final String SQL_DELETE_INVITATION =
        new StringBuilder("delete from CONTACT_INVITATION ")
        .append("where CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to delete an incomining user invitation. */
    private static final String SQL_DELETE_IUI =
        new StringBuilder("delete from CONTACT_INVITATION_INCOMING_USER ")
        .append("where CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to delete an outgoing e-mail invitation. */
    private static final String SQL_DELETE_OEI =
        new StringBuilder("delete from CONTACT_INVITATION_OUTGOING_EMAIL ")
        .append("where CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to delete an outgoing user invitation. */
    private static final String SQL_DELETE_OUI =
        new StringBuilder("delete from CONTACT_INVITATION_OUTGOING_USER ")
        .append("where CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to determine contact existence. */
    private static final String SQL_DOES_EXIST_BY_EMAIL =
        new StringBuilder("select count(C.CONTACT_ID) \"CONTACT_COUNT\" ")
        .append("from CONTACT C ")
        .append("inner join CONTACT_EMAIL_REL CER ")
        .append("on CER.CONTACT_ID=C.CONTACT_ID ")
        .append("inner join EMAIL E on E.EMAIL_ID=CER.EMAIL_ID ")
        .append("where E.EMAIL=?")
        .toString();

    /** Sql to determine contact existence. */
    private static final String SQL_DOES_EXIST_BY_ID =
        new StringBuilder("select COUNT(CONTACT_ID) \"CONTACT_COUNT\" ")
        .append("from CONTACT C ")
        .append("inner join PARITY_USER PU on C.CONTACT_ID=PU.USER_ID ")
        .append("where PU.USER_ID=?")
        .toString();

    /** Sql to determine invitation existence. */
    private static final String SQL_DOES_EXIST_INVITATION =
        new StringBuilder("select count(CI.CONTACT_INVITATION_ID) \"INVITATION_COUNT\" ")
        .append("from CONTACT_INVITATION CI ")
        .append("where CI.CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to determine the existence of an outgoing e-mail invitation. */
    private static final String SQL_DOES_EXIST_OEI_EMAIL =
        new StringBuilder("select count(CI.CONTACT_INVITATION_ID) \"INVITATION_COUNT\" ")
        .append("from CONTACT_INVITATION CI ")
        .append("inner join CONTACT_INVITATION_OUTGOING_EMAIL CIOE ")
        .append("on CI.CONTACT_INVITATION_ID=CIOE.CONTACT_INVITATION_ID ")
        .append("inner join EMAIL E on E.EMAIL_ID=CIOE.EMAIL_ID ")
        .append("where E.EMAIL=?")
        .toString();

    /** Sql to determine the existence of an outgoing user invitation. */
    private static final String SQL_DOES_EXIST_OUI_USER_ID =
        new StringBuilder("select COUNT(CI.CONTACT_INVITATION_ID) \"INVITATION_COUNT\" ")
        .append("from CONTACT_INVITATION CI ")
        .append("inner join CONTACT_INVITATION_OUTGOING_USER CIOU ")
        .append("on CI.CONTACT_INVITATION_ID=CIOU.CONTACT_INVITATION_ID ")
        .append("where CIOU.USER_ID=?")
        .toString();

    /** Sql to read contacts. */
    private static final String SQL_READ =
        new StringBuilder("select C.CONTACT_ID,C.CONTACT_VCARD,U.USER_ID,")
        .append("U.JABBER_ID,U.NAME,U.ORGANIZATION,U.TITLE,U.FLAGS ")
        .append("from CONTACT C ")
        .append("inner join PARITY_USER U on C.CONTACT_ID=U.USER_ID ")
        .toString();

    /** Sql to read a contact by its email. */
    private static final String SQL_READ_BY_EMAIL =
        new StringBuilder(SQL_READ)
        .append("inner join CONTACT_EMAIL_REL CER on CER.CONTACT_ID=C.CONTACT_ID ")
        .append("inner join EMAIL E on E.EMAIL_ID=CER.EMAIL_ID ")
        .append("where E.EMAIL=?")
        .toString();

    /** Sql to read a contact by its id. */
    private static final String SQL_READ_BY_ID =
        new StringBuilder(SQL_READ)
        .append("where U.JABBER_ID=?")
        .toString();

    /** Sql to read contact e-mail addresses. */
    private static final String SQL_READ_EMAIL =
        new StringBuilder("select E.EMAIL ")
        .append("from CONTACT C ")
        .append("inner join CONTACT_EMAIL_REL CER on C.CONTACT_ID=CER.CONTACT_ID ")
        .append("inner join EMAIL E on CER.EMAIL_ID=E.EMAIL_ID ")
        .append("where C.CONTACT_ID=?")
        .toString();

    /** Sql to read all of the incoming e-mail invitations. */
    private static final String SQL_READ_IEI =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIIE.CONTACT_INVITATION_ID,PUEB.JABBER_ID,PUEB.USER_ID,")
        .append("PUEB.NAME,PUEB.ORGANIZATION,PUEB.TITLE,PUEB.FLAGS,E.EMAIL ")
        .append("from CONTACT_INVITATION_INCOMING_EMAIL CIIE ")
        .append("inner join CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIIE.CONTACT_INVITATION_ID ")
        .append("inner join PARITY_USER PUEB ")
        .append("on PUEB.USER_ID=CIIE.EXTENDED_BY_USER_ID ")
        .append("inner join EMAIL E on E.EMAIL_ID=CIIE.EMAIL_ID ")
        .toString();

    /** Sql to read all incoming e-mail invitations extended by a single user. */
    private static final String SQL_READ_IEI_EXTENDED_BY =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIIE.CONTACT_INVITATION_ID,PUEB.JABBER_ID,PUEB.USER_ID,")
        .append("PUEB.NAME,PUEB.ORGANIZATION,PUEB.TITLE,PUEB.FLAGS,E.EMAIL ")
        .append("from CONTACT_INVITATION_INCOMING_EMAIL CIIE ")
        .append("inner join CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIIE.CONTACT_INVITATION_ID ")
        .append("inner join PARITY_USER PUEB ")
        .append("on PUEB.USER_ID=CIIE.EXTENDED_BY_USER_ID ")
        .append("inner join EMAIL E on E.EMAIL_ID=CIIE.EMAIL_ID ")
        .append("where CIIE.EXTENDED_BY_USER_ID=?")
        .toString();

    /** Sql to read an incoming e-mail invitation by its unique key. */
    private static final String SQL_READ_IEI_EXTENDED_UK =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIIE.CONTACT_INVITATION_ID,PUEB.JABBER_ID,PUEB.USER_ID,")
        .append("PUEB.NAME,PUEB.ORGANIZATION,PUEB.TITLE,PUEB.FLAGS,E.EMAIL ")
        .append("from CONTACT_INVITATION_INCOMING_EMAIL CIIE ")
        .append("inner join CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIIE.CONTACT_INVITATION_ID ")
        .append("inner join PARITY_USER PUEB ")
        .append("on PUEB.USER_ID=CIIE.EXTENDED_BY_USER_ID ")
        .append("inner join EMAIL E on E.EMAIL_ID=CIIE.EMAIL_ID ")
        .append("where PUEB.USER_ID=? and E.EMAIL=?")
        .toString();

    /** Sql to read an incoming e-mail invitation by its id. */
    private static final String SQL_READ_IEI_ID =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIIE.CONTACT_INVITATION_ID,PUEB.JABBER_ID,PUEB.USER_ID,")
        .append("PUEB.NAME,PUEB.ORGANIZATION,PUEB.TITLE,PUEB.FLAGS,E.EMAIL ")
        .append("from CONTACT_INVITATION_INCOMING_EMAIL CIIE ")
        .append("inner join CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIIE.CONTACT_INVITATION_ID ")
        .append("inner join PARITY_USER PUEB ")
        .append("on PUEB.USER_ID=CIIE.EXTENDED_BY_USER_ID ")
        .append("inner join EMAIL E on E.EMAIL_ID=CIIE.EMAIL_ID ")
        .append("where CIIE.CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to read all incoming user invitations. */
    private static final String SQL_READ_IUI =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIIU.CONTACT_INVITATION_ID,PUEB.JABBER_ID,PUEB.USER_ID,")
        .append("PUEB.NAME,PUEB.ORGANIZATION,PUEB.TITLE,PUEB.FLAGS ")
        .append("from CONTACT_INVITATION_INCOMING_USER CIIU ")
        .append("inner join CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIIU.CONTACT_INVITATION_ID ")
        .append("inner join PARITY_USER PUEB on PUEB.USER_ID=CIIU.EXTENDED_BY_USER_ID ")
        .toString();

    /** Sql to read an incoming user invitation by the extended by user. */
    private static final String SQL_READ_IUI_EXTENDED_BY =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIIU.CONTACT_INVITATION_ID,PUEB.JABBER_ID,PUEB.USER_ID,")
        .append("PUEB.NAME,PUEB.ORGANIZATION,PUEB.TITLE,PUEB.FLAGS ")
        .append("from CONTACT_INVITATION_INCOMING_USER CIIU ")
        .append("inner join CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIIU.CONTACT_INVITATION_ID ")
        .append("inner join PARITY_USER PUEB on PUEB.USER_ID=CIIU.EXTENDED_BY_USER_ID ")
        .append("where PUEB.USER_ID=?")
        .toString();

    /** Sql to read an incoming user invitation by its primary key. */
    private static final String SQL_READ_IUI_PK =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIIU.CONTACT_INVITATION_ID,PUEB.JABBER_ID,PUEB.USER_ID,")
        .append("PUEB.NAME,PUEB.ORGANIZATION,PUEB.TITLE,PUEB.FLAGS ")
        .append("from CONTACT_INVITATION_INCOMING_USER CIIU ")
        .append("inner join CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIIU.CONTACT_INVITATION_ID ")
        .append("inner join PARITY_USER PUEB on PUEB.USER_ID=CIIU.EXTENDED_BY_USER_ID ")
        .append("where CIIU.CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to read all outgoing contact email invitations. */
    private static final String SQL_READ_OEI =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,E.EMAIL,")
        .append("CI.CONTACT_INVITATION_ID ")
        .append("from CONTACT_INVITATION CI ")
        .append("inner join CONTACT_INVITATION_OUTGOING_EMAIL CIOE on ")
        .append("CI.CONTACT_INVITATION_ID=CIOE.CONTACT_INVITATION_ID ")
        .append("inner join EMAIL E on CIOE.EMAIL_ID=E.EMAIL_ID ")
        .append("inner join PARITY_USER U on CI.CREATED_BY=U.USER_ID")
        .toString();

    /** Sql to read an outgoing contact invitation. */
    private static final String SQL_READ_OEI_EMAIL =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,E.EMAIL,")
        .append("CI.CONTACT_INVITATION_ID ")
        .append("from CONTACT_INVITATION CI ")
        .append("inner join CONTACT_INVITATION_OUTGOING_EMAIL CIOE on ")
        .append("CI.CONTACT_INVITATION_ID=CIOE.CONTACT_INVITATION_ID ")
        .append("inner join EMAIL E on CIOE.EMAIL_ID=E.EMAIL_ID ")
        .append("inner join PARITY_USER U on CI.CREATED_BY=U.USER_ID ")
        .append("where E.EMAIL=?")
        .toString();

    /** Sql to read an outgoing invitation by its id. */
    private static final String SQL_READ_OEI_ID =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,E.EMAIL,")
        .append("CI.CONTACT_INVITATION_ID ")
        .append("from CONTACT_INVITATION CI ")
        .append("inner join CONTACT_INVITATION_OUTGOING_EMAIL CIOE on ")
        .append("CI.CONTACT_INVITATION_ID=CIOE.CONTACT_INVITATION_ID ")
        .append("inner join EMAIL E on CIOE.EMAIL_ID=E.EMAIL_ID ")
        .append("inner join PARITY_USER U on CI.CREATED_BY=U.USER_ID ")
        .append("where CI.CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to read all outgoing contact user invitations. */
    private static final String SQL_READ_OUI =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CI.CONTACT_INVITATION_ID,IU.USER_ID ")
        .append("from CONTACT_INVITATION CI ")
        .append("inner join CONTACT_INVITATION_OUTGOING_USER CIOU on ")
        .append("CI.CONTACT_INVITATION_ID=CIOU.CONTACT_INVITATION_ID ")
        .append("inner join PARITY_USER IU on CIOU.USER_ID=IU.USER_ID ")
        .append("inner join PARITY_USER U on CI.CREATED_BY=U.USER_ID")
        .toString();

    /** Sql to read an outgoing user invitation by its id. */
    private static final String SQL_READ_OUI_ID =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CI.CONTACT_INVITATION_ID,IU.USER_ID ")
        .append("from CONTACT_INVITATION CI ")
        .append("inner join CONTACT_INVITATION_OUTGOING_USER CIOU on ")
        .append("CI.CONTACT_INVITATION_ID=CIOU.CONTACT_INVITATION_ID ")
        .append("inner join PARITY_USER IU on CIOU.USER_ID=IU.USER_ID ")
        .append("inner join PARITY_USER U on CI.CREATED_BY=U.USER_ID ")
        .append("where CI.CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to read an outgoing user invitation by the invited by user. */
    private static final String SQL_READ_OUI_INVITED_BY =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CI.CONTACT_INVITATION_ID,IU.USER_ID ")
        .append("from CONTACT_INVITATION CI ")
        .append("inner join CONTACT_INVITATION_OUTGOING_USER CIOU on ")
        .append("CI.CONTACT_INVITATION_ID=CIOU.CONTACT_INVITATION_ID ")
        .append("inner join PARITY_USER IU on CIOU.USER_ID=IU.USER_ID ")
        .append("inner join PARITY_USER U on CI.CREATED_BY=U.USER_ID ")
        .append("where IU.USER_ID=?")
        .toString();

    /** Sql to read a contact by its pk. */
    private static final String SQL_READ_PK =
        new StringBuilder(SQL_READ)
        .append("where C.CONTACT_ID=?")
        .toString();

    /** Sql to update a contact. */
    private static final String SQL_UPDATE =
        new StringBuilder("update CONTACT ")
        .append("set CONTACT_VCARD=? ")
        .append("where CONTACT_ID=?")
        .toString();

    /** The email db io. */
    private final EmailIOHandler emailIO;

    /** A profile db io. */
    private final ProfileIOHandler profileIO;

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
        this.profileIO = new ProfileIOHandler(dataSource);
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

            userIO.update(session, contact);

            createEMails(session, contact);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#createInvitation(com.thinkparity.codebase.model.contact.IncomingEMailInvitation)
     * 
     */
    public void createInvitation(final IncomingEMailInvitation invitation) {
        final Session session = openSession();
        try {
            // read e-mail
            final Long emailId = emailIO.readId(session,
                    invitation.getInvitationEMail());
            // create invitation
            createInvitation(session, invitation);
            // create incoming e-mail invitation
            session.prepareStatement(SQL_CREATE_IEI);
            session.setLong(1, invitation.getId());
            session.setLong(2, emailId);
            session.setLong(3, invitation.getExtendedBy().getLocalId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create incoming e-mail invitation.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#createInvitation(com.thinkparity.codebase.model.contact.IncomingUserInvitation)
     * 
     */
    public void createInvitation(final IncomingUserInvitation invitation) {
        final Session session = openSession();
        try {
            createInvitation(session, invitation);
            session.prepareStatement(SQL_CREATE_IUI);
            session.setLong(1, invitation.getId());
            session.setLong(2, invitation.getExtendedBy().getLocalId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create incoming user invitation.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#createInvitation(com.thinkparity.codebase.model.contact.OutgoingEMailInvitation)
     *
     */
    public void createInvitation(final OutgoingEMailInvitation outgoing) {
        final Session session = openSession();
        try {
            // create e-mail
            final Long emailId = emailIO.create(session,
                    outgoing.getInvitationEMail());
            // create invitation
            createInvitation(session, outgoing);
            // create outgoing e-mail invitation
            session.prepareStatement(SQL_CREATE_OEI);
            session.setLong(1, outgoing.getId());
            session.setLong(2, emailId);
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not create outgoing invitation.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#createInvitation(com.thinkparity.codebase.model.contact.OutgoingUserInvitation)
     *
     */
    public void createInvitation(final OutgoingUserInvitation outgoing) {
        final Session session = openSession();
        try {
            createInvitation(session, outgoing);
            session.prepareStatement(SQL_CREATE_OUI);
            session.setLong(1, outgoing.getId());
            session.setLong(2, outgoing.getInvitationUser().getLocalId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create outgoing invitation.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#delete(com.thinkparity.codebase.model.contact.Contact)
     * 
     */
    public void delete(final Contact contact) {
        final Session session = openSession();
        try {
            deleteEMails(session, contact);

            session.prepareStatement(SQL_DELETE);
            session.setLong(1, contact.getLocalId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not delete contact.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#deleteInvitation(com.thinkparity.codebase.model.contact.IncomingEMailInvitation)
     * 
     */
    public void deleteInvitation(final IncomingEMailInvitation invitation) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_IEI);
            session.setLong(1, invitation.getId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not delete incoming e-mail invitation.");
            deleteInvitation(session, invitation.getId());
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#deleteInvitation(com.thinkparity.codebase.model.contact.IncomingUserInvitation)
     *
     */
    public void deleteInvitation(final IncomingUserInvitation invitation) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_IUI);
            session.setLong(1, invitation.getId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not delete incoming user invitation.");
            deleteInvitation(session, invitation.getId());
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#deleteInvitation(com.thinkparity.codebase.model.contact.OutgoingEMailInvitation)
     *
     */
    public void deleteInvitation(OutgoingEMailInvitation invitation) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_OEI);
            session.setLong(1, invitation.getId());
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not delete outgoing invitation.");
            deleteInvitation(session, invitation.getId());
            deleteEMail(session, invitation.getInvitationEMail());
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#deleteInvitation(com.thinkparity.codebase.model.contact.OutgoingUserInvitation)
     *
     */
    public void deleteInvitation(final OutgoingUserInvitation invitation) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_OUI);
            session.setLong(1, invitation.getId());
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not delete outgoing invitation.");
            deleteInvitation(session, invitation.getId());
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#doesExist(com.thinkparity.codebase.email.EMail)
     *
     */
    public Boolean doesExist(final EMail email) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_BY_EMAIL);
            session.setEMail(1, email);
            session.executeQuery();
            if (session.nextResult()) {
                final int contactCount = session.getInteger("CONTACT_COUNT");
                if (0 == contactCount) {
                    return Boolean.FALSE;
                } else if (1 == contactCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine .");
                }
            } else {
                throw new HypersonicException("Could not determine .");
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#doesExist(java.lang.Long)
     *
     */
    public Boolean doesExist(final Long contactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_BY_ID);
            session.setLong(1, contactId);
            session.executeQuery();
            session.nextResult();
            if (0 == session.getInteger("CONTACT_COUNT")) {
                return Boolean.FALSE;
            } else if (1 == session.getInteger("CONTACT_COUNT")) {
                return Boolean.TRUE;
            } else {
                throw new HypersonicException("Could not determine contact existence.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#doesExistInvitation(com.thinkparity.codebase.model.contact.ContactInvitation)
     *
     */
    @Override
    public Boolean doesExistInvitation(final ContactInvitation invitation) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_INVITATION);
            session.setLong(1, invitation.getId());
            session.executeQuery();
            if (session.nextResult()) {
                final int invitationCount = session.getInteger("INVITATION_COUNT");
                if (0 == invitationCount) {
                    return Boolean.FALSE;
                } else if (1 == invitationCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine invitation existence.");
                }
            } else {
                throw new HypersonicException("Could not determine invitation existence.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#doesExistOutgoingEMailInvitation(com.thinkparity.codebase.email.EMail)
     *
     */
    public Boolean doesExistOutgoingEMailInvitation(final EMail email) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_OEI_EMAIL);
            session.setEMail(1, email);
            session.executeQuery();
            if (session.nextResult()) {
                final int invitationCount = session.getInteger("INVITATION_COUNT");
                if (0 == invitationCount) {
                    return Boolean.FALSE;
                } else if (1 == invitationCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine invitation existence.");
                }
            } else {
                throw new HypersonicException("Could not determine invitation existence.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#doesExistOutgoingUserInvitation(java.lang.Long)
     *
     */
    public Boolean doesExistOutgoingUserInvitation(final Long userId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_OUI_USER_ID);
            session.setLong(1, userId);
            session.executeQuery();
            session.nextResult();
            if (0 == session.getInteger("INVITATION_COUNT")) {
                return Boolean.FALSE;
            } else if (1 == session.getInteger("INVITATION_COUNT")) {
                return Boolean.TRUE;
            } else {
                throw new HypersonicException("Could not determine contact existence.");
            }
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
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#deleteOutgoingUserInvitation(java.lang.Long)
     * 
     */

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#read(com.thinkparity.codebase.email.EMail)
     *
     */
    public Contact read(EMail email) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_EMAIL);
            session.setEMail(1, email);
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
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#read(java.lang.Long)
     *
     */
    @Override
    public Contact read(final Long contactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_PK);
            session.setLong(1, contactId);
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
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readIncomingEMailInvitation(com.thinkparity.codebase.email.EMail,
     *      com.thinkparity.codebase.model.user.User)
     * 
     */
    public IncomingEMailInvitation readIncomingEMailInvitation(
            final EMail email, final User extendedBy) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_IEI_EXTENDED_UK);
            session.setLong(1, extendedBy.getLocalId());
            session.setEMail(2, email);
            session.executeQuery();
            if (session.nextResult()) {
                return extractIncomingEMailInvitation(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readIncomingEMailInvitation(java.lang.Long)
     *
     */
    public IncomingEMailInvitation readIncomingEMailInvitation(
            final Long invitationId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_IEI_ID);
            session.setLong(1, invitationId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractIncomingEMailInvitation(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readIncomingEMailInvitations()
     *
     */
    public List<IncomingEMailInvitation> readIncomingEMailInvitations() {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_IEI);
            session.executeQuery();
            final List<IncomingEMailInvitation> invitations = new ArrayList<IncomingEMailInvitation>();
            while (session.nextResult()) {
                invitations.add(extractIncomingEMailInvitation(session));
            }
            return invitations;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readIncomingEMailInvitations(com.thinkparity.codebase.model.user.User)
     *
     */
    public List<IncomingEMailInvitation> readIncomingEMailInvitations(final User extendedBy) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_IEI_EXTENDED_BY);
            session.setLong(1, extendedBy.getLocalId());
            session.executeQuery();
            final List<IncomingEMailInvitation> invitations = new ArrayList<IncomingEMailInvitation>();
            while (session.nextResult()) {
                invitations.add(extractIncomingEMailInvitation(session));
            }
            return invitations;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readIncomingUserInvitation(java.lang.Long)
     *
     */
    public IncomingUserInvitation readIncomingUserInvitation(final Long invitationId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_IUI_PK);
            session.setLong(1, invitationId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractIncomingUserInvitation(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readIncomingUserInvitation(com.thinkparity.codebase.model.user.User)
     *
     */
    public IncomingUserInvitation readIncomingUserInvitation(
            final User extendedBy) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_IUI_EXTENDED_BY);
            session.setLong(1, extendedBy.getLocalId());
            session.executeQuery();
            if (session.nextResult()) {
                return extractIncomingUserInvitation(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readIncomingUserInvitations()
     *
     */
    public List<IncomingUserInvitation> readIncomingUserInvitations() {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_IUI);
            session.executeQuery();
            final List<IncomingUserInvitation> invitations = new ArrayList<IncomingUserInvitation>();
            while (session.nextResult()) {
                invitations.add(extractIncomingUserInvitation(session));
            }
            return invitations;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readOutgoingEMailInvitation(com.thinkparity.codebase.email.EMail)
     * 
     */
    public OutgoingEMailInvitation readOutgoingEMailInvitation(final EMail email) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_OEI_EMAIL);
            session.setEMail(1, email);
            session.executeQuery();
            if(session.nextResult()) {
                return extractOutgoingEMailInvitation(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readOutgoingEMailInvitation(java.lang.Long)
     * 
     */
    public OutgoingEMailInvitation readOutgoingEMailInvitation(final Long invitationId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_OEI_ID);
            session.setLong(1, invitationId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractOutgoingEMailInvitation(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readOutgoingEMailInvitations()
     * 
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations() {
        final Session session = openSession();
        try {
            final List<OutgoingEMailInvitation> invitations = new ArrayList<OutgoingEMailInvitation>();
            session.prepareStatement(SQL_READ_OEI);
            session.executeQuery();
            while(session.nextResult()) {
                invitations.add(extractOutgoingEMailInvitation(session));
            }
            return invitations;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readOutgoingEMailInvitations(java.util.List)
     *
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations(final List<EMail> emails) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_OEI_EMAIL);
            final List<OutgoingEMailInvitation> invitations = new ArrayList<OutgoingEMailInvitation>();
            for (final EMail email : emails) {
                session.setEMail(1, email);
                session.executeQuery();
                while (session.nextResult()) {
                    invitations.add(extractOutgoingEMailInvitation(session));
                }
            }
            return invitations;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readOutgoingUserInvitation(java.lang.Long)
     *
     */
    public OutgoingUserInvitation readOutgoingUserInvitation(
            final Long invitationId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_OUI_ID);
            session.setLong(1, invitationId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractOutgoingUserInvitation(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readOutgoingUserInvitation(com.thinkparity.codebase.model.user.User)
     *
     */
    public OutgoingUserInvitation readOutgoingUserInvitation(
            final User invitationUser) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_OUI_INVITED_BY);
            session.setLong(1, invitationUser.getLocalId());
            session.executeQuery();
            if (session.nextResult()) {
                return extractOutgoingUserInvitation(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#readOutgoingUserInvitations()
     * 
     */
    public List<OutgoingUserInvitation> readOutgoingUserInvitations() {
        final Session session = openSession();
        try {
            final List<OutgoingUserInvitation> invitations = new ArrayList<OutgoingUserInvitation>();
            session.prepareStatement(SQL_READ_OUI);
            session.executeQuery();
            while(session.nextResult()) {
                invitations.add(extractOutgoingUserInvitation(session));
            }
            return invitations;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContactIOHandler#update(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.Contact)
     * 
     */
    public void update(final Contact contact) {
        final Session session = openSession();
        try {
            // update user
            userIO.update(session, contact);

            // update contact
            session.prepareStatement(SQL_UPDATE);
            session.setVCard(1, contact.getVCard());
            session.setLong(2, contact.getLocalId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not update contact.");

            // update e-mails
            deleteEMails(session, contact);
            createEMails(session, contact);
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
        contact.addAllEmails(readEMails(contact));
        contact.setFlags(session.getUserFlags("FLAGS"));
        return contact;
    }

    
    /**
     * Extract an incoming e-mail invitation from the session.
     * 
     * @param session
     *            A database session.
     * @return An <code>IncomingEMailInvitation</code>.
     */
    IncomingEMailInvitation extractIncomingEMailInvitation(final Session session) {
        final IncomingEMailInvitation incoming = new IncomingEMailInvitation();
        extractInvitation(session, incoming);
        incoming.setExtendedBy(userIO.extractUser(session));
        incoming.setInvitationEMail(session.getEMail("EMAIL"));
        return incoming;
    }

    /**
     * Extract an incoming e-mail invitation from the session.
     * 
     * @param session
     *            A database session.
     * @return An <code>IncomingEMailInvitation</code>.
     */
    IncomingUserInvitation extractIncomingUserInvitation(final Session session) {
        final IncomingUserInvitation incoming = new IncomingUserInvitation();
        extractInvitation(session, incoming);
        incoming.setExtendedBy(userIO.extractUser(session));
        incoming.setInvitationUser(profileIO.read(session));
        return incoming;
    }

    /**
     * Extract a contact invitation from the session.
     * 
     * @param session
     *            A database session.
     * @return A contact invitation.
     */
    OutgoingEMailInvitation extractOutgoingEMailInvitation(final Session session) {
        final OutgoingEMailInvitation outgoing = new OutgoingEMailInvitation();
        extractInvitation(session, outgoing);
        outgoing.setInvitationEMail(session.getEMail("EMAIL"));
        return outgoing;
    }

    /**
     * Extract a contact invitation from the session.
     * 
     * @param session
     *            A database session.
     * @return A contact invitation.
     */
    OutgoingUserInvitation extractOutgoingUserInvitation(final Session session) {
        final OutgoingUserInvitation outgoing = new OutgoingUserInvitation();
        extractInvitation(session, outgoing);
        outgoing.setInvitationUser(userIO.read(session.getLong("USER_ID")));
        return outgoing;
    }

    /**
     * Create the e-mails for the contact.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param contact
     *            A <code>Contact</code>.
     */
    private void createEMails(final Session session, final Contact contact) {
        final List<Long> emailIds = new ArrayList<Long>(contact.getEmailsSize());
        for (final EMail email : contact.getEmails()) {
            emailIds.add(emailIO.create(session, email));
        }
        session.prepareStatement(SQL_CREATE_EMAIL_REL);
        session.setLong(1, contact.getLocalId());
        for (final Long emailId : emailIds) {
            session.setLong(2, emailId);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create contact email rel.");
        }
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
        session.setLong(1, invitation.getCreatedBy().getLocalId());
        session.setCalendar(2, invitation.getCreatedOn());
        if(1 != session.executeUpdate())
            throw new HypersonicException("COULD NOT CREATE INVITATION");
        invitation.setId(session.getIdentity("CONTACT_INVITATION"));
    }

    /**
     * Delete an e-mail address.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param email
     *            An <code>EMail</code> address.
     */
    private void deleteEMail(final Session session, final EMail email) {
        final Long emailId = emailIO.readId(session, email);
        emailIO.delete(session, emailId);
    }

    /**
     * Delete all persisted e-mails for a contact.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param contact
     *            A <code>Contact</code>.
     */
    private void deleteEMails(final Session session, final Contact contact) {
        final List<EMail> emails = readEMails(session, contact);
        final List<Long> emailIds = new ArrayList<Long>(emails.size());
        for (final EMail email : emails) {
            emailIds.add(emailIO.readId(session, email));
        }

        // delete all e-mail relationship entries
        session.prepareStatement(SQL_DELETE_EMAIL_REL);
        session.setLong(1, contact.getLocalId());
        if (emailIds.size() != session.executeUpdate())
            throw new HypersonicException("Could not delete email rel.");

        // delete all e-mails
        for (final Long emailId : emailIds) {
            emailIO.delete(session, emailId);
        }
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
        invitation.setCreatedBy(userIO.read(session.getLong("CREATED_BY")));
        invitation.setCreatedOn(session.getCalendar("CREATED_ON"));
        invitation.setId(session.getLong("CONTACT_INVITATION_ID"));
    }

    /**
     * Read all persisted e-mail addresses for a contact.
     * 
     * @param contact
     *            A <code>Contact</code>.
     * @return A <code>List</code> of <code>EMail</code> addresses.
     */
    private List<EMail> readEMails(final Contact contact) {
        final Session session = openSession();
        try {
            return readEMails(session, contact);
        } finally {
            session.close();
        }
    }

    /**
     * Read all persisted e-mail addresses for a contact.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param contact
     *            A <code>Contact</code>.
     * @return A <code>List</code> of <code>EMail</code> addresses.
     */
    private List<EMail> readEMails(final Session session, final Contact contact) {
        session.prepareStatement(SQL_READ_EMAIL);
        session.setLong(1, contact.getLocalId());
        session.executeQuery();
        final List<EMail> emails = new ArrayList<EMail>();
        while (session.nextResult()) {
            emails.add(emailIO.extractEMail(session));
        }
        return emails;
    }
}
