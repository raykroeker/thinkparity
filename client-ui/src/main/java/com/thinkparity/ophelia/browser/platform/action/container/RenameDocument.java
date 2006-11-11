/*
 * Created On: Thu Jun 01 2006 16:37 PDT
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;


import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * RenameDocument is run when the user rename's an artifact.
 *
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class RenameDocument extends AbstractAction {

	/** The browser application. */
	private final Browser browser;

	/**
	 * Create RenameDocument.
	 * 
     * @param browser
     *      The browser application.
	 */
	public RenameDocument(final Browser browser) {
		super(ActionId.CONTAINER_RENAME_DOCUMENT);
		this.browser = browser;
	}

	/** @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data) */
	public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
		final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
        final String documentName = (String) data.get(DataKey.DOCUMENT_NAME);

        if(null == documentName) {
            final Document document = getDocumentModel().get(documentId);
            browser.displayRenameDocumentDialog(containerId, documentId, document.getName());
        }
        else {
            getDocumentModel().rename(documentId, documentName);
            browser.fireDocumentUpdated(documentId, Boolean.FALSE);
        }
	}

	public enum DataKey { CONTAINER_ID, DOCUMENT_ID, DOCUMENT_NAME }
}
