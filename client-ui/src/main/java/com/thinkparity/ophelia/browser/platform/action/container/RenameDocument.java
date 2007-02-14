/*
 * Created On: Thu Jun 01 2006 16:37 PDT
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;


import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.document.CannotLockException;

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
     * The <code>Document</code>. Used by invoke and reinvoke to maintain the
     * input data.
     */
    private Document document;

	/**
     * The new document name <code>String</code>. Used by invoke and reinvoke
     * to maintain the input data.
     */
    private String renameTo;

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

	/**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
	public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
		final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
        final String documentName = (String) data.get(DataKey.DOCUMENT_NAME);
        final Document document = getDocumentModel().get(documentId);

        if (null == documentName) {
            browser.displayRenameDocumentDialog(containerId, documentId, document.getName());
        } else {
            invoke(document, documentName);
        }
	}

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#retryInvokeAction()
     *
     */
    @Override
    public void retryInvokeAction() {
        invoke(document, renameTo);
    }

    /**
     * Invoke rename document.
     * 
     * @param document
     *            A <code>Document</code>.
     * @param renameTo
     *            A new name <code>String</code>.
     */
    private void invoke(final Document document, final String renameTo) {
        this.document = document;
        this.renameTo = renameTo;
        try {
            getDocumentModel().rename(document.getId(), renameTo);
            browser.fireDocumentUpdated(document.getId(), Boolean.FALSE);
        } catch (final CannotLockException clx) {
            browser.retry(this, document.getName(), renameTo);
        }
    }

    /**
     * <b>Title:</b>Rename Document Action Data Keys<br>
     * <b>Description:</b><br>
     */
	public enum DataKey { CONTAINER_ID, DOCUMENT_ID, DOCUMENT_NAME }
}
