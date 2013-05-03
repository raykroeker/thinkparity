/*
 * Created On:  4-Dec-06 11:46:36 AM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ActionDelegate;
import com.thinkparity.ophelia.browser.platform.action.DefaultActionDelegate;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ArchiveTabActionDelegate extends DefaultActionDelegate implements
        ActionDelegate {

    /**
     * Create ArchiveTabActionDelegate.
     *
     */
    ArchiveTabActionDelegate(final ArchiveTabModel model) {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabActionDelegate#invokeForContainer(com.thinkparity.codebase.model.container.Container)
     *
     */
    public void invokeForContainer(final Container container) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabActionDelegate#invokeForDocument(com.thinkparity.ophelia.model.container.ContainerDraft, com.thinkparity.codebase.model.document.Document)
     *
     */
    public void invokeForDocument(final ContainerDraft draft,
            final Document document) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabActionDelegate#invokeForDocument(com.thinkparity.codebase.model.document.DocumentVersion, com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta)
     *
     */
    public void invokeForDocument(final DocumentVersion version,
            final Delta delta) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabActionDelegate#invokeForUser(com.thinkparity.codebase.model.user.User)
     *
     */
    public void invokeForUser(final User user) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerTabActionDelegate#invokeForVersion(com.thinkparity.codebase.model.container.ContainerVersion)
     *
     */
    public void invokeForVersion(final ContainerVersion version) {
    }
}
