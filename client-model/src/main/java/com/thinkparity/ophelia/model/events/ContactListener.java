/*
 * Created On: Aug 14, 2006 2:19:09 PM
 */
package com.thinkparity.ophelia.model.events;

import com.thinkparity.codebase.event.EventListener;

/**
 * <b>Title:</b>thinkParity OpheliaModel Contact Event Listener<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ContactListener extends EventListener {
    public void contactDeleted(final ContactEvent e);
    public void contactUpdated(final ContactEvent e);
    public void incomingInvitationAccepted(final ContactEvent e);
    public void incomingInvitationCreated(final ContactEvent e);
    public void incomingInvitationDeclined(final ContactEvent e);
    public void incomingInvitationDeleted(final ContactEvent e);
    public void outgoingEMailInvitationAccepted(final ContactEvent e);
    public void outgoingEMailInvitationCreated(final ContactEvent e);
    public void outgoingEMailInvitationDeclined(final ContactEvent e);
    public void outgoingEMailInvitationDeleted(final ContactEvent e);
    public void outgoingUserInvitationAccepted(final ContactEvent e);
    public void outgoingUserInvitationCreated(final ContactEvent e);
    public void outgoingUserInvitationDeclined(final ContactEvent e);
    public void outgoingUserInvitationDeleted(final ContactEvent e);
}
