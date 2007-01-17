/*
 * Created On: 6-Oct-06 2:06:21 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.archive;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

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

    public void fireArchived(final ContainerEvent e) {
        // NOCOMMIT NYI raymond@thinkparity.com - 17-Jan-07 12:15:10 PM
        throw Assert.createNotYetImplemented("");
    }

    public void fireDeleted (final ContainerEvent e) {
        // NOCOMMIT NYI raymond@thinkparity.com - 17-Jan-07 12:15:10 PM
        throw Assert.createNotYetImplemented("");
    }

    public void fireRestored(final ContainerEvent e) {
        // NOCOMMIT NYI raymond@thinkparity.com - 17-Jan-07 12:15:10 PM
        throw Assert.createNotYetImplemented("");
    }

    /**
     * Show the container (expand the panel and scroll so it
     * is visible).
     * 
     * @param containerIds
     *            A list of container id <code>Long</code>.
     */
    public void showContainer(final List<Long> containerIds) {
        if (false)
            showContainer(0L);
        // NOCOMMIT NYI raymond@thinkparity.com - 17-Jan-07 11:52:57 AM
        throw Assert.createNotYetImplemented("");
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
     * Show the container (expand the panel and scroll so it
     * is visible).
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    private void showContainer(final Long containerId) {
        model.expandPanel(containerId, Boolean.FALSE);
        model.scrollPanelToVisible(containerId);
    }
}
