/*
 * Created On: 9-Oct-06 11:13:05 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.UUID;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerModel;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.container.AddDocument;
import com.thinkparity.ophelia.browser.platform.action.container.CreateDraft;
import com.thinkparity.ophelia.browser.platform.action.container.Delete;
import com.thinkparity.ophelia.browser.platform.action.container.DeleteDraft;
import com.thinkparity.ophelia.browser.platform.action.container.PrintDraft;
import com.thinkparity.ophelia.browser.platform.action.container.Publish;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ContainerPopup {

    /** A <code>Container</code>. */
    private final Container container;

    /** A <code>ContainerModel</code>. */
    private final ContainerModel model;

    /** A <code>PopupItemFactory</code>. */
    private final PopupItemFactory popupItemFactory;

    /**
     * Create ContainerPopup.
     * 
     * @param model
     *            A <code>ContainerModel</code>.
     * @param container
     *            A <code>Container</code>.
     */
    ContainerPopup(final ContainerModel model, final Container container) {
        super();
        this.container = container;
        this.model = model;
        this.popupItemFactory = PopupItemFactory.getInstance();
    }

    /**
     * Show a container popup menu on an invoker at the mouse event's location.
     * 
     * @param invoker
     *            An invoker <code>Component</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    void show(final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        
        // Publish, new draft, discard draft
        if (isOnline()) {
            Boolean bNeedSep = Boolean.FALSE;
            if (isLocalDraft()) {
                final Data publishData = new Data(1);
                publishData.set(Publish.DataKey.CONTAINER_ID, getId());
                jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PUBLISH, publishData));
                bNeedSep = Boolean.TRUE;
            }
            if (!isDraft()) {
                final Data createDraftData = new Data(1);
                createDraftData.set(CreateDraft.DataKey.CONTAINER_ID, getId());
                jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_CREATE_DRAFT, createDraftData));  
                bNeedSep = Boolean.TRUE;
            }
            if (isLocalDraft()) {
                final Data deleteDraftData = new Data(1);
                deleteDraftData.set(DeleteDraft.DataKey.CONTAINER_ID, getId());
                jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_DELETE_DRAFT, deleteDraftData));
                bNeedSep = Boolean.TRUE;
            }
            if (bNeedSep) {
                jPopupMenu.addSeparator();
            }
        }
        
        // Add document
        final Data addDocumentData = new Data(1);
        addDocumentData.set(AddDocument.DataKey.CONTAINER_ID, getId());
        addDocumentData.set(AddDocument.DataKey.FILES, new File[0]);
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_ADD_DOCUMENT, addDocumentData));
        
        jPopupMenu.addSeparator();
        
        // Rename, archive, delete package
        final Data deleteData = new Data(1);
        deleteData.set(Delete.DataKey.CONTAINER_ID, getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_DELETE, deleteData));  
        
        jPopupMenu.addSeparator();

        // include the container's id and unique id in the menu
        if(e.isShiftDown()) {
            final Clipboard systemClipboard =
                Toolkit.getDefaultToolkit().getSystemClipboard();
            final ActionListener debugActionListener = new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    final StringSelection stringSelection =
                        new StringSelection(((JComponent) e.getSource()).getClientProperty("COPY_ME").toString());
                    systemClipboard.setContents(stringSelection, null);
                }
            };
            final JMenuItem idJMenuItem = new JMenuItem("Id - " + getId());
            idJMenuItem.putClientProperty("COPY_ME", getId());
            idJMenuItem.addActionListener(debugActionListener);

            final JMenuItem uidJMenuItem = new JMenuItem("Unique id - " + getUniqueId());
            uidJMenuItem.putClientProperty("COPY_ME", getUniqueId());
            uidJMenuItem.addActionListener(debugActionListener);

            jPopupMenu.addSeparator();
            jPopupMenu.add(idJMenuItem);
            jPopupMenu.add(uidJMenuItem);
        }

        // Export, print
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_EXPORT, Data.emptyData()));
        final Data printData = new Data(1);
        printData.set(PrintDraft.DataKey.CONTAINER_ID, getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PRINT_DRAFT, printData));

        jPopupMenu.show(invoker, e.getX(), e.getY());
    }

    private Long getId() {
        return container.getId();
    }

    private UUID getUniqueId() {
        return container.getUniqueId();
    }

    private Boolean isDraft() {
        return container.isDraft();
    }
    
    private Boolean isLocalDraft() {
        return container.isLocalDraft();
    }

    private Boolean isOnline() {
        return model.isOnline();
    }
}
