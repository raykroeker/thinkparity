/*
 * Created On: October 6, 2006, 1:42 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer.ResizeEdges;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class TabPanelAvatar<T extends TabModel> extends TabAvatar<T> {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel columnHeaderJPanel;
    private javax.swing.JLabel eastPaddingJLabel;
    private javax.swing.JLabel fillJLabel;
    private javax.swing.JLabel headerJLabel;
    private javax.swing.JLabel sortJLabel;
    private javax.swing.JPanel tabJPanel;
    private javax.swing.JScrollPane tabJScrollPane;
    private javax.swing.JLabel westPaddingJLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * The set of <code>GridBagConstraints</code> used when adding a fill
     * component.
     */
    private final GridBagConstraints fillConstraints;

    /** The set of <code>GridBagConstraints</code> used when adding a panel. */
    private final GridBagConstraints panelConstraints;

    /**
     * A <code>TransferHandler</code> which dispatches all import export
     * requests to the model.
     */
    private final TransferHandler transferHandler;

    /**
     * Creates TabPanelAvatar.
     * 
     */
    public TabPanelAvatar(final AvatarId id, final T model) {
    	super(id, model);
        this.fillConstraints = new GridBagConstraints();
        this.fillConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.fillConstraints.weightx = 1.0F;
        this.fillConstraints.weighty = 1.0F;
        this.fillConstraints.gridx = 0;
        this.fillConstraints.gridy = GridBagConstraints.RELATIVE;
        this.panelConstraints = new GridBagConstraints();
        this.panelConstraints.fill = GridBagConstraints.BOTH;
        this.panelConstraints.gridx = 0;
        this.transferHandler = new TransferHandler() {
            @Override
            public boolean canImport(final JComponent comp,
                    final DataFlavor[] transferFlavors) {
                logger.logApiId();
                logger.logVariable("comp", comp);
                logger.logVariable("transferFlavors", transferFlavors);
                return model.canImportData((TabPanel) comp, transferFlavors);
            }
            @Override
            public boolean importData(final JComponent comp,
                    final Transferable transferable) {
                logger.logApiId();
                logger.logVariable("comp", comp);
                logger.logVariable("transferable", transferable);
                try {
                    model.importData((TabPanel) comp, transferable);
                    return true;
                } catch (final Throwable t) {
                    logger.logError(t, "Could not import data {0} for {1}.",
                            transferable, comp);
                    return false;
                }
            }
        };
    	installDataListener();
        initComponents();
        installResizer();
    }

    /**
     * @see com.thinkparity.codebase.swing.AbstractJPanel#debug()
     */
    @Override
    public void debug() {
        final Component[] components = tabJPanel.getComponents();
        logger.logDebug("{0} components.", components.length);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatar#reload()
     * 
     */
    @Override
	public void reload() {
    	super.reload();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getResizeEdges()
     */
    @Override
    protected ResizeEdges getResizeEdges() {
        return Resizer.ResizeEdges.MIDDLE;
    }

    /**
     * Trigger a popup for the tab avatar.
     *
     */
    protected void triggerPopup(final Component invoker, final MouseEvent e) {}

    /**
     * Trigger a sort.
     * 
     * @param sortColumn
     *          What the containers will be sorted by.
     * @param sortDirection
     *          The direction of the sort.
     */
    protected void triggerSort(final SortColumn sortColumn, final SortDirection sortDirection) {       
    }
    
    /**
     * Add the fill component.
     * 
     * @param listSize
     *            The <code>int</code> size of the list.
     */
    private void addFill(final int listSize) {
        tabJPanel.add(fillJLabel, fillConstraints, listSize);
    }
    
    /**
     * Add a panel from a model at an index.
     * 
     * @param index
     *            An <code>int</code> index.
     * @param panel
     *            A <code>TabPanel</code>.
     */
    private void addPanel(final int index, final TabPanel panel) {
        // setup a transfer handler for each panel added
        ((JComponent) panel).setTransferHandler(transferHandler);
        panelConstraints.gridy = index;
        tabJPanel.add((Component) panel, panelConstraints.clone(), index);
    }
    
    private void headerJLabelMousePressed(java.awt.event.MouseEvent e) {// GEN-FIRST:event_headerJLabelMousePressed
    }// GEN-LAST:event_headerJLabelMousePressed
    
    private void headerJLabelMouseReleased(java.awt.event.MouseEvent e) {// GEN-FIRST:event_headerJLabelMouseReleased
        if (e.isPopupTrigger()) {
            triggerPopup(tabJPanel, e);
        }
    }// GEN-LAST:event_headerJLabelMouseReleased

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        headerJLabel = new javax.swing.JLabel();
        columnHeaderJPanel = new javax.swing.JPanel();
        westPaddingJLabel = new javax.swing.JLabel();
        sortJLabel = new javax.swing.JLabel();
        eastPaddingJLabel = new javax.swing.JLabel();
        tabJScrollPane = new javax.swing.JScrollPane();
        tabJPanel = new javax.swing.JPanel();
        fillJLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        headerJLabel.setMaximumSize(new java.awt.Dimension(3, 3));
        headerJLabel.setMinimumSize(new java.awt.Dimension(3, 3));
        headerJLabel.setPreferredSize(new java.awt.Dimension(3, 3));
        headerJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                headerJLabelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                headerJLabelMouseReleased(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        add(headerJLabel, gridBagConstraints);

        columnHeaderJPanel.setLayout(new java.awt.GridBagLayout());

        columnHeaderJPanel.setMaximumSize(new java.awt.Dimension(5000, 19));
        columnHeaderJPanel.setMinimumSize(new java.awt.Dimension(128, 19));
        columnHeaderJPanel.setOpaque(false);
        columnHeaderJPanel.setPreferredSize(new java.awt.Dimension(128, 19));
        westPaddingJLabel.setFocusable(false);
        westPaddingJLabel.setMaximumSize(new java.awt.Dimension(4, 20));
        westPaddingJLabel.setMinimumSize(new java.awt.Dimension(4, 20));
        westPaddingJLabel.setPreferredSize(new java.awt.Dimension(4, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        columnHeaderJPanel.add(westPaddingJLabel, gridBagConstraints);

        sortJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/SortButton.png")));
        sortJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                sortJLabelMousePressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        columnHeaderJPanel.add(sortJLabel, gridBagConstraints);

        eastPaddingJLabel.setFocusable(false);
        eastPaddingJLabel.setMaximumSize(new java.awt.Dimension(1, 20));
        eastPaddingJLabel.setMinimumSize(new java.awt.Dimension(1, 20));
        eastPaddingJLabel.setPreferredSize(new java.awt.Dimension(1, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        columnHeaderJPanel.add(eastPaddingJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        add(columnHeaderJPanel, gridBagConstraints);

        tabJScrollPane.setBorder(null);
        tabJScrollPane.setPreferredSize(new java.awt.Dimension(256, 128));
        tabJPanel.setLayout(new java.awt.GridBagLayout());

        tabJPanel.setBackground(new java.awt.Color(255, 255, 255));
        tabJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tabJPanelMouseReleased(evt);
            }
        });

        tabJPanel.add(fillJLabel, new java.awt.GridBagConstraints());

        tabJScrollPane.setViewportView(tabJPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(tabJScrollPane, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    /**
     * Install the a data listener on the list model. This will translate the
     * model's data events into UI component events.
     * 
     */
    private void installDataListener() {
        final DefaultListModel listModel = model.getListModel();
        listModel.addListDataListener(new ListDataListener() {
            /**
             * @see javax.swing.event.ListDataListener#contentsChanged(javax.swing.event.ListDataEvent)
             * 
             */
            public void contentsChanged(final ListDataEvent e) {
                debug();
                for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
                    removePanel(i);
                    addPanel(i, (TabPanel) listModel.get(i));
                }
                reloadPanels();
            }
            /**
             * @see javax.swing.event.ListDataListener#intervalAdded(javax.swing.event.ListDataEvent)
             * 
             */
            public void intervalAdded(final ListDataEvent e) {
                debug();
                removeFill();
                int startIndex = e.getIndex0();
                if (startIndex > 0) {
                    startIndex--;
                }
                for (int i = startIndex; i < listModel.size(); i++) {
                    addPanel(i, (TabPanel) listModel.get(i));
                }
                addFill(listModel.size());
                reloadPanels();
            }
            /**
             * @see javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event.ListDataEvent)
             * 
             */
            public void intervalRemoved(final ListDataEvent e) {
                debug();
                removeFill();
                int startIndex = e.getIndex0();
                if (startIndex > 0) {
                    startIndex--;
                }
                removePanel(e.getIndex0());
                for (int i = startIndex; i < listModel.size(); i++) {
                    addPanel(i, (TabPanel) listModel.get(i));
                }
                addFill(listModel.size());
                reloadPanels();
            } 
        });
    }

    /**
     * Reload the panels display. The root JPanel is revalidated and repainted
     * as well as the scroll pane and this avatar.
     * 
     */
    private void reloadPanels() {
        tabJPanel.revalidate();
        tabJPanel.repaint();
        tabJScrollPane.revalidate();
        tabJScrollPane.repaint();
        validate();
        repaint();
    }
    /**
     * Remove the fill component.
     *
     */
    private void removeFill() {
        tabJPanel.remove(fillJLabel);
    }
    /**
     * Remove a panel at an index.
     * 
     * @param index
     *            An <code>int</code> index.
     */
    private void removePanel(final int index) {
        tabJPanel.remove(index);
    }
    private void sortJLabelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_sortJLabelMousePressed
        Point location = sortJLabel.getLocation();
        location.y += sortJLabel.getHeight();
        new SortPopup().show(this, location);
    }//GEN-LAST:event_sortJLabelMousePressed
    private void tabJPanelMouseReleased(java.awt.event.MouseEvent e) {// GEN-FIRST:event_tabJPanelMouseReleased
        if (e.isPopupTrigger()) {
            triggerPopup(tabJPanel, e);
        }
    }// GEN-LAST:event_tabJPanelMouseReleased
    public enum SortColumn { BOOKMARK, CONTAINER_DATE, CONTAINER_NAME, DRAFT_OWNER, NONE }
    public enum SortDirection { DOWN, NONE, UP }
    private class SortPopup {
        
        /** A <code>PopupItemFactory</code>. */
        private final PopupItemFactory popupItemFactory;
        
        /**
         * Create SortPopup.
         */
        SortPopup() {
            super();
            this.popupItemFactory = PopupItemFactory.getInstance();
        }
        
        /**
         * Show a sort popup menu on an invoker at the specified location.
         * 
         * @param invoker
         *            An invoker <code>Component</code>.
         * @param location
         *            A location <code>Point</code>.
         */
        void show(final Component invoker, final Point location) {
            final JPopupMenu jPopupMenu = MenuFactory.createPopup();
            
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.PLATFORM_BROWSER_OPEN_HELP, Data.emptyData()));
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.PLATFORM_BROWSER_DISPLAY_INFO, Data.emptyData()));
            
            jPopupMenu.show(invoker, (int)location.getX()+3, (int)location.getY()+4);
        }
    }
}
