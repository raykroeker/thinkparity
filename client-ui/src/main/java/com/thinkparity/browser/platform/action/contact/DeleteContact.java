/**
 * Created On: 4-Jul-2006 11:08:50 AM
 * $Id$
 */
package com.thinkparity.browser.platform.action.contact;

import javax.swing.Icon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class DeleteContact extends AbstractAction {

    /**
     * The action small ICON.
     * 
     */
    private static final Icon ICON;

    /**
     * The action id.
     * 
     */
    private static final ActionId ID;

    /**
     * The action NAME.
     * 
     */
    private static final String NAME;

    /**
     * @see java.io.Serializable
     * 
     */
    private static final long serialVersionUID = 1;

    static {
        ICON = null;
        ID = ActionId.CONTACT_DELETE;
        NAME = "Delete Contact";
    }

    /**
     * The browser application.
     * 
     */
    private final Browser browser;
    
    /**
     * Create an OpenContact.
     * 
     */
    public DeleteContact(final Browser browser) {
        super("Contact.Delete", ID, NAME, ICON);
        this.browser = browser;
    }
    
    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) throws Exception {
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
    public enum DataKey {
        CONTACT_ID
    }
}
