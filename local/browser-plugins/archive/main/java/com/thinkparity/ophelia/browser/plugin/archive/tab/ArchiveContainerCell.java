/*
 * Created On: Sep 1, 2006 10:59:14 AM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.plugin.Plugin;
import com.thinkparity.ophelia.browser.platform.plugin.PluginId;
import com.thinkparity.ophelia.browser.platform.plugin.PluginRegistry;
import com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtension;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ArchiveContainerCell extends ArchiveArtifactCell {

    /**
     * Create ArchiveContainerCell.
     * 
     */
    ArchiveContainerCell() {
        super();
    }

    /**
     * Obtain the artifact
     *
     * @return The Artifact.
     */
    Container getContainer() {
        return (Container) getArtifact();
    }

    /**
     * Set artifact.
     *
     * @param artifact The Artifact.
     */
    void setContainer(final Container container) {
        setArtifact(container);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent)
     */
    @Override
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        switch (connection) {
        case ONLINE:
            final JPopupMenu jPopupMenu = MenuFactory.createPopup();
            final PluginRegistry pluginRegistry = new PluginRegistry();
            final Plugin archivePlugin = pluginRegistry.getPlugin(PluginId.ARCHIVE);
            if (null != archivePlugin) {
                final ActionExtension archiveExtension =
                    pluginRegistry.getActionExtension(PluginId.ARCHIVE, "RestoreAction");
                jPopupMenu.add(popupItemFactory.createPopupItem(
                        archiveExtension, (Container) getArtifact()));
            }

            jPopupMenu.show(invoker, e.getX(), e.getY());
            break;
        case OFFLINE:
            break;
        default:
            Assert.assertUnreachable("UNKNOWN CONNECTION");
        }
    }
}
