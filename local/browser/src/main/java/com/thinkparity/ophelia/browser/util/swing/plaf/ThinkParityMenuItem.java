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

    /**
     * Create a think parity menu item.
     * 
     * @param action
     *            The <code>Action</code>.
     */
    public ThinkParityMenuItem(final Action action) {
        super(action); 
    }

    /**
     * Create a think parity menu item.
     * 
     * @param text
     *            The menu text <code>String</code>.
     */
    public ThinkParityMenuItem(final String text) {
        super(text);
    }
}
