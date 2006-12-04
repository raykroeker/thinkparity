/*
 * Created On:  3-Dec-06 4:36:48 PM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import javax.swing.JPopupMenu;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.ophelia.browser.platform.plugin.PluginId;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ArchiveTabPopupFactory {

    /** An <code>ArchiveTabModel</code>. */
    private ArchiveTabModel model;

    private final PopupItemFactory popupItemFactory;

    /**
     * Create ArchiveTabPopupFactory.
     * 
     * @param model
     *            An <code>ArchiveTabModel</code>.
     */
    ArchiveTabPopupFactory(final ArchiveTabModel model) {
        super();
        this.model = model;
        this.popupItemFactory = PopupItemFactory.getInstance();
    }

    /**
     * Create a popup menu for a container panel on the archive tab.
     * 
     * @param container
     *            A <code>Container</code>.
     * @return A <Code>JPopupMenu</code>.
     */
    JPopupMenu createContainerPopup(final Container container) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();

        if (isOnline()) {
            popupItemFactory.addPopupItem(jPopupMenu, PluginId.ARCHIVE, "RestoreAction", container);
        }

        return jPopupMenu;
    }

    JPopupMenu createVersionDocumentPopup(final DocumentVersion version,
            final Delta delta) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        popupItemFactory.addPopupItem(jPopupMenu, PluginId.ARCHIVE, "OpenDocumentVersionAction", version);
        return jPopupMenu;
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
