/*
 * Created On: Thu May 04 2006 15:56 PDT
 * $Id$
 */
package com.thinkparity.browser.platform.action.document;

import javax.swing.Icon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.xmpp.JabberId;

/**
 * An action used to send a document key to a user.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendKey extends AbstractAction {

	private static final Icon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.DOCUMENT_CLOSE;
		NAME = "Document.SendKey";
	}

	/** The browser application. */
    private final Browser application;

	/**
	 * Create SendKey.
	 *
     * @param application
     *      The browser application.
	 */
	public SendKey(final Browser application) {
        super("Document.SendKey", ID, NAME, ICON);
        this.application = application;
	}

    /**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(Data data) throws Exception {
		final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
		final JabberId userId = (JabberId) data.get(DataKey.USER_ID);

        getArtifactModel().sendKey(documentId, userId);

        application.fireDocumentUpdated(documentId, Boolean.FALSE);
	}

	/** Data keys used by send key. */
	public enum DataKey { DOCUMENT_ID, USER_ID }
}
