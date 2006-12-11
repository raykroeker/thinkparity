/**
 * Created On: 11-Dec-06 12:06:39 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.util.swing.plaf;


/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface ThinkParityBasicMenuItem {

    /**
     * Determine if this is the last menu item.
     * 
     * @return True if this is the last menu item, false otherwise.
     */
    public Boolean isLast();

    /**
     * Set if this is the last menu item.
     * 
     * @param last
     *          Flag indicating if this is the last menu item.
     */
    public void setLast(final Boolean last);
    
    /**
     * Determine if there is a separator next.
     * 
     * @return True if there is a separator next, false otherwise.
     */
    public Boolean isSeparatorNext();

    /**
     * Set if there is a separator next.
     * 
     * @param separatorNext
     *          Flag indicating if there is a separator next.
     */
    public void setSeparatorNext(final Boolean separatorNext);
    
    /**
     * Determine the menu style.
     */
    public MenuStyle getMenuStyle();
        
    /**
     * Set the menu style.
     */
    public void setMenuStyle(final MenuStyle menuStyle);
    
    public enum MenuStyle { NORMAL, WHITE_SPACE }
}
