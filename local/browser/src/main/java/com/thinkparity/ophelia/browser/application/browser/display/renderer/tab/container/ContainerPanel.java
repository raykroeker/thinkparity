/*
 * Created On: October 6, 2006, 5:03 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;

import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.swing.GradientPainter;
import com.thinkparity.codebase.swing.border.MultiColourLineBorder;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.container.ContainerDraft;

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

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerPanel extends DefaultTabPanel {
    
    /** The border for even cells. */
    private static final Border BORDER_EVEN;

    /** The border for the first cell. */
    private static final Border BORDER_FIRST;
    
    /** The border for the cell if it first and last. */
    private static final Border BORDER_FIRST_AND_LAST;
    
    /** The border for the last cell. */
    private static final Border BORDER_LAST_EVEN;
    
    private static final Border BORDER_LAST_ODD;
    
    /** The border for odd cells. */
    private static final Border BORDER_ODD;
    /** The border for a selected cell. */
    private static final Border BORDER_SELECTED;
    
    /** Dimension of the cell. */
    private static final Dimension DIMENSION;
    
    static {
        BORDER_EVEN = new LineBorder(Colors.Browser.List.LIST_EVEN_BG);
        BORDER_ODD = new LineBorder(Colors.Browser.List.LIST_ODD_BG);
        BORDER_SELECTED = new LineBorder(Colors.Browser.List.LIST_SELECTION_BORDER);
        BORDER_FIRST = new MultiColourLineBorder(
                Colours.MAIN_CELL_DEFAULT_BORDER, Colors.Browser.List.LIST_EVEN_BG,
                Colors.Browser.List.LIST_EVEN_BG, Colors.Browser.List.LIST_EVEN_BG);
        BORDER_FIRST_AND_LAST = new MultiColourLineBorder(
                Colours.MAIN_CELL_DEFAULT_BORDER, Colors.Browser.List.LIST_EVEN_BG,
                Colours.MAIN_CELL_DEFAULT_BORDER, Colors.Browser.List.LIST_EVEN_BG);
        BORDER_LAST_EVEN = new MultiColourLineBorder(
                Colors.Browser.List.LIST_EVEN_BG, Colors.Browser.List.LIST_EVEN_BG,
                Colours.MAIN_CELL_DEFAULT_BORDER, Colors.Browser.List.LIST_EVEN_BG);
        BORDER_LAST_ODD = new MultiColourLineBorder(
                Colors.Browser.List.LIST_ODD_BG, Colors.Browser.List.LIST_ODD_BG,
                Colours.MAIN_CELL_DEFAULT_BORDER, Colors.Browser.List.LIST_ODD_BG);
        
        DIMENSION = new Dimension(50,23);
    }
    
    /** An image cache. */
    protected final MainPanelImageCache imageCache;
    
    /** A <code>Container</code>. */
    private Container container = null;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel containerDateJLabel;
    
    private javax.swing.JPanel containerDateJPanel;

    private javax.swing.JPanel containerJPanel;
    
    private javax.swing.JLabel containerNameJLabel;

    /** A <code>ContainerDraft</code>. */
    private ContainerDraft draft = null;

    private javax.swing.JLabel draftOwnerJLabel;
    
    private javax.swing.JPanel draftOwnerJPanel;
    
    private javax.swing.JLabel eastPaddingJLabel;

    /** The focus manager. */
    private final FocusManager focusManager;

    private javax.swing.JLabel iconJLabel;

    /** The panel localization. */
    private final MainCellL18n localization;

    /** The container panel's model. */
    private final ContainerModel model;

    private javax.swing.JProgressBar progressBar;
    
    private javax.swing.JPanel progressBarJPanel;
    
    private javax.swing.JPanel rightSideJPanel;

    private javax.swing.JLabel westPaddingJLabel;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Create ContainerPanel.
     * 
     */
    public ContainerPanel(final ContainerModel model) {
        super();
        this.model = model;
        this.imageCache = new MainPanelImageCache();
        this.localization = new MainCellL18n("ContainerPanel");
        this.focusManager = new FocusManager();
        initComponents();
        initBookmarks();
        installMouseOverTracker();
        focusManager.addFocusListener(this, model);
    }

    /**
     * Called if the focus list changes.
     * 
     * @param focusList
     *      The list with focus.
     */
    public void focusChanged(final FocusManager.FocusList focusList) {
        repaint();
    }

    /**
     * Get the background color.
     * 
     * @return Background color.
     */
    public Color getBackgroundColor() {
        final Color color;
        final Integer containerIndex = model.indexOfContainerPanel(container);

        if (0 == containerIndex % 2) {
            color = Colors.Browser.List.LIST_EVEN_BG;
        } else {
            color = Colors.Browser.List.LIST_ODD_BG;            
        }
        
        return color;
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
        Border border;
        final Integer containerIndex = model.indexOfContainerPanel(container);
 
        if (!isExpanded()) {
            if (isSelectedContainer()) {
                // This is to highlight a panel when there is a popup on that panel,
                // or the user clicked on a container panel.
                border = BORDER_SELECTED;
            } else if (first && last) {
                border = BORDER_FIRST_AND_LAST;
            } else if (first) {
                border = BORDER_FIRST;
            } else if (last) {
                if (0 == containerIndex % 2) {
                    border = BORDER_LAST_EVEN;
                } else {
                    border = BORDER_LAST_ODD;
                }
            } else if (0 == containerIndex % 2) {
                border = BORDER_EVEN;
            } else {
                border = BORDER_ODD; 
            }
        } else {
            border = null;
        }
                
        return border;
    }
    
    /**
     * Get the container associated with this container panel.
     */
    public Container getContainer() {
        return container;
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
     * Get the draft associated with this container panel.
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#installProgressBar()
     *
     */
    @Override
    public void installProgressBar() {
        progressBar.setIndeterminate(true);
        containerJPanel.remove(rightSideJPanel);
        containerJPanel.add(progressBarJPanel);
        validate();
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
     * Prepare for repaint, for example, adjust colors.
     */
    public void prepareForRepaint() {       
        final Color color;
        if ((!container.isLocalDraft()) && (!container.isLatest())) {
            color = Colors.Browser.List.LIST_LACK_MOST_RECENT_VERSION_FG;
        } else {
            color = Colors.Browser.List.LIST_FG;
        }
        containerNameJLabel.setForeground(color);
        containerDateJLabel.setForeground(color);
        draftOwnerJLabel.setForeground(color);
        
        if (!isExpanded() && !isSeen()) {
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
     * Set the container and its draft.
     * 
     * @param container
     *            A <code>Container</code>.
     * @param draft
     *            A <code>ContainerDraft</code>.
     */
    public void setContainerAndDraft(final Container container,
            final ContainerDraft draft) {
        this.container = container;
        this.draft = draft;

        containerNameJLabel.setText(container.getName());
        final Calendar now = DateUtil.getInstance();
        if (DateUtil.isSameDay(container.getUpdatedOn(), now)) {
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#setMouseOver(java.lang.Boolean)
     */
    @Override
    public void setMouseOver(Boolean mouseOver) {
        final Cursor cursor;
        super.setMouseOver(mouseOver);
        if (mouseOver) {
            cursor = new Cursor(Cursor.HAND_CURSOR);
        } else {
            cursor = null;
        }
        changeCursor(cursor, this);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#setProgressDetermination(java.lang.Integer)
     *
     */
    @Override
    public void setProgressDetermination(final Integer steps) {
        progressBar.setMinimum(1);
        progressBar.setMaximum(steps);
        progressBar.setIndeterminate(false);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#uninstallProgressBar()
     *
     */
    @Override
    public void uninstallProgressBar() {
        containerJPanel.remove(progressBarJPanel);
        containerJPanel.add(rightSideJPanel);
        validate();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#updateProgress(java.lang.Float, java.lang.String)
     *
     */
    @Override
    public void updateProgress(final Integer step, final String status) {
        progressBar.setValue(step);
        if (null != status && 0 < status.trim().length())
            progressBar.setString(status);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#formMousePressed(java.awt.event.MouseEvent)
     */
    @Override
    protected void formMousePressed(MouseEvent e) {
        super.formMousePressed(e);
    }
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#formMouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    protected void formMouseReleased(MouseEvent e) {
        // Perform selection first since this affects drawing. Drawing
        // must be completed before the popup opens because of the
        // shadow border on the popup.
        if (e.isPopupTrigger()) {
            model.selectContainer(container);
            if (!isFocusOwner()) {
                requestFocusInWindow();
            } 
        }   
        super.formMouseReleased(e);
    }
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (isExpanded()) {
            final Graphics g2 = g.create();
            try {
                GradientPainter.paintVertical(g2, getSize(),
                        Colors.Browser.List.LIST_GRADIENT_DARK,
                        Colors.Browser.List.LIST_GRADIENT_LIGHT);
                
                // Draw a border if the container is expanded, selected and focused
                if (isSelectedContainer() && (focusManager.getFocusList()==FocusManager.FocusList.CONTAINER)) {
                    g2.setColor(Colors.Browser.List.LIST_SELECTION_BORDER);
                    g2.drawLine(0, 0, getWidth()-1, 0);
                    g2.drawLine(0, 0, 0, getHeight()-1);
                    g2.drawLine(getWidth()-1, 0, getWidth()-1, getHeight()-1);
                }
            }
            finally { g2.dispose(); }
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
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#triggerSingleClick(java.awt.event.MouseEvent)
     */
    @Override
    protected void triggerSingleClick(final MouseEvent e) {
        if (isSetMouseOver()) {
            model.triggerExpand(this);
            if (!isFocusOwner()) {
                requestFocusInWindow();
            }
        }
    }
    /**
     * Change the cursor.
     */
    private void changeCursor(final Cursor cursor, final Component component) {
        component.setCursor(cursor);
        Window window = SwingUtilities.getWindowAncestor(component);
        if (null!=window) {
            window.setCursor(cursor);
        }
    }
    /**
     * Initialize bookmarks
     */
    private void initBookmarks() {
        iconJLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (container.isBookmarked()) {
                    ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER)).runRemoveContainerBookmark(container.getId());
                } else {
                    ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER)).runAddContainerBookmark(container.getId());
                }
            }
            @Override
            public void mouseEntered(final MouseEvent e) {
                setMouseOver(Boolean.TRUE);
                if (container.isBookmarked()) {
                    iconJLabel.setIcon(imageCache.read(TabPanelIcon.CONTAINER_BOOKMARK_ROLLOVER));
                } else {
                    iconJLabel.setIcon(imageCache.read(TabPanelIcon.CONTAINER_ROLLOVER));
                }
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                setMouseOver(Boolean.FALSE);
                if (container.isBookmarked()) {
                    iconJLabel.setIcon(imageCache.read(TabPanelIcon.CONTAINER_BOOKMARK));
                } else {
                    iconJLabel.setIcon(imageCache.read(TabPanelIcon.CONTAINER));
                }
            }           
        });
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        progressBarJPanel = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        westPaddingJLabel = new javax.swing.JLabel();
        iconJLabel = new javax.swing.JLabel();
        containerJPanel = new javax.swing.JPanel();
        containerNameJLabel = new javax.swing.JLabel();
        rightSideJPanel = new javax.swing.JPanel();
        containerDateJPanel = new javax.swing.JPanel();
        containerDateJLabel = new javax.swing.JLabel();
        draftOwnerJPanel = new javax.swing.JPanel();
        draftOwnerJLabel = new javax.swing.JLabel();
        eastPaddingJLabel = new javax.swing.JLabel();

        org.jdesktop.layout.GroupLayout progressBarJPanelLayout = new org.jdesktop.layout.GroupLayout(progressBarJPanel);
        progressBarJPanel.setLayout(progressBarJPanelLayout);
        progressBarJPanelLayout.setHorizontalGroup(
            progressBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(progressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
        );
        progressBarJPanelLayout.setVerticalGroup(
            progressBarJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(progressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

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

        rightSideJPanel.setLayout(new java.awt.GridLayout(1, 0));

        rightSideJPanel.setOpaque(false);
        containerDateJPanel.setLayout(new java.awt.GridBagLayout());

        containerDateJPanel.setOpaque(false);
        containerDateJLabel.setText("!Date!");
        containerDateJLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        containerDateJLabel.setMaximumSize(new java.awt.Dimension(500, 20));
        containerDateJLabel.setMinimumSize(new java.awt.Dimension(50, 20));
        containerDateJLabel.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        containerDateJPanel.add(containerDateJLabel, gridBagConstraints);

        rightSideJPanel.add(containerDateJPanel);

        draftOwnerJPanel.setLayout(new java.awt.GridBagLayout());

        draftOwnerJPanel.setOpaque(false);
        draftOwnerJLabel.setText("!Draft Owner!");
        draftOwnerJLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        draftOwnerJLabel.setMaximumSize(new java.awt.Dimension(500, 20));
        draftOwnerJLabel.setMinimumSize(new java.awt.Dimension(50, 20));
        draftOwnerJLabel.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        draftOwnerJPanel.add(draftOwnerJLabel, gridBagConstraints);

        rightSideJPanel.add(draftOwnerJPanel);

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
     * Determine if the container is selected.
     * 
     * @return True if the container is selected; false otherwise.
     */
    private Boolean isSelectedContainer() {
        return model.isSelectedContainer(container);
    }
}
