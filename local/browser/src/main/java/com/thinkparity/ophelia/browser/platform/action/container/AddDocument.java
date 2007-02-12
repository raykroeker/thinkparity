/*
 * Created On: Jan 19, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.util.DocumentUtil;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerModel;

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
            // Ensure the container has a local draft.
            final Container container = getContainerModel().read(containerId);
            Assert.assertTrue(getContainerModel().read(containerId).isLocalDraft(),
                    "Cannot add documents to container {0}, no local draft.", container.getName());

            // Get the list of existing draft documents in this container.
            final List<Document> draftDocuments = readDraftDocuments(containerId);;
            
            // Determine which files are for adding and which are for updating.
            // A file is added to the 'update' list if the draft contains a
            // document of the same name; otherwise, the file is added to the 'add' list.
            final DocumentUtil documentUtil = DocumentUtil.getInstance();
            final List<File> addFileList = new ArrayList<File>();
            final List<File> updateFileList = new ArrayList<File>();
            for (final File file : files) {
                if (documentUtil.contains(draftDocuments, file)) {
                    updateFileList.add(file);
                } else {
                    addFileList.add(file); 
                }
            }
            
            // Add documents
            for (final File file : addFileList) {
                addDocument(file, containerId);
            }
            
            // Update documents
            final ContainerDraft containerDraft = getContainerModel().readDraft(containerId);
            for (final File file : updateFileList) {
                final Document document = draftDocuments.get(documentUtil.indexOf(draftDocuments, file));
                boolean update = true;
                if (ContainerDraft.ArtifactState.ADDED == containerDraft.getState(document)) {
                    update = browser.confirm("ConfirmOverwriteAddedDocument", new Object[] { file.getName() });
                } else if (ContainerDraft.ArtifactState.REMOVED != containerDraft.getState(document) &&
                        getDocumentModel().isDraftModified(document.getId())) {
                    update = browser.confirm("ConfirmOverwriteModifiedDocument", new Object[] { file.getName() });                    
                }

                // Update the document. If necessary, undelete the document first.
                if (update) {
                    if (ContainerDraft.ArtifactState.REMOVED == containerDraft.getState(document)) {
                        getContainerModel().revertDocument(containerId, document.getId());
                    }
                    updateDocument(file, containerId, document.getId());   
                }
            }
        }
	}

    /**
     * Add a document.
     * 
     * @param file
     *            A <code>File</code>.
     * @param containerId
     *            A container id <code>Long</code>.
     */
    private void addDocument(final File file, final Long containerId) {
        try {
            final InputStream inputStream = new FileInputStream(file);
            try {
                final Document document = getDocumentModel().create(file.getName(), inputStream);
                getContainerModel().addDocument(containerId, document.getId());
                getArtifactModel().applyFlagSeen(document.getId());
            } finally {
                inputStream.close();
            }
        }
        catch(final IOException iox) { throw translateError(iox); }
    }

    /**
     * Read the draft documents.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return A list of <code>Document</code>.
     */
    private List<Document> readDraftDocuments(final Long containerId) {
        final ContainerModel containerModel = getContainerModel();
        final Container container = containerModel.read(containerId);
        if (container.isLocalDraft()) {
            return containerModel.readDraft(containerId).getDocuments();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Update a document.
     * 
     * @param file
     *            A <code>File</code>.
     * @param containerId
     *            A container id <code>Long</code>.
     * @param documentId
     *            A document id <code>Long</code>.
     */
    private void updateDocument(final File file, final Long containerId, final Long documentId) {
        try {
            final InputStream inputStream = new FileInputStream(file);
            try {
                getDocumentModel().updateDraft(documentId, inputStream);
                browser.fireDocumentDraftUpdated(documentId);
            } finally {
                inputStream.close();
            }
        }
        catch(final IOException iox) { throw translateError(iox); }
    }

	public enum DataKey { CONTAINER_ID, FILES }
}
