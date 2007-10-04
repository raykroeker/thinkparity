/*
 * Created On:  4-Sep-07 8:08:15 PM
 */
package com.thinkparity.payment.moneris;

import java.text.MessageFormat;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PaymentTransaction {

    /** A pattern for transaction ids. */
    private static final String PATTERN;

    static {
        PATTERN = "thinkParity Solutions Inc. {0}";
    }

    /** A transaction id. */
    private String id;

    /**
     * Create PaymentTransaction.
     *
     */
    public PaymentTransaction() {
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
        this.id = MessageFormat.format(PATTERN, id);
    }
}
