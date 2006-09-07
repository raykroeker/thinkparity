/*
 * Mar 21, 2006
 */
package com.thinkparity.browser.application.browser;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class BrowserSession {

	/**
	 * The currently selected container id and contact id.
	 * 
	 */
    private Long selectedContainerId;
    private JabberId selectedContactId;

	/**
	 * Create a BrowserSession.
	 * 
	 */
	BrowserSession(final Browser browser) { super(); }
    
    /**
     * Obtain the selected container id.
     * 
     * @return The selected container id.
     */
    public Long getSelectedContainerId() { return selectedContainerId; }
    
    /**
     * Obtain the selected contact id.
     * 
     * @return The selected contact id.
     */
    public JabberId getSelectedContactId() { return selectedContactId; }

	/**
	 * Set the selected container id.
	 * 
	 * @param selectedContainerId
	 *            The container id to select.
	 * @return The previously selected container id.
	 */
	public Long setSelectedContainerId(final Long selectedContainerId) {
		final Long prevSelectedContainerId = this.selectedContainerId;
		this.selectedContainerId = selectedContainerId;
		return prevSelectedContainerId;
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
