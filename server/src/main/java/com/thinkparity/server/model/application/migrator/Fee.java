/*
 * Created On:  27-Sep-07 9:09:33 AM
 */
package com.thinkparity.desdemona.model.migrator;

/**
 * <b>Title:</b>thinkParity Desdemona Model Migrator Fee<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Fee {

    /** The amount. */
    private Long amount;

    /** The description. */
    private String description;

    /** The id. */
    private transient Long id;

    /** The period. */
    private FeePeriod period;

    /**
     * Create Fee.
     *
     */
    public Fee() {
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
     * Obtain the id.
     *
     * @return A <code>Long</code>.
     */
    public Long getId() {
        return id;
    }

    /**
     * Obtain the period.
     *
     * @return A <code>FeePeriod</code>.
     */
    public FeePeriod getPeriod() {
        return period;
    }

    /**
     * Determine if the period is set.
     * 
     * @return True if the period is set.
     */
    public Boolean isSetPeriod() {
        return Boolean.valueOf(null != period);
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
     * Set the id.
     *
     * @param id
     *		A <code>Long</code>.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Set the period.
     *
     * @param period
     *		A <code>FeePeriod</code>.
     */
    public void setPeriod(final FeePeriod period) {
        this.period = period;
    }

    /** <b>Title:</b>Fee Period<br> */
    public enum FeePeriod { MONTH }
}
