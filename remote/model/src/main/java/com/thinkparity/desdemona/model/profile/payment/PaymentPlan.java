/*
 * Created On:  8-Aug-07 4:53:24 PM
 */
package com.thinkparity.desdemona.model.profile.payment;

import java.util.UUID;

import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity Desdemona Model Profile Payment Plan<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PaymentPlan {

    /** An arrears flag. */
    private Boolean arrears;

    /** A billable flag. */
    private Boolean billable;

    /** A currency. */
    private Currency currency;

    /** A plan id. */
    private transient Long id;

    /** An invoice period. */
    private InvoicePeriod invoicePeriod;

    /** An invoice period offset. */
    private Integer invoicePeriodOffset;

    /** A name. */
    private String name;

    /** An owner. */
    private User owner;

    /** A password. */
    private String password;

    /** A unique id. */
    private transient UUID uniqueId;

    /**
     * Create PaymentPlan.
     *
     */
    public PaymentPlan() {
        super();
    }

    /**
     * Obtain the currency.
     *
     * @return A <code>Currency</code>.
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Obtain the plan id.
     *
     * @return An id <code>Long</code>.
     */
    public Long getId() {
        return id;
    }

    /**
     * Obtain the invoicePeriod.
     *
     * @return A <code>InvoicePeriod</code>.
     */
    public InvoicePeriod getInvoicePeriod() {
        return invoicePeriod;
    }

    /**
     * Obtain the invoicePeriodOffset.
     *
     * @return A <code>Integer</code>.
     */
    public Integer getInvoicePeriodOffset() {
        return invoicePeriodOffset;
    }

    /**
     * Obtain name.
     *
     * @return A String.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtain the owner.
     *
     * @return A <code>User</code>.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Obtain password.
     *
     * @return A String.
     */
    public String getPassword() {
        return password;
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
     * Determine if the plan is in arrears.
     * 
     * @return A <code>Boolean</code>.
     */
    public Boolean isArrears() {
        return arrears;
    }

    /**
     * Obtain billable.
     *
     * @return A <code>Boolean</code>.
     */
    public Boolean isBillable() {
        return billable;
    }

    /**
     * Set the arrears.
     *
     * @param arrears
     *		A <code>Boolean</code>.
     */
    public void setArrears(final Boolean arrears) {
        this.arrears = arrears;
    }

    /**
     * Set billable.
     *
     * @param billable
     *      A <code>Boolean</code>.
     */
    public void setBillable(final Boolean billable) {
        this.billable = billable;
    }

    /**
     * Set the currency.
     *
     * @param currency
     *		A <code>Currency</code>.
     */
    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    /**
     * Set the plan id.
     *
     * @param id
     *		An id <code>Long</code>.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Set the invoicePeriod.
     *
     * @param invoicePeriod
     *		A <code>InvoicePeriod</code>.
     */
    public void setInvoicePeriod(final InvoicePeriod invoicePeriod) {
        this.invoicePeriod = invoicePeriod;
    }

    /**
     * Set the invoicePeriodOffset.
     *
     * @param invoicePeriodOffset
     *		A <code>Integer</code>.
     */
    public void setInvoicePeriodOffset(final Integer invoicePeriodOffset) {
        this.invoicePeriodOffset = invoicePeriodOffset;
    }

    /**
     * Set name.
     *
     * @param name
     *		A String.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Set the owner.
     *
     * @param owner
     *      A <code>User</code>.
     */
    public void setOwner(final User owner) {
        this.owner = owner;
    }

    /**
     * Set password.
     *
     * @param password
     *		A String.
     */
    public void setPassword(final String password) {
        this.password = password;
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

    /** <b>Title:</b>Invoice Period<br> */
    public enum InvoicePeriod { MONTH }
}
