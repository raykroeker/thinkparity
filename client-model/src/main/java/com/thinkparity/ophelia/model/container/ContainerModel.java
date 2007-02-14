/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.ophelia.model.container;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.filter.Filter;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.container.monitor.PublishMonitor;
import com.thinkparity.ophelia.model.document.CannotLockException;
import com.thinkparity.ophelia.model.events.ContainerDraftListener;
import com.thinkparity.ophelia.model.events.ContainerListener;
import com.thinkparity.ophelia.model.util.Printer;

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
     * Apply a bookmark to a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void addBookmark(final Long containerId);

    /**
     * Add a document to a container.
     * 
     * @param containerId
     *            A container id.
     * @param documentId
     *            A document id.
     */
    public void addDocument(final Long containerId, final Long documentId);

    /**
     * Add a container listener.
     * 
     * @param listener
     *            A container listener.
     */
    @ThinkParityTransaction(TransactionType.NEVER)
    public void addListener(final ContainerListener listener);

    /**
     * Archive a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void archive(final Long containerId);

    /**
     * Create a container.
     * 
     * @param name
     *            The container name.
     * @return The new container.
     */
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public Container create(final String name);

    /**
     * Create a container draft.
     * 
     * @param containerId
     *            The container id.
     * @return A container draft.
     */
    public ContainerDraft createDraft(final Long containerId);

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
     * Export a container version to a directory. The 
     * 
     * @param exportDirectory
     *            A file output stream representing a zip file.
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     */
    public File export(final File exportDirectory, final Long containerId);

    /**
     * Export a container version to a directory. The 
     * 
     * @param exportDirectory
     *            A file output stream representing a zip file.
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     */
    public File exportVersion(final File exportDirectory, final Long containerId,
            final Long versionId);

    /**
     * Create a document monitor.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param listener
     *            A <code>ContainerDraftListener</code>.
     * @return A <code>ContainerDraftMonitor</code>.
     */
    public ContainerDraftMonitor getDraftMonitor(final Long containerId,
            final ContainerDraftListener listener);

	/**
     * Determine whether or not the container has been distributed.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return True if the container has been distributed.
     */
    public Boolean isDistributed(final Long containerId);

	/**
     * Print a container draft.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param printer
     *            An <code>Printer</code>.
     */
    public void printDraft(final Long containerId, final Printer printer);

    /**
     * Print a container version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param printer
     *            An <code>Printer</code>.
     */
    public void printVersion(final Long containerId, final Long versionId,
            final Printer printer);

    /**
     * Publish the container.
     * 
     * @param containerId
     *            The container id.
     * @param teamMembers
     *            A list of team members to publish to.
     * @param contacts
     *            A list of contacts to publish to.
     */
    public void publish(final PublishMonitor monitor, final Long containerId,
            final String comment, final List<Contact> contacts,
            final List<TeamMember> teamMembers) throws CannotLockException;

    /**
     * Publish the container version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param contacts
     *            A contact <code>List</code>.
     * @param teamMembers
     *            A <code>TeamMember</code> <code>List</code>.
     */
    public void publishVersion(final PublishMonitor monitor,
            final Long containerId, final Long versionId,
            final List<Contact> contacts, final List<TeamMember> teamMembers);

    /**
     * Read the containers.
     * 
     * @return A list of containers.
     */
	public List<Container> read();

    /**
     * Read the containers.
     * 
     * @param comparator
     *            A sort ordering to user.
     * @return A list of containers.
     */
    public List<Container> read(final Comparator<Artifact> comparator);

    /**
     * Read the containers.
     * 
     * @param comparator
     *            A sort ordering to user.
     * @param filter
     *            A filter to apply.
     * @return A list of containers.
     */
    public List<Container> read(final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter);

    /**
     * Read the containers.
     * 
     * @param filter
     *            A filter to apply.
     * @return A list of containers.
     */
    public List<Container> read(final Filter<? super Artifact> filter);

    /**
     * Read a container.
     * 
     * @param containerId
     *            A container id.
     * @return A container.
     */
    public Container read(final Long containerId);

    /**
     * Read the delta between two versions.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param compareVersionId
     *            A container compare version id <code>Long</code>.
     * @return A <code>Map</code> of <code>DocumentVersion</code>s to their
     *         <code>Delta</code>s.
     */
    public Map<DocumentVersion, Delta> readDocumentVersionDeltas(
            final Long containerId, final Long compareVersionId);

    /**
     * Read the delta between two versions.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param compareVersionId
     *            A container compare version id <code>Long</code>.
     * @param compareToVersionId
     *            A container compare to version id <code>Long</code>.
     * @return A <code>Map</code> of <code>DocumentVersion</code>s to their
     *         <code>Delta</code>s.
     */
    public Map<DocumentVersion, Delta> readDocumentVersionDeltas(
            final Long containerId,
            final Long compareVersionId, final Long compareToVersionId);

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
     * Read the document versions for a container version.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @param filter
     *            A document filter.
     * @return A list of document versions.
     */
    public List<DocumentVersion> readDocumentVersions(final Long containerId,
            final Long versionId, final Filter<? super ArtifactVersion> filter);

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
    public Map<User, ArtifactReceipt> readPublishedTo(final Long containerId,
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
     * @return A <code>List&lt;User&gt;</code>.
     */
    public Map<User, ArtifactReceipt> readPublishedTo(final Long containerId,
            final Long versionId, final Comparator<User> comparator);

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
    public Map<User, ArtifactReceipt> readPublishedTo(final Long containerId,
            final Long versionId, final Comparator<User> comparator,
            final Filter<? super User> filter);

    /**
     * Read a list of team members the container version was published to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param filter
     *            A <code>Filter&lt;? super User&gt;</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public Map<User, ArtifactReceipt> readPublishedTo(final Long containerId,
            final Long versionId, final Filter<? super User> filter);

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
     * Read a list of versions for the container.
     * 
     * @param containerId
     *            A container id.
     * @param filter
     *            A container version filter.
     * 
     * @return A list of versions.
     */
    public List<ContainerVersion> readVersions(final Long containerId,
            final Filter<? super ArtifactVersion> filter);

    /**
     * Remove a bookmark from a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void removeBookmark(final Long containerId);

    /**
     * Remove a document from a container.
     * 
     * @param containerId
     *            A container id.
     * @param documentId
     *            A document id.
     */
    public void removeDocument(final Long containerId, final Long documentId)
            throws CannotLockException;

    /**
     * Remove a container listener.
     * 
     * @param listener
     *            A container listener.
     */
    @ThinkParityTransaction(TransactionType.NEVER)
    public void removeListener(final ContainerListener listener);

    /**
     * Rename the container.
     * 
     * @param containerId
     *            A container id.
     * @param name
     *            The new container name.
     */
    public void rename(final Long containerId, final String name);

    /**
     * Restore a container from an archive.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void restore(final Long containerId);

    /**
     * Revert a document to it's pre-draft state.
     * 
     * @param documentId
     *            A document id.
     */
    public void revertDocument(final Long containerId, final Long documentId)
            throws CannotLockException;

    /**
     * Search for containers.
     * 
     * @param expression
     *            A search expression <code>String</code>.
     * @return A <code>List&lt;Long&gt;</code>.
     */
    public List<Long> search(final String expression);
}
