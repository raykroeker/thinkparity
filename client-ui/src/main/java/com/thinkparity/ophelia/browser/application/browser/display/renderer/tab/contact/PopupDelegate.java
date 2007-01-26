/*
 * Created On:  9-Dec-06 9:20:46 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate;
import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;

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
     */
    public void showForContact(final Contact contact);

    /**
     * Show a popup for a contact invitation.
     * 
     * @param invitation
     *            An <code>IncomingInvitation</code>.
     */
    public void showForInvitation(final IncomingInvitation invitation);

    /**
     * Show a popup for a contact invitation.
     * 
     * @param invitation
     *            An <code>OutgoingInvitation</code>.
     */
    public void showForInvitation(final OutgoingInvitation invitation);

    /**
     * Show a popup for a profile.
     * 
     * @param profile
     *            A <code>Profile</code>.
     */
    public void showForProfile(final Profile profile);
}
