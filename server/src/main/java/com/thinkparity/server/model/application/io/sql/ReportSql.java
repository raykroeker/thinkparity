/*
 * Created On:  9-Oct-07 1:01:07 PM
 */
package com.thinkparity.desdemona.model.io.sql;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.user.UserVCard;
import com.thinkparity.codebase.model.util.VCardReader;

import com.thinkparity.desdemona.model.admin.report.Invitation;
import com.thinkparity.desdemona.model.admin.report.ReportUser;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;

/**
 * <b>Title:</b>thinkParity Desdemona Model IO Report SQL<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReportSql extends AbstractSql {

    /** Message format pattern for the column name in extract user. */
    private static final String EXTRACT_USER_NAME_PATTERN =
        "{0}_{1}";

    /** Sql to read accepted invitations. */
    private static final String SQL_READ_ACCEPTED_INVITATION_LIST =
        new StringBuilder("select UI.ACCEPTED_ON,UI.INVITED_ON,")
        .append("U_INVITED_BY.USER_ID \"INVITED_BY_USER_ID\",")
        .append("U_INVITED_BY.USERNAME \"INVITED_BY_USERNAME\",")
        .append("U_INVITED_BY.VCARD \"INVITED_BY_VCARD\",")
        .append("U.USER_ID,U.USERNAME,U.VCARD ")
        .append("from TPSD_USER_INVITATION UI ")
        .append("inner join TPSD_USER U on UI.USER_ID=U.USER_ID ")
        .append("inner join TPSD_USER U_INVITED_BY on U_INVITED_BY.USER_ID=UI.INVITED_BY ")
        .append("inner join TPSD_USER_PAYMENT_PLAN UPP on U.USER_ID=UPP.USER_ID ")
        .append("inner join TPSD_PAYMENT_PLAN PP on PP.PLAN_ID=UPP.PLAN_ID ")
        .append("where UI.ACCEPTED_ON is not null ")
        .append("and PP.PLAN_BILLABLE=?")
        .append("order by UI.ACCEPTED_ON asc")
        .toString();

    /** Sql to read invitations. */
    private static final String SQL_READ_INVITATION_LIST =
        new StringBuilder("select UI.ACCEPTED_ON,UI.INVITED_ON,")
        .append("U_INVITED_BY.USER_ID \"INVITED_BY_USER_ID\",")
        .append("U_INVITED_BY.USERNAME \"INVITED_BY_USERNAME\",")
        .append("U_INVITED_BY.VCARD \"INVITED_BY_VCARD\",")
        .append("U.USER_ID,U.USERNAME,U.VCARD ")
        .append("from TPSD_USER_INVITATION UI ")
        .append("inner join TPSD_USER U on UI.USER_ID=U.USER_ID ")
        .append("inner join TPSD_USER U_INVITED_BY on U_INVITED_BY.USER_ID=UI.INVITED_BY ")
        .append("order by UI.INVITED_ON asc")
        .toString();

    /** Sql to read a user list. */
    private static final String SQL_READ_PLAN_USER_LIST =
        new StringBuilder("select U.USER_ID,U.USERNAME,U.CREATED_ON,U.VCARD,")
        .append("PP.PLAN_BILLABLE,PP.PLAN_NAME,PPII.ITEM_AMOUNT,")
        .append("PPI.INVOICE_DATE,PPI.PAYMENT_DATE ")
        .append("from TPSD_USER U ")
        .append("inner join TPSD_USER_PAYMENT_PLAN UPP on UPP.USER_ID=U.USER_ID ")
        .append("inner join TPSD_PAYMENT_PLAN PP on PP.PLAN_ID=UPP.PLAN_ID ")
        .append("inner join TPSD_PAYMENT_PLAN_INVOICE PPI on PP.PLAN_ID=PPI.PLAN_ID ")
        .append("inner join TPSD_PAYMENT_PLAN_INVOICE_ITEM PPII on PPII.INVOICE_ID=PPI.INVOICE_ID ")
        .append("where U.USERNAME <> ? ")
        .append("and U.USERNAME <> ? ")
        .append("and U.USERNAME <> ?")
        .append("and month(PPI.INVOICE_DATE) = month(current_date) ")
        .append("and PPII.ITEM_NUMBER = ? ")
        .append("order by U.USER_ID asc")
        .toString();

    /** Sql to read a user list. */
    private static final String SQL_READ_NON_PLAN_USER_LIST =
        new StringBuilder("select U.USER_ID,U.USERNAME,U.CREATED_ON,U.VCARD ")
        .append("from TPSD_USER U ")
        .append("where U.USERNAME <> ? ")
        .append("and U.USERNAME <> ? ")
        .append("and U.USERNAME <> ?")
        .append("order by U.USER_ID asc")
        .toString();

    /**
     * Create ReportSql.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     */
    public ReportSql(final DataSource dataSource) {
        super(dataSource, Boolean.TRUE);
    }

    /**
     * Read the accepted invitation list for all billable users.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>List<Invitation></code>.
     */
    public List<Invitation> readAcceptedBillableInvitationList(
            final VCardReader<UserVCard> vcardReader) throws IOException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_ACCEPTED_INVITATION_LIST);
            session.setBoolean(1, Boolean.TRUE);
            session.executeQuery();

            final List<Invitation> invitationList = new ArrayList<Invitation>();
            while (session.nextResult()) {
                invitationList.add(extractInvitation(session, vcardReader));
            }
            return invitationList;
        } finally {
            session.close();
        }
    }

    /**
     * Read the first accepted invitation for all users.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>List<Invitation></code>.
     */
    public List<Invitation> readInvitationList(
            final VCardReader<UserVCard> vcardReader) throws IOException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_INVITATION_LIST);
            session.executeQuery();

            final List<Invitation> invitationList = new ArrayList<Invitation>();
            while (session.nextResult()) {
                invitationList.add(extractInvitation(session, vcardReader));
            }
            return invitationList;
        } finally {
            session.close();
        }
    }

    /**
     * Read a list of users.
     * 
     * @return A <code>List<User></code>.
     */
    public List<ReportUser> readUserList(final VCardReader<UserVCard> vcardReader)
            throws IOException {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_PLAN_USER_LIST);
            session.setString(1, User.THINKPARITY.getSimpleUsername());
            session.setString(2, User.THINKPARITY_BACKUP.getSimpleUsername());
            session.setString(3, User.THINKPARITY_SUPPORT.getSimpleUsername());
            session.setInt(4, 0);
            session.executeQuery();
            final List<ReportUser> reportUserList = new ArrayList<ReportUser>();
            while (session.nextResult()) {
                reportUserList.add(extractPlanReportUser(session, vcardReader));
            }
            session.prepareStatement(SQL_READ_NON_PLAN_USER_LIST);
            session.setString(1, User.THINKPARITY.getSimpleUsername());
            session.setString(2, User.THINKPARITY_BACKUP.getSimpleUsername());
            session.setString(3, User.THINKPARITY_SUPPORT.getSimpleUsername());
            session.executeQuery();
            while (session.nextResult()) {
                reportUserList.add(extractNoPlanReportUser(session, vcardReader));
            }

            return reportUserList;
        } finally {
            session.close();
        }
    }

    /**
     * Extract an invitation.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param vcardReader
     *            A <code>VCardReader<UserVCard></code>.
     * @return An <code>Invitation</code>.
     * @throws IOException
     */
    private Invitation extractInvitation(final HypersonicSession session,
            final VCardReader<UserVCard> vcardReader) throws IOException {
        final Invitation invitation = new Invitation();
        invitation.setAcceptedOn(session.getCalendar("ACCEPTED_ON"));
        invitation.setInvitedBy(extractUser(session, "INVITED_BY", vcardReader));
        invitation.setInvitedOn(session.getCalendar("INVITED_ON"));
        invitation.setUser(extractUser(session, vcardReader));
        return invitation;
    }

    /**
     * Extract a report user.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param vcardReader
     *            A <code>VCardReader<User></code>.
     * @return A <code>ReportUser</code>.
     * @throws IOException
     */
    private ReportUser extractPlanReportUser(final HypersonicSession session,
            final VCardReader<UserVCard> vcardReader) throws IOException {
        final ReportUser reportUser = new ReportUser();
        final UserVCard reportUserVCard = session.getVCard(("VCARD"),
                new UserVCard() {}, vcardReader);
        reportUser.setAddress(reportUserVCard.getAddress());
        reportUser.setBillable(session.getBoolean("PLAN_BILLABLE"));
        reportUser.setCity(reportUserVCard.getCity());
        reportUser.setCountry(reportUserVCard.getCountry());
        reportUser.setCreatedOn(session.getCalendar("CREATED_ON"));
        reportUser.setId(JabberIdBuilder.parseUsername(session.getString("USERNAME")));
        reportUser.setInvoiceDate(session.getCalendar("INVOICE_DATE"));
        reportUser.setLanguage(reportUserVCard.getLanguage());
        reportUser.setLocalId(session.getLong("USER_ID"));
        reportUser.setMobilePhone(reportUserVCard.getMobilePhone());
        reportUser.setName(reportUserVCard.getName());
        reportUser.setOrganization(reportUserVCard.getOrganization());
        reportUser.setPaymentAmount(session.getLong("ITEM_AMOUNT"));
        reportUser.setPaymentDate(session.getCalendar("PAYMENT_DATE"));
        reportUser.setPaymentPlanName(session.getString("PLAN_NAME"));
        reportUser.setPhone(reportUserVCard.getPhone());
        reportUser.setPostalCode(reportUserVCard.getPostalCode());
        reportUser.setProvince(reportUserVCard.getProvince());
        reportUser.setTimeZone(reportUserVCard.getTimeZone());
        reportUser.setTitle(reportUserVCard.getTitle());
        return reportUser;
    }

    /**
     * Extract a report user.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param vcardReader
     *            A <code>VCardReader<User></code>.
     * @return A <code>ReportUser</code>.
     * @throws IOException
     */
    private ReportUser extractNoPlanReportUser(final HypersonicSession session,
            final VCardReader<UserVCard> vcardReader) throws IOException {
        final ReportUser reportUser = new ReportUser();
        final UserVCard reportUserVCard = session.getVCard(("VCARD"),
                new UserVCard() {}, vcardReader);
        reportUser.setAddress(reportUserVCard.getAddress());
        reportUser.setBillable(Boolean.FALSE);
        reportUser.setCity(reportUserVCard.getCity());
        reportUser.setCountry(reportUserVCard.getCountry());
        reportUser.setCreatedOn(session.getCalendar("CREATED_ON"));
        reportUser.setId(JabberIdBuilder.parseUsername(session.getString("USERNAME")));
        reportUser.setLanguage(reportUserVCard.getLanguage());
        reportUser.setLocalId(session.getLong("USER_ID"));
        reportUser.setMobilePhone(reportUserVCard.getMobilePhone());
        reportUser.setName(reportUserVCard.getName());
        reportUser.setOrganization(reportUserVCard.getOrganization());
        reportUser.setPaymentAmount(null);
        reportUser.setPaymentDate(null);
        reportUser.setPaymentPlanName(null);
        reportUser.setPhone(reportUserVCard.getPhone());
        reportUser.setPostalCode(reportUserVCard.getPostalCode());
        reportUser.setProvince(reportUserVCard.getProvince());
        reportUser.setTimeZone(reportUserVCard.getTimeZone());
        reportUser.setTitle(reportUserVCard.getTitle());
        return reportUser;
    }

    /**
     * Extract a user. The column name prefix is used as a pre-cursor to the
     * user id; username and vcard column names.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param columnNamePrefix
     *            A <code>String</code>.
     * @param vcardReader
     *            A <code>VCardReader<User></code>.
     * @return A <code>User</code>.
     * @throws IOException
     */
    private User extractUser(final HypersonicSession session,
            final String columnNamePrefix,
            final VCardReader<UserVCard> vcardReader) throws IOException {
        final User user = new User();
        user.setLocalId(session.getLong(getColumnName(columnNamePrefix, "USER_ID")));
        user.setId(JabberIdBuilder.parseUsername(session.getString(getColumnName(columnNamePrefix, "USERNAME"))));
        final UserVCard userVCard = session.getVCard(getColumnName(columnNamePrefix, "VCARD"),
                new UserVCard() {}, vcardReader);
        user.setName(userVCard.getName());
        user.setOrganization(userVCard.getOrganization());
        user.setTitle(userVCard.getTitle());
        return user;
    }

    /**
     * Extract a user.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param vcardReader
     *            A <code>VCardReader<User></code>.
     * @return A <code>User</code>.
     * @throws IOException
     */
    private User extractUser(final HypersonicSession session,
            final VCardReader<UserVCard> vcardReader) throws IOException {
        return extractUser(session, null, vcardReader);
    }

    /**
     * Obtain the column name given a prefix and suffix.
     * 
     * @param prefix
     *            A <code>String</code>.
     * @param suffix
     *            A <code>String</code>.
     * @return A <code>String</code>.
     */
    private String getColumnName(final String prefix, final String suffix) {
        if (null == prefix) {
            return suffix;
        } else {
            return MessageFormat.format(EXTRACT_USER_NAME_PATTERN, prefix, suffix);
        }
    }
}
