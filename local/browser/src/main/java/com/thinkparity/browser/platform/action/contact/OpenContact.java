/**
 * Created On: 27-Jun-2006 2:25:50 PM
 * $Id$
 */
package com.thinkparity.browser.platform.action.contact;

import javax.swing.Icon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class OpenContact extends AbstractAction {

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
        ID = ActionId.CONTACT_OPEN;
        NAME = "Open Contact";
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
    public OpenContact(final Browser browser) {
        super("Contact.Open", ID, NAME, ICON);
        this.browser = browser;
    }
    
    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) throws Exception {
        final JabberId contactId = (JabberId) data.get(DataKey.CONTACT_ID);
        browser.displayContactInfoDialogue(contactId);
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
