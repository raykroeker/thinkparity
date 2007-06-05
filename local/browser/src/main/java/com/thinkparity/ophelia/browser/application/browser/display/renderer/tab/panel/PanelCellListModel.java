/**
 * Created On: 16-Dec-06 1:18:16 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

import java.text.MessageFormat;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.KeyStroke;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.util.localization.Localization;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class PanelCellListModel implements PanelSelectionManager{

    /** The default selected row when the content changes. */
    private static final int DEFAULT_SELECTED_ROW_PAGING;

    /** The default selected row when the content changes. */
    private static final int DEFAULT_SELECTED_ROW_START;

    /** A session key pattern for the list's selected index. */
    private static final String SK_LIST_SELECTED_INDEX_PATTERN;

    static {
        DEFAULT_SELECTED_ROW_PAGING = 1;
        DEFAULT_SELECTED_ROW_START = 0;
        SK_LIST_SELECTED_INDEX_PATTERN = "PanelCellListModel#List.getSelectedIndex({0}:{1})";
    }

    /** The list manager. */
    private final PanelCellListManager listManager;

    /** The list model <code>DefaultListModel</code>. */
    private final DefaultListModel listModel;

    /** The list name */
    private final String listName;

    /** The selection. */
    private int selectedIndex;

    /** The tab panel */
    private final DefaultTabPanel tabPanel;

    /**
     * Create a PanelCellListModel.
     * 
     * Handles the model of visible panel cells, including
     * selection index and next/previous buttons, for a list
     * of Cells.
     * 
     * @param tabPanel
     *            A <code>DefaultTabPanel</code> invoker.
     * @param listName
     *            A list name <code>String</code>.         
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
     */
    public PanelCellListModel(
        final DefaultTabPanel tabPanel,       
        final String listName,
        final Localization localization,
        final int visibleRows,
        final javax.swing.JLabel firstJLabel,
        final javax.swing.JLabel previousJLabel,
        final javax.swing.JLabel countJLabel,
        final javax.swing.JLabel nextJLabel,
        final javax.swing.JLabel lastJLabel) {
        super();
        this.tabPanel = tabPanel;
        this.listName = listName;
        listModel = new DefaultListModel();
        listManager = new PanelCellListManager(this, localization,
                visibleRows, firstJLabel, previousJLabel, countJLabel,
                nextJLabel, lastJLabel, Boolean.TRUE);
        selectedIndex = -1;
        installDataListener();
    }

    /**
     * Get the index of the cell in the list model.
     * 
     * @param cell
     *            A <code>Cell</code>. 
     * @return The index of the cell in the list model.
     */
    public int getIndexOf(final Cell cell) {
        return listModel.indexOf(cell);
    }

    /**
     * Get the list model.
     * 
     * @return The <code>DefaultListModel</code>. 
     */
    public DefaultListModel getListModel() {
        return listModel;
    }

    /**
     * Get the selected cell.
     * 
     * @return The selected <code>Cell</code>. 
     */
    public Cell getSelectedCell() {
        if (isSelectionEmpty()) {
            return null;
        } else {
            return (Cell)listModel.get(selectedIndex);
        }
    }

    /**
     * Get the selected index.
     * 
     * @return The selected index <code>int</code>. 
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Initialize the list model with a list of cells.
     * 
     * @param cells
     *            A List of <code>Cell</code>.  
     */
    public void initialize(final List<? extends Cell> cells) {
        int initialSelectedIndex;
        if (isSavedSelectedIndex()) {
            initialSelectedIndex = getSavedSelectedIndex();
        } else {
            initialSelectedIndex = DEFAULT_SELECTED_ROW_START;
        }
        // The selected index may not exist, for example, if the
        // draft is deleted and there are no versions.            
        if (initialSelectedIndex >= cells.size()) {
            initialSelectedIndex = 0;
        }
        listManager.initialize(cells);
        setSelectedIndex(initialSelectedIndex);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.PanelSelectionManager#isSelected(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell)
     */
    public Boolean isSelected(final Cell cell) {
        return (!isSelectionEmpty() && selectedIndex == getIndexOf(cell));
    }

    /**
     * Determine if the selection is empty.
     * 
     * @return true if the selection is empty.
     */
    public Boolean isSelectionEmpty() {
        return (selectedIndex < 0);
    }

    /**
     * Process a key stroke.
     * 
     * @param keyStroke
     *            A <code>KeyStroke</code>. 
     */
    public void processKeyStroke(final KeyStroke keyStroke) {
        listManager.processKeyStroke(keyStroke);
    }

    /**
     * Select a cell.
     * 
     * @param cell
     *            A <code>Cell</code>.  
     */
    public void setSelectedCell(final Cell cell) {
        final int page = listManager.getPageContainingCell(cell);
        if (0 <= page) {
            listManager.showPage(page);
        }
        setSelectedIndex(listModel.indexOf(cell));
    }

    /**
     * Select the cell by index.
     * NOTE All cell selection goes through this method.
     * 
     * @param selectedIndex
     *            The selected index <code>int</code>.  
     */
    public void setSelectedIndex(final int selectedIndex) {
        final int oldSelectedIndex = this.selectedIndex;
        this.selectedIndex = selectedIndex;
        if (oldSelectedIndex != selectedIndex && !isSelectionEmpty()) {
            tabPanel.panelCellSelectionChanged(getSelectedCell());
            saveSelectedIndex(selectedIndex);
        }
        selectPanel();
    }

    /**
     * Show the first page.
     */
    public void showFirstPage() {
        listManager.showPage(0);
    }

    /**
     * Get the saved selected index.
     * 
     * @return The saved selected index <code>Integer</code>. 
     */
    private Integer getSavedSelectedIndex() {
        final Integer selectedIndex =
            (Integer) tabPanel.getAttribute(MessageFormat.format(
                    SK_LIST_SELECTED_INDEX_PATTERN, tabPanel.getId(), listName));
        return selectedIndex;
    }

    /**
     * Install the data listener.
     */
    private void installDataListener() {
        listModel.addListDataListener(new ListDataListener() {
            /**
             * @see javax.swing.event.ListDataListener#contentsChanged(javax.swing.event.ListDataEvent)
             */
            public void contentsChanged(final ListDataEvent e) {
                setDefaultSelectedIndex();
            }
            /**
             * @see javax.swing.event.ListDataListener#intervalAdded(javax.swing.event.ListDataEvent)
             */
            public void intervalAdded(final ListDataEvent e) {
                setDefaultSelectedIndex();
            }
            /**
             * @see javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event.ListDataEvent)
             */
            public void intervalRemoved(final ListDataEvent e) {
                setSelectedIndex(-1);
            } 
        });
    }

    /**
     * Determine if there is a saved selected index.
     * 
     * @return true if there is a saved selected index.
     */
    private Boolean isSavedSelectedIndex() {
        return (null != getSavedSelectedIndex());
    }

    /**
     * Save the selected index.
     * 
     * @param selectedIndex
     *            The selected index <code>int</code>.  
     */
    private void saveSelectedIndex(final int selectedIndex) {
        tabPanel.setAttribute(MessageFormat.format(
                SK_LIST_SELECTED_INDEX_PATTERN, tabPanel.getId(), listName),
                Integer.valueOf(selectedIndex));
    }

    /**
     * Select the panel.
     */
    private void selectPanel() {
        // This is done so other panels will deselect when there is activity in
        // the expanded panel. Note also that the null check is because this method
        // may get called during initialization before the delegate is set up.
        final TabDelegate delegate = tabPanel.getTabDelegate();
        if (null != delegate) {
            delegate.selectPanel(tabPanel);
        }
    }

    /**
     * Set the default selected index (for paging).
     */
    private void setDefaultSelectedIndex() {
        int initialSelectedIndex = DEFAULT_SELECTED_ROW_PAGING;
        if (initialSelectedIndex >= listModel.size()) {
            initialSelectedIndex = 0;
        }
        setSelectedIndex(initialSelectedIndex);
    }
}
