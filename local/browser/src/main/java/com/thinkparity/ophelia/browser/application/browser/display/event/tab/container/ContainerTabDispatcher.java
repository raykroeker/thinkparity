/*
 * Created On:  25-Dec-06 10:31:30 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.event.tab.container;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.events.ContainerAdapter;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.events.ContainerListener;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerTabAvatar;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerTabDispatcher implements EventDispatcher<ContainerTabAvatar> {

    /** A <code>ContainerListener</code>. */
    private ContainerListener containerListener;

    /** The <code>ContainerModel</code>. */
    private final ContainerModel containerModel;

    /**
     * Create ContainerTabDispatcher.
     *
     */
    public ContainerTabDispatcher(final ContainerModel containerModel) {
        super();
        this.containerModel = containerModel;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher#addListeners(com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar)
     *
     */
    public void addListeners(final ContainerTabAvatar avatar) {
        Assert.assertIsNull(containerListener,
                "Listener for avatar {0} already added.", avatar.getId());
        containerListener = new ContainerAdapter() {
            @Override
            public void containerBookmarkAdded(final ContainerEvent e) {
                avatar.fireFlagged(e);
            }
            @Override
            public void containerBookmarkRemoved(final ContainerEvent e) {
                avatar.fireFlagged(e);
            }
            @Override
            public void containerCreated(final ContainerEvent e) {
                avatar.fireCreated(e);
            }
            @Override
            public void containerDeleted(final ContainerEvent e) {
                avatar.fireDeleted(e);
            }
            @Override
            public void containerFlagLatestAdded(final ContainerEvent e) {
                avatar.fireFlagged(e);
            }
            @Override
            public void containerFlagLatestRemoved(final ContainerEvent e) {
                avatar.fireFlagged(e);
            }
            @Override
            public void containerFlagSeenAdded(final ContainerEvent e) {
                avatar.fireFlagged(e);
            }
            @Override
            public void containerFlagSeenRemoved(final ContainerEvent e) {
                avatar.fireFlagged(e);
            }
            @Override
            public void containerPublished(final ContainerEvent e) {
                avatar.fireContainerPublished(e);
            }
            @Override
            public void containerReceived(final ContainerEvent e) {
                avatar.fireReceived(e);              
            }
            @Override
            public void containerRenamed(final ContainerEvent e) {
                avatar.fireRenamed(e);
            }
            @Override
            public void containerVersionFlagSeenApplied(final ContainerEvent e) {
                avatar.fireFlaggedVersion(e);
            }
            @Override
            public void containerVersionFlagSeenRemoved(final ContainerEvent e) {
                avatar.fireFlaggedVersion(e);
            }
            @Override
            public void containerUpdated(final ContainerEvent e) {
                avatar.fireUpdated(e);
            }
            @Override
            public void documentAdded(final ContainerEvent e) {
                avatar.fireDocumentAdded(e);
            }
            @Override
            public void documentRemoved(final ContainerEvent e) {
                avatar.fireDocumentRemoved(e);
            }
            @Override
            public void documentReverted(final ContainerEvent e) {
                avatar.fireDocumentReverted(e);
            }
            @Override
            public void draftCreated(final ContainerEvent e) {
                avatar.fireDraftCreated(e);
            }
            @Override
            public void draftDeleted(final ContainerEvent e) {
                avatar.fireDraftDeleted(e);
            }
            @Override
            public void teamMemberAdded(final ContainerEvent e) {
                avatar.fireTeamMemberAdded(e);
            }
            @Override
            public void teamMemberRemoved(final ContainerEvent e) {
                avatar.fireTeamMemberRemoved(e);
            }
        };
        containerModel.addListener(containerListener);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher#removeListeners(com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar)
     * 
     */
    public void removeListeners(final ContainerTabAvatar avatar) {
        Assert.assertNotNull(containerListener,
                "Listener for avatar {0} not yet added.", avatar.getId());
        containerModel.removeListener(containerListener);
        containerListener = null;
    }
}
