/*
 * Created on September 1, 2006, 11:32 AM
 */
package com.thinkparity.browser.application.browser.display.avatar.tab;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.avatar.Resizer;
import com.thinkparity.browser.application.browser.display.provider.ContentProvider;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCellRenderer;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class TabAvatar<T extends TabModel> extends Avatar {

    /** The relative location of the "hot" zone in each cell. */
    private static final Point CELL_NODE_LOCATION = new Point(10, 2);

    /** The size of the "hot" zone in each cell. */
    private static final Dimension CELL_NODE_SIZE = new Dimension(20, 20);

    /** A menu item factory. */
    protected final PopupItemFactory menuItemFactory;
    
    /** The avatar's accompanying <code>TabModel</code>.*/
    protected final T model;

    /** The avatar id. */
    private final AvatarId id;
    
    /** Variables used to modify behavior of selection. */
    private Integer selectedIndex = -1;
    private Boolean selectingLastIndex = Boolean.FALSE;

    /** Create TabAvatar */
    protected TabAvatar(final AvatarId id, final T model) {
        super(id);
        this.id = id;
        this.menuItemFactory = PopupItemFactory.getInstance();
        this.model = model;
        setResizeEdges(Resizer.FormLocation.MIDDLE);
        initComponents();
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
     */
    @Override
    public final AvatarId getId() {
        return id;
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()
     */
    @Override
    public final State getState() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
     */
    @Override
    public void reload() {
        model.reload();
        if (tabJList.isSelectionEmpty()) {
            setSelectedIndex(0);
        }
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setContentProvider(com.thinkparity.browser.application.browser.display.provider.ContentProvider)
     */
    @Override
    public void setContentProvider(final ContentProvider contentProvider) {
        model.setContentProvider(contentProvider);
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setInput(java.lang.Object)
     */
    @Override
    public void setInput(final Object input) {
        model.setInput(input);
        super.setInput(input);
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
     */
    @Override
    public final void setState(final State state) {}

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
     * Set the header label transfer handler.
     * 
     * @param newHandler
     *            A <code>TransferHandler</code>.
     */
    protected void setHeaderTransferHandler(final TransferHandler newHandler) {
        headerJLabel.setTransferHandler(newHandler);
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
     * Set the selected tab cell.
     * 
     * @param tabCell
     *            A <code>TabCell</code>.
     */
    protected void setSelectedCell(final TabCell tabCell) {
        if (tabCell != getSelectedCell()) {
            // Set selectedIndex to -1 so the ListSelectionListener behaves correctly
            selectedIndex = -1;
            tabJList.setSelectedValue(tabCell, true);
        }
    }
    
    /**
     * Select an entry in the JList.
     * 
     * @param index
     *              The JList index to select.
     */
    private void setSelectedIndex(final Integer index) {
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
        gridBagConstraints.ipady = 15;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        add(headerJLabel, gridBagConstraints);

        tabJScrollPane.setBorder(null);
        tabJScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        tabJList.setModel(model.getListModel());
        tabJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabJList.setCellRenderer(new TabCellRenderer());
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

    private void tabJListMousePressed(java.awt.event.MouseEvent e) {//GEN-FIRST:event_tabJListMousePressed
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
    }//GEN-LAST:event_tabJListMousePressed

    private void tabJListValueChanged(javax.swing.event.ListSelectionEvent e) {//GEN-FIRST:event_tabJListValueChanged
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
    }//GEN-LAST:event_tabJListValueChanged

    /**
     * Determine if the mouse event occured on or below the cell in an x-y plane.
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

    private void tabJListMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_tabJListMouseClicked
        if (1 == e.getClickCount()) {
            // first; we grab the index of the list item of the event
            // second; we grab the bounds of the list item's icon
            // third; we check to see that the icon was clicked and if it was
            //      we trigger expand.           
            final Point p = e.getPoint();
            final Integer listIndex = tabJList.locationToIndex(p);
            final Integer selectedIndex = tabJList.getSelectedIndex();
            if (selectedIndex != -1 && listIndex == selectedIndex) {
                final TabCell tabCell = getSelectedCell();
                final Rectangle iconBounds = tabJList.getCellBounds(listIndex, listIndex);
                iconBounds.x += CELL_NODE_LOCATION.x * tabCell.getTextInsetFactor();
                iconBounds.y += CELL_NODE_LOCATION.y;
                iconBounds.width = CELL_NODE_SIZE.width;
                iconBounds.height = CELL_NODE_SIZE.height;
                if(SwingUtil.regionContains(iconBounds, p)) {                            
                    triggerExpand(tabCell);
                }
          }
        } else if (2 == e.getClickCount()) {
            if (isMouseEventWithinCell(e)) {  
                triggerDoubleClick(getSelectedCell());
            } else {
                triggerDoubleClick();
            }
        }
    }//GEN-LAST:event_tabJListMouseClicked

    private void tabJListMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_tabJListMouseReleased
        if(e.isPopupTrigger()) {
            // Desired behavior: if click on an entry in the list then trigger a popup for that entry.
            // If click in the blank area below the last entry in the list then trigger a popup that
            // allows the user to create a container.
            // If there are no containers then expect getSelectedIndex() to return -1.
            // If there are 1 or more containers and the user clicks below the final entry then expect
            // locationToIndex() to return the last entry.
            if (isMouseEventWithinCell(e)) {
                setSelectedIndex(tabJList.locationToIndex(e.getPoint()));
                triggerPopup(getSelectedCell(), e);
            } else {
                triggerPopup(tabJList, e);             
            }
        }
    }//GEN-LAST:event_tabJListMouseReleased

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected final javax.swing.JLabel headerJLabel = new javax.swing.JLabel();
    protected final javax.swing.JList tabJList = new javax.swing.JList();
    protected final javax.swing.JScrollPane tabJScrollPane = new javax.swing.JScrollPane();
    // End of variables declaration//GEN-END:variables

    public enum DataKey { SEARCH_EXPRESSION }
}
