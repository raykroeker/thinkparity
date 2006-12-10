/*
 * Created On: 6-Oct-06 2:12:01 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.text.MessageFormat;
import java.util.Calendar;

import javax.swing.DefaultListModel;
import javax.swing.border.Border;

import com.thinkparity.codebase.FuzzyDateFormat;
import com.thinkparity.codebase.JVMUniqueId;
import com.thinkparity.codebase.swing.AbstractJPanel;
import com.thinkparity.codebase.swing.SwingUtil;
import com.thinkparity.codebase.swing.border.BottomBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserSession;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel.Cell;

/**
 * <b>Title:</b>thinkParity Default Tab Panel<br>
 * <b>Description:</b>A default implementation of a tab panel.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class DefaultTabPanel extends AbstractJPanel implements TabPanel {

	/** The panel <code>Border</code>. */
    protected static final Border BORDER;

    /** An image cache. */
    protected static final MainPanelImageCache IMAGE_CACHE;

    /** The number of rows in the version panel. */
    protected static final int NUMBER_VISIBLE_ROWS;

    /** A <code>FuzzyDateFormat</code>. */
    private static final FuzzyDateFormat FUZZY_DATE_FORMAT;

    /** A session key pattern for the list's selected index. */
    private static final String SK_LIST_SELECTED_INDEX_PATTERN;

    static {
        BORDER = new BottomBorder(Colors.Browser.List.LIST_CONTAINERS_BORDER);
        FUZZY_DATE_FORMAT = new FuzzyDateFormat();
        IMAGE_CACHE = new MainPanelImageCache();
        NUMBER_VISIBLE_ROWS = 6;
        SK_LIST_SELECTED_INDEX_PATTERN =
            "TabPanel#JList.getSelectedIndex({0}:{1})";
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

    /** A <code>TabDelegate</code>. */
    protected TabDelegate tabDelegate;

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
        this.animator = new TabAnimator(this, 12);
        this.constraints = new GridBagConstraints();
        this.constraints.fill = GridBagConstraints.BOTH;
        this.constraints.weightx = this.constraints.weighty = 1.0F;
        this.renderer = new TabRenderer();
		this.id = JVMUniqueId.nextId();
        this.session = session;
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
     * Set the tab delegate.
     * 
     * @param tabDelegate
     *            A <code>TabDelegate</code>.
     */
    public void setTabDelegate(final TabDelegate tabDelegate) {
        this.tabDelegate = tabDelegate;
    }

    /**
     * Handle the focus gained event on the list.
     * 
     * @param jList
     *            A <code>JList</code>.
     * @param e
     *            A <code>FocusEvent</code>.
     */
    protected void jListFocusGained(final javax.swing.JList jList, final java.awt.event.FocusEvent e) {
        repaintLists();
    }

    /**
     * Handle the focus lost event on the list.
     * 
     * @param jList
     *            A <code>JList</code>.
     * @param e
     *            A <code>FocusEvent</code>.
     */
    protected void jListFocusLost(final javax.swing.JList jList, final java.awt.event.FocusEvent e) {
        repaintLists();
    }

    /**
     * Handle the click event for the given list. All we do is check for a
     * double-click event to run a specific action.
     * 
     * @param jList
     *            A <code>JList</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    protected void jListMouseClicked(final javax.swing.JList jList, final java.awt.event.MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        if (jList.isSelectionEmpty()) {
            return;
        } else {
            /* we are using a single-click paradigm similar to clicking on links to
             * invoke actions */
            ((Cell) jList.getSelectedValue()).invokeAction();
        }
    }

    /**
     * Handle the mouse entered event for the given list.
     * 
     * @param jList
     *            A <code>JList</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    protected void jListMouseEntered(final DefaultListModel listModel,
            final javax.swing.JList jList, final java.awt.event.MouseEvent e) {
        jListSetCursor(listModel, jList, e);
    }

    /**
     * Handle the mouse exited event of the given list. All we do is update the
     * cursor.
     * 
     * @param jList
     *            A <code>JList</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    protected void jListMouseExited(final javax.swing.JList jList,
            final java.awt.event.MouseEvent e) {
        SwingUtil.setCursor(jList, java.awt.Cursor.DEFAULT_CURSOR);
    }

    /**
     * Handle the mouse moved event for the given list.
     * 
     * @param listModel
     *            A <code>DefaultListModel</code>.
     * @param jList
     *            A <code>JList</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    protected void jListMouseMoved(final DefaultListModel listModel,
            final javax.swing.JList jList, final java.awt.event.MouseEvent e) {
        jListSetCursor(listModel, jList, e);
    }

    /**
     * Handle the mouse pressed event for the given list.
     * 
     * @param jList
     *            A <code>JList</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    protected void jListMousePressed(final javax.swing.JList jList, final java.awt.event.MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.isPopupTrigger()) {
            jList.setSelectedIndex(jList.locationToIndex(e.getPoint()));
            jList.getParent().repaint();
            getPopupDelegate().initialize((Component) e.getSource(), e.getX(), e.getY());
            ((Cell) jList.getSelectedValue()).showPopup();
        }
    }

    /**
     * Handle the mouse released event for the given list.
     * 
     * @param jList
     *            A <code>JList</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    protected void jListMouseReleased(final javax.swing.JList jList, final java.awt.event.MouseEvent e) {
        logger.logApiId();
        logger.logVariable("e", e);
        if (e.isPopupTrigger()) {
            jList.setSelectedIndex(jList.locationToIndex(e.getPoint()));
            jList.getParent().repaint();
            getPopupDelegate().initialize((Component) e.getSource(), e.getX(), e.getY());
            ((Cell) jList.getSelectedValue()).showPopup();
        }
    }

    /**
     * Repaint the lists.
     *
     */
    protected abstract void repaintLists();

    /**
     * Restore the saved selection for the list. If there is no saved selection
     * then the first index is used.
     * 
     * @param keyPattern
     *            A session attribute key pattern.
     * @param listModel
     *            A <code>DefaultListModel</code>.
     * @param jList
     *            The <code>JList</code>.
     */
    protected final void restoreSelection(final String listKey,
            final DefaultListModel listModel, final javax.swing.JList jList) {
        final Integer selectedIndex =
            (Integer) session.getAttribute(MessageFormat.format(
                    SK_LIST_SELECTED_INDEX_PATTERN, getId(), listKey));
        if (null == selectedIndex) {
            if (listModel.isEmpty()) {
                return;
            } else {
                jList.setSelectedIndex(0);
            }
        } else {
            jList.setSelectedIndex(selectedIndex.intValue());
        }
    }

    /**
     * Save the current selection.
     * 
     * @param keyPattern
     *            A session attribute key pattern.
     * @param jList
     *            The <code>JList</code>.
     */
    protected final void saveSelection(final String listKey, final javax.swing.JList jList) {
        session.setAttribute(MessageFormat.format(
                SK_LIST_SELECTED_INDEX_PATTERN, getId(), listKey),
                Integer.valueOf(jList.getSelectedIndex()));
    }

    /**
     * Set the cursor for the given list.
     * 
     * @param listModel
     *            A <code>DefaultListModel</code>.
     * @param jList
     *            A <code>JList</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void jListSetCursor(final DefaultListModel listModel,
            final javax.swing.JList jList, final java.awt.event.MouseEvent e) {
        if (listModel.isEmpty()) {
            SwingUtil.setCursor(jList, java.awt.Cursor.DEFAULT_CURSOR);
        } else {
            /* if the mouse lies within any row's bounds; update the
             * cursor to a hand */
            final Rectangle bounds =
                jList.getCellBounds(jList.getFirstVisibleIndex(), jList.getLastVisibleIndex());
            if (SwingUtil.regionContains(bounds, e.getPoint())) {
                SwingUtil.setCursor(jList, java.awt.Cursor.HAND_CURSOR);
            } else {
                SwingUtil.setCursor(jList, java.awt.Cursor.DEFAULT_CURSOR);
            }
        }
    }
}
