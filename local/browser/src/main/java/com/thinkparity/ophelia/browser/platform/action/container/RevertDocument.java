/*
 * Created On: Thu Jun 01 2006 16:37 PDT
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;


import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.model.document.CannotLockException;

/**
 * Rename is run when the user rename's an artifact.
 *
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class RevertDocument extends AbstractBrowserAction {
    
    /** The thinkParity browser application. */
    private final Browser browser;

	/**
     * A <code>Container</code>. Used by the invoke and retry to maintain the
     * previous container.
     */
    private Container container;

	/**
     * A <code>Document</code>. Used by the invoke and retry to maintain the
     * previous container.
     */
    private Document document;

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

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
	public void invoke(final Data data) {
	    final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
        final Container container = getContainerModel().read(containerId);
        final Document document = getDocumentModel().get(documentId);
        if (browser.confirm("DocumentRevert.ConfirmRevertMessage",
                new Object[] { document.getName() })) {
            invoke(container, document);
        }
	}

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#retryInvokeAction()
     *
     */
    @Override
    public void retryInvokeAction() {
        invoke(container, document);
    }

	/**
     * Invoke revoke document on a container.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param document
     *            A <code>Document</code>.
     */
    private void invoke(final Container container, final Document document) {
        this.container = container;
        this.document = document;
        try {
            getContainerModel().revertDocument(container.getId(), document.getId());
        } catch (final CannotLockException clx) {
            browser.retry(this, document.getName());
        }
    }

    public enum DataKey { CONTAINER_ID, DOCUMENT_ID }
}
