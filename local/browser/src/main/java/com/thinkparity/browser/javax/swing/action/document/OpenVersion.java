/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.javax.swing.action.document;

import java.util.UUID;

import javax.swing.Icon;

import com.thinkparity.browser.javax.swing.action.BrowserAction;
import com.thinkparity.browser.javax.swing.action.Data;
import com.thinkparity.browser.model.ModelProvider;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class OpenVersion extends BrowserAction {

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
	public static final Icon icon;

	/**
	 * The action name.
	 * 
	 */
	public static final String name;

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	static {
		name = "Open Document Version";
		icon = null;
	}

	/**
	 * Document model api.
	 * 
	 */
	protected final DocumentModel documentModel =
		ModelProvider.getDocumentModel(getClass());

	/**
	 * Create an OpenVersion.
	 */
	public OpenVersion() { super(name, icon); }

	/**
	 * @see com.thinkparity.browser.javax.swing.action.BrowserAction#invoke(com.thinkparity.browser.javax.swing.action.Data)
	 * 
	 */
	public void invoke(Data data) {
		final UUID id = (UUID) data.get(DataKey.DOCUMENT_ID);
		final String versionId = (String) data.get(DataKey.VERSION_ID);
		try { documentModel.openVersion(id, versionId); }
		catch(ParityException px) { registerError(px); }
	}
}
