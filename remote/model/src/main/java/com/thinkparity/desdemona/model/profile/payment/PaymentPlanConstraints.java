/*
 * Created On:  6-Oct-07 4:19:12 PM
 */
package com.thinkparity.desdemona.model.profile.payment;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.constraint.Constraint;
import com.thinkparity.codebase.constraint.IntegerConstraint;
import com.thinkparity.codebase.constraint.PasswordConstraint;
import com.thinkparity.codebase.constraint.StringConstraint;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.profile.payment.PaymentPlan.InvoicePeriod;

/**
 * <b>Title:</b>thinkParity Desdemona Model Profile Payment Plan Constraints<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PaymentPlanConstraints {

    /** A singleton instance. */
    private static PaymentPlanConstraints INSTANCE;

    /**
     * Obtain an instance of payment plan constraints.
     * 
     * @return A set of <code>PaymentPlanConstraints</code>.
     */
    public static PaymentPlanConstraints getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new PaymentPlanConstraints();
        }
        return INSTANCE;
    }

    /** The currency constraint. */
    private final Constraint<Currency> currency;

    /** The invoice period constraint. */
    private final Constraint<InvoicePeriod> invoicePeriod;

    /** The invoice period offset constraint. */
    private final IntegerConstraint invoicePeriodOffset;

    /** The name constraint. */
    private final StringConstraint name;

    /** The owner constraint. */
    private final Constraint<User> owner;

    /** The password constraint. */
    private final PasswordConstraint password;

    /** The unique id constraint. */
    private final Constraint<UUID> uniqueId;

    /**
     * Create PaymentPlanConstraints.
     *
     */
    private PaymentPlanConstraints() {
        super();
        this.currency = new Constraint<Currency>() {};
        this.currency.setName("Currency");
        this.currency.setNullable(Boolean.FALSE);

        this.invoicePeriod = new Constraint<InvoicePeriod>() {};
        this.invoicePeriod.setName("Invoice period");
        this.invoicePeriod.setNullable(Boolean.FALSE);

        this.invoicePeriodOffset = new IntegerConstraint();
        this.invoicePeriodOffset.setMinValue(1);
        this.invoicePeriodOffset.setName("Invoice period offset");
        this.invoicePeriodOffset.setNullable(Boolean.FALSE);

        this.name = new StringConstraint();
        this.name.setMaxLength(64);
        this.name.setMinLength(8);
        this.name.setName("Name");
        this.name.setNullable(Boolean.FALSE);

        this.owner = new Constraint<User>() {};
        this.owner.setName("Owner");
        this.owner.setNullable(Boolean.FALSE);

        this.password = new PasswordConstraint();
        this.password.setMaxLength(64);
        this.password.setMinLength(6);
        this.password.setName("Password");
        this.password.setNullable(Boolean.TRUE);

        this.uniqueId = new Constraint<UUID>() {};
        this.uniqueId.setName("Unique id");
        this.uniqueId.setNullable(Boolean.FALSE);
    }

    /**
     * Obtain the currency.
     *
     * @return A <code>Constraint<Currency></code>.
     */
    public Constraint<Currency> getCurrency() {
        return currency;
    }

    /**
     * Obtain the invoicePeriod.
     *
     * @return A <code>Constraint<InvoicePeriod></code>.
     */
    public Constraint<InvoicePeriod> getInvoicePeriod() {
        return invoicePeriod;
    }

    /**
     * Obtain the invoicePeriodOffset.
     * 
     * @param now
     *            A <code>Calendar</code>.
     * @param invoicePeriod
     *            An <code>InvoicePeriod</code>.
     * @return A <code>IntegerConstraint</code>.
     */
    public IntegerConstraint getInvoicePeriodOffset(
            final Calendar now, final InvoicePeriod invoicePeriod) {
        final IntegerConstraint clone = (IntegerConstraint) invoicePeriodOffset.clone();
        final Integer maxValue;
        switch (invoicePeriod) {
        case MONTH:
            maxValue = now.getMaximum(Calendar.DAY_OF_MONTH);
            break;
        default:
            throw Assert.createUnreachable("Unexpected invoice period {0}.",
                    invoicePeriod);
        }
        clone.setMaxValue(maxValue);
        return clone;
    }

    /**
     * Obtain the name.
     *
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getName() {
        return name;
    }

    /**
     * Obtain the owner.
     *
     * @return A <code>Constraint<User></code>.
     */
    public Constraint<User> getOwner() {
        return owner;
    }

    /**
     * Obtain the password.
     *
     * @return A <code>PasswordConstraint</code>.
     */
    public PasswordConstraint getPassword() {
        return password;
    }

    /**
     * Obtain the uniqueId.
     *
     * @return A <code>Constraint<UUID></code>.
     */
    public Constraint<UUID> getUniqueId() {
        return uniqueId;
    }
}
