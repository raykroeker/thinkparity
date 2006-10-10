/*
 * Created On: 13-Jul-06 1:03:06 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.model.container.ContainerDraft;

/**
 * @author rob_masako@shaw.ca; raykroeker@gmail.com
 * @version 1.1.2.4
 */
public final class ContainerModel extends TabPanelModel {

    /** An application. */
    public final Browser browser;

    /** An apache logger. */
    protected final Log4JWrapper logger;

    /** A container id lookup. */
    private final Map<Long, Long> containerIdLookup;

    /** A list of all container panels. */
    private final List<TabPanel> containerPanels;

    /** A list of the panel expanded flags. */
    private final Map<TabPanel, Boolean> expanded;

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
        this.containerIdLookup = new HashMap<Long, Long>();
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
     * Determine whether or not the user is online.
     * 
     * @return True if the user is online.
     */
    public Boolean isOnline() {
        return browser.getConnection() == Connection.ONLINE;
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
        for (int i = 0; i < visiblePanels.size(); i++) {
            if (listModel.contains(visiblePanels.get(i))) {
                listModel.set(listModel.indexOf(visiblePanels.get(i)), visiblePanels.get(i));
            } else {
                listModel.add(i, visiblePanels.get(i));
            }
        }

        // prune cells in the model no longer visible
        final TabPanel[] obsoletePanels = new TabPanel[listModel.size()];
        listModel.copyInto(obsoletePanels);
        for (int i = 0; i < obsoletePanels.length; i++) {
            if (!visiblePanels.contains(obsoletePanels[i])) {
                listModel.removeElement(obsoletePanels[i]);
            }
        }
        debug();
    }

    /**
     * Toggle the expansion of a panel on and off.
     *
     * @param panel
     *      The container <code>TabPanel</code>.
     */
    @Override
    public void triggerExpand(final TabPanel tabPanel) {
        if (isExpanded(tabPanel)) {
            expanded.put(tabPanel, Boolean.FALSE);
        }
        else {
            expanded.put(tabPanel, Boolean.TRUE);
        }
        synchronize();
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
     * Synchronize the container in the display.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param remote
     *            A remote event <code>Boolean</code> indicator.
     */
    void syncContainer(final Long containerId, final Boolean remote) {
        debug();
        final Container container = read(containerId);
        // remove the container from the panel list
        if (null == container) {
            removeContainerPanel(container);
        } else {
            if (containsContainerPanel(container)) {
                // if the reload is the result of a remote event add the container
                // at the top of the list; otherwise add it in the same location
                // it previously existed
                final Integer indexOfContainerPanel = indexOfContainerPanel(container);
                final Boolean expanded = isExpanded(getContainerPanel(container));
                removeContainerPanel(container);
                if (remote) {
                    addContainerPanel(0, expanded, container);
                } else {
                    addContainerPanel(indexOfContainerPanel, expanded, container);
                }
            } else {
                addContainerPanel(0, container);
            }
        }

        synchronize();
        debug();
    }

    /**
     * Synchronize a document.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param remote
     *            A remote event <code>Boolean</code> indicator.
     */
    void syncDocument(final Long documentId, final Boolean remote) {
        syncContainer(containerIdLookup.get(documentId), remote);
    }

    /**
     * Add a container panel. This will read the container's versions and add
     * the appropriate version panel as well.
     * 
     * @param container
     *            A <code>container</code>.
     */
    private void addContainerPanel(final Container container) {
        addContainerPanel(containerPanels.size() == 0 ? 0 : containerPanels
                .size() - 1, container);
    }

    /**
     * Add a container panelThis will read the container's versions and add the
     * appropriate version panel as well.
     * 
     * @param index
     *            An <code>Integer</code> index.
     * @param expanded
     *            An expanded <code>Boolean</code> flag.
     * @param container
     *            A <code>container</code>.
     */
    private void addContainerPanel(final Integer index, final Boolean expanded,
            final Container container) {
        final TabPanel containerPanel = toDisplay(container);
        containerPanels.add(index, containerPanel);
        this.expanded.put(containerPanel, expanded);
        final ContainerDraft draft = readDraft(container.getId());
        if (null != draft) {
            for (final Document document : draft.getDocuments()) {
                containerIdLookup.put(document.getId(), container.getId());
            }
        }
        final List<ContainerVersion> versions = readVersions(container.getId());
        final Map<ContainerVersion, List<Document>> documents = new HashMap<ContainerVersion, List<Document>>(versions.size(), 1.0F);
        final Map<ContainerVersion, Map<User, ArtifactReceipt>> users = new HashMap<ContainerVersion, Map<User, ArtifactReceipt>>(versions.size(), 1.0F);
        final Map<ContainerVersion, User> publishedBy = new HashMap<ContainerVersion, User>(versions.size(), 1.0F);

        List<Document> versionDocuments;
        for (final ContainerVersion version : versions) {
            versionDocuments = readDocuments(version.getArtifactId(),
                    version.getVersionId());
            for (final Document versionDocument : versionDocuments) { 
                containerIdLookup.put(versionDocument.getId(), container.getId());
            }
            documents.put(version, versionDocuments);
            users.put(version, readUsers(version.getArtifactId(), version.getVersionId()));
            publishedBy.put(version, readUser(version.getUpdatedBy()));
        }
        versionsPanels.put(containerPanel,
                toDisplay(container, draft, versions, documents, users,
                        publishedBy));
    }

    /**
     * Add a container to the panel.
     * 
     * @param index
     *            The index in the list.
     * @param container
     *            A <code>Container</code>.
     */
    private void addContainerPanel(final Integer index, final Container container) {
        addContainerPanel(index, Boolean.FALSE, container);
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
     * Determine if the container panel exists.
     * 
     * @param container
     *            A <code>Container</code>.
     * @return True if it exists.
     */
    private Boolean containsContainerPanel(final Container container) {
        return -1 != indexOfContainerPanel(container);
    }

    /**
     * Obtain a container panel.
     * 
     * @param container
     *            A <code>Container</code>.
     * @return A <code>TabPanel</code>.
     */
    private TabPanel getContainerPanel(final Container container) {
        return containerPanels.get(indexOfContainerPanel(container));
    }

    /**
     * Obtain the index of the container panel.
     * 
     * @param container
     *            A <code>Container</code>.
     * @return A <code>Integer</code> index; or -1 if the container does not
     *         exist in the panel list.
     */
    private Integer indexOfContainerPanel(final Container container) {
        for (int i = 0; i < containerPanels.size(); i++) {
            if (((ContainerPanel) containerPanels.get(i)).getContainerId()
                    .equals(container.getId())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Determine if the panel is expanded.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @return True if the panel is expanded; false otherwise.
     */
    private Boolean isExpanded(final TabPanel tabPanel) {
        return expanded.containsKey(tabPanel) ? expanded.get(tabPanel) : Boolean.FALSE;
    }

    /**
     * Read the container from the provider.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return A <code>Container</code>.
     */
    private Container read(final Long containerId) {
        return ((ContainerProvider) contentProvider).read(containerId);
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
     * Remove a container panel.
     * 
     * @param container
     *            A <code>Container</code>.
     */
    private void removeContainerPanel(final Container container) {
        final TabPanel containerPanel = getContainerPanel(container);
        Long containerId;
        for (final Iterator<Long> iLookupValues =
            containerIdLookup.values().iterator(); iLookupValues.hasNext(); ) {
            containerId = iLookupValues.next();
            if (containerId.equals(container.getId())) {
                iLookupValues.remove();
            }
        }
        containerPanels.remove(containerPanel);
        versionsPanels.remove(containerPanel);
        expanded.remove(containerPanel);
        
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
            final ContainerDraft draft, final List<ContainerVersion> versions,
            final Map<ContainerVersion, List<Document>> documents,
            final Map<ContainerVersion, Map<User, ArtifactReceipt>> users,
            final Map<ContainerVersion, User> publishedBy) {
        final ContainerVersionsPanel panel = new ContainerVersionsPanel(this);
        panel.setDraft(container, draft);
        for (final ContainerVersion version : versions) {
            panel.add(version, documents.get(version), users.get(version), publishedBy.get(version));
        }
        panel.selectFirstVersion();
        return panel;
    }
}
