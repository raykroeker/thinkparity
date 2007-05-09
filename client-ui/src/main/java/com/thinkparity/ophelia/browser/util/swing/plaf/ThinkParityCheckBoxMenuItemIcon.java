/**
 * Created On: 8-May-07 11:08:00 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.util.swing.plaf;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;

import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ThinkParityCheckBoxMenuItemIcon implements Icon {

    /** The series of <code>BufferedImage</code>s for the vertical thumb. */
    private static final Icon CHECK_BOX_MENU_ITEM_CHECK_ICON;

    static {
        CHECK_BOX_MENU_ITEM_CHECK_ICON = ImageIOUtil.readIcon("CheckBoxMenuItemCheckIcon.png");
    }

    /**
     * Paint the icon.
     * See BasicIconFactory class CheckBoxMenuItemIcon.
     * NOTE This icon paints only if selected. This behavior is expected
     * by BasicMenuItemUI.
     * 
     * @param c
     *            The <code>Component</code>.
     * @param g
     *            The <code>Graphics</code>.
     * @param x
     *            The x location <code>int</code>.
     * @param y
     *            The y location <code>int</code>.
     */
    public void paintIcon(final Component c, final Graphics g, final int x,
            final int y) {
        final AbstractButton b = (AbstractButton) c;
        final ButtonModel model = b.getModel();
        if (model.isSelected()) {
            CHECK_BOX_MENU_ITEM_CHECK_ICON.paintIcon(c, g, x, y);
        }
    }
    public int getIconWidth() { return CHECK_BOX_MENU_ITEM_CHECK_ICON.getIconWidth(); }
    public int getIconHeight() { return CHECK_BOX_MENU_ITEM_CHECK_ICON.getIconWidth(); }
}
