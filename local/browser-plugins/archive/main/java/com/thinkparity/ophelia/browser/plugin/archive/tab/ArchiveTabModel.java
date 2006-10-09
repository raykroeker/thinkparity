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

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabPanelExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.TabPanelExtensionModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ArchiveTabModel extends TabPanelExtensionModel<ArchiveTabProvider> {

    /** A list of the archived containers. */
    private final List<ArchiveArtifactCell> containerCells;

    /** A list of the archived container versions. */
    private final Map<ArchiveCell, List<ArchiveArtifactVersionCell>> containerVersionCells;

    /** A list of the archived container version documents. */
    private final Map<ArchiveCell, List<ArchiveArtifactCell>> containerVersionDocumentCells;

    /** The <code>ArchiveTabAvatar</code>'s <code>JList</code> model. */
    private final DefaultListModel listModel;

    /** A list of all visible cells in the list. */
    private final List<ArchiveCell> visibleCells;

    /** Create ArchiveTabModel. */
    ArchiveTabModel(final TabPanelExtension extension) {
        super(extension);
        this.containerCells = new ArrayList<ArchiveArtifactCell>();
        this.containerVersionCells = new HashMap<ArchiveCell, List<ArchiveArtifactVersionCell>>();
        this.containerVersionDocumentCells = new HashMap<ArchiveCell, List<ArchiveArtifactCell>>();
        this.listModel = new DefaultListModel();
        this.visibleCells = new ArrayList<ArchiveCell>();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#debug()
     */
    @Override
    protected void debug() {
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
        containerCells.clear();
        containerCells.addAll(readContainers());
        List<ArchiveArtifactVersionCell> containerVersionCells;
        for (final ArchiveArtifactCell containerCell : containerCells) {
            containerVersionCells = this.containerVersionCells.get(containerCell);
            if (null == containerVersionCells) {
                containerVersionCells = new ArrayList<ArchiveArtifactVersionCell>();
            }
            containerVersionCells.addAll(readContainerVersions(containerCell.getArtifactUniqueId()));
            List<ArchiveArtifactCell> containerVersionDocumentCells;
            for (final ArchiveArtifactVersionCell containerVersionCell : containerVersionCells) {
                containerVersionDocumentCells =
                    this.containerVersionDocumentCells.get(containerVersionCell);
                if (null == containerVersionDocumentCells) {
                    containerVersionDocumentCells = new ArrayList<ArchiveArtifactCell>();
                }
                containerVersionDocumentCells.addAll(readContainerVersionDocuments(
                        containerCell.getArtifactUniqueId(),
                        containerVersionCell.getArtifactVersionId()));
                this.containerVersionDocumentCells.put(containerVersionCell, containerVersionDocumentCells);
            }
            this.containerVersionCells.put(containerCell, containerVersionCells);
        }
    }

    /**
     * Synchronize the internal state of the model with the display.
     * 
     */
    @Override
    protected void synchronize() {
        debug();

        // update all visible cells
        visibleCells.clear();
        for (final ArchiveCell containerCell : containerCells) {
            visibleCells.add(containerCell);
            if (containerCell.isExpanded()) {
                if (containerVersionCells.containsKey(containerCell)) {
                    for (final ArchiveCell containerVersionCell :
                        containerVersionCells.get(containerCell)) {
                        visibleCells.add(containerVersionCell);
                        if (containerVersionCell.isExpanded()) {
                            if (containerVersionDocumentCells.containsKey(containerVersionCell)) {
                                for (final ArchiveCell containerVersionDocumentCell :
                                    containerVersionDocumentCells.get(containerVersionCell)) {
                                    visibleCells.add(containerVersionDocumentCell);
                                }
                            }
                        }
                    }
                }
            }
        }

        // add visible cells not in the model; as well as update cell
        // locations
        for (final TabCell visibleCell : visibleCells) {
            if (!listModel.contains(visibleCell)) {
                listModel.add(visibleCells.indexOf(visibleCell), visibleCell);
            } else {
                // Always replace the element in the jList Model. The value of the
                listModel.removeElement(visibleCell);
                listModel.add(visibleCells.indexOf(visibleCell), visibleCell);
            }
        }

        // prune cells
        final TabCell[] tabCells = new TabCell[listModel.size()];
        listModel.copyInto(tabCells);
        for (final TabCell tabCell : tabCells) {
            if (!visibleCells.contains(tabCell)) {
                listModel.removeElement(tabCell);
            }
        }
        debug();
    }

    void synchronizeContainer(final UUID uniqueId) {
        synchronizeContainerInternal(uniqueId);
        synchronize();
    }

    /**
     * Read the archived containers.
     * 
     * @return A <code>List&lt;ArchiveArtifactCell&gt;</code>.
     */
    private ArchiveArtifactCell readContainer(final UUID uniqueId) {
        final Container container = ((ArchiveTabProvider) contentProvider).readContainer(uniqueId);
        final ArchiveArtifactCell display = toDisplay(container);
        return display;
    }

    /**
     * Read the archived containers.
     * 
     * @return A <code>List&lt;ArchiveArtifactCell&gt;</code>.
     */
    private List<ArchiveArtifactCell> readContainers() {
        final List<Container> containers = ((ArchiveTabProvider) contentProvider).readContainers();
        final List<ArchiveArtifactCell> display = new ArrayList<ArchiveArtifactCell>(containers.size());
        for(final Container container : containers) {
            display.add(toDisplay(container));
        }
        return display;
    }

    /**
     * Read the archived container version's documents.
     * 
     * @return A <code>List&lt;ArchiveArtifactCell&gt;</code>.
     */
    private List<ArchiveArtifactCell> readContainerVersionDocuments(
            final UUID uniqueId, final Long versionId) {
        final List<Document> documents = ((ArchiveTabProvider) contentProvider).readContainerVersionDocuments(uniqueId, versionId);
        final List<ArchiveArtifactCell> display = new ArrayList<ArchiveArtifactCell>(documents.size());
        for(final Document document : documents) {
            display.add(toDisplay(document));
        }
        return display;
    }

    /**
     * Read the archived container versions.
     * 
     * @return A <code>List&lt;ArchiveArtifactVersionCell&gt;</code>.
     */
    private List<ArchiveArtifactVersionCell> readContainerVersions(final UUID uniqueId) {
        final List<ContainerVersion> versions =
            ((ArchiveTabProvider) contentProvider).readContainerVersions(uniqueId);
        final List<ArchiveArtifactVersionCell> display =
            new ArrayList<ArchiveArtifactVersionCell>(versions.size());
        for(final ContainerVersion version : versions) {
            display.add(toDisplay(version));
        }
        return display;
    }

    /**
     * Synchronize the model based upon the fact that the container represented
     * by the unique id has changed.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     */
    private void synchronizeContainerInternal(final UUID uniqueId) {
        final ArchiveArtifactCell containerCell = readContainer(uniqueId);

        // if the container is null; we can assume the container has been
        // restored 
        if(null == containerCell.getArtifact()) {
            for (int i = 0; i < containerCells.size(); i++) {
                if (containerCells.get(i).getArtifactUniqueId().equals(uniqueId)) {
                    containerCells.remove(i);
                    break;
                }
            }
        }
        // the container is not null; therefore it is either new; or updated
        else {
            // the container is new
            if(!containerCells.contains(containerCell)) {
                containerCells.add(0, containerCell);
            }
            // the container has been updated
            else {
                final int index = containerCells.indexOf(containerCell);
                containerCells.remove(index);
                containerCells.add(0, containerCell);
            }
        }
    }

    /**
     * Create a display cell for an archived container.
     * 
     * @param container
     *            A <code>Container</code>.
     * @return A <code>ArchiveContainerCell</code>.
     */
    private ArchiveContainerCell toDisplay(final Container container) {
        final ArchiveContainerCell display = new ArchiveContainerCell();
        display.setContainer(container);
        return display;
    }

    /**
     * Create a display cell for an archived container version.
     * 
     * @param containerVersion
     *            A <code>ContainerVersion</code>.
     * @return A <code>ArchiveContainerVersionCell</code>.
     */
    private ArchiveContainerVersionCell toDisplay(final ContainerVersion containerVersion) {
        final ArchiveContainerVersionCell display = new ArchiveContainerVersionCell();
        display.setContainerVersion(containerVersion);
        return display;
    }

    /**
     * Create a display cell for an archived container version's document.
     * 
     * @param document
     *            A <code>Document</code>.
     * @return A <code>ArchiveDocumentCell</code>.
     */
    private ArchiveDocumentCell toDisplay(final Document document) {
        final ArchiveDocumentCell display = new ArchiveDocumentCell();
        display.setDocument(document);
        return display;
    }
}
