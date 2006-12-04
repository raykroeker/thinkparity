/*
 * Created On:  3-Dec-06 4:36:48 PM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate;
import com.thinkparity.ophelia.browser.platform.action.DefaultPopupDelegate;
import com.thinkparity.ophelia.browser.platform.plugin.PluginId;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ArchiveTabPopupDelegate extends DefaultPopupDelegate implements
        TabPanelPopupDelegate, PopupDelegate {

    /** An <code>ArchiveTabModel</code>. */
    private ArchiveTabModel model;

    /**
     * Create ArchiveTabPopupFactory.
     * 
     * @param model
     *            An <code>ArchiveTabModel</code>.
     */
    ArchiveTabPopupDelegate(final ArchiveTabModel model) {
        super();
        this.model = model;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showForDocument(com.thinkparity.ophelia.model.container.ContainerDraft, com.thinkparity.codebase.model.document.Document)
     *
     */
    public void showForDocument(final ContainerDraft draft,
            final Document document) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showForDocument(com.thinkparity.codebase.model.document.DocumentVersion, com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta)
     *
     */
    public void showForDocument(final DocumentVersion version, final Delta delta) {
        if (isOnline()) {
            add(PluginId.ARCHIVE, "OpenDocumentVersionAction", version);
            show();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showForDraft(com.thinkparity.codebase.model.container.Container, com.thinkparity.ophelia.model.container.ContainerDraft)
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
        if (isContainerPanel(tabPanel)) {
            showForContainerPanel((ArchiveTabContainerPanel) tabPanel);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showForUser(com.thinkparity.codebase.model.user.User)
     *
     */
    public void showForUser(final User user) {
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showForVersion(com.thinkparity.codebase.model.container.ContainerVersion)
     *
     */
    public void showForVersion(final ContainerVersion version) {
    }

    /**
     * Determine if the panel is an archive tab container panel.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @return True if it is an archive tab container panel.
     */
    private boolean isContainerPanel(final TabPanel tabPanel) {
        return model.isContainerPanel(tabPanel).booleanValue();
    }

    /**
     * Determine if the model is online.
     * 
     * @return True if the model is online.
     */
    private boolean isOnline() {
        return model.isOnline().booleanValue();
    }

    /**
     * Show the archive tab container panel popup menu.
     * 
     * @param containerPanel
     *            A <code>ArchiveTabContainerPanel</code>.
     */
    private void showForContainerPanel(final ArchiveTabContainerPanel containerPanel) {
        if (isOnline()) {
            add(PluginId.ARCHIVE, "RestoreAction", containerPanel
                    .getContainer());
            show();
        }
    }
}
