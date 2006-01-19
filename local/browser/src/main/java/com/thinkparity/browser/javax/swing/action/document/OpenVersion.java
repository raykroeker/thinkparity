/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.javax.swing.action.document;

import java.util.UUID;

import javax.swing.Icon;

import com.thinkparity.browser.javax.swing.action.AbstractAction;
import com.thinkparity.browser.javax.swing.action.Data;

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
	 * Create an OpenVersion.
	 */
	public OpenVersion() { super(name, icon); }

	/**
	 * @see com.thinkparity.browser.javax.swing.action.AbstractAction#invoke(com.thinkparity.browser.javax.swing.action.Data)
	 * 
	 */
	public void invoke(Data data) {
		final UUID id = (UUID) data.get(DataKey.DOCUMENT_ID);
		final String versionId = (String) data.get(DataKey.VERSION_ID);
		try { getDocumentModel().openVersion(id, versionId); }
		catch(ParityException px) { registerError(px); }
	}
}
