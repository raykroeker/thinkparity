/*
 * Created On: October 6, 2006, 5:03 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.swing.GradientPainter;
import com.thinkparity.codebase.swing.border.BottomBorder;
import com.thinkparity.codebase.swing.border.TopBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerModel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;
import com.thinkparity.ophelia.model.container.ContainerDraft;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerPanel extends DefaultTabPanel {
        
    /** The border for the bottom of the cell. */
    private static final Border BORDER_BOTTOM;
    
    /** The border for the bottom of the cell if it is expanded and selected. */
    private static final Border BORDER_BOTTOM_EXPANDED_SELECTED;
    
    /** The border for the bottom of the cell if it is expanded and not selected. */
    private static final Border BORDER_BOTTOM_EXPANDED_NOT_SELECTED;
    
    /** The border for the bottom of the last cell. */
    private static final Border BORDER_BOTTOM_LAST;
    
    /** The border for the top of the cell. */
    private static final Border BORDER_TOP;
    
    /** The border for the top when the cell is expanded. */
    private static final Border BORDER_TOP_EXPANDED;
    
    /** The border for the top of a group of cells. */
    private static final Border BORDER_TOP_GROUP;
    
    /** The border for a selected cell. */
    private static final Border BORDER_SELECTED;
    
    /** The border for an unselected cell. */
    private static final Border BORDER_UNSELECTED;
    
    /** The border colours. */
    private static final Color[] BORDER_COLOURS;
    
    /** The border colours for the first cell of a group. */
    private static final Color[] BORDER_COLOURS_GROUP_TOP;
    
    /** Dimension of the cell. */
    private static final Dimension DIMENSION;
    
    /** An image cache. */
    protected final MainPanelImageCache imageCache;
    
    /** The panel localization. */
    private final MainCellL18n localization;
    
    static {
        BORDER_COLOURS = new Color[] {/*Colours.MAIN_CELL_DEFAULT_BORDER*/Color.white, Color.WHITE};
        BORDER_COLOURS_GROUP_TOP = new Color[] {Colours.MAIN_CELL_DEFAULT_BORDER,
                Colours.MAIN_CELL_DEFAULT_BORDER, Colours.MAIN_CELL_DEFAULT_BORDER, Color.WHITE};
        
        BORDER_TOP = new TopBorder(BORDER_COLOURS, 2, new Insets(2,0,0,0));
        BORDER_TOP_EXPANDED = new TopBorder(Color.WHITE, 2, new Insets(2,0,0,0));
        BORDER_TOP_GROUP = new TopBorder(/*BORDER_COLOURS_GROUP_TOP*/Color.white, 4, new Insets(4,0,0,0));
        BORDER_BOTTOM = new BottomBorder(Color.WHITE);
        BORDER_BOTTOM_LAST = new BottomBorder(Colours.MAIN_CELL_DEFAULT_BORDER, 1, new Insets(0,0,1,0));
        
        // These are so there isn't a white line between the ContainerPanel and the ContainerVersionsPanel
        BORDER_BOTTOM_EXPANDED_SELECTED = new BottomBorder(Colors.Browser.List.LIST_EXPANDED_SELECTED_BG);
        BORDER_BOTTOM_EXPANDED_NOT_SELECTED = new BottomBorder(Colors.Browser.List.LIST_EXPANDED_NOT_SELECTED_BG);
        
        BORDER_SELECTED = new LineBorder(Colors.Browser.List.LIST_SELECTION_BORDER);
        BORDER_UNSELECTED = new LineBorder(Color.WHITE);
        
        DIMENSION = new Dimension(50,23);
    }

    /** A <code>Container</code>. */
    private Container container = null;
    
    /** A <code>ContainerDraft</code>. */
    private ContainerDraft draft = null;

    /** The container panel's model. */
    private final ContainerModel model;

    /**
     * Create ContainerPanel.
     * 
     */
    public ContainerPanel(final ContainerModel model) {
        super();
        this.model = model;
        this.imageCache = new MainPanelImageCache();
        this.localization = new MainCellL18n("ContainerPanel");
        initComponents();
        initBookmarks();
        installMouseOverTracker();
    }
    
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics g2 = g.create();
        try {
            if (isExpanded()) {
                GradientPainter.paintVertical(g2, getSize(),
                        new Color(204, 208, 214, 255), new Color(245, 246, 247, 255));
            }
        }
        finally { g2.dispose(); }
    }

    /**
     * Obtain the container id.
     * 
     * @return A container id <code>Long</code>.
     */
    public Long getContainerId() {
        return container.getId();
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
     * Set the container and its draft.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     */
    public void setContainer(final Container container,
            final ContainerDraft draft) {
        this.container = container;
        this.draft = draft;

        containerNameJLabel.setText(container.getName());
        if (isToday(container.getUpdatedOn().getTime())) {
            containerDateJLabel.setText(localization.getString("TextToday", container.getUpdatedOn().getTime()));
        } else {
            containerDateJLabel.setText(localization.getString("Text", container.getUpdatedOn().getTime())); 
        }  
        if (container.isDraft()) {
            draftOwnerJLabel.setText(draft.getOwner().getName());
        } else {
            draftOwnerJLabel.setText("");
        }
    }
    
    /**
     * Get the container associated with this container panel.
     */
    public Container getContainer() {
        return container;
    }
    
    /**
     * Get the draft associated with this container panel.
     */
    public ContainerDraft getDraft() {
        return draft;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#triggerSingleClick(java.awt.event.MouseEvent)
     */
    @Override
    protected void triggerSingleClick(final MouseEvent e) {
        if (isSetMouseOver()) {
            model.triggerExpand(this);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#triggerDoubleClick(java.awt.event.MouseEvent)
     */
    @Override
    protected void triggerDoubleClick(MouseEvent e) {
        if (!isSetMouseOver()) {
            model.triggerExpand(this);
        }
    }

    /**
     * Show the popup menu for the container.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    @Override
    protected void triggerPopup(final Component invoker, final MouseEvent e) {
        new ContainerPopup(model, container).show(invoker, e);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        westPaddingJLabel = new javax.swing.JLabel();
        iconJLabel = new javax.swing.JLabel();
        containerJPanel = new javax.swing.JPanel();
        containerNameJLabel = new javax.swing.JLabel();
        rightSideJPanel = new javax.swing.JPanel();
        containerDateJLabel = new javax.swing.JLabel();
        draftOwnerJLabel = new javax.swing.JLabel();
        eastPaddingJLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(32767, 23));
        setMinimumSize(new java.awt.Dimension(120, 23));
        setPreferredSize(new java.awt.Dimension(120, 23));
        westPaddingJLabel.setFocusable(false);
        westPaddingJLabel.setMaximumSize(new java.awt.Dimension(4, 20));
        westPaddingJLabel.setMinimumSize(new java.awt.Dimension(4, 20));
        westPaddingJLabel.setPreferredSize(new java.awt.Dimension(4, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(westPaddingJLabel, gridBagConstraints);

        iconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconContainer.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        add(iconJLabel, gridBagConstraints);

        containerJPanel.setLayout(new java.awt.GridLayout(1, 3));

        containerJPanel.setOpaque(false);
        containerNameJLabel.setText("!Package!");
        containerNameJLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        containerNameJLabel.setMaximumSize(new java.awt.Dimension(500, 20));
        containerNameJLabel.setMinimumSize(new java.awt.Dimension(100, 20));
        containerNameJLabel.setPreferredSize(new java.awt.Dimension(100, 20));
        containerJPanel.add(containerNameJLabel);

        rightSideJPanel.setLayout(new java.awt.GridLayout());

        rightSideJPanel.setOpaque(false);
        containerDateJLabel.setText("!Date!");
        containerDateJLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        containerDateJLabel.setMaximumSize(new java.awt.Dimension(500, 20));
        containerDateJLabel.setMinimumSize(new java.awt.Dimension(50, 20));
        containerDateJLabel.setPreferredSize(new java.awt.Dimension(50, 20));
        rightSideJPanel.add(containerDateJLabel);

        draftOwnerJLabel.setText("!Draft Owner!");
        draftOwnerJLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        draftOwnerJLabel.setMaximumSize(new java.awt.Dimension(500, 20));
        draftOwnerJLabel.setMinimumSize(new java.awt.Dimension(50, 20));
        draftOwnerJLabel.setPreferredSize(new java.awt.Dimension(50, 20));
        rightSideJPanel.add(draftOwnerJLabel);

        containerJPanel.add(rightSideJPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(containerJPanel, gridBagConstraints);

        eastPaddingJLabel.setFocusable(false);
        eastPaddingJLabel.setMaximumSize(new java.awt.Dimension(1, 20));
        eastPaddingJLabel.setMinimumSize(new java.awt.Dimension(1, 20));
        eastPaddingJLabel.setPreferredSize(new java.awt.Dimension(1, 20));
        add(eastPaddingJLabel, new java.awt.GridBagConstraints());

    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Initialize bookmarks
     */
    private void initBookmarks() {
        iconJLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                if (container.isBookmarked()) {
                    iconJLabel.setIcon(imageCache.read(TabPanelIcon.CONTAINER_BOOKMARK_ROLLOVER));
                } else {
                    iconJLabel.setIcon(imageCache.read(TabPanelIcon.CONTAINER_ROLLOVER));
                }
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                if (container.isBookmarked()) {
                    iconJLabel.setIcon(imageCache.read(TabPanelIcon.CONTAINER_BOOKMARK));
                } else {
                    iconJLabel.setIcon(imageCache.read(TabPanelIcon.CONTAINER));
                }
            }
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (container.isBookmarked()) {
                    ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER)).runRemoveContainerBookmark(container.getId());
                } else {
                    ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER)).runAddContainerBookmark(container.getId());
                }
            }           
        });
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#setMouseOver(java.lang.Boolean)
     */
    @Override
    public void setMouseOver(final Boolean mouseOver) {
        super.setMouseOver(mouseOver);
/*        if (mouseOver) {
            containerNameJLabel.setText(new StringBuffer("<html><u>")
                .append(container.getName()).append("</u></html>").toString());
        } else {*/
            containerNameJLabel.setText(new StringBuffer("<html>")
                .append(container.getName()).append("</html>").toString());
//        }
        //repaint();
        model.synchronize();
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#formMousePressed(java.awt.event.MouseEvent)
     */
    @Override
    protected void formMousePressed(MouseEvent e) {
        super.formMousePressed(e);
        if (e.getButton()==MouseEvent.BUTTON1) {
            model.selectContainer(container);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#formMouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    protected void formMouseReleased(MouseEvent e) {
        super.formMouseReleased(e);
        if (e.isPopupTrigger()) {
            model.selectContainer(container);
        }
    }
    
    /**
     * Prepare for repaint, for example, adjust colors.
     */
    public void prepareForRepaint() {       
        final Color color;
        if (!isUpToDate()) {
            color = Colors.Browser.List.LIST_NOT_UP_TO_DATE;
        } else if (isSelectedContainer()) {
            color = Colors.Browser.List.LIST_SELECTION_FG;
        } else {
            color = Colors.Browser.List.LIST_FG;
        }
        containerNameJLabel.setForeground(color);
        containerDateJLabel.setForeground(color);
        draftOwnerJLabel.setForeground(color);
        
        if (isExpanded() || (!isSeen())) {
            containerNameJLabel.setFont(Fonts.DefaultFontBold);
        } else {
            containerNameJLabel.setFont(Fonts.DefaultFont);
        }
        
        if (container.isBookmarked()) {
            iconJLabel.setIcon(imageCache.read(TabPanelIcon.CONTAINER_BOOKMARK));
        } else {
            iconJLabel.setIcon(imageCache.read(TabPanelIcon.CONTAINER));
        }
    }
    
    /**
     * Get the background color.
     * 
     * @return Background color.
     */
    public Color getBackgroundColor() {
        final Color color;
        final Integer containerIndex = model.indexOfContainerPanel(container);
        if (isExpanded()) {
            if (isSelectedContainer()) {
                color = Colors.Browser.List.LIST_EXPANDED_SELECTED_BG;
            } else {
                color = Colors.Browser.List.LIST_EXPANDED_NOT_SELECTED_BG;
            }
        } else if (0 == containerIndex % 2) {
            color = Colors.Browser.List.LIST_EVEN_BG;
        } else {
            color = Colors.Browser.List.LIST_ODD_BG;            
        }
        
/*        if (isSelectedContainer()) {
            color = Colors.Browser.List.LIST_SELECTION_BG;
        } else if (isExpanded()) {
            color = Colors.Browser.List.LIST_EXPANDED_NOT_SELECTED_BG;
        } else if (0 == containerIndex % 2) {
            color = Colors.Browser.List.LIST_EVEN_BG;
        } else {
            color = Colors.Browser.List.LIST_ODD_BG;
        }*/
        
        return color;
    }  

    /**
     * Get the preferred size.
     * 
     * @param last
     *          True if this is the last entity.
     * @return The preferred size <code>Dimension</code>.
     */   
    public Dimension getPreferredSize(final Boolean last) {
        final Dimension dimension;
        int heightAdjust = 0;
        
        // Adjust height to account for border thickness
        if (isFirstNonDraft()) {
            heightAdjust += 2;
        }
        if (last && !isExpanded()) {
            heightAdjust += 1;
        }
        if (heightAdjust>0) {
            dimension = new Dimension(DIMENSION);
            dimension.height += heightAdjust;
        } else {
            dimension = DIMENSION;
        }
        
        return dimension;
    }
    
    /**
     * Get the border for the package.
     * 
     * @param last
     *          True if this is the last entity.
     * @return A border.
     */
    public Border getBorder(final Boolean last) {
        //final Border topBorder;
        final Border bottomBorder;
        
        if (isExpanded()) {
            if (isSelectedContainer()) {
                bottomBorder = BORDER_BOTTOM_EXPANDED_SELECTED;
            } else {
                bottomBorder = BORDER_BOTTOM_EXPANDED_NOT_SELECTED;
            }
/*        } else if (isSelectedContainer()) {
            bottomBorder = BORDER_SELECTED;*/
        } else if (isSetMouseOver()) {
            bottomBorder = BORDER_SELECTED;
        } else if (last) {
            bottomBorder = BORDER_BOTTOM_LAST;
        } else {
            bottomBorder = BORDER_UNSELECTED;
        }
        
        return bottomBorder;
        
/*      Code intentionally left here until look and feel is finalized
        if (isFirstNonDraft()) {
            topBorder = BORDER_TOP_GROUP;
        } else if (isExpanded() || isPanelBeforeExpanded()) {
            topBorder = BORDER_TOP_EXPANDED;
        } else {
            topBorder = BORDER_TOP;
        }
        
        if (isExpanded()) {
            if (isSelectedContainer()) {
                bottomBorder = BORDER_BOTTOM_EXPANDED_SELECTED;
            } else {
                bottomBorder = BORDER_BOTTOM_EXPANDED_NOT_SELECTED;
            }
        } else if (last) {
            bottomBorder = BORDER_BOTTOM_LAST;
        } else {
            bottomBorder = BORDER_BOTTOM;
        }*/
        
        //return BorderFactory.createCompoundBorder(topBorder, bottomBorder);
    }
    
    /**
     * Determine if the panel is expanded.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @return True if the panel is expanded; false otherwise.
     */
    private Boolean isExpanded() {
        return model.isExpanded(this);
    }
    
    /**
     * Determine if the panel before this one is expanded.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @return True if the panel before this one is expanded; false otherwise.
     */
    private Boolean isPanelBeforeExpanded() {
        return model.isPanelBeforeExpanded(this);
    }
    
    /**
     * Determine whether or not the container has been seen.
     * 
     * @return True if the container has been seen; false otherwise.
     */
    public Boolean isSeen() {
        return container.contains(ArtifactFlag.SEEN);
    }
    
    /**
     * Determine whether or not this container is up-to-date.
     * 
     * @return True if the container is up-to-date.
     */
    public Boolean isUpToDate() {
        // TODO Do this right
        final String gray = "Gray";
        if (container.getName().equals(gray)) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }       
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
     * Determine if the panel is the first non-draft.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @return True if the panel is the first non-draft; false otherwise.
     */
    private Boolean isFirstNonDraft() {
        return model.isFirstNonDraft(this);
    }
    
    /**
     * Determine if the specified date is today's date.
     */
    private Boolean isToday(Date date) {
        final StringBuffer dateStr = new StringBuffer();
        final StringBuffer todayStr = new StringBuffer();
        dateStr.append(MessageFormat.format("{0,date,yyyyMMMdd}", date));
        todayStr.append(MessageFormat.format("{0,date,yyyyMMMdd}", Calendar.getInstance().getTime()));
        return dateStr.toString().equals(todayStr.toString());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel containerDateJLabel;
    private javax.swing.JPanel containerJPanel;
    private javax.swing.JLabel containerNameJLabel;
    private javax.swing.JLabel draftOwnerJLabel;
    private javax.swing.JLabel eastPaddingJLabel;
    private javax.swing.JLabel iconJLabel;
    private javax.swing.JPanel rightSideJPanel;
    private javax.swing.JLabel westPaddingJLabel;
    // End of variables declaration//GEN-END:variables
}
