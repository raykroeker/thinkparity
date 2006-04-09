/*
 * Apr 8, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.xmpp.JabberId;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ConfirmationReceipt extends AuditEvent {

    private JabberId receivedBy;

    /**
     * Create a ConfirmationReceipt.
     */
    public ConfirmationReceipt() { super(); }

    /**
     * @return Returns the receivedBy.
     */
    public JabberId getReceivedBy() { return receivedBy; }

    /**
     * @param receivedBy The receivedBy to set.
     */
    public void setReceivedBy(final JabberId receivedBy) {
        this.receivedBy = receivedBy;
    }
}
