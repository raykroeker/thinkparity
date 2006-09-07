/**
 * Created On: 4-Jul-2006 11:08:50 AM
 * $Id$
 */
package com.thinkparity.browser.platform.action.contact;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.xmpp.contact.Contact;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Delete extends AbstractAction {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The browser application. */
    private final Browser browser;
    
    /** Create Delete. */
    public Delete(final Browser browser) {
        super(ActionId.CONTACT_DELETE);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final JabberId contactId = (JabberId) data.get(DataKey.CONTACT_ID);
        final Contact contact = getContactModel().read(contactId);

        if(browser.confirm("ContactDelete.ConfirmDeleteMessage", new Object[] {contact.getName()})) {
            getContactModel().delete(contactId);
            browser.fireContactDeleted(contactId);
        }
    }

    /**
     * The key used to set\get the data.
     * 
     * @see Data
     */
    public enum DataKey { CONTACT_ID }
}
