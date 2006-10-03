/*
 * Created On: Aug 7, 2006 11:47:07 AM
 */
package com.thinkparity.desdemona.wildfire.handler.container;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.model.Constants.Xml.Service;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Send extends AbstractHandler {

    /** Create SendVersion. */
    public Send() { super(Service.Container.SEND); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        sendArtifact(readUUID("uniqueId"), readLong("versionId"),
                readString("name"), readInteger("artifactCount"),
                readJabberId("sentBy"), readJabberIds("sentTo", "sentTo"),
                readCalendar("sentOn"));
    }

    /**
     * Send a container version.
     * 
     * @param uniqueId
     *            A container unique id.
     * @param versionId
     *            A container version id.
     * @param name
     *            A container name.
     * @param artifactCount
     *            The number of artifacts in the version.
     * @param sentBy
     *            By whom the container version was sent.
     * @param sentTo
     *            To whom to sent the container was sent.
     * @param sentOn
     *            When the container version was sent.
     */
    private void sendArtifact(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final JabberId sentBy, final List<JabberId> sentTo,
            final Calendar sentOn) {
        getContainerModel().send(uniqueId, versionId, name, artifactCount,
                sentBy, sentTo, sentOn);
    }
}
