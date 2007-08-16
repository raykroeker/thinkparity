/**
 * Created On: 18-May-07 10:02:23 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.model.container.ContainerModel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ApplyFlagSeen extends AbstractBrowserAction {

    /**
     * Create ApplyFlagSeen.
     * 
     * @param browser
     *            The <code>Browser</code>.
     */
    public ApplyFlagSeen(final Browser browser) {
        super(ActionId.CONTAINER_APPLY_FLAG_SEEN);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final Long versionId = (Long) data.get(DataKey.VERSION_ID);
        final ContainerModel containerModel = getContainerModel();
        final ContainerVersion containerVersion = containerModel.readVersion(containerId, versionId);
        if (!containerVersion.isSeen().booleanValue()) {
            containerModel.applyFlagSeen(containerVersion);
        }
        // ensure the container seen flag is in sync with the version seen flags
        final Container container = containerModel.read(containerId);
        if (!container.isSeen().booleanValue()) {
            // set the container seen flag if all versions have been seen
            if (containerModel.isVersionFlagSeenAppliedAll(containerId)) {
                containerModel.applyFlagSeen(containerId);
            }
        }

        // Clear related notification
        clearNotifications(containerId, versionId);
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
        final Data data = new Data(3);
        data.set(ClearNotifications.DataKey.ALL_VERSIONS, Boolean.FALSE);
        data.set(ClearNotifications.DataKey.CONTAINER_ID, containerId);
        data.set(ClearNotifications.DataKey.VERSION_ID, versionId);
        invoke(ActionId.CONTAINER_CLEAR_NOTIFICATIONS, data);
    }

    /** The data key. */
    public enum DataKey { CONTAINER_ID, VERSION_ID }
}
