/*
 * Created On: Thu Jun 01 2006 16:37 PDT
 * $Id$
 */
package com.thinkparity.browser.platform.action.document;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.document.Document;

/**
 * Rename is run when the user rename's an artifact.
 *
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class Rename extends AbstractAction {

	/** The browser application. */
	private final Browser browser;

	/**
	 * Create Rename.
	 * 
     * @param browser
     *      The browser application.
	 */
	public Rename(final Browser browser) {
		super(null, ActionId.DOCUMENT_RENAME, null, null);
		this.browser = browser;
	}

	/** @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data) */
	public void invoke(final Data data) throws Exception {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
		final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
        final String documentName = (String) data.get(DataKey.DOCUMENT_NAME);

        if(null == documentName) {
            final Document document = getDocumentModel().get(documentId);
            browser.displayRenameDocument(containerId, documentId, document.getName());
        }
        else {
            getDocumentModel().rename(documentId, documentName);
            browser.fireDocumentUpdated(containerId,documentId);
        }
	}

	public enum DataKey { CONTAINER_ID, DOCUMENT_ID, DOCUMENT_NAME }
}
