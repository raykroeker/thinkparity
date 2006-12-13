/*
 * Created On:  3-Dec-06 4:36:48 PM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import java.util.Map;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showForContainer(com.thinkparity.codebase.model.container.Container)
     *
     */
    public void showForContainer(final Container container) {
        if (isOnline()) {
            add(PluginId.ARCHIVE, "RestoreAction", container);
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
        showForContainer(((ArchiveTabContainerPanel) tabPanel).getContainer());
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.PopupDelegate#showForVersion(com.thinkparity.codebase.model.container.ContainerVersion,
     *      java.util.Map, java.util.Map,
     *      com.thinkparity.codebase.model.user.User)
     * 
     */
    public void showForVersion(final ContainerVersion version,
            final Map<DocumentVersion, Delta> documentVersions,
            final Map<User, ArtifactReceipt> publishedTo, final User publishedBy) {
    }

    /**
     * Determine if the model is online.
     * 
     * @return True if the model is online.
     */
    private boolean isOnline() {
        return model.isOnline().booleanValue();
    }
}
