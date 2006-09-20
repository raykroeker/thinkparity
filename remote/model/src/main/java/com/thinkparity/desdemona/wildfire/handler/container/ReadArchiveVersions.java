/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.container;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadArchiveVersions extends AbstractHandler {

    /** Create ReadArchive. */
    public ReadArchiveVersions() {
        super("container:readarchiveversions");
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        final List<ContainerVersion> versions =
            readArchiveVersions(readJabberId("userId"), readUUID("uniqueId"));
        writeContainerVersions("containerVersions", "containerVersions", versions);
    }

    /**
     * Read a list of container versions from the archive.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    private List<ContainerVersion> readArchiveVersions(final JabberId userId,
            final UUID uniqueId) {
        return getContainerModel().readArchiveVersions(userId, uniqueId);
    }
}
