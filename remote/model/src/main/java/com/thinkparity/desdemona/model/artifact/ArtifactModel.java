/*
 * Created On: Nov 29, 2005
 * $Id$
 */
package com.thinkparity.desdemona.model.artifact;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.DraftExistsException;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ArtifactModel {

    /**
     * Confrim an artifact receipt for the model user.
     * 
     * @param uniqueId
     * @param versionId
     * @param publishedBy
     * @param publishedOn
     * @param publishedTo
     * @param receivedOn
     */
    public void confirmReceipt(final UUID uniqueId, final Long versionId,
            final JabberId publishedBy, final Calendar publishedOn,
            final List<JabberId> publishedTo, final Calendar receivedOn);

    /**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            The artifact unique id.
     */
    public void createDraft(final List<JabberId> team, final UUID uniqueId,
            final Calendar createdOn) throws DraftExistsException;

	/**
     * Delete a draft from an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    public void deleteDraft(final List<JabberId> team, final UUID uniqueId,
            final Calendar deletedOn);

    /**
     * Read the key holder for an artifact.
     * 
     * @param userId
     *            The user id <code>JabberId</code>.
     * @param uniqueId
     *            The artifact unique id <code>UUID</code>.
     * @return The artifact key holder <code>JabberId</code>.
     */
    public JabberId readKeyHolder(final UUID uniqueId);
}
