/*
 * Created On: Aug 22, 2006 8:21:37 AM
 */
package com.thinkparity.browser.platform.action.container;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.model.container.ContainerModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Delete extends AbstractAction {

    /** The thinkParity browser application. */
    private final Browser browser;

    /**
     * Create Delete.
     * 
     * @param browser
     *            The thinkParity browser application.
     */
    public Delete(final Browser browser) {
        super(ActionId.CONTAINER_DELETE);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);

        final ContainerModel containerModel = getContainerModel();
        final Container container = containerModel.read(containerId);
        if (browser.confirm("ContainerDelete.ConfirmDeleteMessage",
                new Object[] { container.getName() })) {
            containerModel.delete(containerId);
        }
    }

    public enum DataKey { CONTAINER_ID }
}
