/*
 * Created On: 6-Oct-06 2:06:21 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.events.ContainerEvent;

import com.thinkparity.ophelia.browser.BrowserException;
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
            showPanel(e.getContainer().getId(), Boolean.FALSE);
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
                model.syncDocumentAdded(e.getContainer(), e.getDraft(), e.getDocument());
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
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncDocumentModified(e.getContainer(), e.getDraft(), e.getDocument());
            }
        });
    }

    /**
     * Notify the avatar that a document has been reverted.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireDocumentReverted(final ContainerEvent e) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncDocumentModified(e.getContainer(), e.getDraft(), e.getDocument());
            }
        });
    }

    /**
     * Notify the avatar that the draft has been created.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.   
     */
    public void fireDraftCreated(final ContainerEvent e) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncDraftChanged(e.getContainer(), e.getDraft(), e.isRemote());
            }
        });
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
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncDraftChanged(e.getContainer(), null, e.isRemote());
            }
        });
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
        if (e.isLocal()) {
            setVersionSelection(e.getContainer().getId(), e.getVersion().getVersionId());
        }
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
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncRenamed(e.getContainer());
            }
        }); 
    }

    /**
     * Notify the avatar that a team member has been added.
     *
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireTeamMemberAdded(final ContainerEvent e) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncTeamMemberAdded(e.getContainer(), e.getTeamMember());
            }
        }); 
    }

    /**
     * Notify the avatar that a team member has been removed.
     *
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireTeamMemberRemoved(final ContainerEvent e) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncTeamMemberRemoved(e.getContainer(), e.getTeamMember());
            }
        }); 
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
     * @param container
     *            A <code>Container</code>.
     */
    public void showContainer(final Container container) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                showPanel(container.getId(), Boolean.FALSE);
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
        model.syncContainer(containerId, remote);
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
     * Remove the seen flag from a container within the event.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    private void removeFlagSeen(final ContainerEvent e) {
        getController().runRemoveContainerFlagSeen(e.getContainer().getId());
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
        model.syncContainer(e.getContainer().getId(), e.isRemote());
    }
}
