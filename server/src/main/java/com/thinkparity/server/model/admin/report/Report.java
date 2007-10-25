/*
 * Created On:  24-Oct-07 1:19:08 PM
 */
package com.thinkparity.desdemona.model.admin.report;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Title:</b>thinkParity Desdmona Model Admin Report<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Report {

    private final List<Invitation> firstAcceptedInvitationList;

    private final List<Invitation> invitationList;

    private final List<ReportUser> userList;

    /**
     * Create Report.
     *
     */
    public Report() {
        super();
        this.firstAcceptedInvitationList = new ArrayList<Invitation>();
        this.invitationList = new ArrayList<Invitation>();
        this.userList = new ArrayList<ReportUser>();
    }

    /**
     * Obtain the firstAcceptedInvitationList.
     *
     * @return A <code>List<Invitation></code>.
     */
    public List<Invitation> getFirstAcceptedInvitationList() {
        return firstAcceptedInvitationList;
    }

    /**
     * Obtain the invitationList.
     *
     * @return A <code>List<Invitation></code>.
     */
    public List<Invitation> getInvitationList() {
        return invitationList;
    }

    /**
     * Obtain the report user list.
     *
     * @return A <code>List<User></code>.
     */
    public List<ReportUser> getUserList() {
        return userList;
    }

    /**
     * Set the first accepted invitation list.
     * 
     * @param firstAcceptedInvitationList
     *            A <code>List<Invitation></code>.
     */
    public void setFirstAcceptedInvitationList(
            final List<Invitation> firstAcceptedInvitationList) {
        this.firstAcceptedInvitationList.clear();
        this.firstAcceptedInvitationList.addAll(firstAcceptedInvitationList);
    }

    /**
     * Set the invitation list.
     * 
     * @param invitationList
     *            A <code>List<Invitation></code>.
     */
    public void setInvitationList(final List<Invitation> invitationList) {
        this.invitationList.clear();
        this.invitationList.addAll(invitationList);
    }

    /**
     * Set the user list.
     * 
     * @param userList
     *            A <code>List<User></code>.
     */
    public void setUserList(final List<ReportUser> userList) {
        this.userList.clear();
        this.userList.addAll(userList);
    }
}
