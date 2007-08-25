/*
 * Created On:  17-Jan-07 3:40:57 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.ContactInvitation;

/**
 * <b>Title:</b>thinkParity Contact Panel Id<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContactPanelId {

    /**
     * Create a contact panel id.
     * 
     * @param contactId
     *            A contact id <code>Long</code>.
     * @return A <code>ContactPanelId</code>.
     */
    public static ContactPanelId newContactPanelId(final Long contactId) {
        return new ContactPanelId(contactId, null);
    }

    /**
     * Create an invitation contact panel id.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     * @return A <code>ContactPanelId</code>.
     */
    public static ContactPanelId newInvitationPanelId(final Long invitationId) {
        return new ContactPanelId(null, invitationId);
    }

    /**
     * Create a contact panel id.
     * 
     * @param contact
     *            A <code>Contact</code>.
     * @return A <code>ContactPanelId</code>.
     */
    public static ContactPanelId newPanelId(final Contact contact) {
        return new ContactPanelId(contact.getLocalId(), null);
    }

    /**
     * Create an invitation contact panel id.
     * 
     * @param invitation
     *            A <code>ContactInvitation</code>.
     * @return A <code>ContactPanelId</code>.
     */
    public static ContactPanelId newPanelId(final ContactInvitation invitation) {
        return new ContactPanelId(null, invitation.getId());
    }

    /** The contact id <code>Long</code>. */
    private final Long contactId;

    /** The invitation id <code>Long</code>. */
    private final Long invitationId;

    /**
     * Create ContactPanelId.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    private ContactPanelId(final Long contactId, final Long invitationId) {
        super();
        this.contactId = contactId;
        this.invitationId = invitationId;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     */
    @Override
    public boolean equals(final Object obj) {
        if (null == obj)
            return false;
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        final ContactPanelId contactPanelId = (ContactPanelId) obj;
        if (isSetContactId() && !contactPanelId.isSetContactId())
            return false;
        else if (isSetInvitationId() && !contactPanelId.isSetInvitationId())
            return false;
        else if (isSetContactId() && contactPanelId.isSetContactId())
            return contactId.equals(contactPanelId.contactId);
        else if (isSetInvitationId() && contactPanelId.isSetInvitationId())
            return invitationId.equals(contactPanelId.invitationId);
        return true;
    }

    /**
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        if (isSetContactId())
            return contactId.toString().hashCode();
        else if (isSetInvitationId())
            return invitationId.toString().hashCode();
        else
            throw Assert.createUnreachable("Unknown contact panel id type.");
    }

    /**
     * Obtain the contact id.
     * 
     * @return A contact id <code>JabberId</code>.
     */
    Long getContactId() {
        return contactId;
    }

    /**
     * Obtain the invitation id.
     * 
     * @return An invitation id <code>Long</code>.
     */
    Long getInvitationId() {
        return invitationId;
    }

    /**
     * Determine if the contact id is set.
     * 
     * @return True if the contact id is set.
     */
    Boolean isSetContactId() {
        return null != contactId;
    }

    /**
     * Determine if the invitation id is set.
     * 
     * @return True if the invitation id is set.
     */
    Boolean isSetInvitationId() {
        return null != invitationId;
    }
}
