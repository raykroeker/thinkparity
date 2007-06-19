/**
 * Created On: 18-Jun-07 9:38:56 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.util.swing.plaf;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ThinkParityCheckBoxMenuItemUI extends ThinkParityMenuItemUI {

    /**
     * Create a thinkParity check box menu item ui.
     * 
     * @param c
     *            A <code>JComponent</code>.
     * @return A <code>ComponentUI</code>.
     */
    public static ComponentUI createUI(final JComponent c) {
        return new ThinkParityCheckBoxMenuItemUI();
    }

    /**
     * Create ThinkParityCheckBoxMenuItemUI.
     */
    public ThinkParityCheckBoxMenuItemUI() {
        super();
    }

    /**
     * Copied from BasicCheckBoxMenuItemUI.
     */
    protected String getPropertyPrefix() {
        return "CheckBoxMenuItem";
    }
}
