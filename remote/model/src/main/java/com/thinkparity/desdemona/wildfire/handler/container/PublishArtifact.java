/*
 * Created On: Aug 7, 2006 11:47:07 AM
 */
package com.thinkparity.desdemona.wildfire.handler.container;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.artifact.ArtifactType;

import com.thinkparity.desdemona.model.Constants.Xml.Service;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PublishArtifact extends AbstractHandler {

    /** Create PublishArtifact. */
    public PublishArtifact() { super(Service.Container.PUBLISH_ARTIFACT); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        publishArtifact(readUUID("uniqueId"), readLong("versionId"),
                readString("name"), readInteger("artifactCount"),
                readInteger("artifactIndex"), readUUID("artifactUniqueId"),
                readLong("artifactVersionId"), readString("artifactName"),
                readArtifactType("artifactType"),
                readString("artifactChecksum"), readByteArray("artifactBytes"),
                readJabberIds("publishTo", "publishTo"),
                readJabberId("publishedBy"), readCalendar("publishedOn"));
    }

    /**
     * Publish the artifact version.
     * 
     * @param uniqueId
     *            The container unique id.
     * @param versionId
     *            The container version id.
     * @param name
     *            The container name.
     * @param artifactCount
     *            The number of artifacts in the container version.
     * @param artifactIndex
     *            The index of this artifact in the container version.
     * @param artifactUniqueId
     *            The artifact unique id.
     * @param artifactVersionId
     *            The artifact version id.
     * @param artifactName
     *            The artifact name.
     * @param artifactType
     *            The artifact type.
     * @param artifactChecksum
     *            The artifact checksum.
     * @param artifactBytes
     *            The artifact bytes.
     * @param publishTo
     *            To whom the artifact should be published.
     * @param publishedBy
     *            By whom the artifact was published.
     * @param publishedOn
     *            When the artifact was published.
     */
    private void publishArtifact(final UUID uniqueId,
            final Long versionId, final String name, final Integer artifactCount,
            final Integer artifactIndex, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final byte[] artifactBytes, final List<JabberId> publishTo,
            final JabberId publishedBy, final Calendar publishedOn) {
        getContainerModel().publishArtifact(uniqueId, versionId, name,
                artifactCount, artifactIndex, artifactUniqueId,
                artifactVersionId, artifactName, artifactType,
                artifactChecksum, artifactBytes, publishTo, publishedBy,
                publishedOn);
    }
}
