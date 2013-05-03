/*
 * Created On:  1-Oct-07 6:47:39 PM
 */
package com.thinkparity.desdemona.model.profile.payment;

import java.util.Calendar;
import java.util.UUID;

/**
 * <b>Title:</b>thinkParity Desdemona Model Profile Payment Invoice Transaction<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class InvoiceTransaction {

    /** A date. */
    private Calendar date;

    /** An id. */
    private transient Long id;

    /** A pass/fail result. */
    private Boolean success;

    /** A unique id. */
    private UUID uniqueId;

    /**
     * Create InvoiceTransaction.
     *
     */
    public InvoiceTransaction() {
        super();
    }

    /**
     * Obtain the date.
     *
     * @return A <code>Calendar</code>.
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * Obtain the id.
     *
     * @return A <code>Long</code>.
     */
    public Long getId() {
        return id;
    }

    /**
     * Obtain the uniqueId.
     *
     * @return A <code>UUID</code>.
     */
    public UUID getUniqueId() {
        return uniqueId;
    }

    /**
     * Determine the result.
     * 
     * @return True if the transaction succeeded.
     */
    public Boolean isSuccess() {
        return success;
    }

    /**
     * Set the date.
     *
     * @param date
     *		A <code>Calendar</code>.
     */
    public void setDate(final Calendar date) {
        this.date = date;
    }

    /**
     * Set the id.
     *
     * @param id
     *		A <code>Long</code>.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Set the success.
     *
     * @param success
     *		A <code>Boolean</code>.
     */
    public void setSuccess(final Boolean success) {
        this.success = success;
    }

    /**
     * Set the uniqueId.
     *
     * @param uniqueId
     *		A <code>UUID</code>.
     */
    public void setUniqueId(final UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
}
