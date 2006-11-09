/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.backup;

import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class CreateStream extends AbstractHandler {

    /** Create CreateStream. */
    public CreateStream() {
        super("backup:createstream");
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        createStream(readJabberId("userId"), readString("streamId"),
                readUUID("uniqueId"), readLong("versionId"));
    }

    /**
     * Read a list of documents from the backup.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    private void createStream(final JabberId userId, final String streamId,
            final UUID uniqueId, final Long versionId) {
        getBackupModel().createStream(userId, streamId, uniqueId, versionId);
    }
}
