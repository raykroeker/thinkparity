/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.filter.Filter;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.document.CannotLockException;

/**
 * <b>Title:</b>thinkParity Container Model<br>
 * <b>Description:</b>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.28
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface ContainerModel {

    /**
     * Delete a container.
     * 
     * @param containerId
     *            A container id.
     * @throws CannotLockException
     *             if the container documents cannot be locked
     */
    public void delete(final Long containerId) throws CannotLockException;

    /**
     * Delete a draft.
     * 
     * @param containerId
     *            A container id.
     */
    public void deleteDraft(final Long containerId) throws CannotLockException;

    /**
     * Determine whether or not a local draft exists.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return True if a local draft exists.
     */
    public Boolean doesExistLocalDraft(final Long containerId);

    /**
     * Determine whether or not the container has been distributed.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return True if the container has been distributed.
     */
    public Boolean isDistributed(final Long containerId);

    /**
     * Read the containers.
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
     * Read the document versions for a container version.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @return A list of documents.
     */
    public List<DocumentVersion> readDocumentVersions(final Long containerId,
            final Long versionId);

    /**
     * Read the document versions for a container version.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @param comparator
     *            A document comparator.
     * @return A list of documents.
     */
    public List<DocumentVersion> readDocumentVersions(final Long containerId,
            final Long versionId, final Comparator<ArtifactVersion> comparator);

    /**
     * Read the document versions for a container version.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @param comparator
     *            A document comparator.
     * @param filter
     *            A document filter.
     * @return A list of documents.
     */
    public List<DocumentVersion> readDocumentVersions(final Long containerId,
            final Long versionId, final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter);

    /**
     * Read a draft for the container.
     * 
     * @param containerId
     *            The container id.
     * @return A container draft; or null if none exists.
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
     * Read the next container version sequentially.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>ContainerVersion</code>.
     */
    public ContainerVersion readNextVersion(final Long containerId,
            final Long versionId);

    /**
     * Read the previous container version sequentially.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>ContainerVersion</code>.
     */
    public ContainerVersion readPreviousVersion(final Long containerId,
            final Long versionId);

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public List<ArtifactReceipt> readPublishedTo(final Long containerId,
            final Long versionId);

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param comparator
     *            A <code>Comparator&lt;User&gt;</code>.
     * @param filter
     *            A <code>Filter&lt;? super User&gt;</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public List<ArtifactReceipt> readPublishedTo(final Long containerId,
            final Long versionId, final Comparator<ArtifactReceipt> comparator,
            final Filter<? super ArtifactReceipt> filter);

    /**
     * Read the team for the container.
     * 
     * @param containerId
     *            A container id.
     * @return A list of users.
     */
    public List<TeamMember> readTeam(final Long containerId);

    /**
     * Read a container version.
     * 
     * @param containerId
     *            The container id.
     * @param versionId
     *            The version id.
     * @return A container version.
     */
    public ContainerVersion readVersion(final Long containerId,
            final Long versionId);

    /**
     * Read a list of versions for the container.
     * 
     * @param containerId
     *            A container id.
     * @return A list of versions.
     */
    public List<ContainerVersion> readVersions(final Long containerId);

	/**
     * Read a list of versions for the container.
     * 
     * @param containerId
     *            A container id.
     * @param comparator
     *            A container version comparator.
     * @return A list of versions.
     */
    public List<ContainerVersion> readVersions(final Long containerId,
            final Comparator<ArtifactVersion> comparator);

    /**
     * Read a list of versions for the container.
     * 
     * @param containerId
     *            A container id.
     * @param comparator
     *            A container version comparator.
     * @param filter
     *            A container version filter.
     * @return A list of versions.
     */
    public List<ContainerVersion> readVersions(final Long containerId,
            final Comparator<ArtifactVersion> comparator,
            final Filter<? super ArtifactVersion> filter);

    /**
     * Determine whether or not the container is published to the user.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param user
     *            A <code>User</code>.
     * @return A <code>Boolean</code>.
     */
    Boolean isPublishedBy(ContainerVersion version, User user);

    /**
     * Determine whether or not the container is published to the user.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param user
     *            A <code>User</code>.
     * @return A <code>Boolean</code>.
     */
    Boolean isPublishedTo(ContainerVersion version, User user);
}
