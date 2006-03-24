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
 * ImageIO Utility
 * 
 * Provides simple image\icon read capability via the javax.imageio.ImageIO
 * class.
 * 
 * TODO:  Move to com.thinkparity.codebase.javax.imageio
 * 
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
	 * Read the image. The image is assumed to be in /images/imageName
	 * 
	 * @param imageName
	 *            The image name.
	 * @return The buffered image.
	 */
	public static BufferedImage read(final String imageName) {
		logger.info("[JAVAX] [IMAGE IO] [READ BUFFERED IMAGE]");
		logger.debug(imageName);
		return read(ResourceUtil.getURL("images/" + imageName));
	}

	/**
	 * Read the icon. The image is assumed to be in /images/imageName
	 * 
	 * @param imageName
	 *            The image filename.
	 * @return The image icon.
	 */
	public static ImageIcon readIcon(final String imageName) {
		logger.info("[JAVAX] [IMAGE IO] [READ ICON]");
		logger.debug(imageName);
		return new ImageIcon(ResourceUtil.getURL("images/" + imageName));
	}

	/**
	 * Read the image.
	 * 
	 * @param imageName
	 *            The image name.
	 * @return The buffered image.
	 */
	private static BufferedImage read(final URL imageURL) {
		try { return ImageIO.read(imageURL); }
		catch(final IOException iox) { throw new RuntimeException(iox); }
	}

	/**
	 * Create an ImageIOUtil.
	 * 
	 */
	private ImageIOUtil() { super(); }
}
