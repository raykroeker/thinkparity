/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.ophelia.model.container;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.AbstractModel;
import com.thinkparity.ophelia.model.events.ContainerListener;
import com.thinkparity.ophelia.model.user.TeamMember;
import com.thinkparity.ophelia.model.util.Printer;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Container Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.3
 */
public class ContainerModel extends AbstractModel<ContainerModelImpl> {

    /**
	 * Create a Container interface.
	 * 
	 * @param context
	 *            A thinkParity internal context.
	 * @return The internal Container interface.
	 */
	public static InternalContainerModel getInternalModel(
            final Context context, final Environment environment,
            final Workspace workspace) {
		return new InternalContainerModel(context, environment, workspace);
	}

    /**
	 * Create a Container interface.
	 * 
	 * @return The Container interface.
	 */
	public static ContainerModel getModel(final Environment environment,
            final Workspace workspace) {
		return new ContainerModel(environment, workspace);
	}

    /**
	 * Create ContainerModel.
	 *
	 * @param workspace
	 *		The thinkParity workspace.
	 */
	protected ContainerModel(final Environment environment, final Workspace workspace) {
		super(new ContainerModelImpl(environment, workspace));
	}

    /**
     * Add a document to a container.
     * 
     * @param containerId
     *            A container id.
     * @param documentId
     *            A document id.
     */
    public void addDocument(final Long containerId, final Long documentId) {
        synchronized(getImplLock()) {
            getImpl().addDocument(containerId, documentId);
        }
    }

    /**
     * Add a container listener.
     * 
     * @param listener
     *            A container listener.
     */
    public void addListener(final ContainerListener listener) {
        synchronized(getImplLock()) {
            getImpl().addListener(listener);
        }
    }

    /**
     * Archive a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void archive(final Long containerId) {
        synchronized (getImplLock()) {
            getImpl().archive(containerId);
        }
    }

    /**
     * Create a container.
     * 
     * @param name
     *            The container name.
     * @return The new container.
     */
    public Container create(final String name) {
        synchronized(getImplLock()) { return getImpl().create(name); }
    }

    /**
     * Create a container draft.
     * 
     * @param containerId
     *            The container id.
     * @return A container draft.
     */
    public ContainerDraft createDraft(final Long containerId) {
        synchronized(getImplLock()) { return getImpl().createDraft(containerId); }
    }

	/**
     * Delete a container.
     * 
     * @param containerId
     *            A container id.
     */
    public void delete(final Long containerId) {
        synchronized(getImplLock()) { getImpl().delete(containerId); }
    }

	/**
     * Delete a draft.
     * 
     * @param containerId
     *            A container id.
     */
    public void deleteDraft(final Long containerId) {
        synchronized (getImplLock()) {
            getImpl().deleteDraft(containerId);
        }
    }

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
    public void export(final File exportDirectory, final Long containerId,
            final Long versionId) {
        synchronized (getImplLock()) {
            getImpl().export(exportDirectory, containerId, versionId);
        }
    }

    /**
     * Print a container draft.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param printer
     *            An <code>Printer</code>.
     */
    public void printDraft(final Long containerId, final Printer printer) {
        synchronized (getImplLock()) {
            getImpl().printDraft(containerId, printer);
        }
    }

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
            final Printer printer) {
        synchronized (getImplLock()) {
            getImpl().printVersion(containerId, versionId, printer);
        }
    }

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
    public void publish(final Long containerId, final List<Contact> contacts,
            final List<TeamMember> teamMembers) {
        synchronized (getImplLock()) {
            getImpl().publish(containerId, contacts, teamMembers);
        }
    }

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
    public void publish(final Long containerId, final String comment,
            final List<Contact> contacts, final List<TeamMember> teamMembers) {
        synchronized (getImplLock()) {
            getImpl().publish(containerId, comment, contacts, teamMembers);
        }
    }

    /**
     * Read the containers.
     * 
     * @return A list of containers.
     */
	public List<Container> read() { synchronized(getImplLock()) { return getImpl().read(); } }

    /**
     * Read the containers.
     * 
     * @param comparator
     *            A sort ordering to user.
     * @return A list of containers.
     */
    public List<Container> read(final Comparator<Artifact> comparator) {
        synchronized(getImplLock()) { return getImpl().read(comparator); }
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
    public List<Container> read(final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        synchronized(getImplLock()) { return getImpl().read(comparator, filter); }
    }

    /**
     * Read the containers.
     * 
     * @param filter
     *            A filter to apply.
     * @return A list of containers.
     */
    public List<Container> read(final Filter<? super Artifact> filter) {
        synchronized(getImplLock()) { return getImpl().read(filter); }
    }

    /**
     * Read a container.
     * 
     * @param containerId
     *            A container id.
     * @return A container.
     */
    public Container read(final Long containerId) {
        synchronized(getImplLock()) { return getImpl().read(containerId); }
    }

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
            final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readDocumentVersions(containerId, versionId);
        }
    }

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
            final Long versionId, final Comparator<ArtifactVersion> comparator) {
        synchronized (getImplLock()) {
            return getImpl().readDocumentVersions(containerId, versionId, comparator);
        }
    }

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
            final Filter<? super ArtifactVersion> filter) {
        synchronized (getImplLock()) {
            return getImpl().readDocumentVersions(containerId, versionId,
                    comparator, filter);
        }
    }

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
            final Long versionId, final Filter<? super ArtifactVersion> filter) {
        synchronized (getImplLock()) {
            return getImpl().readDocumentVersions(containerId, versionId, filter);
        }
    }

    /**
     * Read a draft for the container.
     * 
     * @param containerId
     *            The container id.
     * @return A container draft; or null if none exists.
     */
    public ContainerDraft readDraft(final Long containerId) {
        synchronized(getImplLock()) { return getImpl().readDraft(containerId); }
    }

    /**
     * Read the latest container version.
     * 
     * @param containerId
     *            A container id.
     * @return A container version.
     */
    public ContainerVersion readLatestVersion(final Long containerId) {
        synchronized (getImplLock()) {
            return getImpl().readLatestVersion(containerId);
        }
    }

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
            final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readPublishedTo(containerId, versionId);
        }
    }

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
            final Long versionId, final Comparator<User> comparator) {
        synchronized (getImplLock()) {
            return getImpl().readPublishedTo(containerId, versionId, comparator);
        }
    }

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
            final Filter<? super User> filter) {
        synchronized (getImplLock()) {
            return getImpl().readPublishedTo(containerId, versionId, comparator, filter);
        }
    }

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
            final Long versionId, final Filter<? super User> filter) {
        synchronized (getImplLock()) {
            return getImpl().readPublishedTo(containerId, versionId, filter);
        }
    }

    /**
     * Read a list of users the container version was shared with.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public Map<User, ArtifactReceipt> readSharedWith(final Long containerId,
            final Long versionId) {
        synchronized (getImplLock()) {
            return getImpl().readSharedWith(containerId, versionId);
        }
    }

    /**
     * Read a list of users the container version was shared with.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.\
     * @param comparator
     *            A <code>Comparator&lt;User&gt;</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public Map<User, ArtifactReceipt> readSharedWith(final Long containerId,
            final Long versionId, final Comparator<User> comparator) {
        synchronized (getImplLock()) {
            return getImpl().readSharedWith(containerId, versionId, comparator);
        }
    }

    /**
     * Read a list of users the container version was shared with.
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
    public Map<User, ArtifactReceipt> readSharedWith(final Long containerId,
            final Long versionId, final Comparator<User> comparator,
            final Filter<? super User> filter) {
        synchronized (getImplLock()) {
            return getImpl().readSharedWith(containerId, versionId, comparator,
                    filter);
        }
    }

    /**
     * Read a list of users the container version was shared with.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param filter
     *            A <code>Filter&lt;? super User&gt;</code>.
     * @return A <code>List&lt;User&gt;</code>.
     */
    public Map<User, ArtifactReceipt> readSharedWith(final Long containerId,
            final Long versionId, final Filter<? super User> filter) {
        synchronized (getImplLock()) {
            return getImpl().readSharedWith(containerId, versionId, filter);
        }
    }

	/**
     * Read the team for the container.
     * 
     * @param containerId
     *            A container id.
     * @return A list of users.
     */
    public List<TeamMember> readTeam(final Long containerId) {
        synchronized (getImplLock()) {
            return getImpl().readTeam(containerId);
        }
    }

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
            final Long versionId) {
        synchronized(getImplLock()) { return getImpl().readVersion(containerId, versionId); }
    }

    /**
     * Read a list of versions for the container.
     * 
     * @param containerId
     *            A container id.
     * @return A list of versions.
     */
    public List<ContainerVersion> readVersions(final Long containerId) {
        synchronized(getImplLock()) {
            return getImpl().readVersions(containerId);
        }
    }

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
            final Comparator<ArtifactVersion> comparator) {
        synchronized(getImplLock()) {
            return getImpl().readVersions(containerId, comparator);
        }
    }

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
            final Filter<? super ArtifactVersion> filter) {
        synchronized(getImplLock()) {
            return getImpl().readVersions(containerId, comparator, filter);
        }
    }

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
            final Filter<? super ArtifactVersion> filter) {
        synchronized(getImplLock()) {
            return getImpl().readVersions(containerId, filter);
        }
    }

    /**
     * Remove a document from a container.
     * 
     * @param containerId
     *            A container id.
     * @param documentId
     *            A document id.
     */
    public void removeDocument(final Long containerId, final Long documentId) {
        synchronized(getImplLock()) { getImpl().removeDocument(containerId, documentId); }
    }

    /**
     * Remove a container listener.
     * 
     * @param listener
     *            A container listener.
     */
    public void removeListener(final ContainerListener listener) {
        synchronized(getImplLock()) { getImpl().removeListener(listener); }
    }

    /**
     * Rename the container.
     * 
     * @param containerId
     *            A container id.
     * @param name
     *            The new container name.
     */
    public void rename(final Long containerId, final String name) {
        synchronized (getImplLock()) {
            getImpl().rename(containerId, name);
        }
    }

    /**
     * Restore a container from an archive.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     */
    public void restore(final UUID uniqueId) {
        synchronized (getImplLock()) {
            getImpl().restore(uniqueId);
        }
    }

    /**
     * Revert a document to it's pre-draft state.
     * 
     * @param documentId
     *            A document id.
     */
    public void revertDocument(final Long containerId, final Long documentId) {
        synchronized (getImplLock()) {
            getImpl().revertDocument(containerId, documentId);
        }
    }

    /**
     * Search for containers.
     * 
     * @param expression
     *            A search expression <code>String</code>.
     * @return A <code>List&lt;Long&gt;</code>.
     */
    public List<Long> search(final String expression) {
        synchronized (getImplLock()) {
            return getImpl().search(expression);
        }
    }

    /**
     * Share a version of the container with a list of users.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @param contacts
     *            A <code>List&lt;Contact&gt;</code>.
     * @param teamMembers
     *            A <code>List&lt;TeamMember&gt;</code>.
     */
    public void share(final Long containerId, final Long versionId,
            final List<Contact> contacts, final List<TeamMember> teamMembers) {
        synchronized (getImplLock()) {
            getImpl().share(containerId, versionId, contacts, teamMembers);
        }
    }

    /**
     * Subscribe to the container's team.
     * 
     * @param containerId
     *            A container id.
     */
    public void subscribe(final Long containerId) {
        synchronized (getImplLock()) {
            getImpl().subscribe(containerId);
        }
    }

    /**
     * Unsubscribe from the container's team.
     * 
     * @param containerId
     *            A container id.
     */
    public void unsubscribe(final Long containerId) {
        synchronized (getImplLock()) {
            getImpl().unsubscribe(containerId);
        }
    }
}
