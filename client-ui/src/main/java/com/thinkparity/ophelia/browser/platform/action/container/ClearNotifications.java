/*
 * Created On:  3-Jul-07 1:06:44 PM
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.system.SystemApplication;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>Clear Notifications<br>
 * <b>Description:</b>Clear all notifications pertaining to a container.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ClearNotifications extends AbstractAction {

    /** The system application. */
    private final SystemApplication system;

    /**
     * Create ClearNotifications.
     * 
     * @param system
     *            A <code>SystemApplication</code>.
     */
    public ClearNotifications(final SystemApplication system) {
        super(ActionId.CONTAINER_CLEAR_NOTIFICATIONS);
        this.system = system;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     *
     */
    @Override
    protected void invoke(final Data data) {
        final Boolean allVersions = (Boolean) data.get(DataKey.ALL_VERSIONS);
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        if (allVersions) {
            clearNotifications(containerId);
        } else {
            final Long versionId = (Long) data.get(DataKey.VERSION_ID);
            clearNotifications(containerId, versionId);
        }
    }

    /**
     * Clear notifications.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    private void clearNotifications(final Long containerId) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                system.clearContainerNotifications(containerId);
            }
        });
    }

    /**
     * Clear notifications.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     */
    private void clearNotifications(final Long containerId, final Long versionId) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                system.clearContainerNotifications(containerId, versionId);
            }
        });
    }

    /** <b>Title:</b>Data Keys<br> */
    public enum DataKey { ALL_VERSIONS, CONTAINER_ID, VERSION_ID }
}
