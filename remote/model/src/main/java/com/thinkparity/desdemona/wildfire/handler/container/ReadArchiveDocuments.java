/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.container;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadArchiveDocuments extends AbstractHandler {

    /** Create ReadArchive. */
    public ReadArchiveDocuments() {
        super("container:readarchivedocuments");
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        final List<Document> documents = readArchiveDocuments(
                readJabberId("userId"), readUUID("uniqueId"),
                readLong("versionId"));
        marshalDocuments(documents);
    }

    /**
     * Read a list of documents from the archive.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List&lt;Container&gt;</code>.
     */
    private List<Document> readArchiveDocuments(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        return getContainerModel().readArchiveDocuments(userId, uniqueId, versionId);
    }
}
