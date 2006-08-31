/*
 * Created On: Aug 14, 2006 2:19:09 PM
 */
package com.thinkparity.model.parity.api.events;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ContactListener {
    public void contactDeleted(final ContactEvent e);
    public void contactUpdated(final ContactEvent e);
    public void incomingInvitationAccepted(final ContactEvent e);
    public void incomingInvitationCreated(final ContactEvent e);
    public void incomingInvitationDeclined(final ContactEvent e);
    public void incomingInvitationDeleted(final ContactEvent e);
    public void outgoingInvitationAccepted(final ContactEvent e);
    public void outgoingInvitationCreated(final ContactEvent e);
    public void outgoingInvitationDeclined(final ContactEvent e);
    public void outgoingInvitationDeleted(final ContactEvent e);
}
