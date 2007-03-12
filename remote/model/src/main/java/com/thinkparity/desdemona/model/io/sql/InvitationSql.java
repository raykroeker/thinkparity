/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.model.io.sql;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.contact.ContactInvitation;
import com.thinkparity.codebase.model.contact.IncomingInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Contact Invitation SQL
 * Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class InvitationSql extends AbstractSql {

    /** Sql to create a generic invitation. */
    private static final String SQL_CREATE =
        new StringBuilder("insert into CONTACT_INVITATION ")
        .append("(CREATED_BY,CREATED_ON) ")
        .append("values (?,?)")
        .toString();

    /** Sql to create an incomint invitation. */
    private static final String SQL_CREATE_INCOMING =
        new StringBuilder("insert into CONTACT_INVITATION_INCOMING ")
        .append("(CONTACT_INVITATION_ID,USER_ID,EMAIL_ID,FROM_USER_ID) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to create an outgoing e-mail invitation. */
    private static final String SQL_CREATE_OUTGOING_EMAIL =
        new StringBuilder("insert into CONTACT_INVITATION_OUTGOING_EMAIL ")
        .append("(CONTACT_INVITATION_ID,USER_ID,TO_EMAIL_ID) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to create an outgoing user invitation. */
    private static final String SQL_CREATE_OUTGOING_USER =
        new StringBuilder("insert into CONTACT_INVITATION_OUTGOING_USER ")
        .append("(CONTACT_INVITATION_ID,USER_ID,TO_USER_ID) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to delete an invitation. */
    private static final String SQL_DELETE =
        new StringBuilder("delete from CONTACT_INVITATION ")
        .append("where CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to delete an incoming invitation. */
    private static final String SQL_DELETE_INCOMING =
        new StringBuilder("delete from CONTACT_INVITATION_INCOMING ")
        .append("where CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to delete an outgoing e-mail invitation. */
    private static final String SQL_DELETE_OUTGOING_EMAIL =
        new StringBuilder("delete from CONTACT_INVITATION_OUTGOING_EMAIL ")
        .append("where CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to delete an outgoing user invitation. */
    private static final String SQL_DELETE_OUTGOING_USER =
        new StringBuilder("delete from CONTACT_INVITATION_OUTGOING_USER ")
        .append("where CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to read an incoming invitation without an e-mail address. */
    private static final String SQL_READ_INCOMING =
        new StringBuilder("select PUC.USER_ID \"CREATED_BY\",")
        .append("CI.CREATED_ON,CI.CONTACT_INVITATION_ID,E.EMAIL,PU.USER_ID,")
        .append("PU.USERNAME ")
        .append("from CONTACT_INVITATION_INCOMING CII ")
        .append("inner join CONTACT_INVITATION CI on CI.CONTACT_INVITATION_ID=CII.CONTACT_INVITATION_ID ")
        .append("inner join PARITY_USER PUC on PUC.USER_ID=CI.CREATED_BY ")
        .append("inner join PARITY_USER PU on PU.USER_ID=CII.USER_ID ")
        .append("left join EMAIL E on E.EMAIL_ID=CII.EMAIL_ID ")
        .append("where CII.EMAIL_ID is null and CII.USER_ID=? ")
        .append("and CII.FROM_USER_ID=?")
        .toString();

    /** Sql to read an incoming invitation with an e-mail address. */
    private static final String SQL_READ_INCOMING_BY_EMAIL =
        new StringBuilder("select PUC.USER_ID \"CREATED_BY\",")
        .append("CI.CREATED_ON,CI.CONTACT_INVITATION_ID,E.EMAIL,PU.USER_ID,")
        .append("PU.USERNAME ")
        .append("from CONTACT_INVITATION_INCOMING CII ")
        .append("inner join CONTACT_INVITATION CI on CI.CONTACT_INVITATION_ID=CII.CONTACT_INVITATION_ID ")
        .append("inner join PARITY_USER PUC on PUC.USER_ID=CI.CREATED_BY ")
        .append("inner join EMAIL E on E.EMAIL_ID=CII.EMAIL_ID ")
        .append("inner join PARITY_USER PU on PU.USER_ID=CII.USER_ID ")
        .append("where CII.USER_ID=? and CII.FROM_USER_ID=? and E.EMAIL=?")
        .toString();

    /** Sql to read all incoming invitations for two users. */
    private static final String SQL_READ_INCOMING_IDS_ALL =
        new StringBuilder("select CI.CONTACT_INVITATION_ID ")
        .append("from CONTACT_INVITATION_INCOMING CII ")
        .append("inner join CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CII.CONTACT_INVITATION_ID ")
        .append("where (CII.USER_ID=? and CII.FROM_USER_ID=?) ")
        .append("or (CII.USER_ID=? and CII.FROM_USER_ID=?)")
        .toString();

    /** Sql to read incoming invitations. */
    private static final String SQL_READ_INCOMING_INVITATIONS =
        new StringBuilder("select CREATED_BY,CREATED_ON ")
        .append("from USER_INVITATION UI ")
        .append("where UI.INVITATION_IO=? ")
        .append("order by UI.CREATED_ON asc")
        .toString();

    /** Sql to read an outgoing e-mail invitation. */
    private static final String SQL_READ_OUTGOING_EMAIL =
        new StringBuilder("select PUC.USER_ID \"CREATED_BY\",")
        .append("CI.CREATED_ON,CI.CONTACT_INVITATION_ID,ET.EMAIL,PU.USER_ID,")
        .append("PU.USERNAME ")
        .append("from CONTACT_INVITATION_OUTGOING_EMAIL CIOE ")
        .append("inner join CONTACT_INVITATION CI on CI.CONTACT_INVITATION_ID=CIOE.CONTACT_INVITATION_ID ")
        .append("inner join EMAIL ET on ET.EMAIL_ID=CIOE.TO_EMAIL_ID ")
        .append("inner join PARITY_USER PUC on PUC.USER_ID=CI.CREATED_BY ")
        .append("inner join PARITY_USER PU on PU.USER_ID=CIOE.USER_ID ")
        .append("where CIOE.USER_ID=? and ET.EMAIL=?")
        .toString();

    /** Sql to read all outgoing e-mail invitation ids for two users. */
    private static final String SQL_READ_OUTGOING_EMAIL_IDS_ALL =
        new StringBuilder("select CI.CONTACT_INVITATION_ID ")
        .append("from CONTACT_INVITATION_OUTGOING_EMAIL CIOE ")
        .append("inner join CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIOE.CONTACT_INVITATION_ID ")
        .append("inner join PARITY_USER PU on PU.USER_ID=CIOE.USER_ID ")
        .append("inner join EMAIL ET on ET.EMAIL_ID=CIOE.TO_EMAIL_ID ")
        .append("inner join USER_EMAIL UET on UET.EMAIL_ID=ET.EMAIL_ID ")
        .append("inner join PARITY_USER PUT on PUT.USER_ID=UET.USER_ID ")
        .append("where (PU.USER_ID=? and PUT.USER_ID=?) ")
        .append("or (PU.USER_ID=? and PUT.USER_ID=?)")
        .toString();

    /** Sql to read an outgoing e-mail invitation. */
    private static final String SQL_READ_OUTGOING_USER =
        new StringBuilder("select PUC.USER_ID \"CREATED_BY\",")
        .append("CI.CREATED_ON,CI.CONTACT_INVITATION_ID,PU.USER_ID,")
        .append("PU.USERNAME ")
        .append("from CONTACT_INVITATION_OUTGOING_USER CIOU ")
        .append("inner join CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIOU.CONTACT_INVITATION_ID ")
        .append("inner join PARITY_USER PUC on PUC.USER_ID=CI.CREATED_BY ")
        .append("inner join PARITY_USER PU on PU.USER_ID=CIOU.USER_ID ")
        .append("where CIOU.USER_ID=? and CIOU.TO_USER_ID=?")
        .toString();

    /** Sql to read all outgoing user invitations ids for two users. */
    private static final String SQL_READ_OUTGOING_USER_IDS_ALL =
        new StringBuilder("select CI.CONTACT_INVITATION_ID ")
        .append("from CONTACT_INVITATION_OUTGOING_USER CIOU ")
        .append("inner join CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIOU.CONTACT_INVITATION_ID ")
        .append("where (CIOU.USER_ID=? and CIOU.TO_USER_ID=?) ")
        .append("or (CIOU.USER_ID=? and CIOU.TO_USER_ID=?)")
        .toString();

    private final EMailSql emailSql;

    private final UserSql userSql;

    /**
     * Create InvitationSql.
     *
     */
    public InvitationSql() {
        super();
        this.emailSql = new EMailSql();
        this.userSql = new UserSql();
    }

    /**
     * Create an incoming invitation for a user.
     * 
     * @param userId
     *            A user id <code>Long</code>.
     * @param invitation
     *            An <code>IncomingInvitation</code>.
     */
    public void createIncoming(final Long userId, final IncomingInvitation invitation) {
        final HypersonicSession session = openSession();
        try {
            final Long emailId;
            if (invitation.isSetInvitedAs()) {
                emailId = emailSql.readLazyCreate(session, invitation.getInvitedAs());
            } else {
                emailId = null;
            }
            // create the invitation
            create(session, invitation);

            // create the incoming invitation
            session.prepareStatement(SQL_CREATE_INCOMING);
            session.setLong(1, invitation.getId());
            session.setLong(2, userId);
            session.setLong(3, emailId);
            session.setLong(4, invitation.getInvitedBy().getLocalId());
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not create invitation {0} for user {1}.", userId,
                        invitation);
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Create an outgoing e-mail invitation.
     * 
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     */
    public void createOutgoingEMail(final OutgoingEMailInvitation invitation) {
        final HypersonicSession session = openSession();
        try {
            // create the e-mail if required
            final Long emailId = emailSql.readLazyCreate(session, invitation.getEmail());
            // create the invitation
            create(session, invitation);
            // create the outgoing e-mail invitation
            session.prepareStatement(SQL_CREATE_OUTGOING_EMAIL);
            session.setLong(1, invitation.getId());
            session.setLong(2, invitation.getCreatedBy());
            session.setLong(3, emailId);
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not create outgoing e-mail invitation for {0} to {1}.",
                        invitation.getCreatedBy(), invitation.getEmail());
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void createOutgoingUser(final OutgoingUserInvitation invitation) {
        final HypersonicSession session = openSession();
        try {
            // create the invitation
            create(session, invitation);
            // create the outgoing user invitation
            session.prepareStatement(SQL_CREATE_OUTGOING_USER);
            session.setLong(1, invitation.getId());
            session.setLong(2, invitation.getCreatedBy());
            session.setLong(3, invitation.getUser().getLocalId());
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not create outgoing user invitation for {0} to {1}.",
                        invitation.getCreatedBy(), invitation.getUser().getLocalId());
            session.commit();
        } finally {
            session.close();
        }
    }

    public void deleteIncoming(final Long invitationId) {
        final HypersonicSession session = openSession();
        try {
            deleteIncoming(session, invitationId);
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Delete the incoming invitations in either direction for the user from the
     * other user.
     * 
     * @param userId
     * @param fromUserId
     */
    public void deleteIncoming(final Long userId, final Long fromUserId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_INCOMING_IDS_ALL);
            session.setLong(1, userId);
            session.setLong(2, fromUserId);
            session.setLong(3, fromUserId);
            session.setLong(4, userId);
            session.executeQuery();
            while (session.nextResult()) {
                deleteIncoming(session.getLong("CONTACT_INVITATION_ID"));
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void deleteOutgoingEMail(final Long invitationId) {
        final HypersonicSession session = openSession();
        try {
            deleteOutgoingEMail(session, invitationId);
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void deleteOutgoingEMail(final Long userId, final Long toUserId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_OUTGOING_EMAIL_IDS_ALL);
            session.setLong(1, userId);
            session.setLong(2, toUserId);
            session.setLong(3, toUserId);
            session.setLong(4, userId);
            session.executeQuery();
            while (session.nextResult()) {
                deleteOutgoingEMail(session,
                        session.getLong("CONTACT_INVITATION_ID"));
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void deleteOutgoingUser(final Long invitationId) {
        final HypersonicSession session = openSession();
        try {
            deleteOutgoingUser(session, invitationId);
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }        
    }

    public void deleteOutgoingUser(final Long userId, final Long toUserId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_OUTGOING_USER_IDS_ALL);
            session.setLong(1, userId);
            session.setLong(2, toUserId);
            session.setLong(3, toUserId);
            session.setLong(4, userId);
            session.executeQuery();
            while (session.nextResult()) {
                deleteOutgoingUser(session,
                        session.getLong("CONTACT_INVITATION_ID"));
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public IncomingInvitation readIncoming(final Long userId,
            final Long fromUserId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_INCOMING);
            session.setLong(1, userId);
            session.setLong(2, fromUserId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractIncoming(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public IncomingInvitation readIncoming(final Long userId,
            final Long fromUserId, final EMail email) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_INCOMING_BY_EMAIL);
            session.setLong(1, userId);
            session.setLong(2, fromUserId);
            session.setEMail(3, email);
            session.executeQuery();
            if (session.nextResult()) {
                return extractIncoming(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public List<IncomingInvitation> readIncomingInvitations(final Long userId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_INCOMING_INVITATIONS);
            session.setLong(1, userId);
            session.executeQuery();
            final List<IncomingInvitation> invitations = new ArrayList<IncomingInvitation>();
            while (session.nextResult()) {
                invitations.add(extractIncoming(session));
            }
            return invitations;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public OutgoingEMailInvitation readOutgoingEMail(final Long userId,
            final EMail email) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_OUTGOING_EMAIL);
            session.setLong(1, userId);
            session.setString(2, email.toString());
            session.executeQuery();
            if (session.nextResult()) {
                return extractOutgoingEMail(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations(final Long userId) {
        // NOCOMMIT InvitationSql.readOutgoingEMailInvitations NYI raymond@thinkparity.com - 11-Mar-07 1:11:53 PM
        throw Assert.createNotYetImplemented("");
    }

    public OutgoingUserInvitation readOutgoingUser(final Long userId,
            final Long toUserId) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_OUTGOING_USER);
            session.setLong(1, userId);
            session.setLong(2, toUserId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractOutgoingUser(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public List<OutgoingUserInvitation> readOutgoingUserInvitations(final Long userId) {
        // NOCOMMIT InvitationSql.readOutgoingUserInvitations NYI raymond@thinkparity.com - 11-Mar-07 1:12:06 PM
        throw Assert.createNotYetImplemented("");
    }

    /**
     * Extract an incoming contact invitation from the session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return An <code>IncomingInvitation</code>.
     */
    IncomingInvitation extractIncoming(final HypersonicSession session) {
        final IncomingInvitation invitation = new IncomingInvitation();
        extract(session, invitation);
        invitation.setInvitedAs(session.getEMail("EMAIL"));
        invitation.setInvitedBy(userSql.extract(session));
        return invitation;
    }

    /**
     * Extract an outgoing e-mail invitation from the session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return An <code>OutgoingEMailInvitation</code>.
     */
	OutgoingEMailInvitation extractOutgoingEMail(final HypersonicSession session) {
        final OutgoingEMailInvitation invitation = new OutgoingEMailInvitation();
        extract(session, invitation);
        invitation.setEmail(session.getEMail("EMAIL"));
        return invitation;
    }

    /**
     * Extract an outgoing user invitation from the session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return An <code>OutgoingUserInvitation</code>.
     */
    OutgoingUserInvitation extractOutgoingUser(final HypersonicSession session) {
        final OutgoingUserInvitation invitation = new OutgoingUserInvitation();
        extract(session, invitation);
        invitation.setUser(userSql.extract(session));
        return invitation;
    }

    /**
     * Create a generic contact invitation.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param userId
     *            A user id <code>Long</code>.
     * @param invitation
     *            A <code>ContactInvitation</code>.
     */
    private void create(final HypersonicSession session,
            final ContactInvitation invitation) {
        session.prepareStatement(SQL_CREATE);
        session.setLong(1, invitation.getCreatedBy());
        session.setCalendar(2, invitation.getCreatedOn());
        if (1 != session.executeUpdate())
            throw new HypersonicException("Could not create contact invitation.");
        invitation.setId(session.getIdentity());
    }

    /**
     * Delete a generic contact invitation.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param invitationId
     *            An invitation id <code>Long</code>.
     */
    private void delete(final HypersonicSession session, final Long invitationId) {
        session.prepareStatement(SQL_DELETE);
        session.setLong(1, invitationId);
        if (1 != session.executeUpdate())
            throw new HypersonicException("Could not delete invitation {0}.",
                    invitationId);
    }

    private void deleteIncoming(final HypersonicSession session,
            final Long invitationId) {
        session.prepareStatement(SQL_DELETE_INCOMING);
        session.setLong(1, invitationId);
        if (1 != session.executeUpdate())
            throw new HypersonicException(
                    "Could not delete incoming invitation {0}.",
                    invitationId);
        delete(session, invitationId);
    }

    private void deleteOutgoingEMail(final HypersonicSession session,
            final Long invitationId) {
        session.prepareStatement(SQL_DELETE_OUTGOING_EMAIL);
        session.setLong(1, invitationId);
        if (1 != session.executeUpdate())
            throw new HypersonicException(
                    "Could not delete outgoing e-mail invitation {0}.",
                    invitationId);

        delete(session, invitationId);
    }

    private void deleteOutgoingUser(final HypersonicSession session, final Long invitationId) {
        session.prepareStatement(SQL_DELETE_OUTGOING_USER);
        session.setLong(1, invitationId);
        if (1 != session.executeUpdate())
            throw new HypersonicException(
                    "Could not delete outgoing user invitation {0}.",
                    invitationId);
        delete(session, invitationId);
    }

    /**
     * Extract common fields into the contact invitation.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param invitation
     *            A <code>ContactInvitation</code>.
     */
	private void extract(final HypersonicSession session,
            final ContactInvitation invitation) {
        invitation.setCreatedBy(session.getLong("CREATED_BY"));
        invitation.setCreatedOn(session.getCalendar("CREATED_ON"));
        invitation.setId(session.getLong("CONTACT_INVITATION_ID"));
    }
}
