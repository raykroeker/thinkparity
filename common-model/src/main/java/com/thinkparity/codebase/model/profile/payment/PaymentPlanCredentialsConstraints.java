/*
 * Created On:  8-Aug-07 6:16:31 PM
 */
package com.thinkparity.codebase.model.profile.payment;

import com.thinkparity.codebase.constraint.StringConstraint;

/**
 * <b>Title:</b>thinkParity Common Model Profile Payment Info Constraints<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PaymentPlanCredentialsConstraints {

    /** A singleton instance. */
    private static PaymentPlanCredentialsConstraints INSTANCE;

    /**
     * Obtain an instance of payment plan credentials constraints.
     * 
     * @return A <code>PaymentPlanCredentialsConstraints</code>.
     */
    public static PaymentPlanCredentialsConstraints getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new PaymentPlanCredentialsConstraints();
        }
        return INSTANCE;
    }

    /** A name constraint. */
    private final StringConstraint name;

    /** A password constraint. */
    private final StringConstraint password;

    /**
     * Create PaymentInfoConstraints.
     *
     */
    private PaymentPlanCredentialsConstraints() {
        super();
        this.name = new StringConstraint();
        this.name.setMaxLength(128);
        this.name.setMinLength(6);
        this.name.setName("Payment plan credentials name.");
        this.name.setNullable(Boolean.FALSE);

        this.password = new StringConstraint();
        this.password.setMaxLength(32);
        this.password.setMinLength(6);
        this.password.setName("Payment plan credentials password.");
        this.password.setNullable(Boolean.FALSE);
    }

    /**
     * Obtain the name constraint.
     * 
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getName() {
        return name;
    }

    /**
     * Obtain the password constraint.
     *
     * @return A <code>StringConstraint</code>.
     */
    public StringConstraint getPassword() {
        return password;
    }
}
