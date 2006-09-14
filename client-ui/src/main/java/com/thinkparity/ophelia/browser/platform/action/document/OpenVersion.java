/*
 * Jan 16, 2006
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
public class OpenVersion extends AbstractAction {

	/** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

	/** The browser application. */
	private final Browser browser;

	/**
	 * Create an OpenVersion.
	 * 
	 * @param browser The browser application.
	 */
	public OpenVersion(final Browser browser) {
		super(ActionId.DOCUMENT_OPEN_VERSION);
		this.browser = browser;
	}

	/**
	 * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
		final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
		final Long versionId = (Long) data.get(DataKey.VERSION_ID);
		getDocumentModel().openVersion(documentId, versionId);

        // Flag the document as having been seen
        browser.runApplyFlagSeenArtifact(documentId, ArtifactType.DOCUMENT);
	}

	/**
	 * The keys used to get\set the action data.
	 * 
	 * @see Data
	 */
	public enum DataKey { DOCUMENT_ID, VERSION_ID }
}
