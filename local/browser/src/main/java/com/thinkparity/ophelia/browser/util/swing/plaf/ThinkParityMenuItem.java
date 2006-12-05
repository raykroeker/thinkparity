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
public class ThinkParityMenuItem extends JMenuItem {
    
    /** Flag indicating if this is the last menu item. */
    Boolean last;
    
    /** Flag indicating if there is a separator next. */
    Boolean separatorNext;

    /**
     * @param action
     *          The action.
     */
    public ThinkParityMenuItem(Action action) {
        super(action);
        this.last = Boolean.TRUE;
        this.separatorNext = Boolean.FALSE;
    }    

    /**
     * @param text
     */
    public ThinkParityMenuItem(String text) {
        super(text);
        this.last = Boolean.TRUE;
        this.separatorNext = Boolean.FALSE;
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
}
