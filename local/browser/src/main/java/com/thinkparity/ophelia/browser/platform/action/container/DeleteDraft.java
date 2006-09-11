/*
 * Created On: Aug 22, 2006 2:31:53 PM
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.codebase.model.container.Container;


import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DeleteDraft extends AbstractAction {

    /** The thinkParity browser application. */
    private final Browser browser;

    /**
     * Create DeleteDraft.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public DeleteDraft(final Browser browser) {
        super(ActionId.CONTAINER_DELETE_DRAFT);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);

        final Container container = getContainerModel().read(containerId);
        if (browser.confirm(
                "ContainerDeleteDraft.ConfirmDeleteDraftMessage",
                new Object[] { container.getName() })) {
            getContainerModel().deleteDraft(containerId);
        }
    }

    public enum DataKey { CONTAINER_ID }
}
