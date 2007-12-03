/*
 * Created On: 6-Oct-06 2:12:01 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import com.thinkparity.codebase.FuzzyDateFormat;
import com.thinkparity.codebase.JVMUniqueId;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.swing.AbstractJPanel;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.border.BottomBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserSession;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell;

/**
 * <b>Title:</b>thinkParity Default Tab Panel<br>
 * <b>Description:</b>A default implementation of a tab panel.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class DefaultTabPanel extends AbstractJPanel implements
        TabPanel {

    /** The number of pixels to adjust the height of the panel by when animating. */
    protected static final int ANIMATION_HEIGHT_ADJUSTMENT;

    /** The maximum height of a panel. */
    protected static final int ANIMATION_MAXIMUM_HEIGHT;

    /** The minimum height of a panel. */
    protected static final int ANIMATION_MINIMUM_HEIGHT;

	/** The collapsed panel <code>Border</code>. */
    protected static final Border BORDER_COLLAPSED;

    /** The expanded panel <code>Border</code>. */
    protected static final Border BORDER_EXPANDED;

    /** An image cache. */
    protected static final MainPanelImageCache IMAGE_CACHE;

    /** A keyboard popup y location <code>int</code>. */
    protected static final int KEYBOARD_POPUP_Y;

    /** A <code>FuzzyDateFormat</code>. */
    private static final FuzzyDateFormat FUZZY_DATE_FORMAT;

    static {
        ANIMATION_HEIGHT_ADJUSTMENT = 12;
        ANIMATION_MAXIMUM_HEIGHT = 170;
        ANIMATION_MINIMUM_HEIGHT = 25;
        BORDER_COLLAPSED = new BottomBorder(Colors.Browser.Panel.PANEL_COLLAPSED_BORDER);
        BORDER_EXPANDED = new BottomBorder(Colors.Browser.Panel.PANEL_EXPANDED_BORDER);
        FUZZY_DATE_FORMAT = new FuzzyDateFormat();
        IMAGE_CACHE = new MainPanelImageCache();
        KEYBOARD_POPUP_Y = 12;
    }

    /**
     * Format a calendar as a fuzzy date.
     * 
     * @param calendar
     *            A <code>Calendar</code>.
     * @return A formatted date <code>String</code>.
     */
    protected static String formatFuzzy(final Calendar calendar) {
        return FUZZY_DATE_FORMAT.format(calendar);
    }

    /** The tab panel's <code.TabAnimator</code>. */
    protected final TabAnimator animator;

    /**
     * The collapsed and expanded
     * <code>JPanel</code> <code>GridBagConstraints</code>.
     */
    protected final GridBagConstraints constraints;

    /** The tab panel's <code>TabRenderer</code>. */
    protected final TabRenderer renderer;

    /** The selected <code>Boolean</code> state. */
    protected boolean selected;

    /** A <code>TabDelegate</code>. */
    protected TabDelegate tabDelegate;

    /** The panel's animating state. */
    private boolean animating;

    /** The panel's expanded state. */
    private boolean expanded;

    /** A panel's <code>JVMUniqueId</code>. */
	private final JVMUniqueId id;

    /** A <code>BrowserSession</code>. */
    private final BrowserSession session;

    /**
	 * Create DefaultTabPanel.
	 *
	 */
	public DefaultTabPanel(final BrowserSession session) {
		super();
        this.animator = new TabAnimator(this);
        this.constraints = new GridBagConstraints();
        this.constraints.fill = GridBagConstraints.BOTH;
        this.constraints.weightx = this.constraints.weighty = 1.0F;
        this.constraints.insets = new java.awt.Insets(0, 5, 0, 5);
        this.renderer = new TabRenderer();
		this.id = JVMUniqueId.nextId();
        this.session = session;
        bindKeys();
        addPopupListeners();
	}

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     * 
     */
    @Override
    public boolean equals(final Object obj) {
        if (null == obj)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof DefaultTabPanel) {
            return ((DefaultTabPanel) obj).getId().equals(getId());
        } else {
            return false;
        }
    }

    /**
     * Handle a mouse press on the expand/collapse icon.
     * 
     * @param e
     *          A <code>MouseEvent</code>.
     */
    public void expandIconMousePressed(final java.awt.event.MouseEvent e) {
    }

    /**
     * Obtain a session attribute.
     * 
     * @param name
     *            An attribute name <code>String</code>.
     * @return An attribute value <code>Object</code> or null if no such
     *         attribute exists.
     */
    public final Object getAttribute(final String name) {
        return session.getAttribute(name);
    }

    /**
	 * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#getId()
     * 
	 */
	public Object getId() {
		return id;
	}

    /**
     * Obtain the tab delegate.
     * 
     * @return A <code>TabDelegate</code>.
     */
    public TabDelegate getTabDelegate() {
        return tabDelegate;
    }

    /**
     * @see java.lang.Object#hashCode()
     * 
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#isAnimating()
     */
    public Boolean isAnimating() {
        return Boolean.valueOf(animating);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#isExpanded()
     */
    public Boolean isExpanded() {
        return Boolean.valueOf(expanded);
    }

    /**
     * Handle a mouse press on a panel cell.
     * 
     * @param cell
     *          A <code>Cell</code>.
     * @param panelLocation
     *          A <code>PanelLocation</code> indicating where the mouse was pressed.        
     * @param e
     *          A <code>MouseEvent</code>.
     */
    public void panelCellMousePressed(final Cell cell,
            final PanelLocation panelLocation, final java.awt.event.MouseEvent e) {        
    }

    /**
     * Handle a change of selection in the panel cell.
     * 
     * @param cell
     *          A <code>Cell</code>.
     */
    public void panelCellSelectionChanged(final Cell cell) {
    }

    /**
     * Remove a session attribute.
     * 
     * @param name
     *            An attribute name <code>String</code>.
     */
    public final void removeAttribute(final String name) {
        session.removeAttribute(name);
    }

    /**
     * Set a session attribute.
     * 
     * @param name
     *            An attribute name <code>String</code>.
     * @param value
     *            An attribute value <code>Object</code>.
     */
    public final void setAttribute(final String name, final Object value) {
        session.setAttribute(name, value);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#setExpanded(java.lang.Boolean)
     */
    public void setExpanded(final Boolean expanded) {
        if (expanded.booleanValue()) {
            expand(false);
        } else {
            collapse(false);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#setSelected(java.lang.Boolean)
     */
    public void setSelected(final Boolean selected) {
        this.selected = selected;
        repaint();
    }

    /**
     * Set the tab delegate.
     * 
     * @param tabDelegate
     *            A <code>TabDelegate</code>.
     */
    public void setTabDelegate(final TabDelegate tabDelegate) {
        this.tabDelegate = tabDelegate;
    }

    /**
     * Add popup listeners. This is to show a popup when shift-f10 is pressed.
     */
    protected void addPopupListeners() {
    }

    /**
     * Adjust the border color.
     * 
     * @param expanded
     *            The expanded <code>Boolean</code>.
     */
    protected void adjustBorderColor(final Boolean expanded) {
        if (!expanded) {
            if (tabDelegate.isNextPanelExpanded(this)) {
                ((BottomBorder)BORDER_COLLAPSED).setColor(Colors.Browser.Panel.PANEL_EXPANDED_BORDER);
            } else {
                ((BottomBorder)BORDER_COLLAPSED).setColor(Colors.Browser.Panel.PANEL_COLLAPSED_BORDER);
            }
        }
    }

    /**
     * Bind or unbind expanded panel key bindings.
     * 
     * When expanded, the keys are bound to actions. When collapsed,
     * the key binding is removed so it can be interpreted elsewhere.
     * 
     * @param expanded
     *            The expanded <code>Boolean</code>.
     */
    protected void bindExpandedKeys(final Boolean expanded) {
    }

    /**
     * Bind keys to actions.
     */
    protected void bindKeys() {
        bindExpandedKeys(isExpanded());

        // plus to expand
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.SHIFT_DOWN_MASK),
            new AbstractAction() {
                public void actionPerformed(final ActionEvent e) {
                    if (!isExpanded() && isExpandable()) {
                        tabDelegate.toggleExpansion(DefaultTabPanel.this);
                    }
                }
            });
        // minus to collapse
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0),
            new AbstractAction() {
                public void actionPerformed(final ActionEvent e) {
                    if (isExpanded()) {
                        tabDelegate.toggleExpansion(DefaultTabPanel.this);
                    }
                }
            });
        // enter to expand
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                new AbstractAction() {
                    public void actionPerformed(final ActionEvent e) {
                        if (isExpanded()) {
                            invokeAction();
                        } else if (isExpandable()) {
                            tabDelegate.toggleExpansion(DefaultTabPanel.this);
                        }
                    }
                });
        // escape to collapse
        bindKey(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            new AbstractAction() {
                public void actionPerformed(final ActionEvent e) {
                    if (isExpanded()) {
                        tabDelegate.toggleExpansion(DefaultTabPanel.this);
                    }
                }
            });
    }

    /**
     * Collapse the panel.
     * 
     * @param animate
     *            Whether or not to animate.
     * @param collapsedJPanel
     *            The collapsed <code>javax.swing.JPanel</code>.
     * @param expandedJPanel
     *            The expanded <code>javax.swing.JPanel</code>.
     */
    protected void doCollapse(final boolean animate,
            final javax.swing.JPanel collapsedJPanel,
            final javax.swing.JPanel expandedJPanel) {
        final boolean requestFocus = hasFocus();
        if (animate) {
            animating = true;
            final Dimension expandedPreferredSize = getPreferredSize();
            expandedPreferredSize.height = ANIMATION_MAXIMUM_HEIGHT;
            setPreferredSize(expandedPreferredSize);
            animator.collapse(ANIMATION_HEIGHT_ADJUSTMENT,
                    ANIMATION_MINIMUM_HEIGHT, new Runnable() {
                        public void run() {
                            animating = false;
                            expanded = false;

                            if (isAncestorOf(expandedJPanel))
                                remove(expandedJPanel);
                            if (isAncestorOf(collapsedJPanel))
                                remove(collapsedJPanel);
                            add(collapsedJPanel, constraints.clone());
                            
                            revalidate();
                            if (requestFocus) {
                                requestFocusInWindow();
                            }
                            repaint();
                            firePropertyChange("expanded", !expanded, expanded);
                        }
            });
        } else {
            expanded = false;

            if (isAncestorOf(expandedJPanel))
                remove(expandedJPanel);
            if (isAncestorOf(collapsedJPanel))
                remove(collapsedJPanel);
            add(collapsedJPanel, constraints.clone());
            
            final Dimension preferredSize = getPreferredSize();
            preferredSize.height = ANIMATION_MINIMUM_HEIGHT;
            setPreferredSize(preferredSize);

            revalidate();
            if (requestFocus) {
                requestFocusInWindow();
            }
            repaint();
            firePropertyChange("expanded", !expanded, expanded);
        }
        bindExpandedKeys(Boolean.FALSE);
    }

    /**
     * Expand the panel.
     * 
     * @param animate
     *            Whether or not to animate.
     * @param collapsedJPanel
     *            The collapsed <code>javax.swing.JPanel</code>.
     * @param expandedJPanel
     *            The expanded <code>javax.swing.JPanel</code>.
     */
    protected void doExpand(final boolean animate,
            final javax.swing.JPanel collapsedJPanel,
            final javax.swing.JPanel expandedJPanel) {
        // The animate flag has to be set before panels are removed and added.
        if (animate) {
            animating = true;
        }
        final boolean requestFocus = hasFocus();
        SwingUtil.setCursor(collapsedJPanel, null);
        if (isAncestorOf(expandedJPanel))
            remove(expandedJPanel);
        if (isAncestorOf(collapsedJPanel))
            remove(collapsedJPanel);
        add(expandedJPanel, constraints.clone());

        if (animate) {
            final Dimension preferredSize = getPreferredSize();
            preferredSize.height = ANIMATION_MINIMUM_HEIGHT;
            setPreferredSize(preferredSize);
            animator.expand(ANIMATION_HEIGHT_ADJUSTMENT,
                    ANIMATION_MAXIMUM_HEIGHT, new Runnable() {
                        public void run() {
                            expanded = true;
                            animating = false;

                            revalidate();
                            if (requestFocus) {
                                requestFocusInWindow();
                            }
                            repaint();
                            firePropertyChange("expanded", !expanded, expanded);
                        }
            });
        } else {
            expanded = true;
            final Dimension preferredSize = getPreferredSize();
            preferredSize.height = ANIMATION_MAXIMUM_HEIGHT;
            setPreferredSize(preferredSize);

            revalidate();
            if (requestFocus) {
                requestFocusInWindow();
            }
            repaint();
            firePropertyChange("expanded", !expanded, expanded);
        }
        bindExpandedKeys(Boolean.TRUE);
    }

    /**
     * Invoke the action as appropriate for the current focus.
     */
    protected void invokeAction() {
        if (!isExpanded() && isExpandable()) {
            tabDelegate.toggleExpansion(DefaultTabPanel.this);
        }
    }

    /**
     * Determine if the panel is expandable.
     * 
     * @return True if the panel is expandable.
     */
    protected Boolean isExpandable() {
        return Boolean.TRUE;
    }

    /**
     * Read an icon.
     * 
     * @param tabPanelIcon
     *            A <code>TabPanelIcon</code>.
     * @return An <code>Icon</code>.
     */
    protected Icon readIcon(final TabPanelIcon tabPanelIcon) {
        return IMAGE_CACHE.read(tabPanelIcon);
    }

    /**
     * Reload a display label.
     * 
     * @param jLabel
     *            A swing <code>JLabel</code>.
     * @param value
     *            The label value.
     */
    protected void reload(final javax.swing.JLabel jLabel,
            final String value) {
        jLabel.setText(null == value ? Separator.Space.toString() : value);
    }

    public enum PanelLocation { BODY, EXPAND_ICON, ICON } 
}
