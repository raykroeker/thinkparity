/*
 * Generated On: Jul 06 06 09:02:36 AM
 */
package com.thinkparity.desdemona.model.container;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.xmpp.packet.IQ;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.archive.ArchiveModel;
import com.thinkparity.desdemona.model.io.sql.ArtifactSql;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.util.xmpp.IQWriter;


/**
 * <b>Title:</b>thinkParity Container Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
class ContainerModelImpl extends AbstractModelImpl {

    /** Artifact database io. */
    private final ArtifactSql artifactSql;

    /**
     * Create ContainerModelImpl.
     *
     * @param session
     *		The user's session.
     */
    ContainerModelImpl(final Session session) {
        super(session);
        this.artifactSql = new ArtifactSql();
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
     *            To whom to container was published.
     * @param publishedOn
     *            When the container was published.
     */
    void publish(final UUID uniqueId, final Long versionId, final String name,
            final Integer artifactCount, final JabberId publishedBy,
            final List<JabberId> publishedTo, final Calendar publishedOn) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("name", name);
        logVariable("artifactCount", artifactCount);
        logVariable("publishedBy", publishedBy);
        logVariable("publishedTo", publishedTo);
        logVariable("publishedOn", publishedOn);
        try {
            final IQWriter publishArtifact = createIQWriter("container:published");
            publishArtifact.writeUniqueId("uniqueId", uniqueId);
            publishArtifact.writeLong("versionId", versionId);
            publishArtifact.writeString("name", name);
            publishArtifact.writeInteger("artifactCount", artifactCount);
            publishArtifact.writeJabberId("publishedBy", publishedBy);
            publishArtifact.writeJabberIds("publishedTo", "publishedTo", publishedTo);
            publishArtifact.writeCalendar("publishedOn", publishedOn);
            for (final JabberId publishedToId : publishedTo) {
                final IQ iq = publishArtifact.getIQ();
                setTo(iq, publishedToId);
                send(publishedToId, iq);
            }

            final Artifact artifact = getArtifactModel().read(uniqueId);
            artifactSql.updateKeyHolder(artifact.getId(),
                    User.THINK_PARITY.getId().getUsername(), publishedBy);
            getContainerArchiveModel().backup(null);
        } catch (final Throwable t) {
            throw translateError(t);
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
    void publishArtifact(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final Integer artifactIndex, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final byte[] artifactBytes, final List<JabberId> publishTo,
            final JabberId publishedBy, final Calendar publishedOn) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("name", name);
        logVariable("artifactCount", artifactCount);
        logVariable("artifactIndex", artifactIndex);
        logVariable("artifactUniqueId", artifactUniqueId);
        logVariable("artifactVersionId", artifactVersionId);
        logVariable("artifactName", artifactName);
        logVariable("artifactType", artifactType);
        logVariable("artifactChecksum", artifactChecksum);
        logVariable("artifactBytes", artifactBytes);
        logVariable("publishTo", publishTo);
        logVariable("publishedBy", publishedBy);
        logVariable("publishedOn", publishedOn);
        try {
            final IQWriter publishArtifact = createIQWriter("container:artifactpublished");
            publishArtifact.writeUniqueId("uniqueId", uniqueId);
            publishArtifact.writeLong("versionId", versionId);
            publishArtifact.writeString("name", name);
            publishArtifact.writeInteger("artifactCount", artifactCount);
            publishArtifact.writeInteger("artifactIndex", artifactIndex);
            publishArtifact.writeUniqueId("artifactUniqueId", artifactUniqueId);
            publishArtifact.writeLong("artifactVersionId", artifactVersionId);
            publishArtifact.writeString("artifactName", artifactName);
            publishArtifact.writeArtifactType("artifactType", artifactType);
            publishArtifact.writeString("artifactChecksum", artifactChecksum);
            publishArtifact.writeBytes("artifactBytes", artifactBytes);
            publishArtifact.writeJabberId("publishedBy", publishedBy);
            publishArtifact.writeCalendar("publishedOn", publishedOn);
            for (final JabberId publishToId : publishTo) {
                final IQ iq = publishArtifact.getIQ();
                setTo(iq, publishToId);
                send(publishToId, iq);
            }
            getArtifactArchiveModel().backup(null);
        } catch (final Throwable t) {
            throw translateError(t);
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
     * @param sendTo
     *            To whom the container version should be sent.
     * @param sentOn
     *            When the container was sent.
     */
    void send(final UUID uniqueId, final Long versionId, final String name,
            final Integer artifactCount, final JabberId sentBy,
            final List<JabberId> sentTo, final Calendar sentOn) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("name", name);
        logVariable("artifactCount", artifactCount);
        logVariable("sentBy", sentBy);
        logVariable("sentTo", sentTo);
        logVariable("sentOn", sentOn);
        try {
            final IQWriter sendArtifact = createIQWriter("container:sent");
            sendArtifact.writeUniqueId("uniqueId", uniqueId);
            sendArtifact.writeLong("versionId", versionId);
            sendArtifact.writeString("name", name);
            sendArtifact.writeInteger("artifactCount", artifactCount);
            sendArtifact.writeJabberId("sentBy", sentBy);
            sendArtifact.writeJabberIds("sentTo", "sentTo", sentTo);
            sendArtifact.writeCalendar("sentOn", sentOn);
            for (final JabberId sentToId : sentTo) {
                final IQ iq = sendArtifact.getIQ();
                setTo(iq, sentToId);
                send(sentToId, iq);
            }
            getContainerArchiveModel().backup(null);
        } catch (final Throwable t) {
            throw translateError(t);
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
    void sendArtifact(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final Integer artifactIndex, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final byte[] artifactBytes, final List<JabberId> sendTo,
            final JabberId sentBy, final Calendar sentOn) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("name", name);
        logVariable("artifactCount", artifactCount);
        logVariable("artifactIndex", artifactIndex);
        logVariable("artifactUniqueId", artifactUniqueId);
        logVariable("artifactVersionId", artifactVersionId);
        logVariable("artifactName", artifactName);
        logVariable("artifactType", artifactType);
        logVariable("artifactChecksum", artifactChecksum);
        logVariable("artifactBytes", artifactBytes);
        logVariable("sendTo", sendTo);
        logVariable("sentBy", sentBy);
        logVariable("sentOn", sentOn);
        try {
            final IQWriter sendArtifact = createIQWriter("container:artifactsent");
            sendArtifact.writeUniqueId("uniqueId", uniqueId);
            sendArtifact.writeLong("versionId", versionId);
            sendArtifact.writeString("name", name);
            sendArtifact.writeInteger("artifactCount", artifactCount);
            sendArtifact.writeInteger("artifactIndex", artifactIndex);
            sendArtifact.writeUniqueId("artifactUniqueId", artifactUniqueId);
            sendArtifact.writeLong("artifactVersionId", artifactVersionId);
            sendArtifact.writeString("artifactName", artifactName);
            sendArtifact.writeArtifactType("artifactType", artifactType);
            sendArtifact.writeString("artifactChecksum", artifactChecksum);
            sendArtifact.writeBytes("artifactBytes", artifactBytes);
            sendArtifact.writeJabberId("sentBy", sentBy);
            sendArtifact.writeCalendar("sentOn", sentOn);
            for (final JabberId sendToId : sendTo) {
                final IQ iq = sendArtifact.getIQ();
                setTo(iq, sendToId);
                send(sendToId, iq);
            }
            getArtifactArchiveModel().backup(null);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Obtain a thinkParity artifact archive interface.
     * 
     * @return A thinkParity artifact archive interface.
     */
    private ArchiveModel<ArtifactVersion> getArtifactArchiveModel() {
        return ArchiveModel.<ArtifactVersion>getModel(session);
    }

    /**
     * Obtain a thinkParity container archive interface.
     * 
     * @return A thinkParity container archive interface.
     */
    private ArchiveModel<ContainerVersion> getContainerArchiveModel() {
        return ArchiveModel.<ContainerVersion>getModel(session);
    }
}
