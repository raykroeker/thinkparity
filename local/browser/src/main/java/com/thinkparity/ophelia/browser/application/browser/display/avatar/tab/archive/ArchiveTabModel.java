/*
 * Created On: 2007-01-17 12:00:00
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.archive;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.*;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterBy;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.archive.ArchiveTabProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.container.ContainerProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.archive.ArchiveTabActionDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.archive.ArchiveTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.archive.ArchiveTabPopupDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view.DocumentView;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view.DraftView;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view.PublishedToView;

/**
 * <b>Title:</b>thinkParity Archive Tab Model<br>
 * <b>Description:</b>The archive tab model implementation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ArchiveTabModel extends TabPanelModel<Long> implements
        TabAvatarFilterDelegate {

    /** A <code>ArchiveTabActionDelegate</code>. */
    private final ArchiveTabActionDelegate actionDelegate;

    /** The <code>Comparator</code>. */
    private Comparator<TabPanel> comparator;

    /** A way to lookup container ids from document ids. */
    private final Map<Long, Long> containerIdLookup;

    /** The current filter. */
    private Filter<TabPanel> filterBy;

    /** A <code>ArchiveTabPopupDelegate</code>. */
    private final ArchiveTabPopupDelegate popupDelegate;

    /**
     * Create ArchiveTabModel.
     * 
     */
    ArchiveTabModel() {
        super();
        this.actionDelegate = new ArchiveTabActionDelegateImpl(this);
        this.containerIdLookup = new HashMap<Long, Long>();
        this.popupDelegate = new ArchiveTabPopupDelegateImpl(this);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate#getFilterBy()
     */
    public List<TabAvatarFilterBy> getFilterBy() {
        final List<TabAvatarFilterBy> filterBy = new ArrayList<TabAvatarFilterBy>();
        for (final FilterBy filterByValue : FilterBy.values()) {
            filterBy.add(new TabAvatarFilterBy() {
                public Action getAction() {
                    return new AbstractAction() {
                        public void actionPerformed(final ActionEvent e) {
                            applyFilter(filterByValue);
                        }
                    };
                }
                public String getName() {
                    return filterByValue.toString();
                }
                public String getText() {
                    return getString(filterByValue);
                }
            });
        }
        return filterBy;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#getTabButtonActionDelegate()
     */
    @Override
    public TabButtonActionDelegate getTabButtonActionDelegate() {
        return actionDelegate;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate#isFilterApplied()
     */
    public Boolean isFilterApplied() {
        return (null != filterBy && !filterBy.equals(FilterBy.FILTER_NONE));
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate#isFilterSelected(com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterBy)
     */
    public Boolean isFilterSelected(final TabAvatarFilterBy tabAvatarFilterBy) {
        return (null != this.filterBy && this.filterBy.toString().equals(tabAvatarFilterBy.getName()));
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#toggleExpansion(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel, java.lang.Boolean)
     */
    @Override
    public void toggleExpansion(final TabPanel tabPanel, final Boolean animate) {
        checkThread();
        ArchiveTabPanel archivePanel = (ArchiveTabPanel) tabPanel;
        if (!archivePanel.getContainer().isSeen()) { 
            browser.runApplyContainerFlagSeen(archivePanel.getContainer().getId());
        }

        super.toggleExpansion(archivePanel, animate);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#applyFilter()
     */
    @Override
    protected void applyFilter() {
        checkThread();
        if (isFilterApplied()) {
            FilterManager.filter(filteredPanels, filterBy);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#applySearch(java.lang.String)
     *
     */
    @Override
    protected void applySearch(final String searchExpression) {
        this.comparator = new Comparator<TabPanel>() {
            public int compare(final TabPanel o1, final TabPanel o2) {
                return 0;
            }
        };
        super.applySearch(searchExpression);
    }

    /**
     * Apply the sort to the filtered list of panels.
     * 
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#applySort()
     */
    @Override
    protected void applySort() {
        checkThread();
        Collections.sort(filteredPanels, getComparator());
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#debug()
     * 
     */
    @Override
    protected void debug() {
        checkThread();
        logger.logDebug("{0} container panels.", panels.size());
        logger.logDebug("{0} filtered panels.", filteredPanels.size());
        logger.logDebug("{0} visible panels.", visiblePanels.size());
        for (final TabPanel filteredPanel : filteredPanels) {
            logger.logVariable("filteredPanel.getContainer().getName()", ((ArchiveTabPanel) filteredPanel).getContainer().getName());
        }
        for (final TabPanel visiblePanel : visiblePanels) {
            logger.logVariable("visiblePanel.getContainer().getName()", ((ArchiveTabPanel) visiblePanel).getContainer().getName());
        }
        logger.logDebug("{0} model elements.", listModel.size());
        final TabPanel[] listModelPanels = new TabPanel[listModel.size()];
        listModel.copyInto(listModelPanels);
        for (final TabPanel listModelPanel : listModelPanels) {
            logger.logVariable("listModelPanel.getId()", listModelPanel.getId());
        }
        logger.logDebug("Search expression:  {0}", searchExpression);
        logger.logDebug("{0} search result hits.", searchResults.size());
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#deletePanel(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     */
    @Override
    protected void deletePanel(final TabPanel tabPanel) {
        browser.runDeleteContainer(lookupId(tabPanel));
    }

    /**
     * Initialize the container model with containers; container versions;
     * documents and users from the provider.
     * 
     */
    @Override
    protected void initialize() {
        super.initialize();
        debug();
        clearPanels();
        final List<Container> containers = readContainers();
        for (final Container container : containers) {
            addContainerPanel(container);
        }
        applyFilter(getInitialFilter());
        debug();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#lookupId(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     * 
     */
    @Override
    protected Long lookupId(final TabPanel tabPanel) {
        return ((ArchiveTabPanel) tabPanel).getContainer().getId();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#lookupPanel(java.lang.Object)
     * 
     */
    @Override
    protected TabPanel lookupPanel(final Long panelId) {
        final int panelIndex = lookupIndex(panelId);
        return -1 == panelIndex ? null : panels.get(panelIndex);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#readSearchResults()
     * 
     */
    @Override
    protected List<Long> readSearchResults() {
        checkThread();
        return getProvider().search(searchExpression);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#removeSearch()
     *
     */
    @Override
    protected void removeSearch() {
        this.comparator = null;
        super.removeSearch();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#setExpandedPanelData(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     */
    @Override
    protected void setExpandedPanelData(final TabPanel tabPanel) {
        final ContainerPanel containerPanel = (ContainerPanel)tabPanel;
        final Container container = containerPanel.getContainer();
        final List<ContainerVersion> versions = readVersions(container.getId());
        final Map<ContainerVersion, List<DocumentView>> documentViews =
            new HashMap<ContainerVersion, List<DocumentView>>(versions.size(), 1.0F);
        final Map<ContainerVersion, PublishedToView> publishedTo =
            new HashMap<ContainerVersion, PublishedToView>(versions.size(), 1.0F);
        final Map<ContainerVersion, User> publishedBy = new HashMap<ContainerVersion, User>(versions.size(), 1.0F);
        List<DocumentView> versionDocumentViews;
        for (final ContainerVersion version : versions) {
            versionDocumentViews = readDocumentViews(version.getArtifactId(),
                    version.getVersionId());
            documentViews.put(version, versionDocumentViews);
            publishedTo.put(version, readPublishedTo(version.getArtifactId(),
                    version.getVersionId()));
            publishedBy.put(version, readUser(version.getUpdatedBy()));
        }
        containerPanel.setPanelData(versions, documentViews, publishedTo,
                publishedBy, readTeam(container.getId()));
    }

    /**
     * Obtain the popup delegate.
     * 
     * @return An <code>ArchiveTabPopupDelegate</code>.
     */
    ArchiveTabPopupDelegate getPopupDelegate() {
        return popupDelegate;
    }

    /**
     * Lookup the container panel.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return A <code>ContainerPanel</code>.
     */
    ContainerPanel lookupContainerPanel(final Long containerId) {
        final int index = lookupIndex(containerId);
        return -1 == index ? null : (ContainerPanel) panels.get(index);
    }

    /**
     * Determine if the container has been distributed.
     * 
     * @param containerId
     *            A <code>Long</code>.
     * @return True if this container has been distributed; false otherwise.
     */
    Boolean readIsDistributed(final Long containerId) {
        checkThread();
        return getProvider().isDistributed(containerId).booleanValue();
    }
    
    /**
     * Determine if the specified user is the local user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return True if this is the local user; false otherwise.
     */
    Boolean readIsLocalUser(final User user) {
        checkThread();
        final Profile profile = readProfile();
        return (user.getId().equals(profile.getId()));
    }

    /**
     * Select a version in a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     */
    void setVersionSelection(final Long containerId, final Long versionId) {
        checkThread();
        final TabPanel tabPanel = lookupPanel(containerId);
        if (isExpanded(tabPanel)) {
            ((ContainerPanel) tabPanel).setVersionSelection(versionId);
        }
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
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                applyBusyIndicator();
                syncContainerImpl(containerId, remote);
                removeBusyIndicator();
            }
        });
    }

    /**
     * Synchronize the container in the display.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param remote
     *            A remote event <code>Boolean</code> indicator.             
     */
    void syncContainerImpl(final Long containerId, final Boolean remote) {
        debug();
        boolean requestFocus = false;
        final Container container = read(containerId);
        // remove the container from the panel list
        if (null == container) {
            removeContainerPanel(containerId, true);
        } else {
            final int panelIndex = lookupIndex(container.getId());
            if (-1 < panelIndex) {
                // if the reload is the result of a remote event add the container
                // at the top of the list; otherwise add it in the same location
                // it previously existed
                requestFocus = ((Component)lookupPanel(containerId)).hasFocus();
                removeContainerPanel(containerId, false);
                if (remote) {
                    addContainerPanel(0, container);
                } else {
                    addContainerPanel(panelIndex, container);
                }
            } else {
                addContainerPanel(0, container);
            }
        }
        synchronize();
        if (requestFocus) {
            requestFocusInWindow(lookupPanel(containerId));
        }
        debug();
    }

    /**
     * Synchronize when a flag has changed.
     * Performance is a concern so unnecessary steps are avoided.
     * 
     * @param container
     *            A <code>Container</code>.
     */
    void syncFlagged(final Container container) {
        // check the panel exists, it might be on the container tab
        if (isPanel(container.getId())) {
            lookupContainerPanel(container.getId()).setPanelData(container);
            synchronize();
        }
    }

    /**
     * Add a lookup relationship.
     * 
     * @param documentId
     *            A document id <code>Long</code>.
     * @param containerId
     *            A container id <code>Long</code>.
     */
    private void addContainerIdLookup(final Long documentId, final Long containerId) {
        containerIdLookup.put(documentId, containerId);
    }

    /**
     * Add a container panel. This will read the container's versions and add
     * the appropriate version panel as well.
     * 
     * @param container
     *            A <code>container</code>.
     */
    private void addContainerPanel(final Container container) {
        checkThread();
        final DraftView draftView = readDraftView(container.getId());
        final ContainerVersion latestVersion = readLatestVersion(container.getId());
        final ContainerVersion earliestVersion;
        if (null == latestVersion) {
            earliestVersion = null;
        } else {
            earliestVersion = readEarliestVersion(container.getId());
        }
        if (draftView.isLocal()) {
            for (final Document document : draftView.getDocuments()) {
                addContainerIdLookup(document.getId(), container.getId());
            }
        }
        final TabPanel tabPanel = toDisplay(container, draftView, earliestVersion, latestVersion);
        panels.add(tabPanel);
        if (isExpanded(tabPanel)) {
            setExpandedPanelData(tabPanel);
        }
    }

    /**
     * Apply a filter to the panels.
     * 
     * @param filterBy
     *            A <code>FilterBy</code>.
     */
    private void applyFilter(final FilterBy filterBy) {
        debug();
        this.filterBy = filterBy;
        synchronize();
    }

    /**
     * Check we are on the AWT event dispatching thread.
     */
    private void checkThread() {
        Assert.assertTrue(EventQueue.isDispatchThread(), "Archive tab model not on the AWT event dispatch thread.");
    }

    /**
     * Get the tab comparator.
     * 
     * @return A <code>Comparator</code>.
     */
    private Comparator<TabPanel> getComparator() {
        if (null == comparator) {
            comparator = new Comparator<TabPanel>() {
                public int compare(final TabPanel o1, final TabPanel o2) {
                    // Descending sort by date first seen.
                    final ContainerPanel p1 = (ContainerPanel) o1;
                    final ContainerPanel p2 = (ContainerPanel) o2;
                    final int multiplier = -1;
                    return multiplier * p1.getDateFirstSeen().compareTo(p2.getDateFirstSeen());
                }
            };
        }
        return comparator;
    }

    /**
     * Get the initial filter.
     * 
     * @return A <code>FilterBy</code>.
     */
    private FilterBy getInitialFilter() {
        final FilterBy filterBy = FilterBy.FILTER_NONE;
        return filterBy;
    }

    /**
     * Obtain the provider.
     * 
     * @return An instance of <code>ArchiveTabProvider</code>.
     */
    private ArchiveTabProvider getProvider() {
        return (ArchiveTabProvider) contentProvider;
    }

    /**
     * Obtain a localized string for a filter.
     * 
     * @param filterBy
     *            A <code>FilterBy</code>.
     * @return A localized <code>String</code>.
     */
    private String getString(final FilterBy filterBy) {
        return localization.getString(filterBy);
    }

    /**
     * Determine if the panel exists for the container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return true if the panel exists.
     */
    private Boolean isPanel(final Long containerId) {
        return (-1 != lookupIndex(containerId));
    }

    /**
     * Lookup the index of the container's corresponding panel.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return An <code>int</code> index; or -1 if the container does not
     *         exist in the panel list.
     */
    private int lookupIndex(final Long containerId) {
        for (int i = 0; i < panels.size(); i++)
            if (((ArchiveTabPanel) panels.get(i)).getContainer()
                    .getId().equals(containerId))
                return i;
        return -1;
    }
    
    /**
     * Read the container from the provider.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return A <code>Container</code>.
     */
    private Container read(final Long containerId) {
        return getProvider().read(containerId);
    }

    /**
     * Read the containers from the provider.
     * 
     * @return The containers.
     */
    private List<Container> readContainers() {
        return getProvider().read();
    }

    /**
     * Read the documents from the provider.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A <code>Map&lt;DocumentVersion, Delta&gt;</code>.
     */
    private List<DocumentView> readDocumentViews(final Long containerId,
            final Long versionId) {
        return getProvider().readDocumentViews(
                containerId, versionId);
    }

    /**
     * Read the draft view for a container.
     *
     * @param containerId
     *      A container id <code>Long</code>.
     * @return A <code>DraftView</code>.
     */
    private DraftView readDraftView(final Long containerId) {
        return getProvider().readDraftView(containerId);
    }

    /**
     * Read the earliest version for a container.
     * 
     * @param containerId
     *      A container id <code>Long</code>.
     * @return A <code>ContainerVersion</code>.
     */
    private ContainerVersion readEarliestVersion(final Long containerId) {
        return ((ContainerProvider) contentProvider).readEarliestVersion(containerId);
    }

    /**
     * Read the latest version for a container.
     * 
     * @param containerId
     *      A container id <code>Long</code>.
     * @return A <code>ContainerVersion</code>.
     */
    private ContainerVersion readLatestVersion(final Long containerId) {
        return getProvider().readLatestVersion(containerId);
    }

    /**
     * Read the profile.
     * 
     * @return A <code>Profile</code>.   
     */
    private Profile readProfile() {
        return getProvider().readProfile();
    }

    /**
     * Read the published to user list.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>PublishedToView</code>.
     */
    private PublishedToView readPublishedTo(final Long containerId,
            final Long versionId) {
        return getProvider().readPublishedTo(containerId, versionId);
    }

    /**
     * Read the team.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return A list of <code>TeamMember</code>s.
     */
    private List<TeamMember> readTeam(final Long containerId) {
        return getProvider().readTeam(containerId);
    }

    /**
     * Read the user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    private User readUser(final JabberId userId) {
        return getProvider().readUser(userId);
    }

    /**
     * Read the container versions from the provider.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    private List<ContainerVersion> readVersions(final Long containerId) {
        return getProvider().readVersions(containerId);
    }

    /**
     * Remove a lookup relationship.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    private void removeContainerIdLookup(final Long containerId) {
        Long lookupContainerId;
        for (final Iterator<Long> iLookupValues =
                containerIdLookup.values().iterator(); iLookupValues.hasNext(); ) {
            lookupContainerId = iLookupValues.next();
            if (lookupContainerId.equals(containerId)) {
                iLookupValues.remove();
            }
        }
    }

    /**
     * Remove a container panel.
     * 
     * @param container
     *            The container id <code>Long</code>.
     * @param removeExpandedState
     *            Remove expanded state <code>boolean</code>.
     */
    private void removeContainerPanel(final Long containerId,
            final boolean removeExpandedState) {
        removeContainerIdLookup(containerId);
        final int panelIndex = lookupIndex(containerId);
        if (-1 == panelIndex) {
            logger.logError("Cannot remove archive panel, container id {0}.", containerId);
        } else {
            final TabPanel containerPanel = panels.remove(panelIndex);
            if (removeExpandedState) {
                removeExpandedState(containerPanel);
            }
        }
    }

    /**
     * Synchronize the container in the display.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param remote
     *            A remote event <code>Boolean</code> indicator.             
     */
    void syncContainerImpl(final Long containerId, final Boolean remote) {
        debug();
        boolean requestFocus = false;
        final Container container = read(containerId);
        if (null == container) {
            removeContainerPanel(containerId, true);
        } else {
            final int panelIndex = lookupIndex(containerId);
            if (-1 < panelIndex) {
                requestFocus = ((Component)lookupPanel(containerId)).isFocusOwner();
                removeContainerPanel(containerId, false);
            }
            addContainerPanel(container);
        }
        synchronize();
        if (requestFocus) {
            requestFocusInWindow(lookupPanel(containerId));
        }
        debug();
    }

    /**
     * Create a tab panel for a container.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draftView
     *            A <code>DraftView</code>.
     * @param earliestVersion
     *            The earliest <code>ContainerVersion</code>.
     * @param latestVersion
     *            The latest <code>ContainerVersion</code>.
     * @return A <code>TabPanel</code>.
     */
    private TabPanel toDisplay(
            final Container container,
            final DraftView draftView,
            final ContainerVersion earliestVersion,
            final ContainerVersion latestVersion) {
        final ArchiveTabPanel panel = new ArchiveTabPanel(session);
        panel.setActionDelegate(actionDelegate);
        panel.setPopupDelegate(popupDelegate);
        panel.setPanelData(container, draftView, earliestVersion, latestVersion);
        panel.setExpanded(isExpanded(panel));
        panel.setSelected(isSelected(panel));
        panel.setTabDelegate(this);
        if (isExpanded(panel)) {
            browser.runApplyContainerFlagSeen(panel.getContainer().getId());
        }
        return panel;
    }

    /** An enumerated type defining the tab panel filtering. */
    private enum FilterBy implements Filter<TabPanel> {
        FILTER_BOOKMARK, FILTER_NONE;

        /**
         * @see com.thinkparity.codebase.filter.Filter#doFilter(java.lang.Object)
         */
        public Boolean doFilter(final TabPanel o) {
            final ContainerPanel panel = (ContainerPanel) o;
            // Items flagged true are removed.
            switch (this) {
            case FILTER_BOOKMARK:
                return !panel.getContainer().isBookmarked();
            case FILTER_NONE:
                return Boolean.FALSE;
            default:
                return false;
            }
        }
    }
}
