/*
 * Created On: 6-Oct-06 2:06:21 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.event.tab.container.ContainerTabDispatcher;
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
public class ContainerTabAvatar extends TabPanelAvatar<ContainerTabModel> {

    /**
     * Create ContainerAvatar.
     * 
     */
    public ContainerTabAvatar() {
        super(AvatarId.TAB_CONTAINER, new ContainerTabModel());
        model.setLocalization(getLocalization());
        model.setSession(getSession());
        setSortByDelegate(model);
        addPropertyChangeListener("eventDispatcher",
                new PropertyChangeListener() {
                    public void propertyChange(final PropertyChangeEvent evt) {
                        ((ContainerTabDispatcher) getEventDispatcher())
                                .addListeners(ContainerTabAvatar.this);
                    }
                });
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
            getController().changeTab(AvatarId.TAB_CONTAINER);
            sync(e);
            showContainer(e.getContainer().getId());           
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
        sync(e);
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
     * Notify the avatar that a draft has been published.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void fireDraftPublished(final ContainerEvent e) {
        if (e.isRemote())
            removeFlagSeen(e);
        sync(e);
    }
    
    /**
     * Notify the avatar that a "seen" flag has been updated.
     * 
     * @param artifactId
     *            An artifactId <code>Long</code>.
     */
    public void fireSeenFlagUpdated(final Long artifactId) {
        getController().runDisplayContainerSeenFlagInfo();
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
        if (e.isRemote()) {
            removeFlagSeen(e);
        }
        sync(e);
    }
    
    /**
     * Show the container (expand the panel and scroll so it
     * is visible).
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    public void showContainer(final Long containerId) {
        model.expandPanel(containerId, Boolean.FALSE);
        model.scrollPanelToVisible(containerId); 
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
        model.syncDocument(documentId, remote);
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
        model.setDraftSelection(e.getContainer().getId());
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
