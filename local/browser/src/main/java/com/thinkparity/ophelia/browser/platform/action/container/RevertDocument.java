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
 * Rename is run when the user rename's an artifact.
 *
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class RevertDocument extends AbstractAction {
    
    /** The thinkParity browser application. */
    private final Browser browser;

	/**
	 * Create RevertDocument.
	 * 
     * @param browser
     *      The browser application.
	 */
	public RevertDocument(final Browser browser) {
		super(ActionId.CONTAINER_REVERT_DOCUMENT);
        this.browser = browser;
	}

	/** @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data) */
	public void invoke(final Data data) {
	    final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
        final Document document = getDocumentModel().get(documentId);
        if (browser.confirm("DocumentRevert.ConfirmRevertMessage",
                new Object[] { document.getName() })) {
            getContainerModel().revertDocument(containerId, documentId);
        }
	}

	public enum DataKey { CONTAINER_ID, DOCUMENT_ID }
}
