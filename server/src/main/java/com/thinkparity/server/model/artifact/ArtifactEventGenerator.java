/*
 * Created On: Aug 1, 2006 6:18:53 PM
 */
package com.thinkparity.server.model.artifact;

import java.util.UUID;

import org.xmpp.packet.IQ;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.EventGenerator;
import com.thinkparity.model.xmpp.IQWriter;

import com.thinkparity.server.Constants.Xml;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ArtifactEventGenerator extends EventGenerator {

    /** Create ArtifactEventGenerator. */
    ArtifactEventGenerator() { super(); }

    /**
     * Generate the internet query for the event with a unique id.
     * 
     * @param eventName
     *            The event name.
     * @param uniqueId
     *            The unique id.
     * @return The internet query.
     */
    IQ generate(final String eventName, final UUID uniqueId) {
        final IQWriter iqWriter = createEventWriter(eventName);
        iqWriter.writeUniqueId(Xml.Artifact.UNIQUE_ID, uniqueId);
        return iqWriter.getIQ();
    }

    /**
     * Generate the internet query for the event with a unique id and a jabber
     * id.
     * 
     * @param eventName
     *            The event name.
     * @param uniqueId
     *            The unique id.
     * @param jabberId
     *            The jabber id.
     * @return The internet query.
     */
    IQ generate(final String eventName, final UUID uniqueId,
            final JabberId jabberId) {
        final IQWriter iqWriter = createEventWriter(eventName);
        iqWriter.writeUniqueId(Xml.Artifact.UNIQUE_ID, uniqueId);
        iqWriter.writeJabberId(Xml.User.JABBER_ID, jabberId);
        return iqWriter.getIQ();
    }
}
