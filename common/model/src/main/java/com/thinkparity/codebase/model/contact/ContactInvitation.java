/*
 * Created On: Jul 13, 2006 10:17:57 AM
 */
package com.thinkparity.codebase.model.contact;

import java.util.Calendar;

/**
 * <b>Title:</b>thinkParity Contact Invitation<br>
 * <b>Description:</b>
 * 
 * @author raymond@thinkparity.com
 * @version
 */
public abstract class ContactInvitation {

    /** By whom the invitation was created. */
    protected Long createdBy;

    /** The date/time the invitation was created. */
    protected Calendar createdOn;

    /** The invitation id. */
    protected Long id;

    /** Create ContactInvitation. */
    protected ContactInvitation() { super(); }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (null != obj && getClass().isAssignableFrom(obj.getClass())) {
            return ((ContactInvitation) obj).id.equals(id);
        }
        return false;
    }

    /**
     * Obtain the createdBy
     *
     * @return The JabberId.
     */
    public Long getCreatedBy() { return createdBy; }

    /**
     * Obtain the createdOn
     *
     * @return The Calendar.
     */
    public Calendar getCreatedOn() { return createdOn; }

    /**
     * Obtain the id
     *
     * @return The Long.
     */
    public Long getId() { return id; }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Set createdBy.
     *
     * @param createdBy The JabberId.
     */
    public void setCreatedBy(final Long createdBy) {
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
     * Set id.
     *
     * @param id The Long.
     */
    public void setId(final Long id) { this.id = id; }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(id)
                .toString();
    }
}
