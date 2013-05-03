/*
 * Created On:  28-Sep-07 4:19:37 PM
 */
package com.thinkparity.desdemona.model.profile.payment.provider;

/**
 * <b>Title:</b>thinkParity Desdemona Model Profile Payment Provider Transaction<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Transaction {

    /** An order id. */
    private String id;

    /**
     * Create Transaction.
     *
     */
    public Transaction() {
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
