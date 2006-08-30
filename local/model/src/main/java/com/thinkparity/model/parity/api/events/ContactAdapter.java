/*
 * Created On: Aug 14, 2006 2:19:19 PM
 */
package com.thinkparity.model.parity.api.events;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContactAdapter implements ContactListener {

    /**
     * @see com.thinkparity.model.parity.api.events.ContactListener#contactDeleted(com.thinkparity.model.parity.api.events.ContactEvent)
     */
    public void contactDeleted(final ContactEvent e) {}

    /**
     * @see com.thinkparity.model.parity.api.events.ContactListener#incomingInvitationAccepted(com.thinkparity.model.parity.api.events.ContactEvent)
     */
    public void incomingInvitationAccepted(final ContactEvent e) {}

    /**
     * @see com.thinkparity.model.parity.api.events.ContactListener#incomingInvitationCreated(com.thinkparity.model.parity.api.events.ContactEvent)
     */
    public void incomingInvitationCreated(final ContactEvent e) {}

    /**
     * @see com.thinkparity.model.parity.api.events.ContactListener#incomingInvitationDeclined(com.thinkparity.model.parity.api.events.ContactEvent)
     */
    public void incomingInvitationDeclined(final ContactEvent e) {}

    /**
     * @see com.thinkparity.model.parity.api.events.ContactListener#incomingInvitationDeleted(com.thinkparity.model.parity.api.events.ContactEvent)
     */
    public void incomingInvitationDeleted(final ContactEvent e) {}

    /**
     * @see com.thinkparity.model.parity.api.events.ContactListener#outgoingInvitationAccepted(com.thinkparity.model.parity.api.events.ContactEvent)
     */
    public void outgoingInvitationAccepted(final ContactEvent e) {}

    /**
     * @see com.thinkparity.model.parity.api.events.ContactListener#outgoingInvitationCreated(com.thinkparity.model.parity.api.events.ContactEvent)
     */
    public void outgoingInvitationCreated(final ContactEvent e) {}

    /**
     * @see com.thinkparity.model.parity.api.events.ContactListener#outgoingInvitationDeclined(com.thinkparity.model.parity.api.events.ContactEvent)
     */
    public void outgoingInvitationDeclined(final ContactEvent e) {}

    /**
     * @see com.thinkparity.model.parity.api.events.ContactListener#outgoingInvitationDeleted(com.thinkparity.model.parity.api.events.ContactEvent)
     */
    public void outgoingInvitationDeleted(final ContactEvent e) {}
}
