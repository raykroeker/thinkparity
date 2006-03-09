/*
 * Mar 9, 2006
 */
package com.thinkparity.swingtest;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.BrowserWindow;
import com.thinkparity.browser.javax.swing.AbstractJFrame;
import com.thinkparity.browser.platform.util.ImageIOUtil;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SwingTest extends AbstractJFrame {

	/**
	 * The size of the main window.
	 * 
	 * @see #getMainWindowSize()
	 */
	private static Dimension mainWindowSize;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;


	/**
	 * The window icon.
	 * 
	 */
	private static final BufferedImage THINKPARITY_ICON;

	static {
		THINKPARITY_ICON = ImageIOUtil.read("thinkParity32x32.png");
	}

	/**
	 * Obtain the size of the main window.
	 * 
	 * @return The size of the main window.
	 */
	public static Dimension getMainWindowSize() {
		if(null == mainWindowSize) {
			// DIMENSION BrowserWindow 354x562
			mainWindowSize = new Dimension(354, 562);
		}
		return mainWindowSize;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try { SwingTest.open(); }
		catch(final Throwable t) {
			t.printStackTrace(System.err);
			System.exit(1);
		}
	}

	/**
	 * Open the main window.
	 * 
	 * @return The main window.
	 */
	static SwingTest open() {
		final SwingTest mainWindow = new SwingTest();
		mainWindow.setVisible(true);
		mainWindow.applyRenderingHints();
		mainWindow.debugGeometry();
		mainWindow.debugLookAndFeel();
		return mainWindow;
	}

	/**
	 * Handle to an apache logger.
	 * 
	 */
	public final Logger logger = LoggerFactory.getLogger(getClass());


	/**
	 * Create a BrowserWindow.
	 * 
	 * @throws HeadlessException
	 */
	private SwingTest() throws HeadlessException {
		super("SwingTest");
		getRootPane().setBorder(BrowserConstants.BrowserWindowBorder);
		setIconImage(THINKPARITY_ICON);
		setTitle(getString("Title"));
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());
		setLocation(new Point(100, 100));
		setResizable(false);
		setSize(BrowserWindow.getMainWindowSize());
		initComponents();
	}

	/**
	 * Add the main panel to the window.
	 * 
	 */
	private void initComponents() {
		final GridBagConstraints c = new GridBagConstraints();


		final JButton jButton = new JButton("Click Me", icon);

		final JLabel jLabel = new JLabel("Click Me", icon, SwingConstants.CENTER);
		
		final PButton pButton = new PButton();
		pButton.setSize(new Dimension(68, 25));
		pButton.setPreferredSize(new Dimension(68, 25));
		pButton.setMaximumSize(new Dimension(68, 25));
		pButton.setMinimumSize(new Dimension(68, 25));
		
		add(jButton, c.clone());
		c.gridy = 1;
		add(jLabel, c.clone());
		c.gridy = 2;
		add(pButton, c.clone());
	}

	private static final Icon icon = ImageIOUtil.readIcon("StandardButton.png");
	private static final BufferedImage image = ImageIOUtil.read("StandardButton.png");

	private class PButton extends JButton {
		
		private static final long serialVersionUID = 1;

		protected void paintComponent(Graphics g) {
			final Graphics2D g2 = (Graphics2D) g.create();
			try {
				g2.drawImage(image, 0, 0, this);
				g2.setColor(Color.BLACK);
				g2.setFont(BrowserConstants.DefaultFont);
				final FontMetrics fm = g2.getFontMetrics();
				final Dimension d = getSize();
System.out.println("d:" + d.toString());
System.out.println("fm.getHeight():" + fm.getHeight());
//				g2.drawString("Click Me", getInsets().left, d.height / 2 + fm.getDescent());
				g2.drawString("Click Me", getInsets().left, getInsets().top + d.height / 2 + fm.getAscent() / 2);
			}
			finally { g2.dispose(); }
		}
	}
}
