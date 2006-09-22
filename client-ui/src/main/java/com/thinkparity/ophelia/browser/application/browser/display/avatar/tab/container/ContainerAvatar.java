/**
 * Created On: 13-Jul-06 11:02:44 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;


import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.application.browser.dnd.ImportTxHandler;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * The containers avatar displays the list of containers (packages)
 * and the documents inside the containers.
 * 
 * @author rob_masako@shaw.ca; raymond@thinkparity.com
 * @version 1.1.2.5
 */
public class ContainerAvatar extends TabAvatar<ContainerModel> {

    /** Create ContainerAvatar. */
    public ContainerAvatar() {
        super(AvatarId.TAB_CONTAINER, new ContainerModel());
        installMouseOverListener();
        setListTransferHandler(new ImportTxHandler(getController(), tabJList, model));
    }

    /**
     * Synchronize the container in the list.
     * Called, for example, if a new container is created.
     * 
     * @param containerId
     *            The container id.
     * @param remote
     *            Indicates whether the sync is the result of a remote event
     * @param select
     *            Indicates whether the container should be selected.
     */
    public void syncContainer(final Long containerId, final Boolean remote, final Boolean select) {
        final TabCell selectedCell = getSelectedCell();
        final Integer selectedIndex = getSelectedIndex();
        model.syncContainer(containerId, remote);
        // If "select" then attempt to select and expand this container,
        // otherwise select the previously selected container
        if (select) {
            final TabCell container = model.getContainerCell(containerId);
            if (null!=container) {
                model.triggerExpand(container, Boolean.TRUE);
                setSelectedCell(container);
            }
        }
        else {
            if (!setSelectedCell(selectedCell)) {
                setSelectedIndex(selectedIndex);
            }
        }
    }
    
    /**
     * Synchronize a document in the model.
     * 
     * @param documentId
     *            A document id.
     * @param remote
     *            Indicates whether the sync is the result of a remote event.
     */
    public void syncDocument(final Long documentId, final Boolean remote) {
        final TabCell selectedCell = getSelectedCell();
        model.syncDocument(documentId, remote);
        setSelectedCell(selectedCell);
    }

    /**
     * Synchronized a document in the model.
     * 
     * @param containerId
     *            The container id.
     * @param documentId
     *            The document id.           
     * @param remote
     *            Indicates whether the sync is the result of a remote event.
     */
    public void syncDocument(final Long containerId, final Long documentId,
            final Boolean remote) {
        final TabCell selectedCell = getSelectedCell();
        model.syncDocument(containerId, documentId, remote);
        setSelectedCell(selectedCell);
    }
    
    /**
     * Select an entry in the JList.
     * 
     * @param index
     *              The JList index to select.
     */
    @Override
    protected void triggerPopup(final Component invoker, final MouseEvent e) {
      final JPopupMenu jPopupMenu = MenuFactory.createPopup();
      jPopupMenu.add(menuItemFactory.createPopupItem(ActionId.CONTAINER_CREATE, Data.emptyData()));
      jPopupMenu.show(invoker, e.getX(), e.getY());
    }
}
