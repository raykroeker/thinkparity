/**
 * Created On: 1-Sep-06 3:03:28 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.document;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PrintVersion extends AbstractAction {

    /**
     * Create Export.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public PrintVersion(final Browser browser) {
        super(ActionId.DOCUMENT_PRINT_VERSION);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        Assert.assertNotYetImplemented("PrintVersion#invoke()");
    }

    public enum DataKey { CONTAINER_ID, CONTAINER_VERSION_ID, DOCUMENT_ID }
}
