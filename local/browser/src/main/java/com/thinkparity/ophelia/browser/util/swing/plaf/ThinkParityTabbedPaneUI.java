/**
 * Created On: 6-Dec-06 5:52:42 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.util.swing.plaf;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ThinkParityTabbedPaneUI extends WindowsTabbedPaneUI {

    /**
     * Create a thinkParity tabbed pane ui.
     * 
     * @param c
     *            A <code>JComponent</code>.
     * @return A <code>ComponentUI</code>.
     */
    public static ComponentUI createUI(final JComponent c) {
        return new ThinkParityTabbedPaneUI();
    }
    
    /**
     * Create ThinkParityTabbedPaneUI.
     * 
     */
    public ThinkParityTabbedPaneUI() {
        super();
    }
    
    /**
     * Paint focus indicator. Dashed line is not drawn.
     */
    protected void paintFocusIndicator(Graphics g, int tabPlacement,
            Rectangle[] rects, int tabIndex, 
            Rectangle iconRect, Rectangle textRect,
            boolean isSelected) {
    }
}
