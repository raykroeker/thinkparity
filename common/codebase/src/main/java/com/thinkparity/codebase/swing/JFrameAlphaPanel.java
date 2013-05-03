/*
 * Created On:  4-May-07 3:33:59 PM
 */
package com.thinkparity.codebase.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

/**
 * <b>Title:</b>thinkParity Common JFrame Alpha Panel<br>
 * <b>Description:</b>A panel used to paint a semi-transparent "sheen" on top
 * of an existing jframe.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class JFrameAlphaPanel extends JPanel {

    /**
     * Obtain an instance of an alhpa composite used to paint the transparency.
     * 
     * @param alpha
     *            An alpha value <code>float</code>.
     * @return An <code>AlphaComposite</code>.
     */
    private static AlphaComposite getAlphaCompositeInstance(final float alpha) {
        return AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
    }

    /** The panel's alpha value. */
    private final float alpha;

    /** The panel's alpha color. */
    private final Color color;

    /** The <code>Rectangle</code> used within paint component. */
    private final Rectangle fillRectangle;

    /** The panel's jFrame. */
    private AbstractJFrame jFrame;

    /**
     * Create JFrameAlphaPanel.
     * 
     * @param alpha
     *            The alpha to use when creating the composite.
     * @param color
     *            The color of the transparency.
     */
    public JFrameAlphaPanel(final Float alpha, final Color color) {
        super();
        setBorder(null);
        setOpaque(false);
        setLocation(0, 0);
        this.alpha = alpha.floatValue();
        this.color = color;
        this.fillRectangle = new Rectangle();
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     *
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setComposite(getAlphaCompositeInstance(alpha));
            g2.setPaint(color);
            g2.fill(fillRectangle);
        } finally {
            g2.dispose();
        }
    }

    /**
     * Set the swing frame to draw on top of.
     * 
     * @param jFrame
     *            An <code>AbstractJFrame</code>.
     */
    void setJFrame(final AbstractJFrame jFrame) {
        this.jFrame = jFrame;
        this.fillRectangle.width = this.jFrame.getWidth();
        this.fillRectangle.height = this.jFrame.getHeight();
        setSize(this.jFrame.getSize());
    }
}