/*
 * Created On: 6-Oct-06 2:06:21 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.events.ContainerEvent;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.event.tab.container.ContainerTabDispatcher;

/**
 * <b>Title:</b>thinkParity Container Tab Avatar<br>
 * <b>Description:</b>The container tab avatar borrows heavily from the tab
 * avatar and uses a separate model to populate the the tab as well as a set of
 * display renderers to provide the main display.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerTabAvatar extends TabPanelAvatar<ContainerTabModel> {

    /**
     * Create ContainerAvatar.
     * 
     */
    public ContainerTabAvatar() {
        super(AvatarId.TAB_CONTAINER, new ContainerTabModel());
        model.setLocalization(getLocalization());
        model.setSession(getSession());
        setFilterDelegate(model);
        addPropertyChangeListener("eventDispatcher",
                new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent evt) {
                ((ContainerTabDispatcher) getEventDispatcher())
                        .addListeners(ContainerTabAvatar.this);
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
     * Notify the avatar that a container has been archived.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireArchived(final ContainerEvent e) {
        sync(e);
    }

    /**
     * Notify the avatar that a container has been created.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireCreated(final ContainerEvent e) {
        if (e.isRemote()) {
            removeFlagSeen(e);
            sync(e);
        } else {
            sync(e);
            showPanel(e.getContainer().getId());           
            setDraftSelection(e);
        }
    }

    /**
     * Notify the avatar that a container has been deleted.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.       
     */
    public void fireDeleted(final ContainerEvent e) {
        sync(e);
    }

    /**
     * Notify the avatar that a document has been added.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireDocumentAdded(final ContainerEvent e) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncDocumentAdded(e.getContainer(), e.getDocument(), e.getDraft());
            }
        });
    }

    /**
     * Notify the avatar that a document has been deleted.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireDocumentRemoved(final ContainerEvent e) {
        sync(e);
    }

    /**
     * Notify the avatar that a document has been reverted.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireDocumentReverted(final ContainerEvent e) {
        sync(e);
    }

    /**
     * Notify the avatar that the draft has been created.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.   
     */
    public void fireDraftCreated(final ContainerEvent e) {
        sync(e);
        if (e.isLocal())
            setDraftSelection(e);
    }

    /**
     * Notify the avatar that the draft has been deleted.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireDraftDeleted(final ContainerEvent e) {
        sync(e);
    }

    /**
     * Notify the avatar that a container draft or version
     * has been published.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireContainerPublished(final ContainerEvent e) {
        if (e.isRemote()) {
            removeFlagSeen(e);
        }
        sync(e); 
    }

    /**
     * Notify the avatar that a container has been flagged.
     * (eg. bookmark added or removed, latest flag changed, etc.)
     * 
     * @param e
     *            A <code>ContainerEvent</code>.       
     */
    public void fireFlagged(final ContainerEvent e) {
        sync(e);
    }

    /**
     * Notify the avatar that a container has been received.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.  
     */
    public void fireReceived(final ContainerEvent e) {
        sync(e);
    }

    /**
     * Notify the avatar that a container has been renamed.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.       
     */
    public void fireRenamed(final ContainerEvent e) {
        sync(e); 
    }

    /**
     * Notify the avatar that a container has been restored.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireRestored(final ContainerEvent e) {
        if (e.isRemote()) {
            removeFlagSeen(e);
        }
        sync(e);
    }

    /**
     * Notify the avatar that a team member has been added.
     *
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireTeamMemberAdded(final ContainerEvent e) {
        sync(e);
    }

    /**
     * Notify the avatar that a team member has been removed.
     *
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireTeamMemberRemoved(final ContainerEvent e) {
        sync(e);
    }

    /**
     * Notify the avatar that a container has been updated.
     *
     * @param e
     *            A <code>ContainerEvent</code>.     
     */
    public void fireUpdated(final ContainerEvent e) {
        sync(e);
    }

    /**
     * Show the container (expand the panel and scroll so it
     * is visible).
     * 
     * @param containerIds
     *            A list of container id <code>Long</code>.
     * @param displayIndex
     *            The index of the container to show (0 indicates the container displayed at top). 
     */
    public void showContainer(final Container container) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                showPanel(container.getId());
            }
        });
    }

    /**
     * Synchronize a container in the avatar.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param remote
     *            A remote event <code>Boolean</code> indicator.
     */
    public void syncContainer(final Long containerId, final Boolean remote) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncContainer(containerId, remote);
            }
        });
    }

    /**
     * Synchronize a document in the avatar.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param remote
     *            A remote event <code>Boolean</code> indicator.
     */
    public void syncDocument(final Long documentId, final Boolean remote) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncDocument(documentId, remote);
            }
        });
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
     * Remove the seen flag from a container within the event.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    private void removeFlagSeen(final ContainerEvent e) {
        getController().runRemoveContainerFlagSeen(e.getContainer().getId());
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
     * Select the draft for a container.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    private void setDraftSelection(final ContainerEvent e) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.setDraftSelection(e.getContainer().getId());
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
