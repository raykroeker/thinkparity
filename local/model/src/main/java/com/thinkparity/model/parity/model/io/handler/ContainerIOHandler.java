/*
 * Created On: Jun 28, 2006 8:44:55 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.io.handler;

import java.util.List;

import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.model.container.ContainerDraft;
import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public interface ContainerIOHandler {

    /**
     * Add an artifact version to a container version.
     * 
     * @param containerId
     *            The container id.
     * @param containerVersionId
     *            The container version id.
     * @param artifactId
     *            The artifact id.
     * @param artifactVersionId
     *            The artifact version id.
     * @param artifactType
     *            The artifact type.
     */
    public void addVersion(final Long containerId,
            final Long containerVersionId, final Long artifactId,
            final Long artifactVersionId, final ArtifactType artifactType);

    /**
     * Create a container.
     * 
     * @param container
     *            The container.
     */
    public void create(final Container container);

    /**
     * Create a draft.
     * 
     * @param draft
     *            A draft.
     */
    public void createDraft(final ContainerDraft draft);

    /**
     * Create a container version.
     * 
     * @param version
     *            The container version.
     */
    public void createVersion(final ContainerVersion version);

    /**
     * Delete a container.
     * 
     * @param containerId
     *            The container id.
     */
    public void delete(final Long containerId);

    /**
     * Delete a container version.
     * 
     * @param containerId
     *            The container id.
     * @param versionId
     *            The version id.
     */
    public void deleteVersion(final Long containerId, final Long versionId);

    /**
     * Read a list of containers.
     * 
     * @return A list of containers.
     */
    public List<Container> read();

    /**
     * Read a container.
     * 
     * @param containerId
     *            A container id.
     * @return A container.
     */
    public Container read(final Long containerId);

    /**
     * Read the documents attached to a container version.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @return A list of documents.
     */
    public List<Document> readDocuments(final Long containerId,
            final Long versionId);

    /**
     * Read the document versions for a container version.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A container version id.
     * @return A list of document versions.
     */
    public List<DocumentVersion> readDocumentVersions(final Long containerId,
            final Long versionId);

    /**
     * Read the latest container version.
     * 
     * @param containerId
     *            A container id.
     * @return A container version.
     */
    public ContainerVersion readLatestVersion(final Long containerId);

    /**
     * Read a container version.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @return A container version.
     */
    public ContainerVersion readVersion(final Long containerId,
            final Long versionId);

    /**
     * Read a list of container versions.
     * 
     * @param containerId
     *            A container id.
     * @return A list of container versions.
     */
    public List<ContainerVersion> readVersions(final Long containerId);

    /**
     * Remove an artifact version from a container version.
     * 
     * @param containerId
     *            The container id.
     * @param containerVersionId
     *            The container version id.
     * @param artifactId
     *            The artifact id.
     * @param artifactVersionId
     *            The artifact version id.
     */
    public void removeVersion(final Long containerId,
            final Long containerVersionId, final Long artifactId,
            final Long artifactVersionId);

    /**
     * Remove all artifact versions.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     */
    public void removeVersions(final Long containerId, final Long versionId);
}
