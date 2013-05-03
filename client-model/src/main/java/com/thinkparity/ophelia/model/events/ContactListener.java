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
    public void contactCreated(final ContactEvent e);
    public void contactDeleted(final ContactEvent e);
    public void contactUpdated(final ContactEvent e);
    public void incomingEMailInvitationCreated(final ContactEvent e);
    public void incomingEMailInvitationDeclined(final ContactEvent e);
    public void incomingEMailInvitationDeleted(final ContactEvent e);
    public void incomingUserInvitationAccepted(final ContactEvent e);
    public void incomingUserInvitationCreated(final ContactEvent e);
    public void incomingUserInvitationDeclined(final ContactEvent e);
    public void incomingUserInvitationDeleted(final ContactEvent e);
    public void outgoingEMailInvitationAccepted(final ContactEvent e);
    public void outgoingEMailInvitationCreated(final ContactEvent e);
    public void outgoingEMailInvitationDeclined(final ContactEvent e);
    public void outgoingEMailInvitationDeleted(final ContactEvent e);
    public void outgoingUserInvitationAccepted(final ContactEvent e);
    public void outgoingUserInvitationCreated(final ContactEvent e);
    public void outgoingUserInvitationDeclined(final ContactEvent e);
    public void outgoingUserInvitationDeleted(final ContactEvent e);
}
