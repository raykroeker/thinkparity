/*
 * Jan 10, 2006
 */
package com.thinkparity.browser.ui.action.document;

import java.util.UUID;

import javax.swing.Icon;

import com.thinkparity.browser.ui.action.AbstractAction;
import com.thinkparity.browser.ui.action.ActionId;
import com.thinkparity.browser.ui.action.Data;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Open extends AbstractAction {

	/**
	 * The key used to set\get the data.
	 * 
	 * @see Data
	 */
	public enum DataKey { DOCUMENT_ID }

	/**
	 * The action small ICON.
	 * 
	 */
	private static final Icon ICON;

	/**
	 * The action id.
	 * 
	 */
	private static final ActionId ID;

	/**
	 * The action NAME.
	 * 
	 */
	private static final String NAME;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static {
		ICON = null;
		ID = ActionId.DOCUMENT_OPEN;
		NAME = "Open Document";
	}

	/**
	 * Create an Open.
	 * 
	 */
	public Open() { super("Document.Open", ID, NAME, ICON); }

	/**
	 * @see com.thinkparity.browser.ui.action.AbstractAction#invoke(com.thinkparity.browser.ui.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final UUID id = (UUID) data.get(DataKey.DOCUMENT_ID);
		getDocumentModel().open(id);
	}
}
