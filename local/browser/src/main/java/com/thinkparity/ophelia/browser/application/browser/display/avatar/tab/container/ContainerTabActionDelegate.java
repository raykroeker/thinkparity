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

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.DefaultActionDelegate;
import com.thinkparity.ophelia.browser.platform.action.ActionFactory;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.Read;
import com.thinkparity.ophelia.browser.platform.action.container.AddBookmark;
import com.thinkparity.ophelia.browser.platform.action.container.ReadVersion;
import com.thinkparity.ophelia.browser.platform.action.container.RemoveBookmark;
import com.thinkparity.ophelia.browser.platform.action.document.Open;
import com.thinkparity.ophelia.browser.platform.action.document.OpenVersion;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ContainerTabActionDelegate extends DefaultActionDelegate implements
        ActionDelegate {

    /** The container's add bookmark <code>AbstractAction</code>. */
    private final AbstractAction containerAddBookmark;

    /** The container's add bookmark <code>AbstractAction</code>. */
    private final AbstractAction containerRemoveBookmark;

    /** The draft document <code>AbstractAction</code>. */
    private final AbstractAction documentOpenDraft;

    /** The document version <code>AbstractAction</code>. */
    private final AbstractAction documentOpenVersion;

    /** The user <code>AbstractAction</code>. */
    private final AbstractAction userRead;

    /** A <code>ContainerVersion</code> <code>AbstractAction</code>. */
    private final AbstractAction versionRead;

    /**
     * Create ContainerTabActionDelegate.
     *
     */
    ContainerTabActionDelegate() {
        super();
        this.containerAddBookmark = ActionFactory.create(ActionId.CONTAINER_ADD_BOOKMARK);
        this.containerRemoveBookmark = ActionFactory.create(ActionId.CONTAINER_REMOVE_BOOKMARK);
        this.documentOpenDraft = getInstance(ActionId.DOCUMENT_OPEN);
        this.documentOpenVersion = getInstance(ActionId.DOCUMENT_OPEN_VERSION);
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
            containerRemoveBookmark.invoke(data);
        } else {
            data.set(AddBookmark.DataKey.CONTAINER_ID, container.getId());
            containerAddBookmark.invoke(data);
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
        invoke(documentOpenDraft, data);
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
        invoke(documentOpenVersion, data);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabActionDelegate#invokeForUser(com.thinkparity.codebase.model.user.User)
     * 
     */
    public void invokeForUser(final User user) {
        final Data data = new Data(1);
        data.set(Read.DataKey.CONTACT_ID, user.getId());
        invoke(userRead, data);
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
            invoke(versionRead, data);
        }
    }
}
