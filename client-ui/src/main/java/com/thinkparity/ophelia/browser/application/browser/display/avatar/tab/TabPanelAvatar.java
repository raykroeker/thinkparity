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
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.thinkparity.codebase.swing.border.BottomBorder;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer.ResizeEdges;
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
    
    /** The panel localization. */
    private final MainCellL18n localization;
    
    /** The sort element. */
    private SortElement sortElement = SortElement.NONE;
    
    /** The sort direction. */
    private SortDirection sortDirection = SortDirection.NONE;

    /** Creates new form TabPanelAvatar */
    public TabPanelAvatar(final AvatarId id, final T model) {
    	super(id, model);
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
        initHeader();
        initHeaderListeners();

        //new CursorMovementCustodian().applyCursorKeyListener(this);
    }

    /**
	 * @see com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar#reload()
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
    
    private void initHeader() {
        // Borders
        final Color[] borderColours = new Color[] {Color.WHITE, Colours.MAIN_CELL_DEFAULT_BORDER, Color.WHITE};
        final Border bottomBorder = new BottomBorder(borderColours, 3, new Insets(3,0,0,0));
        columnHeaderJLabel.setBorder(bottomBorder); 
        
        // Text
        iconJLabel.setText(localization.getString("IconHeading"));
        containerNameJLabel.setText(localization.getString("ContainerNameHeading"));
        containerDateJLabel.setText(localization.getString("ContainerDateHeading"));
        draftOwnerJLabel.setText(localization.getString("DraftOwnerHeading"));
        
        // Colours
        final Color headingColor = new Color(0,100,100,255);
        iconJLabel.setForeground(headingColor);
        containerNameJLabel.setForeground(headingColor);
        containerDateJLabel.setForeground(headingColor);
        draftOwnerJLabel.setForeground(headingColor);
    }
    
    private void initHeaderListeners() {
        final MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                headingMouseClicked(e);
            }
            @Override
            public void mouseEntered(final MouseEvent e) {
                JLabel jLabel = (JLabel) e.getSource();
                jLabel.setForeground(Color.RED);
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                JLabel jLabel = (JLabel) e.getSource();
                jLabel.setForeground(new Color(0,100,100,255));
            }  
        };
        iconJLabel.addMouseListener(mouseAdapter);
        containerNameJLabel.addMouseListener(mouseAdapter);
        containerDateJLabel.addMouseListener(mouseAdapter);
        draftOwnerJLabel.addMouseListener(mouseAdapter);       
    }
    
    private void headingMouseClicked(final MouseEvent e) {
        JLabel jLabel = (JLabel) e.getSource();
        Boolean sortElementChanged = Boolean.FALSE;
        if (jLabel.equals(iconJLabel)) {
            if (sortElement != SortElement.BOOKMARK) {
                sortElementChanged = Boolean.TRUE;
            }
            sortElement = SortElement.BOOKMARK;
        } else if (jLabel.equals(containerNameJLabel)) {
            if (sortElement != SortElement.CONTAINER_NAME) {
                sortElementChanged = Boolean.TRUE;
            }
            sortElement = SortElement.CONTAINER_NAME;
        } else if (jLabel.equals(containerDateJLabel)) {
            if (sortElement != SortElement.CONTAINER_DATE) {
                sortElementChanged = Boolean.TRUE;
            }
            sortElement = SortElement.CONTAINER_DATE;
        } else if (jLabel.equals(draftOwnerJLabel)) {
            if (sortElement != SortElement.DRAFT_OWNER) {
                sortElementChanged = Boolean.TRUE;
            }
            sortElement = SortElement.DRAFT_OWNER;
        }
        
        if (sortElementChanged) {
            sortDirection = SortDirection.DOWN;
        } else {
            sortDirection = nextSortDirection(sortDirection);
        }
        
        triggerSort(sortElement, sortDirection);
    }
    
    private SortDirection nextSortDirection(final SortDirection sortDirection) {
        if (sortDirection == SortDirection.NONE) {
            return SortDirection.DOWN;
        } else if (sortDirection == SortDirection.DOWN) {
            return SortDirection.UP;
        } else {
            return SortDirection.NONE;
        }
    }
    
    /**
     * Trigger a sort.
     * 
     * @param sortElement
     *          What the containers will be sorted by.
     * @param sortDirection
     *          The direction of the sort.
     */
    protected void triggerSort(final SortElement sortElement, final SortDirection sortDirection) {       
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        headerJLabel = new javax.swing.JLabel();
        columnHeaderJLabel = new javax.swing.JPanel();
        westPaddingJLabel = new javax.swing.JLabel();
        iconJLabel = new javax.swing.JLabel();
        containerJPanel = new javax.swing.JPanel();
        containerNameJLabel = new javax.swing.JLabel();
        containerDateJLabel = new javax.swing.JLabel();
        draftOwnerJLabel = new javax.swing.JLabel();
        eastPaddingJLabel = new javax.swing.JLabel();
        tabJScrollPane = new javax.swing.JScrollPane();
        tabJPanel = new javax.swing.JPanel();
        fillJLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        headerJLabel.setText(" ");
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

        columnHeaderJLabel.setLayout(new java.awt.GridBagLayout());

        columnHeaderJLabel.setMaximumSize(new java.awt.Dimension(5000, 20));
        columnHeaderJLabel.setOpaque(false);
        columnHeaderJLabel.setPreferredSize(new java.awt.Dimension(128, 20));
        westPaddingJLabel.setFocusable(false);
        westPaddingJLabel.setMaximumSize(new java.awt.Dimension(1, 20));
        westPaddingJLabel.setMinimumSize(new java.awt.Dimension(1, 20));
        westPaddingJLabel.setPreferredSize(new java.awt.Dimension(1, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        columnHeaderJLabel.add(westPaddingJLabel, gridBagConstraints);

        iconJLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        iconJLabel.setText("Flag");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        columnHeaderJLabel.add(iconJLabel, gridBagConstraints);

        containerJPanel.setLayout(new java.awt.GridLayout(1, 0));

        containerJPanel.setOpaque(false);
        containerNameJLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        containerNameJLabel.setText("!Package!");
        containerNameJLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        containerNameJLabel.setMaximumSize(new java.awt.Dimension(500, 14));
        containerNameJLabel.setMinimumSize(new java.awt.Dimension(50, 14));
        containerNameJLabel.setPreferredSize(new java.awt.Dimension(50, 14));
        containerJPanel.add(containerNameJLabel);

        containerDateJLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        containerDateJLabel.setText("!Date!");
        containerDateJLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        containerDateJLabel.setMaximumSize(new java.awt.Dimension(500, 14));
        containerDateJLabel.setMinimumSize(new java.awt.Dimension(50, 14));
        containerDateJLabel.setPreferredSize(new java.awt.Dimension(50, 14));
        containerJPanel.add(containerDateJLabel);

        draftOwnerJLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        draftOwnerJLabel.setText("!Draft Owner!");
        draftOwnerJLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        draftOwnerJLabel.setMaximumSize(new java.awt.Dimension(500, 14));
        draftOwnerJLabel.setMinimumSize(new java.awt.Dimension(50, 14));
        draftOwnerJLabel.setPreferredSize(new java.awt.Dimension(50, 14));
        containerJPanel.add(draftOwnerJLabel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        columnHeaderJLabel.add(containerJPanel, gridBagConstraints);

        eastPaddingJLabel.setFocusable(false);
        eastPaddingJLabel.setMaximumSize(new java.awt.Dimension(4, 20));
        eastPaddingJLabel.setMinimumSize(new java.awt.Dimension(4, 20));
        eastPaddingJLabel.setPreferredSize(new java.awt.Dimension(4, 20));
        columnHeaderJLabel.add(eastPaddingJLabel, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(columnHeaderJLabel, gridBagConstraints);

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

    private void tabJPanelMouseReleased(java.awt.event.MouseEvent e) {// GEN-FIRST:event_tabJPanelMouseReleased
        if (e.isPopupTrigger()) {
            triggerPopup(tabJPanel, e);
        }
    }// GEN-LAST:event_tabJPanelMouseReleased

    private void headerJLabelMouseReleased(java.awt.event.MouseEvent e) {// GEN-FIRST:event_headerJLabelMouseReleased
        if (e.isPopupTrigger()) {
            triggerPopup(tabJPanel, e);
        }
    }// GEN-LAST:event_headerJLabelMouseReleased

    private void headerJLabelMousePressed(java.awt.event.MouseEvent e) {// GEN-FIRST:event_headerJLabelMousePressed
    }// GEN-LAST:event_headerJLabelMousePressed

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
                    panel.setPreferredSize(panel.getPreferredSize(i==(listModel.size()-1)));
                    panel.prepareForRepaint();
                    panel.setBackground(panel.getBackgroundColor());
                    panel.setBorder(panel.getBorder(i==(listModel.size()-1)));
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
                    panel.setPreferredSize(panel.getPreferredSize(i==(listModel.size()-1)));
                    panel.prepareForRepaint();
                    panel.setBackground(panel.getBackgroundColor());
                    panel.setBorder(panel.getBorder(i==(listModel.size()-1)));
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
                    panel.setPreferredSize(panel.getPreferredSize(i==(listModel.size()-1)));
                    panel.prepareForRepaint();
                    panel.setBackground(panel.getBackgroundColor());
                    panel.setBorder(panel.getBorder(i==(listModel.size()-1)));
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
     * Trigger a popup for the tab avatar.
     *
     */
    protected void triggerPopup(final Component invoker, final MouseEvent e) {}

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel columnHeaderJLabel;
    private javax.swing.JLabel containerDateJLabel;
    private javax.swing.JPanel containerJPanel;
    private javax.swing.JLabel containerNameJLabel;
    private javax.swing.JLabel draftOwnerJLabel;
    private javax.swing.JLabel eastPaddingJLabel;
    private javax.swing.JLabel fillJLabel;
    private javax.swing.JLabel headerJLabel;
    private javax.swing.JLabel iconJLabel;
    private javax.swing.JPanel tabJPanel;
    private javax.swing.JScrollPane tabJScrollPane;
    private javax.swing.JLabel westPaddingJLabel;
    // End of variables declaration//GEN-END:variables
    
    public enum SortElement { BOOKMARK, CONTAINER_NAME, CONTAINER_DATE, DRAFT_OWNER, NONE }
    public enum SortDirection { DOWN, UP, NONE }
}
