/**
 * Created On: 19-Jun-07 11:24:24 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.util.swing.plaf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ThinkParitySeparatorUI extends BasicSeparatorUI {

    /** The colour for the separator. */
    private static final Color COLOR_SEPARATOR;

    static {
        COLOR_SEPARATOR = new Color(197, 207, 217, 255);
    }

    /**
     * Create a thinkParity separator ui.
     * 
     * @param c
     *            A <code>JComponent</code>.
     * @return A <code>ComponentUI</code>.
     */
    public static ComponentUI createUI(final JComponent c) {
        return new ThinkParitySeparatorUI();
    }

    /**
     * Paint.
     * 
     * @param g
     *            A <code>Graphics</code>.
     * @param c
     *            A <code>JComponent</code>.
     */
    public void paint(final Graphics g, final JComponent c) {
        Dimension s = c.getSize();

        if (((JSeparator) c).getOrientation() == JSeparator.VERTICAL) {
            g.setColor(COLOR_SEPARATOR);
            g.drawLine(0, 0, 0, s.height);
        } else { // HORIZONTAL
            g.setColor(COLOR_SEPARATOR);
            g.drawLine(0, 0, s.width, 0);
        }
    }

    /**
     * Get preferred size.
     * 
     * @param c
     *            A <code>JComponent</code>.
     */
    public Dimension getPreferredSize(final JComponent c) {
        if (((JSeparator) c).getOrientation() == JSeparator.VERTICAL)
            return new Dimension(1, 0);
        else
            return new Dimension(0, 1);
    }
}
