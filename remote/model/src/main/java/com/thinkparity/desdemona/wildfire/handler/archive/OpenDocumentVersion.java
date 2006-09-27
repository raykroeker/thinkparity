/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.archive;

import java.io.InputStream;
import java.util.UUID;

import com.thinkparity.codebase.StreamUtil;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class OpenDocumentVersion extends AbstractHandler {

    /** Create ReadArchive. */
    public OpenDocumentVersion() {
        super("archive:opendocumentversion");
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        InputStream inputStream = null;
        try {
            inputStream = openDocumentVersionStream(readJabberId("userId"), readUUID("uniqueId"), readLong("versionId"));
            writeBytes("content", StreamUtil.read(inputStream));
        } catch (final Throwable t) {
            throw translateError(t);
        } finally {
            try {
                inputStream.close();
            } catch (final Throwable t) {
                throw translateError(t);
            }
            inputStream = null;
        }
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
    private InputStream openDocumentVersionStream(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        return getArchiveModel().openDocumentVersionStream(userId, uniqueId, versionId);
    }

    private RuntimeException translateError(final Throwable t) {
        throw new RuntimeException(t);
    }
}
