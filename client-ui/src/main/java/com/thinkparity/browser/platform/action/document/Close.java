/*
 * Jan 30, 2006
 */
package com.thinkparity.browser.platform.action.document;

import javax.swing.Icon;

import com.thinkparity.browser.javax.swing.JOptionPaneUtil;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.document.Document;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Close extends AbstractAction {

	/**
	 * Data key used by close.
	 * 
	 */
	public enum DataKey { DOCUMENT_ID }

	private static final Icon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.DOCUMENT_CLOSE;
		NAME = "Close Document";
	}

	/**
	 * Create a Close.
	 * 
	 */
	public Close() { super("Document.Close", ID, NAME, ICON); }

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(Data data) throws Exception {
		final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
		final Document document = getDocumentModel().get(documentId);
		if(JOptionPaneUtil.showConfirmationDialog(
				getString("ConfirmClosureMessage", new String[] {document.getName()}),
				getString("ConfirmClosureTitle"))) {
			getDocumentModel().close(documentId);
			getArtifactModel().applyFlagSeen(documentId);
		}
	}
}
