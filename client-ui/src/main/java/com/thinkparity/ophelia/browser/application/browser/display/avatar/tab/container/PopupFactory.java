/*
 * Created On:  30-Nov-06 12:25:24 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;

import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.VersionsPopupFactory;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.Read;
import com.thinkparity.ophelia.browser.platform.action.container.AddDocument;
import com.thinkparity.ophelia.browser.platform.action.container.CreateDraft;
import com.thinkparity.ophelia.browser.platform.action.container.Delete;
import com.thinkparity.ophelia.browser.platform.action.container.DeleteDraft;
import com.thinkparity.ophelia.browser.platform.action.container.DisplayVersionInfo;
import com.thinkparity.ophelia.browser.platform.action.container.PrintDraft;
import com.thinkparity.ophelia.browser.platform.action.container.Publish;
import com.thinkparity.ophelia.browser.platform.action.container.PublishVersion;
import com.thinkparity.ophelia.browser.platform.action.container.RemoveDocument;
import com.thinkparity.ophelia.browser.platform.action.container.RenameDocument;
import com.thinkparity.ophelia.browser.platform.action.container.RevertDocument;
import com.thinkparity.ophelia.browser.platform.action.document.Open;
import com.thinkparity.ophelia.browser.platform.action.document.OpenVersion;
import com.thinkparity.ophelia.browser.platform.plugin.PluginId;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class PopupFactory implements VersionsPopupFactory {

    /** A <code>ContainerModel</code>. */
    private final ContainerModel model;

    /** A <code>PopupItemFactory</code>. */
    private final PopupItemFactory popupItemFactory;

    /**
     * Create PopupFactory.
     * 
     * @param model
     *            The <code>ContainerModel</code>.
     */
    PopupFactory(final ContainerModel model) {
        super();
        this.model = model;
        this.popupItemFactory = PopupItemFactory.getInstance();
    }

    public JPopupMenu createDraftDocumentPopup(ContainerDraft draft, Document document) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        final PopupItemFactory popupItemFactory = PopupItemFactory.getInstance();
        final ContainerDraft.ArtifactState state = draft.getState(document);

        final Data openData = new Data(1);
        openData.set(Open.DataKey.DOCUMENT_ID, document.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.DOCUMENT_OPEN, openData));

        jPopupMenu.addSeparator();

        final Data renameData = new Data(2);
        renameData.set(RenameDocument.DataKey.CONTAINER_ID, draft.getContainerId());
        renameData.set(RenameDocument.DataKey.DOCUMENT_ID, document.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_RENAME_DOCUMENT, renameData));
        
        switch (state) {
        case ADDED:
            break;
        case MODIFIED:
            final Data revertData = new Data(2);
            revertData.set(RevertDocument.DataKey.CONTAINER_ID, draft.getContainerId());
            revertData.set(RevertDocument.DataKey.DOCUMENT_ID, document.getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_REVERT_DOCUMENT, renameData));
            break;
        case NONE:
            break;
        case REMOVED:
            final Data removeData = new Data(2);
            removeData.set(RemoveDocument.DataKey.CONTAINER_ID, draft.getContainerId());
            removeData.set(RemoveDocument.DataKey.DOCUMENT_ID, document.getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_UNDELETE_DOCUMENT, renameData));
            break;
        default:
            throw Assert.createUnreachable("UNKNOWN ARTIFACT STATE");
        }
        
        if (ContainerDraft.ArtifactState.REMOVED != state) {
            final Data removeData = new Data(2);
            removeData.set(RemoveDocument.DataKey.CONTAINER_ID, draft.getContainerId());
            removeData.set(RemoveDocument.DataKey.DOCUMENT_ID, document.getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_REMOVE_DOCUMENT, removeData));
        }
        
        jPopupMenu.addSeparator();
        
        final Data printData = new Data(1);
        printData.set(com.thinkparity.ophelia.browser.platform.action.document.PrintDraft.DataKey.DOCUMENT_ID, document.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.DOCUMENT_PRINT_DRAFT, printData));
        return jPopupMenu;
    }

    public JPopupMenu createDraftPopup(final Container container, final ContainerDraft draft) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
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

        final Data printData = new Data(1);
        printData.set(PrintDraft.DataKey.CONTAINER_ID, draft.getContainerId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PRINT_DRAFT, printData));
        return jPopupMenu;
    }

    public JPopupMenu createPublishedByPopup(final User user) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        final PopupItemFactory popupItemFactory = PopupItemFactory.getInstance();

        final Data readData = new Data(1);
        readData.set(Read.DataKey.CONTACT_ID, user.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_READ, readData));
        return jPopupMenu;
    }

    public JPopupMenu createPublishedToPopup(final User user,
            final ArtifactReceipt receipt) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        final PopupItemFactory popupItemFactory = PopupItemFactory.getInstance();

        final Data readData = new Data(1);
        readData.set(Read.DataKey.CONTACT_ID, user.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_READ, readData));
        return jPopupMenu;
    }

    public JPopupMenu createVersionDocumentPopup(DocumentVersion version, Delta delta) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        final PopupItemFactory popupItemFactory = PopupItemFactory.getInstance();
        
        final Data openData = new Data(2);
        openData.set(OpenVersion.DataKey.DOCUMENT_ID, version.getArtifactId());
        openData.set(OpenVersion.DataKey.VERSION_ID, version.getVersionId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.DOCUMENT_OPEN_VERSION, openData));

        jPopupMenu.addSeparator();

        final Data printData = new Data(2);
        printData.set(com.thinkparity.ophelia.browser.platform.action.document.PrintVersion.DataKey.DOCUMENT_ID, version.getArtifactId());
        printData.set(com.thinkparity.ophelia.browser.platform.action.document.PrintVersion.DataKey.VERSION_ID, version.getVersionId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.DOCUMENT_PRINT_VERSION, printData));
        return jPopupMenu;
    }

    public JPopupMenu createVersionPopup(final ContainerVersion version) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        final PopupItemFactory popupItemFactory = PopupItemFactory.getInstance();
        
        if (version.isSetComment()) {
            final Data commentData = new Data(2);
            commentData.set(DisplayVersionInfo.DataKey.CONTAINER_ID, version.getArtifactId());
            commentData.set(DisplayVersionInfo.DataKey.VERSION_ID, version.getVersionId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_DISPLAY_VERSION_INFO, commentData));
            jPopupMenu.addSeparator();
        }
        
        if (model.isOnline()) {
            final Data shareData = new Data(2);
            shareData.set(PublishVersion.DataKey.CONTAINER_ID, version.getArtifactId());
            shareData.set(PublishVersion.DataKey.VERSION_ID, version.getVersionId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PUBLISH_VERSION, shareData));    
            jPopupMenu.addSeparator();
        }
        final Data exportData = new Data(2);
        exportData.set(com.thinkparity.ophelia.browser.platform.action.container.ExportVersion.DataKey.CONTAINER_ID, version.getArtifactId());
        exportData.set(com.thinkparity.ophelia.browser.platform.action.container.ExportVersion.DataKey.VERSION_ID, version.getVersionId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_EXPORT_VERSION, exportData));
        
        final Data printData = new Data(2);
        printData.set(com.thinkparity.ophelia.browser.platform.action.container.PrintVersion.DataKey.CONTAINER_ID, version.getArtifactId());
        printData.set(com.thinkparity.ophelia.browser.platform.action.container.PrintVersion.DataKey.VERSION_ID, version.getVersionId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PRINT_VERSION, printData));
        return jPopupMenu;
    }

    /**
     * Create a container popup menu.
     * 
     * @param container
     *            A <code>Container</code>.
     * @return A <code>JPopupMenu</code>.
     */
    JPopupMenu createContainerPopup(final Container container) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        // Publish, new draft, discard draft
        if (model.isOnline()) {
            boolean needSeparator = false;
            if (container.isLocalDraft()) {
                final Data publishData = new Data(1);
                publishData.set(Publish.DataKey.CONTAINER_ID, container.getId());
                jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PUBLISH, publishData));
                needSeparator = true;
            }
            if (!container.isDraft()) {
                final Data createDraftData = new Data(1);
                createDraftData.set(CreateDraft.DataKey.CONTAINER_ID, container.getId());
                jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_CREATE_DRAFT, createDraftData));  
                needSeparator = true;
            }
            if (container.isLocalDraft()) {
                final Data deleteDraftData = new Data(1);
                deleteDraftData.set(DeleteDraft.DataKey.CONTAINER_ID, container.getId());
                jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_DELETE_DRAFT, deleteDraftData));
                needSeparator = true;
            }
            if (needSeparator) {
                jPopupMenu.addSeparator();
            }
        }
        
        // Add document
        final Data addDocumentData = new Data(1);
        addDocumentData.set(AddDocument.DataKey.CONTAINER_ID, container.getId());
        addDocumentData.set(AddDocument.DataKey.FILES, new File[0]);
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_ADD_DOCUMENT, addDocumentData));
        
        jPopupMenu.addSeparator();
        
        // TODO rename

        // archive
        popupItemFactory.addPopupItem(jPopupMenu, PluginId.ARCHIVE, "ArchiveAction", container);

        // delete
        final Data deleteData = new Data(1);
        deleteData.set(Delete.DataKey.CONTAINER_ID, container.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_DELETE, deleteData));  
        
        jPopupMenu.addSeparator();

        // include the container's id and unique id in the menu
        if(model.isDevelopmentMode()) {
            final Clipboard systemClipboard =
                Toolkit.getDefaultToolkit().getSystemClipboard();
            final ActionListener debugActionListener = new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    final StringSelection stringSelection =
                        new StringSelection(((JComponent) e.getSource()).getClientProperty("COPY_ME").toString());
                    systemClipboard.setContents(stringSelection, null);
                }
            };
            final JMenuItem idJMenuItem = new JMenuItem("Id - " + container.getId());
            idJMenuItem.putClientProperty("COPY_ME", container.getId());
            idJMenuItem.addActionListener(debugActionListener);

            final JMenuItem uidJMenuItem = new JMenuItem("Unique id - " + container.getUniqueId());
            uidJMenuItem.putClientProperty("COPY_ME", container.getUniqueId());
            uidJMenuItem.addActionListener(debugActionListener);

            jPopupMenu.addSeparator();
            jPopupMenu.add(idJMenuItem);
            jPopupMenu.add(uidJMenuItem);
        }

        // Export
        final Data exportData = new Data(1);
        exportData.set(com.thinkparity.ophelia.browser.platform.action.container.Export.DataKey.CONTAINER_ID, container.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_EXPORT, exportData));
        return jPopupMenu;
    }


}
