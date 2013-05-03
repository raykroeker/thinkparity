/**
 * Created On: Feb 9, 2007 11:28:43 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Expand extends AbstractBrowserAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create a Expand.
     * 
     * @param browser
     *            The browser application.
     */
    public Expand(final Browser browser) {
        super(ActionId.CONTACT_EXPAND);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long contactId = (Long) data.get(DataKey.CONTACT_ID);
        browser.expandContact(contactId);  
    }

    /** The data keys. */
    public enum DataKey { CONTACT_ID }
}
