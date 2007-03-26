/**
 * Created On: 27-Jun-2006 2:25:50 PM
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;


/**
 * @author rob_masako@shaw.ca
 * @version 1.1.2.2
 */
public class Read extends AbstractBrowserAction {

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
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final JabberId contactId = (JabberId) data.get(DataKey.CONTACT_ID);
        browser.displayContactInfoDialog(contactId);
    }

    /**
     * The key used to set\get the data.
     * 
     * @see Data
     */
    public enum DataKey { CONTACT_ID }
}
