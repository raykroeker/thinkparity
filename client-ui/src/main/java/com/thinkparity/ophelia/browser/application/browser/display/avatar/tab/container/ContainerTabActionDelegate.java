/*
 * Created On:  4-Dec-06 11:05:43 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerDraft.ArtifactState;

import com.thinkparity.ophelia.browser.application.browser.DefaultBrowserActionDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate;
import com.thinkparity.ophelia.browser.platform.action.ActionFactory;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.ActionInvocation;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.container.*;
import com.thinkparity.ophelia.browser.platform.action.document.Open;
import com.thinkparity.ophelia.browser.platform.action.document.OpenVersion;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
final class ContainerTabActionDelegate extends DefaultBrowserActionDelegate implements
        ActionDelegate, TabButtonActionDelegate {

    /** The container's add bookmark <code>AbstractAction</code>. */
    private final ActionInvocation containerAddBookmark;

    /** The container's apply flag seen <code>AbstractAction</code>. */
    private final ActionInvocation containerApplyFlagSeen;

    /** The create container <code>AbstractAction</code>. */
    private final ActionInvocation containerCreate;

    /** The create draft <code>AbstractAction</code>. */
    private final ActionInvocation containerCreateDraft;

    /** The delete container <code>AbstractAction</code>. */
    private final ActionInvocation containerDelete;

    /** The delete draft <code>AbstractAction</code>. */
    private final ActionInvocation containerDeleteDraft;

    /** The container publish <code>AbstractAction</code>. */
    private final ActionInvocation containerPublish;

    /** The container publish version <code>AbstractAction</code>. */
    private final ActionInvocation containerPublishVersion;

    /** The container's remove bookmark <code>AbstractAction</code>. */
    private final ActionInvocation containerRemoveBookmark;

    /** The container's remove document <code>AbstractAction</code>. */
    private final ActionInvocation containerRemoveDocument;

    /** The container update draft comment <code>AbstractAction</code>. */
    private final ActionInvocation containerUpdateDraftComment;

    /** The draft document <code>AbstractAction</code>. */
    private final ActionInvocation documentOpenDraft;

    /** The document version <code>AbstractAction</code>. */
    private final ActionInvocation documentOpenVersion;

    /** A <code>ContainerModel</code>. */
    private final ContainerTabModel model;

    /** The profile update <code>AbstractAction</code>. */
    private final ActionInvocation profileUpdate;

    /** The read team member action. */
    private final ActionInvocation teamMemberRead;

    /** A <code>ContainerVersion</code> <code>AbstractAction</code>. */
    private final ActionInvocation versionRead;

    /**
     * Create ContainerTabActionDelegate.
     *
     */
    ContainerTabActionDelegate(final ContainerTabModel model) {
        super();
        this.model = model;
        this.containerAddBookmark = ActionFactory.create(ActionId.CONTAINER_ADD_BOOKMARK);
        this.containerApplyFlagSeen = ActionFactory.create(ActionId.CONTAINER_APPLY_FLAG_SEEN);
        this.containerCreate = ActionFactory.create(ActionId.CONTAINER_CREATE);
        this.containerCreateDraft = ActionFactory.create(ActionId.CONTAINER_CREATE_DRAFT);
        this.containerDelete = ActionFactory.create(ActionId.CONTAINER_DELETE);
        this.containerDeleteDraft = ActionFactory.create(ActionId.CONTAINER_DELETE_DRAFT);
        this.containerPublish = ActionFactory.create(ActionId.CONTAINER_PUBLISH);
        this.containerPublishVersion = ActionFactory.create(ActionId.CONTAINER_PUBLISH_VERSION);
        this.containerRemoveBookmark = ActionFactory.create(ActionId.CONTAINER_REMOVE_BOOKMARK);
        this.containerRemoveDocument = ActionFactory.create(ActionId.CONTAINER_REMOVE_DOCUMENT);
        this.containerUpdateDraftComment = getInstance(ActionId.CONTAINER_UPDATE_DRAFT_COMMENT);
        this.documentOpenDraft = getInstance(ActionId.DOCUMENT_OPEN);
        this.documentOpenVersion = getInstance(ActionId.DOCUMENT_OPEN_VERSION);
        this.profileUpdate = getInstance(ActionId.PROFILE_UPDATE);
        this.teamMemberRead = getInstance(ActionId.CONTAINER_READ_TEAM_MEMBER);
        this.versionRead = getInstance(ActionId.CONTAINER_READ_VERSION);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate#getCommonActionForContainerDisplayNameKey(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft)
     */
    public String getCommonActionForContainerDisplayNameKey(
            final Container container, final ContainerDraft draft) {
        return "CreateDraft";
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate#getCommonActionForDraftDisplayNameKey(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft)
     */
    public String getCommonActionForDraftDisplayNameKey(
            final Container container, final ContainerDraft draft) {
        return "Publish";
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate#getCommonActionForVersionDisplayNameKey(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft, com.thinkparity.codebase.model.container.ContainerVersion, java.lang.Boolean)
     */
    public String getCommonActionForVersionDisplayNameKey(
            final Container container, final ContainerDraft draft,
            final ContainerVersion version, final Boolean latestVersion) {
        if (null == draft && latestVersion && container.isLatest()) {
            return "CreateDraft";
        } else {
            return "PublishVersion";
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate#invokeCommonActionForContainer(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft)
     */
    public void invokeCommonActionForContainer(final Container container, final ContainerDraft draft) {
        if (isCommonActionForContainerEnabled(container, draft)) {
            final Data createDraftData = new Data(1);
            createDraftData.set(CreateDraft.DataKey.CONTAINER_ID, container.getId());
            invoke(containerCreateDraft, getApplication(), createDraftData);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate#invokeCommonActionForDraft(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft)
     */
    public void invokeCommonActionForDraft(final Container container, final ContainerDraft draft) {
        if (isCommonActionForDraftEnabled(container, draft)) {
           final Data publishData = new Data(2);
           publishData.set(Publish.DataKey.CONTAINER_ID, container.getId());
           publishData.set(Publish.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
           invoke(containerPublish, getApplication(), publishData);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate#invokeCommonActionForVersion(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft, com.thinkparity.codebase.model.container.ContainerVersion, java.lang.Boolean)
     */
    public void invokeCommonActionForVersion(final Container container,
            final ContainerDraft draft, final ContainerVersion version,
            final Boolean latestVersion) {
        if (isCommonActionForVersionEnabled(container, draft, version, latestVersion)) {
            // if nobody has the draft and the user selects the latest version,
            // the common action is Create Draft;
            // for remaining historical versions, the common action is Forward
            if (null == draft && latestVersion && container.isLatest()) {
                final Data createDraftData = new Data(1);
                createDraftData.set(CreateDraft.DataKey.CONTAINER_ID, container.getId());
                invoke(containerCreateDraft, getApplication(), createDraftData);
            } else {
                final Data publishData = new Data(3);
                publishData.set(PublishVersion.DataKey.CONTAINER_ID, version.getArtifactId());
                publishData.set(PublishVersion.DataKey.VERSION_ID, version.getVersionId());
                publishData.set(PublishVersion.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
                invoke(containerPublishVersion, getApplication(), publishData);
            }
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate#invokeDeleteForContainer(com.thinkparity.codebase.model.container.Container)
     */
    public void invokeDeleteForContainer(final Container container) {
        if (isOnline() || !isDistributed(container.getId())) {
            final Data deleteData = new Data(1);
            deleteData.set(Delete.DataKey.CONTAINER_ID, container.getId());
            invoke(containerDelete, getApplication(), deleteData);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate#invokeDeleteForDocument(com.thinkparity.ophelia.model.container.ContainerDraft, com.thinkparity.codebase.model.document.Document)
     */
    public void invokeDeleteForDocument(final ContainerDraft draft, final Document document) {
        if (ArtifactState.REMOVED != draft.getState(document)) {
            final Data removeData = new Data(2);
            removeData.set(RemoveDocument.DataKey.CONTAINER_ID, draft.getContainerId());
            removeData.set(RemoveDocument.DataKey.DOCUMENT_ID, document.getId());
            invoke(containerRemoveDocument, getApplication(), removeData);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate#invokeDeleteForDraft(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft)
     */
    public void invokeDeleteForDraft(final Container container, final ContainerDraft draft) {
        // this command is no longer allowed if the package has not been distributed
        if (isOnline() && isDistributed(container.getId())) {
            final Data deleteData = new Data(1);
            deleteData.set(DeleteDraft.DataKey.CONTAINER_ID, draft.getContainerId());
            invoke(containerDeleteDraft, getApplication(), deleteData);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabActionDelegate#invokeForContainer(com.thinkparity.codebase.model.container.Container)
     *
     */
    public void invokeForContainer(final Container container) {
        final Data data = new Data(1);
        if (container.isBookmarked()) {
            data.set(RemoveBookmark.DataKey.CONTAINER_ID, container.getId());
            containerRemoveBookmark.invokeAction(getApplication(), data);
        } else {
            data.set(AddBookmark.DataKey.CONTAINER_ID, container.getId());
            containerAddBookmark.invokeAction(getApplication(), data);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabActionDelegate#invokeForDocument(com.thinkparity.ophelia.model.container.ContainerDraft, com.thinkparity.codebase.model.document.Document)
     *
     */
    public void invokeForDocument(final ContainerDraft draft,
            final Document document) {
        final Data data = new Data(1);
        data.set(Open.DataKey.DOCUMENT_ID, document.getId());
        invoke(documentOpenDraft, getApplication(), data);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabActionDelegate#invokeForDocument(com.thinkparity.codebase.model.document.DocumentVersion,
     *      com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta)
     * 
     */
    public void invokeForDocument(final DocumentVersion version,
            final Delta delta) {
        final Data data = new Data(2);
        data.set(OpenVersion.DataKey.DOCUMENT_ID, version.getArtifactId());
        data.set(OpenVersion.DataKey.VERSION_ID, version.getVersionId());
        invoke(documentOpenVersion, getApplication(), data);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate#invokeForDraft(com.thinkparity.ophelia.model.container.ContainerDraft)
     */
    public void invokeForDraft(final ContainerDraft draft) {
        final Data data = new Data(2);
        data.set(UpdateDraftComment.DataKey.CONTAINER_ID, draft.getContainerId());
        data.set(UpdateDraftComment.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
        invoke(containerUpdateDraftComment, getApplication(), data);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate#invokeForTabButton()
     */
    public void invokeForTabButton() {
        invoke(containerCreate, getApplication(), Data.emptyData());
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabActionDelegate#invokeForUser(com.thinkparity.codebase.model.user.User)
     * 
     */
    public void invokeForUser(final User user) {        
        if (isLocalUser(user)) {
            invoke(profileUpdate, getApplication(), Data.emptyData());
        } else {
            final Data data = new Data(1);
            data.set(ReadTeamMember.DataKey.USER_ID, user.getLocalId());
            invoke(teamMemberRead, getApplication(), data);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabActionDelegate#invokeForVersion(com.thinkparity.codebase.model.container.ContainerVersion, java.lang.Boolean)
     * 
     */
    public void invokeForVersion(final ContainerVersion version,
            final Boolean showComment) {
        if (!version.isSeen()) {
            final Data applyFlagSeenData = new Data(2);
            applyFlagSeenData.set(ApplyFlagSeen.DataKey.CONTAINER_ID, version.getArtifactId());
            applyFlagSeenData.set(ApplyFlagSeen.DataKey.VERSION_ID, version.getVersionId());
            invoke(containerApplyFlagSeen, getApplication(), applyFlagSeenData);
        }
        if (showComment && version.isSetComment()) {
            final Data data = new Data(2);
            data.set(ReadVersion.DataKey.CONTAINER_ID, version.getArtifactId());
            data.set(ReadVersion.DataKey.VERSION_ID, version.getVersionId());
            invoke(versionRead, getApplication(), data);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate#isCommonActionForContainerEnabled(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft)
     */
    public Boolean isCommonActionForContainerEnabled(final Container container, final ContainerDraft draft) {
        return (null == draft && (!isDistributed(container.getId()) || isOnline()));
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate#isCommonActionForDraftEnabled(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft)
     */
    public Boolean isCommonActionForDraftEnabled(final Container container, final ContainerDraft draft) {
        // make sure there is at least one document, excluding removed documents.
        Boolean publishable = Boolean.FALSE;
        for (final Artifact artifact : draft.getArtifacts()) {
            switch (draft.getState(artifact)) {
            case ADDED:
            case MODIFIED:
            case NONE:
                publishable = Boolean.TRUE;
                break;
            case REMOVED:
                break;
            default:
                throw Assert.createUnreachable("Unknown artifact state.");
            }
            if (publishable) {
                break;
            }
        }
        return (isOnline() && isLocalDraftModified(container.getId()) && publishable);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate#isCommonActionForVersionEnabled(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft, com.thinkparity.codebase.model.container.ContainerVersion, java.lang.Boolean)
     */
    public Boolean isCommonActionForVersionEnabled(final Container container,
            final ContainerDraft draft, final ContainerVersion version,
            final Boolean latestVersion) {
        return isOnline();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate#isTabButtonActionAvailable()
     */
    public Boolean isTabButtonActionAvailable() {
        return Boolean.TRUE;
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
    private Boolean isOnline() {
        return model.isOnlineUI().booleanValue();
    }
}
