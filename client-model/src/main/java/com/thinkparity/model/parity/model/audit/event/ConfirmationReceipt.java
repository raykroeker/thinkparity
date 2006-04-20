/*
 * Apr 8, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.xmpp.user.User;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ConfirmationReceipt extends AuditEvent {

    private User receivedFrom;

    /**
     * Create a ConfirmationReceipt.
     */
    public ConfirmationReceipt() { super(); }

    /**
     * @return Returns the receivedFrom.
     */
    public User getReceivedFrom() { return receivedFrom; }

    /**
     * @param receivedFrom The receivedFrom to set.
     */
    public void setReceivedFrom(final User receivedBy) {
        this.receivedFrom = receivedBy;
    }
}
