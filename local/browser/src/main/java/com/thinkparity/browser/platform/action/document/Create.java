/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.platform.action.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

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
		NAME = "Create Document";
	}

	/**
	 * The browser application.
	 * 
	 */
	private final Browser browser;

	/**
	 * Create a document.
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
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);

        final InputStream inputStream = new FileInputStream(file);
        Document document = null;
        try {
    		document =
    			getDocumentModel().create(containerId, file.getName(), inputStream);
            // Add the document to the container
            getContainerModel().addDocument(containerId, document.getId());
        }
        finally { inputStream.close(); }
        
        getArtifactModel().applyFlagSeen(document.getId());

		browser.fireDocumentCreated(containerId, document.getId(), Boolean.FALSE);
        
        // Test code
        //final List<Document> documents = getContainerModel().readDocuments(containerId);
	}

	public enum DataKey { FILE, CONTAINER_ID }
}
