/*
 * Created On: 6-Oct-06 2:12:01 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab;

import java.awt.GridBagConstraints;
import java.util.Calendar;

import javax.swing.border.Border;

import com.thinkparity.codebase.FuzzyDateFormat;
import com.thinkparity.codebase.JVMUniqueId;
import com.thinkparity.codebase.swing.AbstractJPanel;
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
public abstract class DefaultTabPanel extends AbstractJPanel implements
        TabPanel {

	/** The panel <code>Border</code>. */
    protected static final Border BORDER;

    /** An image cache. */
    protected static final MainPanelImageCache IMAGE_CACHE;

    /** The number of rows in the version panel. */
    protected static final int NUMBER_VISIBLE_ROWS;

    /** A <code>FuzzyDateFormat</code>. */
    private static final FuzzyDateFormat FUZZY_DATE_FORMAT;

    static {
        BORDER = new BottomBorder(Colors.Browser.List.LIST_CONTAINERS_BORDER);
        FUZZY_DATE_FORMAT = new FuzzyDateFormat();
        IMAGE_CACHE = new MainPanelImageCache();
        NUMBER_VISIBLE_ROWS = 6;
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
        this.animator = new TabAnimator(this);
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
     * Handle a mouse click on a panel cell.
     * 
     * @param cell
     *          A <code>Cell</code>.
     * @param e
     *          A <code>MouseEvent</code>.
     */
    public void panelCellMouseClicked(final Cell cell, final java.awt.event.MouseEvent e) {        
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
     * Repaint the lists.
     *
     */
    protected abstract void repaintLists();

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
}
