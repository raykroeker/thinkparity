/*
 * Created On: Aug 14, 2006 2:28:15 PM
 */
package com.thinkparity.model.parity.model.contact;

import com.thinkparity.model.parity.api.events.ContactEvent;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ContactEventGenerator {

    /** The source to use when generating events. */
    private final ContactEvent.Source source;

    /**
     * Create ContainerEventGenerator.
     * 
     * @param source
     *            The container event source.
     */
    ContactEventGenerator(final ContactEvent.Source source) {
        super();
        this.source = source;
    }

    /**
     * Generate a container event for a container.
     * 
     * @param contact
     *            A contact.
     * @return A container event.
     */
    ContactEvent generate(final Contact contact) {
        return new ContactEvent(source, contact);
    }

    ContactEvent generate(final Contact contact,
            final IncomingInvitation incomingInvitation) {
        return new ContactEvent(source, contact, incomingInvitation);
    }

    ContactEvent generate(final Contact contact,
            final OutgoingInvitation outgoingInvitation) {
        return new ContactEvent(source, contact, outgoingInvitation);
    }

    /**
     * Generate a container event for a container and a draft.
     * 
     * @param incomingInvitation
     *            An incoming invitationb.
     * @return A container event.
     */
    ContactEvent generate(final IncomingInvitation incomingInvitation) {
        return new ContactEvent(source, incomingInvitation);
    }

    /**
     * Generate a container event for a container and a draft.
     * 
     * @param outgoingInvitation
     *            An outgoing invitation.
     * @return A container event.
     */
    ContactEvent generate(final OutgoingInvitation outgoingInvitation) {
        return new ContactEvent(source, outgoingInvitation);
    }
}
