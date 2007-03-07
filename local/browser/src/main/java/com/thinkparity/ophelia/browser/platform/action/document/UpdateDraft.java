/*
 * Created On: Jan 19, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.model.document.CannotLockException;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.13
 */
public class UpdateDraft extends AbstractBrowserAction {

	/** The browser application. */
	private final Browser browser;

	private Document document;

	private File file;

    /**
	 * Create Document.
	 * 
	 * @param browser
	 *            The browser application.
	 */
	public UpdateDraft(final Browser browser) {
		super(ActionId.DOCUMENT_UPDATE_DRAFT);
		this.browser = browser;
	}

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
	public void invoke(final Data data) {
	    final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
		final File file = (File) data.get(DataKey.FILE);
		final Document document = getDocumentModel().get(documentId);
        invoke(document, file);
	}

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#retryInvokeAction()
     *
     */
    @Override
    public void retryInvokeAction() {
        invoke(document, file);
    }

    /**
     * Update a document.
     * 
     * @param file
     *            A <code>File</code>.
     * @param containerId
     *            A container id <code>Long</code>.
     * @param documentId
     *            A document id <code>Long</code>.
     */
    private void invoke(final Document document, final File file) {
        this.document = document;
        this.file = file;
        try {
            final InputStream inputStream = new FileInputStream(file);
            try {
                getDocumentModel().updateDraft(document.getId(), inputStream);
                browser.fireDocumentDraftUpdated(document.getId());
            } catch (final CannotLockException clx) {
                browser.retry(this, document.getName());
            } finally {
                inputStream.close();
            }
        } catch (final IOException iox) {
            throw translateError(iox);
        }
    }

	public enum DataKey { DOCUMENT_ID, FILE }
}
