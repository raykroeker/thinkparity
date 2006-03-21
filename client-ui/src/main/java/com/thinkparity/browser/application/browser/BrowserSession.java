/*
 * Mar 21, 2006
 */
package com.thinkparity.browser.application.browser;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class BrowserSession {

	/**
	 * The currently selected document id.
	 * 
	 */
	private Long selectedDocumentId;

	/**
	 * Create a BrowserSession.
	 * 
	 */
	BrowserSession(final Browser browser) { super(); }

	/**
	 * Obtain the selected document id.
	 * 
	 * @return The selected document id.
	 */
	public Long getSelectedDocumentId() { return selectedDocumentId; }

	/**
	 * Set the selected document id.
	 * 
	 * @param selectedDocumentId
	 *            The document id to select.
	 * @return The previously selected document id.
	 */
	public Long setSelectedDocumentId(final Long selectedDocumentId) {
		final Long prevSelectedDocumentId = this.selectedDocumentId;
		this.selectedDocumentId = selectedDocumentId;
		return prevSelectedDocumentId;
	}
}
