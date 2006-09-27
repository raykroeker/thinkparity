/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.archive;

import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadContainer extends AbstractHandler {

    /** Create ReadContainer. */
    public ReadContainer() {
        super("archive:readcontainer");
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        final Container container = readContainer(
                readJabberId("userId"), readUUID("uniqueId"));
        writeContainer("container", container);
    }

    /**
     * Read a list of containers from the archive.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>Container</code>.
     */
    private Container readContainer(final JabberId userId, final UUID uniqueId) {
        return getContainerModel().readArchive(userId, uniqueId);
    }
}
