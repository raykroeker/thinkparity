/**
 * Created On: 3-Aug-06 4:47:01 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;


import com.thinkparity.codebase.model.artifact.DraftExistsException;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class CreateDraft extends AbstractBrowserAction {

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
        try {
            getContainerModel().createDraft(containerId);
        } catch (final DraftExistsException dex) {
            /* a draft already exists; a likely candidate for this error is that
             * another user started the draft creation milliseconds prior to
             * this user; and the draft is created after the local check; but
             * before this user can create the draft
             * 
             * nothing need be done in this case */
            logger.logWarning("Draft for artifact {0} already exists.", containerId);
        }
    }

    public enum DataKey { CONTAINER_ID }
}
