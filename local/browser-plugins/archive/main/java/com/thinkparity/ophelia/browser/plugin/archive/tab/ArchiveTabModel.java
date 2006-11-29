/*
 * Created On: Sep 1, 2006 8:13:28 AM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.DefaultListModel;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabPanelExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabPanelExtensionModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ArchiveTabModel extends TabPanelExtensionModel<ArchiveTabProvider> {

    /** A list of the archived container panels. */
    private final List<TabPanel> containerPanels;

    /** A list of the archived container version panels. */
    private final Map<TabPanel, TabPanel> containerVersionPanels;

    private final List<TabPanel> expandedPanels;

    /** The list model. */
    private final DefaultListModel listModel;

    /** A list of all visible panels. */
    private final List<TabPanel> visiblePanels;

    /** Create ArchiveTabModel. */
    ArchiveTabModel(final TabPanelExtension extension) {
        super(extension);
        this.containerPanels = new ArrayList<TabPanel>();
        this.containerVersionPanels = new HashMap<TabPanel, TabPanel>();
        this.expandedPanels = new ArrayList<TabPanel>();
        this.listModel = new DefaultListModel();
        this.visiblePanels = new ArrayList<TabPanel>();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#debug()
     */
    @Override
    protected void debug() {
        logger.logDebug("{0} container panels.", containerPanels.size());
        logger.logDebug("{0} container version panels.", containerVersionPanels.size());
        logger.logDebug("{0} visible panels.", visiblePanels.size());
        logger.logDebug("{0} model elements.", listModel.size());
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#getListModel()
     */
    @Override
    protected DefaultListModel getListModel() {
        return listModel;
    }

    /**
     * Initialize the model. This reads from the provider the initial archive
     * list.
     * 
     */
    protected void initialize() {
        debug();
        clearPanels();
        final List<Container> containers = readContainers();
        for (final Container container : containers) {
            addPanel(container);
        }
        debug();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#synchronize()
     *
     */
    @Override
    protected void synchronize() {
        debug();
        // add container panels and container version panels to the visiblity
        // list
        visiblePanels.clear();
        for (final TabPanel containerPanel : containerPanels) {
            visiblePanels.add(containerPanel);
            if (isExpanded(containerPanel)) {
                visiblePanels.add(containerVersionPanels.get(containerPanel));
            }
        }
        // add newly visible panels to the model; and set updated panels
        for (int i = 0; i < visiblePanels.size(); i++) {
            if (listModel.contains(visiblePanels.get(i))) {
                listModel.set(i, visiblePanels.get(i));
            } else {
                listModel.add(i, visiblePanels.get(i));
            }
        }
        // prune newly invisible panels from the model
        final TabPanel[] invisiblePanels = new TabPanel[listModel.size()];
        listModel.copyInto(invisiblePanels);
        for (int i = 0; i < invisiblePanels.length; i++) {
            if (!visiblePanels.contains(invisiblePanels[i])) {
                listModel.removeElement(invisiblePanels[i]);
            }
        }
        debug();
    }

    /**
     * Synchronize a container.
     * 
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
    void synchronizeContainer(final UUID uniqueId) {
    }

    /**
     * Add a container panel.
     * 
     * @param container
     *            A <code>Container</code>.
     */
    private void addPanel(final Container container) {
        addPanel(containerPanels.isEmpty() ? 0 : containerPanels.size() - 1,
                container);
    }

    /**
     * Add a container panel.
     * 
     * @param index
     *            The index in the container list within which to add the
     *            container.
     * @param container
     *            A <code>Container</code>.
     */
    private void addPanel(final int index, final Container container) {
        final TabPanel containerPanel = toDisplay(container);
        containerPanels.add(index, containerPanel);
        final List<ContainerVersion> versions = Collections.emptyList();
        final Map<ContainerVersion, Map<DocumentVersion, Delta>> documentVersions =
            Collections.emptyMap();
        final Map<ContainerVersion, Map<User, ArtifactReceipt>> publishedTo =
            Collections.emptyMap();
        final Map<ContainerVersion, User> publishedBy = Collections.emptyMap();
        containerVersionPanels.put(containerPanel,
                toDisplay(container, versions, documentVersions, publishedTo,
                        publishedBy));
    }

    /**
     * Clear all container and container version panels.
     *
     */
    private void clearPanels() {
        containerPanels.clear();
        containerVersionPanels.clear();
    }

    private boolean isExpanded(final TabPanel tabPanel) {
        return expandedPanels.contains(tabPanel);
    }

    /**
     * Read the archived containers.
     * 
     * @return A <code>List</code> of <code>Container</code>s.
     */
    private List<Container> readContainers() {
        return ((ArchiveTabProvider) contentProvider).readContainers();
    }

    /**
     * Create a tab panel for a container.
     * 
     * @param container
     *            A <code>Container</code>.
     * @return A <code>TabPanel</code>.
     */
    private TabPanel toDisplay(final Container container) {
        final ArchiveTabPanel panel = new ArchiveTabPanel();
        panel.setExpanded(Boolean.FALSE);
        panel.setPanelData(container, null, null);
        return panel;
    }

    /**
     * Create a tab panel for a container and its versions; the versions'
     * documents, the versions' published to list; the versions' publisher.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param versions
     *            A <code>ContainerVersion</code>.
     * @param documentVersions
     *            A list of <code>DocumentVersion</code>s and their
     *            <code>Delta</code>s.
     * @param publishedTo
     *            A list of <code>User</code>s and their
     *            <code>ArtifactReceipt</code>s.
     * @param publishedBy
     *            The published by <code>User</code>.
     * @return A <code>TabPanel</code>.
     */
    private TabPanel toDisplay(
            final Container container,
            final List<ContainerVersion> versions,
            final Map<ContainerVersion, Map<DocumentVersion, Delta>> documentVersions,
            final Map<ContainerVersion, Map<User, ArtifactReceipt>> publishedTo,
            final Map<ContainerVersion, User> publishedBy) {
        return null;
    }
}
