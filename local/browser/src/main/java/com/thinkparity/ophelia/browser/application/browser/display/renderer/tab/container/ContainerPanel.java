/*
 * Created On: October 6, 2006, 5:03 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;

import com.thinkparity.codebase.FuzzyDateFormat;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.swing.border.BottomBorder;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.model.container.ContainerDraft;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerPanel extends DefaultTabPanel {
    
    /** The border for cells. */
    static final Border BORDER;

    /** A <code>FuzzyDateFormat</code>. */
    private static final FuzzyDateFormat FUZZY_DATE_FORMAT;

    static {
        BORDER = new BottomBorder(Colors.Browser.List.LIST_CONTAINERS_BORDER);
        FUZZY_DATE_FORMAT = new FuzzyDateFormat();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel iconJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel nameJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables

    /** A <code>Container</code>. */
    protected Container container;

    /** A <code>ContainerDraft</code>. */
    protected ContainerDraft draft;

    /** The expanded <code>Boolean</code> state. */
    protected Boolean expanded;

    /** An image cache. */
    protected final MainPanelImageCache imageCache;

    /** The latest version <code>ContainerVersion</code>. */
    protected ContainerVersion latestVersion;

    /** The container tab's <code>DefaultActionDelegate</code>. */
    private ActionDelegate actionDelegate;

    /** The panel localization. */
    private final MainCellL18n localization;

    /** The container tab's <code>PopupDelegate</code>. */
    private PopupDelegate popupDelegate;

    /** A <code>BackgroundRenderer</code>. */
    private final BackgroundRenderer renderer;

    /**
     * Create ContainerPanel.
     * 
     */
    public ContainerPanel() {
        super();
        this.expanded = Boolean.FALSE;
        this.imageCache = new MainPanelImageCache();
        this.localization = new MainCellL18n("ContainerPanel");
        this.renderer = new BackgroundRenderer();
        initComponents();
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
     * Obtain the latest version.
     * 
     * @return A <code>ContainerVersion</code>.
     */
    public ContainerVersion getLatestVersion() {
        return latestVersion;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#getPanelPopupDelegate()
     *
     */
    public TabPanelPopupDelegate getPanelPopupDelegate() {
        return popupDelegate;
    }

    /**
     * Obtain popupDelegate.
     *
     * @return A ContainerTabPopupDelegate.
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
        return expanded;
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
        this.expanded = expanded;
        reloadText();
        reloadBorder();
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
        this.latestVersion = latestVersion;
        setTextIcon(container.isBookmarked() ?
                imageCache.read(TabPanelIcon.CONTAINER_BOOKMARK) :
                imageCache.read(TabPanelIcon.CONTAINER));
        reloadBorder();
        reloadText();
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
        if (expanded.booleanValue()) {
            renderer.paintExpandedBackground(g, this);
        } else {
            renderer.paintBackground(g, this);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        final javax.swing.JLabel paddingJLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        iconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconContainer.png")));
        iconJLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                actionDelegate.invokeForContainer(container);
            }
            @Override
            public void mouseEntered(final MouseEvent e) {
                SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), Cursor.HAND_CURSOR);
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), Cursor.DEFAULT_CURSOR);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 4);
        add(iconJLabel, gridBagConstraints);

        nameJLabel.setText("!Package!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        add(nameJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(11, 0, 12, 0);
        add(paddingJLabel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    /**
     * Reload the border.
     *
     */
    private void reloadBorder() {
        // if not expanded display a border
        if (expanded.booleanValue()) {
            setBorder(BorderFactory.createEmptyBorder());
        } else {
            setBorder(BORDER);
        }        
    }

    /**
     * Reload the text on the panel.
     *
     */
    private void reloadText() {
        final StringBuffer text = new StringBuffer();
        // if expanded display the container name
        // if not expanded display the container name; if there exists a draft
        // and the the latest version also display the draft owner; otherwise
        // if there exists the latest version display the published on date
        if (expanded.booleanValue()) {
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
        setText(text.toString());
        if (!expanded && !container.contains(ArtifactFlag.SEEN)) {
            setTextFont(Fonts.DefaultFontBold);
        }
        if (!container.isLocalDraft() && !container.isLatest()) {
            setTextForeground(Colors.Browser.List.LIST_LACK_MOST_RECENT_VERSION_FG);
        }
    }

    private void setText(final String text) {
        nameJLabel.setText(text);
    }

    private void setTextFont(final Font font) {
        nameJLabel.setFont(font);
    }

    private void setTextForeground(final Color foreground) {
        nameJLabel.setForeground(foreground);
    }

    private void setTextIcon(final ImageIcon icon) {
        iconJLabel.setIcon(icon);
    }
}
