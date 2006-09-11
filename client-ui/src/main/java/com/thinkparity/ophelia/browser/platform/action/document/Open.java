/*
 * Jan 10, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.document;

import com.thinkparity.codebase.model.artifact.ArtifactType;


import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Open extends AbstractAction {

	/** @see java.io.Serializable */
    private static final long serialVersionUID = 1;
    
    /** The browser application. */
    private final Browser browser;

	/**
     * Create Open.
     * 
     * @param browser
     *            The browser application.
     */
	public Open(final Browser browser) {
		super(ActionId.DOCUMENT_OPEN);
        this.browser = browser;
	}

	/**
	 * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
		final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
		getDocumentModel().open(documentId);
        
        // Flag the document as having been seen
        browser.runApplyFlagSeenArtifact(documentId, ArtifactType.DOCUMENT);
	}

	/** Data keys. */
	public enum DataKey { DOCUMENT_ID }
}
