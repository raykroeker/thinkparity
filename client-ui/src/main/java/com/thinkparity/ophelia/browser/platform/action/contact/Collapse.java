/**
 * Created On: Feb 9, 2007 11:33:00 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Collapse extends AbstractAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create a Collapse.
     * 
     * @param browser
     *            The browser application.
     */
    public Collapse(final Browser browser) {
        super(ActionId.CONTACT_COLLAPSE);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final JabberId contactId = (JabberId) data.get(DataKey.CONTACT_ID);
        browser.collapseContact(contactId);  
    }

    /** The data keys. */
    public enum DataKey { CONTACT_ID }
}
