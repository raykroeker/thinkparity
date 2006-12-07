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

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarPopupDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerTabModel.Ordering;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.DefaultPopupDelegate;
import com.thinkparity.ophelia.browser.platform.action.contact.Read;
import com.thinkparity.ophelia.browser.platform.action.container.*;
import com.thinkparity.ophelia.browser.platform.action.document.Open;
import com.thinkparity.ophelia.browser.platform.action.document.OpenVersion;
import com.thinkparity.ophelia.browser.platform.action.profile.Update;
import com.thinkparity.ophelia.browser.platform.plugin.PluginId;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityMenuItem;
import com.thinkparity.ophelia.model.container.ContainerDraft;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ContainerTabPopupDelegate extends DefaultPopupDelegate implements
        TabAvatarPopupDelegate, TabPanelPopupDelegate, PopupDelegate {

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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabPopupDelegate#showForDocument(com.thinkparity.ophelia.model.container.ContainerDraft,
     *      com.thinkparity.codebase.model.document.Document)
     * 
     */
    public void showForDocument(final ContainerDraft draft,
            final Document document) {
        final ContainerDraft.ArtifactState state = draft.getState(document);

        final Data openData = new Data(1);
        openData.set(Open.DataKey.DOCUMENT_ID, document.getId());
        add(ActionId.DOCUMENT_OPEN, openData);

        addSeparator();
        final Data renameData = new Data(2);
        renameData.set(RenameDocument.DataKey.CONTAINER_ID, draft.getContainerId());
        renameData.set(RenameDocument.DataKey.DOCUMENT_ID, document.getId());
        add(ActionId.CONTAINER_RENAME_DOCUMENT, renameData);
        
        switch (state) {
        case ADDED:
            break;
        case MODIFIED:
            final Data revertData = new Data(2);
            revertData.set(RevertDocument.DataKey.CONTAINER_ID, draft.getContainerId());
            revertData.set(RevertDocument.DataKey.DOCUMENT_ID, document.getId());
            add(ActionId.CONTAINER_REVERT_DOCUMENT, revertData);
            break;
        case NONE:
            break;
        case REMOVED:
            final Data undeleteData = new Data(2);
            undeleteData.set(UndeleteDocument.DataKey.CONTAINER_ID, draft.getContainerId());
            undeleteData.set(UndeleteDocument.DataKey.DOCUMENT_ID, document.getId());
            add(ActionId.CONTAINER_UNDELETE_DOCUMENT, undeleteData);
            break;
        default:
            throw Assert.createUnreachable("UNKNOWN ARTIFACT STATE");
        }
        
        if (ContainerDraft.ArtifactState.REMOVED != state) {
            final Data removeData = new Data(2);
            removeData.set(RemoveDocument.DataKey.CONTAINER_ID, draft.getContainerId());
            removeData.set(RemoveDocument.DataKey.DOCUMENT_ID, document.getId());
            add(ActionId.CONTAINER_REMOVE_DOCUMENT, removeData);
        }
        
        addSeparator();
        final Data printData = new Data(1);
        printData.set(com.thinkparity.ophelia.browser.platform.action.document.PrintDraft.DataKey.DOCUMENT_ID, document.getId());
        add(ActionId.DOCUMENT_PRINT_DRAFT, printData);

        show();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabPopupDelegate#showForDocument(com.thinkparity.codebase.model.document.DocumentVersion,
     *      com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta)
     * 
     */
    public void showForDocument(final DocumentVersion version, final Delta delta) {
        final Data openData = new Data(2);
        openData.set(OpenVersion.DataKey.DOCUMENT_ID, version.getArtifactId());
        openData.set(OpenVersion.DataKey.VERSION_ID, version.getVersionId());
        add(ActionId.DOCUMENT_OPEN_VERSION, openData);

        addSeparator();
        final Data printData = new Data(2);
        printData.set(com.thinkparity.ophelia.browser.platform.action.document.PrintVersion.DataKey.DOCUMENT_ID, version.getArtifactId());
        printData.set(com.thinkparity.ophelia.browser.platform.action.document.PrintVersion.DataKey.VERSION_ID, version.getVersionId());
        add(ActionId.DOCUMENT_PRINT_VERSION, printData);

        show();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabPopupDelegate#showForDraft(com.thinkparity.codebase.model.container.Container,
     *      com.thinkparity.ophelia.model.container.ContainerDraft)
     * 
     */
    public void showForDraft(final Container container, final ContainerDraft draft) {
        if (container.isLocalDraft()) {
            if(isOnline()) {
                final Data publishData = new Data(1);
                publishData.set(Publish.DataKey.CONTAINER_ID, draft.getContainerId());
                add(ActionId.CONTAINER_PUBLISH, publishData);
    
                final Data deleteData = new Data(1);
                deleteData.set(DeleteDraft.DataKey.CONTAINER_ID, draft.getContainerId());
                add(ActionId.CONTAINER_DELETE_DRAFT, deleteData);
                addSeparator();
            }
            final Data addDocumentData = new Data(2);
            addDocumentData.set(AddDocument.DataKey.CONTAINER_ID, draft.getContainerId());
            addDocumentData.set(AddDocument.DataKey.FILES, new File[0]);
            add(ActionId.CONTAINER_ADD_DOCUMENT, addDocumentData);
            
            addSeparator();
            final Data printData = new Data(1);
            printData.set(PrintDraft.DataKey.CONTAINER_ID, draft.getContainerId());
            add(ActionId.CONTAINER_PRINT_DRAFT, printData);

            show();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate#showForPanel(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     *
     */
    public void showForPanel(final TabPanel tabPanel) {
        showForContainerPanel((ContainerPanel) tabPanel);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarPopupDelegate#showForSort()
     *
     */
    public void showForSort() {
        for (final Ordering ordering : Ordering.values()) {
            add(ordering);
        }
        show();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabPopupDelegate#showForUser(com.thinkparity.codebase.model.user.User)
     * 
     */
    public void showForUser(final User user) {
        if (isLocalUser(user)) {
            final Data data = new Data(1);
            data.set(Update.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
            add(ActionId.PROFILE_UPDATE, data);
            show();
        } else {
            final Data data = new Data(1);
            data.set(Read.DataKey.CONTACT_ID, user.getId());
            add(ActionId.CONTACT_READ, data);
            show();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabPopupDelegate#showForVersion(com.thinkparity.codebase.model.container.ContainerVersion)
     * 
     */
    public void showForVersion(final ContainerVersion version) {
        if (version.isSetComment()) {
            final Data commentData = new Data(2);
            commentData.set(DisplayVersionInfo.DataKey.CONTAINER_ID, version.getArtifactId());
            commentData.set(DisplayVersionInfo.DataKey.VERSION_ID, version.getVersionId());
            add(ActionId.CONTAINER_DISPLAY_VERSION_INFO, commentData);
            addSeparator();
        }
        
        if (isOnline()) {
            final Data shareData = new Data(2);
            shareData.set(PublishVersion.DataKey.CONTAINER_ID, version.getArtifactId());
            shareData.set(PublishVersion.DataKey.VERSION_ID, version.getVersionId());
            add(ActionId.CONTAINER_PUBLISH_VERSION, shareData);    
            addSeparator();
        }
        final Data exportData = new Data(2);
        exportData.set(com.thinkparity.ophelia.browser.platform.action.container.ExportVersion.DataKey.CONTAINER_ID, version.getArtifactId());
        exportData.set(com.thinkparity.ophelia.browser.platform.action.container.ExportVersion.DataKey.VERSION_ID, version.getVersionId());
        add(ActionId.CONTAINER_EXPORT_VERSION, exportData);
        
        final Data printData = new Data(2);
        printData.set(com.thinkparity.ophelia.browser.platform.action.container.PrintVersion.DataKey.CONTAINER_ID, version.getArtifactId());
        printData.set(com.thinkparity.ophelia.browser.platform.action.container.PrintVersion.DataKey.VERSION_ID, version.getVersionId());
        add(ActionId.CONTAINER_PRINT_VERSION, printData);

        show();
    }

    /**
     * Create a sort by menu item.
     * 
     * @param ordering
     *            The model tab <code>Ordering</code>.
     * @return A <code>JMenuItem</code>.
     */
    private void add(final Ordering ordering) {
        // TODO localize ordering
        final JCheckBoxMenuItem item = new JCheckBoxMenuItem(ordering.toString());
        item.setSelected(isSortApplied(ordering));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final JMenuItem menuItem = (JMenuItem) e.getSource();
                if (menuItem.isSelected())
                    model.applySort(ordering);
                else
                    model.removeSort(ordering);
            }
        });
        add(item);
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

    /**
     * Determine whether or not an order is applied.
     * 
     * @param ordering
     *            An <code>Ordering</code>.
     * @return True if the ordering is applied.
     */
    private boolean isSortApplied(final Ordering ordering) {
        return model.isSortApplied(ordering).booleanValue();
    }

    /**
     * Show the popup for a container panel.
     * 
     * @param containerPanel
     *            A <code>ContainerPanel</code>.
     */
    private void showForContainerPanel(final ContainerPanel containerPanel) {
        final Container container = containerPanel.getContainer();
        if (isOnline()) {
            boolean needSeparator = false;
            if (container.isLocalDraft()) {
                final Data publishData = new Data(1);
                publishData.set(Publish.DataKey.CONTAINER_ID, container.getId());
                add(ActionId.CONTAINER_PUBLISH, publishData);
                needSeparator = true;
            }
            if (!container.isDraft()) {
                final Data createDraftData = new Data(1);
                createDraftData.set(CreateDraft.DataKey.CONTAINER_ID, container.getId());
                add(ActionId.CONTAINER_CREATE_DRAFT, createDraftData);  
                needSeparator = true;
            }
            if (container.isLocalDraft()) {
                final Data deleteDraftData = new Data(1);
                deleteDraftData.set(DeleteDraft.DataKey.CONTAINER_ID, container.getId());
                add(ActionId.CONTAINER_DELETE_DRAFT, deleteDraftData);
                needSeparator = true;
            }
            if (needSeparator) {
                addSeparator();
            }
        }

        // Add document
        final Data addDocumentData = new Data(1);
        addDocumentData.set(AddDocument.DataKey.CONTAINER_ID, container.getId());
        addDocumentData.set(AddDocument.DataKey.FILES, new File[0]);
        add(ActionId.CONTAINER_ADD_DOCUMENT, addDocumentData);
        addSeparator();

        // Rename container
        if (container.isLocalDraft() && !isDistributed(container.getId())) {
            final Data renameData = new Data(1);
            renameData.set(Rename.DataKey.CONTAINER_ID, container.getId());
            add(ActionId.CONTAINER_RENAME, renameData);
        }

        // archive
        add(PluginId.ARCHIVE, "ArchiveAction", container);

        // delete
        final Data deleteData = new Data(1);
        deleteData.set(Delete.DataKey.CONTAINER_ID, container.getId());
        add(ActionId.CONTAINER_DELETE, deleteData);  
        addSeparator();

        // export
        final Data exportData = new Data(1);
        exportData.set(com.thinkparity.ophelia.browser.platform.action.container.Export.DataKey.CONTAINER_ID, container.getId());
        add(ActionId.CONTAINER_EXPORT, exportData);

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
        }
        show();
    }
}
