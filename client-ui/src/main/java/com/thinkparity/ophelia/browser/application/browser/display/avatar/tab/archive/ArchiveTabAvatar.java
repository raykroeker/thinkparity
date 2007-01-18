/*
 * Created On: 6-Oct-06 2:06:21 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.archive;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.thinkparity.ophelia.model.events.ContainerEvent;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.event.tab.archive.ArchiveTabDispatcher;

/**
 * <b>Title:</b>thinkParity Container Tab Avatar<br>
 * <b>Description:</b>The container tab avatar borrows heavily from the tab
 * avatar and uses a separate model to populate the the tab as well as a set of
 * display renderers to provide the main display.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArchiveTabAvatar extends TabPanelAvatar<ArchiveTabModel> {

    /**
     * Create ContainerAvatar.
     * 
     */
    public ArchiveTabAvatar() {
        super(AvatarId.TAB_ARCHIVE, new ArchiveTabModel());
        model.setLocalization(getLocalization());
        model.setSession(getSession());
        setSortByDelegate(model);
        addPropertyChangeListener("eventDispatcher",
                new PropertyChangeListener() {
                    public void propertyChange(final PropertyChangeEvent evt) {
                        ((ArchiveTabDispatcher) getEventDispatcher())
                                .addListeners(ArchiveTabAvatar.this);
                    }
                });
    }

    /**
     * Fire an archived container event.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireArchived(final ContainerEvent e) {
        syncContainer(e.getContainer().getId(), e.isRemote());
    }

    /**
     * Fire an deleted container event.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireDeleted (final ContainerEvent e) {
        syncContainer(e.getContainer().getId(), e.isRemote());
    }

    /**
     * Fire a restored container event.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireRestored(final ContainerEvent e) {
        syncContainer(e.getContainer().getId(), e.isRemote());
    }

    /**
     * Synchronize a container in the avatar.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param remote
     *            A remote event <code>Boolean</code> indicator.
     */
    private void syncContainer(final Long containerId, final Boolean remote) {
        model.syncContainer(containerId, remote);
    }
}
