/*
 * Created On:  7-Feb-07 9:06:48 PM
 */
package com.thinkparity.ophelia.model.util.jta;

import com.thinkparity.codebase.model.util.jta.Xid;

import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity OpheliaModel XidFactory<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class XidFactory {

    /**
     * Obtain an instance of an xid factory.
     * 
     * @param workspace
     *            A <code>Workspace</code>.
     * @return An <code>XidFactory</code>.
     */
    public static XidFactory getInstance(final Workspace workspace) {
        return new XidFactory(workspace);
    }

    /** An xid root <code>String</code>. */
    private final String xidRoot;

    /**
     * Create XidFactory.
     * 
     * @param workspace
     *            A <code>Workspace</code>.
     */
    private XidFactory(final Workspace workspace) {
        super();
        this.xidRoot = workspace.getWorkspaceDirectory().getName();
    }

    /**
     * Create an xid.
     * 
     * @param xid
     *            An xid <code>String</code>.
     * @return An <code>Xid</code>.
     */
    public Xid createXid(final String xid) {
        return new XidImpl(new StringBuffer(xidRoot).append('_')
                .append(xid).toString());
    }
}
