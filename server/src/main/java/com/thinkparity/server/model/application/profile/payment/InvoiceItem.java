/*
 * Created On:  6-Sep-07 9:12:04 AM
 */
package com.thinkparity.desdemona.model.profile.payment;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class InvoiceItem {

    /** An invoice item amount. */
    private Long amount;

    /** An invoice item description. */
    private String description;

    /** A number. */
    private Integer number;

    /**
     * Create InvoiceItem.
     *
     */
    public InvoiceItem() {
        super();
    }

    /**
     * Obtain the amount.
     *
     * @return A <code>Long</code>.
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * Obtain the description.
     *
     * @return A <code>String</code>.
     */
    public String getDescription() {
        return description;
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
     * Set the amount.
     *
     * @param amount
     *		A <code>Long</code>.
     */
    public void setAmount(final Long amount) {
        this.amount = amount;
    }

    /**
     * Set the description.
     *
     * @param description
     *		A <code>String</code>.
     */
    public void setDescription(final String description) {
        this.description = description;
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
}
