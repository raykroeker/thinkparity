/*
 * Created On: 13-Jul-06 1:03:06 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.swing.dnd.TxUtils;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.container.ContainerProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerPanel;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.util.DocumentUtil;

/**
 * @author rob_masako@shaw.ca; raykroeker@gmail.com
 * @version 1.1.2.4
 */
public final class ContainerTabModel extends TabPanelModel {

    /** An application. */
    public final Browser browser;

    /** A <code>ContainerTabActionDelegate</code>. */
    private final ContainerTabActionDelegate actionDelegate;

    /** A way to lookup container ids from document ids. */
    private final Map<Long, Long> containerIdLookup;

    /** A list of all container panels. */
    private final List<TabPanel> containerPanels;

    /** A list of the container's expanded state. */
    private final Map<TabPanel, Boolean> expandedState;

    /** A list model. */
    private final DefaultListModel listModel;

    /** A <code>ContainerTabPopupDelegate</code>. */
    private final ContainerTabPopupDelegate popupDelegate;

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

    /** A list of visible panels. */
    private final List<TabPanel> visiblePanels;

    /**
     * Create ContainerModel.
     * 
     */
    ContainerTabModel() {
        super();
        this.actionDelegate = new ContainerTabActionDelegate(this);
        this.browser = getBrowser();
        this.containerIdLookup = new HashMap<Long, Long>();
        this.containerPanels = new ArrayList<TabPanel>();
        this.expandedState = new HashMap<TabPanel, Boolean>();
        this.listModel = new DefaultListModel();
        this.popupDelegate = new ContainerTabPopupDelegate(this);
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
     * Determine if the specified user is the local user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return True if this is the local user; false otherwise.
     */
    public Boolean isLocalUser(final User user) {
        final Profile profile = readProfile();
        return (user.getId().equals(profile.getId()));
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
     * Determine if the container has been distributed.
     * 
     * @param containerId
     *            A <code>Long</code>.
     * @return True if this container has been distributed; false otherwise.
     */
    public boolean readIsDistributed(final Long containerId) {
        return ((ContainerProvider) contentProvider).isDistributed(containerId).booleanValue();
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
     * Synchronize the container in the display.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param remote
     *            A remote event <code>Boolean</code> indicator.
     */
    public void syncContainer(final Long containerId, final Boolean remote) {
        debug();
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
    public void syncDocument(final Long documentId, final Boolean remote) {
        syncContainer(containerIdLookup.get(documentId), remote);
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#canImportData(java.awt.datatransfer.DataFlavor[])
     * 
     */
    @Override
    protected boolean canImportData(final DataFlavor[] transferFlavors) {
        return TxUtils.containsJavaFileList(transferFlavors);
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#canImportData(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel, java.awt.datatransfer.DataFlavor[])
     *
     */
    @Override
    protected boolean canImportData(final TabPanel tabPanel,
            final DataFlavor[] transferFlavors) {
        if (TxUtils.containsJavaFileList(transferFlavors)) {
            return canImportData(((ContainerPanel) tabPanel).getContainer());
        }
        return false;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#importData(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel, java.awt.datatransfer.Transferable)
     *
     */
    @Override
    protected void importData(final TabPanel tabPanel,
            final Transferable transferable) {
        Assert.assertTrue(canImportData(tabPanel,
                transferable.getTransferDataFlavors()),
                "Cannot import data {0} onto {1}.", transferable, tabPanel);
        importData(((ContainerPanel) tabPanel).getContainer(), transferable);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#importData(java.awt.datatransfer.Transferable)
     * 
     */
    @Override
    protected void importData(final Transferable transferable) {
        Assert.assertTrue(canImportData(transferable.getTransferDataFlavors()),
                "Cannot import data {0}.", transferable);
        final File[] transferableFiles = extractFiles(transferable);
        // Determine the list of files to add to a new package. Check if the user
        // is trying to drag folders.
        final List<File> importFiles = new ArrayList<File>();
        Boolean foundFolders = Boolean.FALSE;
        Boolean foundFiles = Boolean.FALSE;
        for (final File transferableFile : transferableFiles) {
            if (transferableFile.isDirectory()) {
                foundFolders = Boolean.TRUE;
            } else {
                foundFiles = Boolean.TRUE;
                importFiles.add(transferableFile);
            }
        }
        // Report an error if the user tries to drag folders. Otherwise
        // create a package and add documents.
        if (foundFolders) {
            browser.displayErrorDialog("ErrorCreatePackageIsFolder");
        } else if (foundFiles) {
            browser.runCreateContainer(importFiles);
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
     * Create the final list of container cells; container draft cells; draft
     * document cells; container version cells and container version document
     * cells. The search filter is also applied here.
     * 
     */
    @Override
    protected void synchronize() {
        debug();
        /* add container panels and container version panels to the visibility
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
     *
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
        return ContainerPanel.class.isAssignableFrom(tabPanel.getClass());
    }

    /**
     * Determine whether or not thinkParity is running in development mode.
     * 
     * @return True if thinkParity is running in development mode.
     */
    boolean isDevelopmentMode() {
        return browser.isDevelopmentMode();
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
     * Add a container panel. This will read the container's versions and add the
     * appropriate version panel as well.
     * 
     * @param index
     *            An <code>Integer</code> index.
     * @param container
     *            A <code>container</code>.
     */
    private void addContainerPanel(final int index,
            final Container container) {
        final ContainerDraft draft = readDraft(container.getId());
        final ContainerVersion latestVersion = readLatestVersion(container.getId());
        if ((null != draft) && container.isLocalDraft()) {
            for (final Document document : draft.getDocuments()) {
                containerIdLookup.put(document.getId(), container.getId());
            }
        }
        final List<ContainerVersion> versions = readVersions(container.getId());
        final Map<ContainerVersion, Map<DocumentVersion, Delta>> documentVersions =
            new HashMap<ContainerVersion, Map<DocumentVersion, Delta>>(versions.size(), 1.0F);
        final Map<ContainerVersion, Map<User, ArtifactReceipt>> publishedTo =
            new HashMap<ContainerVersion, Map<User, ArtifactReceipt>>(versions.size(), 1.0F);
        final Map<ContainerVersion, User> publishedBy = new HashMap<ContainerVersion, User>(versions.size(), 1.0F);
        Map<DocumentVersion, Delta> versionDocumentVersions;
        for (final ContainerVersion version : versions) {
            versionDocumentVersions = readDocumentVersionDeltas(version.getArtifactId(), version.getVersionId());
            for (final Entry<DocumentVersion, Delta> entry : versionDocumentVersions.entrySet()) {
                containerIdLookup.put(entry.getKey().getArtifactId(), container.getId());
            }
            documentVersions.put(version, versionDocumentVersions);
            publishedTo.put(version, readUsers(version.getArtifactId(), version.getVersionId()));
            publishedBy.put(version, readUser(version.getUpdatedBy()));
        }
        containerPanels.add(index, toDisplay(container, draft, latestVersion,
                versions, documentVersions, publishedTo, publishedBy));
    }

    /**
     * Determine if an import can be performed on a container. The user needs to
     * have the draft, or alternatively if nobody has the draft then he needs to
     * be online in order to create a new draft.
     * 
     * @param container
     *            A <code>Container</code>.
     * @return True if an import can be performed.
     */
    private boolean canImportData(final Container container) {
        if (container.isLocalDraft()) {
            return true;                
        } else if (!container.isDraft() && isOnline()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Clear all panels.
     *
     */
    private void clearPanels() {
        containerPanels.clear();
    }

    /**
     * Extract a list of files from a transferable.
     * 
     * @param transferable
     *            A <code>Transferable</code>.
     * @return A <code>List</code> of <code>File</code>s.
     */
    private File[] extractFiles(final Transferable transferable) {
        try {
            return TxUtils.extractFiles(transferable);
        } catch (final Exception x) {
            throw new BrowserException("Cannot extract files from transferrable.", x);
        }
    }

    /**
     * Import data into a container.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param transferable
     *            Import data <code>Transferable</code>.
     */
    private void importData(final Container container,
            final Transferable transferable) {
        final File[] transferableFiles = extractFiles(transferable);
        // Get the list of documents in this package.
        final List <String> existingDocuments = new ArrayList<String>();
        List<Document> draftDocuments = null;
        if (container.isLocalDraft()) {
            draftDocuments = readDraftDocuments(container.getId());
            for (final Document document : draftDocuments) {
                existingDocuments.add(document.getName());
            }
        }
        // Determine the list of files to add and/or update. Check if the user
        // is trying to drag folders. Create two lists, one for adding and one
        // for updating, depending on whether there is a document of the same
        // name found in the package.
        final List<File> addFileList = new ArrayList<File>();
        final List<File> updateFileList = new ArrayList<File>();
        Boolean foundFolders = Boolean.FALSE;
        Boolean foundFilesToAdd = Boolean.FALSE;
        Boolean foundFilesToUpdate = Boolean.FALSE;
        for (final File transferableFile : transferableFiles) {
            if (transferableFile.isDirectory()) {
                foundFolders = Boolean.TRUE;
            } else {
                if (existingDocuments.contains(transferableFile.getName())) {
                    foundFilesToUpdate = Boolean.TRUE;
                    updateFileList.add(transferableFile);
                }
                else {
                    foundFilesToAdd = Boolean.TRUE;
                    addFileList.add(transferableFile);
                }
            }
        }
        // Report an error if the user tries to drag folders.
        if (foundFolders) {
            browser.displayErrorDialog("ErrorAddDocumentIsFolder");
            return;
        }
        // If the draft is required, attempt to get it. This should succeed
        // unless somebody managed to get the draft, or the system went offline,
        // since the call to canImport() above.
        if (foundFilesToUpdate || foundFilesToAdd) {
            if (!container.isLocalDraft() && !container.isDraft() &&
                    (Connection.ONLINE == browser.getConnection())) {
                browser.runCreateContainerDraft(container.getId());
            }
            
            if (!container.isLocalDraft()) {
                browser.displayErrorDialog("ErrorAddDocumentLackDraft",
                        new Object[] { container.getName() });
                return;
            }
            draftDocuments = readDraftDocuments(container.getId());
        }
        // Add one or more documents.
        if (foundFilesToAdd) {
            browser.runAddContainerDocuments(container.getId(), addFileList
                    .toArray(new File[] {}));
        }
        // Update one or more documents.
        final DocumentUtil documentUtil = DocumentUtil.getInstance();
        if ((foundFilesToUpdate) && (null != draftDocuments)) {
            for (final File file : updateFileList) {
                if (documentUtil.contains(draftDocuments, file)) {
                    final Document document =
                        draftDocuments.get(documentUtil.indexOf(draftDocuments, file));
                    if (readIsDraftDocumentModified(document.getId())) {
                        if (browser.confirm("ConfirmOverwriteWorking",
                        new Object[] { file.getName() })) {
                            browser.runUpdateDocumentDraft(document.getId(), file);
                        }
                    } else {
                        browser.runUpdateDocumentDraft(document.getId(), file);
                    } 
                }
            }
        }
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
     * Lookup the index of the container's corresponding panel.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return An <code>int</code> index; or -1 if the container does not
     *         exist in the panel list.
     */
    private int lookupIndex(final Long containerId) {
        for (int i = 0; i < containerPanels.size(); i++)
            if (((ContainerPanel) containerPanels.get(i)).getContainer()
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
    private Map<DocumentVersion, Delta> readDocumentVersionDeltas(
            final Long containerId, final Long versionId) {
        return ((ContainerProvider) contentProvider).readDocumentVersionDeltas(
                containerId, versionId);
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
     * Read the draft's document list.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @return A <code>List</code> of <code>Document</code>s.
     */
    private List<Document> readDraftDocuments(final Long containerId) {
        final ContainerDraft draft = readDraft(containerId);
        if (null == draft) {
            return Collections.emptyList();
        } else {
            return draft.getDocuments();
        }
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
     *            A <code>Container</code>.
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
        final ContainerPanel panel = new ContainerPanel();
        panel.setActionDelegate(actionDelegate);
        panel.setPanelData(container, draft, latestVersion, versions,
                documentVersions, publishedTo, publishedBy);
        panel.setPopupDelegate(popupDelegate);
        panel.setExpanded(isExpanded(panel));
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
                browser.runApplyContainerFlagSeen(
                        containerPanel.getContainer().getId());
            }
            containerPanel.setExpanded(expanded);
        } else {
            expanded = Boolean.FALSE;
        }
        expandedState.put(tabPanel, expanded);
    }
}
