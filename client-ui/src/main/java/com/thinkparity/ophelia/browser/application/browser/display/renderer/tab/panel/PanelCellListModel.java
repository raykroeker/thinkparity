/**
 * Created On: 16-Dec-06 1:18:16 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

import java.util.List;

import javax.swing.DefaultListModel;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PanelCellListModel {
    
    /** The tab panel */
    private final DefaultTabPanel tabPanel;
    
    /** The list model <code>DefaultListModel</code>. */
    private final DefaultListModel listModel;
    
    /** The list manager. */
    private final PanelCellListManager listManager;
    
    /** The selection. */
    private int selectedIndex;
    
    /**
     * Create a PanelCellListModel.
     * 
     * Handles the model of visible panel cells, including
     * selection index and next/previous buttons, for a list
     * of Cells.
     * 
     * @param tabPanel
     *            A <code>DefaultTabPanel</code> invoker.
     * @param localization
     *            A <code>MainCellL18n</code>.          
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
     */
    public PanelCellListModel(
        final DefaultTabPanel tabPanel,
        final MainCellL18n localization,
        final int visibleRows,
        final javax.swing.JLabel firstJLabel,
        final javax.swing.JLabel previousJLabel,
        final javax.swing.JLabel countJLabel,
        final javax.swing.JLabel nextJLabel,
        final javax.swing.JLabel lastJLabel) {
        super();
        this.tabPanel = tabPanel;
        listModel = new DefaultListModel();
        listManager = new PanelCellListManager(this, localization,
                visibleRows, firstJLabel, previousJLabel, countJLabel,
                nextJLabel, lastJLabel, Boolean.TRUE);    
        selectedIndex = -1;
    }
    
    public void initialize(final List<? extends Cell> cells) {
        listManager.initialize(cells);
        setSelectedIndex(0);
    }
    
    public Boolean isSelectionEmpty() {
        return (selectedIndex < 0);
    }
    
    public DefaultListModel getListModel() {
        return listModel;
    }
    
    public int getIndexOf(final Cell cell) {
        return listModel.indexOf(cell);
    }
    
    public Cell getSelectedCell() {
        if (isSelectionEmpty()) {
            return null;
        } else {
            return (Cell)listModel.get(selectedIndex);
        }
    }
    
    public int getSelectedIndex() {
        return selectedIndex;
    }
    
    public void setSelectedCell(final Cell cell) {
        setSelectedIndex(listModel.indexOf(cell));
    }
    
    public void setSelectedIndex(final int selectedIndex) {
        final int saveSelectedIndex = this.selectedIndex;
        this.selectedIndex = selectedIndex;
        if ((saveSelectedIndex != selectedIndex) && !isSelectionEmpty()) {
            tabPanel.panelCellSelectionChanged(getSelectedCell());
        }
    }
}
