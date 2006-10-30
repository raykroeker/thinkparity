/*
 * Created On: October 7, 2006, 10:25 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.swing.GradientPainter;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.border.BottomBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerModel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;
import com.thinkparity.ophelia.browser.util.ArtifactUtil;
import com.thinkparity.ophelia.browser.util.ArtifactVersionUtil;
import com.thinkparity.ophelia.model.container.ContainerDraft;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerVersionsPanel extends DefaultTabPanel {
    
    /** Dimension of the cell. */
    private static final Dimension DIMENSION;
    
    /** The border for the bottom of the last cell. */
    private static final Border BORDER_BOTTOM_LAST;
    
    static {        
        DIMENSION = new Dimension(50,100);
        BORDER_BOTTOM_LAST = new BottomBorder(Colours.MAIN_CELL_DEFAULT_BORDER, 1, new Insets(0,0,1,0));
    }

    /** The <code>Container</code>. */
    private Container container;

    /** The version's <code>DocumentVersion</code>s. */
    private final Map<ContainerVersion, List<DocumentVersion>> documentVersions;

    /** The <code>ContainerModel</code>. */
    private final ContainerModel model;

    /** The version's published by <code>User</code>. */
    private final Map<ContainerVersion, User> publishedBy;

    /** The version's <code>User</code> and <code>ArtifactReceipt</code>. */
    private final Map<ContainerVersion, Map<User, ArtifactReceipt>> users;

    /** The container's <code>ContainerVersion</code>s. */
    private final List<ContainerVersion> versions;

    /** The version's content list model. */
    private final DefaultListModel versionsContentModel;

    /** The version's list model. */
    private final DefaultListModel versionsModel;
    
    /** Flag indicating whether the left side or the right side has focus. */
    private ListType focusList = ListType.VERSION;
    
    /** The popup cell index on the versions list. */
    private int versionsPopupCellIndex = -1;
    
    /** The popup cell index on the content list. */
    private int contentPopupCellIndex = -1;
    
    /** An image cache. */
    protected final MainPanelImageCache imageCache;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JPanel leftJPanel;
    javax.swing.JPanel rightJPanel;
    private javax.swing.JList versionsContentJList;
    private javax.swing.JList versionsJList;
    javax.swing.JSplitPane versionsJSplitPane;
    // End of variables declaration//GEN-END:variables

    /**
     * Create ContainerVersionsPanel
     *
     * @param model
     *      The <code>ContainerModel</code>.
     */
    public ContainerVersionsPanel(final ContainerModel model) {
        super();
        this.documentVersions = new HashMap<ContainerVersion, List<DocumentVersion>>();
        this.model = model;
        this.publishedBy = new HashMap<ContainerVersion, User>();
        this.users = new HashMap<ContainerVersion, Map<User, ArtifactReceipt>>();
        this.versions = new ArrayList<ContainerVersion>();
        this.versionsModel = new DefaultListModel();
        this.versionsContentModel = new DefaultListModel();
        this.imageCache = new MainPanelImageCache();
        initComponents();
        initFocusListeners();
        initResizeListener();
        initMouseOverTrackers();
    }
    
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    private class GradientJPanel extends javax.swing.JPanel {
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            final Graphics g2 = g.create();
            try {
                GradientPainter.paintVertical(g2, getSize(),
                        Colors.Browser.List.LIST_GRADIENT_LIGHT,
                        Colors.Browser.List.LIST_GRADIENT_DARK);
            }
            finally { g2.dispose(); }
        }
    }
    
    /**
     * Initialize focus listeners, so we always know if focus is on left or right.
     */
    private void initFocusListeners() {
        versionsJList.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                focusList = ListType.VERSION;
            }            
        });
        versionsContentJList.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                focusList = ListType.CONTENT;
            }
            public void focusLost(final FocusEvent e) {
                // We don't want to reset this flag when there
                // is a popup in the versionsContentJList.
                if (!isSelectedContainer()) {
                    focusList = ListType.VERSION;
                }
            }
        });
    }
    
    /**
     * Install a mouse over tracker which tracks the list cell that the
     * mouse is over.
     */
    private final void initMouseOverTrackers() {
        class MouseOverTracker extends MouseInputAdapter {
            private int mouseOverIndex = -1;
            
            public MouseOverTracker() {
                super();
            }
            
            @Override
            public void mouseEntered(final MouseEvent e) {
                mouseMoved(e);
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                if (-1 != mouseOverIndex) {
                    final JList jList = (JList) e.getSource();
                    updateCellMouseOver(jList, mouseOverIndex, Boolean.FALSE);
                    mouseOverIndex = -1;
                }
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                final JList jList = (JList) e.getSource();
                final int mouseOverIndex = getMouseOverIndex(e, jList);
                if (mouseOverIndex != this.mouseOverIndex) {                   
                    if (-1 != this.mouseOverIndex) {
                        updateCellMouseOver(jList, this.mouseOverIndex, Boolean.FALSE);
                    }
                    if (-1 != mouseOverIndex) {
                        updateCellMouseOver(jList, mouseOverIndex, Boolean.TRUE);
                    }
                    this.mouseOverIndex = mouseOverIndex;
                }
            }
        }
        
        final MouseOverTracker versionsJListListener = new MouseOverTracker();        
        versionsJList.addMouseListener(versionsJListListener);
        versionsJList.addMouseMotionListener(versionsJListListener);
        
        final MouseOverTracker versionsContentJListListener = new MouseOverTracker();
        versionsContentJList.addMouseListener(versionsContentJListListener);
        versionsContentJList.addMouseMotionListener(versionsContentJListListener);
    }
    
    /**
     * Get the index of the cell the event is over.
     * 
     * @param e
     *          Mouse event.
     * @param jList
     *          JList.       
     * @return  index of the cell the mouse is over, or -1.
     */
    private Integer getMouseOverIndex(java.awt.event.MouseEvent e, final JList jList) {
        Integer tabCellIndex = jList.locationToIndex(e.getPoint());
        
        // Handle the case that the mouse is below the last entry
        if ((tabCellIndex != -1) && (tabCellIndex == jList.getModel().getSize() - 1)) {
            final Rectangle tabCellBounds = jList.getCellBounds(tabCellIndex, tabCellIndex);
            if (!SwingUtil.regionContains(tabCellBounds, e.getPoint())) {
                tabCellIndex = -1;
            }
        }
        
        return tabCellIndex;
    }
    
    /**
     * Update the cell that the mouse is over with the mouseOver flag and
     * cause the cell to redraw.
     * 
     * @param jList
     *              JList.
     * @param index
     *              Index of the cell.
     * @param mouseOver
     *              Mouse over flag.
     */
    private void updateCellMouseOver(final JList jList, final Integer index, final Boolean mouseOver) {
        // If there is no popup then reset the popup flags.
        if (!MenuFactory.isPopupMenu()) {
            versionsPopupCellIndex = -1;
            contentPopupCellIndex = -1;
        }
        
        saveSelection(jList);
        if (index < jList.getModel().getSize()) {
            final AbstractCell cell = (AbstractCell) jList.getModel().getElementAt(index);
            cell.setMouseOver(mouseOver);
            ((DefaultListModel)jList.getModel()).removeElementAt(index);
            ((DefaultListModel)jList.getModel()).add(index, cell);
            restoreSelection(jList);
        }
    }
    
    /**
     * Restore the saved selection.
     *
     */
    private void restoreSelection(final JList jList) {
        final Integer selection =
            (Integer) getClientProperty(ClientProperty.SAVE_SELECTION);
        jList.setSelectedIndex(selection);
    }

    /**
     * Save the current selection cell.
     *
     */
    private void saveSelection(final JList jList) {
        putClientProperty(ClientProperty.SAVE_SELECTION, jList.getSelectedIndex());
    }
       
    /**
     * Initialize resize listener, so we can ensure that the split pane
     * is split 50/50 when it is resized.
     */
    private void initResizeListener() {
        versionsJSplitPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                versionsJSplitPane.setDividerLocation(0.5);               
            }            
        });
    }

    /**
     * Add a container version. The container version includes a list of
     * documents, a list of users and their respective receipts, and the user
     * who published the container.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param documentVersions
     *            A <code>List&lt;Document&gt;</code>.
     * @param users
     *            A <code>Map&lt;User, ArtifactReceipt&gt;</code>.
     * @param publishedBy
     *            A <code>User</code>.
     */
    public void add(final ContainerVersion version,
            final List<DocumentVersion> documentVersions,
            final Map<User, ArtifactReceipt> users, final User publishedBy) {
        this.versions.add(version);
        this.documentVersions.put(version, documentVersions);
        this.users.put(version, users);
        this.publishedBy.put(version, publishedBy);

        versionsModel.addElement(new VersionCell(version, documentVersions,
                users, publishedBy));
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#getId()
     */
    @Override
    public Object getId() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(container.getId()).toString();
    }

    /**
     * Select the first version cell.
     *
     */
    public void selectFirstVersion() {
        if (0 < versionsModel.size()) {
            versionsJList.setSelectedIndex(0);
        }
    }

    /**
     * Set the container draft.
     * 
     * @param container
     *            The <code>Container</code>.
     * @param draft
     *            The <code>ContainerDraft</code>.
     */
    public void setDraft(final Container container, final ContainerDraft draft) {
        this.container = container;

        if (null != draft) {
            versionsModel.addElement(new DraftCell(draft));
        }
    }

    /**
     * Prepare for repaint, for example, adjust colors.
     */
    public void prepareForRepaint() {          
        // Set background colour
        final Color color = getBackgroundColor();
        leftJPanel.setBackground(color);
        rightJPanel.setBackground(color);  
        
        // If necessary, add filler rows to the versions list
        // so the backgrounds will paint in alternating colours
        if (versionsJList.getModel().getSize() < versionsJList.getVisibleRowCount()) {
            for (int i = versionsJList.getModel().getSize(); i < versionsJList.getVisibleRowCount(); i++) {
                versionsModel.addElement(new VersionFillerCell());
            }
        }
    }
    
    /**
     * Prepare for repaint, after validate().
     */
    public void prepareForRepaintAfterValidate() {
        // Make sure the split pane is in a 50/50 split.
        // This can only be done after the first validation() completes.
        versionsJSplitPane.setDividerLocation(0.5);
        
        // Make sure one of the lists has focus.
        // TODO This should not have to rely on SwingUtilities.invokeLater to work.
        if (!versionsJList.isFocusOwner() && !versionsContentJList.isFocusOwner()) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (focusList == ListType.CONTENT) {
                        versionsContentJList.requestFocusInWindow();
                    } else {
                        versionsJList.requestFocusInWindow();
                    }
                }
            });
        }
    }
    
    /**
     * Get the background color.
     * 
     * @return Background color.
     */
    public Color getBackgroundColor() {
        return Colors.Browser.List.LIST_EXPANDED_BG;
    }  
    
    /**
     * Get the border for the package.
     * 
     * @param first
     *          True if this is the first entity in the list.
     * @param last
     *          True if this is the last entity in the list.
     * @return A border.
     */
    public Border getBorder(final Boolean first, final Boolean last) {
        final Border bottomBorder;
        if (last) {
            bottomBorder = BORDER_BOTTOM_LAST;
        } else {
            bottomBorder = null;
        }

        return bottomBorder;    
    }
    
    /**
     * Determine if the container is selected.
     * 
     * @return True if the container is selected; false otherwise.
     */
    private Boolean isSelectedContainer() {
        return model.isSelectedContainer(container);
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#triggerPopup(java.awt.Component,
     *      java.awt.event.MouseEvent)
     */
    @Override
    protected void triggerPopup(final Component invoker, final MouseEvent e) {
        new ContainerPopup(model, container).show(invoker, e);
    }
  
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#triggerDoubleClick(java.awt.event.MouseEvent)
     */
    @Override
    protected void triggerDoubleClick(MouseEvent e) {
        model.triggerExpand(container);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JScrollPane versionsContentJScrollPane;
        javax.swing.JScrollPane versionsJScrollPane;

        versionsJSplitPane = new javax.swing.JSplitPane();
        leftJPanel = new GradientJPanel();
        versionsJScrollPane = new javax.swing.JScrollPane();
        versionsJList = new javax.swing.JList();
        rightJPanel = new GradientJPanel();
        versionsContentJScrollPane = new javax.swing.JScrollPane();
        versionsContentJList = new javax.swing.JList();

        setLayout(new java.awt.GridBagLayout());

        setOpaque(false);
        versionsJSplitPane.setBorder(null);
        versionsJSplitPane.setDividerSize(0);
        versionsJSplitPane.setResizeWeight(0.5);
        versionsJSplitPane.setMinimumSize(new java.awt.Dimension(52, 75));
        leftJPanel.setLayout(new java.awt.GridBagLayout());

        leftJPanel.setBackground(new java.awt.Color(255, 255, 255));
        leftJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                leftJPanelMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                leftJPanelMousePressed(evt);
            }
        });

        versionsJScrollPane.setBorder(null);
        versionsJList.setModel(versionsModel);
        versionsJList.setCellRenderer(new VersionCellRenderer());
        versionsJList.setVisibleRowCount(5);
        versionsJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                versionsJListValueChanged(evt);
            }
        });
        versionsJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                versionsJListMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                versionsJListMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                versionsJListMouseReleased(evt);
            }
        });

        versionsJScrollPane.setViewportView(versionsJList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 4, 1);
        leftJPanel.add(versionsJScrollPane, gridBagConstraints);

        versionsJSplitPane.setLeftComponent(leftJPanel);

        rightJPanel.setLayout(new java.awt.GridBagLayout());

        rightJPanel.setBackground(new java.awt.Color(255, 255, 255));
        rightJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rightJPanelMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                rightJPanelMousePressed(evt);
            }
        });

        versionsContentJScrollPane.setBorder(null);
        versionsContentJList.setModel(versionsContentModel);
        versionsContentJList.setCellRenderer(new VersionContentCellRenderer());
        versionsContentJList.setVisibleRowCount(5);
        versionsContentJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                versionsContentJListMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                versionsContentJListMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                versionsContentJListMouseReleased(evt);
            }
        });

        versionsContentJScrollPane.setViewportView(versionsContentJList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 1, 4, 4);
        rightJPanel.add(versionsContentJScrollPane, gridBagConstraints);

        versionsJSplitPane.setRightComponent(rightJPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(versionsJSplitPane, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void rightJPanelMouseClicked(final java.awt.event.MouseEvent e) {// GEN-FIRST:event_rightJPanelMouseClicked
        formMouseClicked(e);
    }// GEN-LAST:event_rightJPanelMouseClicked

    private void leftJPanelMouseClicked(final java.awt.event.MouseEvent e) {// GEN-FIRST:event_leftJPanelMouseClicked
        formMouseClicked(e);
    }// GEN-LAST:event_leftJPanelMouseClicked

    private void rightJPanelMousePressed(final java.awt.event.MouseEvent e) {// GEN-FIRST:event_rightJPanelMousePressed
        formMousePressed(e);
    }// GEN-LAST:event_rightJPanelMousePressed

    private void leftJPanelMousePressed(final java.awt.event.MouseEvent e) {// GEN-FIRST:event_leftJPanelMousePressed
        formMousePressed(e);
    }// GEN-LAST:event_leftJPanelMousePressed
    
    private void triggerJListPopup(final AbstractContentCell selectedContent,
            final MouseEvent e) {
        selectedContent.showPopupMenu(versionsContentJList, e);
    }

    private void triggerJListPopup(final AbstractVersionCell selectedVersion,
            final MouseEvent e) {
        selectedVersion.showPopupMenu(versionsJList, e);
    }
    
    private void versionsContentJListMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_versionsContentJListMouseClicked
        final JList jList = (JList) e.getSource();
        handleJListMouseClicked(jList, ListType.CONTENT, e);
    }//GEN-LAST:event_versionsContentJListMouseClicked

    private void versionsContentJListMousePressed(java.awt.event.MouseEvent e) {//GEN-FIRST:event_versionsContentJListMousePressed
        final JList jList = (JList) e.getSource();
        handleJListMousePressed(jList, ListType.CONTENT, e);
    }//GEN-LAST:event_versionsContentJListMousePressed

    private void versionsContentJListMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_versionsContentJListMouseReleased
        final JList jList = (JList) e.getSource();
        handleJListMouseReleased(jList, ListType.CONTENT, e);   
    }//GEN-LAST:event_versionsContentJListMouseReleased

    private void versionsJListMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_versionsJListMouseClicked
        final JList jList = (JList) e.getSource();
        handleJListMouseClicked(jList, ListType.VERSION, e);
    }//GEN-LAST:event_versionsJListMouseClicked
    
    private void versionsJListMousePressed(java.awt.event.MouseEvent e) {//GEN-FIRST:event_versionsJListMousePressed
        final JList jList = (JList) e.getSource();
        handleJListMousePressed(jList, ListType.VERSION, e);
    }//GEN-LAST:event_versionsJListMousePressed

    private void versionsJListMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_versionsJListMouseReleased
        final JList jList = (JList) e.getSource();
        handleJListMouseReleased(jList, ListType.VERSION, e);        
    }//GEN-LAST:event_versionsJListMouseReleased
    
    private void versionsJListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_versionsJListValueChanged
        if (!evt.getValueIsAdjusting()) {
            logger.logVariable("evt", evt);
            versionsContentModel.clear();
            for (final Object selectedValue : versionsJList.getSelectedValues()) {
                for (final AbstractContentCell contentCell : ((AbstractVersionCell) selectedValue).contentCells) {
                    versionsContentModel.addElement(contentCell);
                }
            }
        }
    }//GEN-LAST:event_versionsJListValueChanged
    
    /**
     * Process a JList mouse pressed event.
     */
    private void handleJListMousePressed(final JList jList, final ListType listType, final java.awt.event.MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.getButton()==MouseEvent.BUTTON1) {
            jList.requestFocusInWindow();
            versionsPopupCellIndex = -1;
            contentPopupCellIndex = -1;
        }
        if (e.isPopupTrigger()) { 
            handleJListMousePopupTrigger(jList, listType, e);
        }
    }
    
    /**
     * Process a JList mouse released event.
     */
    private void handleJListMouseReleased(final JList jList, final ListType listType, final java.awt.event.MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.isPopupTrigger()) { 
            handleJListMousePopupTrigger(jList, listType, e);
        }
    }
    
    /**
     * Process a JList popup trigger event.
     */
    private void handleJListMousePopupTrigger(final JList jList, final ListType listType, final java.awt.event.MouseEvent e) {
        if (isMouseEventWithinCell(jList, e)) {
            final int index = jList.locationToIndex(e.getPoint());
            if (!isFillerCell(jList, index)) {               
                if (!jList.isFocusOwner()) {
                    jList.requestFocusInWindow();
                }                 
                setSelectedIndex(jList, index);
                if (listType == ListType.VERSION) {
                    versionsPopupCellIndex = index;
                    triggerJListPopup((AbstractVersionCell) jList.getSelectedValue(), e);
                } else if (listType == ListType.CONTENT) {
                    contentPopupCellIndex = index;
                    triggerJListPopup((AbstractContentCell) jList.getSelectedValue(), e);
                }
                e.consume();
            }
        }
    }
    
    /**
     * Process a JList mouse clicked event.
     */
    private void handleJListMouseClicked(final JList jList, final ListType listType, final java.awt.event.MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        if (!MenuFactory.isPopupMenu()) {
            if (e.getButton()==MouseEvent.BUTTON1) {
                final Boolean isInCell = isMouseEventWithinCell(jList, e);
                if (isInCell) { 
                    triggerJListSingleClick(jList, listType, e);
                } else {
                    triggerJListSingleClick();
                }
                // A click count of 2, 4, 6, etc. triggers double click event
                if (e.getClickCount() % 2 == 0) {
                    if (isInCell) {  
                        triggerJListDoubleClick(jList, listType, e);
                    } else {
                        triggerJListDoubleClick();
                    }
                }
            }
        }
    }
    
    /**
     * Handle single click in a JList.
     */
    private void triggerJListSingleClick(final JList jList, final ListType listType, final java.awt.event.MouseEvent e) {        
    }
    
    /**
     * Handle single click below the last entry in a JList.
     */
    private void triggerJListSingleClick() {
    }
    
    /**
     * Handle double click in a JList.
     */
    private void triggerJListDoubleClick(final JList jList, final ListType listType, final java.awt.event.MouseEvent e) {        
        if (listType == ListType.VERSION) {
            ((AbstractVersionCell)jList.getSelectedValue()).doubleClick(jList, e);
        } else if (listType == ListType.CONTENT) {
            ((AbstractContentCell)jList.getSelectedValue()).doubleClick(jList, e);
        }
    }
    
    /**
     * Handle double click below the last entry in a JList.
     */
    private void triggerJListDoubleClick() {
    }
    
    /**
     * Determine if the mouse event occurred on or below the cell of a JList in an x-y plane.
     * @param jList
     *              The JList.
     * @param e
     *              The MouseEvent.
     * @return True if the mouse event occurred in the cell.
     */
    private Boolean isMouseEventWithinCell(final JList jList, final java.awt.event.MouseEvent e) {
        final Integer tabCellIndex = jList.locationToIndex(e.getPoint());
        if (tabCellIndex == -1) {  // Will be -1 if the model is empty
            return Boolean.FALSE;
        } else {
            final Rectangle tabCellBounds = jList.getCellBounds(tabCellIndex, tabCellIndex);
            return SwingUtil.regionContains(tabCellBounds, e.getPoint());
        }
    }
    
    /**
     * Determine if the cell at this index in the list is a filler cell.
     * @param jList
     *              The JList.
     * @param index
     *              The index.
     */
    private Boolean isFillerCell(final JList jList, final int index) {
        final AbstractCell cell = (AbstractCell) jList.getModel().getElementAt(index);
        if ((cell instanceof VersionFillerCell) || (cell instanceof ContentFillerCell)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
    
    /**
     * Select an entry in the JList.
     * 
     * @param jList
     *              The JList.
     * @param index
     *              The JList index to select.
     */
    private void setSelectedIndex(final JList jList, final Integer index) {
        if (index != jList.getSelectedIndex()) {
            jList.setSelectedIndex(index);
        }
    }
    
    /** The abstract list cell. */
    abstract class AbstractCell {
        /** A flag indicating the mouse over status. */
        protected Boolean mouseOver = Boolean.FALSE;  
        
        /** The text associated with this cell. */
        protected String text;
        
        /** The icon associated with this cell. */
        protected Icon icon = null;
        
        protected AbstractCell() {
            super();
        }        
        protected void setMouseOver(final Boolean mouseOver) {
            this.mouseOver = mouseOver;  
        }
        protected Boolean isMouseOver() {
            return mouseOver;
        }
        protected String getText() {
            return text;
        }
        protected void setText(final String text) {
            this.text = text;
        }
        protected Icon getIcon() {
            return icon;
        }
        protected void setIcon(final Icon icon) {
            this.icon = icon;
        }
        protected Boolean isFillerCell() {
            return Boolean.FALSE;
        }
        protected Integer getContainerIndex() {
            return model.indexOfContainerPanel(container);
        }
        protected Boolean isSelectedContainer() {
            return ContainerVersionsPanel.this.isSelectedContainer();
        }
        protected abstract void showPopupMenu(final Component invoker,
                final MouseEvent e);
        protected abstract void doubleClick(final Component invoker,
                final MouseEvent e);
    }

    /** The version's content list cell. */
    abstract class AbstractContentCell extends AbstractCell {                
        protected AbstractContentCell() {
            super();
        }
        protected Boolean isFocusOnThisList() {
            return (ContainerVersionsPanel.this.focusList == ListType.CONTENT);
        }
        protected int getPopupCellIndex() {
            return contentPopupCellIndex;
        }
    }

    /** The version's list cell. */
    abstract class AbstractVersionCell extends AbstractCell {
        /** The list of content cells. */
        private final List<AbstractContentCell> contentCells;
        
        protected AbstractVersionCell() {
            super();
            this.contentCells = new ArrayList<AbstractContentCell>();
        }
        protected void addContentCell(final AbstractContentCell contentCell) {
            this.contentCells.add(contentCell);
        }
        protected List<AbstractContentCell> getContentCells() {
            return Collections.unmodifiableList(contentCells);
        }
        protected Boolean isFocusOnThisList() {
            return (ContainerVersionsPanel.this.focusList == ListType.VERSION);
        }
        protected int getPopupCellIndex() {
            return versionsPopupCellIndex;
        }
    }

    /** A draft cell. */
    final class DraftCell extends AbstractVersionCell {
        private final ContainerDraft draft;
        private DraftCell(final ContainerDraft draft) {
            super();
            this.draft = draft;
            final Integer documentCount = draft.getDocumentCount();
            setText(1 == documentCount ?
                    "Draft - 1 Document" :
                    MessageFormat.format("Draft - {0} Documents",
                            documentCount));
            setIcon(imageCache.read(TabPanelIcon.DRAFT));
            int countCells = 0;
            for (final Document document : draft.getDocuments()) {
                addContentCell(new DraftDocumentCell(draft, document));
                countCells++;
            }
            for (int i = countCells; i < versionsContentJList.getVisibleRowCount(); i++) {
                addContentCell(new ContentFillerCell());
            }
        }
        @Override
        protected void showPopupMenu(final Component invoker, final MouseEvent e) {
            new ContainerVersionsPopup(model, this).show(invoker, e);
        }
        @Override
        protected void doubleClick(final Component invoker, final MouseEvent e) {
        }
        Long getContainerId() {
            return draft.getContainerId();
        }
    }

    /** A draft document cell. */
    final class DraftDocumentCell extends AbstractContentCell {
        private final ContainerDraft draft;
        private final Document document;
        private DraftDocumentCell(final ContainerDraft draft,
                final Document document) {
            super();
            this.document = document;
            this.draft = draft;
            final String formatPattern;
            switch (draft.getState(document)) {
            case ADDED:
            case MODIFIED:
            case REMOVED:
                formatPattern = "{0} - {1}";
                break;
            case NONE:
                formatPattern = "{0}";
                break;
            default:
                throw Assert.createUnreachable("UNKNOWN DOCUMENT STATE");
            }
            setText(MessageFormat.format(formatPattern,
                    document.getName(), draft.getState(document)));
            setIcon(getDocumentIcon(document));
        }
        @Override
        protected void showPopupMenu(final Component invoker, final MouseEvent e) {
            new ContainerVersionsPopup(model, this).show(invoker, e);
        }
        @Override
        protected void doubleClick(final Component invoker, final MouseEvent e) {
            ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER)).runOpenDocument(getId());
        }
        Long getContainerId() {
            return draft.getContainerId();
        }
        Long getId() {
            return document.getId();
        }
        ContainerDraft.ArtifactState getState() {
            return draft.getState(document);
        }
    }

    /** A user cell. */
    final class UserCell extends AbstractContentCell {
        private final User user;
        private UserCell(final User user, final ArtifactReceipt receipt) {
            this.user = user;
            if (receipt.isSetReceivedOn()) {
                setText(MessageFormat.format("{0} - {3,date,MMM dd, yyyy h:mm a}",
                        user.getName(), user.getOrganization(), user.getTitle(),
                        receipt.getReceivedOn().getTime()));
            } else {
                setText(MessageFormat.format("{0}",
                        user.getName(), user.getOrganization(), user.getTitle()));
            }
            setIcon(imageCache.read(TabPanelIcon.USER));
        }
        JabberId getId() {
            return user.getId();
        }
        @Override
        protected void showPopupMenu(final Component invoker, final MouseEvent e) {
            new ContainerVersionsPopup(model, this).show(invoker, e);
        }
        @Override
        protected void doubleClick(final Component invoker, final MouseEvent e) {
            ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER)).runReadContact(getId());
        }
    }

    /** A version cell. */
    final class VersionCell extends AbstractVersionCell {
        private final ContainerVersion version;
        private VersionCell(final ContainerVersion version,
                final List<DocumentVersion> documentVersions,
                final Map<User, ArtifactReceipt> users, final User publishedBy) {
            super();
            this.version = version;
            setText(MessageFormat.format(
                    "Version - {0,date,MMM d, yyyy h:mm a} - {1}",
                    version.getCreatedOn().getTime(),
                    publishedBy.getName(), publishedBy.getTitle(),
                    publishedBy.getOrganization()));
            setIcon(imageCache.read(TabPanelIcon.VERSION));
            int countCells = 0;
            // TODO fix this
            addContentCell(new CommentCell(version));
            countCells++;
            
/*            if (version.isSetComment()) {
                addContentCell(new CommentCell(version));
            }*/
            for (final DocumentVersion documentVersion : documentVersions) {
                addContentCell(new DocumentVersionCell(documentVersion));
                countCells++;
            }
            for (final Entry<User, ArtifactReceipt> entry : users.entrySet()) {
                addContentCell(new UserCell(entry.getKey(), entry.getValue()));
                countCells++;
            }
            for (int i = countCells; i < versionsContentJList.getVisibleRowCount(); i++) {
                addContentCell(new ContentFillerCell());
            }
        }
        @Override
        protected void showPopupMenu(final Component invoker, final MouseEvent e) {
            new ContainerVersionsPopup(model, this).show(invoker, e);
        }
        @Override
        protected void doubleClick(final Component invoker, final MouseEvent e) {
        }
        Long getArtifactId() {
            return version.getArtifactId();
        }
        Long getVersionId() {
            return version.getVersionId();
        }
    }

    /** A version document cell. */
    final class DocumentVersionCell extends AbstractContentCell {
        private final DocumentVersion version;
        private DocumentVersionCell(final DocumentVersion version) {
            super();
            this.version = version;
            setText(MessageFormat.format("{0}", version.getName()));
            setIcon(getDocumentIcon(version));
        }
        @Override
        protected void showPopupMenu(final Component invoker, final MouseEvent e) {
            new ContainerVersionsPopup(model, this).show(invoker, e);
        }
        @Override
        protected void doubleClick(final Component invoker, final MouseEvent e) {
            ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER)).runOpenDocumentVersion(getDocumentId(), getVersionId());
        }
        Long getDocumentId() {
            return version.getArtifactId();
        }
        Long getVersionId() {
            return version.getVersionId();
        }
    }
    
    /** A version comment cell. */
    final class CommentCell extends AbstractContentCell {
        private final ContainerVersion version;
        private CommentCell(final ContainerVersion version) {
            super();
            this.version = version;
            // TODO Get rid of this test code.
            if (version.isSetComment()) {
                setText(version.getComment());
            } else {
                setText("This is a version comment. I like this version very much.");
            }
            setIcon(imageCache.read(TabPanelIcon.COMMENT));
        }
        @Override
        protected void showPopupMenu(final Component invoker, final MouseEvent e) {
            new ContainerVersionsPopup(model, this).show(invoker, e);
        }
        @Override
        protected void doubleClick(final Component invoker, final MouseEvent e) {
            ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER)).displayContainerVersionInfoDialog(getArtifactId(), getVersionId());
        }
        Long getArtifactId() {
            return version.getArtifactId();
        }
        Long getVersionId() {
            return version.getVersionId();
        }
    }
    
    /** A filler cell for the version list, so the background can be drawn */
    final class VersionFillerCell extends AbstractVersionCell {
        private VersionFillerCell() {
            super();
        }
        @Override
        protected void showPopupMenu(final Component invoker, final MouseEvent e) {
        }
        @Override
        protected void doubleClick(final Component invoker, final MouseEvent e) {
        }
        @Override
        protected Boolean isFillerCell() {
            return Boolean.TRUE;
        }       
    }

    /** A filler cell for the content list, so the background can be drawn */
    final class ContentFillerCell extends AbstractContentCell {
        private ContentFillerCell() {
            super();
        }
        @Override
        protected void doubleClick(Component invoker, MouseEvent e) { 
        }
        @Override
        protected void showPopupMenu(Component invoker, MouseEvent e) { 
        }      
        @Override
        protected Boolean isFillerCell() {
            return Boolean.TRUE;
        } 
    }
    
    /**
     * Get the preferred size.
     * 
     * @param first
     *          True if this is the first entity in the list.
     * @param last
     *          True if this is the last entity in the list.
     * @return The preferred size <code>Dimension</code>.
     */   
    public Dimension getPreferredSize(final Boolean first, final Boolean last) {
        return DIMENSION;
    }
    
    /**
     * Get the icon associated with this file extension.
     * 
     * @return The file icon.
     */
    private ImageIcon getFileIcon(final String extension) {
        if (extension.equalsIgnoreCase(".DOC")) {
            return imageCache.read(TabPanelIcon.FILE_DOC); 
        } else if (extension.equalsIgnoreCase(".XLS")) {
            return imageCache.read(TabPanelIcon.FILE_XLS);  
        } else if (extension.equalsIgnoreCase(".PDF")) {
            return imageCache.read(TabPanelIcon.FILE_PDF);
        } else {
            return imageCache.read(TabPanelIcon.FILE_DEFAULT); 
        }
    }
    
    /**
     * Get the document icon.
     * 
     * @return The document icon.
     */
    private ImageIcon getDocumentIcon(final Document document) {
        final String extension = ArtifactUtil.getNameExtension(document);
        return getFileIcon(extension);
    }
    
    /**
     * Get the document icon.
     * 
     * @return The document icon.
     */
    private ImageIcon getDocumentIcon(final DocumentVersion documentVersion) {
        final String extension = ArtifactVersionUtil.getNameExtension(documentVersion);
        return getFileIcon(extension);
    }
    
    private enum ListType { VERSION, CONTENT }
    private enum ClientProperty { SAVE_SELECTION }
}
