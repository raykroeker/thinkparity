/*
 * Generated On: Jul 06 06 09:02:36 AM
 */
package com.thinkparity.server.model.container;

import java.util.Calendar;
import java.util.UUID;

import org.jivesoftware.messenger.auth.UnauthorizedException;

import org.xmpp.packet.IQ;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.xmpp.IQWriter;

import com.thinkparity.server.ParityServerConstants.Jabber;
import com.thinkparity.server.ParityServerConstants.Xml;
import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.Artifact;
import com.thinkparity.server.model.io.sql.artifact.ArtifactSql;
import com.thinkparity.server.model.session.Session;

/**
 * <b>Title:</b>thinkParity Container Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
class ContainerModelImpl extends AbstractModelImpl {

    /** Artifact db io. */
    private final ArtifactSql artifactIO;

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
        this.artifactIO = new ArtifactSql();
        this.eventGenerator = new ContainerEventGenerator();
    }

    /**
     * Publish the container.
     * 
     * @param uniqueId
     *            The container unique id.
     */
    void publish(final UUID uniqueId) {
        logApiId();
        debugVariable("uniqueId", uniqueId);
        try {
            final Artifact artifact = artifactIO.select(uniqueId);
            final JabberId systemJabberId =
                JabberIdBuilder.parseQualifiedJabberId(Jabber.SYSTEM_QUALIFIED_JABBER_ID);
            artifactIO.updateKeyHolder(
                    artifact.getArtifactId(),
                    systemJabberId.getUsername(),
                    session.getJabberId());
        } catch (final Throwable t) {
            throw translateError(t);
        }
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
    void publishArtifact(final UUID uniqueId, final Long versionId,
            final Integer count, final Integer index,
            final UUID artifactUniqueId, final Long artifactVersionId,
            final ArtifactType type, final byte[] bytes, final JabberId publishedBy, final Calendar publishedOn) {
        logApiId();
        logger.debug(uniqueId);
        logger.debug(count);
        logger.debug(index);
        logger.debug(type);
        logger.debug(bytes);
        try {
            doPublishArtifact(uniqueId, versionId, count, index,
                    artifactUniqueId, artifactVersionId, type, bytes,
                    publishedBy, publishedOn);
        }
        catch(final Throwable t) { throw translateError(t); }
    }

    /**
     * Send an artifact version.
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
    void sendArtifact(final JabberId sentBy, final Calendar sentOn,
            final JabberId jabberId, final UUID uniqueId, final Long versionId,
            final String name, final Integer count, final Integer index,
            final UUID artifactUniqueId, final Long artifactVersionId,
            final String artifactName, final ArtifactType type,
            final byte[] bytes) {
        logApiId();
        debugVariable("sentBy", sentBy);
        debugVariable("sentOn", sentOn);
        debugVariable("uniqueId", uniqueId);
        debugVariable("versionId", versionId);
        debugVariable("name", name);
        debugVariable("count", count);
        debugVariable("index", index);
        debugVariable("artifactUniqueId", artifactUniqueId);
        debugVariable("artifactVersionId", artifactVersionId);
        debugVariable("artifactName", artifactName);
        debugVariable("type", type);
        debugVariable("bytes", bytes);
        try {
            doSendArtifact(sentBy, sentOn, jabberId, uniqueId, versionId, name,
                    count, index, artifactUniqueId, artifactVersionId,
                    artifactName, type, bytes);
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
            final ArtifactType type, final byte[] bytes,
            final JabberId publishedBy, final Calendar publishedOn)
            throws ParityServerModelException, UnauthorizedException {
        notifyArtifactPublished(uniqueId, versionId, count, index,
                artifactUniqueId, artifactVersionId, type, bytes, publishedBy,
                publishedOn, eventGenerator);
    }

    /**
     * @see ContainerModelImpl#sendArtifact(JabberId, UUID, Long, String,
     *      Integer, Integer, UUID, Long, String, ArtifactType, byte[])
     * 
     */
    private void doSendArtifact(final JabberId sentBy, final Calendar sentOn,
            final JabberId jabberId, final UUID uniqueId, final Long versionId,
            final String name, final Integer count, final Integer index,
            final UUID artifactUniqueId, final Long artifactVersionId,
            final String artifactName, final ArtifactType type,
            final byte[] bytes) throws ParityServerModelException,
            UnauthorizedException {
        final IQ artifactIQ = new IQ();
        artifactIQ.setTo(jabberId.getQualifiedUsername());
        artifactIQ.setType(IQ.Type.set);
        artifactIQ.setChildElement("query","jabber:iq:parity:" + Xml.Event.Container.ARTIFACT_SENT);
        final IQWriter artifactWriter = new IQWriter(artifactIQ);
        artifactWriter.writeJabberId(Xml.Container.SENT_BY, sentBy);
        artifactWriter.writeCalendar(Xml.Container.SENT_ON, sentOn);
        artifactWriter.writeUniqueId(Xml.Container.CONTAINER_UNIQUE_ID, uniqueId);
        artifactWriter.writeLong(Xml.Container.CONTAINER_VERSION_ID, versionId);
        artifactWriter.writeString(Xml.Container.CONTAINER_NAME, name);
        artifactWriter.writeInteger(Xml.Container.ARTIFACT_COUNT, count);
        artifactWriter.writeInteger(Xml.Container.ARTIFACT_INDEX, index);
        artifactWriter.writeUniqueId(Xml.Artifact.UNIQUE_ID, artifactUniqueId);
        artifactWriter.writeLong(Xml.Artifact.VERSION_ID, artifactVersionId);
        artifactWriter.writeString(Xml.Artifact.NAME, artifactName);
        artifactWriter.writeArtifactType(Xml.Artifact.TYPE, type);
        artifactWriter.writeBytes(Xml.Artifact.BYTES, bytes);
        send(jabberId, artifactWriter.getIQ());
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
            final Long containerVersionId, final Integer artifactCount,
            final Integer artifactIndex, final UUID artifactUniqueId,
            final Long artifactVersionId, final ArtifactType artifactType,
            final byte[] artifactBytes, final JabberId publishedBy,
            final Calendar publishedOn,
            final ContainerEventGenerator eventGenerator)
            throws ParityServerModelException, UnauthorizedException {
        
    }
}
