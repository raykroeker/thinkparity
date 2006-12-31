/*
 * Created On:  December 12, 2006, 6:35 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.AbstractJPanel;
import com.thinkparity.codebase.swing.border.DropShadowBorder;
import com.thinkparity.codebase.swing.border.TopBorder;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;

/**
 * <b>Title:</b>thinkParity Tab Avatar Sort By Panel<br>
 * <b>Description:</b>A panel used to contain the sort by options.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class TabAvatarSortByPanel extends AbstractJPanel {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JPanel sortByJPanel = new javax.swing.JPanel();
    // End of variables declaration//GEN-END:variables

    /** A <code>DropShadowBorder</code>. */
    private final DropShadowBorder border;

    /** The menu item <code>GridBagConstraints</code>. */
    private final GridBagConstraints constraints;

    /** The menu item icon <code>GridBagConstraints</code>. */
    private final GridBagConstraints iconConstraints;

    /** The menu item icon <code>GridBagConstraints</code>. */
    private final GridBagConstraints panelConstraints;

    /** The <code>TabAvatarSortByDelegate</code>. */
    private TabAvatarSortByDelegate delegate;

    /** The <code>Component</code> invoker. */
    private Component invoker;

    /** The original <Code>JFrame</code> glass pane. */
    private Component jFrameGlassPane;

    /** The x, y coordinates to show the panel at. */
    private int x, y;
    
    /** An image cache. */
    private static final MainPanelImageCache IMAGE_CACHE;
    
    static {
        IMAGE_CACHE = new MainPanelImageCache();
    }

    /**
     * Create TabAvatarSortByPanel.
     * 
     */
    public TabAvatarSortByPanel() {
        super();
        try {
            border = new DropShadowBorder(Colors.Swing.MENU_BG);
        } catch (final AWTException awtx) {
            throw new BrowserException("", awtx);
        }
        this.panelConstraints = new GridBagConstraints();
        this.panelConstraints.fill = GridBagConstraints.BOTH;
        this.panelConstraints.gridx = this.panelConstraints.gridy = 0;
        this.panelConstraints.ipady = 4;
        this.panelConstraints.weightx = this.panelConstraints.weighty = 1.0F;

        this.constraints = new GridBagConstraints();
        this.constraints.fill = GridBagConstraints.BOTH;
        this.constraints.insets.left = 10;
        this.constraints.weightx = 1.0F;

        this.iconConstraints = new GridBagConstraints();
        this.iconConstraints.insets.right = 6;

        initComponents();
    }

    /**
     * Set the delegate.
     * 
     * @param delegate
     *            A <code>TabAvatarSortByDelegate</code>.
     */
    void setDelegate(final TabAvatarSortByDelegate delegate) {
        this.delegate = delegate;
    }

    /**
     * Show the sort by panel.
     * 
     * @param invoker
     *            The invoker <code>Component</code>.
     * @param x
     *            The x coordinaaate to show the panel at.
     * @param y
     *            The y coordinaaate to show the panel at.
     */
    void show(final Component invoker, final int x, final int y) {
        this.invoker = invoker;
        this.x = x;
        this.y = y;
        doShow();
    }

    /**
     * Add a sort by to the display panel.
     * 
     * @param index
     *            An index within the panel to add to.
     * @param sortBy
     *            The sort by to add.
     */
    private void addSortBy(final int index, final TabAvatarSortBy sortBy) {
        final JLabel jLabel = LabelFactory.create(sortBy.getText(),
                BrowserConstants.Fonts.DefaultFont);
        final JPanel jPanel = new AbstractJPanel() {};
        jPanel.setLayout(new GridBagLayout());
        
        final JLabel iconJLabel = LabelFactory.create(getIcon(sortBy));
        if (0 < index) {
            jPanel.setBorder(new TopBorder(Colors.Swing.MENU_BETWEEN_ITEMS_BG));
        }

        final MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                sortBy.getAction().actionPerformed(
                        new ActionEvent(e.getSource(), e.getID(),
                                "SortBy", e.getWhen(), e.getModifiers()));
                reload();
            }
        };
        jPanel.addMouseListener(mouseListener);
        jPanel.add(jLabel, constraints.clone());
        jPanel.add(iconJLabel, iconConstraints.clone());

        panelConstraints.gridy++;
        sortByJPanel.add(jPanel, panelConstraints.clone());
    }
    
    /**
     * Obtain an icon for an ordering.
     * 
     * @param ordering
     *            An <code>Ordering</code>.
     * @return An <code>Icon</code>.
     */
    private Icon getIcon(final TabAvatarSortBy sortBy) {
        final Icon icon;
        switch (sortBy.getDirection()) {
        case ASCENDING:
            icon = IMAGE_CACHE.read(TabPanelIcon.SORT_ASCENDING);
            break;
        case DESCENDING:
            icon = IMAGE_CACHE.read(TabPanelIcon.SORT_DESCENDING);
            break;
        case NONE:
            icon = IMAGE_CACHE.read(TabPanelIcon.SORT_NONE);
            break;
        default:
            throw Assert.createUnreachable("UNKNOWN SORT DIRECTION");
        }
        return icon;
    }

    /**
     * Reload the sort by labels.
     *
     */
    private void reload() {
        final List<TabAvatarSortBy> sortBy = delegate.getSortBy();
        for (int i = 0; i < sortBy.size(); i++) {
            ((JLabel) ((JPanel) sortByJPanel.getComponent(i)).getComponent(0)).setText(sortBy.get(i).getText());
            ((JLabel) ((JPanel) sortByJPanel.getComponent(i)).getComponent(1)).setIcon(getIcon(sortBy.get(i)));
        }
        sortByJPanel.revalidate();
    }

    /**
     * Show the panel. Add the appropriate sort by elements from the delegate to
     * the sortByJPanel; set the location and size and set this panel as the
     * root glass pane. If the user clicks outside the bounds of the
     * sortByJPanel the menu is destroyed.
     * 
     */
    private void doShow() {
        final List<TabAvatarSortBy> sortBy = delegate.getSortBy();
        for (int i = 0; i < sortBy.size(); i++) {
            addSortBy(i, sortBy.get(i));
        }
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                if (!sortByJPanel.contains(e.getPoint()))
                    uninstall();
            }
        });
        final JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor(invoker);
        jFrameGlassPane = jFrame.getRootPane().getGlassPane();
        jFrame.getRootPane().setGlassPane(this);

        final Point location = SwingUtilities.convertPoint(invoker, x, y, jFrame);
        add(sortByJPanel);
        final Dimension size = sortByJPanel.getPreferredSize();
        /* HACK the drop shadow has hard-coded sizes and doesn't auto-adjust for
         * the component.  the +4 in for the width/height is for that  the -3
         * is to keep off the right edge of the invoker. */
        size.width = invoker.getWidth() - location.x - 3 + 4;
        size.height += 4;
        sortByJPanel.setSize(size);
        location.x--;
        sortByJPanel.setLocation(location);
        sortByJPanel.setBorder(border);
        border.paintUnderneathBorder(invoker, location.x, location.y,
                size.width, size.height);
        setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        sortByJPanel.setLayout(new java.awt.GridBagLayout());

        sortByJPanel.setBackground(Colors.Swing.MENU_BG);

        setLayout(null);

        setOpaque(false);
    }// </editor-fold>//GEN-END:initComponents
    
    
    private void uninstall() {
        setVisible(false);
        final JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor(invoker);
        jFrame.getRootPane().setGlassPane(jFrameGlassPane);
    }
}
