/*
 * Created On: Aug 22, 2006 8:21:37 AM
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.model.document.CannotLockException;

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
     */
    @Override
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);

        final Container container = getContainerModel().read(containerId);
        if (browser.confirm("ContainerDelete.ConfirmDeleteMessage",
                new Object[] { container.getName() })) {
            invoke(container);
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
     * Invoke delete.
     * 
     * @param container
     *            A <code>Container</code>.
     */
    private void invoke(final Container container) {
        this.container = container;
        try {
            getContainerModel().delete(container.getId());
        } catch (final CannotLockException clx) {
            browser.retry(this, container.getName());
        }
    }

    public enum DataKey { CONTAINER_ID }
}
