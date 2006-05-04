/*
 * Feb 22, 2006
 */
package com.thinkparity.model.parity.model.document.history;

import java.util.Calendar;

/**
 * The history item is a read-only localized version of the user's local audit
 * events.  The history item includes the date of the event; and text describing
 * the nature of the event.
 * 
 * The history item also includes links to the document and potentially a
 * specific version of that document.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HistoryItem {

	/** The event date and time. */
	private Calendar date;

	/** The document id. */
	private Long documentId;

	/** The event text. */
	private String event;

	/** The document version id. */
	private Long versionId;

    /** Flag indicating the pending state of this event. */
    private Boolean pending;

    /** The history item id. */
    private Long id;

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
	 * Obtain the history item document id.
	 * 
	 * @return The history item document id.
	 */
	public Long getDocumentId() { return documentId; }

    /**
     * Obtain the history item id.
     *
     * @return The history item id.
     */
    public Long getId() { return id; }

	/**
	 * Obtain the history item's version id.
	 * 
	 * @return The history item's version id.
	 */
	public Long getVersionId() { return versionId; }

    /**
     * Determine whether or not this event is pending.
     *
     * @return True if pending; false otherwise.
     */
    public Boolean isPending() { return pending; }

	/**
	 * Determine whether or not the version id has been set.
	 * 
	 * @return True if the version id is set; false otherwise.
	 */
	public Boolean isSetVersionId() { return null != versionId; }

	/**
	 * Set the history item's event date.
	 * 
	 * @param date
	 * 	The event date.
	 */
	public void setDate(final Calendar date) { this.date = date; }

    /**
     * Set the history item id.
     *
     * @param id
     *      The history item id.
     */
    public void setId(final Long id) { this.id = id; }

	/**
	 * Set the history item's document id.
	 * 
	 * @param documentId
	 * 	The history item's document id.
	 */
	public void setDocumentId(final Long documentId) {
		this.documentId = documentId;
	}

	/**
	 * Set the history item's version id.
	 * 
	 * @param versionId
	 * 	The history item's version id.
	 */
	public void setVersionId(final Long versionId) {
		this.versionId = versionId;
	}

	/**
	 * Set the history item's event text.
	 * 
	 * @param event
	 * 	The history item's event text.
	 */
	public void setEvent(final String event) {
		this.event = event;
	}

    /**
     * Set the pending state.
     *
     * @param pending
     *      True if the item is pending; false otherwise.
     */
    public void setPending(final Boolean pending) {
        this.pending = pending;
    }
}
