/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.ophelia.model.container;

import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.email.EMail;
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
import com.thinkparity.codebase.model.util.jta.TransactionType;

import com.thinkparity.ophelia.model.annotation.ThinkParityOnline;
import com.thinkparity.ophelia.model.document.CannotLockException;
import com.thinkparity.ophelia.model.events.ContainerDraftListener;
import com.thinkparity.ophelia.model.events.ContainerListener;
import com.thinkparity.ophelia.model.util.ProcessMonitor;

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
    @ThinkParityOnline
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
     * Determine whether or not a local draft exists.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return True if a local draft exists.
     */
    public Boolean doesExistLocalDraft(final Long containerId);

    /**
     * Export a container version to a directory. The 
     * 
     * @param exportStream
     *            A <code>OutputStream</code> to export to.
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     */
    public void export(final OutputStream outputStream, final Long containerId);

    /**
     * Export a container version to a directory. The 
     * 
     * @param exportStream
     *            An <code>OutputStream</code> to export to.
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     */
    public void exportVersion(final OutputStream exportStream,
            final Long containerId, final Long versionId);

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
     * Determine whether or not a draft is modified.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return True if the draft is modified.
     */
    public Boolean isLocalDraftModified(final Long containerId);

    /**
     * Print a container draft.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param printer
     *            An <code>Printer</code>.
     */
    public void printDraft(final Long containerId, final ContainerDraftPrinter printer);

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
            final ContainerVersionPrinter printer);

	/**
     * Publish a container.
     * 
     * @param monitor
     *            A <code>ProgressMonitor</code>.
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionName
     *            An optional name <code>String</code> to apply to the
     *            version.
     * @param emails
     *            A <code>List</code> of <code>EMail</code>s to publish to.
     * @param contacts
     *            A <code>List</code> of <code>Contact</code>s to publish
     *            to.
     * @param teamMembers
     *            A <code>List</code> of <code>TeamMember</code>s to
     *            publish to.
     * @throws CannotLockException
     *             if the local files representing the container's documents
     *             cannot be exclusively locked
     */
    @ThinkParityOnline
    public void publish(final ProcessMonitor monitor, final Long containerId,
            final String versionName, final List<EMail> emails,
            final List<Contact> contacts, final List<TeamMember> teamMembers)
            throws CannotLockException;

    /**
     * Publish the container version.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param emails
     *            A <code>List</code> of <code>EMail</code>s to publish to.
     * @param contacts
     *            A contact <code>List</code>.
     * @param teamMembers
     *            A <code>TeamMember</code> <code>List</code>.
     */
    @ThinkParityOnline
    public void publishVersion(final ProcessMonitor monitor,
            final Long containerId, final Long versionId,
            final List<EMail> emails, final List<Contact> contacts,
            final List<TeamMember> teamMembers);

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
     * Read a draft for the container.
     * 
     * @param containerId
     *            The container id.
     * @param comparator
     *            An artifact <code>Comparator</code>.
     * @return A container draft; or null if none exists.
     */
    public ContainerDraft readDraft(final Long containerId,
            final Comparator<Artifact> comparator);

    /**
     * Read a draft for the container.
     * 
     * @param containerId
     *            The container id.
     * @param comparator
     *            An artifact <code>Comparator</code>.
     * @param filter
     *            A document <code>Filter</code>.
     * @return A container draft; or null if none exists.
     */
    public ContainerDraft readDraft(final Long containerId,
            final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter);

    /**
     * Read a draft for the container.
     * 
     * @param containerId
     *            The container id.
     * @param filter
     *            A document <code>Filter</code>.
     * @return A container draft; or null if none exists.
     */
    public ContainerDraft readDraft(final Long containerId,
            final Filter<? super Artifact> filter);

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
     * @return A <code>List&lt;User&gt;</code>.
     */
    public List<ArtifactReceipt> readPublishedTo(final Long containerId,
            final Long versionId, final Comparator<ArtifactReceipt> comparator);

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
    public List<ArtifactReceipt> readPublishedTo(final Long containerId,
            final Long versionId, final Filter<? super ArtifactReceipt> filter);

    /**
     * Read a list of team members that the user can publish to.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return A <code>List</code> of <code>TeamMember</code>s.
     */
    public List<TeamMember> readPublishToTeam(final Long containerId);

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
     * Rename a document.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param documentId
     *            A document id <code>Long</code>.
     * @param name
     *            The new document name <code>String</code>.
     */
    public void renameDocument(final Long containerId, final Long documentId,
            final String name) throws CannotLockException;

    /**
     * Restore a container from an archive.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    @ThinkParityOnline
    public void restore(final Long containerId);

    /**
     * Restore the draft. The draft that has previously been saved is restored.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void restoreDraft(final Long containerId) throws CannotLockException;

    /**
     * Revert a document to it's pre-draft state.
     * 
     * @param documentId
     *            A document id.
     */
    public void revertDocument(final Long containerId, final Long documentId)
            throws CannotLockException;

    /**
     * Save the draft. All "dirty" draft documents are saved to the database.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void saveDraft(final Long containerId) throws CannotLockException;

    /**
     * Search for containers.
     * 
     * @param expression
     *            A search expression <code>String</code>.
     * @return A <code>List&lt;Long&gt;</code>.
     */
    public List<Long> search(final String expression);

    /**
     * Update the local draft comment.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param comment
     *            The comment <code>String</code>.
     */
    public void updateDraftComment(final Long containerId, final String comment);
}
