/*
 * Created On:  8-Jan-07 10:34:14 AM
 */
package com.thinkparity.codebase.model.util.jta;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface TransactionContext {

    /**
     * Obtain the transction type.
     * 
     * @return A <code>TransactionType</code>.
     */
    public TransactionType getType();

    /**
     * Obtain the transaction id.
     * 
     * @return An <code>Xid</code>.
     */
    public Xid getXid();
}
