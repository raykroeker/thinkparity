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
public class ThinkParityPopupSubMenu extends JMenu {

    /**
     * Create a think parity popup submenu.
     * 
     * @param action
     *            The <code>Action</code>.
     */
    public ThinkParityPopupSubMenu(final Action action) {
        super(action);
    }    

    /**
     * Create a think parity popup submenu.
     * 
     * @param text
     *            The menu text <code>String</code>.
     */
    public ThinkParityPopupSubMenu(final String text) {
        super(text);
    }
}
