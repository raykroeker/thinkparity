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
import com.thinkparity.codebase.swing.GradientPainter;
import com.thinkparity.codebase.swing.border.BottomBorder;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.model.container.ContainerDraft;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionFactory;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.container.RemoveBookmark;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerPanel extends DefaultTabPanel {
    
    /** An add bookmark <code>AbstractAction</code>. */
    private static final AbstractAction ADD_BOOKMARK;
    
    /** The border for cells. */
    private static final Border BORDER_DEFAULT;

    /** A <code>FuzzyDateFormat</code>. */
    private static final FuzzyDateFormat FUZZY_DATE_FORMAT;
    
    /** A remove bookmark <code>AbstractAction</code>. */
    private static final AbstractAction REMOVE_BOOKMARK;
    
    static {
        ADD_BOOKMARK = ActionFactory.create(ActionId.CONTAINER_ADD_BOOKMARK);
        BORDER_DEFAULT = new BottomBorder(Colors.Browser.List.LIST_CONTAINERS_BORDER);
        FUZZY_DATE_FORMAT = new FuzzyDateFormat();
        REMOVE_BOOKMARK = getInstance(ActionId.CONTAINER_REMOVE_BOOKMARK);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel containerNameJLabel;
    private javax.swing.JLabel eastPaddingJLabel;
    private javax.swing.JLabel iconJLabel;
    private javax.swing.JLabel westPaddingJLabel;
    // End of variables declaration//GEN-END:variables
    
    /** An image cache. */
    protected final MainPanelImageCache imageCache;

    /** A <code>Container</code>. */
    protected Container container;

    /** A <code>ContainerDraft</code>. */
    protected ContainerDraft draft;

    /** The expanded <code>Boolean</code> state. */
    protected Boolean expanded;

    /** The latest version <code>ContainerVersion</code>. */
    protected ContainerVersion latestVersion;

    /** The panel localization. */
    private final MainCellL18n localization;

    /**
     * Create ContainerPanel.
     * 
     */
    public ContainerPanel() {
        super();
        this.expanded = Boolean.FALSE;
        this.imageCache = new MainPanelImageCache();
        this.localization = new MainCellL18n("ContainerPanel");
        initComponents();
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
     * Determine the expanded state.
     * 
     * @return A <code>Boolean</code> expanded state.
     */
    public Boolean isExpanded() {
        return expanded;
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
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     * 
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (expanded.booleanValue()) {
            final Graphics g2 = g.create();
            try {
                GradientPainter.paintVertical(g2, getSize(),
                        Colors.Browser.List.LIST_CONTAINER_GRADIENT_TOP,
                        Colors.Browser.List.LIST_CONTAINER_GRADIENT_BOTTOM);
            } finally {
                g2.dispose();
            }
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
        iconJLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (container.isBookmarked()) {
                    final Data data = new Data(1);
                    data.set(RemoveBookmark.DataKey.CONTAINER_ID, container.getId());
                    REMOVE_BOOKMARK.invoke(data);
                } else {
                    final Data data = new Data(1);
                    data.set(RemoveBookmark.DataKey.CONTAINER_ID, container.getId());
                    ADD_BOOKMARK.invoke(data);
                }
            }
            @Override
            public void mouseEntered(final MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
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
     * Reload the border.
     *
     */
    private void reloadBorder() {
        // if not expanded display a border
        if (expanded.booleanValue()) {
            setBorder(BorderFactory.createEmptyBorder());
        } else {
            setBorder(BORDER_DEFAULT);
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
        containerNameJLabel.setText(text);
    }

    private void setTextFont(final Font font) {
        containerNameJLabel.setFont(font);
    }

    private void setTextForeground(final Color foreground) {
        containerNameJLabel.setForeground(foreground);
    }

    private void setTextIcon(final ImageIcon icon) {
        iconJLabel.setIcon(icon);
    }
}
