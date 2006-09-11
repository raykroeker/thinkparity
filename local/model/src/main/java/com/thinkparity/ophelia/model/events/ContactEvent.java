/*
 * Created On: Jun 29, 2006 8:58:06 AM
 */
package com.thinkparity.ophelia.model.events;

import com.thinkparity.codebase.model.contact.Contact;

import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.4.3
 */
public class ContactEvent {

    /** A contact. */
    private final Contact contact;

    /** An incoming invitation. */
    private final IncomingInvitation incomingInvitation;

    /** An outgoing invitation. */
    private final OutgoingInvitation outgoingInvitation;

    /** The event source. */
    private final Source source;

    /**
     * Create ContainerEvent.
     * 
     * @param source
     *            The event source
     * @param contact
     *            A contact.
     */
    public ContactEvent(final Source source, final Contact contact) {
        this(source, contact, null, null);
    }

    /**
     * Create ContactEvent.
     * 
     * @param source
     *            The event source
     * @param incomingInvitation
     *            An incoming invitation.
     */
    public ContactEvent(final Source source,
            final IncomingInvitation incomingInvitation) {
        this(source, null, incomingInvitation, null);
    }

    /**
     * Create ContactEvent.
     * 
     * @param source
     *            The event source
     * @param contact
     *            A contact.
     * @param incomingInvitation
     *            An incoming invitation.
     */
    public ContactEvent(final Source source, final Contact contact,
            final IncomingInvitation incomingInvitation) {
        this(source, contact, incomingInvitation, null);
    }

    /**
     * Create ContactEvent.
     * 
     * @param source
     *            The event source
     * @param contact
     *            A contact.
     * @param outgoingInvitation
     *            An outgoing invitation.
     */
    public ContactEvent(final Source source, final Contact contact,
            final OutgoingInvitation outgoingInvitation) {
        this(source, contact, null, outgoingInvitation);
    }
    /**
     * Create ContainerEvent.
     * 
     * @param source
     *            The event source
     * @param incomingInvitation
     *            An incoming invitation.
     */
    public ContactEvent(final Source source,
            final OutgoingInvitation outgoingInvitation) {
        this(source, null, null, outgoingInvitation);
    }

    /**
     * Create ContainerEvent.
     * 
     * @param source
     *            The event source
     * @param container
     *            The container.
     * @param draft
     *            The draft.
     * @param version
     *            The version.
     * @param teamMember
     *            The teamMember.
     * @param document
     *            A document.
     */
    private ContactEvent(final Source source, final Contact contact,
            final IncomingInvitation incomingInvitation,
            final OutgoingInvitation outgoingInvitation) {
        super();
        this.source = source;
        this.contact = contact;
        this.incomingInvitation = incomingInvitation;
        this.outgoingInvitation = outgoingInvitation;
    }


    /**
     * Obtain the contact
     *
     * @return The Contact.
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Obtain the incomingInvitation
     *
     * @return The IncomingInvitation.
     */
    public IncomingInvitation getIncomingInvitation() {
        return incomingInvitation;
    }

    /**
     * Obtain the outgoingInvitation
     *
     * @return The OutgoingInvitation.
     */
    public OutgoingInvitation getOutgoingInvitation() {
        return outgoingInvitation;
    }

    /**
     * Determine if the event is a local event.
     * 
     * @return True if the event is a local event.
     */
    public Boolean isLocal() { return Source.LOCAL == source; }

    /**
     * Determine if the event is a remote event.
     * 
     * @return True if the event is a remote event.
     */
    public Boolean isRemote() { return Source.REMOTE == source; }

    /** A container event source. */
    public enum Source { LOCAL, REMOTE }
}
