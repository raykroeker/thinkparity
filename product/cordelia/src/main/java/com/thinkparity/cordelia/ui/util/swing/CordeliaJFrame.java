/**
 * 
 */
package com.thinkparity.cordelia.ui.util.swing;

import javax.swing.JFrame;

import com.thinkparity.codebase.swing.AbstractJFrame;

/**
 * @author raymond
 *
 */
public abstract class CordeliaJFrame extends AbstractJFrame {

    /**
     * 
     */
    public CordeliaJFrame() {
        super(null);
        // TODO Refactor the border.
        // getRootPane().setBorder(new WindowBorder());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // TODO Refactor the image
        // setIconImage(com.thinkparity.ophelia.browser.Constants.Images.WINDOW_ICON_IMAGE);
        // TODO Insert custom decorations
        // setUndecorated(true);
    }

    /* (non-Javadoc)
     * @see java.awt.Component#setVisible(boolean)
     */
    @Override
    public void setVisible(final boolean b) {
        if (b) {
            setLocation(calculateLocation());
        }
        super.setVisible(b);
    }

    
}
