/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.container;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadArchive extends AbstractHandler {

    /** Create ReadArchive. */
    public ReadArchive() {
        super("container:readarchive");
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        final List<Container> containers = readArchive(readJabberId("userId"));
        writeContainers("containers", "container", containers);
    }

    /**
     * Read a list of containers from the archive.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    private List<Container> readArchive(final JabberId userId) {
        return getContainerModel().readArchive(userId);
    }
}
