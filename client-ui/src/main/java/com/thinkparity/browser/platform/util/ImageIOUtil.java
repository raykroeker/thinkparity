/*
 * Jan 31, 2006
 */
package com.thinkparity.browser.platform.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.codebase.ResourceUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ImageIOUtil {

	/**
	 * An apache logger.
	 * 
	 */
	protected static final Logger logger =
		LoggerFactory.getLogger(ImageIOUtil.class);

	/**
	 * Read the image date into a buffered image from the named image.
	 * 
	 * @param imageName
	 *            The image name.
	 * @return The buffered image.
	 */
	public static BufferedImage read(final String imageName) {
		logger.info("read(String)");
		logger.debug(imageName);
		final String imagePath = "images/" + imageName;
		return read(ResourceUtil.getURL(imagePath));
	}

	public static ImageIcon readIcon(final String imageName) {
		logger.info("[BROWSER2] [PLATFORM] [READ ICON]");
		logger.debug(imageName);
		return readIcon(ResourceUtil.getURL("images/" + imageName));
	}

	public static ImageIcon readIcon(final URL imageURL) {
		logger.info("[BROWSER2] [PLATFORM] [READ ICON]");
		logger.debug(imageURL);
		return new ImageIcon(imageURL);
	}

	/**
	 * Read the image data into a buffered image from the imageURL.
	 * 
	 * @param imageURL
	 *            The image url.
	 * @return The buffered image.
	 */
	public static BufferedImage read(final URL imageURL) {
		logger.info("read(URL)");
		logger.debug(imageURL);
		try { return ImageIO.read(imageURL); }
		catch(final IOException iox) { throw new RuntimeException(iox); }
	}

	/**
	 * Create an ImageIOUtil.
	 * 
	 */
	private ImageIOUtil() { super(); }
}
