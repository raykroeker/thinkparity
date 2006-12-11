/*
 * Created On: 6-Oct-06 2:06:21 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelAvatar;

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
        setPopupDelegate(model.getPopupDelegate());
    }

    /**
     * Synchronize a container.
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
     * Synchronize a document.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param remote
     *            A remote event <code>Boolean</code> indicator.
     */
    public void syncDocument(final Long documentId, final Boolean remote) {
        model.syncDocument(documentId, remote);
    }
}
