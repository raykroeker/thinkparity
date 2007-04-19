/**
 * Created On: 12-Dec-06 12:01:28 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

import java.awt.event.MouseAdapter;
import java.util.List;

import com.thinkparity.ophelia.browser.util.localization.Localization;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PanelCellListManager {
       
    /** The complete list of <code>Cell</code>. */
    private List<? extends Cell> cells;
    
    /** The list model. */
    private final PanelCellListModel listModel;
    
    /** The panel localization. */
    private final Localization localization;
    
    /** The number of visible rows. */
    private final int visibleRows;
    
    /** The first JLabel */
    private final javax.swing.JLabel firstJLabel;
    
    /** The previous JLabel */
    private final javax.swing.JLabel previousJLabel;
    
    /** The count JLabel */
    private final javax.swing.JLabel countJLabel;
    
    /** The next JLabel */
    private final javax.swing.JLabel nextJLabel;
    
    /** The last JLabel */
    private final javax.swing.JLabel lastJLabel;
    
    /** Flag indicating if row 0 stays at the top, & is in the cells list */
    private final Boolean fixedFirstRow;
            
    /** Number of rows per page */
    private final int perPage;
    
    /** The number of pages */
    private int numberPages;
    
    /** The current page */
    private int currentPage;
    
    /**
     * Create a PanelCellListManager.
     * This handles the paging up and down through a Cell list
     * by clicking on next / previous style buttons.
     * 
     * @param listModel
     *            A <code>PanelCellListModel</code>.
     * @param localization
     *            A <code>Localization</code>.          
     * @param visibleRows
     *            The number of visible rows.
     * @param firstJLabel
     *            The first <code>JLabel</code>.
     * @param previousJLabel
     *            The previous <code>JLabel</code>.           
     * @param countJLabel
     *            The count <code>JLabel</code>.                             
     * @param nextJLabel
     *            The next <code>JLabel</code>.                              
     * @param lastJLabel
     *            The last <code>JLabel</code>.
     * @param fixedFirstRow
     *            Flag indicating that the first row is fixed, & in the cells list.                                                   
     */
    public PanelCellListManager(
            final PanelCellListModel listModel,
            final Localization localization,
            final int visibleRows,
            final javax.swing.JLabel firstJLabel,
            final javax.swing.JLabel previousJLabel,
            final javax.swing.JLabel countJLabel,
            final javax.swing.JLabel nextJLabel,
            final javax.swing.JLabel lastJLabel,
            final Boolean fixedFirstRow) {
        super();
        this.localization = localization;
        this.listModel = listModel;
        this.visibleRows = visibleRows;
        this.firstJLabel = firstJLabel;
        this.previousJLabel = previousJLabel;
        this.countJLabel = countJLabel;
        this.nextJLabel = nextJLabel;
        this.lastJLabel = lastJLabel;
        this.fixedFirstRow = fixedFirstRow;
        perPage = fixedFirstRow ? visibleRows-1 : visibleRows;
        initializeMouseListeners();
    }
    
    /**
     * Initialize with the cells list.
     * Called after the cells list is populated or changed.
     * 
     * @param cells
     *            The complete list of <code>Cell</code>.
     */
    public void initialize(final List<? extends Cell> cells) {
        this.cells = cells;
        if (null == cells) {
            numberPages = 0;
        } else {                     
            final int firstItemOffset = fixedFirstRow ? 1 : 0;
            numberPages = 1 + (cells.size() - firstItemOffset - 1) / perPage;
        }
        currentPage = 0;
        updateModel();
        reloadControls();
    }
    
    /**
     * Show the first page.
     */
    public void showFirstPage() {
        if (0 != currentPage) {
            currentPage = 0;
            updateModel();
            reloadControls();
        }
    }
    
    private void initializeMouseListeners() {
        MouseAdapter mouseAdapter = new java.awt.event.MouseAdapter() {
            public void mousePressed(final java.awt.event.MouseEvent e) {
                iconJLabelMousePressed(e);
            }
        };
        this.firstJLabel.addMouseListener(mouseAdapter);
        this.previousJLabel.addMouseListener(mouseAdapter);
        this.nextJLabel.addMouseListener(mouseAdapter);
        this.lastJLabel.addMouseListener(mouseAdapter);
    }
    
    private void reloadControls() {
        reloadFirst();
        reloadPrevious();
        reloadCount();
        reloadNext();
        reloadLast();
    }
    
    private void reloadFirst() {
        firstJLabel.setVisible(currentPage > 1);
    }
    
    private void reloadPrevious() {
        previousJLabel.setVisible(currentPage > 0);
    }
    
    private void reloadCount() {
        if (numberPages > 1) {
            countJLabel.setText(localization.getString("countJLabel", new Object[] {currentPage+1, numberPages}));
            countJLabel.setVisible(true);
        } else {
            countJLabel.setVisible(false);
        }
    }
    
    private void reloadNext() {
        nextJLabel.setVisible(currentPage+1 < numberPages);
    }
    
    private void reloadLast() {
        lastJLabel.setVisible(currentPage+2 < numberPages);
    }
    
    private void iconJLabelMousePressed(final java.awt.event.MouseEvent e) {
        if (e.getSource().equals(firstJLabel)) {
            currentPage = 0;
        } else if (e.getSource().equals(previousJLabel)) {
            currentPage--;
        } else if (e.getSource().equals(nextJLabel)) {
            currentPage++;
        } else if (e.getSource().equals(lastJLabel)) {
            currentPage = numberPages - 1;
        }
        updateModel();
        reloadControls();
    }
    
    private void updateModel() {
        final int rowsThisPage;
        if (null == cells) {
            rowsThisPage = 0;
        } else if (currentPage+1 < numberPages) {
            rowsThisPage = visibleRows;
        } else {
            rowsThisPage = cells.size() - (numberPages-1)*perPage;
        }
        final int offset = currentPage * perPage;
        
        // Repopulate the list model
        listModel.getListModel().clear();
        if (rowsThisPage > 0) {
            if (fixedFirstRow) {
                listModel.getListModel().addElement(cells.get(0));
                for (int index = 1; index < rowsThisPage; index++) {
                    listModel.getListModel().addElement(cells.get(offset+index));
                }
            } else {
                for (int index = 0; index < rowsThisPage; index++) {
                    listModel.getListModel().addElement(cells.get(offset+index));
                }
            }
        }
    }
}
