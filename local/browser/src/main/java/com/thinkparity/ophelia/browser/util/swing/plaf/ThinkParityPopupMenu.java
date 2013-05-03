/**
 * Created On: 19-Jun-07 10:18:05 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.util.swing.plaf;


/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface ThinkParityPopupMenu {

    /**
     * Get the menu background type.
     * 
     * @return A <code>MenuBackgroundType</code>.
     */
    public MenuBackgroundType getMenuBackgroundType();

    /**
     * Set the menu background type.
     * 
     * @param menuBackgroundType
     *            A <code>MenuBackgroundType</code>.
     */
    public void setMenuBackgroundType(final MenuBackgroundType menuBackgroundType);

    public enum MenuBackgroundType { GRADIENT, NORMAL }
}
