/*
 * Created On:  17-Jan-07 11:48:57 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.event.tab.archive;

import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.events.ContainerAdapter;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.events.ContainerListener;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.archive.ArchiveTabAvatar;
import com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArchiveTabDispatcher implements EventDispatcher<ArchiveTabAvatar> {

    /** A <code>ContainerListener</code>. */
    private ContainerListener containerListener;

    /** An instance of <code>ContainerModel</code>. */
    private final ContainerModel containerModel;

    /**
     * Create ArchiveTabDispatcher.
     * 
     * @param containerModel
     *            An instance of <code>ContainerModel</code>.
     */
    public ArchiveTabDispatcher(final ContainerModel containerModel) {
        super();
        this.containerModel = containerModel;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher#addListeners(com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar)
     *
     */
    public void addListeners(final ArchiveTabAvatar avatar) {
        containerListener = new ContainerAdapter() {
            @Override
            public void containerArchived(final ContainerEvent e) {
                avatar.fireArchived(e);
            }
            @Override
            public void containerRestored(final ContainerEvent e) {
                avatar.fireRestored(e);
            }
            @Override
            public void containerDeleted(final ContainerEvent e) {
                avatar.fireDeleted(e);
            }
            @Override
            public void containerFlagged(final ContainerEvent e) {
                avatar.fireFlagged(e);
            }
        };
        containerModel.addListener(containerListener);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.EventDispatcher#removeListeners(com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar)
     *
     */
    public void removeListeners(final ArchiveTabAvatar avatar) {
        containerModel.removeListener(containerListener);
        containerListener = null;
    }
}
