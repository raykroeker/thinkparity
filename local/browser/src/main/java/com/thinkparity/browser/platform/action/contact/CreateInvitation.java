/**
 * Created On: 4-Jul-2006 3:52:46 PM
 * $Id$
 */
package com.thinkparity.browser.platform.action.contact;

import javax.swing.Icon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.contact.ContactInvitation;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class CreateInvitation extends AbstractAction {

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
        ID = ActionId.CONTACT_ADD;
        NAME = "CreateInvitation";
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
    public CreateInvitation(final Browser browser) {
        super("CreateInvitation", ID, NAME, ICON);
        this.browser = browser;
    }
    
    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) throws Exception {
        final String contactEmail = (String) data.get(DataKey.CONTACT_EMAIL);
        if (null==contactEmail) {
            browser.displayContactCreateInvitation();
        }
        else {
            final ContactInvitation invitation = getContactModel().createInvitation(contactEmail);
            browser.fireContactInvitationCreated(invitation.getId(), Boolean.FALSE);            
        }
    }

    /**
     * The key used to set\get the data.
     * 
     * @see Data
     */
    public enum DataKey { CONTACT_EMAIL }
}
