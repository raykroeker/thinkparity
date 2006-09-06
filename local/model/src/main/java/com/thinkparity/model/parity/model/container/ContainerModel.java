/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.model.parity.model.container;

import java.util.Comparator;
import java.util.List;

import com.thinkparity.model.Printer;
import com.thinkparity.model.parity.api.events.ContainerListener;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.user.TeamMember;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * <b>Title:</b>thinkParity Container Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.3
 */
public class ContainerModel {

    /**
	 * Create a Container interface.
	 * 
	 * @param context
	 *            A thinkParity internal context.
	 * @return The internal Container interface.
	 */
	public static InternalContainerModel getInternalModel(final Context context) {
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		return new InternalContainerModel(workspace, context);
	}

    /**
	 * Create a Container interface.
	 * 
	 * @return The Container interface.
	 */
	public static ContainerModel getModel() {
		final Workspace workspace = WorkspaceModel.getModel().getWorkspace();
		return new ContainerModel(workspace);
	}

    /** The model implementation. */
	private final ContainerModelImpl impl;

    /** The model implementation synchronization lock. */
	private final Object implLock;

    /**
	 * Create ContainerModel.
	 *
	 * @param workspace
	 *		The thinkParity workspace.
	 */
	protected ContainerModel(final Workspace workspace) {
		super();
		this.impl = new ContainerModelImpl(workspace);
		this.implLock = new Object();
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
        synchronized(implLock) { impl.addListener(listener); }
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
        synchronized(implLock) { impl.delete(containerId); }
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
	public List<Container> read() { synchronized(implLock) { return impl.read(); } }

    /**
     * Read the containers.
     * 
     * @param comparator
     *            A sort ordering to user.
     * @return A list of containers.
     */
    public List<Container> read(final Comparator<Artifact> comparator) {
        synchronized(implLock) { return impl.read(comparator); }
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
        synchronized(implLock) { return impl.read(comparator, filter); }
    }

    /**
     * Read the containers.
     * 
     * @param filter
     *            A filter to apply.
     * @return A list of containers.
     */
    public List<Container> read(final Filter<? super Artifact> filter) {
        synchronized(implLock) { return impl.read(filter); }
    }

    /**
     * Read a container.
     * 
     * @param containerId
     *            A container id.
     * @return A container.
     */
    public Container read(final Long containerId) {
        synchronized(implLock) { return impl.read(containerId); }
    }

    /**
     * Read the documents for the container.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @return A list of documents.
     */
    public List<Document> readDocuments(final Long containerId,
            final Long versionId) {
        synchronized(implLock) { return impl.readDocuments(containerId, versionId); }
    }

    /**
     * Read the documents for the container.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @param comparator
     *            A document comparator.
     * @return A list of documents.
     */
    public List<Document> readDocuments(final Long containerId,
            final Long versionId, final Comparator<Artifact> comparator) {
        synchronized(implLock) { return impl.readDocuments(containerId, versionId, comparator); }
    }

    /**
     * Read the documents for the container.
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
    public List<Document> readDocuments(final Long containerId,
            final Long versionId, final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        synchronized(implLock) { return impl.readDocuments(containerId, versionId, comparator, filter); }
    }

    /**
     * Read the document versions for the container.
     * 
     * @param containerId
     *            A container id.
     * @param versionId
     *            A version id.
     * @param filter
     *            A document filter.
     * @return A list of document versions.
     */
    public List<Document> readDocuments(final Long containerId, final Long versionId,
            final Filter<? super Artifact> filter) {
        synchronized(implLock) { return impl.readDocuments(containerId, versionId, filter); }
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
    public List<User> readPublishedTo(final Long containerId,
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
    public List<User> readPublishedTo(final Long containerId,
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
    public List<User> readPublishedTo(final Long containerId,
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
    public List<User> readPublishedTo(final Long containerId,
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
    public List<User> readSharedWith(final Long containerId,
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
    public List<User> readSharedWith(final Long containerId,
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
    public List<User> readSharedWith(final Long containerId,
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
    public List<User> readSharedWith(final Long containerId,
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
        synchronized(implLock) { return impl.readVersion(containerId, versionId); }
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
        synchronized(implLock) { impl.removeDocument(containerId, documentId); }
    }

    /**
     * Remove a container listener.
     * 
     * @param listener
     *            A container listener.
     */
    public void removeListener(final ContainerListener listener) {
        synchronized(implLock) { impl.removeListener(listener); }
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

	/**
	 * Obtain the model implementation.
	 * 
	 * @return The model implementation.
	 */
	protected ContainerModelImpl getImpl() { return impl; }

    /**
	 * Obtain the model implementation synchronization lock.
	 * 
	 * @return The model implementation synchrnoization lock.
	 */
	protected Object getImplLock() { return implLock; }
}
