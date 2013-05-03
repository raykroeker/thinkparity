/*
 * Created On:  24-Sep-07 11:04:53 AM
 */
package com.thinkparity.ophelia.model.workspace;

import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class TransactionContext {

    /** A transaction type. */
    private TransactionType type;

    /** A transaction id. */
    private String xid;

    /**
     * Create TransactionContext.
     *
     */
    public TransactionContext() {
        super();
    }

    /**
     * Obtain the type.
     *
     * @return A <code>Type</code>.
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * Obtain the xid.
     *
     * @return A <code>String</code>.
     */
    public String getXid() {
        return xid;
    }

    /**
     * Set the type.
     *
     * @param type
     *		A <code>Type</code>.
     */
    public void setType(final TransactionType type) {
        this.type = type;
    }

    /**
     * Set the xid.
     *
     * @param xid
     *		A <code>String</code>.
     */
    public void setXid(final String xid) {
        this.xid = xid;
    }
}
