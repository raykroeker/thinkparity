/*
 * Created On: Jan 19, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.container.IllegalStateTransitionException;
import com.thinkparity.ophelia.model.document.CannotLockException;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity OpheliaUI Remove Document Action<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.3
 */
public class RemoveDocument extends AbstractBrowserAction {

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
	 * Create RemoveDocument.
	 * 
	 * @param browser
	 *            The browser application.
	 */
	public RemoveDocument(final Browser browser) {
		super(ActionId.CONTAINER_REMOVE_DOCUMENT);
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
        if (browser.confirm("DocumentRemove.ConfirmRemoveMessage",
                new Object[] {document.getName()})) {
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
     * Invoke remove on a container document.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param document
     *            A <code>Document</code>.
     * @throws CannotLockException
     */
    private void invoke(final Container container, final Document document) {
        this.container = container;
        this.document = document;
        try {
            getContainerModel().removeDocument(container.getId(), document.getId());
        } catch (final CannotLockException clx) {
            browser.retry(this, document.getName());
        } catch (final IllegalStateTransitionException istx) {
            throw translateError(istx);
        }
    }

    /**
     * <b>Title:</b>Remove Document Action Data Keys<br>
     * <b>Description:</b>The remove document action input data keys.<br>
     */
    public enum DataKey { CONTAINER_ID, DOCUMENT_ID }
}
