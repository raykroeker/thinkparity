/*
 * Created On:  9-Dec-06 9:20:29 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingInvitation;
import com.thinkparity.codebase.model.profile.Profile;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ActionDelegate {

    /**
     * Invoke an action for a contact.
     * 
     * @param contact
     *            A <code>Contact</code>.
     */
    public void invokeForContact(final Contact contact);
    
    /**
     * Invoke an action for an incoming invitation.
     * 
     * @param invitation
     *            An <code>IncomingEMailInvitation</code>.
     */
    public void invokeForInvitation(final IncomingEMailInvitation invitation,
            final Action action);

    /**
     * Invoke an action for an incoming invitation.
     * 
     * @param invitation
     *            An <code>IncomingUserInvitation</code>.
     */
    public void invokeForInvitation(final IncomingUserInvitation invitation,
            final Action action);

    /**
     * Invoke an action for an outgoing invitation.
     * 
     * @param invitation
     *            An <code>OutgoingInvitation</code>.
     */
    public void invokeForInvitation(final OutgoingInvitation invitation);

    /**
     * Invoke an action for a profile.
     * 
     * @param profile
     *            A <code>Profile</code>.
     */
    public void invokeForProfile(final Profile profile);
    
    public enum Action { ACCEPT, DECLINE }
}
