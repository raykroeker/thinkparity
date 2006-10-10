/*
 * Created On: 10-Oct-06 10:20:22 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JPopupMenu;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerModel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionsPanel.DraftCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionsPanel.DraftDocumentCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionsPanel.UserCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionsPanel.VersionCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionsPanel.VersionDocumentCell;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.Read;
import com.thinkparity.ophelia.browser.platform.action.container.*;
import com.thinkparity.ophelia.browser.platform.action.document.Open;
import com.thinkparity.ophelia.browser.platform.action.document.OpenVersion;
import com.thinkparity.ophelia.browser.platform.action.document.Rename;

import com.thinkparity.ophelia.model.container.ContainerDraft;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ContainerVersionsPopup {

    /** The <code>JPopupMenu</code>. */
    private final JPopupMenu jPopupMenu;

    /**
     * Create ContainerVersionsPopup. This will construct a popup menu for a
     * draft document cell.
     * 
     * @param model
     *            The <code>ContainerModel</code>.
     * @param draftDocument
     *            A <code>DraftDocumentCell</code>.
     */
    ContainerVersionsPopup(final ContainerModel model,
            final DraftDocumentCell draftDocument) {
        super();
        this.jPopupMenu = MenuFactory.createPopup();
        final PopupItemFactory popupItemFactory = PopupItemFactory.getInstance();
        final ContainerDraft.ArtifactState state = draftDocument.getState();

        final Data openData = new Data(1);
        openData.set(Open.DataKey.DOCUMENT_ID, draftDocument.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.DOCUMENT_OPEN, openData));

        jPopupMenu.addSeparator();

        final Data renameData = new Data(1);
        renameData.set(Rename.DataKey.DOCUMENT_ID, draftDocument.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.DOCUMENT_RENAME, renameData));
        switch (state) {
        case ADDED:
            break;
        case MODIFIED:
            final Data revertData = new Data(2);
            revertData.set(RevertDocument.DataKey.CONTAINER_ID, draftDocument.getContainerId());
            revertData.set(RevertDocument.DataKey.DOCUMENT_ID, draftDocument.getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_REVERT_DOCUMENT, renameData));
            break;
        case NONE:
            break;
        case REMOVED:
            final Data removeData = new Data(2);
            removeData.set(RemoveDocument.DataKey.CONTAINER_ID, draftDocument.getContainerId());
            removeData.set(RemoveDocument.DataKey.DOCUMENT_ID, draftDocument.getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_UNDELETE_DOCUMENT, renameData));
            break;
        default:
            throw Assert.createUnreachable("UNKNOWN ARTIFACT STATE");
        }
        if (ContainerDraft.ArtifactState.REMOVED != state) {
            final Data removeData = new Data(2);
            removeData.set(RemoveDocument.DataKey.CONTAINER_ID, draftDocument.getContainerId());
            removeData.set(RemoveDocument.DataKey.DOCUMENT_ID, draftDocument.getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_REMOVE_DOCUMENT, removeData));
        }
        jPopupMenu.addSeparator();
        final Data printData = new Data(1);
        printData.set(com.thinkparity.ophelia.browser.platform.action.document.PrintDraft.DataKey.DOCUMENT_ID, draftDocument.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.DOCUMENT_PRINT_DRAFT, printData));
    }

    /**
     * Create ContainerVersionsPopup. This will construct a popup menu for a
     * container version document cell.
     * 
     * @param model
     *            The <code>ContainerModel</code>.
     * @param versionDocument
     *            A <code>VersionDocumentCell</code>.
     */
    ContainerVersionsPopup(final ContainerModel model,
            final VersionDocumentCell versionDocument) {
        super();
        // TODO New UI:  The version documents popup will not work.  Need more data.
        this.jPopupMenu = MenuFactory.createPopup();
        final PopupItemFactory popupItemFactory = PopupItemFactory.getInstance();
        final Data openData = new Data(2);
        openData.set(OpenVersion.DataKey.DOCUMENT_ID, -1L);
        openData.set(OpenVersion.DataKey.VERSION_ID, -1L);
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.DOCUMENT_OPEN_VERSION, openData));

        jPopupMenu.addSeparator();

        final Data printData = new Data(2);
        printData.set(com.thinkparity.ophelia.browser.platform.action.document.PrintVersion.DataKey.CONTAINER_ID, -1L);
        printData.set(com.thinkparity.ophelia.browser.platform.action.document.PrintVersion.DataKey.CONTAINER_VERSION_ID, -1L);
        printData.set(com.thinkparity.ophelia.browser.platform.action.document.PrintVersion.DataKey.DOCUMENT_ID, -1L);
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.DOCUMENT_PRINT_VERSION, printData));
    }

    /**
     * Create ContainerVersionsPopup. This will construct a popup menu for a
     * draft cell.
     * 
     * @param model
     *            The <code>ContainerModel</code>.
     * @param draft
     *            The <code>DraftCell</code>.
     */
    ContainerVersionsPopup(final ContainerModel model,
            final DraftCell draft) {
        super();
        this.jPopupMenu = MenuFactory.createPopup();
        final PopupItemFactory popupItemFactory = PopupItemFactory.getInstance();
        if(model.isOnline()) {
            final Data publishData = new Data(1);
            publishData.set(Publish.DataKey.CONTAINER_ID, draft.getContainerId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PUBLISH, publishData));

            final Data deleteData = new Data(1);
            deleteData.set(DeleteDraft.DataKey.CONTAINER_ID, draft.getContainerId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_DELETE_DRAFT, deleteData));
            
            jPopupMenu.addSeparator();
        }
        final Data addDocumentData = new Data(2);
        addDocumentData.set(AddDocument.DataKey.CONTAINER_ID, draft.getContainerId());
        addDocumentData.set(AddDocument.DataKey.FILES, new File[0]);
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_ADD_DOCUMENT, addDocumentData));       
        jPopupMenu.addSeparator();       
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_EXPORT_DRAFT, Data.emptyData()));
        final Data printData = new Data(1);
        printData.set(PrintDraft.DataKey.CONTAINER_ID, draft.getContainerId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PRINT_DRAFT, printData));
    }

    /**
     * Create ContainerVersionsPopup. This will create a popup menu for a user
     * cell.
     * 
     * @param model
     *            The <code>ContainerModel</code>.
     * @param user
     *            A <code>UserCell</code>.
     */
    ContainerVersionsPopup(final ContainerModel model,
            final UserCell user) {
        super();
        this.jPopupMenu = MenuFactory.createPopup();
        final PopupItemFactory popupItemFactory = PopupItemFactory.getInstance();

        final Data readData = new Data(1);
        readData.set(Read.DataKey.CONTACT_ID, user.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_READ, readData));
    }

    /**
     * Create ContainerVersionsPopup. This will construct a popup menu for a
     * version cell.
     * 
     * @param model
     *            The <code>ContainerModel</code>.
     * @param version
     *            The <code>VersionCell</code>.
     */
    ContainerVersionsPopup(final ContainerModel model,
            final VersionCell version) {
        super();
        this.jPopupMenu = MenuFactory.createPopup();
        final PopupItemFactory popupItemFactory = PopupItemFactory.getInstance();
        if (model.isOnline()) {
            final Data shareData = new Data(2);
            shareData.set(Share.DataKey.CONTAINER_ID, version.getArtifactId());
            shareData.set(Share.DataKey.VERSION_ID, version.getVersionId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_SHARE, shareData));    
            jPopupMenu.addSeparator();
        }
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_EXPORT_VERSION, Data.emptyData()));
        final Data printData = new Data(2);
        printData.set(com.thinkparity.ophelia.browser.platform.action.container.PrintVersion.DataKey.CONTAINER_ID, version.getArtifactId());
        printData.set(com.thinkparity.ophelia.browser.platform.action.container.PrintVersion.DataKey.VERSION_ID, version.getVersionId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PRINT_VERSION, printData));
    }

    /**
     * Show the popup menu.
     * 
     * @param invoker
     *            A swing <code>Component</code>.
     * @param e
     *            A <code>MenuEvent</code>.
     */
    void show(final Component invoker, final MouseEvent e) {
        jPopupMenu.show(invoker, e.getX(), e.getY());
    }
}
