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
public class SendArtifact extends AbstractHandler {

    /** Create SendVersion. */
    public SendArtifact() { super(Service.Container.SEND_ARTIFACT); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        sendArtifact(readUUID("uniqueId"), readLong("versionId"),
                readString("name"), readInteger("artifactCount"),
                readInteger("artifactIndex"), readUUID("artifactUniqueId"),
                readLong("artifactVersionId"), readString("artifactName"),
                readArtifactType("artifactType"),
                readString("artifactChecksum"), readByteArray("artifactBytes"),
                readJabberIds("sendTo", "sendTo"), readJabberId("sentBy"),
                readCalendar("sentOn"));
    }

    /**
     * Send a container version's artifact version.
     * 
     * @param uniqueId
     *            The container unique id.
     * @param versionId
     *            The container version id.
     * @param name
     *            The container name.
     * @param artifactCount
     *            The artifact count.
     * @param artifactIndex
     *            The artifact index.
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
     * @param sendTo
     *            To whom to send the artifact.
     * @param sentBy
     *            Whom the artifact was sent by.
     * @param sentOn
     *            When the artifact was sent.
     */
    private void sendArtifact(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final Integer artifactIndex, final UUID artifactUniqueId,
            final Long artifactVersionId, final String artifactName,
            final ArtifactType artifactType, final String artifactChecksum,
            final byte[] artifactBytes, final List<JabberId> sendTo,
            final JabberId sentBy, final Calendar sentOn) {
        getContainerModel().sendArtifact(uniqueId, versionId, name,
                artifactCount, artifactIndex, artifactUniqueId,
                artifactVersionId, artifactName, artifactType,
                artifactChecksum, artifactBytes, sendTo, sentBy, sentOn);
    }
}
