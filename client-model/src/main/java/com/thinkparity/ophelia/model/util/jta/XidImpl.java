/*
 * Created On:  8-Jan-07 11:02:59 AM
 */
package com.thinkparity.ophelia.model.util.jta;

import java.util.Arrays;

import com.thinkparity.codebase.model.util.jta.Xid;

/**
 * <b>Title:</b>thinkParity Ophelia Model Xid<br>
 * <b>Description:</b>A transaction id for the ophelia model.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class XidImpl implements Xid {

    /** The transaction id <code>String</code>. */
    private final String id;

    /** The transaction id <code>byte[]</code>. */
    private final byte[] idBytes;

    /**
     * Create XidImpl.
     * 
     * @param id
     *            A transaction id <code>String</code>.
     */
    public XidImpl(final String id) {
        super();
        this.id = id;
        this.idBytes = id.getBytes();
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
        if (XidImpl.class.isAssignableFrom(obj.getClass()))
            return Arrays.equals(((XidImpl) obj).idBytes, idBytes);
        else
            return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        return idBytes.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     *
     */
    @Override
    public String toString() {
        return id;
    }
}
