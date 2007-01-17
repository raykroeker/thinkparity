/*
 * Created On: 2007-01-17 13:12:00
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.container.ContainerModel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity Archive Container Action<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Archive extends AbstractAction {

    /** A thinkParity <code>Browser</code> application. */
    private final Browser browser;

    /**
     * Create Archive.
     * 
     */
    public Archive(final Browser browser) {
        super(ActionId.CONTAINER_ARCHIVE);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     *
     */
    @Override
    protected void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final ContainerModel containerModel = getContainerModel();
        final Container container = containerModel.read(containerId);
        if (browser.confirm("ContainerArchive.ConfirmArchiveMessage",
                new Object[] {container.getName()})) {
            containerModel.archive(container.getId());
        }
    }

    /** The action data keys. */
    public enum DataKey { CONTAINER_ID }
}
