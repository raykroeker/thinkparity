/*
 * Jan 10, 2006
 */
package com.thinkparity.browser.ui.action.document;

import java.util.UUID;

import javax.swing.Icon;

import com.thinkparity.browser.ui.action.AbstractAction;
import com.thinkparity.browser.ui.action.Data;

import com.thinkparity.model.parity.ParityException;

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
	 * Create an Open.
	 * 
	 */
	public Open() { super(name, icon); }

	/**
	 * @see com.thinkparity.browser.ui.action.AbstractAction#invoke(com.thinkparity.browser.ui.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
		final UUID id = (UUID) data.get(DataKey.DOCUMENT_ID);
		try { getDocumentModel().open(id); }
		catch(ParityException px) { registerError(px); }
	}
}
