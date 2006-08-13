/**
 * Created On: 4-Jul-2006 3:52:46 PM
 * $Id$
 */
package com.thinkparity.browser.platform.action.contact;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class CreateInvitation extends AbstractAction {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The browser application. */
    private final Browser browser;
    
    /** Create CreateInvitation. */
    public CreateInvitation(final Browser browser) {
        super(ActionId.CONTACT_CREATE_INVITATION);
        this.browser = browser;
    }
    
    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final String contactEmail = (String) data.get(DataKey.CONTACT_EMAIL);
        if (null == contactEmail) {
            browser.displayContactCreateInvitation();
        }
        else {
            getContactModel().createInvitation(contactEmail);
        }
    }

    /**
     * The key used to set\get the data.
     * 
     * @see Data
     */
    public enum DataKey { CONTACT_EMAIL }
}
