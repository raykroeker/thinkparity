/*
 * Created On:  4-Dec-06 11:05:43 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;

import com.thinkparity.ophelia.browser.application.browser.DefaultBrowserActionDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate;
import com.thinkparity.ophelia.browser.platform.action.ActionFactory;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.ActionInvocation;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.Read;
import com.thinkparity.ophelia.browser.platform.action.container.AddBookmark;
import com.thinkparity.ophelia.browser.platform.action.container.ReadVersion;
import com.thinkparity.ophelia.browser.platform.action.container.RemoveBookmark;
import com.thinkparity.ophelia.browser.platform.action.document.Open;
import com.thinkparity.ophelia.browser.platform.action.document.OpenVersion;
import com.thinkparity.ophelia.browser.platform.action.profile.Update;

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

    /** The create container <code>AbstractAction</code>. */
    private final ActionInvocation containerCreate;

    /** The container's add bookmark <code>AbstractAction</code>. */
    private final ActionInvocation containerRemoveBookmark;

    /** The draft document <code>AbstractAction</code>. */
    private final ActionInvocation documentOpenDraft;

    /** The document version <code>AbstractAction</code>. */
    private final ActionInvocation documentOpenVersion;

    /** A <code>ContainerModel</code>. */
    private final ContainerTabModel model;

    /** The profile update <code>AbstractAction</code>. */
    private final ActionInvocation profileUpdate;

    /** The user <code>AbstractAction</code>. */
    private final ActionInvocation userRead;

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
        this.containerCreate = ActionFactory.create(ActionId.CONTAINER_CREATE);
        this.containerRemoveBookmark = ActionFactory.create(ActionId.CONTAINER_REMOVE_BOOKMARK);
        this.documentOpenDraft = getInstance(ActionId.DOCUMENT_OPEN);
        this.documentOpenVersion = getInstance(ActionId.DOCUMENT_OPEN_VERSION);
        this.profileUpdate = getInstance(ActionId.PROFILE_UPDATE);
        this.userRead = getInstance(ActionId.CONTACT_READ);
        this.versionRead = getInstance(ActionId.CONTAINER_READ_VERSION);
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
            final Data data = new Data(1);
            data.set(Update.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
            invoke(profileUpdate, getApplication(), data);
        } else {
            final Data data = new Data(1);
            data.set(Read.DataKey.CONTACT_ID, user.getId());
            invoke(userRead, getApplication(), data);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabActionDelegate#invokeForVersion(com.thinkparity.codebase.model.container.ContainerVersion)
     * 
     */
    public void invokeForVersion(final ContainerVersion version) {
        if (version.isSetComment()) {
            final Data data = new Data(2);
            data.set(ReadVersion.DataKey.CONTAINER_ID, version.getArtifactId());
            data.set(ReadVersion.DataKey.VERSION_ID, version.getVersionId());
            invoke(versionRead, getApplication(), data);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate#isTabButtonActionAvailable()
     */
    public Boolean isTabButtonActionAvailable() {
        return Boolean.TRUE;
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
