/*
 * Created On: Jul 13, 2006 10:17:57 AM
 */
package com.thinkparity.model.parity.model.contact;

import java.util.Calendar;

import com.thinkparity.model.xmpp.JabberId;

/**
 * <b>Title:</b>thinkParity Contact Invitation<br>
 * <b>Description:</b>
 * 
 * @author raymond@thinkparity.com
 * @version
 */
public class ContactInvitation {

    /** By whom the invitation was created. */
    private JabberId createdBy;

    /** The date/time the invitation was created. */
    private Calendar createdOn;

    /** The e-mail address the invitation was sent to. */
    private String email;

    /** The invitation id. */
    private Long id;

    /** Create ContactInvitation. */
    public ContactInvitation() { super(); }

    /**
     * Obtain the createdBy
     *
     * @return The JabberId.
     */
    public JabberId getCreatedBy() { return createdBy; }

    /**
     * Obtain the createdOn
     *
     * @return The Calendar.
     */
    public Calendar getCreatedOn() { return createdOn; }

    /**
     * Obtain the email
     *
     * @return The String.
     */
    public String getEmail() { return email; }

    /**
     * Obtain the id
     *
     * @return The Long.
     */
    public Long getId() { return id; }

    /**
     * Set createdBy.
     *
     * @param createdBy The JabberId.
     */
    public void setCreatedBy(final JabberId createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Set createdOn.
     *
     * @param createdOn The Calendar.
     */
    public void setCreatedOn(final Calendar createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * Set email.
     *
     * @param email The String.
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Set id.
     *
     * @param id The Long.
     */
    public void setId(final Long id) { this.id = id; }
}
