/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.ui.action.document;

import java.io.File;

import javax.swing.Icon;

import com.thinkparity.browser.ui.action.AbstractAction;
import com.thinkparity.browser.ui.action.ActionId;
import com.thinkparity.browser.ui.action.Data;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Create extends AbstractAction {

	public enum DataKey { FILE }

	private static final Icon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.DOCUMENT_CREATE;
		NAME = "Create";
	}

	/**
	 * Create a Create.
	 * 
	 */
	public Create() { super(ID, NAME, ICON); }

	/**
	 * @see com.thinkparity.browser.ui.action.AbstractAction#invoke(com.thinkparity.browser.ui.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final File file = (File) data.get(DataKey.FILE);
		final String name = file.getName();
		final String description = name;
    	getDocumentModel().create(getProjectId(), name, description, file);
	}
}
