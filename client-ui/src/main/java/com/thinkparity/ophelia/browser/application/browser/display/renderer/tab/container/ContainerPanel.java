/*
 * Created On: October 6, 2006, 5:03 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserSession;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.FileIconReader;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.*;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;
import com.thinkparity.ophelia.model.container.ContainerDraft;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerPanel extends DefaultTabPanel {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel additionalTextJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel collapsedJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel eastCountJLabel = new javax.swing.JLabel();
    private javax.swing.JLabel eastFillerJLabel;
    private final javax.swing.JLabel eastFirstJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel eastJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel eastLastJLabel = new javax.swing.JLabel();
    private javax.swing.JPanel eastListJPanel;
    private final javax.swing.JLabel eastNextJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel eastPreviousJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel expandedJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel iconJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel textJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel westCountJLabel = new javax.swing.JLabel();
    private javax.swing.JLabel westFillerJLabel;
    private final javax.swing.JLabel westFirstJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel westJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel westLastJLabel = new javax.swing.JLabel();
    private javax.swing.JPanel westListJPanel;
    private final javax.swing.JLabel westNextJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel westPreviousJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables

    /** A <code>Container</code>. */
    protected Container container;

    /** The container's created by <code>User</code>. */
    protected User containerCreatedBy;

    /** A <code>ContainerDraft</code>. */
    protected ContainerDraft draft;

    /** The expanded <code>Boolean</code> state. */
    protected boolean expanded;

    /** The container tab's <code>DefaultActionDelegate</code>. */
    private ActionDelegate actionDelegate;

    /** The visible east list model. */
    private final PanelCellListModel eastListModel;
        
    /** The east list of <code>PanelCellRenderer</code>.*/
    private final List<PanelCellRenderer> eastCellPanels;

    /** A  <code>FileIconReader</code>. */
    private final FileIconReader fileIconReader;

    /** The most recent <code>ContainerVersion</code>. */
    private ContainerVersion latestVersion;

    /** The panel localization. */
    private final MainCellL18n localization;

    /** The container tab's <code>PopupDelegate</code>. */
    private PopupDelegate popupDelegate;

    /** The visible west list model. */
    private final PanelCellListModel westListModel;
    
    /** The west list of <code>Cell</code>. */
    private final List<Cell> westCells;
    
    /** The west list of <code>PanelCellRenderer</code>.*/
    private final List<PanelCellRenderer> westCellPanels;

    /**
     * Create ContainerPanel.
     * 
     */
    public ContainerPanel(final BrowserSession session) {
        super(session);            
        this.expanded = Boolean.FALSE;
        this.fileIconReader = new FileIconReader();
        this.localization = new MainCellL18n("ContainerPanel");
        this.eastListModel = new PanelCellListModel(this, localization,
                NUMBER_VISIBLE_ROWS, eastFirstJLabel, eastPreviousJLabel,
                eastCountJLabel, eastNextJLabel, eastLastJLabel);
        this.westListModel = new PanelCellListModel(this, localization,
                NUMBER_VISIBLE_ROWS, westFirstJLabel, westPreviousJLabel,
                westCountJLabel, westNextJLabel, westLastJLabel);
        this.westCells = new ArrayList<Cell>();
        this.eastCellPanels = new ArrayList<PanelCellRenderer>();
        this.westCellPanels = new ArrayList<PanelCellRenderer>();
        for (int index = 0; index < NUMBER_VISIBLE_ROWS; index++) {
            eastCellPanels.add(new EastCellRenderer(this));
            if (index==0) {
                westCellPanels.add(new TopWestCellRenderer(this));
            } else {
                westCellPanels.add(new WestCellRenderer(this));
            }
        }
        initComponents();
    }

    /**
     * Collapse the panel.
     * 
     */
    public void collapse() {
        doCollapse(true);
    }

    /**
     * Obtain actionDelegate.
     *
     * @return A ContainerTabActionDelegate.
     */
    public ActionDelegate getActionDelegate() {
        return actionDelegate;
    }
    
    /**
     * Obtain the container.
     * 
     * @return A <code>Container</code>.
     */
    public Container getContainer() {
        return container;
    }

    /**
     * Obtain the container draft.
     * 
     * @return A <code>ContainerDraft</code>.
     */
    public ContainerDraft getDraft() {
        return draft;
    }

    /**
     * Obtain the id for the tab panel.  In this case it's a container id.
     *
     * @return An id <code>Object</code>.
     */
    @Override
    public Object getId() {
        return new StringBuffer(getClass().getName()).append("//")
            .append(container.getId()).toString();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#getPopupDelegate()
     * 
     */
    public PopupDelegate getPopupDelegate() {
        return popupDelegate;
    }

    /**
     * Determine the expanded state.
     * 
     * @return A <code>Boolean</code> expanded state.
     */
    public Boolean isExpanded() {
        return Boolean.valueOf(expanded);
    }

    /**
     * Set actionDelegate.
     *
     * @param actionDelegate
     *		A ContainerTabActionDelegate.
     */
    public void setActionDelegate(final ActionDelegate actionDelegate) {
        this.actionDelegate = actionDelegate;
    }

    /**
     * Set the expanded state.
     * 
     * @param expanded
     *            A <code>Boolean</code> expanded state.
     */
    public void setExpanded(final Boolean expanded) {
        if (expanded.booleanValue())
            doExpand(false);
        else
            doCollapse(false);
    }

    /**
     * Expand the panel.
     *
     */
    public void expand() {
        doExpand(true);
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#panelCellMouseClicked(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell, java.awt.event.MouseEvent)
     */
    @Override
    public void panelCellMouseClicked(final Cell cell, final MouseEvent e) {
        if (cell instanceof WestCell) {
            if (e.isPopupTrigger() || (e.getButton() == MouseEvent.BUTTON1)) {
                westListModel.setSelectedCell(cell);
            }
            if (((e.getClickCount() % 2) == 0) &&  (westListModel.getIndexOf(cell)==0)) {
                tabDelegate.toggleExpansion(this);
            }
        } else if (cell instanceof EastCell) {
            if (((e.getClickCount() % 2) == 0) &&  (eastListModel.getIndexOf(cell)==0)) {
                tabDelegate.toggleExpansion(this);
            }
        }
    }
       
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#panelCellSelectionChanged()
     */
    @Override
    public void panelCellSelectionChanged(final Cell cell) {
        if (cell instanceof WestCell) {
            if (westListModel.isSelectionEmpty()) {
                eastListModel.initialize(null);
            } else {
                eastListModel.initialize(((WestCell)cell).getEastCells());
            }
            repaint();
        }
    }

    /**
     * Set the panel data.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     * @param latestVersion
     *            The latest <code>ContainerVersion</code>.
     */
    public void setPanelData(
            final Container container,
            final User containerCreatedBy,
            final ContainerDraft draft,
            final ContainerVersion latestVersion,
            final List<ContainerVersion> versions,
            final Map<ContainerVersion, Map<DocumentVersion, Delta>> documentVersions,
            final Map<ContainerVersion, Map<User, ArtifactReceipt>> publishedTo,
            final Map<ContainerVersion, User> publishedBy,
            final List<TeamMember> team) {
        this.container = container;
        this.containerCreatedBy = containerCreatedBy;
        this.draft = draft;
        this.latestVersion = latestVersion;
        
        // Build the west list
        westCells.add(new ContainerCell(draft, latestVersion, versions, documentVersions, team));        
        if (container.isLocalDraft()) {
            westCells.add(new DraftCell());
        }
        for (final ContainerVersion version : versions) {
            westCells.add(new VersionCell(version, documentVersions
                    .get(version), publishedTo.get(version), publishedBy
                    .get(version)));
        }
        
        // Initialize the list model
        westListModel.initialize(westCells);
        
        iconJLabel.setIcon(container.isBookmarked()
                ? IMAGE_CACHE.read(TabPanelIcon.CONTAINER_BOOKMARK)
                : IMAGE_CACHE.read(TabPanelIcon.CONTAINER));
        reloadText();
        // TODO
        //restoreSelection("eastList", eastListModel, eastJList);
        //restoreSelection("westList", westListModel, westJList);
    }

    /**
     * Set popupDelegate.
     *
     * @param popupDelegate
     *		A ContainerTabPopupDelegate.
     */
    public void setPopupDelegate(final PopupDelegate popupDelegate) {
        this.popupDelegate = popupDelegate;
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     * 
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (expanded) {
            renderer.paintExpandedBackground(g, this);
            if (!westListModel.isSelectionEmpty()) {
                final int selectionIndex = westListModel.getSelectedIndex();
                renderer.paintExpandedBackgroundWest(g, westJPanel.getWidth(), getHeight(), selectionIndex, this);
                renderer.paintExpandedBackgroundCenter(g, westJPanel.getWidth(), getHeight(), selectionIndex, this);
                renderer.paintExpandedBackgroundEast(g, westJPanel.getWidth(), getHeight(), selectionIndex, this);
            }
        } else {
            renderer.paintBackground(g, getWidth(), getHeight());
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#repaintLists()
     *
     */
    @Override
    protected void repaintLists() {
        eastListJPanel.repaint();
        westListJPanel.repaint();
    }

    private void collapsedJPanelMousePressed(java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedJPanelMousePressed
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.isPopupTrigger()) {
            tabDelegate.toggleExpansion(this);
            popupDelegate.initialize(expandedJPanel, e.getX(), e.getY());
            popupDelegate.showForContainer(container);
        } else if ((e.getClickCount() % 2) == 0) {
            tabDelegate.toggleExpansion(this);
        }
    }//GEN-LAST:event_collapsedJPanelMousePressed

    private void collapsedJPanelMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedJPanelMouseReleased
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.isPopupTrigger()) {
            tabDelegate.toggleExpansion(this);
            popupDelegate.initialize(expandedJPanel, e.getX(), e.getY());
            popupDelegate.showForContainer(container);
        }
    }//GEN-LAST:event_collapsedJPanelMouseReleased

    /**
     * Expand the panel.
     * 
     * @param animate
     *            Whether or not to animate.
     */
    private void doExpand(final boolean animate) {
        this.expanded = true;
        remove(collapsedJPanel);
        add(expandedJPanel, constraints.clone());

        if (animate) {
            animator.expand(20, 166);
        } else {
            final Dimension preferredSize = expandedJPanel.getPreferredSize();
            preferredSize.height = 166;
            expandedJPanel.setPreferredSize(preferredSize);
        }

        revalidate();
        reload();
        repaint();
    }

    /**
     * Collapse the panel.
     * 
     * @param animate
     *            Whether or not to animate.
     */
    private void doCollapse(final boolean animate) {
        this.expanded = false;
        remove(expandedJPanel);
        add(collapsedJPanel, constraints.clone());

        if (animate) {
            animator.collapse(20, 25);
        } else {
            final Dimension preferredSize = expandedJPanel.getPreferredSize();
            preferredSize.height = 25;
            expandedJPanel.setPreferredSize(preferredSize);
        }

        revalidate();
        reload();
        repaint();
    }
    
    /**
     * Get a panel cell.
     * 
     * @param listType
     *          The list type.
     * @return The panel cell.
     */
    private PanelCellRenderer getPanelCellRenderer(final ListType listType, final int index) {
        Assert.assertTrue("INVALID PANEL CELL INDEX", (index>=0 && index<NUMBER_VISIBLE_ROWS));
        if (ListType.WEST_LIST == listType) {
            return westCellPanels.get(index);
        } else if (ListType.EAST_LIST == listType) {
            return eastCellPanels.get(index);
        } else {
            throw Assert.createUnreachable("UNKNOWN LIST TYPE");
        }
    }

    private void iconJLabelMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_iconJLabelMouseClicked
        actionDelegate.invokeForContainer(container);
    }//GEN-LAST:event_iconJLabelMouseClicked

    private void iconJLabelMouseEntered(java.awt.event.MouseEvent e) {//GEN-FIRST:event_iconJLabelMouseEntered
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.HAND_CURSOR);
    }//GEN-LAST:event_iconJLabelMouseEntered

    private void iconJLabelMouseExited(java.awt.event.MouseEvent e) {//GEN-FIRST:event_iconJLabelMouseExited
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.DEFAULT_CURSOR);
    }//GEN-LAST:event_iconJLabelMouseExited

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        westListJPanel = new PanelCellListJPanel(westListModel, ListType.WEST_LIST);
        westFillerJLabel = new javax.swing.JLabel();
        eastListJPanel = new PanelCellListJPanel(eastListModel, ListType.EAST_LIST);
        eastFillerJLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setBorder(BORDER);
        collapsedJPanel.setLayout(new java.awt.GridBagLayout());

        collapsedJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                collapsedJPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                collapsedJPanelMouseReleased(evt);
            }
        });

        iconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconContainer.png")));
        iconJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconJLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconJLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconJLabelMouseExited(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 32, 0, 5);
        collapsedJPanel.add(iconJLabel, gridBagConstraints);

        textJLabel.setText("!Package Text!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 4, 0);
        collapsedJPanel.add(textJLabel, gridBagConstraints);

        additionalTextJLabel.setForeground(Colors.Browser.List.INNER_LIST_SELECTION_BORDER);
        additionalTextJLabel.setText("!Package Additional Text!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 4, 0);
        collapsedJPanel.add(additionalTextJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(collapsedJPanel, gridBagConstraints);

        expandedJPanel.setLayout(new java.awt.GridLayout(1, 0));

        expandedJPanel.setOpaque(false);
        westJPanel.setLayout(new java.awt.GridBagLayout());

        westJPanel.setOpaque(false);
        westListJPanel.setLayout(new java.awt.GridBagLayout());

        westListJPanel.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        westJPanel.add(westListJPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 0);
        westJPanel.add(westFillerJLabel, gridBagConstraints);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("localization/ListItem_Messages"); // NOI18N
        westFirstJLabel.setText(bundle.getString("ContainerPanel.firstJLabelWest")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 5);
        westJPanel.add(westFirstJLabel, gridBagConstraints);

        westPreviousJLabel.setText(bundle.getString("ContainerPanel.previousJLabelWest")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        westJPanel.add(westPreviousJLabel, gridBagConstraints);

        westCountJLabel.setText(bundle.getString("ContainerPanel.countJLabel")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        westJPanel.add(westCountJLabel, gridBagConstraints);

        westNextJLabel.setText(bundle.getString("ContainerPanel.nextJLabelWest")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        westJPanel.add(westNextJLabel, gridBagConstraints);

        westLastJLabel.setText(bundle.getString("ContainerPanel.lastJLabelWest")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        westJPanel.add(westLastJLabel, gridBagConstraints);

        expandedJPanel.add(westJPanel);

        eastJPanel.setLayout(new java.awt.GridBagLayout());

        eastJPanel.setOpaque(false);
        eastListJPanel.setLayout(new java.awt.GridBagLayout());

        eastListJPanel.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        eastJPanel.add(eastListJPanel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 0);
        eastJPanel.add(eastFillerJLabel, gridBagConstraints);

        eastFirstJLabel.setText(bundle.getString("ContainerPanel.firstJLabelEast")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        eastJPanel.add(eastFirstJLabel, gridBagConstraints);

        eastPreviousJLabel.setText(bundle.getString("ContainerPanel.previousJLabelEast")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        eastJPanel.add(eastPreviousJLabel, gridBagConstraints);

        eastCountJLabel.setText(bundle.getString("ContainerPanel.countJLabel")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        eastJPanel.add(eastCountJLabel, gridBagConstraints);

        eastNextJLabel.setText(bundle.getString("ContainerPanel.nextJLabelEast")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        eastJPanel.add(eastNextJLabel, gridBagConstraints);

        eastLastJLabel.setText(bundle.getString("ContainerPanel.lastJLabelEast")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 5);
        eastJPanel.add(eastLastJLabel, gridBagConstraints);

        expandedJPanel.add(eastJPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(expandedJPanel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    /**
     * Determine if there is a latest version or not.
     * 
     * @return True if the version is the latest.
     */
    private boolean isLatest() {
        return container.isLocalDraft() || container.isLatest();
    }

    /**
     * Reload the container panel based upon internal criteria; more
     * specifically whether or not the panel is expanded controls the text to
     * display; the icons and fonts to use.
     * 
     */
    private void reload() {
        reloadText();
    }

    /**
     * Reload the text on the panel.
     *
     */
    private void reloadText() {
        // if expanded display the container name;
        // if not expanded and there exists a draft
        // and the user has latest version also display the draft owner; otherwise
        // if there exists the latest version display the published on date
        textJLabel.setText(container.getName());
        if (!expanded) {
            if (container.isDraft() && container.isLatest()) {
                if (container.isLocalDraft()) {
                    additionalTextJLabel.setText(localization.getString(
                            "ContainerMessageLocalDraftOwner"));      
                } else {
                    additionalTextJLabel.setText(localization.getString(
                            "ContainerMessageDraftOwner",
                            draft.getOwner().getName()));
                }
            } else if (null != latestVersion) {
                additionalTextJLabel.setText(localization.getString(
                        "ContainerMessagePublishDate",
                        formatFuzzy(latestVersion.getUpdatedOn())));
            } else {
                additionalTextJLabel.setText("");
            }
        }
        if (!expanded && !container.isSeen().booleanValue()) {
            textJLabel.setFont(Fonts.DefaultFontBold);
            additionalTextJLabel.setFont(Fonts.DefaultFontBold);
        }
        if (!isLatest()) {
            textJLabel.setForeground(
                    Colors.Browser.List.LIST_LACK_MOST_RECENT_VERSION_FG);
        }
    }

    /** An east list cell. */
    private abstract class AbstractEastCell extends DefaultCell implements
            EastCell {
        /**
         * Create AbstractEastCell.
         *
         */
        private AbstractEastCell() {
            super();
            setEnabled(isLatest());
        }
    }

    /** A west list cell. */
    private abstract class AbstractWestCell extends WestCell {
        /**
         * Create AbstractEastCell.
         *
         */
        private AbstractWestCell() {
            super();
            setEnabled(isLatest());
            add(EmptyCell.getEmptyCell());
        }
    }

    /** A container cell. */
    private final class ContainerCell extends AbstractWestCell {
        /**
         * Create ContainerCell.
         */
        private ContainerCell(final ContainerDraft draft,
                final ContainerVersion latestVersion,
                final List<ContainerVersion> versions,
                final Map<ContainerVersion, Map<DocumentVersion, Delta>> documentVersions,
                final List<TeamMember> team) {
            if (container.isLocalDraft()) {
                addDraftDocumentCells(draft);
            } else if (null != latestVersion) {
                addActiveVersionDocumentCells(latestVersion, documentVersions.get(latestVersion));
            }
            for (final ContainerVersion version : versions) {
                addRemovedVersionDocumentCells(version, documentVersions.get(version));
            }
            addUserCells(team);
            prepareText();
        }
        private void addDraftDocumentCells(final ContainerDraft draft) {
            for (final Document document : draft.getDocuments()) {
                add(new ContainerDraftDocumentCell(this, document));
            }
        }
        private void addActiveVersionDocumentCells(final ContainerVersion containerVersion,
                final Map<DocumentVersion, Delta> documentVersions) {
            for (final Entry<DocumentVersion, Delta> entry : documentVersions.entrySet()) {
                final Delta delta = entry.getValue();
                if (Delta.REMOVED != delta) {
                    add(new ContainerVersionDocumentCell(this,
                            containerVersion, entry.getKey(), entry.getValue()));
                }
            }
        }
        private void addRemovedVersionDocumentCells(final ContainerVersion containerVersion,
                final Map<DocumentVersion, Delta> documentVersions) {
            for (final Entry<DocumentVersion, Delta> entry : documentVersions.entrySet()) {
                final Delta delta = entry.getValue();
                if (Delta.REMOVED == delta) {
                    add(new ContainerVersionDocumentCell(this,
                            containerVersion, entry.getKey(), entry.getValue()));
                }
            }
        }
        private void addUserCells(final List<TeamMember> team) {
            for (final TeamMember teamMember : team) {
                add(new ContainerVersionUserCell(this, teamMember));
            }
        }
        private void prepareText() {
            if (container.isDraft() && container.isLatest()) {
                if (container.isLocalDraft()) {
                    setAdditionalText(localization.getString(
                            "ContainerMessageLocalDraftOwner"));      
                } else {
                    setAdditionalText(localization.getString(
                            "ContainerMessageDraftOwner",
                            draft.getOwner().getName()));
                }
            } else if (null != latestVersion) {
                setAdditionalText(localization.getString(
                        "ContainerMessagePublishDate",
                        formatFuzzy(latestVersion.getUpdatedOn())));
            }
        }
        @Override
        public Icon getIcon() {
            if (container.isBookmarked())
                return IMAGE_CACHE.read(TabPanelIcon.CONTAINER_BOOKMARK);
            else
                return IMAGE_CACHE.read(TabPanelIcon.CONTAINER);
        }
        @Override
        public String getText() {
            return container.getName();
        }        
        @Override
        public void invokeAction() {
            actionDelegate.invokeForContainer(container);
        }
        @Override
        public void showPopup() {
            popupDelegate.showForContainer(container);
        }
        @Override
        public Boolean isActionAvailable() {
            return Boolean.TRUE;
        }
    }

    /** A container draft document cell. */
    private final class ContainerDraftDocumentCell extends AbstractEastCell {
        /** A <code>WestCell</code> parent. */
        private final WestCell parent;
        /** A <code>Document</code>. */
        private final Document document;
        /**
         * Create ContainerDraftDocumentCell.
         * 
         * @param document
         *            A <code>Document</code>.
         */
        private ContainerDraftDocumentCell(final WestCell parent, final Document document) {
            super();
            this.parent = parent;
            this.document = document;
            setIcon(fileIconReader.getIcon(document));
            switch (draft.getState(document)) {
            case ADDED:
                setAdditionalText(localization.getString("DocumentSummaryDraftAdded"));
                break;
            case MODIFIED:
                setAdditionalText(localization.getString("DocumentSummaryDraftModified",
                        formatFuzzy(document.getCreatedOn())));
                break;
            case REMOVED:
                setAdditionalText(localization.getString("DocumentSummaryDraftRemoved",
                        formatFuzzy(document.getCreatedOn())));
                break;
            case NONE:
                setAdditionalText(localization.getString("DocumentSummary",
                        formatFuzzy(document.getCreatedOn())));
                break;
            default:
                throw Assert.createUnreachable("UNKNOWN DOCUMENT STATE");
            }
            setText(document.getName());
        }
        @Override
        public void invokeAction() {
            actionDelegate.invokeForDocument(draft, document);
        }
        @Override
        public void showPopup() {
            parent.showPopup();
        }
    }
    
    /** A container version document cell. */
    private final class ContainerVersionDocumentCell extends AbstractEastCell {
        /** A <code>WestCell</code> parent. */
        private final WestCell parent;
        /** A <code>Delta</code>. */
        private final Delta delta;
        /** A <code>DocumentVersion</code>. */
        private final DocumentVersion version;
        /**
         * Create ContainerVersionDocumentCell.
         * 
         * @param containerVersion
         *            A <code>ContainerVersion</code>.
         * @param version
         *            A <code>DocumentVersion</code>.
         * @param delta
         *            A <code>Delta</code>.
         */
        private ContainerVersionDocumentCell(final WestCell parent, final ContainerVersion containerVersion,
                final DocumentVersion version, final Delta delta) {
            super();
            this.parent = parent;
            this.delta = delta;
            this.version = version;
            setIcon(fileIconReader.getIcon(version));
            switch (delta) {
            case ADDED:
            case MODIFIED:
            case NONE:
                setAdditionalText(localization.getString("DocumentSummary",
                        formatFuzzy(version.getCreatedOn())));
                break;
            case REMOVED:
                setAdditionalText(localization.getString("DocumentSummaryVersionRemoved",
                        formatFuzzy(version.getCreatedOn()), formatFuzzy(containerVersion.getCreatedOn())));
                break;
            default:
                throw Assert.createUnreachable("UNKNOWN DOCUMENT STATE");
            }
            setText(version.getName());
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#invokeAction()
         */
        @Override
        public void invokeAction() {
            actionDelegate.invokeForDocument(version, delta);
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#showPopup()
         */
        @Override
        public void showPopup() {
            parent.showPopup();
        }
    }
    
    /** A container user cell. */
    private final class ContainerVersionUserCell extends AbstractEastCell {
        /** A <code>User</code>. */
        private final User user;
        /** A <code>WestCell</code> parent. */
        private final WestCell parent;
        /**
         * Create ContainerVersionUserCell.
         * 
         * @param user
         *            A <code>User</code>.
         */
        private ContainerVersionUserCell(final WestCell parent, final User user) {
            super();
            this.parent = parent;
            this.user = user;
            setIcon(IMAGE_CACHE.read(TabPanelIcon.USER));
            setText(user.getName());
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#invokeAction()
         */
        @Override
        public void invokeAction() {
            actionDelegate.invokeForUser(user);
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#showPopup()
         */
        @Override
        public void showPopup() {
            parent.showPopup();
        }
    }

    /** A draft cell. */
    private final class DraftCell extends AbstractWestCell {
        /**
         * Create DraftCell.
         *
         */
        private DraftCell() {
            super();
            for (final Document document : draft.getDocuments()) {
                add(new DraftDocumentCell(document));
            }
        }
        @Override
        public Icon getIcon() {
            return IMAGE_CACHE.read(TabPanelIcon.DRAFT);
        }
        @Override
        public String getText() {
            if (container.isLocalDraft()) {
                return localization.getString("Draft");
            } else {
                return localization.getString("DraftNotLocal", draft.getOwner().getName());
            }
        }
        
        @Override
        public void showPopup() {
            popupDelegate.showForDraft(container, draft);
        }
    }

    /** A draft document cell. */
    private final class DraftDocumentCell extends AbstractEastCell {
        private final Document document;
        private DraftDocumentCell(final Document document) {
            super();
            this.document = document;
            setIcon(fileIconReader.getIcon(document));
            switch (draft.getState(document)) {
            case ADDED:
            case MODIFIED:
            case REMOVED:
                setAdditionalText(localization.getString(draft.getState(document)));
                break;
            case NONE:
                break;
            default:
                throw Assert.createUnreachable("UNKNOWN DOCUMENT STATE");
            }
            setText(document.getName());
        }
        @Override
        public void invokeAction() {
            actionDelegate.invokeForDocument(draft, document);
        }
        @Override
        public void showPopup() {
            popupDelegate.showForDraft(container, draft);
        }
    }

    /** A version cell. */
    private final class VersionCell extends AbstractWestCell {
        /** The <code>DocumentVersion</code>s and their <code>Delta</code>s. */ 
        private final Map<DocumentVersion, Delta> documentVersions;
        /** A published to <code>User</code>. */
        private final User publishedBy;
        /** The <code>User</code>s and their <code>ArtifactReceipt</code>s. */
        private final Map<User, ArtifactReceipt> publishedTo;
        /** A <code>ContainerVersion</code>. */
        private final ContainerVersion version;
        /**
         * Create VersionCell.
         * 
         * @param version
         *            A <code>ContainerVersion</code>.
         * @param documentVersions
         *            A <code>List</code> of <code>DocumentVersion</code>s.
         * @param users
         *            A <code>List</code> of published to
         *            <code>ArtifactReceipt</code>s and <code>User</code>.
         * @param publishedBy
         *            A published by <code>User</code>.
         */
        private VersionCell(final ContainerVersion version,
                final Map<DocumentVersion, Delta> documentVersions,
                final Map<User, ArtifactReceipt> publishedTo, final User publishedBy) {
            this.documentVersions = documentVersions;
            this.version = version;
            this.publishedBy = publishedBy;
            this.publishedTo = publishedTo;
            for (final Entry<DocumentVersion, Delta> entry : documentVersions.entrySet()) {
                add(new VersionDocumentCell(this, entry.getKey(), entry.getValue()));
            }
            add(new VersionUserCell(this, publishedBy));
            for (final Entry<User, ArtifactReceipt> entry : publishedTo.entrySet()) {
                add(new VersionUserCell(this, entry.getKey(), entry.getValue()));
            }
        }
        @Override
        public Icon getIcon() {
            if (version.isSetComment()) {
                return IMAGE_CACHE.read(TabPanelIcon.VERSION_WITH_COMMENT);
            } else {
                return IMAGE_CACHE.read(TabPanelIcon.VERSION); 
            }
        }
        @Override
        public String getText() {
            return localization.getString("Version", formatFuzzy(version
                    .getCreatedOn()), publishedBy.getName());
        }
        @Override
        public void invokeAction() {
            actionDelegate.invokeForVersion(version);
        }
        @Override
        public void showPopup() {
            popupDelegate.showForVersion(version, documentVersions,
                    publishedTo, publishedBy);
        }
        @Override
        public Boolean isActionAvailable() {
            return version.isSetComment();
        }
    }

    /** A version document cell. */
    private final class VersionDocumentCell extends AbstractEastCell {
        /** A <code>Delta</code>. */
        private final Delta delta;
        /** A <code>WestCell</code> parent. */
        private final WestCell parent;
        /** A <code>DocumentVersion</code>. */
        private final DocumentVersion version;
        /**
         * Create VersionDocumentCell.
         * 
         * @param version
         *            A <code>DocumentVersion</code>.
         * @param delta
         *            A <code>Delta</code>.
         */
        private VersionDocumentCell(final WestCell parent,
                final DocumentVersion version, final Delta delta) {
            super();
            this.delta = delta;
            this.parent = parent;
            this.version = version;
            setIcon(fileIconReader.getIcon(version));
            switch (delta) {
            case ADDED:
            case MODIFIED:
            case REMOVED:
                setAdditionalText(localization.getString(delta));
                break;
            case NONE:
                break;
            default:
                throw Assert.createUnreachable("UNKNOWN DOCUMENT STATE");
            }
            setText(version.getName());
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#invokeAction()
         */
        @Override
        public void invokeAction() {
            actionDelegate.invokeForDocument(version, delta);
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#showPopup()
         */
        @Override
        public void showPopup() {
            parent.showPopup();
        }
    }

    /** A user cell. */
    private final class VersionUserCell extends AbstractEastCell {
        /** A <code>User</code>. */
        private final User user;
        /** A <code>WestCell</code> parent. */
        private final WestCell parent;
        /**
         * Create VersionUserCell.
         * 
         * @param user
         *            A <code>User</code>.
         */
        private VersionUserCell(final WestCell parent, final User user) {
            super();
            this.parent = parent;
            this.user = user;
            setIcon(IMAGE_CACHE.read(TabPanelIcon.USER));
            setText(user.getName());
            setAdditionalText(localization.getString("UserPublished"));
        }
        
        /**
         * Create VersionUserCell.
         * 
         * @param user
         *            A <code>User</code>.
         * @param receipt
         *            An <code>ArtifactReceipt</code>.
         */
        private VersionUserCell(final WestCell parent, final User user,
                final ArtifactReceipt receipt) {
            super();
            this.parent = parent;
            this.user = user;
            setIcon(receipt.isSetReceivedOn()
                    ? IMAGE_CACHE.read(TabPanelIcon.USER)
                    : IMAGE_CACHE.read(TabPanelIcon.USER_NOT_RECEIVED));
            setText(user.getName());
            if (receipt.isSetReceivedOn()) {
                setAdditionalText(localization.getString("UserReceived",
                        formatFuzzy(receipt.getReceivedOn())));
            }                   
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#invokeAction()
         */
        @Override
        public void invokeAction() {
            actionDelegate.invokeForUser(user);
        }
        /**
         * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.DefaultCell#showPopup()
         */
        @Override
        public void showPopup() {
            parent.showPopup();
        }
    }

    private class PanelCellListJPanel extends javax.swing.JPanel {
        
        /** The list model. */
        private final PanelCellListModel panelCellListModel;
        
        /** The list type. */
        private final ListType listType;
        
        /**
         * The set of <code>GridBagConstraints</code> used when adding a fill
         * component.
         */
        private final GridBagConstraints fillConstraints;

        /** The set of <code>GridBagConstraints</code> used when adding a panel. */
        private final GridBagConstraints panelConstraints;
        
        /** A fill <code>JLabel</code>. */
        private final javax.swing.JLabel fillJLabel = new javax.swing.JLabel();
        
        PanelCellListJPanel(final PanelCellListModel panelCellListModel,
                final ListType listType) {
            super();
            this.panelCellListModel = panelCellListModel;
            this.listType = listType;
            this.fillConstraints = new GridBagConstraints();
            this.fillConstraints.fill = GridBagConstraints.HORIZONTAL;
            this.fillConstraints.weightx = 1.0F;
            this.fillConstraints.weighty = 1.0F;
            this.fillConstraints.gridx = 0;
            this.fillConstraints.gridy = GridBagConstraints.RELATIVE;
            this.panelConstraints = new GridBagConstraints();
            this.panelConstraints.fill = GridBagConstraints.BOTH;
            this.panelConstraints.gridx = 0;
            add(fillJLabel, new java.awt.GridBagConstraints());
            installDataListener();
        }
        
        private void installDataListener() {
            final DefaultListModel listModel = panelCellListModel.getListModel();
            listModel.addListDataListener(new ListDataListener() {
                /**
                 * @see javax.swing.event.ListDataListener#contentsChanged(javax.swing.event.ListDataEvent)
                 */
                public void contentsChanged(final ListDataEvent e) {
                    removeFill();
                    for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
                        removePanel(i);
                        addPanel((Cell) listModel.get(i), i);
                    }
                    addFill(listModel.size());
                    reloadPanels();
                }
                
                /**
                 * @see javax.swing.event.ListDataListener#intervalAdded(javax.swing.event.ListDataEvent)
                 */
                public void intervalAdded(final ListDataEvent e) {
                    removeFill();
                    for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
                        addPanel((Cell) listModel.get(i), i);
                    }
                    addFill(listModel.size());
                    reloadPanels();
                }
                
                /**
                 * @see javax.swing.event.ListDataListener#intervalRemoved(javax.swing.event.ListDataEvent)
                 */
                public void intervalRemoved(final ListDataEvent e) {
                    removeFill();
                    for (int i = e.getIndex1(); i >= e.getIndex0(); i--) {
                        removePanel(i);
                    }
                    addFill(listModel.size());
                    reloadPanels();
                } 
            });
        }
        
        /**
         * Add the fill component.
         * 
         * @param listSize
         *            The <code>int</code> size of the list.
         */
        private void addFill(final int listSize) {
            add(fillJLabel, fillConstraints, listSize);
        }

        /**
         * Add a panel from a model at an index.
         * 
         * @param cell
         *            A <code>Cell</code>.
         * @param index
         *            An <code>int</code> index.
         */
        private void addPanel(final Cell cell, final int index) {
            panelConstraints.gridy = index;
            final PanelCellRenderer panelCell = getPanelCellRenderer(listType, index);
            panelCell.renderComponent(cell, index);
            add((Component)panelCell, panelConstraints.clone(), index);
        }
        
        /**
         * Reload the panels display. The jpanel is revalidated.
         * 
         */
        private void reloadPanels() {
            revalidate();
            repaint();
        }
        
        /**
         * Remove the fill component.
         *
         */
        private void removeFill() {
            remove(fillJLabel);
        }

        /**
         * Remove a panel at an index.
         * 
         * @param index
         *            An <code>int</code> index.
         */
        private void removePanel(final int index) {
            remove(index);
        }
    }
    
    private enum ListType { WEST_LIST, EAST_LIST }
}
