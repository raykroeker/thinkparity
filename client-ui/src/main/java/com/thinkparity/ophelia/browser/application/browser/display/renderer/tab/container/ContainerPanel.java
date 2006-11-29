/*
 * Created On: October 6, 2006, 5:03 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.thinkparity.codebase.FuzzyDateFormat;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.swing.GradientPainter;
import com.thinkparity.codebase.swing.border.BottomBorder;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.model.container.ContainerDraft;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerModel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.application.browser.dnd.ImportTxHandler;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerPanel extends DefaultTabPanel {
    
    /** The border for cells. */
    private static final Border BORDER_DEFAULT;
    
    /** A <code>FuzzyDateFormat</code>. */
    private static final FuzzyDateFormat FUZZY_DATE_FORMAT;

    static {
        BORDER_DEFAULT = new BottomBorder(Colors.Browser.List.LIST_CONTAINERS_BORDER);
        FUZZY_DATE_FORMAT = new FuzzyDateFormat();
    }
    
    /** An image cache. */
    protected final MainPanelImageCache imageCache;
    
    /** The browser application. */
    private final Browser browser;
    
    /** A <code>Container</code>. */
    private Container container;
    
    /** A <code>ContainerDraft</code>. */
    private ContainerDraft draft;

    /** The focus manager. */
    private final FocusManager focusManager;

    /** The panel localization. */
    private final MainCellL18n localization;

    /** The container panel's model. */
    private final ContainerModel model;
    
    /**
     * Create ContainerPanel.
     * 
     */
    public ContainerPanel(final ContainerModel model) {
        super();
        this.model = model;
        this.browser = ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER));
        this.imageCache = new MainPanelImageCache();
        this.localization = new MainCellL18n("ContainerPanel");
        this.focusManager = new FocusManager();
        focusManager.addFocusListener(this, model);
        initComponents();
        initBookmarks();
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
     * Determine whether or not the container has been seen.
     * 
     * @return True if the container has been seen; false otherwise.
     */
    public Boolean isSeen() {
        return container.contains(ArtifactFlag.SEEN);
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
    public void setPanelData(final Container container,
            final ContainerDraft draft, final ContainerVersion latestVersion) {
        this.container = container;
        this.draft = draft;
        setTransferHandler(new ImportTxHandler(browser, model, container));
        iconJLabel.setIcon(container.isBookmarked() ?
                imageCache.read(TabPanelIcon.CONTAINER_BOOKMARK) :
                imageCache.read(TabPanelIcon.CONTAINER));
        final StringBuffer text = new StringBuffer();
        // if expanded display the container name
        // if not expanded display the container name; if there exists a draft
        // and the the latest version also display the draft owner; otherwise
        // if there exists the latest version display the published on date
        if (isExpanded()) {
            text.append(container.getName());
        } else {
            text.append(container.getName())
                .append(Separator.DoubleSpace);
            if (container.isDraft() && container.isLatest()) {
                text.append(localization.getString("ContainerMessageDraftOwner",
                        draft.getOwner().getName()));
            } else if (null != latestVersion) {
                text.append(localization.getString(
                        "ContainerMessagePublishDate",
                        FUZZY_DATE_FORMAT.format(latestVersion.getUpdatedOn())));
            }
        }
        containerNameJLabel.setText(text.toString());
        if (!isExpanded() && !isSeen()) {
            containerNameJLabel.setFont(Fonts.DefaultFontBold);
        }
        if (!container.isLocalDraft() && !container.isLatest()) {
            containerNameJLabel.setForeground(
                    Colors.Browser.List.LIST_LACK_MOST_RECENT_VERSION_FG);
        }
        // if not expanded display a border
        if (!isExpanded()) {
            setBorder(BORDER_DEFAULT);
        }
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
                        Colors.Browser.List.LIST_CONTAINER_GRADIENT_TOP,
                        Colors.Browser.List.LIST_CONTAINER_GRADIENT_BOTTOM);
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
        model.triggerExpand(this);
        if (!isFocusOwner()) {
            requestFocusInWindow();
        }
    }

    /**
     * Change the cursor.
     * 
     */
    private void changeCursor(final Cursor cursor, final Component component) {
        component.setCursor(cursor);
        Window window = SwingUtilities.getWindowAncestor(component);
        if (null!=window) {
            window.setCursor(cursor);
        }
    }
    /**
     * Initialize the bookmark label's mouse listeners.
     * 
     */
    private void initBookmarks() {
        iconJLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (container.isBookmarked()) {
                    browser.runRemoveContainerBookmark(container.getId());
                } else {
                    browser.runAddContainerBookmark(container.getId());
                }
            }
            @Override
            public void mouseEntered(final MouseEvent e) {
                final Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
                changeCursor(cursor, ContainerPanel.this);
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                changeCursor(null, ContainerPanel.this);
            }           
        });
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        westPaddingJLabel = new javax.swing.JLabel();
        iconJLabel = new javax.swing.JLabel();
        containerNameJLabel = new javax.swing.JLabel();
        eastPaddingJLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setBackground(Colors.Browser.List.LIST_CONTAINERS_BACKGROUND);
        setMaximumSize(new java.awt.Dimension(32767, 23));
        setMinimumSize(new java.awt.Dimension(120, 23));
        setPreferredSize(new java.awt.Dimension(120, 23));
        westPaddingJLabel.setFocusable(false);
        westPaddingJLabel.setMaximumSize(new java.awt.Dimension(12, 20));
        westPaddingJLabel.setMinimumSize(new java.awt.Dimension(12, 20));
        westPaddingJLabel.setPreferredSize(new java.awt.Dimension(12, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(westPaddingJLabel, gridBagConstraints);

        iconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconContainer.png")));
        iconJLabel.setMaximumSize(new java.awt.Dimension(16, 16));
        iconJLabel.setMinimumSize(new java.awt.Dimension(16, 16));
        iconJLabel.setPreferredSize(new java.awt.Dimension(16, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        add(iconJLabel, gridBagConstraints);

        containerNameJLabel.setText("!Package!");
        containerNameJLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        containerNameJLabel.setMaximumSize(new java.awt.Dimension(500, 24));
        containerNameJLabel.setMinimumSize(new java.awt.Dimension(100, 24));
        containerNameJLabel.setPreferredSize(new java.awt.Dimension(100, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(containerNameJLabel, gridBagConstraints);

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel containerNameJLabel;
    private javax.swing.JLabel eastPaddingJLabel;
    private javax.swing.JLabel iconJLabel;
    private javax.swing.JLabel westPaddingJLabel;
    // End of variables declaration//GEN-END:variables
}
