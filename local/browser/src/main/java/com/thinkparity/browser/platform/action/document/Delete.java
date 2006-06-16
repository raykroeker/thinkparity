/*
 * Jan 30, 2006
 */
package com.thinkparity.browser.platform.action.document;

import javax.swing.Icon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.document.Document;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Delete extends AbstractAction {

	private static final Icon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.DOCUMENT_DELETE;
		NAME = "Delete";
	}

	/**
	 * The application browser.
	 * 
	 */
	private final Browser browser;

	/**
	 * Create a Delete.
	 * 
	 * @param browser
	 *            The application browser.
	 */
	public Delete(final Browser browser) {
		super("Document.Delete", ID, NAME, ICON);
		this.browser = browser;
	}

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(Data data) throws Exception {
	    final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
		final Document document = getDocumentModel().get(documentId);

        if(browser.confirm("DocumentDelete.ConfirmDeleteMessage", new Object[] {document.getName()})) {
            getDocumentModel().delete(documentId);
            browser.fireDocumentDeleted(documentId);
        }
	}

	public enum DataKey { DOCUMENT_ID }
}
