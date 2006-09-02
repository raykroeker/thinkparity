/**
 * Created On: 1-Sep-06 4:15:11 PM
 * $Id$
 */
package com.thinkparity.browser.platform.action.container;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class UndeleteDocument extends AbstractAction {

    /** The thinkParity browser application. */
    private final Browser browser;

    /**
     * Create Export.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public UndeleteDocument(final Browser browser) {
        super(ActionId.CONTAINER_UNDELETE_DOCUMENT);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        Assert.assertNotYetImplemented("Undelete Document");     
    }


}
