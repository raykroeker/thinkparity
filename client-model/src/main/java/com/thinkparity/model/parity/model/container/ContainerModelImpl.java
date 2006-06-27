/*
 * Generated On: Jun 27 06 12:13:12 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.filter.ArtifactFilterManager;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.filter.container.DefaultFilter;
import com.thinkparity.model.parity.model.sort.ComparatorBuilder;
import com.thinkparity.model.parity.model.sort.ModelSorter;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Container Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version $Revision$
 */
class ContainerModelImpl extends AbstractModelImpl {

    /**
     * Obtain an apache api log id.
     * @param api The api.
     * @return The log id.
     */
    private static StringBuffer getApiId(final String api) {
        return getModelId("[CONTAINER]").append(" ").append(api);
    }

    /** A default container comparator. */
    private final Comparator<Artifact> defaultComparator;

    /** A default container filter. */
    private final Filter<? super Artifact> defaultFilter;

    /**
     * Create ContainerModelImpl.
     *
     * @param workspace
     *		The thinkParity workspace.
     */
    ContainerModelImpl(final Workspace workspace) {
        super(workspace);
        this.defaultComparator = new ComparatorBuilder().createByName(Boolean.TRUE);
        this.defaultFilter = new DefaultFilter();
    }

    /**
     * Add a document to a container.
     * 
     * @param containerId
     *            A container id.
     * @param documentId
     *            A document id.
     */
    void addDocument(final Long containerId, final Long documentId) {}

    /**
     * Create a container.
     * 
     * @param name
     *            The container name.
     * @return The new container.
     */
    Container create(final String name) {
        logger.info(getApiId("[CREATE]"));
        logger.warn(getApiId("[CREATE] [NOT YET IMPLEMENTED]"));
        final Container container = new Container();
        return container;
    }

    /**
     * Delete a container.
     * 
     * @param containerId
     *            A container id.
     */
    void delete(final Long containerId) {}

    /**
     * Read the containers.
     * 
     * @return A list of containers.
     */
    List<Container> read() {
        logger.info(getApiId("[READ]"));
        logger.warn(getApiId("[READ] [NOT YET IMPLEMENTED]"));
        return read(defaultComparator);
    }

    /**
     * Read a container.
     * 
     * @param containerId
     *            A container id.
     * @return A container.
     */
    Container read(final Long containerId) {
        logger.info(getApiId("[READ]"));
        logger.debug(containerId);
        logger.warn(getApiId("[READ] [NOT YET IMPLEMENTED]"));
        final Container container = new Container();
        container.setName("Fake Container 0");
        return container;
    }

    /**
     * Read the containers.
     * 
     * @param comparator
     *            A sort ordering to user.
     * @return A list of containers.
     */
    List<Container> read(final Comparator<Artifact> comparator) {
        logger.info(getApiId("[READ]"));
        logger.debug(comparator);
        logger.warn(getApiId("[READ] [NOT YET IMPLEMENTED]"));
        return read(comparator, defaultFilter);
    }

    /**
     * Read the containers.
     * 
     * @param comparator
     *            A sort ordering to user.
     * @param filter
     *            A filter to apply.
     * @return A list of containers.
     */
    List<Container> read(final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        logger.info(getApiId("[READ]"));
        logger.debug(comparator);
        logger.debug(filter);
        logger.warn(getApiId("[READ] [NOT YET IMPLEMENTED]"));
        final List<Container> containers = new LinkedList<Container>();
        Container container;

        container = new Container();
        container.setName("Fake Package 0");
        containers.add(container);

        container = new Container();
        container.setName("Fake Package 1");
        containers.add(container);
        
        container = new Container();
        container.setName("Fake Package 2");
        containers.add(container);

        container = new Container();
        container.setName("Fake Package 3");
        containers.add(container);
        
        container = new Container();
        container.setName("Fake Package 4");
        containers.add(container);

        ArtifactFilterManager.filter(containers, filter);
        ModelSorter.sortContainers(containers, comparator);
        return containers;        
    }

    /**
     * Read the containers.
     * 
     * @param filter
     *            A filter to apply.
     * @return A list of containers.
     */
    List<Container> read(final Filter<? super Artifact> filter) {
        logger.info(getApiId("[READ]"));
        logger.debug(filter);
        return read(defaultComparator, filter);
    }

    /**
     * Read the documents for the container.
     * 
     * @param containerId
     *            A container id.
     * @return A list of documents.
     * @throws ParityException
     */
    List<Document> readDocuments(final Long containerId) throws ParityException {
        logger.info(getApiId("[READ DOCUMENTS]"));
        logger.info(getApiId("[READ DOCUMENTS]"));
        logger.debug(containerId);
        logger.warn(getApiId("[CREATE] [NOT YET IMPLEMENTED]"));
        return readDocuments(containerId, null, null);
    }

    /**
     * Read the documents for the container.
     * 
     * @param containerId
     *            A container id.
     * @param comparator
     *            A document comparator.
     * @return A list of documents.
     * @throws ParityException
     */
    List<Document> readDocuments(final Long containerId,
            final Comparator<Artifact> comparator) throws ParityException {
        logger.info(getApiId("[READ DOCUMENTS]"));
        return readDocuments(containerId, null, null);
    }

    /**
     * Read the documents for the container.
     * 
     * @param containerId
     *            A container id.
     * @param comparator
     *            A document comparator.
     * @param filter
     *            A document filter.
     * @return A list of documents.
     * @throws ParityException
     */
    List<Document> readDocuments(final Long containerId,
            final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) throws ParityException {
        logger.info(getApiId("[READ DOCUMENTS]"));
        logger.debug(containerId);
        logger.debug(comparator);
        logger.debug(filter);
        logger.warn(getApiId("[CREATE] [NOT YET IMPLEMENTED]"));
        final Collection<Document> documents = getInternalDocumentModel().list(comparator, filter);
        final List<Document> documentsList = new LinkedList<Document>();
        documentsList.addAll(documents);
        for(int i = 0; i < documentsList.size() && i > 2; i++) {
            documentsList.remove(i);
        }
        return documentsList;
    }

    /**
     * Read the documents for the container.
     * 
     * @param containerId
     *            A container id.
     * @param filter
     *            A document filter.
     * @return A list of documents.
     * @throws ParityException
     */
    List<Document> readDocuments(final Long containerId,
            final Filter<? super Artifact> filter) throws ParityException {
        logger.info(getApiId("[READ DOCUMENTS]"));
        logger.info(getApiId("[READ DOCUMENTS]"));
        logger.debug(containerId);
        logger.debug(filter);
        logger.warn(getApiId("[CREATE] [NOT YET IMPLEMENTED]"));
        return readDocuments(containerId, null, filter);
    }

    /**
     * Remove a document from a container.
     * 
     * @param containerId
     *            A container id.
     * @param documentId
     *            A document id.
     */
    void removeDocument(final Long containerId, final Long documentId) {}
}
