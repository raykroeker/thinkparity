/*
 * Created On:  11-Jan-07 9:31:03 AM
 */
package com.thinkparity.ophelia.model.qtags;

import org.xdoclet.XDocletTag;

import com.thoughtworks.qdox.model.AbstractJavaEntity;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ThinkParityTransactionImpl extends XDocletTag implements
        ThinkParityTransaction {

    /** The xdoclet tag name <code>String</code>. */
    public static final String NAME;

    static {
        NAME = "thinkparity-transaction";
    }

    /**
     * Create ThinkParityInterfaceMethodImpl.
     *
     * @param name
     *            The xdoclet tag name <code>String</code>.
     * @param value
     *            The xdoclet tag value <code>String</code>.
     * @param javaEntity
     *            The qdox <code>AbstractJavaEntity</code>.
     * @param lineNumber
     *            The source line number <code>int</code>.
     */
    public ThinkParityTransactionImpl(final String name,
            final String value, final AbstractJavaEntity javaEntity,
            final int lineNumber) {
        super(name, value, javaEntity, lineNumber);
    }

    /**
     * @see com.thinkparity.ophelia.model.qtags.ThinkParityInterfaceMethod#visibility()
     *
     */
    public String getType() {
        final String type = getNamedParameter("type");
        if (null == type)
            bomb(NAME + " type is required.");
        return type;
    }

    /**
     * @see org.xdoclet.XDocletTag#validateLocation()
     *
     */
    @Override
    protected void validateLocation() {
        if (isOnConstructor)
            bomb("Cannot specify " + NAME + " on a constructor.");
        if (isOnField)
            bomb("Cannot specify " + NAME + " on a field.");
    }
}
