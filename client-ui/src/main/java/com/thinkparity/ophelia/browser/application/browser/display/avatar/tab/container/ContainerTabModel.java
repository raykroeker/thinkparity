/*
 * Created On: 13-Jul-06 1:03:06 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerDraftMonitor;
import com.thinkparity.ophelia.model.events.ContainerDraftListener;
import com.thinkparity.ophelia.model.events.ContainerEvent;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterBy;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.container.ContainerProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view.DocumentView;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view.DraftView;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.view.PublishedToView;
import com.thinkparity.ophelia.browser.platform.application.Application;
import com.thinkparity.ophelia.browser.platform.application.ApplicationListener;

/**
 * @author rob_masako@shaw.ca; raykroeker@gmail.com
 * @version 1.1.2.4
 */
public final class ContainerTabModel extends TabPanelModel<Long> implements
        TabAvatarFilterDelegate {

    /** A session key for the draft monitor. */
    private static final String SK_DRAFT_MONITOR;

    static {
        SK_DRAFT_MONITOR = new StringBuffer(ContainerTabModel.class.getName())
            .append("#ContainerDraftMonitor").toString();
    }

    /** A <code>ContainerTabActionDelegate</code>. */
    private final ContainerTabActionDelegate actionDelegate;

    /** The <code>Comparator</code>. */
    private Comparator<TabPanel> comparator;

    /** A way to lookup container ids from document ids. */
    private final Map<Long, Long> containerIdLookup;

    /** The browser controller's display helper. */
    private final ContainerTabImportHelper containerTabImportHelper;

    /** The current filter. */
    private Filter<TabPanel> filterBy;

    /** A <code>ContainerTabPopupDelegate</code>. */
    private final ContainerTabPopupDelegate popupDelegate;

    /**
     * Create ContainerModel.
     * 
     */
    ContainerTabModel() {
        super();
        this.actionDelegate = new ContainerTabActionDelegate(this);
        this.containerIdLookup = new HashMap<Long, Long>();
        this.containerTabImportHelper = new ContainerTabImportHelper(this, browser);
        this.popupDelegate = new ContainerTabPopupDelegate(this);
        addApplicationListener();
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
    public void toggleExpansion(TabPanel tabPanel, Boolean animate) {
        checkThread();
        ContainerPanel containerPanel = (ContainerPanel) tabPanel;
        if (!containerPanel.getContainer().isSeen()) {
            final Long containerId = containerPanel.getContainer().getId();  
            browser.runApplyContainerFlagSeen(containerPanel.getContainer().getId());
            syncContainer(containerPanel.getContainer().getId(), Boolean.FALSE);
            containerPanel = (ContainerPanel)lookupPanel(containerId);
        }
        
        super.toggleExpansion(containerPanel, animate);
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#canImportData(java.awt.datatransfer.DataFlavor[])
     * 
     */
    @Override
    protected boolean canImportData(final DataFlavor[] transferFlavors) {
        checkThread();
        return containerTabImportHelper.canImportData(transferFlavors);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#canImportData(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel, java.awt.datatransfer.DataFlavor[])
     *
     */
    @Override
    protected boolean canImportData(final TabPanel tabPanel,
            final DataFlavor[] transferFlavors) {
        checkThread();
        return containerTabImportHelper.canImportData(tabPanel, transferFlavors);
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
            logger.logVariable("filteredPanel.getContainer().getName()", ((ContainerPanel) filteredPanel).getContainer().getName());
        }
        for (final TabPanel visiblePanel : visiblePanels) {
            logger.logVariable("visiblePanel.getContainer().getName()", ((ContainerPanel) visiblePanel).getContainer().getName());
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#importData(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel, java.awt.datatransfer.Transferable)
     *
     */
    @Override
    protected void importData(final TabPanel tabPanel, final Transferable transferable) {
        checkThread();
        Assert.assertTrue(canImportData(tabPanel,
                transferable.getTransferDataFlavors()),
                "Cannot import data {0} onto {1}.", transferable, tabPanel);
        containerTabImportHelper.importData(((ContainerPanel) tabPanel).getContainer(), transferable);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#importData(java.awt.datatransfer.Transferable)
     * 
     */
    @Override
    protected void importData(final Transferable transferable) {
        checkThread();
        Assert.assertTrue(canImportData(transferable.getTransferDataFlavors()),
                "Cannot import data {0}.", transferable);
        containerTabImportHelper.importData(transferable);
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
        applyFilter(getInitialFilter());
        debug();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#lookupId(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     * 
     */
    @Override
    protected Long lookupId(final TabPanel tabPanel) {
        return ((ContainerPanel)tabPanel).getContainer().getId();
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
        return ((ContainerProvider) contentProvider).search(searchExpression);
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
            for (final DocumentView versionDocumentView : versionDocumentViews) {
                containerIdLookup.put(versionDocumentView.getDocumentId(), container.getId());
            }
            documentViews.put(version, versionDocumentViews);
            publishedTo.put(version, readPublishedTo(version.getArtifactId(),
                    version.getVersionId()));
            publishedBy.put(version, readUser(version.getUpdatedBy()));
        }
        containerPanel.setPanelData(versions, documentViews, publishedTo,
                publishedBy, readTeam(container.getId()));
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#synchronize()
     *
     */
    @Override
    protected void synchronize() {
        super.synchronize();
    }
    
    /**
     * Obtain the popup delegate.
     * 
     * @return A <code>ContainerTabPopupDelegate</code>.
     */
    ContainerTabPopupDelegate getPopupDelegate() {
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
     * Determine if the specified user is the local user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return True if this is the local user; false otherwise.
     */
    Boolean readDoesExistContact(final User user) {
        return ((ContainerProvider) contentProvider).doesExistContact(
                user.getLocalId());
    }

    /**
     * Determine if the specified user is the local user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return True if this is the local user; false otherwise.
     */
    Boolean readDoesExistOutgoingUserInvitation(final User user) {
        return ((ContainerProvider) contentProvider).doesExistOutgoingUserInvitationForUser(
                user.getLocalId());
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
        return ((ContainerProvider) contentProvider).isDistributed(containerId).booleanValue();
    }

    /**
     * Determine if the local draft is modified, ie. at least one document changed.
     * 
     * @param containerId
     *            A <code>Long</code>.
     * @return True if this container has a document that has been modified; false otherwise.
     */
    Boolean readIsLocalDraftModified(final Long containerId) {
        return ((ContainerProvider) contentProvider).isLocalDraftModified(containerId).booleanValue();
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
        return user.getId().equals(profile.getId());
    }

    /**
     * Select the draft in a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    void setDraftSelection(final Long containerId) {
        checkThread();
        final TabPanel tabPanel = lookupPanel(containerId);
        if (isExpanded(tabPanel)) {
            ((ContainerPanel) tabPanel).setDraftSelection();
        }
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
        if (EventQueue.isDispatchThread()) {
            syncContainerImpl(containerId, remote);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    syncContainerImpl(containerId, remote);
                }
            });
        }
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
     * Synchronize a document for the case of added document.
     * Performance is a concern so unnecessary steps are avoided.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param document
     *            A <code>Document</code>.
     * @param draft
     *      A <code>ContainerDraft</code>.
     */
    void syncDocumentAdded(final Container container, final Document document,
            final ContainerDraft draft) {
        containerIdLookup.put(document.getId(), container.getId());
        lookupContainerPanel(container.getId()).setPanelData(container, draft);
        synchronize();
    }

    /**
     * Add an application listener. The session draft monitor is
     * stopped before the application ends.
     */
    private void addApplicationListener() {
        browser.addListener(new ApplicationListener() {
            public void notifyEnd(Application application) {
                stopSessionDraftMonitor();
            }
            public void notifyHibernate(Application application) {}
            public void notifyRestore(Application application) {}
            public void notifyStart(Application application) {}           
        });
    }

    /**
     * Add a container panel. This will read the container's versions and add
     * the appropriate version panel as well.
     * 
     * @param container
     *            A <code>container</code>.
     */
    private void addContainerPanel(final Container container) {
        addContainerPanel(panels.size() == 0 ? 0 : panels.size() - 1, container);
    }

    /**
     * Add a container panel. This will read the container's data and add the
     * container panel.
     * 
     * @param index
     *            An <code>Integer</code> index.
     * @param container
     *            A <code>container</code>.
     */
    private void addContainerPanel(final int index,
            final Container container) {
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
                containerIdLookup.put(document.getId(), container.getId());
            }
        }
        final TabPanel tabPanel = toDisplay(container, draftView, earliestVersion, latestVersion);
        panels.add(index, tabPanel);
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
        Assert.assertTrue(EventQueue.isDispatchThread(), "Container tab model not on the AWT event dispatch thread.");
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
     * Obtain a container draft monitor created by the model.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param listener
     *            A <code>ContainerDraftListener</code>.
     * @return A <code>ContainerDraftMonitor</code>.
     */
    private ContainerDraftMonitor getDraftMonitor(final Long containerId,
            final ContainerDraftListener listener) {
        return ((ContainerProvider) contentProvider).getDraftMonitor(containerId, listener);
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
     * Obtain the session draft monitor.
     * 
     * @return The session draft monitor; or null if no draft monitor is set.
     */
    private ContainerDraftMonitor getSessionDraftMonitor() {
        return (ContainerDraftMonitor) session.getAttribute(SK_DRAFT_MONITOR);        
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
     * Determine if the session draft monitor is set for the container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return True if the session draft monitor is not null and is monitoring
     *         the container.
     */
    private boolean isSetSessionDraftMonitor(final Long containerId) {
        final ContainerDraftMonitor monitor = getSessionDraftMonitor();
        return null != monitor && monitor.getContainerId().equals(containerId);
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
            if (((ContainerPanel) panels.get(i)).getContainer()
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
     * @return A <code>Map&lt;DocumentVersion, Delta&gt;</code>.
     */
    private List<DocumentView> readDocumentViews(final Long containerId,
            final Long versionId) {
        return ((ContainerProvider) contentProvider).readDocumentViews(
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
        return ((ContainerProvider) contentProvider).readDraftView(containerId);
    }

    /**
     * Determine if the draft document has been modified.
     * 
     * @param documentId
     *            A document id.
     * @return True if the draft document has been modified; false otherwise.
     */
    private boolean readIsDraftDocumentModified(final Long documentId) {
        return ((ContainerProvider) contentProvider).isDraftDocumentModified(documentId).booleanValue();
    }

    /**
     * Read to see if any draft document ArtifactState has become out of date.
     * This can happen during the time that the session draft monitor is disabled.
     * 
     * @param draft
     *      A <code>ContainerDraft</code>.
     * @return True if the a draft document state has changed; false otherwise.
     */
    private Boolean readIsDraftDocumentStateChanged(final ContainerDraft draft) {
        for (final Document document : draft.getDocuments()) {
            final ContainerDraft.ArtifactState state = draft.getState(document);
            if (ContainerDraft.ArtifactState.NONE == state ||
                ContainerDraft.ArtifactState.MODIFIED == state ) {
                final Boolean documentModified =
                    ContainerDraft.ArtifactState.NONE == state ? Boolean.FALSE : Boolean.TRUE;
                if (readIsDraftDocumentModified(document.getId()) != documentModified) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
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
        return ((ContainerProvider) contentProvider).readLatestVersion(containerId);
    }

    /**
     * Read the profile.
     * 
     * @return A <code>Profile</code>.   
     */
    private Profile readProfile() {
        return ((ContainerProvider) contentProvider).readProfile();
    }

    /**
     * Read the team.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return A list of <code>TeamMember</code>s.
     */
    private List<TeamMember> readTeam(final Long containerId) {
        return ((ContainerProvider) contentProvider).readTeam(containerId);
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
     * Read the published to view.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>PublishedToView</code>.
     */
    private PublishedToView readPublishedTo(final Long containerId,
            final Long versionId) {
        return ((ContainerProvider) contentProvider).readPublishedTo(
                containerId, versionId);
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
     *            The container id <code>Long</code>.
     * @param removeExpandedState
     *            Remove expanded state <code>boolean</code>.
     */
    private void removeContainerPanel(final Long containerId,
            final boolean removeExpandedState) {
        Long lookupContainerId;
        for (final Iterator<Long> iLookupValues =
            containerIdLookup.values().iterator(); iLookupValues.hasNext(); ) {
            lookupContainerId = iLookupValues.next();
            if (lookupContainerId.equals(containerId)) {
                iLookupValues.remove();
            }
        }
        final int panelIndex = lookupIndex(containerId);
        if (-1 == panelIndex) {
            logger.logError("Cannot remove container panel, container id {0}.", containerId);
        } else {
            final TabPanel containerPanel = panels.remove(panelIndex);
            if (removeExpandedState) {
                expandedState.remove(containerPanel);
            }
        }
        if (isSetSessionDraftMonitor(containerId)) {
            stopSessionDraftMonitor();
        }
    }

    /**
     * Start a draft monitor for a container.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    private void startSessionDraftMonitor(final Long containerId) {
        // if the current draft monitor is set for this container; do nothing
        if (isSetSessionDraftMonitor(containerId))
            return;
        // if a current draft monitor exists stop it
        ContainerDraftMonitor monitor = getSessionDraftMonitor();
        if (null != monitor) {
            stopSessionDraftMonitor();
        }
        // create a new monitor and start it
        monitor = getDraftMonitor(containerId, new ContainerDraftListener() {
            public void documentModified(final ContainerEvent e) {
                syncContainer(containerId, Boolean.FALSE);
            }
        });
        session.setAttribute(SK_DRAFT_MONITOR, monitor);
        monitor.start();
    }

    /**
     * Stop the session draft monitor.
     *
     */
    private void stopSessionDraftMonitor() {
        final ContainerDraftMonitor monitor = getSessionDraftMonitor();
        if (null != monitor) {
            monitor.stop();
            session.removeAttribute(SK_DRAFT_MONITOR);
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
    private void syncContainerImpl(final Long containerId, final Boolean remote) {
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
        final ContainerPanel panel = new ContainerPanel(session);
        panel.setActionDelegate(actionDelegate);
        panel.setPopupDelegate(popupDelegate);
        panel.setPanelData(container, draftView, earliestVersion, latestVersion);
        panel.setExpanded(isExpanded(panel));
        panel.setSelected(isSelected(panel));
        panel.setTabDelegate(this);
        if (isExpanded(panel)) {
            browser.runApplyContainerFlagSeen(panel.getContainer().getId());
        }
        // the session will maintain the single draft monitor for the tab
        if (draftView.isSetDraft() && draftView.isLocal()) {
            if (isExpanded(panel)) {
                startSessionDraftMonitor(panel.getContainer().getId());
            }
            panel.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(final PropertyChangeEvent evt) {
                    if ("expanded".equals(evt.getPropertyName())) {
                        if (panel.isExpanded()) {
                            // Check if the DocumentDraft become out of date while
                            // the draft monitor was off, if it has then syncContainer()
                            // will result in startSessionDraftMonitor() being called above.
                            if (readIsDraftDocumentStateChanged(draftView.getDraft())) {
                                syncContainer(container.getId(), Boolean.FALSE);
                            } else {
                                startSessionDraftMonitor(panel.getContainer().getId());
                            }
                        } else {
                            stopSessionDraftMonitor();
                        }
                    }
                }
            });
        }
        return panel;
    }

    /** An enumerated type defining the tab panel filtering. */
    private enum FilterBy implements Filter<TabPanel> {
        FILTER_BOOKMARK, FILTER_DRAFT_OWNER, FILTER_NONE;

        /**
         * @see com.thinkparity.codebase.filter.Filter#doFilter(java.lang.Object)
         */
        public Boolean doFilter(final TabPanel o) {
            final ContainerPanel panel = (ContainerPanel) o;
            // Items flagged true are removed.
            switch (this) {
            case FILTER_BOOKMARK:
                return !panel.getContainer().isBookmarked();
            case FILTER_DRAFT_OWNER:
                return !panel.isLocalDraft();
            case FILTER_NONE:
                return Boolean.FALSE;
            default:
                return false;
            }
        }
    }
}
