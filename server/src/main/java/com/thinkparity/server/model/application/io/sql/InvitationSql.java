/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.model.io.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.contact.ContactInvitation;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.contact.invitation.Attachment;
import com.thinkparity.desdemona.model.contact.invitation.ContainerVersionAttachment;
import com.thinkparity.desdemona.model.contact.invitation.Attachment.ReferenceType;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.node.Node;

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
        new StringBuilder("insert into TPSD_CONTACT_INVITATION ")
        .append("(CREATED_BY,CREATED_ON) ")
        .append("values (?,?)")
        .toString();

    /** Sql to create an attachment. */
    private static final String SQL_CREATE_ATTACHMENT =
        new StringBuilder("insert into TPSD_CONTACT_INVITATION_ATTACHMENT ")
        .append("(CONTACT_INVITATION_ID,ATTACHMENT_REFERENCE_TYPE_ID,")
        .append("ATTACHMENT_REFERENCE_ID) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to create an incoming e-mail invitation. */
    private static final String SQL_CREATE_INCOMING_EMAIL =
        new StringBuilder("insert into TPSD_CONTACT_INVITATION_INCOMING_EMAIL ")
        .append("(CONTACT_INVITATION_ID,USER_ID,EMAIL_ID,EXTENDED_BY_USER_ID) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to create an incoming user invitation. */
    private static final String SQL_CREATE_INCOMING_USER =
        new StringBuilder("insert into TPSD_CONTACT_INVITATION_INCOMING_USER ")
        .append("(CONTACT_INVITATION_ID,USER_ID,EXTENDED_BY_USER_ID) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to create a generic invitation lock. */
    private static final String SQL_CREATE_LOCK =
        new StringBuilder("insert into TPSD_CONTACT_INVITATION_LOCK ")
        .append("(CONTACT_INVITATION_ID) ")
        .append("values (?)")
        .toString();

    /** Sql to create an outgoing e-mail invitation. */
    private static final String SQL_CREATE_OUTGOING_EMAIL =
        new StringBuilder("insert into TPSD_CONTACT_INVITATION_OUTGOING_EMAIL ")
        .append("(CONTACT_INVITATION_ID,USER_ID,INVITATION_EMAIL_ID) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to create an outgoing user invitation. */
    private static final String SQL_CREATE_OUTGOING_USER =
        new StringBuilder("insert into TPSD_CONTACT_INVITATION_OUTGOING_USER ")
        .append("(CONTACT_INVITATION_ID,USER_ID,INVITATION_USER_ID) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to delete an invitation. */
    private static final String SQL_DELETE =
        new StringBuilder("delete from TPSD_CONTACT_INVITATION ")
        .append("where CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to delete an attachment. */
    private static final String SQL_DELETE_ATTACHMENT =
        new StringBuilder("delete from TPSD_CONTACT_INVITATION_ATTACHMENT ")
        .append("where CONTACT_INVITATION_ID=? ")
        .append("and ATTACHMENT_REFERENCE_TYPE_ID=? ")
        .append("and ATTACHMENT_REFERENCE_ID=?")
        .toString();

    /** Sql to delete an attachment. */
    private static final String SQL_DELETE_ATTACHMENTS =
        new StringBuilder("delete from TPSD_CONTACT_INVITATION_ATTACHMENT ")
        .append("where CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to delete an incoming e-mail. */
    private static final String SQL_DELETE_INCOMING_EMAIL =
        new StringBuilder("delete from TPSD_CONTACT_INVITATION_INCOMING_EMAIL ")
        .append("where CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to delete an incoming e-mail. */
    private static final String SQL_DELETE_INCOMING_USER =
        new StringBuilder("delete from TPSD_CONTACT_INVITATION_INCOMING_USER ")
        .append("where CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to delete an invitation lock. */
    private static final String SQL_DELETE_LOCK =
        new StringBuilder("delete from TPSD_CONTACT_INVITATION_LOCK ")
        .append("where CONTACT_INVITATION_ID=? and NODE_ID=?")
        .toString();

    /** Sql to delete an outgoing e-mail invitation. */
    private static final String SQL_DELETE_OUTGOING_EMAIL =
        new StringBuilder("delete from TPSD_CONTACT_INVITATION_OUTGOING_EMAIL ")
        .append("where CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to delete an outgoing user invitation. */
    private static final String SQL_DELETE_OUTGOING_USER =
        new StringBuilder("delete from TPSD_CONTACT_INVITATION_OUTGOING_USER ")
        .append("where CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to lock a series of invitations. */
    private static final Map<Integer, String> SQL_LOCK = new HashMap<Integer, String>(3, 1.0F);

    /** Sql to read attachments. */
    private static final String SQL_READ_ATTACHMENTS =
        new StringBuilder("select CIA.CONTACT_INVITATION_ID,")
        .append("CIA.ATTACHMENT_REFERENCE_ID,CIA.ATTACHMENT_REFERENCE_TYPE_ID ")
        .append("from TPSD_CONTACT_INVITATION_ATTACHMENT CIA ")
        .append("where CIA.CONTACT_INVITATION_ID=?")
        .toString();

    /** Sql to read container version attachments. */
    private static final String SQL_READ_CONTAINER_VERSION_ATTACHMENTS =
        new StringBuilder("select CIA.CONTACT_INVITATION_ID,")
        .append("CIA.ATTACHMENT_REFERENCE_ID,CIA.ATTACHMENT_REFERENCE_TYPE_ID ")
        .append("from TPSD_CONTACT_INVITATION_ATTACHMENT CIA ")
        .append("where CIA.CONTACT_INVITATION_ID=? ")
        .append("and CIA.ATTACHMENT_REFERENCE_TYPE_ID=?")
        .toString();

    /** Sql to read an incoming e-mail invitation. */
    private static final String SQL_READ_INCOMING_EMAIL =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIIE.CONTACT_INVITATION_ID,CIIE.USER_ID,E.EMAIL,")
        .append("CIIE.EXTENDED_BY_USER_ID ")
        .append("from TPSD_CONTACT_INVITATION_INCOMING_EMAIL CIIE ")
        .append("inner join TPSD_CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIIE.CONTACT_INVITATION_ID ")
        .append("inner join TPSD_EMAIL E on E.EMAIL_ID=CIIE.EMAIL_ID ")
        .append("where CIIE.USER_ID=?")
        .toString();

    /** Sql to read an incoming e-mail invitation by its unique key. */
    private static final String SQL_READ_INCOMING_EMAIL_UK =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIIE.CONTACT_INVITATION_ID,CIIE.USER_ID,E.EMAIL,")
        .append("CIIE.EXTENDED_BY_USER_ID ")
        .append("from TPSD_CONTACT_INVITATION_INCOMING_EMAIL CIIE ")
        .append("inner join TPSD_CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIIE.CONTACT_INVITATION_ID ")
        .append("inner join TPSD_EMAIL E on E.EMAIL_ID=CIIE.EMAIL_ID ")
        .append("where CIIE.USER_ID=? ")
        .append("and E.EMAIL=?")
        .append("and CIIE.EXTENDED_BY_USER_ID=?")
        .toString();

    /** Sql to read incoming invitations. */
    private static final String SQL_READ_INCOMING_USER =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIIU.CONTACT_INVITATION_ID,CIIU.USER_ID,")
        .append("CIIU.EXTENDED_BY_USER_ID ")
        .append("from TPSD_CONTACT_INVITATION_INCOMING_USER CIIU ")
        .append("inner join TPSD_CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIIU.CONTACT_INVITATION_ID ")
        .append("where CIIU.USER_ID=?")
        .toString();

    /** Sql to read an incoming invitation by its unique key. */
    private static final String SQL_READ_INCOMING_USER_UK =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIIU.CONTACT_INVITATION_ID,CIIU.USER_ID,")
        .append("CIIU.EXTENDED_BY_USER_ID ")
        .append("from TPSD_CONTACT_INVITATION_INCOMING_USER CIIU ")
        .append("inner join TPSD_CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIIU.CONTACT_INVITATION_ID ")
        .append("where CIIU.USER_ID=? and CIIU.EXTENDED_BY_USER_ID=?")
        .toString();

    /** Sql to read an outgoing e-mail invitations. */
    private static final String SQL_READ_OUTGOING_EMAIL =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIOE.CONTACT_INVITATION_ID,CIOE.USER_ID,IE.EMAIL ")
        .append("from TPSD_CONTACT_INVITATION_OUTGOING_EMAIL CIOE ")
        .append("inner join TPSD_CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIOE.CONTACT_INVITATION_ID ")
        .append("inner join TPSD_EMAIL IE on IE.EMAIL_ID=CIOE.INVITATION_EMAIL_ID ")
        .append("where CIOE.USER_ID=?")
        .toString();

    /** Sql to read an outgoing e-mail invitations. */
    private static final String SQL_READ_OUTGOING_EMAIL_BY_EMAIL =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIOE.CONTACT_INVITATION_ID,CIOE.USER_ID,E.EMAIL ")
        .append("from TPSD_CONTACT_INVITATION_OUTGOING_EMAIL CIOE ")
        .append("inner join TPSD_CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIOE.CONTACT_INVITATION_ID ")
        .append("inner join TPSD_EMAIL E on E.EMAIL_ID=CIOE.INVITATION_EMAIL_ID ")
        .append("where E.EMAIL=?")
        .toString();

    /** Sql to read an outgoing e-mail invitations. */
    private static final String SQL_READ_OUTGOING_EMAIL_FK =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIOE.CONTACT_INVITATION_ID,CIOE.USER_ID,IE.EMAIL ")
        .append("from TPSD_CONTACT_INVITATION_OUTGOING_EMAIL CIOE ")
        .append("inner join TPSD_CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIOE.CONTACT_INVITATION_ID ")
        .append("inner join TPSD_CONTACT_INVITATION_ATTACHMENT CIA ")
        .append("on CIA.CONTACT_INVITATION_ID=CIOE.CONTACT_INVITATION_ID ")
        .append("inner join TPSD_EMAIL IE on IE.EMAIL_ID=CIOE.INVITATION_EMAIL_ID ")
        .append("where CIOE.USER_ID=? and CIA.ATTACHMENT_REFERENCE_TYPE_ID=? ")
        .append("and CIA.ATTACHMENT_REFERENCE_ID=?")
        .toString();

    /** Sql to read an outgoing e-mail invitation by its unique key. */
    private static final String SQL_READ_OUTGOING_EMAIL_UK =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIOE.CONTACT_INVITATION_ID,CIOE.USER_ID,IE.EMAIL ")
        .append("from TPSD_CONTACT_INVITATION_OUTGOING_EMAIL CIOE ")
        .append("inner join TPSD_CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIOE.CONTACT_INVITATION_ID ")
        .append("inner join TPSD_EMAIL IE on IE.EMAIL_ID=CIOE.INVITATION_EMAIL_ID ")
        .append("where CIOE.USER_ID=? and IE.EMAIL=?")
        .toString();

    /** Sql to read an outgoing e-mail invitation. */
    private static final String SQL_READ_OUTGOING_USER =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIOU.CONTACT_INVITATION_ID,CIOU.USER_ID,")
        .append("CIOU.INVITATION_USER_ID ")
        .append("from TPSD_CONTACT_INVITATION_OUTGOING_USER CIOU ")
        .append("inner join TPSD_CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIOU.CONTACT_INVITATION_ID ")
        .append("where CIOU.USER_ID=?")
        .toString();

    /** Sql to read an outgoing e-mail invitation. */
    private static final String SQL_READ_OUTGOING_USER_UK =
        new StringBuilder("select CI.CREATED_BY,CI.CREATED_ON,")
        .append("CIOU.CONTACT_INVITATION_ID,CIOU.USER_ID,")
        .append("CIOU.INVITATION_USER_ID ")
        .append("from TPSD_CONTACT_INVITATION_OUTGOING_USER CIOU ")
        .append("inner join TPSD_CONTACT_INVITATION CI ")
        .append("on CI.CONTACT_INVITATION_ID=CIOU.CONTACT_INVITATION_ID ")
        .append("where CIOU.USER_ID=? and CIOU.INVITATION_USER_ID=?")
        .toString();

    /** Sql to unlock an invitation. */
    private static final String SQL_UNLOCK =
        new StringBuilder("update TPSD_CONTACT_INVITATION_LOCK ")
        .append("set NODE_ID=null ")
        .append("where NODE_ID=? ")
        .append("and CONTACT_INVITATION_ID=?")
        .toString();

    static {
        SQL_LOCK.put(Integer.valueOf(1),
                new StringBuilder("update TPSD_CONTACT_INVITATION_LOCK ")
                .append("set NODE_ID=? ")
                .append("where CONTACT_INVITATION_ID=? ")
                .append("and NODE_ID is null")
                .toString());
        SQL_LOCK.put(Integer.valueOf(2),
                new StringBuilder("update TPSD_CONTACT_INVITATION_LOCK ")
                .append("set NODE_ID=? ")
                .append("where (CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=?) ")
                .append("and NODE_ID is null")
                .toString());
        SQL_LOCK.put(Integer.valueOf(3),
                new StringBuilder("update TPSD_CONTACT_INVITATION_LOCK ")
                .append("set NODE_ID=? ")
                .append("where (CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=?)")
                .append("and NODE_ID is null")
                .toString());
        SQL_LOCK.put(Integer.valueOf(4),
                new StringBuilder("update TPSD_CONTACT_INVITATION_LOCK ")
                .append("set NODE_ID=? ")
                .append("where (CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=?) ")
                .append("and NODE_ID is null")
                .toString());
        SQL_LOCK.put(Integer.valueOf(5),
                new StringBuilder("update TPSD_CONTACT_INVITATION_LOCK ")
                .append("set NODE_ID=? ")
                .append("where (CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=?) ")
                .append("and NODE_ID is null")
                .toString());
        SQL_LOCK.put(Integer.valueOf(6),
                new StringBuilder("update TPSD_CONTACT_INVITATION_LOCK ")
                .append("set NODE_ID=? ")
                .append("where (CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=?) ")
                .append("and NODE_ID is null")
                .toString());
        SQL_LOCK.put(Integer.valueOf(7),
                new StringBuilder("update TPSD_CONTACT_INVITATION_LOCK ")
                .append("set NODE_ID=? ")
                .append("where (CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=?) ")
                .append("and NODE_ID is null")
                .toString());
        SQL_LOCK.put(Integer.valueOf(8),
                new StringBuilder("update TPSD_CONTACT_INVITATION_LOCK ")
                .append("set NODE_ID=? ")
                .append("where (CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=? ")
                .append("or CONTACT_INVITATION_ID=?) ")
                .append("and NODE_ID is null")
                .toString());
    }

    /** An e-mail sql interface. */
    private final EMailSql emailSql;

    /** A user sql interface. */
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
     * Create an incoming e-mail invitation for a user.
     * 
     * @param user
     *            A <code>User</code>.
     * @param invitation
     *            An <code>IncomingEMailInvitation</code>.
     */
    public void create(final User user, final IncomingEMailInvitation invitation) {
        final HypersonicSession session = openSession();
        try {
            final Long emailId = emailSql.readLazyCreate(session,
                    invitation.getInvitationEMail());
            // create the invitation
            create(session, invitation);

            // create the permanent record
            userSql.createInvitation(session, user, invitation);

            // create the incoming invitation
            session.prepareStatement(SQL_CREATE_INCOMING_EMAIL);
            session.setLong(1, invitation.getId());
            session.setLong(2, user.getLocalId());
            session.setLong(3, emailId);
            session.setLong(4, invitation.getExtendedBy().getLocalId());
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not create invitation {0} for user {1}.",
                        invitation, user);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Create an incoming user invitation.
     * 
     * @param user
     *            A <code>User</code>.
     * @param invitation
     *            An <code>IncomingUserInvitation</code>.
     */
    public void create(final User user, final IncomingUserInvitation invitation) {
        final HypersonicSession session = openSession();
        try {
            // create the invitation
            create(session, invitation);

            // create the permanent record
            userSql.createInvitation(session, user, invitation);

            // create the incoming invitation
            session.prepareStatement(SQL_CREATE_INCOMING_USER);
            session.setLong(1, invitation.getId());
            session.setLong(2, user.getLocalId());
            session.setLong(3, invitation.getExtendedBy().getLocalId());
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not create invitation {0} for user {1}.",
                        invitation, user);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Create an outgoing e-mail invitation.
     * 
     * @param user
     *            A <code>User</code>.
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     */
    public void create(final User user, final OutgoingEMailInvitation invitation) {
        final HypersonicSession session = openSession();
        try {
            // create the e-mail if required
            final Long emailId = emailSql.readLazyCreate(session, invitation.getInvitationEMail());

            // create the invitation
            create(session, invitation);

            // create the outgoing e-mail invitation
            session.prepareStatement(SQL_CREATE_OUTGOING_EMAIL);
            session.setLong(1, invitation.getId());
            session.setLong(2, user.getLocalId());
            session.setLong(3, emailId);
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not create outgoing e-mail invitation for {0} to {1}.",
                        invitation.getCreatedBy(), invitation.getInvitationEMail());

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Create an outgoing user invitation.
     * 
     * @param user
     *            A <code>User</code>.
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>.
     */
    public void create(final User user, final OutgoingUserInvitation invitation) {
        final HypersonicSession session = openSession();
        try {
            // create the invitation
            create(session, invitation);

            // create the outgoing user invitation
            session.prepareStatement(SQL_CREATE_OUTGOING_USER);
            session.setLong(1, invitation.getId());
            session.setLong(2, user.getLocalId());
            session.setLong(3, invitation.getInvitationUser().getLocalId());
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not create outgoing user invitation {0} for {1}.",
                        invitation, user);

            session.commit();
        } finally {
            session.close();
        }
    }

    /**
     * Create an invitation attachment.
     * 
     * @param attachment
     *            An <code>Attachment</code>.
     */
    public void createAttachment(final Attachment attachment) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_ATTACHMENT);
            session.setLong(1, attachment.getInvitationId());
            session.setInt(2, attachment.getReferenceType().getId());
            session.setString(3, attachment.getReferenceId());
            if (1 != session.executeUpdate())
                throw panic("Could not create attachment.");

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void delete(final Node node, final OutgoingEMailInvitation invitation) {
        final HypersonicSession session = openSession();
        try {
            // delete the invitation
            session.prepareStatement(SQL_DELETE_OUTGOING_EMAIL);
            session.setLong(1, invitation.getId());
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not delete invitation {0}.",
                        invitation);

            // delete the generic invitation
            delete(session, node, invitation);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void delete(final Node node, final OutgoingUserInvitation invitation) {
        final HypersonicSession session = openSession();
        try {
            // delete the invitation
            session.prepareStatement(SQL_DELETE_OUTGOING_USER);
            session.setLong(1, invitation.getId());
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not delete invitation {0}.",
                        invitation);

            // delete the generic invitation
            delete(session, node, invitation);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void delete(final Node node, final User user,
            final IncomingEMailInvitation invitation) {
        final HypersonicSession session = openSession();
        try {
            // delete the invitation
            session.prepareStatement(SQL_DELETE_INCOMING_EMAIL);
            session.setLong(1, invitation.getId());
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not delete invitation {0}.",
                        invitation);

            // delete the generic invitation
            delete(session, node, invitation);

            // delete the permanent record
            userSql.deleteInvitation(session, user, invitation);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void delete(final Node node, final User user,
            final IncomingUserInvitation invitation) {
        final HypersonicSession session = openSession();
        try {
            // delete the invitation
            session.prepareStatement(SQL_DELETE_INCOMING_USER);
            session.setLong(1, invitation.getId());
            if (1 != session.executeUpdate())
                throw new HypersonicException(
                        "Could not delete invitation {0}.",
                        invitation);

            // delete the generic invitation
            delete(session, node, invitation);

            // delete the permanent record
            userSql.deleteInvitation(session, user, invitation);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Delete an invitation attachment.
     * 
     * @param attachment
     *            An <code>Attachment</code>.
     */
    public void deleteAttachment(final Attachment attachment) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_ATTACHMENT);
            session.setLong(1, attachment.getInvitationId());
            session.setInt(2, attachment.getReferenceType().getId());
            session.setString(3, attachment.getReferenceId());
            if (1 != session.executeUpdate())
                throw panic("Could not delete attachment.");

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Delete all invitation attachments.
     * 
     * @param invitation
     *            A <code>ContactInvitation</code>.
     */
    public void deleteAttachments(final ContactInvitation invitation) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_ATTACHMENTS);
            session.setLong(1, invitation.getId());
            session.executeUpdate();

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Lock invitations for a node to process.
     * 
     * @param invitationList
     *            A <code>List<ContactInvitation></code>.
     */
    public Boolean lock(final Node node,
            final List<ContactInvitation> invitationList) {
        if (invitationList.isEmpty()) {
            return Boolean.TRUE;
        } else {
            Assert.assertTrue(1 == invitationList.size()
                    || 2 == invitationList.size() || 3 == invitationList.size()
                    || 4 == invitationList.size() || 5 == invitationList.size()
                    || 6 == invitationList.size() || 7 == invitationList.size()
                    || 8 == invitationList.size(),
                    "Invitation list must be of size [1,2,3,4,5,6,7,8] and is {0}.",
                    invitationList.size());
            final HypersonicSession session = openSession();
            try {
                session.prepareStatement(SQL_LOCK.get(Integer.valueOf(invitationList.size())));
                session.setLong(1, node.getId());
                int i = 2;
                for (final ContactInvitation invitation : invitationList) {
                    session.setLong(i++, invitation.getId());
                }
                final int locked = session.executeUpdate();
    
                if (locked == invitationList.size()) {
                    session.commit();
                    return Boolean.TRUE;
                } else {
                    session.rollback();
                    return Boolean.FALSE;
                }
            } catch (final Throwable t) {
                throw translateError(session, t);
            } finally {
                session.close();
            }
        }
    }

    /**
     * Read the attachments for an invitation.
     * 
     * @param invitation
     *            A <code>ContactInvitation</code>.
     * @return A <code>List</code> of <code>Attachment</code>s.
     */
    public List<Attachment> readAttachments(final ContactInvitation invitation) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_ATTACHMENTS);
            session.setLong(1, invitation.getId());
            session.executeQuery();
            final List<Attachment> attachments = new ArrayList<Attachment>();
            while (session.nextResult()) {
                attachments.add(extractAttachment(session));
            }
            return attachments;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read the attachments for an invitation.
     * 
     * @param invitation
     *            A <code>ContactInvitation</code>.
     * @return A <code>List</code> of <code>Attachment</code>s.
     */
    public List<ContainerVersionAttachment> readContainerVersionAttachments(
            final ContactInvitation invitation) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_CONTAINER_VERSION_ATTACHMENTS);
            session.setLong(1, invitation.getId());
            session.setInt(2, ReferenceType.CONTAINER_VERSION.getId());
            session.executeQuery();
            final List<ContainerVersionAttachment> attachments = new ArrayList<ContainerVersionAttachment>();
            while (session.nextResult()) {
                attachments.add(extractContainerVersionAttachment(session));
            }
            return attachments;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read incoming e-mail invitations for a user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>List</code> of <code>IncomingEMailInvitation</code>s.
     */
    public List<IncomingEMailInvitation> readIncomingEMail(final User user) {
        final HypersonicSession session = openSession();
        try {
            // read all invitations
            session.prepareStatement(SQL_READ_INCOMING_EMAIL);
            session.setLong(1, user.getLocalId());
            session.executeQuery();
            final List<IncomingEMailInvitation> invitations = new ArrayList<IncomingEMailInvitation>();
            while (session.nextResult()) {
                invitations.add(extractIncomingEMail(session));
            }
            return invitations;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read an incoming e-mail invitation for a user to an e-mail address, from
     * an invitation user.
     * 
     * @param user
     *            A <code>User</code>.
     * @param email
     *            An <code>EMail</code> address.
     * @param invitationUser
     *            The invitation <code>User</code>.
     * @return A <code>List</code> of <code>IncomingEMailInvitation</code>s.
     */
    public IncomingEMailInvitation readIncomingEMail(final User user,
            final EMail email, final User invitationUser) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_INCOMING_EMAIL_UK);
            session.setLong(1, user.getLocalId());
            session.setEMail(2, email);
            session.setLong(3, invitationUser.getLocalId());
            session.executeQuery();
            if (session.nextResult()) {
                return extractIncomingEMail(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public List<IncomingUserInvitation> readIncomingUser(final User user) {
        final HypersonicSession session = openSession();
        try {
            // read all invitations
            session.prepareStatement(SQL_READ_INCOMING_USER);
            session.setLong(1, user.getLocalId());
            session.executeQuery();
            final List<IncomingUserInvitation> invitations = new ArrayList<IncomingUserInvitation>();
            while (session.nextResult()) {
                invitations.add(extractIncomingUser(session));
            }
            return invitations;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public IncomingUserInvitation readIncomingUser(final User user,
            final User invitationUser) {
        final HypersonicSession session = openSession();
        try {
            // read invitation
            session.prepareStatement(SQL_READ_INCOMING_USER_UK);
            session.setLong(1, user.getLocalId());
            session.setLong(2, invitationUser.getLocalId());
            session.executeQuery();
            if (session.nextResult()) {
                return extractIncomingUser(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public List<OutgoingEMailInvitation> readOutgoingEMail(final EMail email) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_OUTGOING_EMAIL_BY_EMAIL);
            session.setEMail(1, email);
            session.executeQuery();
            final List<OutgoingEMailInvitation> invitations = new ArrayList<OutgoingEMailInvitation>();
            while (session.nextResult()) {
                invitations.add(extractOutgoingEMail(session));
            }
            return invitations;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }        
    }

    public List<OutgoingEMailInvitation> readOutgoingEMail(final User user) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_OUTGOING_EMAIL);
            session.setLong(1, user.getLocalId());
            session.executeQuery();
            final List<OutgoingEMailInvitation> invitations = new ArrayList<OutgoingEMailInvitation>();
            while (session.nextResult()) {
                invitations.add(extractOutgoingEMail(session));
            }
            return invitations;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }        
    }

    /**
     * Read the outgoing e-mail invitations created by a user; with the
     * specified attachment.
     * 
     * @param user
     *            A <code>User</code>.
     * @param attachment
     *            An <code>Attachment</code>.
     * @return A <code>List<OutgoingEMailInvitation</code>.
     */
    public List<OutgoingEMailInvitation> readOutgoingEMail(final User user,
            final Attachment attachment) {
        final HypersonicSession session = openSession();
        try {
            // read invitations
            session.prepareStatement(SQL_READ_OUTGOING_EMAIL_FK);
            session.setLong(1, user.getLocalId());
            session.setInt(2, attachment.getReferenceType().getId());
            session.setString(3, attachment.getReferenceId());
            session.executeQuery();
            final List<OutgoingEMailInvitation> invitations = new ArrayList<OutgoingEMailInvitation>();
            while (session.nextResult()) {
                invitations.add(extractOutgoingEMail(session));
            }
            return invitations;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public OutgoingEMailInvitation readOutgoingEMail(final User user,
            final EMail email) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_OUTGOING_EMAIL_UK);
            session.setLong(1, user.getLocalId());
            session.setEMail(2, email);
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

    public List<OutgoingUserInvitation> readOutgoingUser(final User user) {
        final HypersonicSession session = openSession();
        try {
            // read all invitations
            session.prepareStatement(SQL_READ_OUTGOING_USER);
            session.setLong(1, user.getLocalId());
            session.executeQuery();
            final List<OutgoingUserInvitation> invitations = new ArrayList<OutgoingUserInvitation>();
            while (session.nextResult()) {
                invitations.add(extractOutgoingUser(session));
            }
            return invitations;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public OutgoingUserInvitation readOutgoingUser(final User user,
            final User invitationUser) {
        final HypersonicSession session = openSession();
        try {
            // read invitation
            session.prepareStatement(SQL_READ_OUTGOING_USER_UK);
            session.setLong(1, user.getLocalId());
            session.setLong(2, invitationUser.getLocalId());
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

    /**
     * Unlock an invitation for a node.
     * 
     * @param invitation
     *            A <code>ContactInvitation</code>.
     */
    public void unlock(final Node node,
            final List<ContactInvitation> invitationList) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_UNLOCK);
            session.setLong(1, node.getId());
            for (final ContactInvitation invitation : invitationList) {
                session.setLong(2, invitation.getId());
                if (1 != session.executeUpdate()) {
                    throw panic("Could not unlock invitation.  {0}", invitation);
                }
            }

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Extract an incoming e-mail invitation from the session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return An <code>IncomingEMailInvitation</code>.
     */
    IncomingEMailInvitation extractIncomingEMail(final HypersonicSession session) {
        final IncomingEMailInvitation invitation = new IncomingEMailInvitation();
        extract(session, invitation);
        invitation.setExtendedBy(userSql.read(session.getLong("EXTENDED_BY_USER_ID")));
        invitation.setInvitationEMail(session.getEMail("EMAIL"));
        return invitation;
    }

    /**
     * Extract an incoming user invitation from the session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return An <code>IncomingUserInvitation</code>.
     */
    IncomingUserInvitation extractIncomingUser(final HypersonicSession session) {
        final IncomingUserInvitation invitation = new IncomingUserInvitation();
        extract(session, invitation);
        invitation.setExtendedBy(userSql.read(session.getLong("EXTENDED_BY_USER_ID")));
        invitation.setInvitationUser(userSql.read(session.getLong("USER_ID")));
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
        invitation.setInvitationEMail(session.getEMail("EMAIL"));
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
        invitation.setInvitationUser(userSql.read(session.getLong("INVITATION_USER_ID")));
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
        session.setLong(1, invitation.getCreatedBy().getLocalId());
        session.setCalendar(2, invitation.getCreatedOn());
        if (1 != session.executeUpdate()) {
            throw panic("Could not create contact invitation.");
        }
        invitation.setId(session.getIdentity("TPSD_CONTACT_INVITATION"));

        session.prepareStatement(SQL_CREATE_LOCK);
        session.setLong(1, invitation.getId());
        if (1 != session.executeUpdate()) {
            throw panic("Could not create contact invitation.");
        }
    }

    /**
     * Delete the generic contact invitation.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param node
     *            A <code>Node</code>.
     * @param invitation
     *            A <code>ContactInvitation</code>.
     */
    private void delete(final HypersonicSession session, final Node node,
            final ContactInvitation invitation) {
        session.prepareStatement(SQL_DELETE_LOCK);
        session.setLong(1, invitation.getId());
        session.setLong(2, node.getId());
        if (1 != session.executeUpdate()) {
            throw panic("Could not delete invitation lock.  {0}", invitation);
        }

        session.prepareStatement(SQL_DELETE);
        session.setLong(1, invitation.getId());
        if (1 != session.executeUpdate()) {
            throw panic("Could not delete invitation.  {0}", invitation);
        }
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
        invitation.setCreatedBy(userSql.read(session.getLong("CREATED_BY")));
        invitation.setCreatedOn(session.getCalendar("CREATED_ON"));
        invitation.setId(session.getLong("CONTACT_INVITATION_ID"));
    }

    /**
     * Extract an attachment from a session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return An <code>Attachment</code>.
     */
    private Attachment extractAttachment(final HypersonicSession session) {
        final Attachment attachment = new Attachment();
        attachment.setInvitationId(session.getLong("CONTACT_INVITATION_ID"));
        attachment.setReferenceId(session.getString("ATTACHMENT_REFERENCE_ID"));
        attachment.setReferenceType(Attachment.ReferenceType.fromId(
                session.getInteger("ATTACHMENT_REFERENCE_TYPE_ID")));
        return attachment;
    }

    /**
     * Extract a container version attachment from a session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return An <code>ContainerVersionAttachment</code>.
     */
    private ContainerVersionAttachment extractContainerVersionAttachment(
            final HypersonicSession session) {
        final ContainerVersionAttachment cva = new ContainerVersionAttachment();
        cva.setInvitationId(session.getLong("CONTACT_INVITATION_ID"));
        cva.setReferenceId(session.getString("ATTACHMENT_REFERENCE_ID"));
        cva.setReferenceType(Attachment.ReferenceType.fromId(
                session.getInteger("ATTACHMENT_REFERENCE_TYPE_ID")));
        return cva;
    }
}
