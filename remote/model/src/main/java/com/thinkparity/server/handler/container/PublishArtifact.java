/*
 * Created On: Aug 7, 2006 11:47:07 AM
 */
package com.thinkparity.server.handler.container;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.artifact.ArtifactType;

import com.thinkparity.server.ParityServerConstants.Xml;
import com.thinkparity.server.handler.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PublishArtifact extends AbstractController {

    /** Create PublishArtifact. */
    public PublishArtifact() { super("container:publishartifact"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logApiId();
        publishArtifact(readUUID(Xml.Container.CONTAINER_UNIQUE_ID),
                readLong(Xml.Container.CONTAINER_VERSION_ID),
                readInteger(Xml.Container.ARTIFACT_COUNT),
                readInteger(Xml.Container.ARTIFACT_INDEX),
                readUUID(Xml.Artifact.UNIQUE_ID),
                readLong(Xml.Artifact.VERSION_ID),
                readArtifactType(Xml.Artifact.TYPE),
                readByteArray(Xml.Artifact.BYTES),
                readJabberId(Xml.Container.PUBLISHED_BY),
                readCalendar(Xml.Container.PUBLISHED_ON));
    }

    /**
     * Publish the artifact version.
     * 
     * @param containerModel
     *            The container model.
     * @param uniqueId
     *            The unique id.
     * @param versionId
     *            The version id.
     * @param count
     *            The total count.
     * @param index
     *            The index.
     * @param artifactUniqueId
     *            The artifact unique id.
     * @param artifactVersionId
     *            The artifact version id.
     * @param type
     *            The artifact type.
     * @param bytes
     *            The artifact bytes.
     */
    private void publishArtifact(final UUID uniqueId, final Long versionId,
            final Integer count, final Integer index,
            final UUID artifactUniqueId, final Long artifactVersionId,
            final ArtifactType type, final byte[] bytes,
            final JabberId publishedBy, final Calendar publishedOn) {
        getContainerModel().publishArtifact(uniqueId, versionId, count, index,
                artifactUniqueId, artifactVersionId, type, bytes, publishedBy,
                publishedOn);
    }
}
