/*
 * Created On: Aug 14, 2006 2:19:19 PM
 */
package com.thinkparity.ophelia.model.events;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContactAdapter implements ContactListener {

    /**
     * @see com.thinkparity.ophelia.model.events.ContactListener#contactDeleted(com.thinkparity.ophelia.model.events.ContactEvent)
     */
    public void contactDeleted(final ContactEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContactListener#contactUpdated(com.thinkparity.ophelia.model.events.ContactEvent)
     */
    public void contactUpdated(final ContactEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContactListener#incomingInvitationAccepted(com.thinkparity.ophelia.model.events.ContactEvent)
     */
    public void incomingInvitationAccepted(final ContactEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContactListener#incomingInvitationCreated(com.thinkparity.ophelia.model.events.ContactEvent)
     */
    public void incomingInvitationCreated(final ContactEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContactListener#incomingInvitationDeclined(com.thinkparity.ophelia.model.events.ContactEvent)
     */
    public void incomingInvitationDeclined(final ContactEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContactListener#incomingInvitationDeleted(com.thinkparity.ophelia.model.events.ContactEvent)
     */
    public void incomingInvitationDeleted(final ContactEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContactListener#outgoingInvitationAccepted(com.thinkparity.ophelia.model.events.ContactEvent)
     */
    public void outgoingInvitationAccepted(final ContactEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContactListener#outgoingInvitationCreated(com.thinkparity.ophelia.model.events.ContactEvent)
     */
    public void outgoingInvitationCreated(final ContactEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContactListener#outgoingInvitationDeclined(com.thinkparity.ophelia.model.events.ContactEvent)
     */
    public void outgoingInvitationDeclined(final ContactEvent e) {}

    /**
     * @see com.thinkparity.ophelia.model.events.ContactListener#outgoingInvitationDeleted(com.thinkparity.ophelia.model.events.ContactEvent)
     */
    public void outgoingInvitationDeleted(final ContactEvent e) {}
}
