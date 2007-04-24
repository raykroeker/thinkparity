/*
 * Mar 6, 2006
 */
package com.thinkparity.ophelia.model.index;

import com.thinkparity.codebase.Pair;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.index.container.ContainerIndexImpl;
import com.thinkparity.ophelia.model.index.container.ContainerVersionIndexImpl;
import com.thinkparity.ophelia.model.index.document.DocumentIndexEntry;
import com.thinkparity.ophelia.model.index.document.DocumentIndexImpl;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Index Model<br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * 
 * TODO Include the index within the transaction.
 */
public final class IndexModelImpl extends Model implements
        IndexModel, InternalIndexModel {

    /** A container index implementation. */
    private IndexImpl<Container, Long> containerIndex;

    /** A container version index implementation. */
    private IndexImpl<ContainerVersion, Pair<Long, Long>> containerVersionIndex;

    /** A document index implementation. */
    private IndexImpl<DocumentIndexEntry, Long> documentIndex;

    /**
	 * Create a IndexModelImpl.
	 * 
	 */
	public IndexModelImpl() {
		super();
	}

    /**
     * Delete a container from the index.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void deleteContainer(final Long containerId) {
        try {
            containerIndex.delete(containerId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Delete a container from the index.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void deleteContainerVersion(final Long containerId,
            final Long versionId) {
        try {
            final Pair<Long, Long> containerVersionId = new Pair<Long, Long>(containerId, versionId);
            containerVersionIndex.delete(containerVersionId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Delete a document from the index.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     */
    public void deleteDocument(final Long documentId) {
        try {
            documentIndex.delete(documentId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Create an index entry for the container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void indexContainer(final Long containerId) {
        try {
            containerIndex.delete(containerId);
            final Container container = getContainerModel().read(containerId);
            containerIndex.index(container);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Create an index entry for the container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void indexContainerVersion(final Pair<Long, Long> containerVersionId) {
        try {
            containerVersionIndex.delete(containerVersionId);
            final ContainerVersion version = getContainerModel().readVersion(
                    containerVersionId.getOne(), containerVersionId.getTwo());
            containerVersionIndex.index(version);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Create an index entry for a document.
     * 
     * @param containerId A container id ,code>Long</code>.
     * @param documentId
     *            A document id <code>Long</code>.
     */
	public void indexDocument(final Long containerId, final Long documentId) {
        try {
            documentIndex.delete(documentId);
            final Document document = getDocumentModel().get(documentId);
            final DocumentIndexEntry entry = new DocumentIndexEntry(containerId, document);
            documentIndex.index(entry);
        } catch (final Throwable t) {
            throw panic(t);
        }
	}

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment, com.thinkparity.ophelia.model.workspace.Workspace)
     *
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        this.containerIndex = new ContainerIndexImpl(workspace, modelFactory);
        this.containerVersionIndex = new ContainerVersionIndexImpl(workspace, modelFactory);
        this.documentIndex = new DocumentIndexImpl(workspace, modelFactory);
    }
}
