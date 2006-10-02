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

    public void handleReceived(final UUID uniqueId, final Long versionId,
            final JabberId receivedBy, final Calendar receivedOn);

    public void handleDraftCreated(final UUID uniqueId,
            final JabberId createdBy, final Calendar createdOn);

    public void handleDraftDeleted(final UUID uniqueId,
            final JabberId deletedBy, final Calendar deletedOn);

	public void teamMemberAdded(final UUID uniqueId, final JabberId jabberId);

    public void teamMemberRemoved(final UUID artifactUniqueId,
			final JabberId jabberId);
}
