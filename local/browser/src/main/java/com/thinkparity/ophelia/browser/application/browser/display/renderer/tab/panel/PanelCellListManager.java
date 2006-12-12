/**
 * Created On: 12-Dec-06 12:01:28 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.util.List;

import javax.swing.DefaultListModel;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PanelCellListManager {
       
    /** The complete list of <code>Cell</code>. */
    private List<? extends Cell> cells;
    
    /** The component invoker. */
    private final Component invoker;
    
    /** The panel localization. */
    private final MainCellL18n localization;
    
    /** The list model. */
    private final DefaultListModel listModel;
    
    /** The JList. */
    private final javax.swing.JList jList;
    
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
    
    /** Flag indicating if row 0 stays at the top */
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
     * @param invoker
     *            A <code>Component</code> invoker.
     * @param listModel
     *            A <code>DefaultListModel</code>.
     * @param jList
     *            The <code>JList</code>.
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
     *            Flag indicating that the first row is fixed in place.                                          
     */
    public PanelCellListManager(
            final Component invoker,
            final MainCellL18n localization,
            final DefaultListModel listModel,
            final javax.swing.JList jList,
            final int visibleRows,
            final javax.swing.JLabel firstJLabel,
            final javax.swing.JLabel previousJLabel,
            final javax.swing.JLabel countJLabel,
            final javax.swing.JLabel nextJLabel,
            final javax.swing.JLabel lastJLabel,
            final Boolean fixedFirstRow) {
        super();
        this.invoker = invoker;
        this.localization = localization;
        this.listModel = listModel;
        this.jList = jList;
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
    
    private void initializeMouseListeners() {
        MouseAdapter mouseAdapter = new java.awt.event.MouseAdapter() {
            public void mouseClicked(final java.awt.event.MouseEvent e) {
                iconJLabelMouseClicked(e);
            }
            public void mouseEntered(final java.awt.event.MouseEvent e) {
                iconJLabelMouseEntered(e);
            }
            public void mouseExited(final java.awt.event.MouseEvent e) {
                iconJLabelMouseExited(e);
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
            countJLabel.setText(localization.getString("countJLabel", currentPage+1, numberPages));
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
    
    private void iconJLabelMouseClicked(final java.awt.event.MouseEvent e) {
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
        jList.setSelectedIndex(0);
        invoker.repaint();
    }
    
    private void iconJLabelMouseEntered(final java.awt.event.MouseEvent e) {
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.HAND_CURSOR);
    }

    private void iconJLabelMouseExited(final java.awt.event.MouseEvent e) {
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.DEFAULT_CURSOR);
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
        listModel.clear();
        if (rowsThisPage > 0) {
            if (fixedFirstRow) {
                listModel.addElement(cells.get(0)); 
                for (int index = 1; index < rowsThisPage; index++) {
                    listModel.addElement(cells.get(offset+index));
                }
            } else {
                for (int index = 0; index < rowsThisPage; index++) {
                    listModel.addElement(cells.get(offset+index));
                }
            }
        }
    }
}
