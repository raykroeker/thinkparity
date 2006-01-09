/*
 * Jan 3, 2006
 */
package com.thinkparity.browser.javax.swing.plaf.parity;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jvnet.substance.color.ColorScheme;
import org.jvnet.substance.utils.SubstanceCoreUtilities;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityImageCreator {

	/**
	 * Create a ParityImageCreator.
	 */
	public ParityImageCreator() { super(); }


	public static Icon getExpandIcon(final ColorScheme colorScheme) {
		return getExpandIcon(colorScheme, ParityConstants.DEFAULT_IMAGE_DIMENSION);
	}

	/**
	 * Create the icon for the expand action. The icon is an inverted triangle
	 * with an "echo" overlay.
	 * 
	 * @param colorScheme
	 *            The color scheme to use.
	 * @param imageDimension
	 *            The size of icon to create.
	 * @return The icon.
	 */
	public static Icon getExpandIcon(final ColorScheme colorScheme,
			final int imageDimension) {
		final BufferedImage image = SubstanceCoreUtilities.getBlankImage(imageDimension, imageDimension);

		final Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(colorScheme.getForegroundColor());
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

		final Stroke stroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		graphics.setStroke(stroke);

		final int quarter = imageDimension / 4;
		final Point p1 = new Point(quarter, quarter);
		final Point p2 = new Point(3 * quarter, quarter);
		final Point p3 = new Point(2 * quarter, quarter * 2);

		// horizontal line
//		graphics.drawLine((int) p1.getX() , (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
		// first vertical line
//		graphics.drawLine((int) p2.getX(), (int) p2.getY(), (int) p3.getX(), (int) p3.getY());
		// second vertical line
//		graphics.drawLine((int) p3.getX(), (int) p3.getY(), (int) p1.getX(), (int) p1.getY());

		graphics.fillPolygon(new int[] {(int) p1.getX(), (int) p2.getX(), (int) p3.getX()}, new int[] {(int) p1.getY(), (int) p2.getY(), (int) p3.getY()}, 3);

		overlayEcho(image, colorScheme, 1, 1);
		return new ImageIcon(image);
	}

	/**
	 * Overlays light-colored echo below the specified image.
	 * 
	 * @param image
	 *            The input image.
	 * @param colorScheme
	 *            Color scheme for creating the echo.
	 * @param offsetX
	 *            X offset of the echo.
	 * @param offsetY
	 *            Y offset of the echo.
	 */
	private static void overlayEcho(BufferedImage image,
			ColorScheme colorScheme, int offsetX, int offsetY) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage echo = SubstanceCoreUtilities
				.getBlankImage(width, height);

		int echoColor = colorScheme.getUltraLightColor().getRGB();
		// compute echo
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				int color = image.getRGB(col, row);
				int transp = (color >>> 24) & 0xFF;
				if (transp == 255) {
					int newX = col + offsetX;
					if ((newX < 0) || (newX >= width)) {
						continue;
					}
					int newY = row + offsetY;
					if ((newY < 0) || (newY >= height)) {
						continue;
					}
					echo.setRGB(newX, newY, echoColor);
				}
			}
		}
		// overlay echo
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				int color = image.getRGB(col, row);
				int transp = (color >>> 24) & 0xFF;
				if (transp == 255) {
					continue;
				}
				image.setRGB(col, row, echo.getRGB(col, row));
			}
		}
	}
}
