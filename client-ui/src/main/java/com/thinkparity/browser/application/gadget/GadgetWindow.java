/*
 * Feb 11, 2006
 */
package com.thinkparity.browser.application.gadget;

import java.awt.*;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.MouseInputAdapter;

import com.thinkparity.browser.application.browser.UIConstants;
import com.thinkparity.browser.application.browser.component.LabelFactory;
import com.thinkparity.browser.javax.swing.AbstractJFrame;
import com.thinkparity.browser.platform.util.ImageIOUtil;
import com.thinkparity.browser.util.NativeSkinUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class GadgetWindow extends AbstractJFrame {

	/**
	 * The bright background image.
	 * 
	 * @see #getBrightImage()
	 */
	private static Image brightImage;

	/**
	 * The dim background image.
	 * 
	 * @see #getDimImage()
	 */
	private static Image dimImage;

	/**
	 * The size of the image.
	 * 
	 * @see #getImageSize()
	 */
	private static Dimension imageSize;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Obtain the gadget window size.
	 * 
	 * @return The gadget windo size.
	 */
	public static Dimension getGadgetWindowSize() { return getImageSize(); }

	/**
	 * Obtain the image for painting. Which image to use (dim\bright) is
	 * determined by the number of new system messages\documents.
	 * 
	 * @return The image for painting.
	 */
	public static Image getImage() {
		if(false) { return getBrightImage(); }
		else { return getDimImage(); }
	}

	/**
	 * Open the gadget window.
	 * 
	 * @return The gadget window.
	 */
	static GadgetWindow open() {
		final GadgetWindow gadgetWindow = new GadgetWindow();
		gadgetWindow.setVisible(true);
		gadgetWindow.applyRenderingHints();
		gadgetWindow.debugGeometry();
		gadgetWindow.debugLookAndFeel();
		return gadgetWindow;
	}

	/**
	 * Obtain the bright image.
	 * 
	 * @return The bright image.
	 */
	private static Image getBrightImage() {
		if(null == brightImage) {
			brightImage = ImageIOUtil.read("gadgetBackgroundBright.png");
		}
		return brightImage;
	}

	/**
	 * Obtain the dim image.
	 * 
	 * @return The dim image.
	 */
	private static Image getDimImage() {
		if(null == dimImage) {
			dimImage = ImageIOUtil.read("gadgetBackgroundDim.png");
		}
		return dimImage;
	}

	/**
	 * Obtain the size of the image.
	 * 
	 * @return The image size.
	 */
	private static Dimension getImageSize() {
		if(null == imageSize) {
			final Image image = getImage();
			imageSize = new Dimension(image.getWidth(null), image.getHeight(null));
		}
		return imageSize;
	}

	/**
	 * The gadget controller.
	 * 
	 */
	private Gadget controller;

	/**
	 * Mouse input adapter to track drag'n'drop movement.
	 * 
	 */
	private final MouseInputAdapter mouseInputAdapter;

	/**
	 * Create a GadgetWindow.
	 * 
	 */
	private GadgetWindow() {
		super("GadgetWindow");
		new GadgetWindowState(this);
		this.mouseInputAdapter = new MouseInputAdapter() {
			int offsetX;
			int offsetY;
			public void mouseClicked(final MouseEvent e) {
				if(2 == e.getClickCount()) {
					if(e.isShiftDown()) { System.exit(0); }
					else { runOpenBrowser(); }
				}
			}
			public void mouseDragged(final MouseEvent e) {
				getController().moveWindow(
						new Point(e.getPoint().x - offsetX,
								e.getPoint().y - offsetY));
			}
			public void mousePressed(final MouseEvent e) {
				offsetX = e.getPoint().x;
				offsetY = e.getPoint().y;
			}
		};
		addMouseListener(mouseInputAdapter);
		addMouseMotionListener(mouseInputAdapter);
		getRootPane().setBorder(BorderFactory.createEmptyBorder());
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		applyNativeSkin();
		setResizable(false);
		setSize(getImageSize());
		initComponents();
	}

	/**
	 * NOTE We are deliberately not issuing super.paint(java.awt.Graphics).
	 * 
	 * @see java.awt.Container#paint(java.awt.Graphics)
	 */
	public void paint(final Graphics g) {
		super.paint(g);
		final Graphics2D g2 = (Graphics2D) g.create();
		try { g2.drawImage(getImage(), 0, 0, this); }
		finally { g2.dispose(); }
	}

	/**
	 * Apply the native skin to the window.
	 * 
	 */
	private void applyNativeSkin() { NativeSkinUtil.applyNativeSkin(this); }

	/**
	 * Obtain the gadget controller.
	 * 
	 * @return The gadget controller.
	 */
	private Gadget getController() {
		if(null == controller) { controller = Gadget.getInstance(); }
		return controller;
	}

	private void initComponents() {
		setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();
		final JLabel jLabel = LabelFactory.create("Test", UIConstants.DefaultFontBold, Color.WHITE);
		add(jLabel, c.clone());
	}

	/**
	 * Open the main browser.
	 *
	 */
	private void runOpenBrowser() { getController().runOpenBrowser(); }
}
