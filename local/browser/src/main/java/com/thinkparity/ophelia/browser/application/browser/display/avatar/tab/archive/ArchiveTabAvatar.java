/*
 * Created On: 6-Oct-06 2:06:21 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.archive;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.model.events.ContainerEvent;

import com.thinkparity.ophelia.browser.BrowserException;
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
     * This method waits for completion.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void expandContainer(final Long containerId) {
        showPanel(containerId, Boolean.TRUE);
    }

    /**
     * Expand the container with version selected.
     * This method waits for completion.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     */
    public void expandContainer(final Long containerId, final Long versionId) {
        showPanel(containerId, Boolean.TRUE);
        setVersionSelection(containerId, versionId);
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
     * Notify the avatar that a container has been flagged.
     * (eg. bookmark, seen flag, or latest flag changed)
     * 
     * @param e
     *            A <code>ContainerEvent</code>.       
     */
    public void fireFlagged(final ContainerEvent e) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncFlagged(e.getContainer());
            }
        });
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
     * Select a version for a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     */
    private void setVersionSelection(final Long containerId, final Long versionId) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.setVersionSelection(containerId, versionId);
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
        model.selectPanel(containerId);
        model.expandPanel(containerId, Boolean.FALSE);
        model.scrollPanelToVisible(containerId);
    }

    /**
     * Show the panel (expand the panel and scroll so it
     * is visible).
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param wait
     *            A wait <code>Boolean</code>, if true wait for completion.
     */
    private void showPanel(final Long containerId, final Boolean wait) {
        if (wait) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        showPanel(containerId);
                    }
                });
            } catch (final InterruptedException ix) {
                throw new BrowserException("Unable to show panel.", ix);
            } catch (final InvocationTargetException itx) {
                throw new BrowserException("Unable to show panel.", itx);
            }
        } else {
            SwingUtil.ensureDispatchThread(new Runnable() {
                public void run() {
                    showPanel(containerId);
                }
            });
        }
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
