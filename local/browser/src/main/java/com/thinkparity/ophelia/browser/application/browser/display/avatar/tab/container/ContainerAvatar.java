/*
 * Created On: 6-Oct-06 2:06:21 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelAvatar;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerAvatar extends TabPanelAvatar<ContainerModel> {

    /**
     * Create ContainerAvatar.
     * 
     */
    public ContainerAvatar() {
        super(AvatarId.TAB_CONTAINER, new ContainerModel());
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
    
    /**
     * Trigger a popup when clicking in a blank area.
     * 
     */
    @Override
    protected void triggerPopup(final Component invoker, final MouseEvent e) {       
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        jPopupMenu.add(menuItemFactory.createPopupItem(ActionId.CONTAINER_CREATE, Data.emptyData()));
        jPopupMenu.show(invoker, e.getX(), e.getY());
    }

    /**
     * Trigger a sort.
     * 
     * @param sortElement
     *          What the containers will be sorted by.
     * @param sortDirection
     *          The direction of the sort.
     */
    @Override
    protected void triggerSort(final SortColumn sortColumn, final SortDirection sortDirection) {
        model.sortContainers(sortColumn, sortDirection);
    }      
}
