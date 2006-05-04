/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.platform.action.document;

import java.io.File;
import java.util.List;

import javax.swing.Icon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CreateDocuments extends AbstractAction {

	private static final Icon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.CREATE_DOCUMENTS;
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
	public CreateDocuments(final Browser browser) {
		super("CreateDocuments", ID, NAME, ICON);
		this.browser = browser;
	}

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final List<File> files = (List<File>) data.get(DataKey.FILES);

        final Create create = new Create(browser);
        final Data createData = new Data(1);
        for(final File file : files) {
            createData.set(Create.DataKey.FILE, file);
            create.invoke(createData);
        }
	}

	public enum DataKey { FILES }
}
