/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.ui.action.document;

import java.util.UUID;

import javax.swing.Icon;

import com.thinkparity.browser.ui.action.AbstractAction;
import com.thinkparity.browser.ui.action.ActionId;
import com.thinkparity.browser.ui.action.Data;

import com.thinkparity.model.parity.ParityException;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class OpenVersion extends AbstractAction {

	/**
	 * The keys used to get\set the action data.
	 * 
	 * @see Data
	 */
	public enum DataKey { DOCUMENT_ID, VERSION_ID }

	/**
	 * The action icon.
	 * 
	 */
	private static final Icon ICON;

	/**
	 * The action id.
	 * 
	 */
	private static final ActionId ID;

	/**
	 * The action name.
	 * 
	 */
	private static final String NAME;

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	static {
		ICON = null;
		ID = ActionId.DOCUMENT_OPEN_VERSION;
		NAME = "Open Document Version";
	}

	/**
	 * Create an OpenVersion.
	 */
	public OpenVersion() { super(ID, NAME, ICON); }

	/**
	 * @see com.thinkparity.browser.ui.action.AbstractAction#invoke(com.thinkparity.browser.ui.action.Data)
	 * 
	 */
	public void invoke(Data data) {
		final UUID id = (UUID) data.get(DataKey.DOCUMENT_ID);
		final String versionId = (String) data.get(DataKey.VERSION_ID);
		try { getDocumentModel().openVersion(id, versionId); }
		catch(ParityException px) { registerError(px); }
	}
}
