/*
 * Generated On: Jul 06 06 09:02:36 AM
 */
package com.thinkparity.server.model.container;

import java.util.UUID;

import org.jivesoftware.messenger.auth.UnauthorizedException;

import org.xmpp.packet.IQ;

import com.thinkparity.model.artifact.ArtifactType;

import com.thinkparity.server.ParityServerConstants.Xml;
import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.session.Session;

/**
 * <b>Title:</b>thinkParity Container Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
class ContainerModelImpl extends AbstractModelImpl {

    /** A container event generator. */
    private final ContainerEventGenerator eventGenerator;

    /**
     * Create ContainerModelImpl.
     *
     * @param session
     *		The user's session.
     */
    ContainerModelImpl(final Session session) {
        super(session);
        this.eventGenerator = new ContainerEventGenerator();
    }

    /**
     * Publish an artifact version.
     * 
     * @param uniqueId
     *            The container unique id.
     * @param versionId
     *            The container version id.
     * @param count
     *            The total artifact count.
     * @param index
     *            The artifact index.
     * @param artifactUniqueId
     *            The artifact unique id.
     * @param artifactVersionId
     *            The artifact version id.
     * @param type
     *            The artifact type.
     * @param bytes
     *            The artifact bytes.
     */
    public void publishArtifact(final UUID uniqueId, final Long versionId,
            final Integer count, final Integer index,
            final UUID artifactUniqueId, final Long artifactVersionId,
            final ArtifactType type, final byte[] bytes) {
        logApiId();
        logger.debug(uniqueId);
        logger.debug(count);
        logger.debug(index);
        logger.debug(type);
        logger.debug(bytes);
        try {
            doPublishArtifact(uniqueId, versionId, count, index,
                    artifactUniqueId, artifactVersionId, type, bytes);
        }
        catch(final Throwable t) { throw translateError(t); }
    }

    /**
     * @see ContainerModelImpl#publishArtifact(UUID, Long, Integer, Integer,
     *      UUID, Long, ArtifactType, byte[])
     * 
     */
    private void doPublishArtifact(final UUID uniqueId, final Long versionId,
            final Integer count, final Integer index,
            final UUID artifactUniqueId, final Long artifactVersionId,
            final ArtifactType type, final byte[] bytes)
            throws ParityServerModelException, UnauthorizedException {
        notifyArtifactPublished(uniqueId, versionId, artifactUniqueId,
                artifactVersionId, type, bytes, eventGenerator);
    }

    /**
     * Send a notification that an artifact was published for a container.
     * 
     * @param containerArtifact
     *            The container.
     * @param containerVersionId
     *            The container version id.
     * @param artifact
     *            The artifact.
     * @param artifactVersionId
     *            The artifact version id.
     * @param artifactType
     *            The artifact type.
     * @param artifactBytes
     *            The artifact bytes
     * @param eventGenerator
     *            The event generator.
     * @throws ParityServerModelException
     * @throws UnauthorizedException
     */
    private void notifyArtifactPublished(final UUID containerUniqueId,
            final Long containerVersionId, final UUID artifactUniqueId,
            final Long artifactVersionId, final ArtifactType artifactType,
            final byte[] artifactBytes,
            final ContainerEventGenerator eventGenerator)
            throws ParityServerModelException, UnauthorizedException {
        final IQ notification = eventGenerator.generate(
                Xml.Event.Container.ARTIFACT_PUBLISHED, containerUniqueId,
                containerVersionId, artifactUniqueId, artifactVersionId,
                artifactType, artifactBytes);
        notifyTeam(containerUniqueId, notification);
    }
}
