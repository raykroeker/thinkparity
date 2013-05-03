/*
 * Created On:  5-Sep-07 11:15:55 AM
 */
package com.thinkparity.desdemona.model.profile.payment.provider;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Client {

    /** A client id. */
    private String id;

    /**
     * Create Customer.
     *
     */
    public Client() {
        super();
    }

    /**
     * Obtain the id.
     *
     * @return A <code>String</code>.
     */
    public String getId() {
        return id;
    }

    /**
     * Set the id.
     *
     * @param id
     *		A <code>String</code>.
     */
    public void setId(final String id) {
        this.id = id;
    }
}
