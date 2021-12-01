/*
 * Created On: Feb 22, 2006
 */
package com.thinkparity.ophelia.model.audit;

import java.util.Calendar;

/**
 * The history item is a read-only localized version of the user's local audit
 * events.  The history item includes the date of the event; and text describing
 * the nature of the event.
 * 
 * The history item also includes links to the document and potentially a
 * specific version of that document.
 * 
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public abstract class HistoryItem {

	/** The event date and time. */
	private Calendar date;

	/** The event text. */
	private String event;

	/** The history item id. */
    private Long id;

    /** Flag indicating the pending state of this event. */
    private Boolean pending;

	/** Create a HistoryItem. */
	protected HistoryItem() { super(); }

	/**
	 * Obtain the history item date.
	 * 
	 * @return The history item date.
	 */
	public Calendar getDate() { return date; }

	/**
	 * Obtain the history item's event.
	 * 
	 * @return The history item's event.
	 */
	public String getEvent() { return event; }

    /**
     * Obtain the history item id.
     *
     * @return The history item id.
     */
    public Long getId() { return id; }

    /**
     * Determine whether or not this event is pending.
     *
     * @return True if pending; false otherwise.
     */
    public Boolean isPending() { return pending; }

	/**
	 * Set the history item's event date.
	 * 
	 * @param date
	 * 	The event date.
	 */
	public void setDate(final Calendar date) { this.date = date; }

    /**
     * Set the history item's event text.
     * 
     * @param event
     *            The history item's event text.
     */
	public void setEvent(final String event) {
		this.event = event;
	}

	/**
     * Set the history item id.
     *
     * @param id
     *      The history item id.
     */
    public void setId(final Long id) { this.id = id; }

    /**
     * Set the pending state.
     * 
     * @param pending
     *            True if the item is pending; false otherwise.
     */
    public void setPending(final Boolean pending) {
        this.pending = pending;
    }
}
