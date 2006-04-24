/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.platform.action.document;

import java.io.File;

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
public class Create extends AbstractAction {

	private static final Icon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.DOCUMENT_CREATE;
		NAME = "Create";
	}

	/**
	 * The browser application.
	 * 
	 */
	private final Browser browser;

	/**
	 * Create a Create.
	 * 
	 * @param browser
	 *            The browser application.
	 */
	public Create(final Browser browser) {
		super("Document.Create", ID, NAME, ICON);
		this.browser = browser;
	}

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final File file = (File) data.get(DataKey.FILE);
		final Document document =
			getDocumentModel().create(file.getName(), file.getName(), file);

		browser.fireDocumentCreated(document.getId(), Boolean.FALSE);
	}

	public enum DataKey { FILE }
}
