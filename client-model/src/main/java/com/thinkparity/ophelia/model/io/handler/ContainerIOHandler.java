/*
 * Created On: Jun 28, 2006 8:44:55 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.io.handler;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionDelta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;

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
     * Create a container version delta.
     * 
     * @param versionDelta
     *            A <code>ContainerVersionDelta</code>.
     */
    public void createDelta(final ContainerVersionDelta versionDelta);

    /**
     * Create a draft.
     * 
     * @param draft
     *            A draft.
     */
    public void createDraft(final ContainerDraft draft);

    /**
     * Create a draft artifact relationship.
     * 
     * @param containerId
     *            A container id.
     * @param artifactId
     *            An artifact id.
     * @param state
     *            The state of the artifact within the draft.
     */
    public void createDraftArtifactRel(final Long containerId,
            final Long artifactId, final ContainerDraft.ArtifactState state);

    /**
     * Create a published to list for a container version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param publishedTo
     *            A <code>User</code> <code>List</code>.
     */
    public void createPublishedTo(final Long containerId, final Long versionId,
            final List<User> publishedTo);

    /**
     * Create a published to entry for a container version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param publishedTo
     *            A <code>User</code>.
     */
    public void createPublishedTo(final Long containerId, final Long versionId,
            final User publishedTo);

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
     * Delete a delta between two versions.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param compareVersionId
     *            A container version id <code>Long</code>.
     * @param compareToVersionId
     *            A container version id <code>Long</code>.
     */
    public void deleteDelta(final Long containerId,
            final Long compareVersionId, final Long compareToVersionId);

    /**
     * Delete all deltas for a version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     */
    public void deleteDeltas(final Long containerId, final Long versionId);

    /**
     * Delete a container draft.
     * 
     * @param containerId
     *            A container id.
     */
    public void deleteDraft(final Long containerId);

    /**
     * Delete the draft artifact relationship.
     * 
     * @param containerId
     *            A container id.
     * @param artifactId
     *            An artifact id.
     */
    public void deleteDraftArtifactRel(final Long containerId,
            final Long artifactId);

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
     * Read a container.
     * 
     * @param containerId
     *            A container id.
     * @param localUser
     *            The local user.
     * @return A container.
     */
    public Container read(final Long containerId, final User localUser);

    /**
     * Read a list of containers.
     * 
     * @param localUser
     *            The local user.
     * @return A list of containers.
     */
    public List<Container> read(final User localUser);

    /**
     * Read the container version delta.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param compareVersionId
     *            A container version id <code>Long</code>.
     * @param compareToVersionId
     *            A container version id <code>Long</code>.
     * @return A <code>ContainerVersionDelta</code>.
     */
    public ContainerVersionDelta readDelta(final Long containerId,
            final Long compareVersionId, final Long compareToVersionId);

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
     * Read a container draft.
     * 
     * @param containerId
     *            A container id.
     * @return A container draft.
     */
    public ContainerDraft readDraft(final Long containerId);

    /**
     * Read the latest container version.
     * 
     * @param containerId
     *            A container id.
     * @return A container version.
     */
    public ContainerVersion readLatestVersion(final Long containerId);

    /**
     * Read the published to list for the container version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public Map<User, Calendar> readPublishedTo(final Long containerId,
            final Long versionId);

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

    /**
     * Restore a container.
     * 
     * @param container
     *            A <code>Container</code>.
     */
    public void restore(final Container container);

    /**
     * Update a container version comment.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param comment
     *            A comment <code>String</code>.
     */
    @Deprecated
    public void updateComment(final Long containerId, final Long versionId,
            final String comment);

    /**
     * Update a container.
     * 
     * @param containerId
     *            A container id.
     * @param container
     *            A container.
     */
    public void updateName(final Long containerId, final String name);

    /**
     * Update a container's published to list.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param receivedOn
     *            A received on <code>Calendar</code>.
     */
    public void updatePublishedTo(final Long containerId, final Long versionId,
            final JabberId userId, final Calendar receivedOn);
}
