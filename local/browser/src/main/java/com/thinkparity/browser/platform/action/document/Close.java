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
public class Close extends AbstractAction {

	private static final Icon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.DOCUMENT_CLOSE;
		NAME = "Close Document";
	}

	/** The browser application. */
    private final Browser browser;

	/**
	 * Create a Close.
	 * 
	 */
	public Close(final Browser browser) {
        super("Document.Close", ID, NAME, ICON);
        this.browser = browser;
	}

    /**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(Data data) throws Exception {
		final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
		final Document document = getDocumentModel().get(documentId);
		if(browser.confirm("DocumentClose.ConfirmClosureMessage", new Object[] {document.getName()})) {
			getDocumentModel().close(documentId);
			getArtifactModel().applyFlagSeen(documentId);
		}

        browser.fireDocumentClosed(documentId, Boolean.FALSE);
	}

	/**
	 * Data key used by close.
	 * 
	 */
	public enum DataKey { DOCUMENT_ID }
}
