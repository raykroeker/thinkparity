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
public class Send extends AbstractController {

    /** Create SendVersion. */
    public Send() { super("container:send"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logApiId();
        sendArtifact(readJabberId(Xml.Container.SENT_BY),
                readCalendar(Xml.Container.SENT_ON),
                readJabberId(Xml.User.JABBER_ID),
                readUUID(Xml.Container.CONTAINER_UNIQUE_ID),
                readLong(Xml.Container.CONTAINER_VERSION_ID),
                readString(Xml.Container.CONTAINER_NAME),
                readInteger(Xml.Container.ARTIFACT_COUNT),
                readInteger(Xml.Container.ARTIFACT_INDEX),
                readUUID(Xml.Artifact.UNIQUE_ID),
                readLong(Xml.Artifact.VERSION_ID),
                readString(Xml.Artifact.NAME),
                readArtifactType(Xml.Artifact.TYPE),
                readByteArray(Xml.Artifact.BYTES));
    }

    /**
     * Send the artifact version.
     * 
     * @param jabberId
     *            The jabber id.
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
    private void sendArtifact(final JabberId sentBy, final Calendar sentOn,
            final JabberId jabberId, final UUID uniqueId, final Long versionId,
            final String name, final Integer count, final Integer index,
            final UUID artifactUniqueId, final Long artifactVersionId,
            final String artifactName, final ArtifactType type,
            final byte[] bytes) {
        getContainerModel().sendArtifact(sentBy, sentOn, jabberId, uniqueId,
                versionId, name, count, index, artifactUniqueId,
                artifactVersionId, artifactName, type, bytes);
    }
}
