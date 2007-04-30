/*
 * Created On:  Mar 30, 2006
 * $Id$
 */
package com.thinkparity.ophelia.model.util.xmpp.event;

import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftCreatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent;

/**
 * <b>Title:</b>thinkParity XMPP Artifact Listener<br>
 * <b>Description:</b>An event listener monitoring for remote xmpp events.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ArtifactListener extends XMPPEventListener {
    public void handleDraftCreated(final ArtifactDraftCreatedEvent event);
    public void handleDraftDeleted(final ArtifactDraftDeletedEvent event);
    public void handleReceived(final ArtifactReceivedEvent event);
	public void handleTeamMemberAdded(final ArtifactTeamMemberAddedEvent event);
    public void handleTeamMemberRemoved(final ArtifactTeamMemberRemovedEvent event);
}
