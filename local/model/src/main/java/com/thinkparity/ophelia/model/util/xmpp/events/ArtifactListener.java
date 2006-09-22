/*
 * Created On:  Mar 30, 2006
 * $Id$
 */
package com.thinkparity.ophelia.model.util.xmpp.events;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.codebase.event.EventListener;
import com.thinkparity.codebase.jabber.JabberId;



/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface ArtifactListener extends EventListener {

    /**
     * Confirm that an artifact was received.
     *
     * @param uniqueId
     *      The artifact unique id.
     * @param versionId
     *      The artifact version id.
     * @param receivedFrom
     *      From whom the the confirmation was sent.
     */
    public void confirmReceipt(final UUID uniqueId, final Long versionId,
            final JabberId receivedFrom);

    public void handleDraftCreated(final UUID uniqueId,
            final JabberId createdBy, final Calendar createdOn);

    public void handleDraftDeleted(final UUID uniqueId,
            final JabberId deletedBy, final Calendar deletedOn);

	public void teamMemberAdded(final UUID uniqueId, final JabberId jabberId);

    public void teamMemberRemoved(final UUID artifactUniqueId,
			final JabberId jabberId);
}
