/*
 * Jan 5, 2006
 */
package com.thinkparity.codebase.swing;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class GradientJPanel extends JPanel {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The gradient image buffer to paint on the panel.
	 */
	protected BufferedImage buffer;

	/**
	 * The end color of the gradient image.
	 * 
	 */
	protected final Color colourEnd;

	/**
	 * The starting color of the gradient image.
	 * 
	 */
	protected final Color colourStart;

	/**
	 * Create a black\white GradientJPanel.
	 * 
	 */
	public GradientJPanel() { this(Color.BLACK, Color.WHITE); }

	/**
	 * Create a GradientJPanel.
	 * 
	 * @param colourEnd
	 *            The end colour of the gradient.
	 * @param colourStart
	 *            The starting colour of the gradient.
	 */
	public GradientJPanel(final Color colourEnd, final Color colourStart) {
		super();
		this.colourEnd = colourEnd;
		this.colourStart = colourStart;
		addComponentListener(new ComponentAdapter() {
			public void componentHidden(final ComponentEvent e) {
				disposeBuffer();
			}
		});
	}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 */
	protected void paintComponent(Graphics g) {
		createBuffer();
		if(null != buffer) {
			final Shape clip = g.getClip();
			final Rectangle bounds = clip.getBounds();
			final Image bgImage =
				buffer.getSubimage(bounds.x, bounds.y, bounds.width, bounds.height);
			g.drawImage(bgImage, bounds.x, bounds.y, null);
		}
	}

	/**
	 * Create the buffered image to paint onto the panel.
	 *
	 */
	private void createBuffer() {
		final int width = getWidth();
		final int height = getHeight();
		if(0 == height || 0 == width) { return; }
		if(null == buffer || buffer.getWidth() != width || buffer.getHeight() != height) {
			buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			final Graphics2D g2 = buffer.createGraphics();

			try {
				g2.setPaint(getFirstPaint(width, height));
				g2.fill(getFirstRectangle(width, height));
	
				g2.setPaint(getSecondPaint(width, height));
				g2.fill(getSecondRectangle(width, height));
			}
			finally { g2.dispose(); }	// ensure cleanup
		}
	}

	/**
	 * Dispose of the image buffer.
	 *
	 */
	private void disposeBuffer() {
		if(null != buffer) {
			buffer.flush();
			buffer = null;
		}
	}

	/**
	 * Obtain the first gradient paint.
	 * 
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @return The gradient paint.
	 */
	private GradientPaint getFirstPaint(final int width, final int height) {
		return new GradientPaint(0, 0, colourStart, 0, height / 2, colourEnd);
	}

	/**
	 * Obtain the first rectangle.
	 * 
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @return The first rectangle.
	 */
	private Rectangle2D getFirstRectangle(final int width, final int height) {
		return new Rectangle2D.Double(0, 0, width, height / 2.0);
	}

	/**
	 * Obtain the second gradient paint.
	 * 
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @return The gradient paint.
	 */	
	private GradientPaint getSecondPaint(final int width, final int height) {
		return new GradientPaint(0, height / 2, colourEnd, 0, height, colourStart);
	}

	/**
	 * Obtain the first rectangle.
	 * 
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @return The first rectangle.
	 */
	private Rectangle2D getSecondRectangle(final int width, final int height) {
		return new Rectangle2D.Double(0, height / 2.0 - 1.0, width, height);
	}
}
