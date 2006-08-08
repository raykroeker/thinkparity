/*
 * Created On: Aug 1, 2006 6:18:53 PM
 */
package com.thinkparity.server.model.container;

import java.util.UUID;

import org.xmpp.packet.IQ;

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
     * Generate the internet query for the event with a unique id.
     * 
     * @param eventName
     *            The event name.
     * @param uniqueId
     *            The unique id.
     * @return The internet query.
     */
    IQ generate(final String eventName, final UUID uniqueId,
            final Long versionId, final UUID artifactUniqueId,
            final Long artifactVersionId, final ArtifactType artifactType,
            final byte[] artifactBytes) {
        final IQWriter iqWriter = createEventWriter(eventName);
        iqWriter.writeUniqueId(Xml.Container.CONTAINER_UNIQUE_ID, uniqueId);
        iqWriter.writeLong(Xml.Container.CONTAINER_VERSION_ID, versionId);
        iqWriter.writeUniqueId(Xml.Artifact.UNIQUE_ID, artifactUniqueId);
        iqWriter.writeLong(Xml.Artifact.VERSION_ID, artifactVersionId);
        iqWriter.writeArtifactType(Xml.Artifact.TYPE, artifactType);
        iqWriter.writeBytes(Xml.Artifact.BYTES, artifactBytes);
        return iqWriter.getIQ();
    }
}
