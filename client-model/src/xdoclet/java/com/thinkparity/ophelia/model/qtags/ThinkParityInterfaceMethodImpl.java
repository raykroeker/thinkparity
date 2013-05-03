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
public final class ThinkParityInterfaceMethodImpl extends XDocletTag implements
        ThinkParityInterfaceMethod {

    /** The xdoclet tag name <code>String</code>. */
    public static final String NAME;

    static {
        NAME = "thinkparity.interface-method";
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
    public ThinkParityInterfaceMethodImpl(final String name,
            final String value, final AbstractJavaEntity javaEntity,
            final int lineNumber) {
        super(name, value, javaEntity, lineNumber);
    }

    /**
     * @see com.thinkparity.ophelia.model.qtags.ThinkParityInterfaceMethod#visibility()
     *
     */
    public String getVisibility() {
        final String visibility = getNamedParameter("visibility");
        if (null == visibility)
            bomb(NAME + " visibility is required.");
        return visibility;
    }

    /**
     * @see org.xdoclet.XDocletTag#validateLocation()
     *
     */
    @Override
    protected void validateLocation() {
        if (isOnClass)
            bomb("Cannot specify " + NAME + " on a class.");
        if (isOnConstructor)
            bomb("Cannot specify " + NAME + " on a constructor.");
        if (isOnField)
            bomb("Cannot specify " + NAME + " on a field.");
    }
}
