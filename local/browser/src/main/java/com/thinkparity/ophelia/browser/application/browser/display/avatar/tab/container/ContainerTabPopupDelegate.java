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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerDraft.ArtifactState;

import com.thinkparity.ophelia.browser.application.browser.DefaultBrowserPopupDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view.DocumentView;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.CreateOutgoingUserInvitation;
import com.thinkparity.ophelia.browser.platform.action.container.*;
import com.thinkparity.ophelia.browser.platform.action.document.Open;
import com.thinkparity.ophelia.browser.platform.action.document.OpenVersion;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityMenuItem;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ContainerTabPopupDelegate extends DefaultBrowserPopupDelegate
        implements TabPanelPopupDelegate, PopupDelegate {

    /** An empty List of <code>DocumentView</code>. */
    private static final List<DocumentView> EMPTY_DOCUMENT_VIEWS;

    static {
        EMPTY_DOCUMENT_VIEWS = Collections.emptyList();
    }

    /** A list of action ids, used for the container popup. */
    private final List<ActionId> actionIds;

    /** A list of data, used for the container popup. */
    private final List<Data> dataList;

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
        this.actionIds = new ArrayList<ActionId>();
        this.dataList = new ArrayList<Data>();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showAll(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft)
     */
    public void showAll(final Container container, final ContainerDraft draft) {
        showAll(container, draft, null, EMPTY_DOCUMENT_VIEWS, Boolean.FALSE, null, null, null);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showAll(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft, com.thinkparity.codebase.model.container.ContainerVersion, java.util.List, java.lang.Boolean)
     */
    public void showAll(final Container container, final ContainerDraft draft,
            final ContainerVersion version,
            final List<DocumentView> documentViews, final Boolean latestVersion) {
        showAll(container, draft, version, documentViews, latestVersion, null, null, null);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showAll(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft, com.thinkparity.codebase.model.container.ContainerVersion, java.util.List, java.lang.Boolean, com.thinkparity.codebase.model.document.DocumentVersion)
     */
    public void showAll(final Container container, final ContainerDraft draft,
            final ContainerVersion version,
            final List<DocumentView> documentViews,
            final Boolean latestVersion, final DocumentVersion documentVersion) {
        showAll(container, draft, version, documentViews, latestVersion, null, documentVersion, null);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showAll(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft, com.thinkparity.codebase.model.container.ContainerVersion, java.util.List, java.lang.Boolean, com.thinkparity.codebase.model.user.User)
     */
    public void showAll(final Container container, final ContainerDraft draft,
            final ContainerVersion version,
            final List<DocumentView> documentViews,
            final Boolean latestVersion, final User user) {
        showAll(container, draft, version, documentViews, latestVersion, null, null, user);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showAll(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft, com.thinkparity.codebase.model.document.Document)
     */
    public void showAll(final Container container, final ContainerDraft draft, final Document document) {
        showAll(container, draft, null, EMPTY_DOCUMENT_VIEWS, Boolean.FALSE, document, null, null);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showForContainer(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft)
     */
    public void showForContainer(final Container container, final ContainerDraft draft) {
        final boolean online = isOnline();
        final boolean distributed = isDistributed(container.getId());
        boolean needSeparator = false;

        // create draft
        if (null == draft && (online || !distributed)) {
            final Data createDraftData = new Data(1);
            createDraftData.set(CreateDraft.DataKey.CONTAINER_ID, container.getId());
            addWithExpand(ActionId.CONTAINER_CREATE_DRAFT, createDraftData, container);  
            needSeparator = true;
        }

        // publish
        if (null != draft && online && isLocalDraftModified(container.getId())) {
            final Data publishData = new Data(2);
            publishData.set(Publish.DataKey.CONTAINER_ID, draft.getContainerId());
            publishData.set(Publish.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
            addWithExpand(ActionId.CONTAINER_PUBLISH, publishData, container);
            needSeparator = true;
        }

        // add document
        if (isLocalDraft(draft)) {
            final Data addDocumentData = new Data(2);
            addDocumentData.set(AddDocument.DataKey.CONTAINER_ID, draft.getContainerId());
            addDocumentData.set(AddDocument.DataKey.FILES, new File[0]);
            addWithExpand(ActionId.CONTAINER_ADD_DOCUMENT, addDocumentData, container);
            needSeparator = true;
        }

        // delete draft
        // this command is no longer allowed if the package has not been distributed
        if (isLocalDraft(draft) && distributed && online) {
            final Data deleteData = new Data(1);
            deleteData.set(DeleteDraft.DataKey.CONTAINER_ID, draft.getContainerId());
            add(ActionId.CONTAINER_DELETE_DRAFT, deleteData);
            needSeparator = true;
        }

        // separator
        if (needSeparator) {
            addSeparator();
            needSeparator = false;
        }

        // add container menus
        addForContainer(container);

        // include the container's id and unique id in the menu
        if (model.isDevelopmentMode()) {
            if (needSeparator) {
                addSeparator();
            }
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
            add(MessageFormat.format("isLatest:{0}", container.isLatest()));
            add(MessageFormat.format("isSeen:{0}", container.isSeen()));
        }
        show();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showForDocument(com.thinkparity.ophelia.model.container.ContainerDraft, com.thinkparity.codebase.model.document.Document)
     */
    public void showForDocument(final ContainerDraft draft, final Document document) {
        addForDocument(draft, document);
        show();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showForDocument(com.thinkparity.codebase.model.document.DocumentVersion)
     */
    public void showForDocument(final DocumentVersion documentVersion) {
        addForDocument(documentVersion);
        show();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabPopupDelegate#showForDraft(com.thinkparity.codebase.model.container.Container,
     *      com.thinkparity.ophelia.model.container.ContainerDraft)
     * 
     */
    public void showForDraft(final Container container, final ContainerDraft draft) {
        final boolean online = isOnline();

        // publish
        if (online && isLocalDraftModified(container.getId())) {
            final Data publishData = new Data(2);
            publishData.set(Publish.DataKey.CONTAINER_ID, draft.getContainerId());
            publishData.set(Publish.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
            add(ActionId.CONTAINER_PUBLISH, publishData);
        }

        addForDraft(container, draft);
        addSeparator();
        addForContainer(container);

        show();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showForUser(com.thinkparity.codebase.model.user.User)
     */
    public void showForUser(final User user) {
        addForUser(user);
        show();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showForVersion(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft, com.thinkparity.codebase.model.container.ContainerVersion, java.util.List, java.lang.Boolean)
     */
    public void showForVersion(final Container container,
            final ContainerDraft draft, final ContainerVersion version,
            final List<DocumentView> documentViews, final Boolean latestVersion) {
        final boolean online = isOnline();

        // create draft
        if (online && latestVersion && null == draft) {
            final Data createDraftData = new Data(1);
            createDraftData.set(CreateDraft.DataKey.CONTAINER_ID, container.getId());
            addWithExpand(ActionId.CONTAINER_CREATE_DRAFT, createDraftData, container);  
        }

        // publish version
        if (online) {
            final Data publishData = new Data(3);
            publishData.set(PublishVersion.DataKey.CONTAINER_ID, version.getArtifactId());
            publishData.set(PublishVersion.DataKey.VERSION_ID, version.getVersionId());
            publishData.set(PublishVersion.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
            add(ActionId.CONTAINER_PUBLISH_VERSION, publishData);
        }

        addForVersion(container, draft, version, documentViews, latestVersion);
        addSeparator();
        addForContainer(container);

        show();
    }

    /**
     * Add menus for a container.
     * 
     * @param container
     *            A <code>Container</code>.
     */
    private void addForContainer(final Container container) {
        final boolean online = isOnline();
        final boolean distributed = isDistributed(container.getId());

        // bookmark
        if (container.isBookmarked()) {
            final Data removeBookmarkData = new Data(1);
            removeBookmarkData.set(RemoveBookmark.DataKey.CONTAINER_ID, container.getId());
            add(ActionId.CONTAINER_REMOVE_BOOKMARK, removeBookmarkData);
        } else {
            final Data addBookmarkData = new Data(1);
            addBookmarkData.set(AddBookmark.DataKey.CONTAINER_ID, container.getId());
            add(ActionId.CONTAINER_ADD_BOOKMARK, addBookmarkData);
        }

        // Rename container
        if (!distributed) {
            final Data renameData = new Data(1);
            renameData.set(Rename.DataKey.CONTAINER_ID, container.getId());
            add(ActionId.CONTAINER_RENAME, renameData);
        }

        // delete
        // This menu is shown if online, or if it has never been published.
        if (online || !distributed) {
            final Data deleteData = new Data(1);
            deleteData.set(Delete.DataKey.CONTAINER_ID, container.getId());
            add(ActionId.CONTAINER_DELETE, deleteData);
        }

        // audit report and export
        if (distributed) {
            final Data reportData = new Data(1);
            reportData.set(ExportAuditReport.DataKey.CONTAINER_ID, container.getId());
            add(ActionId.CONTAINER_EXPORT_AUDIT_REPORT, reportData);
            
            final Data exportData = new Data(1);
            exportData.set(Export.DataKey.CONTAINER_ID, container.getId());
            add(ActionId.CONTAINER_EXPORT, exportData);
        }
    }

    /**
     * Add menus for a document.
     * 
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param document
     *            A <code>Document</code>.
     */
    private void addForDocument(final ContainerDraft draft, final Document document) {
        // open
        if (ArtifactState.REMOVED != draft.getState(document)) {
            final Data data = new Data(1);
            data.set(Open.DataKey.DOCUMENT_ID, document.getId());
            add(ActionId.DOCUMENT_OPEN, data);
        }

        // rename document
        if (ArtifactState.ADDED == draft.getState(document)) {
            final Data renameData = new Data(2);
            renameData.set(RenameDocument.DataKey.CONTAINER_ID, draft.getContainerId());
            renameData.set(RenameDocument.DataKey.DOCUMENT_ID, document.getId());
            add(ActionId.CONTAINER_RENAME_DOCUMENT, renameData);
        }

        // revert
        if (ArtifactState.MODIFIED == draft.getState(document)) {
            final Data revertData = new Data(2);
            revertData.set(RevertDocument.DataKey.CONTAINER_ID, draft.getContainerId());
            revertData.set(RevertDocument.DataKey.DOCUMENT_ID, document.getId());
            add(ActionId.CONTAINER_REVERT_DOCUMENT, revertData);
        }

        // undelete
        if (ArtifactState.REMOVED == draft.getState(document)) {
            final Data undeleteData = new Data(2);
            undeleteData.set(UndeleteDocument.DataKey.CONTAINER_ID, draft.getContainerId());
            undeleteData.set(UndeleteDocument.DataKey.DOCUMENT_ID, document.getId());
            add(ActionId.CONTAINER_UNDELETE_DOCUMENT, undeleteData);
        }

        // remove document
        if (ArtifactState.REMOVED != draft.getState(document)) {
            final Data removeData = new Data(2);
            removeData.set(RemoveDocument.DataKey.CONTAINER_ID, draft.getContainerId());
            removeData.set(RemoveDocument.DataKey.DOCUMENT_ID, document.getId());
            add(ActionId.CONTAINER_REMOVE_DOCUMENT, removeData);
        }

        // include the document's id and unique id in the menu
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
            final JMenuItem idJMenuItem = new ThinkParityMenuItem(MessageFormat.format("getId():{0,number,#}", document.getId()));
            idJMenuItem.putClientProperty("COPY_ME", document.getId());
            idJMenuItem.addActionListener(debugActionListener);

            final JMenuItem uidJMenuItem = new ThinkParityMenuItem(MessageFormat.format("getUniqueId():{0}", document.getUniqueId()));
            uidJMenuItem.putClientProperty("COPY_ME", document.getUniqueId());
            uidJMenuItem.addActionListener(debugActionListener);

            addSeparator();
            add(idJMenuItem);
            add(uidJMenuItem);
        }
    }

    /**
     * Add menus for a document version.
     * 
     * @param documentVersion
     *            A <code>DocumentVersion</code>.
     */
    private void addForDocument(final DocumentVersion documentVersion) {
        // open
        final Data data = new Data(2);
        data.set(OpenVersion.DataKey.DOCUMENT_ID, documentVersion.getArtifactId());
        data.set(OpenVersion.DataKey.VERSION_ID, documentVersion.getVersionId());
        add(ActionId.DOCUMENT_OPEN_VERSION, data);

        // include the document version's id version id and unique id in the menu
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
            final JMenuItem idJMenuItem = new ThinkParityMenuItem(MessageFormat.format("getId():{0,number,#}", documentVersion.getArtifactId()));
            idJMenuItem.putClientProperty("COPY_ME", documentVersion.getArtifactId());
            idJMenuItem.addActionListener(debugActionListener);

            final JMenuItem versionIdJMenuItem = new ThinkParityMenuItem(MessageFormat.format("getVersionId():{0,number,#}", documentVersion.getVersionId()));
            versionIdJMenuItem.putClientProperty("COPY_ME", documentVersion.getVersionId());
            versionIdJMenuItem.addActionListener(debugActionListener);

            final JMenuItem uidJMenuItem = new ThinkParityMenuItem(MessageFormat.format("getUniqueId():{0}", documentVersion.getArtifactUniqueId()));
            uidJMenuItem.putClientProperty("COPY_ME", documentVersion.getArtifactUniqueId());
            uidJMenuItem.addActionListener(debugActionListener);

            addSeparator();
            add(idJMenuItem);
            add(versionIdJMenuItem);
            add(uidJMenuItem);
        }
    }

    /**
     * Add menus for a draft.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     */
    private void addForDraft(final Container container, final ContainerDraft draft) {
        final boolean online = isOnline();

        // add document
        final Data addDocumentData = new Data(2);
        addDocumentData.set(AddDocument.DataKey.CONTAINER_ID, draft.getContainerId());
        addDocumentData.set(AddDocument.DataKey.FILES, new File[0]);
        add(ActionId.CONTAINER_ADD_DOCUMENT, addDocumentData);

        // update draft comment
        final Data data = new Data(2);
        data.set(UpdateDraftComment.DataKey.CONTAINER_ID, draft.getContainerId());
        data.set(UpdateDraftComment.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
        add(ActionId.CONTAINER_UPDATE_DRAFT_COMMENT, data);

        // delete draft
        // this command is no longer allowed if the package has not been distributed
        if (online && isDistributed(container.getId())) {
            final Data deleteData = new Data(1);
            deleteData.set(DeleteDraft.DataKey.CONTAINER_ID, draft.getContainerId());
            add(ActionId.CONTAINER_DELETE_DRAFT, deleteData);
        }

        // print
        final List<Document> documentsNotDeleted = getDocumentsNotDeleted(draft);
        if (documentsNotDeleted.size() > 0) {
            final Data printData = new Data(1);
            printData.set(PrintDraft.DataKey.CONTAINER_ID, draft.getContainerId());
            add(ActionId.CONTAINER_PRINT_DRAFT, printData);
        }
    }

    /**
     * Add menus for a user.
     * 
     * @param user
     *            A <code>User</code>.
     */
    private void addForUser(final User user) {
        // invite
        if (isOnline()) {
            if (!isLocalUser(user) && !doesExistContact(user)
                    && !doesExistOutgoingUserInvitation(user)
                    && isInviteAvailable(user)) {
                final Data data = new Data(1);
                data.set(CreateOutgoingUserInvitation.DataKey.USER_ID, user.getLocalId());
                add(ActionId.CONTACT_CREATE_OUTGOING_USER_INVITATION, data);
            }
        }

        // open
        if (isLocalUser(user)) {
            add(ActionId.PROFILE_UPDATE, Data.emptyData());
        } else {
            final Data data = new Data(1);
            data.set(ReadTeamMember.DataKey.USER_ID, user.getLocalId());
            add(ActionId.CONTAINER_READ_TEAM_MEMBER, data);
        }
    }

    /**
     * Add menus for a version.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param version
     *            A <code>ContainerVersion</code>, if a version is selected.
     * @param documentViews
     *            A list of <code>DocumentView</code>, if a version is selected.
     * @param latestVersion
     *            A <code>Boolean</code>, true for the latest version.
     */
    private void addForVersion(final Container container,
            final ContainerDraft draft, final ContainerVersion version,
            final List<DocumentView> documentViews, final Boolean latestVersion) {

        // display version comment
        if (version.isSetComment()) {
            final Data commentData = new Data(2);
            commentData.set(DisplayVersionInfo.DataKey.CONTAINER_ID, version.getArtifactId());
            commentData.set(DisplayVersionInfo.DataKey.VERSION_ID, version.getVersionId());
            add(ActionId.CONTAINER_DISPLAY_VERSION_INFO, commentData);
        }

        // print
        final List<DocumentView> documentViewsNotDeleted = getDocumentViewsNotDeleted(documentViews);
        if (documentViewsNotDeleted.size() > 0) {
            final Data printData = new Data(2);
            printData.set(com.thinkparity.ophelia.browser.platform.action.container.PrintVersion.DataKey.CONTAINER_ID, version.getArtifactId());
            printData.set(com.thinkparity.ophelia.browser.platform.action.container.PrintVersion.DataKey.VERSION_ID, version.getVersionId());
            add(ActionId.CONTAINER_PRINT_VERSION, printData);
        }

        // include the version's created on/updated on
        if (model.isDevelopmentMode()) {
            addSeparator();
            add(MessageFormat.format(
                    "getVersionId():{0,number,#}", version.getVersionId()));
            add(MessageFormat.format(
                    "createdOn():{0,date,yyyy-MM-dd HH:mm:ss.SSS Z}",
                    version.getCreatedOn().getTime()));
            add(MessageFormat.format(
                    "updatedOn():{0,date,yyyy-MM-dd HH:mm:ss.SSS Z}",
                    version.getUpdatedOn().getTime()));
        }
    }

    /**
     * Add an action to a popup menu.
     * A second action to expand the container is inserted.
     * 
     * @param actionId
     *            An <code>ActionId</code>.
     * @param data
     *            The <code>Data</code>.       
     * @param container
     *            The <code>Container</code>.            
     */
    private void addWithExpand(final ActionId actionId, final Data data,
            final Container container) {
        actionIds.clear();
        dataList.clear();

        final Data expandData = new Data(1);
        expandData.set(Expand.DataKey.CONTAINER_ID, container.getId());
        actionIds.add(ActionId.CONTAINER_EXPAND);
        dataList.add(expandData);

        actionIds.add(actionId);
        dataList.add(data);
        add(actionIds, dataList, 1);
    }

    /**
     * Determine if the specified user is a contact.
     * 
     * @param user
     *            A <code>User</code>.
     * @return True if this is the local user; false otherwise.
     */
    private boolean doesExistContact(final User user) {
        return model.readDoesExistContact(user).booleanValue();
    }

    /**
     * Determine if the specified user is a contact.
     * 
     * @param user
     *            A <code>User</code>.
     * @return True if this is the local user; false otherwise.
     */
    private boolean doesExistOutgoingUserInvitation(final User user) {
        return model.readDoesExistOutgoingUserInvitation(user).booleanValue();
    }

    /**
     * Get the list of documents that have not been deleted.
     * 
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @return A List of <code>Document</code>.
     */
    private List<Document> getDocumentsNotDeleted(final ContainerDraft draft) {
        final List<Document> documentsNotDeleted = new ArrayList<Document>();
        for (final Document document : draft.getDocuments()) {
            if (ArtifactState.REMOVED != draft.getState(document)) {
                documentsNotDeleted.add(document);
            }
        }
        return documentsNotDeleted;
    }

    /**
     * Get the list of document views that have not been deleted.
     * 
     * @param documentViews
     *            A list of <code>DocumentView</code>.
     * @return A List of <code>DocumentView</code>.
     */
    private List<DocumentView> getDocumentViewsNotDeleted(final List<DocumentView> documentViews) {
        final List<DocumentView> documentsNotDeleted = new ArrayList<DocumentView>();
        for (final DocumentView documentView : documentViews) {
            if (Delta.REMOVED != documentView.getDelta()) {
                documentsNotDeleted.add(documentView);
            }
        }
        return documentsNotDeleted;
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
     * Determine whether or not the invite user interface is enabled.
     * 
     * @param user
     *            A <code>User</code>.
     * @return True if the invite user interface is enabled.
     */
    private Boolean isInviteAvailable(final User user) {
        return model.readIsInviteAvailable(user);
    }

    /**
     * Determine if the draft is a local draft.
     * 
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @return True if the draft is not null and the draft is local.
     */
    private boolean isLocalDraft(final ContainerDraft draft) {
        return null != draft && draft.isLocal().booleanValue();
    }

    /**
     * Determine if the local draft is modified, ie. at least one document changed.
     * 
     * @param containerId
     *            A <code>Long</code>.
     * @return True if this container has a document that has been modified; false otherwise.
     */
    private boolean isLocalDraftModified(final Long containerId) {
        return model.readIsLocalDraftModified(containerId);
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
     * Determine whether or not the user experiences online behavior.
     * 
     * @return True if the user experiences online behavior.
     */
    private boolean isOnline() {
        return model.isOnlineUI().booleanValue();
    }

    /**
     * Display a popup menu that shows all relevant actions.
     * The actions displayed depend on what is selected.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param version
     *            A <code>ContainerVersion</code>, if a version is selected.
     * @param documentViews
     *            A list of <code>DocumentView</code>, if a version is selected.
     * @param latestVersion
     *            A <code>Boolean</code>, true for the latest version.
     * @param document
     *            A <code>Document</code>, if a draft document is selected.
     * @param documentVersion
     *            A <code>DocumentVersion</code>, if a version document is selected.
     * @param user
     *            A <code>User</code>, if a user is selected.
     */
    private void showAll(final Container container, final ContainerDraft draft,
            final ContainerVersion version,
            final List<DocumentView> documentViews,
            final Boolean latestVersion, final Document document,
            final DocumentVersion documentVersion, final User user) {
        final boolean draftSelected = null == version && isLocalDraft(draft);
        final boolean versionSelected = null != version;
        final boolean draftDocumentSelected = null != document;
        final boolean versionDocumentSelected = null != documentVersion;
        final boolean versionUserSelected = null != user;
        final boolean online = isOnline();
        boolean needSeparator = false;

        // show menus for draft document, version document or version user (if selected)
        if (draftDocumentSelected) {
            addForDocument(draft, document);
            needSeparator = true;
        } else if (versionDocumentSelected) {
            addForDocument(documentVersion);
            needSeparator = true;
        } else if (versionUserSelected) {
            addForUser(user);
            needSeparator = true;
        }

        // separator
        if (needSeparator) {
            addSeparator();
            needSeparator = false;
        }

        // show draft actions.
        // publish is not required because it is available on the 'common actions' button.
        if (draftSelected) {
            addForDraft(container, draft);
            needSeparator = true;
        }

        // show version actions.
        // create draft is not required because it is available on the 'common actions' button.
        // If nobody has the draft and the user selects the latest version,
        // the 'common actions' button will have Create Draft, not Forward. This is the one
        // case where we need to show the Forward command in this list.
        if (versionSelected && online && null == draft && latestVersion && container.isLatest()) {
            final Data publishData = new Data(3);
            publishData.set(PublishVersion.DataKey.CONTAINER_ID, version.getArtifactId());
            publishData.set(PublishVersion.DataKey.VERSION_ID, version.getVersionId());
            publishData.set(PublishVersion.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
            add(ActionId.CONTAINER_PUBLISH_VERSION, publishData);
            needSeparator = true;
        }
        if (versionSelected) {
            addForVersion(container, draft, version, documentViews, latestVersion);
            needSeparator = true;
        }

        // separator
        if (needSeparator) {
            addSeparator();
            needSeparator = false;
        }

        // add container menus
        addForContainer(container);

        show();
    }
}
