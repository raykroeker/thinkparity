/**
 * Created On: 1-Sep-06 4:15:11 PM
 * $Id$
 */
package com.thinkparity.browser.platform.action.container;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class UndeleteDocument extends AbstractAction {

    /**
     * Create Export.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public UndeleteDocument(final Browser browser) {
        super(ActionId.CONTAINER_UNDELETE_DOCUMENT);
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        Assert.assertNotYetImplemented("Undelete Document");     
    }
}
