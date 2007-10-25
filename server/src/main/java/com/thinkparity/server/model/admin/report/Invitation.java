/*
 * Created On:  23-Oct-07 6:58:44 PM
 */
package com.thinkparity.desdemona.model.admin.report;

import java.util.Calendar;

import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity Desdemona Model Admin Report Invitation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Invitation {

    /** The accepted on date. */
    private Calendar acceptedOn;

    /** The invited by user. */
    private User invitedBy;

    /** The invited on date. */
    private Calendar invitedOn;

    /** The user. */
    private User user;

    /**
     * Create Invitation.
     *
     */
    public Invitation() {
        super();
    }

    /**
     * Obtain the acceptedOn.
     *
     * @return A <code>Calendar</code>.
     */
    public Calendar getAcceptedOn() {
        return acceptedOn;
    }

    /**
     * Obtain the invitedBy.
     *
     * @return A <code>User</code>.
     */
    public User getInvitedBy() {
        return invitedBy;
    }

    /**
     * Obtain the invitedOn.
     *
     * @return A <code>Calendar</code>.
     */
    public Calendar getInvitedOn() {
        return invitedOn;
    }

    /**
     * Obtain the user.
     *
     * @return A <code>User</code>.
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the acceptedOn.
     *
     * @param acceptedOn
     *		A <code>Calendar</code>.
     */
    public void setAcceptedOn(final Calendar acceptedOn) {
        this.acceptedOn = acceptedOn;
    }

    /**
     * Set the invitedBy.
     *
     * @param invitedBy
     *		A <code>User</code>.
     */
    public void setInvitedBy(final User invitedBy) {
        this.invitedBy = invitedBy;
    }

    /**
     * Set the invitedOn.
     *
     * @param invitedOn
     *		A <code>Calendar</code>.
     */
    public void setInvitedOn(final Calendar invitedOn) {
        this.invitedOn = invitedOn;
    }

    /**
     * Set the user.
     *
     * @param user
     *		A <code>User</code>.
     */
    public void setUser(final User user) {
        this.user = user;
    }
}
