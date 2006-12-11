/**
 * Created On: 10-Dec-06 6:00:24 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.util.swing.plaf;

import javax.swing.Action;
import javax.swing.JMenu;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ThinkParityPopupSubMenu extends JMenu implements ThinkParityBasicMenuItem {
    
    /** Flag indicating if this is the last menu item. */
    Boolean last;
    
    /** Flag indicating if there is a separator next. */
    Boolean separatorNext;
    
    /** The menu style. */
    MenuStyle menuStyle;

    /**
     * @param action
     *          The action.
     */
    public ThinkParityPopupSubMenu(Action action) {
        super(action);
        this.last = Boolean.TRUE;
        this.separatorNext = Boolean.FALSE;
        this.menuStyle = MenuStyle.NORMAL;
    }    

    /**
     * @param text
     */
    public ThinkParityPopupSubMenu(String text) {
        super(text);
        this.last = Boolean.TRUE;
        this.separatorNext = Boolean.FALSE;
        this.menuStyle = MenuStyle.NORMAL;
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
     * Determine the menu style.
     */
    public MenuStyle getMenuStyle() {
        return menuStyle;
    }
        
    /**
     * Set the menu style.
     */
    public void setMenuStyle(final MenuStyle menuStyle) {
        this.menuStyle = menuStyle;
    }
}
