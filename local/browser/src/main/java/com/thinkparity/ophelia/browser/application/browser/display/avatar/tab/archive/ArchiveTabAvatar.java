/*
 * Created On: 6-Oct-06 2:06:21 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.archive;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.event.tab.archive.ArchiveTabDispatcher;
import com.thinkparity.ophelia.model.events.ContainerEvent;

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
        setFilterDelegate(model);
        addPropertyChangeListener("eventDispatcher",
                new PropertyChangeListener() {
                    public void propertyChange(final PropertyChangeEvent evt) {
                        ((ArchiveTabDispatcher) getEventDispatcher())
                                .addListeners(ArchiveTabAvatar.this);
                    }
                });
    }

    /**
     * Collapse the container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void collapseContainer(final Long containerId) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.collapsePanel(containerId, Boolean.FALSE);
            }
        });
    }

    /**
     * Expand the container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void expandContainer(final Long containerId) {
        showPanel(containerId);
    }

    /**
     * Fire an archived container event.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireArchived(final ContainerEvent e) {
        sync(e);
    }

    /**
     * Fire an deleted container event.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireDeleted (final ContainerEvent e) {
        sync(e);
    }

    /**
     * Fire a restored container event.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireRestored(final ContainerEvent e) {
        sync(e);
    }

    /**
     * Expand a panel.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    private void expandPanel(final Long containerId) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.expandPanel(containerId, Boolean.FALSE);
            }
        });
    }

    /**
     * Scroll a panel to make it visible.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    private void scrollPanelToVisible(final Long containerId) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.scrollPanelToVisible(containerId);
            }
        });
    }

    /**
     * Select a panel.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    private void selectPanel(final Long containerId) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.selectPanel(containerId);
            }
        });
    }

    /**
     * Show the panel (expand the panel and scroll so it
     * is visible).
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    private void showPanel(final Long containerId) {
        selectPanel(containerId);
        expandPanel(containerId);
        scrollPanelToVisible(containerId);
    }

    /**
     * Synchronize a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    private void sync(final ContainerEvent e) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncContainer(e.getContainer().getId(), e.isRemote());
            }
        });
    }
}
