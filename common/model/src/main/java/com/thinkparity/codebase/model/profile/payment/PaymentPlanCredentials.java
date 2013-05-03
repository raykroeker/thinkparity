/*
 * Created On:  30-May-07 10:01:57 AM
 */
package com.thinkparity.codebase.model.profile.payment;

/**
 * <b>Title:</b>thinkParity CommonModel Profile Payment Plan Credentials<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PaymentPlanCredentials {

    /** A plan name. */
    private String name;

    /** A plan password. */
    private String password;

    /**
     * Create PaymentPlanCredentials.
     *
     */
    public PaymentPlanCredentials() {
        super();
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
     * Obtain password.
     *
     * @return A String.
     */
    public String getPassword() {
        return password;
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
     * Set password.
     *
     * @param password
     *		A String.
     */
    public void setPassword(final String password) {
        this.password = password;
    }
}
