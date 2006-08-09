/*
 * Created On: Aug 1, 2006 6:18:53 PM
 */
package com.thinkparity.server.model.container;

import java.util.Calendar;
import java.util.UUID;

import org.xmpp.packet.IQ;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.EventGenerator;
import com.thinkparity.model.artifact.ArtifactType;
import com.thinkparity.model.xmpp.IQWriter;

import com.thinkparity.server.ParityServerConstants.Xml;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ContainerEventGenerator extends EventGenerator {

    /** Create ArtifactEventGenerator. */
    ContainerEventGenerator() { super(); }

    /**
     * Generate the internet query for a publish event.
     * 
     * @return The internet query.
     */
    IQ generatePublishEvent(final UUID uniqueId, final Long versionId,
            final Integer count, final Integer index,
            final UUID artifactUniqueId, final Long artifactVersionId,
            final ArtifactType artifactType, final byte[] artifactBytes,
            final JabberId publishedBy, final Calendar publishedOn) {
        final IQWriter iqWriter = createEventWriter(Xml.Event.Container.ARTIFACT_PUBLISHED);
        iqWriter.writeJabberId(Xml.Container.PUBLISHED_BY, publishedBy);
        iqWriter.writeCalendar(Xml.Container.PUBLISHED_ON, publishedOn);
        iqWriter.writeUniqueId(Xml.Container.CONTAINER_UNIQUE_ID, uniqueId);
        iqWriter.writeLong(Xml.Container.CONTAINER_VERSION_ID, versionId);
        iqWriter.writeInteger(Xml.Container.ARTIFACT_COUNT, count);
        iqWriter.writeInteger(Xml.Container.ARTIFACT_INDEX, index);
        iqWriter.writeUniqueId(Xml.Artifact.UNIQUE_ID, artifactUniqueId);
        iqWriter.writeLong(Xml.Artifact.VERSION_ID, artifactVersionId);
        iqWriter.writeArtifactType(Xml.Artifact.TYPE, artifactType);
        iqWriter.writeBytes(Xml.Artifact.BYTES, artifactBytes);
        return iqWriter.getIQ();
    }
}
