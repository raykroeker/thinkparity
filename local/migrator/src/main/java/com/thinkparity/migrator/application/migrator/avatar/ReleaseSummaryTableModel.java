/*
 * Created On: Jun 25, 2006 12:52:14 PM
 * $Id$
 */
package com.thinkparity.migrator.application.migrator.avatar;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReleaseSummaryTableModel {

    /** The release content provider. */
    private ContentProviderFlat contentProvider;

    /** The table model. */
    private final TableModel jTableModel;

    /** The releases. */
    private final List<TableRowRelease> releases;

    /** Create ReleaseSummaryTableModel. */
    public ReleaseSummaryTableModel(final ReleaseSummary releaseSummary) {
        super();
        this.jTableModel = new DefaultTableModel();
        this.releases = new LinkedList<TableRowRelease>();
    }

    /**
     * Obtain the table model.
     * 
     * @return The table model.
     */
    TableModel getTableModel() { return jTableModel; }

    /**
     * Set the content provider.
     * 
     * @param contentProvider
     *            The content provider.
     */
    void setContentProvider(final ContentProviderFlat contentProvider) {
        this.contentProvider = contentProvider;
        initModel();
    }

    /**
     * Initialize the release summary table model.
     *
     */
    private void initModel() {
        // read the releases from the provider into the table
        releases.clear();
        releases.addAll(readReleases());
        syncModel();
    }

    /**
     * Read the list of releases from the content provider.
     * 
     * @return A list of releases.
     */
    private List<TableRowRelease> readReleases() {
        final List<TableRowRelease> l = new LinkedList<TableRowRelease>();
        final TableRowRelease[] a =
                (TableRowRelease[]) contentProvider.getElements(null);
        for(final TableRowRelease trr : a) { l.add(trr); }
        return l;
    }

    /**
     * Synchronize the list of releases with the table.
     *
     */
    private void syncModel() {}
}
