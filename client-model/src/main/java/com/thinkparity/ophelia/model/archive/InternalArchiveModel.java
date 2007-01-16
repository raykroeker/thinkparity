/*
 * Generated On: Sep 01 06 10:06:21 AM
 */
package com.thinkparity.ophelia.model.archive;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityTransaction;
import com.thinkparity.codebase.model.util.jta.TransactionType;

/**
 * <b>Title:</b>thinkParity Archive Internal Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
@ThinkParityTransaction(TransactionType.REQUIRED)
public interface InternalArchiveModel extends ArchiveModel {

    /**
     * Archive an artifact.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     */
    public void archive(final Long artifactId);

    /**
     * Open a document version input stream.
     * 
     * @param uniqueId
     *            A document unique id <code>UUID</code>.
     * @param versionId
     *            A document version id <code>Long</code>.
     * @return An <code>InputStream</code>.
     */
    public InputStream openDocumentVersion(final UUID uniqueId,
            final Long versionId);

    /**
     * Read the team ids.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return A <code>List</code> of <code>JabberId</code>s.
     */
    public List<JabberId> readTeamIds(final UUID uniqueId);

    /**
     * Restore an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    @ThinkParityTransaction(TransactionType.REQUIRES_NEW)
    public void restore(final UUID uniqueId);
}
