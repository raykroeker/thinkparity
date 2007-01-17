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
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerDraft.ArtifactState;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view.DocumentView;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.DefaultPopupDelegate;
import com.thinkparity.ophelia.browser.platform.action.contact.Read;
import com.thinkparity.ophelia.browser.platform.action.container.AddDocument;
import com.thinkparity.ophelia.browser.platform.action.container.Archive;
import com.thinkparity.ophelia.browser.platform.action.container.CreateDraft;
import com.thinkparity.ophelia.browser.platform.action.container.Delete;
import com.thinkparity.ophelia.browser.platform.action.container.DeleteDraft;
import com.thinkparity.ophelia.browser.platform.action.container.DisplayVersionInfo;
import com.thinkparity.ophelia.browser.platform.action.container.PrintDraft;
import com.thinkparity.ophelia.browser.platform.action.container.Publish;
import com.thinkparity.ophelia.browser.platform.action.container.PublishVersion;
import com.thinkparity.ophelia.browser.platform.action.container.RemoveDocument;
import com.thinkparity.ophelia.browser.platform.action.container.Rename;
import com.thinkparity.ophelia.browser.platform.action.container.RenameDocument;
import com.thinkparity.ophelia.browser.platform.action.container.RevertDocument;
import com.thinkparity.ophelia.browser.platform.action.container.UndeleteDocument;
import com.thinkparity.ophelia.browser.platform.action.document.Open;
import com.thinkparity.ophelia.browser.platform.action.document.OpenVersion;
import com.thinkparity.ophelia.browser.platform.action.profile.Update;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityMenuItem;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ContainerTabPopupDelegate extends DefaultPopupDelegate implements
        TabPanelPopupDelegate, PopupDelegate {

    /** A <code>ContainerModel</code>. */
    private final ContainerTabModel model;

    /**
     * Create ContainerTabPopupFactory.
     * 
     * @param model
     *            The <code>ContainerModel</code>.
     */
    ContainerTabPopupDelegate(final ContainerTabModel model) {
        super();
        this.model = model;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showForContainer(com.thinkparity.codebase.model.container.Container)
     *
     */
    public void showForContainer(final Container container) {
        if (isOnline()) {
            boolean needSeparator = false;
            if (container.isLocalDraft()) {
                final Data publishData = new Data(1);
                publishData.set(Publish.DataKey.CONTAINER_ID, container.getId());
                add(ActionId.CONTAINER_PUBLISH, publishData);
                needSeparator = true;
            }
            if (!container.isDraft() && container.isLatest()) {
                final Data createDraftData = new Data(1);
                createDraftData.set(CreateDraft.DataKey.CONTAINER_ID, container.getId());
                add(ActionId.CONTAINER_CREATE_DRAFT, createDraftData);  
                needSeparator = true;
            }
            if (container.isLocalDraft()) {
                final Data deleteDraftData = new Data(2);
                deleteDraftData.set(DeleteDraft.DataKey.CONTAINER_ID, container.getId());
                deleteDraftData.set(DeleteDraft.DataKey.CONFIRM, Boolean.TRUE);
                add(ActionId.CONTAINER_DELETE_DRAFT, deleteDraftData);
                needSeparator = true;
            }
            if (needSeparator) {
                addSeparator();
            }
        }

        // Add document
        if (container.isLocalDraft()) {
            final Data addDocumentData = new Data(1);
            addDocumentData.set(AddDocument.DataKey.CONTAINER_ID, container.getId());
            addDocumentData.set(AddDocument.DataKey.FILES, new File[0]);
            add(ActionId.CONTAINER_ADD_DOCUMENT, addDocumentData);
            addSeparator();
        }

        // Rename container
        if (!isDistributed(container.getId())) {
            final Data renameData = new Data(1);
            renameData.set(Rename.DataKey.CONTAINER_ID, container.getId());
            add(ActionId.CONTAINER_RENAME, renameData);
        }

        if (isDistributed(container.getId()) && !container.isLocalDraft()) {
            final Data archiveData = new Data(1);
            archiveData.set(Archive.DataKey.CONTAINER_ID, container.getId());
            add(ActionId.CONTAINER_ARCHIVE, archiveData);
        }

        // delete
        final Data deleteData = new Data(1);
        deleteData.set(Delete.DataKey.CONTAINER_ID, container.getId());
        add(ActionId.CONTAINER_DELETE, deleteData);  


        // export
        // This menu is not shown if there is a new package with no draft and no versions.
        if (container.isLocalDraft() || isDistributed(container.getId())) {
            addSeparator();
            final Data exportData = new Data(1);
            exportData.set(com.thinkparity.ophelia.browser.platform.action.container.Export.DataKey.CONTAINER_ID, container.getId());
            add(ActionId.CONTAINER_EXPORT, exportData);
        }

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
            final JMenuItem idJMenuItem = new ThinkParityMenuItem(MessageFormat.format("getId():{0,number,#}", container.getId()));
            idJMenuItem.putClientProperty("COPY_ME", container.getId());
            idJMenuItem.addActionListener(debugActionListener);

            final JMenuItem uidJMenuItem = new ThinkParityMenuItem(MessageFormat.format("getUniqueId():{0}", container.getUniqueId()));
            uidJMenuItem.putClientProperty("COPY_ME", container.getUniqueId());
            uidJMenuItem.addActionListener(debugActionListener);

            addSeparator();
            add(idJMenuItem);
            add(uidJMenuItem);
            add(MessageFormat.format("isBookmarked:{0}", container.isBookmarked()));
            add(MessageFormat.format("isDraft:{0}", container.isDraft()));
            add(MessageFormat.format("isLatest:{0}", container.isLatest()));
            add(MessageFormat.format("isLocalDraft:{0}", container.isLocalDraft()));
            add(MessageFormat.format("isSeen:{0}", container.isSeen()));
        }
        show();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabPopupDelegate#showForDraft(com.thinkparity.codebase.model.container.Container,
     *      com.thinkparity.ophelia.model.container.ContainerDraft)
     * 
     */
    public void showForDraft(final Container container, final ContainerDraft draft) {
        
        final List<Document> documents = draft.getDocuments();
        
        // Open submenu, documents
        for (final Document document : documents) {
            final Data openData = new Data(1);
            openData.set(Open.DataKey.DOCUMENT_ID, document.getId());
            add(ActionId.DOCUMENT_OPEN, document.getName(), openData);
        }
        if (documents.size() > 0) {
            addSeparator();
        }
        
        // Publish, delete draft
        if (isOnline()) {
            final Data publishData = new Data(1);
            publishData.set(Publish.DataKey.CONTAINER_ID, draft.getContainerId());
            add(ActionId.CONTAINER_PUBLISH, publishData);

            final Data deleteData = new Data(2);
            deleteData.set(DeleteDraft.DataKey.CONTAINER_ID, draft.getContainerId());
            deleteData.set(DeleteDraft.DataKey.CONFIRM, Boolean.TRUE);
            add(ActionId.CONTAINER_DELETE_DRAFT, deleteData);

            addSeparator();
        }
        
        // Add document
        final Data addDocumentData = new Data(2);
        addDocumentData.set(AddDocument.DataKey.CONTAINER_ID, draft.getContainerId());
        addDocumentData.set(AddDocument.DataKey.FILES, new File[0]);
        add(ActionId.CONTAINER_ADD_DOCUMENT, addDocumentData);

        // Rename document
        for (final Document document : documents) {
            if (ArtifactState.ADDED == draft.getState(document)) {
                final Data renameData = new Data(2);
                renameData.set(RenameDocument.DataKey.CONTAINER_ID, draft.getContainerId());
                renameData.set(RenameDocument.DataKey.DOCUMENT_ID, document.getId());
                add(ActionId.CONTAINER_RENAME_DOCUMENT, document.getName(), renameData);
            }
        }
        
        // Discard changes
        for (final Document document : documents) {
            if (ArtifactState.MODIFIED == draft.getState(document)) {
                final Data revertData = new Data(2);
                revertData.set(RevertDocument.DataKey.CONTAINER_ID, draft.getContainerId());
                revertData.set(RevertDocument.DataKey.DOCUMENT_ID, document.getId());
                add(ActionId.CONTAINER_REVERT_DOCUMENT, document.getName(), revertData);
            }
        }
        
        // Undelete
        for (final Document document : documents) {
            if (ArtifactState.REMOVED == draft.getState(document)) {
                final Data undeleteData = new Data(2);
                undeleteData.set(UndeleteDocument.DataKey.CONTAINER_ID, draft.getContainerId());
                undeleteData.set(UndeleteDocument.DataKey.DOCUMENT_ID, document.getId());
                add(ActionId.CONTAINER_UNDELETE_DOCUMENT, document.getName(), undeleteData);
            }
        }
        
        // Remove document
        for (final Document document : documents) {
            if (ArtifactState.REMOVED != draft.getState(document)) {
                final Data removeData = new Data(2);
                removeData.set(RemoveDocument.DataKey.CONTAINER_ID, draft.getContainerId());
                removeData.set(RemoveDocument.DataKey.DOCUMENT_ID, document.getId());
                add(ActionId.CONTAINER_REMOVE_DOCUMENT, document.getName(), removeData);
            }
        }
        
        if (documents.size() > 0) {
            addSeparator();
        }
        
        // Print
        if (documents.size() > 1) {
            final Data printData = new Data(1);
            printData.set(PrintDraft.DataKey.CONTAINER_ID, draft.getContainerId());
            add(ActionId.DOCUMENT_PRINT_DRAFT, ActionId.CONTAINER_PRINT_DRAFT, printData);
            addSeparator(ActionId.DOCUMENT_PRINT_DRAFT);           
        }
       
        for (final Document document : documents) {
            final Data documentPrintData = new Data(1);
            documentPrintData.set(com.thinkparity.ophelia.browser.platform.action.document.PrintDraft.DataKey.DOCUMENT_ID, document.getId());
            add(ActionId.DOCUMENT_PRINT_DRAFT, document.getName(), documentPrintData);     
        }
        
        show();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate#showForPanel(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     *
     */
    public void showForPanel(final TabPanel tabPanel) {
        showForContainer(((ContainerPanel) tabPanel).getContainer());
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showForVersion(com.thinkparity.codebase.model.container.ContainerVersion,
     *      java.util.Map, java.util.Map,
     *      com.thinkparity.codebase.model.user.User)
     * 
     */
    public void showForVersion(final ContainerVersion version,
            final List<DocumentView> documentViews,
            final Map<User, ArtifactReceipt> publishedTo, final User publishedBy) {
        
        // Open submenu, documents
        for (final DocumentView documentView : documentViews) {
            final Data openData = new Data(2);
            openData.set(OpenVersion.DataKey.DOCUMENT_ID, documentView.getDocumentId());
            openData.set(OpenVersion.DataKey.VERSION_ID, documentView.getVersionId());
            add(ActionId.DOCUMENT_OPEN_VERSION, documentView.getDocumentName(), openData);
        }
        if (documentViews.size() > 0) {
            addSeparator(ActionId.DOCUMENT_OPEN_VERSION);
        }
        
        // Open submenu, publisher
        if (isLocalUser(publishedBy)) {
            final Data data = new Data(1);
            data.set(Update.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
            add(ActionId.PROFILE_UPDATE, publishedBy.getName(), data);
        } else {
            final Data data = new Data(1);
            data.set(Read.DataKey.CONTACT_ID, publishedBy.getId());
            add(ActionId.CONTACT_READ, publishedBy.getName(), data);
        }
        
        // Open submenu, users
        for (final Entry<User, ArtifactReceipt> entry : publishedTo.entrySet()) {
            if (isLocalUser(entry.getKey())) {
                final Data data = new Data(1);
                data.set(Update.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
                add(ActionId.PROFILE_UPDATE, entry.getKey().getName(), data);
            } else {
                final Data data = new Data(1);
                data.set(Read.DataKey.CONTACT_ID, entry.getKey().getId());
                add(ActionId.CONTACT_READ, entry.getKey().getName(), data);
            }
        }
        
        addSeparator();
        
        // Show version comment
        if (version.isSetComment()) {
            final Data commentData = new Data(2);
            commentData.set(DisplayVersionInfo.DataKey.CONTAINER_ID, version.getArtifactId());
            commentData.set(DisplayVersionInfo.DataKey.VERSION_ID, version.getVersionId());
            add(ActionId.CONTAINER_DISPLAY_VERSION_INFO, commentData);
        }
        
        // Forward
        if (isOnline()) {
            final Data shareData = new Data(2);
            shareData.set(PublishVersion.DataKey.CONTAINER_ID, version.getArtifactId());
            shareData.set(PublishVersion.DataKey.VERSION_ID, version.getVersionId());
            add(ActionId.CONTAINER_PUBLISH_VERSION, shareData);    
        }
        
        // Export
        final Data exportData = new Data(2);
        exportData.set(com.thinkparity.ophelia.browser.platform.action.container.ExportVersion.DataKey.CONTAINER_ID, version.getArtifactId());
        exportData.set(com.thinkparity.ophelia.browser.platform.action.container.ExportVersion.DataKey.VERSION_ID, version.getVersionId());
        add(ActionId.CONTAINER_EXPORT_VERSION, exportData);
        
        if (documentViews.size() > 0) {
            addSeparator();
        }
        
        // Print
        if (documentViews.size() > 1) {
            final Data printData = new Data(2);
            printData.set(com.thinkparity.ophelia.browser.platform.action.container.PrintVersion.DataKey.CONTAINER_ID, version.getArtifactId());
            printData.set(com.thinkparity.ophelia.browser.platform.action.container.PrintVersion.DataKey.VERSION_ID, version.getVersionId());
            add(ActionId.DOCUMENT_PRINT_VERSION, ActionId.CONTAINER_PRINT_VERSION, printData);        
            addSeparator(ActionId.DOCUMENT_PRINT_VERSION);
        }
        
        for (final DocumentView documentView : documentViews) {
            final Data documentVersionPrintData = new Data(2);
            documentVersionPrintData.set(com.thinkparity.ophelia.browser.platform.action.document.PrintVersion.DataKey.DOCUMENT_ID, documentView.getDocumentId());
            documentVersionPrintData.set(com.thinkparity.ophelia.browser.platform.action.document.PrintVersion.DataKey.VERSION_ID, documentView.getVersionId());
            add(ActionId.DOCUMENT_PRINT_VERSION, documentView.getDocumentName(), documentVersionPrintData);           
        }    

        show();
    }

    /**
     * Determine if the container has been distributed.
     * 
     * @param containerId
     *            A <code>Long</code>.
     * @return True if this container has been distributed; false otherwise.
     */
    private boolean isDistributed(final Long containerId) {
        return model.readIsDistributed(containerId);
    }

    /**
     * Determine if the specified user is the local user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return True if this is the local user; false otherwise.
     */
    private boolean isLocalUser(final User user) {
        return model.readIsLocalUser(user).booleanValue();
    }

    /**
     * Determine if online.
     * 
     * @return True if online; false otherwise.
     */
    private boolean isOnline() {
        return model.isOnline().booleanValue();
    }
}
