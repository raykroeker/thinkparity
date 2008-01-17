/*
 * Created On:  8-Jan-07 10:58:51 AM
 */
package com.thinkparity.ophelia.model.util.jta;

import com.thinkparity.common.StringUtil;

import com.thinkparity.codebase.model.util.jta.TransactionContext;
import com.thinkparity.codebase.model.util.jta.TransactionType;
import com.thinkparity.codebase.model.util.jta.Xid;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class TransactionContextImpl implements TransactionContext {

    /** The <code>TransactionType</code>. */
    private TransactionType type;

    /** The transaction <code>Xid</code>. */
    private Xid xid;

    /**
     * Create OpheliaTransactionContextn.
     *
     */
    public TransactionContextImpl() {
        super();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     */
    @Override
    public boolean equals(final Object obj) {
        if (null == obj)
            return false;
        if (this == obj)
            return true;
        if (TransactionContextImpl.class.isAssignableFrom(obj.getClass()))
            return ((TransactionContextImpl) obj).xid.equals(xid);
        else
            return false;
    }

    /**
     * Obtain the type.
     * 
     * @return A <code>TransactionType</code>.
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * @see com.thinkparity.codebase.model.util.jta.TransactionContext#getXid()
     *
     */
    public Xid getXid() {
        return xid;
    }

    /**
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        return xid.hashCode();
    }

    /**
     * Set the type.
     *
     * @param type
     *		A <code>TransactionType</code>.
     */
    public void setType(final TransactionType type) {
        this.type = type;
    }

    /**
     * Set the xid.
     *
     * @param xid
     *		An <code>Xid</code>.
     */
    public void setXid(final Xid xid) {
        this.xid = xid;
    }

    /**
     * @see java.lang.Object#toString()
     *
     */
    @Override
    public String toString() {
        return StringUtil.toString(getClass(), "xid", xid, "type", type);
    }
}
