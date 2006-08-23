/*
 * Generated On: Jul 06 06 09:02:36 AM
 */
package com.thinkparity.server.model.container;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.artifact.ArtifactType;

import com.thinkparity.server.model.AbstractModel;
import com.thinkparity.server.model.session.Session;

/**
 * <b>Title:</b>thinkParity Container Model<br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
public class ContainerModel extends AbstractModel {

	/**
	 * Create a Container interface.
	 * 
	 * @return The Container interface.
	 */
	public static ContainerModel getModel(final Session session) {
		return new ContainerModel(session);
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
	protected ContainerModel(final Session session) {
		super();
		this.impl = new ContainerModelImpl(session);
		this.implLock = new Object();
	}

    /**
     * Publish the container version.
     * 
     * @param uniqueId
     *            A container unique id.
     * @param versionId
     *            A container version id.
     * @param name
     *            A container name.
     * @param artifactCount
     *            The number of artifacts in the container version.
     * @param publishedBy
     *            By whom the container was published.
     * @param publishedTo
     *            Whom to container was published to.
     * @param publishedOn
     *            When the container was published.
     */
    public void publish(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final JabberId publishedBy, final List<JabberId> publishedTo,
            final Calendar publishedOn) {
        synchronized (getImplLock()) {
            getImpl().publish(uniqueId, versionId, name, artifactCount,
                    publishedBy, publishedTo, publishedOn);
        }
    }

    /**
     * Publish a container version artifact version to a subset of team members.
     * 
     * @param uniqueId
     *            A container unique id.
     * @param versionId
     *            A container version id.
     * @param name
     *            A container name.
     * @param artifactCount
     *            An artifact count for the container version.
     * @param artifactIndex
     *            The artifact's index in the container version.
     * @param artifactUniqueId
     *            An artifact unique id.
     * @param artifactVersionId
     *            An artifact version id.
     * @param artifactName
     *            An artifact name.
     * @param artifactType
     *            An artifact type.
     * @param artifactChecksum
     *            An artifact checksum.
     * @param artifactBytes
     *            An artifact byte array.
     * @param publishTo
     *            To whom the container was published.
     * @param publishedBy
     *            By whom the artifact was published.
     * @param publishedOn
     *            When the artifact was published.
     */
    public void publishArtifact(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final Integer artifactIndex, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final byte[] artifactBytes, final List<JabberId> publishTo,
            final JabberId publishedBy, final Calendar publishedOn) {
        synchronized (getImplLock()) {
            getImpl().publishArtifact(uniqueId, versionId, name, artifactCount,
                    artifactIndex, artifactUniqueId, artifactVersionId,
                    artifactName, artifactType, artifactChecksum,
                    artifactBytes, publishTo, publishedBy, publishedOn);
        }
    }

    /**
     * Send a container version.
     * 
     * @param uniqueId
     *            The container unique id.
     * @param versionId
     *            The container version id.
     * @param name
     *            The container name.
     * @param artifactCount
     *            The number of artifacts in the container version.
     * @param sentBy
     *            By whom the container was sent.
     * @param sentTo
     *            To whom the container version was sent.
     * @param sentOn
     *            When the container was sent.
     */
    public void send(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final JabberId sentBy, final List<JabberId> sentTo,
            final Calendar sentOn) {
        synchronized (getImplLock()) {
            getImpl().send(uniqueId, versionId, name, artifactCount, sentBy,
                    sentTo, sentOn);
        }
    }

    /**
     * Send a container version artifact version to a list of users.
     * 
     * @param uniqueId
     *            The container unique id.
     * @param versionId
     *            The container version id.
     * @param name
     *            The container name.
     * @param artifactCount
     *            The number of artifacts within the container version.
     * @param artifactIndex
     *            The index of the artifact within the container version.
     * @param artifactUniqueId
     *            The artifact unique id.
     * @param artifactVersionId
     *            The artifact version id.
     * @param artifactName
     *            The artifact name.
     * @param type
     *            The artifact type.
     * @param checksum
     *            The artifact checksum.
     * @param artifactBytes
     *            The artifact bytes.
     * @param sendTo
     *            Whom to send the container version to.
     * @param sentBy
     *            By whom the container version was sent.
     * @param sentOn
     *            When the container version was sent.
     */
    public void sendArtifact(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final Integer artifactIndex, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final byte[] artifactBytes, final List<JabberId> sendTo,
            final JabberId sentBy, final Calendar sentOn) {
        synchronized (getImplLock()) {
            getImpl().sendArtifact(uniqueId, versionId, name, artifactCount,
                    artifactIndex, artifactUniqueId, artifactVersionId,
                    artifactName, artifactType, artifactChecksum,
                    artifactBytes, sendTo, sentBy, sentOn);
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
