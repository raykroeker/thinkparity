/*
 * Created On: Sep 17, 2006 2:28:00 PM
 */
package com.thinkparity.desdemona.wildfire.handler.container;

import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Archive extends AbstractHandler {

    /** Create Archive. */
    public Archive() {
        super("container:archive");
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        archive(readJabberId("userId"), readUUID("uniqueId"));
    }

    /**
     * Archive a container.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     */
    private void archive(final JabberId userId, final UUID uniqueId) {
        getContainerModel().archive(userId, uniqueId);
    }
}
