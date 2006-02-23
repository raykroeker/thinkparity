/*
 * Feb 22, 2006
 */
package com.thinkparity.model.parity.model.document.history;

import java.util.Calendar;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class HistoryItem {

	private Calendar date;

	private Long documentId;

	private HistoryItemEvent event;

	private String name;

	private Long versionId;

	/**
	 * Create a HistoryItem.
	 * 
	 */
	protected HistoryItem() { super(); }

	/**
	 * @return Returns the date.
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @return Returns the documentId.
	 */
	public Long getDocumentId() {
		return documentId;
	}

	/**
	 * @return Returns the event.
	 */
	public HistoryItemEvent getEvent() {
		return event;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the versionId.
	 */
	public Long getVersionId() {
		return versionId;
	}

	public Boolean isSetVersionId() { return null != versionId; }

	/**
	 * @param date The date to set.
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * @param documentId The documentId to set.
	 */
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	/**
	 * @param event The event to set.
	 */
	public void setEvent(HistoryItemEvent event) {
		this.event = event;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param versionId The versionId to set.
	 */
	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}
}
