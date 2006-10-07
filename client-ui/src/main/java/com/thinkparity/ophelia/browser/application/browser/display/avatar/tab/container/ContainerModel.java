/*
 * Created On: 13-Jul-06 1:03:06 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.container.ContainerProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionsPanel;
import com.thinkparity.ophelia.model.container.ContainerDraft;

/**
 * @author rob_masako@shaw.ca; raykroeker@gmail.com
 * @version 1.1.2.4
 */
public final class ContainerModel extends TabPanelModel {

    /** An application. */
    public final Browser browser;

    /** A list of the panel expanded flags. */
    private final Map<TabPanel, Boolean> expanded;

    /** An apache logger. */
    protected final Log4JWrapper logger;

    /** A list of all container panels. */
    private final List<TabPanel> containerPanels;

    /** A list model. */
    private final DefaultListModel listModel;

    /**
     * The user input search expression.
     * 
     * @see #applySearch(String)
     */
    private String searchExpression;

    /**
     * A list of contact ids matching the search criteria.
     * 
     * @see #applySearch(List)
     * @see #removeSearch()
     */
    private List<Long> searchResults;

    /** A map of the container panel to its versions panel. */
    private final Map<TabPanel, TabPanel> versionsPanels;

    /** A list of visible panels. */
    private final List<TabPanel> visiblePanels;

    /**
     * Create BrowserContainersModel.
     * 
     */
    ContainerModel() {
        super();
        this.browser = getBrowser();
        this.containerPanels = new ArrayList<TabPanel>();
        this.expanded = new HashMap<TabPanel, Boolean>();
        this.listModel = new DefaultListModel();
        this.logger = new Log4JWrapper();
        this.versionsPanels = new HashMap<TabPanel, TabPanel>();
        this.visiblePanels = new ArrayList<TabPanel>();
    }

    /**
     * Apply the user's search to the container list.
     * 
     * @param searchExpression
     *            A search expression <code>String</code>.
     * 
     * @see #searchExpression
     * @see #searchResults
     * @see #removeSearch()
     */
    @Override
    public void applySearch(final String searchExpression) {
        debug();
        if (searchExpression.equals(this.searchExpression)) {
            return;
        } else {
            this.searchExpression = searchExpression;
            this.searchResults = readSearchResults();
            synchronize();
        }
    }

    /**
     * Debug the container avatar.
     * 
     */
    @Override
    public void debug() {
        logger.logDebug("{0} container panels.", containerPanels.size());
        logger.logDebug("{0} version panels.", versionsPanels.size());
        logger.logDebug("{0} visible panels.", visiblePanels.size());
        for (final TabPanel visiblePanel : visiblePanels) {
            logger.logVariable("visiblePanel.getId()", visiblePanel.getId());
        }
        logger.logDebug("{0} model elements.", listModel.size());
        final TabPanel[] listModelPanels = new TabPanel[listModel.size()];
        listModel.copyInto(listModelPanels);
        for (final TabPanel listModelPanel : listModelPanels) {
            logger.logVariable("listModelPanel.getId()", listModelPanel.getId());
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#getListModel()
     */
    @Override
    public DefaultListModel getListModel() {
        debug();
        return listModel;
    }

    /**
     * Remove the search.
     * 
     * @see #searchExpression
     * @see #searchResults
     * @see #applySearch(String)
     */
    @Override
    public void removeSearch() {
        debug();
        // if the member search expression is already null; then there is no
        // search applied -> do nothing
        if (null == searchExpression) {
            return;
        } else {
            searchExpression = null;
            searchResults = null;
            synchronize();
        }
    }

    /**
     * Create the final list of container cells; container draft cells; draft
     * document cells; container version cells and container version document
     * cells. The search filter is also applied here.
     * 
     */
    @Override
    public void synchronize() {
        debug();

        visiblePanels.clear();
        for (final TabPanel containerPanel : containerPanels) {
            visiblePanels.add(containerPanel);
            if (isExpanded(containerPanel)) {
                visiblePanels.add(versionsPanels.get(containerPanel));
            }
        }
        
        // add visible cells not in the model
        for (final TabPanel visiblePanel : visiblePanels) {
            if (!listModel.contains(visiblePanel)) {
                listModel.add(visiblePanels.indexOf(visiblePanel), visiblePanel);
            }
        }

        // prune cells in the model no longer visible
        final TabPanel[] obsolutePanels = new TabPanel[listModel.size()];
        listModel.copyInto(obsolutePanels);
        for (final TabPanel obsoletePanel : obsolutePanels) {
            if (!visiblePanels.contains(obsoletePanel)) {
                listModel.removeElement(obsoletePanel);
            }
        }
        debug();
    }

    /**
     * Determine if the panel is expanded.
     * @param tabPanel
     * @return
     */
    private Boolean isExpanded(final TabPanel tabPanel) {
        return expanded.get(tabPanel);
    }

    /**
     * Trigger a click event on the panel. If the click is a double-click; we
     * will add the versions to the list model.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    public void triggerClick(final MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        if (2 == e.getClickCount()) {
            e.consume();
            toggleExpand((TabPanel) e.getSource());
        }
    }

    /**
     * Initialize the container model with containers; container versions;
     * documents and users from the provider.
     * 
     */
    @Override
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
     * Add a container panel. This will read the container's versions and add
     * the appropriate version panel as well.
     * 
     * @param container
     *            A <code>container</code>.
     */
    private void addContainerPanel(final Container container) {
        final TabPanel containerPanel = toDisplay(container);
        containerPanels.add(containerPanel);
        versionsPanels.put(containerPanel, toDisplay(container,
                readDraft(container.getId()),
                readVersions(container.getId())));
        expanded.put(containerPanel, Boolean.FALSE);
    }

    /**
     * Clear all panels.
     *
     */
    private void clearPanels() {
        containerPanels.clear();
        versionsPanels.clear();
    }

    /**
     * Read the containers from the provider.
     * 
     * @return The containers.
     */
    private List<Container> readContainers() {
        return ((ContainerProvider) contentProvider).read();
    }

    /**
     * Read the documents from the provider.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A <code>List&lt;Document&gt;</code>.
     */
    private List<Document> readDocuments(final Long containerId,
            final Long versionId) {
        return ((ContainerProvider) contentProvider).readDocuments(containerId,
                versionId);
    }

    /**
     * Read the draft for a container.
     *
     * @param containerId
     *      A container id <code>Long</code>.
     * @return A <code>ContainerDraft</code>.
     */
    private ContainerDraft readDraft(final Long containerId) {
        return ((ContainerProvider) contentProvider).readDraft(containerId);
    }

    /**
     * Search for a list of container ids through the content provider.
     * 
     * @return A list of container ids.
     */
    private List<Long> readSearchResults() {
        return ((ContainerProvider) contentProvider).search(searchExpression);
    }

    /**
     * Read the user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    private User readUser(final JabberId userId) {
        return ((ContainerProvider) contentProvider).readUser(userId);
    }

    /**
     * Read the list of users.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>Map&lt;User, ArtifactReceipt&gt;</code>.
     */
    private Map<User, ArtifactReceipt> readUsers(final Long containerId,
            final Long versionId) {
        final Map<User, ArtifactReceipt> publishedTo = ((ContainerProvider) contentProvider)
                .readPublishedTo(containerId, versionId);
        final Map<User, ArtifactReceipt> sharedWith = ((ContainerProvider) contentProvider)
                .readSharedWith(containerId, versionId);
        final Map<User, ArtifactReceipt> users = new HashMap<User, ArtifactReceipt>(
                publishedTo.size() + sharedWith.size(), 1.0F);
        users.putAll(publishedTo);
        for (final Entry<User, ArtifactReceipt> entry : sharedWith.entrySet()) {
            if (!users.containsKey(entry.getKey())) {
                users.put(entry.getKey(), entry.getValue());
            }
        }
        return users;
    }

    /**
     * Read the container versions from the provider.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    private List<ContainerVersion> readVersions(final Long containerId) {
        return ((ContainerProvider) contentProvider).readVersions(containerId);
    }

    /**
     * Create a tab panel for a container.
     * 
     * @param container
     *            A <code>Container</code>.
     * @return A <code>TabPanel</code>.
     */
    private TabPanel toDisplay(final Container container) {
        final ContainerPanel panel = new ContainerPanel(this);
        panel.setContainer(container, readDraft(container.getId()));
        return panel;
    }

    /**
     * Create a tab panel for a container; its draft and its versions..
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param containers
     *            A <code>List&lt;ContainerVersion&gt;</code>.
     * @return A <code>List&lt;TabPanel&gt;</code>.
     */
    private TabPanel toDisplay(final Container container,
            final ContainerDraft draft, final List<ContainerVersion> versions) {
        final ContainerVersionsPanel panel = new ContainerVersionsPanel(this);
        panel.setDraft(container, draft);
        for (final ContainerVersion version : versions) {
            panel.add(version, readDocuments(version.getArtifactId(), version.getVersionId()), readUsers(version.getArtifactId(), version.getVersionId()), readUser(version.getUpdatedBy()));
                    
        }
        return panel;
    }

    /**
     * Toggle the expansion of a panel on and off.
     *
     * @param panel
     *      The container <code>TabPanel</code>.
     */
    private void toggleExpand(final TabPanel panel) {
        final TabPanel versionsPanel = versionsPanels.get(panel);
        if (visiblePanels.contains(versionsPanel)) {
            expanded.put(panel, Boolean.FALSE);
        }
        else {
            expanded.put(panel, Boolean.TRUE);
        }
        synchronize();
    }
}
