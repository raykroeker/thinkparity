/*
 * Created On:  Wed May 03 13:02:07 PDT 2006
 * $Id$
 */
package com.thinkparity.ophelia.model.audit.event;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.ophelia.model.audit.AuditEventType;

/**
 * The audit event created when a document is published.
 *
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class PublishEvent extends AuditVersionEvent {

    private JabberId publishedBy;

    private Calendar publishedOn;

	/**
     * Create PublishEvent.
     * 
     */
	public PublishEvent() {
        super(AuditEventType.PUBLISH);
	}

    /**
     * Obtain the publishedBy
     *
     * @return The JabberId.
     */
    public JabberId getPublishedBy() {
        return publishedBy;
    }

    /**
     * Obtain the publishedOn
     *
     * @return The Calendar.
     */
    public Calendar getPublishedOn() {
        return publishedOn;
    }

    /**
     * Set publishedBy.
     *
     * @param publishedBy The JabberId.
     */
    public void setPublishedBy(JabberId publishedBy) {
        this.publishedBy = publishedBy;
    }

    /**
     * Set publishedOn.
     *
     * @param publishedOn The Calendar.
     */
    public void setPublishedOn(Calendar publishedOn) {
        this.publishedOn = publishedOn;
    }
}
