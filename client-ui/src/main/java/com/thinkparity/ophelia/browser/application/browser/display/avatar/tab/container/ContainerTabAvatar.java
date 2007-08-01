/*
 * Created On: 6-Oct-06 2:06:21 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.SwingUtilities;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

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
     * 
     * The panel is expanded (without animation), scrolled so it is visible,
     * and the container is selected.
     * This method waits for completion.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void expandContainer(final Long containerId) {
        showPanel(containerId, Boolean.TRUE);
    }

    /**
     * Expand the container.
     * 
     * The panel is expanded (without animation), scrolled so it is visible,
     * and the container and version are selected.
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
        sync(e);
        if (!e.isRemote()) {
            showPanel(e.getContainer().getId(), Boolean.FALSE);
            setDraftSelection(e.getContainer().getId());
        }
    }

    /**
     * Synchronize the container version's artifact receipts.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param receipts
     *            A <code>List<ArtifactReceipt></code>.
     */
    public void syncContainerVersionReceipts(final ContainerVersion version,
            final List<ArtifactReceipt> receipts) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            @Override
            public void run() {
                model.syncContainerVersionReceipts(version, receipts);
            }
        });
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
        if (e.isLocal()) {
            setDraftDocumentSelection(e.getContainer().getId(), e.getDocument().getId());
        }
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
            setDraftSelection(e.getContainer().getId());
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
            // synchronize the container seen flag with the version flag,
            // in the case somebody forwards a version you already have do nothing
            if (!e.getVersion().isSeen()) {
                removeFlagSeen(e);
            }
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
     * Notify the avatar that a container version has been flagged.
     * (eg. seen flag)
     * 
     * @param e
     *            A <code>ContainerEvent</code>.       
     */
    public void fireFlaggedVersion(final ContainerEvent e) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncFlaggedVersion(e.getVersion());
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
     * Show the topmost visible unread container version.
     * 
     * The panel is expanded (without animation), scrolled so it is visible,
     * and the container and version are selected.
     */
    public void showTopVisibleUnreadContainerVersion() {
        final Container container = model.getTopVisibleUnreadContainer();
        if (null != container) {
            showPanel(container.getId());
            final ContainerVersion version = model.getTopUnreadContainerVersion(container.getId());
            if (null != version) {
                setVersionSelection(version.getArtifactId(), version.getVersionId());
            }
        }
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
                model.syncDocument(model.lookupId(documentId), documentId, remote);
            }
        });
        if (!remote) {
            setDraftDocumentSelection(model.lookupId(documentId), documentId);
        }
    }

    /**
     * Remove the seen flag from a container version within the event.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    private void removeFlagSeen(final ContainerEvent e) {
        getController().runRemoveContainerFlagSeen(e.getContainer().getId(),
                e.getVersion().getVersionId());
    }

    /**
     * Select the draft document in a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param documentId
     *            A document id <code>Long</code>.
     */
    private void setDraftDocumentSelection(final Long containerId, final Long documentId) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.setDraftDocumentSelection(containerId, documentId);
            }
        });
    }

    /**
     * Select the draft for a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    private void setDraftSelection(final Long containerId) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.setDraftSelection(containerId);
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
        if (wait && !EventQueue.isDispatchThread()) {
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
