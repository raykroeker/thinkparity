/*
 * Mar 21, 2006
 */
package com.thinkparity.browser.application.browser;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class BrowserSession {

	/**
	 * The currently selected document id and contact id.
	 * 
	 */
	private Long selectedDocumentId;
    private JabberId selectedContactId;

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
     * Obtain the selected contact id.
     * 
     * @return The selected contact id.
     */
    public JabberId getSelectedContactId() { return selectedContactId; }

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
    
    /**
     * Set the selected contact id.
     * 
     * @param selectedContactId
     *            The contact id to select.
     * @return The previously selected contact id.
     */
    public JabberId setSelectedContactId(final JabberId selectedContactId) {
        final JabberId prevSelectedContactId = this.selectedContactId;
        this.selectedContactId = selectedContactId;
        return prevSelectedContactId;
    }
}
