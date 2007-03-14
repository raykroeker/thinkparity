/*
 * Created On:  9-Dec-06 9:20:46 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface PopupDelegate extends TabPanelPopupDelegate {

    /**
     * Show a popup for a contact.
     * 
     * @param contact
     *            A <code>Contact</code>.
     * @param expanded
     *            A <code>boolean</code>.     
     */
    public void showForContact(final Contact contact, final boolean expanded);

    /**
     * Show a popup for a contact invitation.
     * 
     * @param invitation
     *            An <code>IncomingEMailInvitation</code>.
     */
    public void showForInvitation(final IncomingEMailInvitation invitation);

    /**
     * Show a popup for a contact invitation.
     * 
     * @param invitation
     *            An <code>IncomingUserInvitation</code>.
     */
    public void showForInvitation(final IncomingUserInvitation invitation);

    /**
     * Show a popup for an outgoing e-mail invitation.
     * 
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     */
    public void showForInvitation(final OutgoingEMailInvitation invitation);

    /**
     * Show a popup for an outgoing e-mail invitation.
     * 
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>.
     */
    public void showForInvitation(final OutgoingUserInvitation invitation);

    /**
     * Show a popup for a profile.
     * 
     * @param profile
     *            A <code>Profile</code>.
     * @param expanded
     *            A <code>boolean</code>.     
     */
    public void showForProfile(final Profile profile, final boolean expanded);
}
