/*
 * Created On:  Saturday Dec 9 2006, 21:44
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import com.thinkparity.codebase.model.contact.Contact;

import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ActionDelegate;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.DefaultActionDelegate;
import com.thinkparity.ophelia.browser.platform.action.contact.Read;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ContactTabActionDelegate extends DefaultActionDelegate implements
        ActionDelegate {

    /** A contact read <code>AbstractAction</code>. */
    private final AbstractAction contactRead;

    /**
     * Create ContactTabActionDelegate.
     * 
     * @param model
     *            The <code>ContactTabModel</code>.
     */
    ContactTabActionDelegate(final ContactTabModel model) {
        super();
        this.contactRead = getInstance(ActionId.CONTACT_READ);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ActionDelegate#invokeForContact(com.thinkparity.codebase.model.contact.Contact)
     * 
     */
    public void invokeForContact(final Contact contact) {
        final Data data = new Data(1);
        data.set(Read.DataKey.CONTACT_ID, contact.getId());
        invoke(contactRead, data);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ActionDelegate#invokeForInvitation(com.thinkparity.ophelia.model.contact.IncomingInvitation)
     * 
     */
    public void invokeForInvitation(final IncomingInvitation invitation) {}

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ActionDelegate#invokeForInvitation(com.thinkparity.ophelia.model.contact.OutgoingInvitation)
     * 
     */
    public void invokeForInvitation(OutgoingInvitation invitation) {}
}
