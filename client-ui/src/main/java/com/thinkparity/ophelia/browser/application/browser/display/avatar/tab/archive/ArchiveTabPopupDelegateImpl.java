/*
 * Created On:  2007-01-17 11:57:00
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.archive;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.archive.ArchiveTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.archive.ArchiveTabPopupDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view.DocumentView;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.DefaultPopupDelegate;
import com.thinkparity.ophelia.browser.platform.action.contact.Read;
import com.thinkparity.ophelia.browser.platform.action.container.Delete;
import com.thinkparity.ophelia.browser.platform.action.container.DisplayVersionInfo;
import com.thinkparity.ophelia.browser.platform.action.container.Restore;
import com.thinkparity.ophelia.browser.platform.action.document.OpenVersion;
import com.thinkparity.ophelia.browser.platform.action.profile.Update;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityMenuItem;

/**
 * <b>Title:</b>thinkParity Archive Tab Popup Delegate Implementation<br>
 * <b>Description:</b>Provides an archive tab popup implementation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ArchiveTabPopupDelegateImpl extends DefaultPopupDelegate implements
        TabPanelPopupDelegate, ArchiveTabPopupDelegate {

    /** A <code>ArchiveTabModel</code>. */
    private final ArchiveTabModel model;

    /**
     * Create ArchiveTabPopupDelegateImpl.
     * 
     * @param model
     *            The <code>ArchiveTabModel</code>.
     */
    ArchiveTabPopupDelegateImpl(final ArchiveTabModel model) {
        super();
        this.model = model;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showForContainer(com.thinkparity.codebase.model.container.Container)
     *
     */
    public void showForContainer(final Container container) {
        // restore
        if (isDistributed(container.getId()) && !container.isLocalDraft()) {
            final Data archiveData = new Data(1);
            archiveData.set(Restore.DataKey.CONTAINER_ID, container.getId());
            add(ActionId.CONTAINER_RESTORE, archiveData);
        }
        // delete
        final Data deleteData = new Data(1);
        deleteData.set(Delete.DataKey.CONTAINER_ID, container.getId());
        add(ActionId.CONTAINER_DELETE, deleteData);  
        // export
        if (container.isLocalDraft() || isDistributed(container.getId())) {
            addSeparator();
            final Data exportData = new Data(1);
            exportData.set(com.thinkparity.ophelia.browser.platform.action.container.Export.DataKey.CONTAINER_ID, container.getId());
            add(ActionId.CONTAINER_EXPORT, exportData);
        }
        // debugging info
        if (model.isDevelopmentMode()) {
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
    public void showForDraft(final Container container,
            final ContainerDraft draft) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate#showForPanel(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     *
     */
    public void showForPanel(final TabPanel tabPanel) {
        showForContainer(((ArchiveTabPanel) tabPanel).getContainer());
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
        // open document versions
        for (final DocumentView documentView : documentViews) {
            final Data openData = new Data(2);
            openData.set(OpenVersion.DataKey.DOCUMENT_ID, documentView.getDocumentId());
            openData.set(OpenVersion.DataKey.VERSION_ID, documentView.getVersionId());
            add(ActionId.DOCUMENT_OPEN_VERSION, documentView.getDocumentName(), openData);
        }
        if (documentViews.size() > 0) {
            addSeparator(ActionId.DOCUMENT_OPEN_VERSION);
        }
        // update profile/read contact
        if (isLocalUser(publishedBy)) {
            final Data data = new Data(1);
            data.set(Update.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
            add(ActionId.PROFILE_UPDATE, publishedBy.getName(), data);
        } else {
            final Data data = new Data(1);
            data.set(Read.DataKey.CONTACT_ID, publishedBy.getId());
            add(ActionId.CONTACT_READ, publishedBy.getName(), data);
        }
        // update profile/read contacts
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
        // display version info
        if (version.isSetComment()) {
            final Data commentData = new Data(2);
            commentData.set(DisplayVersionInfo.DataKey.CONTAINER_ID, version.getArtifactId());
            commentData.set(DisplayVersionInfo.DataKey.VERSION_ID, version.getVersionId());
            add(ActionId.CONTAINER_DISPLAY_VERSION_INFO, commentData);
        }
        // export version
        final Data exportData = new Data(2);
        exportData.set(com.thinkparity.ophelia.browser.platform.action.container.ExportVersion.DataKey.CONTAINER_ID, version.getArtifactId());
        exportData.set(com.thinkparity.ophelia.browser.platform.action.container.ExportVersion.DataKey.VERSION_ID, version.getVersionId());
        add(ActionId.CONTAINER_EXPORT_VERSION, exportData);
        if (documentViews.size() > 0) {
            addSeparator();
        }
        // print
        if (documentViews.size() > 1) {
            final Data printData = new Data(2);
            printData.set(com.thinkparity.ophelia.browser.platform.action.container.PrintVersion.DataKey.CONTAINER_ID, version.getArtifactId());
            printData.set(com.thinkparity.ophelia.browser.platform.action.container.PrintVersion.DataKey.VERSION_ID, version.getVersionId());
            add(ActionId.DOCUMENT_PRINT_VERSION, ActionId.CONTAINER_PRINT_VERSION, printData);        
            addSeparator(ActionId.DOCUMENT_PRINT_VERSION);
        }
        // print versions
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
}
