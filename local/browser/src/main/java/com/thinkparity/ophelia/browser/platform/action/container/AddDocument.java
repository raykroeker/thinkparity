/*
 * Created On: Jan 19, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;



import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.Document;
import com.thinkparity.ophelia.model.document.DocumentModel;

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
		super(ActionId.CONTAINER_ADD_DOCUMENT);
		this.browser = browser;
	}

	/**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
	public void invoke(final Data data) {
	    final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
		final File[] files = (File[]) data.get(DataKey.FILES);

        if(0 == files.length) {
            // prompt for files
            browser.runAddContainerDocuments(containerId);
        } else {
            final ArtifactModel artifactModel = browser.getArtifactModel();
            final ContainerModel containerModel = browser.getContainerModel();
            final DocumentModel documentModel = browser.getDocumentModel();
            Document document;
            for(final File file : files) {
                try {
                    final InputStream inputStream = new FileInputStream(file);
                    try {
                        document = documentModel.create(file.getName(), inputStream);
                        containerModel.addDocument(containerId, document.getId());
                        artifactModel.applyFlagSeen(document.getId());
                    } finally {
                        inputStream.close();
                    }
                }
                catch(final IOException iox) { throw translateError(iox); }
            }
        }
	}

	public enum DataKey { CONTAINER_ID, FILES }
}
