/*
 * Created On: Jun 15, 2006 4:05:23 PM
 * $Id$
 */
package com.thinkparity.browser.platform.action.document;

import javax.swing.ImageIcon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.document.Document;

/**
 * Reactivate is run when the user reactivates artifact.
 *
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class Reactivate extends AbstractAction {

	private static final ImageIcon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.ARTIFACT_ACCEPT_KEY_REQUEST;
		NAME = "Reactivate";
	}

	/** The browser application. */
	private final Browser browser;

	/**
	 * Create Reactivate.
	 * 
     * @param browser
     *      The browser application.
	 */
	public Reactivate(final Browser browser) {
		super(NAME, ID, NAME, ICON);
		this.browser = browser;
	}

	/** @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data) */
	public void invoke(final Data data) throws Exception {
		final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
        final Document document = getDocumentModel().get(documentId);

        if(browser.confirm("DocumentReactivate.ConfirmReactivateMessage", new Object[] {document.getName()})) {
            getDocumentModel().reactivate(documentId);
            browser.fireDocumentUpdated(documentId);
        }
	}

	public enum DataKey { DOCUMENT_ID }
}
