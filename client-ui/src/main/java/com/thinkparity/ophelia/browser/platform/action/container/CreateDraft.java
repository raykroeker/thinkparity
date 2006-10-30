/**
 * Created On: 3-Aug-06 4:47:01 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;


import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class CreateDraft extends AbstractAction {

    /**
     * Create a Create.
     * 
     * @param browser
     *            The browser application.
     */
    public CreateDraft(final Browser browser) {
        super(ActionId.CONTAINER_CREATE_DRAFT);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        getContainerModel().createDraft(containerId);
    }

    public enum DataKey { CONTAINER_ID }
}
