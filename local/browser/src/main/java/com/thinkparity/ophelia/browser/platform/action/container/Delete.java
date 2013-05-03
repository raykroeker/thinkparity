/*
 * Created On: Aug 22, 2006 8:21:37 AM
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.document.CannotLockException;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity OpheliaUI Delete Action<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public class Delete extends AbstractBrowserAction {

    /** The thinkParity browser application. */
    private final Browser browser;

    /**
     * A <code>Container</code>. Used by invoke and retry invoke to maintain
     * the previous container.
     */
    private Container container;

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
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Container container = getContainerModel().read(containerId);
        final Boolean draftExists = getContainerModel().doesExistLocalDraft(containerId);
        if (draftExists) {
            if (browser.confirm("ContainerDelete.ConfirmDeleteDraftExistsMessage",
                    new Object[] { container.getName() })) {
                invoke(container);
            }
        } else {
            if (browser.confirm("ContainerDelete.ConfirmDeleteMessage",
                    new Object[] { container.getName() })) {
                invoke(container);
            }
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#retryInvokeAction()
     *
     */
    @Override
    public void retryInvokeAction() {
        invoke(container);
    }

    /**
     * Clear notifications.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    private void clearNotifications(final Long containerId) {
        final Data data = new Data(2);
        data.set(ClearNotifications.DataKey.ALL_VERSIONS, Boolean.TRUE);
        data.set(ClearNotifications.DataKey.CONTAINER_ID, containerId);
        invoke(ActionId.CONTAINER_CLEAR_NOTIFICATIONS, data);
    }

    /**
     * Invoke delete.
     * 
     * @param container
     *            A <code>Container</code>.
     */
    private void invoke(final Container container) {
        this.container = container;
        clearNotifications(container.getId());
        try {
            getContainerModel().delete(container.getId());
        } catch (final CannotLockException clx) {
            logger.logWarning("Cannot lock document for container {0}.  {1}",
                    container.getName(), clx.getMessage());
            browser.retry(this, container.getName());
        }
    }

    /** <b>Title:</b>Delete Data Keys<br> */
    public enum DataKey { CONTAINER_ID }
}
