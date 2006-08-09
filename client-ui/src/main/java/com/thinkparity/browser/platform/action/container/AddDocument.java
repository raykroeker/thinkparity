/*
 * Created On: Jan 19, 2006
 */
package com.thinkparity.browser.platform.action.container;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.container.ContainerModel;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.13
 */
public class AddDocument extends AbstractAction {

	/** The browser application. */
	private final Browser browser;

	/**
	 * Create Document.
	 * 
	 * @param browser
	 *            The browser application.
	 */
	public AddDocument(final Browser browser) {
		super(null, ActionId.CONTAINER_ADD_DOCUMENT, null, null);
		this.browser = browser;
	}

	/**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
	public void invoke(final Data data) throws Exception {
	    final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
		final File[] files = (File[]) data.get(DataKey.FILES);

        final ArtifactModel artifactModel = browser.getArtifactModel();
        final ContainerModel containerModel = browser.getContainerModel();
        final DocumentModel documentModel = browser.getDocumentModel();
        Document document;
        for(final File file : files) {
            final InputStream inputStream = new FileInputStream(file);
            try {
                document = documentModel.create(file.getName(), inputStream);
                containerModel.addDocument(containerId, document.getId());
                artifactModel.applyFlagSeen(document.getId());
                browser.fireDocumentAdded(containerId, document.getId());
            } finally {
                inputStream.close();
            }
        }
	}

	public enum DataKey { CONTAINER_ID, FILES }
}
