/*
 * Created On: Aug 7, 2006 11:47:07 AM
 */
package com.thinkparity.server.handler.container;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.Constants.Xml.Service;
import com.thinkparity.server.handler.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Publish extends AbstractController {

    /** Create Publish. */
    public Publish() { super(Service.Container.PUBLISH); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logApiId();
        publish(readUUID("uniqueId"), readLong("versionId"),
                readString("name"), readInteger("artifactCount"),
                readJabberId("publishedBy"),
                readJabberIds("publishedTo", "publishedTo"),
                readCalendar("publishedOn"));
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
     *            A container version artifact count.
     * @param publishedBy
     *            By whom the container was published.
     * @param publishedTo
     *            To whom the container was published.
     * @param publishedOn
     *            When the container was published.
     */
    private void publish(final UUID uniqueId, final Long versionId,
            final String name, final Integer artifactCount,
            final JabberId publishedBy, final List<JabberId> publishedTo,
            final Calendar publishedOn) {
        getContainerModel().publish(uniqueId, versionId, name, artifactCount,
                publishedBy, publishedTo, publishedOn);
    }
}
