package com.thinkparity.browser.javax.swing.animation.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

/**
 * The glass pane interceptor is added\removed to the jFrame during the
 * subsequent start\stop calls. This is so that during the animation no
 * mouse events can be captured by the underlying components.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class GlassPaneInterceptor extends JComponent implements
		MouseMotionListener {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Hints used to render the transulcent background.
	 * 
	 */
	private final RenderingHints renderingHints;

	/**
	 * Create a GlassPaneInterceptor.
	 * 
	 */
	public GlassPaneInterceptor() {
		super();
		addMouseMotionListener(this);
        
		this.renderingHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        this.renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.renderingHints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(final MouseEvent e) {}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(final MouseEvent e) {}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 */
	public void paintComponent(Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(renderingHints);
        
        g2.setColor(new Color(255, 255, 255, (int) (255 * 0.70f)));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.dispose();
	}
}