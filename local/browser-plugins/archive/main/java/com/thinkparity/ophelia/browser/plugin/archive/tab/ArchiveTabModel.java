/*
 * Created On: Sep 1, 2006 8:13:28 AM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.DefaultListModel;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerPanel;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabPanelExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabPanelExtensionModel;
import com.thinkparity.ophelia.browser.util.UserUtil;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.6
 */
final class ArchiveTabModel extends TabPanelExtensionModel<ArchiveTabProvider> {

    /** The <code>ArchiveTabActionDelegate</code>. */
    private final ArchiveTabActionDelegate actionDelegate;

    /** A list of the archived container panels. */
    private final List<TabPanel> containerPanels;

    /** A list of the panels' expanded state. */
    private final Map<TabPanel, Boolean> expandedState;

    /** The list model. */
    private final DefaultListModel listModel;

    /** The <code>ArchiveTabPopupDelegate</code>. */
    private final ArchiveTabPopupDelegate popupDelegate;

    /** A list of all visible panels. */
    private final List<TabPanel> visiblePanels;

    /** Create ArchiveTabModel. */
    ArchiveTabModel(final TabPanelExtension extension) {
        super(extension);
        this.actionDelegate = new ArchiveTabActionDelegate(this);
        this.containerPanels = new ArrayList<TabPanel>();
        this.expandedState = new HashMap<TabPanel, Boolean>();
        this.listModel = new DefaultListModel();
        this.popupDelegate = new ArchiveTabPopupDelegate(this);
        this.visiblePanels = new ArrayList<TabPanel>();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#debug()
     * 
     */
    @Override
    protected void debug() {
        logger.logDebug("{0} container panels.", containerPanels.size());
        int expandedCount = 0;
        for (final Boolean state : expandedState.values()) {
            if (Boolean.TRUE == state)
                expandedCount++;
        }
        logger.logDebug("{0} expanded panels.", expandedCount);
        logger.logDebug("{0} visible panels.", visiblePanels.size());
        logger.logDebug("{0} model elements.", listModel.size());
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#getListModel()
     */
    @Override
    protected DefaultListModel getListModel() {
        debug();
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
            addContainerPanel(container);
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
        /* add container panels and container version panels to the visiblity
           list */
        visiblePanels.clear();
        for (final TabPanel containerPanel : containerPanels) {
            visiblePanels.add(containerPanel);
        }
        // add newly visible panels to the model; and set other panels
        int listModelIndex;
        for (int i = 0; i < visiblePanels.size(); i++) {
            if (listModel.contains(visiblePanels.get(i))) {
                listModelIndex = listModel.indexOf(visiblePanels.get(i));
                /* the position of the panel in the model is identical to that
                 * of the panel the list */
                if (i == listModelIndex) {
                    listModel.set(i, visiblePanels.get(i));
                } else {
                    listModel.remove(listModelIndex);
                    listModel.add(i, visiblePanels.get(i));
                }
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#toggleSelection(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel, java.awt.event.MouseEvent)
     */
    @Override
    protected void toggleSelection(final TabPanel tabPanel) {
        toggleExpand(tabPanel);
        synchronize();
    }

    /**
     * Determine if the tab panel is a container tab panel.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @return True if it is a container tab panel.
     */
    Boolean isContainerPanel(final TabPanel tabPanel) {
        return ArchiveTabContainerPanel.class.isAssignableFrom(tabPanel.getClass());
    }

    /**
     * Determine if the model is online.
     * 
     * @return True if the model is online.
     */
    Boolean isOnline() {
        return Connection.ONLINE == super.getBrowser().getConnection();
    }

    /**
     * Synchronize a container.
     * 
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
    void synchronizeContainer(final UUID uniqueId) {
        debug();
        final Container container = read(uniqueId);
        if (null ==  container) {
            removeContainerPanel(uniqueId, true);
        } else {
            final int panelIndex = lookupIndex(uniqueId);
            if (-1 < panelIndex) {
                removeContainerPanel(uniqueId, false);
                addContainerPanel(panelIndex, container);
            } else {
                addContainerPanel(0, container);
            }
        }
        synchronize();
        debug();
    }

    /**
     * Add a container panel.
     * 
     * @param container
     *            A <code>Container</code>.
     */
    private void addContainerPanel(final Container container) {
        addContainerPanel(containerPanels.isEmpty() ? 0 : containerPanels.size() - 1,
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
    private void addContainerPanel(final int index, final Container container) {
        final List<TeamMember> team = readTeam(container.getUniqueId());
        final List<ContainerVersion> versions = readVersions(container.getUniqueId());

        final Map<ContainerVersion, Map<DocumentVersion, Delta>> documentVersions =
            new HashMap<ContainerVersion, Map<DocumentVersion, Delta>>(versions.size(), 1.0F);
        final Map<ContainerVersion, User> publishedBy =
            new HashMap<ContainerVersion, User>(versions.size(), 1.0F);
        final Map<ContainerVersion, Map<User, ArtifactReceipt>> publishedTo =
            new HashMap<ContainerVersion, Map<User, ArtifactReceipt>>(versions.size(), 1.0F);
        ContainerVersion version, previousVersion;
        for (int i = 0; i < versions.size(); i++) {
            version = versions.get(i);
            if (versions.size() - 1 == i) {
                /* reading the last version in the list which is the first
                 * version chronologically */
                documentVersions.put(version, readDocumentVersionDeltas(
                        container.getUniqueId(), versions.get(i).getVersionId()));
            } else {
                previousVersion = versions.get(i + 1);
                documentVersions.put(version, readDocumentVersionDeltas(
                        container.getUniqueId(), version.getVersionId(),
                        previousVersion.getVersionId()));
            }
            publishedBy.put(version, find(team, version.getUpdatedBy()));
            publishedTo.put(version, readPublishedTo(container.getUniqueId(), version.getVersionId()));
        }
        containerPanels.add(index, toDisplay(container, null, null, versions,
                documentVersions, publishedTo, publishedBy));
    }

    /**
     * Clear all container and container version panels.
     * 
     */
    private void clearPanels() {
        containerPanels.clear();
    }

    private <T extends User> T find(final List<T> users,
            final JabberId userId) {
        return users.get(UserUtil.indexOf(users, userId));
    }

    /**
     * Determine if a panel is expanded.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @return True if the panel is expanded; false otherwise.
     */
    private boolean isExpanded(final TabPanel tabPanel) {
        if (expandedState.containsKey(tabPanel)) {
            return expandedState.get(tabPanel).booleanValue();
        } else {
            // NOTE the default panel expanded state can be changed here
            expandedState.put(tabPanel, Boolean.FALSE);
            return isExpanded(tabPanel);
        }
    }

    /**
     * Lookup the panel index for the container unique id.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return The panel index; or -1 if the container does not exist in the
     *         list.
     */
    private int lookupIndex(final UUID uniqueId) {
        for (int i = 0; i < containerPanels.size(); i++)
            if (((ContainerPanel) containerPanels.get(i)).getContainer()
                    .getUniqueId().equals(uniqueId))
                return i;
        return -1;    }

    /**
     * Read a container from the archive.
     * 
     * @param uniqueId
     *            A contianer unique id <code>UUID</code>.
     * @return A <code>Container</code>.
     */
    private Container read(final UUID uniqueId) {
        return ((ArchiveTabProvider) contentProvider).readContainer(uniqueId);
    }

    /**
     * Read the archived containers.
     * 
     * @return A <code>List</code> of <code>Container</code>s.
     */
    private List<Container> readContainers() {
        return ((ArchiveTabProvider) contentProvider).readContainers();
    }

    private Map<DocumentVersion, Delta> readDocumentVersionDeltas(
            final UUID uniqueId, final Long compareVersionId) {
        return ((ArchiveTabProvider) contentProvider).readDocumentVersionDeltas(
                uniqueId, compareVersionId);
    }

    private Map<DocumentVersion, Delta> readDocumentVersionDeltas(
            final UUID uniqueId, final Long compareVersionId,
            final Long compareToVersionId) {
        return ((ArchiveTabProvider) contentProvider).readDocumentVersionDeltas(
                uniqueId, compareVersionId, compareToVersionId);
    }

    private Map<User, ArtifactReceipt> readPublishedTo(final UUID uniqueId,
            final Long versionId) {
        return ((ArchiveTabProvider) contentProvider).readPublishedTo(uniqueId,
                versionId);
    }

    private List<TeamMember> readTeam(final UUID uniqueId) {
        return ((ArchiveTabProvider) contentProvider).readTeam(uniqueId);
    }

    /**
     * Read a list of container versions for a container.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * 
     * @return A <code>List</code> of <code>ContainerVersion</code>s.
     */
    private List<ContainerVersion> readVersions(final UUID uniqueId) {
        return ((ArchiveTabProvider) contentProvider).readVersions(uniqueId);
    }

    /**
     * Remove a container panel.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     */
    private void removeContainerPanel(final UUID uniqueId,
            final boolean removeExpandedState) {
        final int panelIndex = lookupIndex(uniqueId);
        if (removeExpandedState) {
            final TabPanel containerPanel = containerPanels.remove(panelIndex);
            expandedState.remove(containerPanel);
        } else {
            containerPanels.remove(panelIndex);
        }
    }

    /**
     * Create a tab panel for a container.
     * 
     * @param container
     *            A <code>Container</code>.
     * @return A <code>TabPanel</code>.
     */
    private TabPanel toDisplay(
            final Container container,
            final ContainerDraft draft,
            final ContainerVersion latestVersion,
            final List<ContainerVersion> versions,
            final Map<ContainerVersion, Map<DocumentVersion, Delta>> documentVersions,
            final Map<ContainerVersion, Map<User, ArtifactReceipt>> publishedTo,
            final Map<ContainerVersion, User> publishedBy) {
        final ContainerPanel panel = new ArchiveTabContainerPanel();
        panel.setActionDelegate(actionDelegate);
        panel.setPanelData(container, draft, latestVersion, versions,
                documentVersions, publishedTo, publishedBy);
        panel.setExpanded(isExpanded(panel));
        panel.setPopupDelegate(popupDelegate);
        return panel;
    }

    /**
     * Toggle the expansion of a panel on and off. At the moment only
     * single-panel expansion is enabled as well as containers only.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     */
    private void toggleExpand(final TabPanel tabPanel) {
        final Boolean expanded;
        if (isContainerPanel(tabPanel).booleanValue()) {
            final ContainerPanel containerPanel = (ContainerPanel) tabPanel;
            if (isExpanded(containerPanel)) {
                expanded = Boolean.FALSE;
            } else {
                // NOTE-BEGIN:multi-expand to allow multiple selection in the list; remove here
                for (final TabPanel visiblePanel : visiblePanels) {
                    if (isExpanded(visiblePanel)) {
                        toggleExpand(visiblePanel);
                    }
                }
                // NOTE-END:multi-expand
                expanded = Boolean.TRUE;
            }
            containerPanel.setExpanded(expanded);
        } else {
            expanded = Boolean.FALSE;
        }
        expandedState.put(tabPanel, expanded);
    }
}
