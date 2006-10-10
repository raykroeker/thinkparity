/*
 * Created on September 1, 2006, 11:32 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.TransferHandler;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer.ResizeEdges;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCellRenderer;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class TabListAvatar<T extends TabListModel> extends TabAvatar<T> {

    /** Variables used to modify behavior of selection. */
    private Integer selectedIndex = -1;

    private Boolean selectingLastIndex = Boolean.FALSE;
    
    /** Create TabListAvatar */
    protected TabListAvatar(final AvatarId id, final T model) {
        super(id, model);
        initComponents();
        installResizer();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        super.reload();
        if (tabJList.isSelectionEmpty()) {
            setSelectedIndex(0);
        }
    }

    /**
     * Obtain the cell at a given index.
     * 
     * @param index
     *            An index <code>Integer</code>.
     * @return A <code>TabCell</code>.
     */
    protected final TabCell getCell(final Integer index) {
        return (TabCell) model.getListModel().elementAt(index);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getResizeEdges()
     */
    @Override
    protected ResizeEdges getResizeEdges() {
        return Resizer.ResizeEdges.MIDDLE;
    }

    /**
     * Obtain the selected tab cell.
     * 
     * @return A <code>TabCell</code>.
     */
    protected TabCell getSelectedCell() {
        return (TabCell) tabJList.getSelectedValue();
    }
        
    /**
     * Obtain the selected cell index.
     * 
     * @return A list index.
     */
    protected int getSelectedIndex() {
        return tabJList.getSelectedIndex();
    }                                     

    /**
     * Install a mouse over tracker which tracks the mouse and records the index
     * above which it is positioned. It also updates the underlying cell's
     * "mouseOver" property.
     * 
     */
    protected final void installMouseOverTracker() {
        final MouseInputListener mouseOverListener = new MouseInputAdapter() {
            private int mouseOverIndex = -1;
            @Override
            public void mouseEntered(final MouseEvent e) {
                mouseMoved(e);
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                if (!isResizeDragging()) {
                    if (-1 != mouseOverIndex) {
                        updateCellMouseOver(mouseOverIndex, Boolean.FALSE);
                        mouseOverIndex = -1;
                    }
                }
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!isResizeDragging()) {
                    final int mouseOverIndex = getMouseOverIndex(e);
                    if (mouseOverIndex != this.mouseOverIndex) {
                        if (-1 != this.mouseOverIndex) {
                            updateCellMouseOver(this.mouseOverIndex, Boolean.FALSE);
                        }
                        if (-1 != mouseOverIndex) {
                            updateCellMouseOver(mouseOverIndex, Boolean.TRUE);
                        }
                        this.mouseOverIndex = mouseOverIndex;
                    }
                }
            }
        };
        tabJList.addMouseListener(mouseOverListener);
        tabJList.addMouseMotionListener(mouseOverListener);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#isSupportMouseMove()
     */
    @Override
    protected Boolean isSupportMouseMove() {
        return Boolean.FALSE;
    }

    /**
     * Restore the saved selection.
     *
     */
    protected void restoreSelection() {
        final Integer selection =
            (Integer) getClientProperty(ClientProperty.SAVE_SELECTION);
        tabJList.setSelectedIndex(selection);
    }

    /**
     * Save the current selection cell.
     *
     */
    protected void saveSelection() {
        putClientProperty(ClientProperty.SAVE_SELECTION, getSelectedIndex());
    }

    /**
     * Set the header label transfer handler.
     * 
     * @param newHandler
     *            A <code>TransferHandler</code>.
     */
    protected void setHeaderTransferHandler(final TransferHandler newHandler) {
        headerJLabel.setTransferHandler(newHandler);
    }

    /**
     * Set the list transfer handler.
     * 
     * @param newHandler
     *            A <code>TransferHandler</code>.
     */
    protected void setListTransferHandler(final TransferHandler newHandler) {
        tabJList.setTransferHandler(newHandler);
    }

    /**
     * Set the selected tab cell.
     * 
     * @param tabCell
     *            A <code>TabCell</code>.
     * @return Success or failure.
     */
    protected Boolean setSelectedCell(final TabCell tabCell) {
        if (tabCell != getSelectedCell()) {
            // Set selectedIndex to -1 so the ListSelectionListener behaves correctly
            selectedIndex = -1;
            tabJList.setSelectedValue(tabCell, true);
            
            if (getSelectedCell() != tabCell) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     * Select an entry in the JList.
     * 
     * @param index
     *              The JList index to select.
     */
    protected void setSelectedIndex(final Integer index) {
        if (index != getSelectedIndex()) {
            // Set selectedIndex to -1 so the ListSelectionListener behaves correctly        
            selectedIndex = -1;
            tabJList.setSelectedIndex(index);
        }
    }
    
    /**
     * Trigger a double click event on the tab.
     *
     */
    protected void triggerDoubleClick() {}
    
    /**
     * Trigger a popup for the tab avatar.
     *
     */
    protected void triggerPopup(final Component invoker, final MouseEvent e) {}

    /**
     * Get the index of the cell the event is over.
     * 
     * @param e
     *          Mouse event.
     * @return  index of the cell the mouse is over, or -1.
     */
    private Integer getMouseOverIndex(java.awt.event.MouseEvent e) {
        Integer tabCellIndex = tabJList.locationToIndex(e.getPoint());
        
        // Handle the case that the mouse is below the last entry
        if ((tabCellIndex != -1) && (tabCellIndex == tabJList.getModel().getSize() - 1)) {
            final Rectangle tabCellBounds = tabJList.getCellBounds(tabCellIndex, tabCellIndex);
            if (!SwingUtil.regionContains(tabCellBounds, e.getPoint())) {
                tabCellIndex = -1;
            }
        }
        
        return tabCellIndex;
    }

    private void headerJLabelMousePressed(java.awt.event.MouseEvent e) {//GEN-FIRST:event_headerJLabelMousePressed
    }//GEN-LAST:event_headerJLabelMousePressed

    private void headerJLabelMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_headerJLabelMouseReleased
        if (e.isPopupTrigger()) {
            triggerPopup(tabJList, e);
        }
    }//GEN-LAST:event_headerJLabelMouseReleased

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        setLayout(new java.awt.GridBagLayout());

        headerJLabel.setMaximumSize(new java.awt.Dimension(3, 14));
        headerJLabel.setMinimumSize(new java.awt.Dimension(3, 14));
        headerJLabel.setPreferredSize(new java.awt.Dimension(3, 14));
        headerJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                headerJLabelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                headerJLabelMouseReleased(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        add(headerJLabel, gridBagConstraints);

        tabJScrollPane.setBorder(null);
        tabJScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        tabJScrollPane.setMinimumSize(new java.awt.Dimension(23, 24));
        tabJList.setModel(model.getListModel());
        tabJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabJList.setCellRenderer(new TabCellRenderer());
        tabJList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabJListKeyPressed(evt);
            }
        });
        tabJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                tabJListValueChanged(evt);
            }
        });
        tabJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabJListMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabJListMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabJListMouseReleased(evt);
            }
        });

        tabJScrollPane.setViewportView(tabJList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        add(tabJScrollPane, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Determine if the mouse event occurred on or below the cell in an x-y plane.
     * 
     * @return True if the mouse event occurred in the cell.
     */
    private boolean isMouseEventWithinCell(final java.awt.event.MouseEvent e) {
        final Integer tabCellIndex = tabJList.locationToIndex(e.getPoint());
        if (tabCellIndex == -1) {  // Will be -1 if the model is empty
            return false;
        } else {
            final Rectangle tabCellBounds = tabJList.getCellBounds(tabCellIndex, tabCellIndex);
            return SwingUtil.regionContains(tabCellBounds, e.getPoint());
        }
    }
    
    private void tabJListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabJListKeyPressed
        if ((evt.getKeyCode()==KeyEvent.VK_DOWN) ||
            (evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN) ||
            (evt.getKeyCode()==KeyEvent.VK_END)) {
            if (tabJList.getSelectedIndex() != tabJList.getModel().getSize()-1) {
                // Set selectedIndex to -1 so the ListSelectionListener behaves correctly
                // ie. allow the selection to change.
                selectedIndex = -1;
            }
        }
    }//GEN-LAST:event_tabJListKeyPressed

    private void tabJListMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_tabJListMouseClicked
        // Interesting fact aobut getClickCount() is that the count continues to 3, 4 and beyond if
        // the user keeps clicking with less than (say) 1/2 second delay between clicks.
        if (!MenuFactory.isPopupMenu()) {
            if (e.getButton()==MouseEvent.BUTTON1) {
                final Boolean isInCell = isMouseEventWithinCell(e);
                // Every click (regardless of getClickCount() triggers
                // expand / collapse if the node supports it.
                if (isInCell) { 
                    triggerExpand(getSelectedCell());
                }
                // A click count of 2, 4, 6, etc. triggers double click event
                if (e.getClickCount() % 2 == 0) {
                    if (isInCell) {  
                        triggerDoubleClick(getSelectedCell());
                    } else {
                        triggerDoubleClick();
                    }
                }
            }
        }
    }//GEN-LAST:event_tabJListMouseClicked

    private void tabJListMousePressed(java.awt.event.MouseEvent e) {                                             
        // If selectingLastIndex is true then the selection was interrupted. Don't
        // perform the selection if the user clicked below the last entry, otherwise
        // proceed with the selection.
        if (selectingLastIndex) {
            if (isMouseEventWithinCell(e)) { 
                selectedIndex = tabJList.locationToIndex(e.getPoint());                        
                tabJList.setSelectedIndex(selectedIndex);
            }
            selectingLastIndex = Boolean.FALSE; 
        }
    }

    private void tabJListMouseReleased(java.awt.event.MouseEvent e) {                                       
        if (e.isPopupTrigger()) {
            // Desired behavior: if click on an entry in the list then trigger a popup for that cell.
            // If click in the blank area below the last entry in the list then trigger a popup that
            // is related to the tab rather than a cell in the tab.
            if (isMouseEventWithinCell(e)) {
                setSelectedIndex(tabJList.locationToIndex(e.getPoint()));
                triggerPopup(getSelectedCell(), e);
            } else {
                triggerPopup(tabJList, e);             
            }
        }
    }                                          

    private void tabJListValueChanged(javax.swing.event.ListSelectionEvent e) {                                      
        final Integer newSelectedIndex = tabJList.getSelectedIndex();
        final Integer lastIndex = tabJList.getModel().getSize() - 1;
        
        // The first time here, or if the current selection is the last item
        // in the list, or if the new selection is not the last item in
        // the list, then proceed as usual.
        if ((selectedIndex == -1) || (selectedIndex == lastIndex)
                || (newSelectedIndex != lastIndex)) {
            selectedIndex = newSelectedIndex;
            selectingLastIndex = Boolean.FALSE;                    
        }
        // If the last item is being selected then hold off until we can determine
        // that the user has clicked on the cell and not below the cell.
        else {
            tabJList.setSelectedIndex(selectedIndex);
            selectingLastIndex = Boolean.TRUE;   
        }
    }                                       

    /**
     * Trigger a double click event on the tab cell.
     * 
     * @param tabCell
     *            A <code>TabCell</code>.
     */
    private void triggerDoubleClick(final TabCell tabCell) {
        model.triggerDoubleClick(tabCell);
        setSelectedCell(tabCell);
    }

    /**
     * Trigger an expand for the tab cell.
     * 
     * @param tabCell
     *            A <code>TabCell</code>.
     */
    private void triggerExpand(final TabCell tabCell) {
        model.triggerExpand(tabCell);
        setSelectedCell(tabCell);
    }
    /**
     * Trigger a popup menu for a tab cell.
     *
     * @param tabCell A <code>TabCell</code>.
     */
    private void triggerPopup(final TabCell tabCell, final MouseEvent e) {
        model.triggerPopup(tabCell, tabJList, e);
        setSelectedCell(tabCell);
    }
    /**
     * Update the cell that the mouse is over with the mouseOver flag and
     * cause the cell to redraw.
     * 
     * @param index
     *              Index of the cell.
     * @param mouseOver
     *              Mouse over flag.
     */
    private void updateCellMouseOver(final Integer index, final Boolean mouseOver) {
        saveSelection();
        if (index < tabJList.getModel().getSize()) {
            final TabCell cell = (TabCell) tabJList.getModel().getElementAt(index);
            cell.setMouseOver(mouseOver);
            model.getListModel().removeElementAt(index);
            model.getListModel().add(index, cell);
            restoreSelection();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected final javax.swing.JLabel headerJLabel = new javax.swing.JLabel();
    protected final javax.swing.JList tabJList = new javax.swing.JList();
    protected final javax.swing.JScrollPane tabJScrollPane = new javax.swing.JScrollPane();
    // End of variables declaration//GEN-END:variables

    public enum DataKey { SEARCH_EXPRESSION }

    private enum ClientProperty { SAVE_SELECTION }
}
