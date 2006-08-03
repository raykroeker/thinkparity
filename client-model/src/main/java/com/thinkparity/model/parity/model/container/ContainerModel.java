/*
 * Generated On: Jun 27 06 12:13:12 PM
 */
package com.thinkparity.model.parity.model.container;

import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.ContainerListener;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.artifact.KeyRequest;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.progress.ProgressIndicator;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.JabberId;
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
     * Accept a key request made by a user.
     * 
     * @param containerId
     *            The container id.
     * @param jabberId
     *            The user making the request.
     */
    public void acceptKeyRequest(final Long keyRequestId)
            throws ParityException {
        synchronized(implLock) { impl.acceptKeyRequest(keyRequestId); }
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
     * Archive a container.
     * 
     * @param containerId
     *            A container id.
     */
    public void archive(final Long containerId) {
        Assert.assertNotYetImplemented("");
    }

	/**
     * Archive a container.
     * 
     * @param containerId
     *            A container id.
     * @param progressIndicator
     *            A progress indicator.
     */
    public void archive(final Long containerId,
            final ProgressIndicator progressIndicator) {
        Assert.assertNotYetImplemented("");
    }

	/**
     * Close a container.
     * 
     * @param containerId
     *            A container id.
     */
    public void close(final Long containerId) throws ParityException {
        synchronized(implLock) { impl.close(containerId); }
    }

    /**
     * Create a container.
     * 
     * @param name
     *            The container name.
     * @return The new container.
     */
    public Container create(final String name) throws ParityException {
        synchronized(implLock) { return impl.create(name); }
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
     * Decline a key request made by a user.
     * 
     * @param containerId
     *            The container id.
     * @param jabberId
     *            The user making the request.
     */
    public void declineKeyRequest(final Long keyRequestId)
            throws ParityException {
        synchronized(implLock) { impl.declineKeyRequest(keyRequestId); }
    }

    /**
     * Delete a container.
     * 
     * @param containerId
     *            A container id.
     */
    public void delete(final Long containerId) throws ParityException {
        synchronized(implLock) { impl.delete(containerId); }
    }

    /**
     * Publish the container.
     * 
     * @param containerId
     *            The container id.
     * @throws ParityException
     */
    public void publish(final Long containerId) throws ParityException {
        synchronized(implLock) { impl.publish(containerId); }
    }

    /**
     * Reactivate a container.
     * 
     * @param containerId
     *            A container id.
     * @throws ParityException
     */
    public void reactivate(final Long containerId) throws ParityException {
        synchronized(implLock) { impl.reactivate(containerId); }
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
     * @throws ParityException
     */
    public List<Document> readDocuments(final Long containerId,
            final Long versionId) throws ParityException {
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
     * @throws ParityException
     */
    public List<Document> readDocuments(final Long containerId,
            final Long versionId, final Comparator<Artifact> comparator)
            throws ParityException {
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
     * @throws ParityException
     */
    public List<Document> readDocuments(final Long containerId,
            final Long versionId, final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) throws ParityException {
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
     * @throws ParityException
     */
    public List<Document> readDocuments(final Long containerId, final Long versionId,
            final Filter<? super Artifact> filter) throws ParityException {
        synchronized(implLock) { return impl.readDocuments(containerId, versionId, filter); }
    }

    /**
     * Read a list of key requests for the container.
     * 
     * @param containerId
     *            A container id.
     * @return A list of key requests.
     */
    public List<KeyRequest> readKeyRequests(final Long containerId) {
        synchronized(implLock) { return impl.readKeyRequests(containerId); }
    }

    /**
     * Read a list of key requests for the container.
     * 
     * @param containerId
     *            A container id.
     * @param comparator
     *            A key request comparator.
     * @return A list of key requests.
     */
    public List<KeyRequest> readKeyRequests(final Long containerId,
            final Comparator<KeyRequest> comparator) {
        synchronized(implLock) { return impl.readKeyRequests(containerId, comparator); }
    }

    /**
     * Read a list of key requests for the container.
     * 
     * @param containerId
     *            A container id.
     * @param comparator
     *            A key request comparator.
     * @param filter
     *            A key request filter.
     * @return A list of key requests.
     */
    public List<KeyRequest> readKeyRequests(final Long containerId,
            final Comparator<KeyRequest> comparator,
            final Filter<? super KeyRequest> filter) {
        synchronized(implLock) { return impl.readKeyRequests(containerId, comparator, filter); }
    }

    /**
     * Read a list of key requests for the container.
     * 
     * @param containerId
     *            A container id.
     * @param filter
     *            A key request filter.
     * @return A list of key requests.
     */
    public List<KeyRequest> readKeyRequests(final Long containerId, final Filter<? super KeyRequest> filter) {
        synchronized(implLock) { return impl.readKeyRequests(containerId, filter); }
    }

    /**
     * Read the latest container version.
     * 
     * @param containerId
     *            A container id.
     * @return A container version.
     */
    public ContainerVersion readLatestVersion(final Long containerId) {
        synchronized(implLock) { return impl.readLatestVersion(containerId); }
    }

	/**
     * Read the team for the container.
     * 
     * @param containerId
     *            A container id.
     * @return A list of users.
     */
    public List<User> readTeam(final Long containerId) {
        synchronized(getImplLock()) { return getImpl().readTeam(containerId); }
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
    public void removeDocument(final Long containerId, final Long documentId)
            throws ParityException {
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
     * Send the container's key to a user. All pending key requests not made by
     * the recipient of the key will be declined. This is the same as accepting
     * a key request if one exists for a user.
     * 
     * @param containerId
     *            The container id.
     * @param jabberId
     *            The jabber id.
     */
    public void sendKey(final Long containerId, final JabberId jabberId)
            throws ParityException {
        synchronized(implLock) { impl.sendKey(containerId, jabberId); }
    }

    /**
     * Update the team for the container.
     * 
     * @param containerId
     *            A container id.
     * @param team
     *            A list of users.
     */
    public void updateTeam(final Long containerId, final List<User> team) {
        synchronized(getImplLock()) { getImpl().updateTeam(containerId, team); }
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
