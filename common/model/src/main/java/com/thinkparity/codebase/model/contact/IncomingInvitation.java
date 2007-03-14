/*
 * Created On: Aug 14, 2006 10:48:55 AM
 */
package com.thinkparity.codebase.model.contact;

import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity OpheliaModel Incoming Contact Invitation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class IncomingInvitation extends ContactInvitation {

    /** The extended by <code>User</code>. */
    protected User extendedBy;

    /**
     * Create IncomingInvitation.
     *
     */
    protected IncomingInvitation() {
        super();
    }

    public User getExtendedBy() {
        return extendedBy;
    }

    public void setExtendedBy(final User extendedBy) {
        this.extendedBy = extendedBy;
    }
}
