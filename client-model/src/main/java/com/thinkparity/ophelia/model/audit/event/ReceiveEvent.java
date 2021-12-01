/*
 * Created On: Feb 21, 2006
 * $Id$
 */
package com.thinkparity.ophelia.model.audit.event;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.ophelia.model.audit.AuditEventType;

/**
 * This event is recorded when a document is received.
 *
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class ReceiveEvent extends AuditVersionEvent {

    /** From whom the document was received. */
	private JabberId receivedBy;

    private Calendar receivedOn;

	/** Create  ReceiveEvent. */
	public ReceiveEvent() {
        super(AuditEventType.RECEIVE);
	}

	/**
     * Obtain the received from user.
     *
	 * @return A user.
	 */
	public JabberId getReceivedBy() { return receivedBy; }

	/**
     * Obtain the receivedOn
     *
     * @return The Calendar.
     */
    public Calendar getReceivedOn() {
        return receivedOn;
    }

    /**
     * Set the received from user.
     *
	 * @param receivedFrom A user.
	 */
	public void setReceivedBy(final JabberId receivedBy) {
		this.receivedBy = receivedBy;
	}

    /**
     * Set receivedOn.
     *
     * @param receivedOn The Calendar.
     */
    public void setReceivedOn(final Calendar receivedOn) {
        this.receivedOn = receivedOn;
    }
}
