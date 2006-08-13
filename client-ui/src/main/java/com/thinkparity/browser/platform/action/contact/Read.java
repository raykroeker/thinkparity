/**
 * Created On: 27-Jun-2006 2:25:50 PM
 */
package com.thinkparity.browser.platform.action.contact;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author rob_masako@shaw.ca
 * @version 1.1.2.2
 */
public class Read extends AbstractAction {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The browser application. */
    private final Browser browser;

    /** Create ReadContact. */
    public Read(final Browser browser) {
        super(ActionId.CONTACT_READ);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final JabberId contactId = (JabberId) data.get(DataKey.CONTACT_ID);
        browser.displayReadContactDialog(contactId);
    }

    /**
     * The key used to set\get the data.
     * 
     * @see Data
     */
    public enum DataKey { CONTACT_ID }
}
