/*
 * Created On: October 6, 2006, 1:42 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.border.Border;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.thinkparity.codebase.swing.border.BottomBorder;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer.ResizeEdges;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;

/**
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class TabPanelAvatar<T extends TabModel> extends TabAvatar<T> {

    /** The filler constraints. */
    private final GridBagConstraints fillConstraints;

    /** The panel constraints. */
    private final GridBagConstraints panelConstraints;
    
    /** The sort column. */
    private SortColumn sortColumn = SortColumn.NONE;
    
    /** The sort direction. */
    private SortDirection sortDirection = SortDirection.NONE;
    
    /** An image cache. */
    private final MainPanelImageCache imageCache;
    
    /** The panel localization. */
    private final MainCellL18n localization;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel columnHeaderJPanel;
    private javax.swing.JLabel containerDateFillerJLabel;
    private javax.swing.JLabel containerDateJLabel;
    private javax.swing.JPanel containerDateJPanel;
    private javax.swing.JPanel containerJPanel;
    private javax.swing.JLabel containerNameFillerJLabel;
    private javax.swing.JLabel containerNameJLabel;
    private javax.swing.JPanel containerNameJPanel;
    private javax.swing.JLabel draftOwnerFillerJLabel;
    private javax.swing.JLabel draftOwnerJLabel;
    private javax.swing.JPanel draftOwnerJPanel;
    private javax.swing.JLabel eastPaddingJLabel;
    private javax.swing.JLabel fillJLabel;
    private javax.swing.JLabel fillerJLabel;
    private javax.swing.JLabel headerJLabel;
    private javax.swing.JLabel iconJLabel;
    private javax.swing.JPanel tabJPanel;
    private javax.swing.JScrollPane tabJScrollPane;
    private javax.swing.JLabel westPaddingJLabel;
    // End of variables declaration//GEN-END:variables
    
    /** Creates new form TabPanelAvatar */
    public TabPanelAvatar(final AvatarId id, final T model) {
    	super(id, model);
        this.imageCache = new MainPanelImageCache();
        this.localization = new MainCellL18n("TabPanelAvatar");
        this.fillConstraints = new GridBagConstraints();
        this.fillConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.fillConstraints.weightx = 1.0F;
        this.fillConstraints.weighty = 1.0F;
        this.fillConstraints.gridx = 0;
        this.fillConstraints.gridy = GridBagConstraints.RELATIVE;
        this.panelConstraints = new GridBagConstraints();
        this.panelConstraints.fill = GridBagConstraints.BOTH;
        this.panelConstraints.gridx = 0;
    	installDataListener();
        initComponents();
        installResizer();
        initHeaderTooltips();
    }

    /**
     * @see com.thinkparity.codebase.swing.AbstractJPanel#debug()
     */
    @Override
    public void debug() {
        final Component[] components = tabJPanel.getComponents();
        logger.logDebug("{0} components.", components.length);
        for (final Component component : components) {
            logger.logVariable("component", component);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#getResizeEdges()
     */
    @Override
    protected ResizeEdges getResizeEdges() {
        return Resizer.ResizeEdges.MIDDLE;
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
        iconJLabel = new HeaderJLabel(SortColumn.BOOKMARK);
        containerJPanel = new javax.swing.JPanel();
        containerNameJPanel = new javax.swing.JPanel();
        containerNameJLabel = new HeaderJLabel(SortColumn.CONTAINER_NAME);
        containerNameFillerJLabel = new javax.swing.JLabel();
        fillerJLabel = new javax.swing.JLabel();
        containerDateJPanel = new javax.swing.JPanel();
        containerDateJLabel = new HeaderJLabel(SortColumn.CONTAINER_DATE);
        containerDateFillerJLabel = new javax.swing.JLabel();
        draftOwnerJPanel = new javax.swing.JPanel();
        draftOwnerJLabel = new HeaderJLabel(SortColumn.DRAFT_OWNER);
        draftOwnerFillerJLabel = new javax.swing.JLabel();
        eastPaddingJLabel = new javax.swing.JLabel();
        tabJScrollPane = new javax.swing.JScrollPane();
        tabJPanel = new javax.swing.JPanel();
        fillJLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        headerJLabel.setMaximumSize(new java.awt.Dimension(3, 1));
        headerJLabel.setMinimumSize(new java.awt.Dimension(3, 1));
        headerJLabel.setPreferredSize(new java.awt.Dimension(3, 1));
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
        gridBagConstraints.ipady = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        add(headerJLabel, gridBagConstraints);

        columnHeaderJPanel.setLayout(new java.awt.GridBagLayout());

        columnHeaderJPanel.setMaximumSize(new java.awt.Dimension(5000, 20));
        columnHeaderJPanel.setMinimumSize(new java.awt.Dimension(128, 20));
        columnHeaderJPanel.setOpaque(false);
        columnHeaderJPanel.setPreferredSize(new java.awt.Dimension(128, 20));
        westPaddingJLabel.setFocusable(false);
        westPaddingJLabel.setMaximumSize(new java.awt.Dimension(4, 20));
        westPaddingJLabel.setMinimumSize(new java.awt.Dimension(4, 20));
        westPaddingJLabel.setPreferredSize(new java.awt.Dimension(4, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        columnHeaderJPanel.add(westPaddingJLabel, gridBagConstraints);

        iconJLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        iconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/SortNone.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 10);
        columnHeaderJPanel.add(iconJLabel, gridBagConstraints);

        containerJPanel.setLayout(new java.awt.GridLayout(1, 0));

        containerJPanel.setMaximumSize(new java.awt.Dimension(32767, 20));
        containerJPanel.setMinimumSize(new java.awt.Dimension(200, 20));
        containerJPanel.setOpaque(false);
        containerJPanel.setPreferredSize(new java.awt.Dimension(200, 20));
        containerNameJPanel.setLayout(new java.awt.GridBagLayout());

        containerNameJPanel.setOpaque(false);
        containerNameJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/SortNone.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        containerNameJPanel.add(containerNameJLabel, gridBagConstraints);

        containerNameFillerJLabel.setMaximumSize(new java.awt.Dimension(12, 12));
        containerNameFillerJLabel.setMinimumSize(new java.awt.Dimension(12, 12));
        containerNameFillerJLabel.setPreferredSize(new java.awt.Dimension(12, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        containerNameJPanel.add(containerNameFillerJLabel, gridBagConstraints);

        containerJPanel.add(containerNameJPanel);

        containerJPanel.add(fillerJLabel);

        containerDateJPanel.setLayout(new java.awt.GridBagLayout());

        containerDateJPanel.setOpaque(false);
        containerDateJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/SortNone.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 0);
        containerDateJPanel.add(containerDateJLabel, gridBagConstraints);

        containerDateFillerJLabel.setMaximumSize(new java.awt.Dimension(12, 12));
        containerDateFillerJLabel.setMinimumSize(new java.awt.Dimension(12, 12));
        containerDateFillerJLabel.setPreferredSize(new java.awt.Dimension(12, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        containerDateJPanel.add(containerDateFillerJLabel, gridBagConstraints);

        containerJPanel.add(containerDateJPanel);

        draftOwnerJPanel.setLayout(new java.awt.GridBagLayout());

        draftOwnerJPanel.setOpaque(false);
        draftOwnerJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/SortNone.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        draftOwnerJPanel.add(draftOwnerJLabel, gridBagConstraints);

        draftOwnerFillerJLabel.setMaximumSize(new java.awt.Dimension(12, 12));
        draftOwnerFillerJLabel.setMinimumSize(new java.awt.Dimension(12, 12));
        draftOwnerFillerJLabel.setPreferredSize(new java.awt.Dimension(12, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        draftOwnerJPanel.add(draftOwnerFillerJLabel, gridBagConstraints);

        containerJPanel.add(draftOwnerJPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        columnHeaderJPanel.add(containerJPanel, gridBagConstraints);

        eastPaddingJLabel.setFocusable(false);
        eastPaddingJLabel.setMaximumSize(new java.awt.Dimension(1, 20));
        eastPaddingJLabel.setMinimumSize(new java.awt.Dimension(1, 20));
        eastPaddingJLabel.setPreferredSize(new java.awt.Dimension(1, 20));
        columnHeaderJPanel.add(eastPaddingJLabel, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        add(tabJScrollPane, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Initialize header tooltips.
     */
    private void initHeaderTooltips() {
        iconJLabel.setToolTipText(localization.getString("BookmarkToolTipText"));
        containerNameJLabel.setToolTipText(localization.getString("ContainerNameToolTipText"));
        containerDateJLabel.setToolTipText(localization.getString("ContainerDateToolTipText"));
        draftOwnerJLabel.setToolTipText(localization.getString("DraftOwnerToolTipText"));
    }
    
    /**
     * Install the a data listener on the list model. This will translate the
     * model's data events into UI component events.
     * 
     */
    private void installDataListener() {
        final DefaultListModel listModel = model.getListModel();
        listModel.addListDataListener(new ListDataListener() {
            public void contentsChanged(final ListDataEvent e) {
                debug();

                // update from index 0 to index 1
                for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
                    panelConstraints.gridy = i;
                    tabJPanel.remove(i);
                    final DefaultTabPanel panel = (DefaultTabPanel) listModel.get(i);
                    panel.setPreferredSize(panel.getPreferredSize(i==0, i==(listModel.size()-1)));
                    panel.prepareForRepaint();
                    panel.setBackground(panel.getBackgroundColor());
                    panel.setBorder(panel.getBorder(i==0, i==(listModel.size()-1)));
                    tabJPanel.add(panel, panelConstraints.clone(), i);
                }
                
                tabJPanel.revalidate();
                tabJPanel.repaint();
                tabJScrollPane.revalidate();
                tabJScrollPane.repaint();
                validate();
                
                for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
                    final DefaultTabPanel panel = (DefaultTabPanel) listModel.get(i);
                    panel.prepareForRepaintAfterValidate();
                }
                
                repaint();
                debug();
            }
            
            public void intervalAdded(final ListDataEvent e) {
                debug();

                tabJPanel.remove(fillJLabel);
                
                // Adding an interval can cause the border on the interval before it to change,
                // so adjust the start index to one before the interval added.
                int startIndex = e.getIndex0();
                if (startIndex > 0) {
                    startIndex--;
                }

                // refresh the element at index 0 and the elements after index 0
                for (int i = startIndex; i < listModel.size(); i++) {
                    panelConstraints.gridy = i;
                    final DefaultTabPanel panel = (DefaultTabPanel) listModel.get(i);
                    panel.setPreferredSize(panel.getPreferredSize(i==0, i==(listModel.size()-1)));
                    panel.prepareForRepaint();
                    panel.setBackground(panel.getBackgroundColor());
                    panel.setBorder(panel.getBorder(i==0, i==(listModel.size()-1)));
                    tabJPanel.add(panel, panelConstraints.clone(), i);
                }

                tabJPanel.add(fillJLabel, fillConstraints, listModel.size());
 
                tabJPanel.revalidate();
                tabJPanel.repaint();
                tabJScrollPane.revalidate();
                tabJScrollPane.repaint();
                
                // Use validate() so everything is laid out immediately.
                validate();
                
                // Loop over elements one more time. Some things can only be done
                // after everything is laid out, for example, set the split pane
                // divider location.
                for (int i = startIndex; i < listModel.size(); i++) {
                    final DefaultTabPanel panel = (DefaultTabPanel) listModel.get(i);
                    panel.prepareForRepaintAfterValidate();
                }
                
                repaint();
                debug();
            }
            
            public void intervalRemoved(final ListDataEvent e) {
                debug();

                tabJPanel.remove(fillJLabel);
                
                // Removing an interval can cause the border on the interval before it to change,
                // so adjust the start index to one before the interval added.
                int startIndex = e.getIndex0();
                if (startIndex > 0) {
                    startIndex--;
                }

                // remove index 0
                tabJPanel.remove(e.getIndex0());

                // refresh from index 0 forward
                for (int i = startIndex; i < listModel.size(); i++) {
                    panelConstraints.gridy = i;
                    final DefaultTabPanel panel = (DefaultTabPanel) listModel.get(i);
                    panel.setPreferredSize(panel.getPreferredSize(i==0, i==(listModel.size()-1)));
                    panel.prepareForRepaint();
                    panel.setBackground(panel.getBackgroundColor());
                    panel.setBorder(panel.getBorder(i==0, i==(listModel.size()-1)));
                    tabJPanel.add(panel, panelConstraints.clone(), i);
                }

                tabJPanel.add(fillJLabel, fillConstraints, listModel.size());

                tabJPanel.revalidate();
                tabJPanel.repaint();
                tabJScrollPane.revalidate();
                tabJScrollPane.repaint();
                validate();
                
                for (int i = startIndex; i < listModel.size(); i++) {
                    final DefaultTabPanel panel = (DefaultTabPanel) listModel.get(i);
                    panel.prepareForRepaintAfterValidate();
                }
                
                repaint();
                debug();
            } 
        });
    }
    
    /**
	 * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
	 */
    @Override
	public void reload() {
    	super.reload();
    }
    
    private void tabJPanelMouseReleased(java.awt.event.MouseEvent e) {// GEN-FIRST:event_tabJPanelMouseReleased
        if (e.isPopupTrigger()) {
            triggerPopup(tabJPanel, e);
        }
    }// GEN-LAST:event_tabJPanelMouseReleased
    
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
    
    private void updateHeaderIcon(final javax.swing.JLabel jLabel, final SortColumn column, final Boolean rollover) {
        if (rollover) {
            if ((sortColumn != column) || (sortDirection == SortDirection.NONE)) {
                jLabel.setIcon(imageCache.read(TabPanelIcon.SORT_NONE_ROLLOVER));
            } else if (sortDirection == SortDirection.DOWN) {
                jLabel.setIcon(imageCache.read(TabPanelIcon.SORT_DOWN_ROLLOVER));
            } else {
                jLabel.setIcon(imageCache.read(TabPanelIcon.SORT_UP_ROLLOVER));
            } 
        } else {
            if ((sortColumn != column) || (sortDirection == SortDirection.NONE)) {
                jLabel.setIcon(imageCache.read(TabPanelIcon.SORT_NONE));
            } else if (sortDirection == SortDirection.DOWN) {
                jLabel.setIcon(imageCache.read(TabPanelIcon.SORT_DOWN));
            } else {
                jLabel.setIcon(imageCache.read(TabPanelIcon.SORT_UP));
            } 
        }
    }
    
    private void updateHeaderIcons(final SortColumn rolloverColumn) {
        updateHeaderIcon(iconJLabel, SortColumn.BOOKMARK, (rolloverColumn==SortColumn.BOOKMARK));
        updateHeaderIcon(containerNameJLabel, SortColumn.CONTAINER_NAME, (rolloverColumn==SortColumn.CONTAINER_NAME));
        updateHeaderIcon(containerDateJLabel, SortColumn.CONTAINER_DATE, (rolloverColumn==SortColumn.CONTAINER_DATE));
        updateHeaderIcon(draftOwnerJLabel, SortColumn.DRAFT_OWNER, (rolloverColumn==SortColumn.DRAFT_OWNER));
    }
    
    private class HeaderJLabel extends javax.swing.JLabel {
        
        public HeaderJLabel(final SortColumn column) {
            super();
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (sortColumn != column) {
                        sortDirection = SortDirection.NONE; 
                    }
                    sortDirection = nextSortDirection(sortDirection, column);
                    sortColumn = column;
                    updateHeaderIcons(column);
                    triggerSort(sortColumn, sortDirection);
                }
                @Override
                public void mouseEntered(final MouseEvent e) {
                    updateHeaderIcon(HeaderJLabel.this, column, Boolean.TRUE);
                }
                @Override
                public void mouseExited(final MouseEvent e) {
                    updateHeaderIcon(HeaderJLabel.this, column, Boolean.FALSE);
                }
            });
        }
        
        private SortDirection nextSortDirection(final SortDirection sortDirection, final SortColumn column) {
            // Text based columns will start with sorting up, others will start with sorting down.
            if ((column == SortColumn.CONTAINER_NAME) || (column == SortColumn.DRAFT_OWNER)) {
                if (sortDirection == SortDirection.NONE) {
                    return SortDirection.UP;
                } else if (sortDirection == SortDirection.UP)  {
                    return SortDirection.DOWN;
                } else {
                    return SortDirection.NONE;
                }
            } else {
                if (sortDirection == SortDirection.NONE) {
                    return SortDirection.DOWN;
                } else if (sortDirection == SortDirection.DOWN)  {
                    return SortDirection.UP;
                } else {
                    return SortDirection.NONE;
                }
            }
        } 
    }
    
    public enum SortColumn { BOOKMARK, CONTAINER_NAME, CONTAINER_DATE, DRAFT_OWNER, NONE }
    public enum SortDirection { DOWN, UP, NONE }
}
