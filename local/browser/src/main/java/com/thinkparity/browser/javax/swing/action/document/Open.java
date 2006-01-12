/*
 * Jan 10, 2006
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
public class Open extends BrowserAction {

	/**
	 * The key used to set\get the data.
	 * 
	 * @see Data
	 */
	public enum DataKey { DOCUMENT_ID }

	/**
	 * The action name.
	 * 
	 */
	public static final String name;

	/**
	 * The action small icon.
	 * 
	 */
	private static final Icon icon;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static {
		name = "Open Document";
		icon = null;
	}

	/**
	 * Document model api.
	 * 
	 */
	protected final DocumentModel documentModel =
		ModelProvider.getDocumentModel(getClass());

	/**
	 * Create an Open.
	 * 
	 */
	public Open() { super(name, icon); }

	/**
	 * @see com.thinkparity.browser.javax.swing.action.BrowserAction#invoke(com.thinkparity.browser.javax.swing.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
		final UUID id = (UUID) data.get(DataKey.DOCUMENT_ID);
		try { documentModel.open(id); }
		catch(ParityException px) { registerError(px); }
	}
}
