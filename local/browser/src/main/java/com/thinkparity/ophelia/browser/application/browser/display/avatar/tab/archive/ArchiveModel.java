/*
 * Created On: Sep 1, 2006 8:13:28 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.archive;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import com.thinkparity.codebase.model.artifact.Artifact;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel;
import com.thinkparity.ophelia.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.archive.ArchiveCell;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ArchiveModel extends TabModel {

    /** A list of the archived artifact display cells. */
    private final List<ArchiveCell> archiveCells;

    /** The <code>ArchiveAvatar</code>'s <code>JList</code> model. */
    private final DefaultListModel listModel;

    /** A list of all visible cells in the tab list. */
    private final List<TabCell> visibleCells;

    /** Create ArchiveModel. */
    ArchiveModel() {
        super();
        this.archiveCells = new ArrayList<ArchiveCell>();
        this.listModel = new DefaultListModel();
        this.visibleCells = new ArrayList<TabCell>();
    }

    /**
     * Synchronize an artifact.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param remote
     *            The remote event flag.
     */
    void synchronizeArtifact(final Long artifactId, final Boolean remote) {}

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
    protected ListModel getListModel() {
        return listModel;
    }

    /**
     * Initialize the model. This reads from the provider the initial archive
     * list.
     * 
     */
    protected void initialize() {
        archiveCells.clear();
        archiveCells.addAll(readArchivedArtifacts());
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
        for (final ArchiveCell archiveCell : archiveCells) {
            visibleCells.add(archiveCell);
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

    /**
     * Read the archived artifacts.
     * 
     * @return A <code>List&lt;ArchiveCell&gt;</code>.
     */
    private List<ArchiveCell> readArchivedArtifacts() {
        final List<ArchiveCell> display = new LinkedList<ArchiveCell>();
        final Artifact[] artifacts = (Artifact[]) ((FlatContentProvider) contentProvider).getElements(null);
        for(final Artifact artifact : artifacts) {
            display.add(toDisplay(artifact));
        }
        return display;
    }

    /**
     * Create a display cell for an artifact.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @return A display cell <code>ArchiveCell</code>.
     */
    private ArchiveCell toDisplay(final Artifact artifact) {
        final ArchiveCell display = new ArchiveCell();
        display.setArtifact(artifact);
        return display;
    }
}
