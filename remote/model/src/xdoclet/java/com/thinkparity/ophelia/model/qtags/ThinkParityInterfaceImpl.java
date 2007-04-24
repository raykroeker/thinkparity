/*
 * Created On:  11-Jan-07 11:42:08 AM
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
public final class ThinkParityInterfaceImpl extends XDocletTag implements
        ThinkParityInterface {

    /** The xdoclet tag name <code>String</code>. */
    public static final String NAME;

    static {
        NAME = "thinkparity-interface";
    }

    /**
     * Create ThinkParityInterfaceImpl.
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
    public ThinkParityInterfaceImpl(final String name, final String value,
            final AbstractJavaEntity javaEntity, final int lineNumber) {
        super(name, value, javaEntity, lineNumber);
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
        if (isOnMethod)
            bomb("Cannot specify " + NAME + " on a method.");
    }
}
