/**
 * Created On: 4-Dec-06 3:36:13 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.util.swing.plaf;

import javax.swing.Action;
import javax.swing.JMenuItem;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ThinkParityMenuItem extends JMenuItem implements ThinkParityBasicMenuItem {

    /** Flag indicating if this is the last menu item. */
    private Boolean last;

    /** Flag indicating if there is a separator next. */
    private Boolean separatorNext;

    /** The menu style. */
    private MenuStyle menuStyle;

    /**
     * Create a think parity menu item.
     * 
     * @param action
     *            The <code>Action</code>.
     */
    public ThinkParityMenuItem(final Action action) {
        this(action, MenuStyle.NORMAL); 
    }

    /**
     * Create a think parity menu item.
     * 
     * @param action
     *            The <code>Action</code>.
     * @param menuStyle
     *            The <code>MenuStyle</code>.
     */
    public ThinkParityMenuItem(final Action action, final MenuStyle menuStyle) {
        super(action);
        this.last = Boolean.TRUE;
        this.separatorNext = Boolean.FALSE;
        this.menuStyle = menuStyle;
    }

    /**
     * Create a think parity menu item.
     * 
     * @param text
     *            The menu text <code>String</code>.
     */
    public ThinkParityMenuItem(final String text) {
        this(text, MenuStyle.NORMAL);
    }

    /**
     * Create a think parity menu item.
     * 
     * @param text
     *            The menu text <code>String</code>.
     * @param menuStyle
     *            The <code>MenuStyle</code>.
     */
    public ThinkParityMenuItem(final String text, final MenuStyle menuStyle) {
        super(text);
        this.last = Boolean.TRUE;
        this.separatorNext = Boolean.FALSE;
        this.menuStyle = menuStyle;
    }

    /**
     * Determine if this is the last menu item.
     * 
     * @return True if this is the last menu item, false otherwise.
     */
    public Boolean isLast() {
        return last;
    }

    /**
     * Set if this is the last menu item.
     * 
     * @param last
     *          Flag indicating if this is the last menu item.
     */
    public void setLast(final Boolean last) {
        this.last = last;
    }

    /**
     * Determine if there is a separator next.
     * 
     * @return True if there is a separator next, false otherwise.
     */
    public Boolean isSeparatorNext() {
        return separatorNext;
    }

    /**
     * Set if there is a separator next.
     * 
     * @param separatorNext
     *          Flag indicating if there is a separator next.
     */
    public void setSeparatorNext(final Boolean separatorNext) {
        this.separatorNext = separatorNext;
    }

    /**
     * Get the menu style.
     * 
     * @return The <code>MenuStyle</code>.
     */
    public MenuStyle getMenuStyle() {
        return menuStyle;
    }

    /**
     * Set the menu style.
     * 
     * @param menuStyle
     *            The <code>MenuStyle</code>.
     */
    public void setMenuStyle(final MenuStyle menuStyle) {
        this.menuStyle = menuStyle;
    }
}
