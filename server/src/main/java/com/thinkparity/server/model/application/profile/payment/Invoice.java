/*
 * Created On:  6-Sep-07 9:09:38 AM
 */
package com.thinkparity.desdemona.model.profile.payment;

import java.util.Calendar;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Invoice {

    /** A retry flag. */
    private Boolean retry;

    /** An invoice date. */
    private Calendar date;

    /** An invoice id. */
    private transient Long id;

    /** An invoice number. */
    private Integer number;

    /** A payment date. */
    private Calendar paymentDate;

    /**
     * Create Invoice.
     *
     */
    public Invoice() {
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
     * Obtain the number.
     *
     * @return A <code>Integer</code>.
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * Obtain the paymentDate.
     *
     * @return A <code>Calendar</code>.
     */
    public Calendar getPaymentDate() {
        return paymentDate;
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
     * Set the number.
     *
     * @param number
     *		A <code>Integer</code>.
     */
    public void setNumber(final Integer number) {
        this.number = number;
    }

    /**
     * Set the paymentDate.
     *
     * @param paymentDate
     *		A <code>Calendar</code>.
     */
    public void setPaymentDate(final Calendar paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * Obtain the retry.
     *
     * @return A <code>Boolean</code>.
     */
    public Boolean isRetry() {
        return retry;
    }

    /**
     * Set the retry.
     *
     * @param retry
     *		A <code>Boolean</code>.
     */
    public void setRetry(final Boolean retry) {
        this.retry = retry;
    }
}
