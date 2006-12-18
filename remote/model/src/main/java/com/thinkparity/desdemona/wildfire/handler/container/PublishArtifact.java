/*
 * Created On: Aug 7, 2006 11:47:07 AM
 */
package com.thinkparity.desdemona.wildfire.handler.container;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactType;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PublishArtifact extends AbstractHandler {

    /**
     * Create PublishArtifact.
     *
     */
    public PublishArtifact() {
        super("container:publishartifact");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider,
     *      com.thinkparity.desdemona.util.service.ServiceRequestReader,
     *      com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     * 
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        logger.logApiId();
        publishArtifact(provider, reader.readUUID("uniqueId"),
                reader.readLong("versionId"), reader.readString("name"),
                reader.readInteger("artifactCount"),
                reader.readInteger("artifactIndex"),
                reader.readUUID("artifactUniqueId"),
                reader.readLong("artifactVersionId"),
                reader.readString("artifactName"),
                reader.readArtifactType("artifactType"),
                reader.readString("artifactChecksum"),
                reader.readString("artifactStreamId"),
                reader.readJabberIds("publishTo", "publishTo"),
                reader.readJabberId("publishedBy"),
                reader.readCalendar("publishedOn"));
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
     * @param artifactStreamId
     *            A stream id.
     * @param publishTo
     *            To whom the artifact should be published.
     * @param publishedBy
     *            By whom the artifact was published.
     * @param publishedOn
     *            When the artifact was published.
     */
    private void publishArtifact(final ServiceModelProvider context,
            final UUID uniqueId, final Long versionId, final String name,
            final Integer artifactCount, final Integer artifactIndex,
            final UUID artifactUniqueId, final Long artifactVersionId,
            final String artifactName, final ArtifactType artifactType,
            final String artifactChecksum, final String artifactStreamId,
            final List<JabberId> publishTo, final JabberId publishedBy,
            final Calendar publishedOn) {
        context.getContainerModel().publishArtifact(uniqueId, versionId, name,
                artifactCount, artifactIndex, artifactUniqueId,
                artifactVersionId, artifactName, artifactType,
                artifactChecksum, artifactStreamId, publishTo, publishedBy,
                publishedOn);
    }
}
