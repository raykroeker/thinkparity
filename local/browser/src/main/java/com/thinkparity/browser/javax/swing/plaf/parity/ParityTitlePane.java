/*
 * Jan 3, 2006
 */
package com.thinkparity.browser.javax.swing.plaf.parity;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import org.apache.log4j.Logger;

import com.thinkparity.browser.javax.swing.AbstractJPanel;
import com.thinkparity.browser.ui.UIConstants;
import com.thinkparity.browser.util.log4j.LoggerFactory;

import com.thinkparity.codebase.ResourceUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityTitlePane extends AbstractJPanel {

	/**
	 * Provides layout management for the title pane.
	 * 
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class TitlePaneLayoutManager implements LayoutManager {

		/**
		 * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
		 *      java.awt.Component)
		 * 
		 */
		public void addLayoutComponent(String name, Component c) {}

		/**
		 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
		 * 
		 */
		public void layoutContainer(Container c) {
			final int titlePaneHeight = getHeight();
			final int titlePaneWidth = getWidth();

			// NOTE Substance Removal
			final int height = closeJButton.getIcon().getIconHeight();
			final int width = closeJButton.getIcon().getIconWidth();
			final int x = titlePaneWidth - (8 + width);
			final int y = (titlePaneHeight - height) / 2;
			closeJButton.setBounds(x, y, height, width);
		}

		/**
		 * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
		 * 
		 */
		public Dimension minimumLayoutSize(Container c) {
			return preferredLayoutSize(c);
		}

		/**
		 * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
		 * 
		 */
		public Dimension preferredLayoutSize(Container c) {
			final int height = computeHeight();
			return new Dimension(height, height);
		}

		/**
		 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
		 * 
		 */
		public void removeLayoutComponent(Component c) {}

		/**
		 * Compute the height of the title pane.
		 * 
		 * @return The height of the title pane.
		 */
		private int computeHeight() { return UIConstants.TitlePaneHeight; }
	}

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Apache logger.
	 * 
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Close button.
	 * 
	 */
	private JButton closeJButton;

	/**
	 * Property change listener added to the window.
	 * 
	 */
	private PropertyChangeListener propertyChangeListener;

	/**
	 * Listens for changes in the state of the Window listener to update the
	 * state of the widgets.
	 * 
	 */
	private WindowListener windowListener;

	/**
	 * Create a ParityTitlePane.
	 * 
	 * @param jRootPane
	 *            Root pane.
	 */
	public ParityTitlePane() {
		super("ParityTitlePane");
		installSubcomponents();
		installDefaults();
		setLayout(createLayoutManager());
		setToolTipText(getFullTitle());
	}

	/**
	 * @see java.awt.Component#addNotify()
	 * 
	 */
	public void addNotify() {
		super.addNotify();

		final Window window = SwingUtilities.getWindowAncestor(this);
		uninstallListeners(window);
		if(null != window) {
			setActiveState(window.isActive());
			installListeners(window);
		}
		setToolTipText(getFullTitle());
	}

	/**
	 * @see java.awt.Component#removeNotify()
	 * 
	 */
	public void removeNotify() {
		super.removeNotify();

		final Window window = SwingUtilities.getWindowAncestor(this);
		uninstallListeners(window);
	}

	/**
	 * Uninstalls the necessary state.
	 */
	public void uninstall() {
		final Window window = SwingUtilities.getWindowAncestor(this);
		uninstallListeners(window);
		removeAll();
	}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 */
	protected void paintComponent(Graphics g) {
		final Graphics2D g2 = (Graphics2D) g.create();

		paintGradientBackground(g2, getWidth(), getHeight());
		paintTitleText(g2, getHeight());

		// overlay the border so that the cornered edges get a border as well
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(-1, -1, getWidth() + 1, getHeight() * 2,
				UIConstants.TitlePaneCurvature, UIConstants.TitlePaneCurvature);

		g2.dispose();
	}

	/**
	 * Creates the Buttons that will be placed on the TitlePane.
	 */
	private void createButtons() {
		closeJButton = createTitleJButton();
		closeJButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				final Window window = SwingUtilities.getWindowAncestor(ParityTitlePane.this);
				if(null != window) {
					window.dispatchEvent(
							new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
				}
			}
		});
		closeJButton.setText(null);
		closeJButton.setBorder(null);
		closeJButton.setToolTipText("Close");
		closeJButton.setIcon(createCloseJButtonIcon());
	}

	private Icon createCloseJButtonIcon() {
		return new ImageIcon(ResourceUtil.getURL("images/closeButton.png"));
//		final BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
//		final Graphics2D g2 = image.createGraphics();
//		try {
//			g2.setPaint(new GradientPaint(0, 0, new Color(226, 234, 234, 255), 0, 16 - 1  / 2, new Color(205, 217, 224, 255)));
//			g2.fill(new Rectangle2D.Double(0, 0, 16, 16));
//			g2.setColor(new Color(116, 120, 131, 255));
//			g2.drawRoundRect(0, 0, 16 - 1, 16 - 1, 2, 2);
//
//			final Stroke stroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
//
//			g2.setStroke(stroke);
//			g2.setColor(new Color(116, 120, 131, 255));
//			g2.drawLine(4, 4, 12, 12);
//			g2.drawLine(12, 4, 4, 12);
//		}
//		finally { g2.dispose(); }
//		return new ImageIcon(image);
	}

	/**
	 * Create the layout manager for the title pane.
	 * 
	 * @return The layout manager for the title pane.
	 */
	private LayoutManager createLayoutManager() {
		return new TitlePaneLayoutManager();
	}
	
	/**
	 * Create a JButton for the title pane.
	 * 
	 * @return The JButton for the title pane.
	 */
	private JButton createTitleJButton() {
		final JButton jButton = new JButton();
		jButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		jButton.setFocusPainted(false);
		jButton.setFocusable(false);
		return jButton;
	}

	/**
	 * Obtain the clipped title of the window. If the full title will not fit
	 * within maximumTitleWidth on the graphics; clip it.
	 * 
	 * @return The clipped title.
	 */
	private String getClippedTitle(final Graphics2D graphics2D,
			final Double maximumTitleWidth) {
		final String fullTitle = getFullTitle();
		// clip the title
		if(null != fullTitle) {
//			final FontMetrics fm = graphics2D.getFontMetrics();
//			final String clippedTitle =
//				SubstanceCoreUtilities.clipString(fm, maximumTitleWidth.intValue(), fullTitle);
			// TODO Substance Removal
			final String clippedTitle = fullTitle;

			// show tooltip with full title only if necessary
			if (fullTitle.equals(clippedTitle)) { this.setToolTipText(null); }
			else { this.setToolTipText(fullTitle); }

			return clippedTitle;
		}
		else { return fullTitle; }
	}

	/**
	 * Obtain the full title of the window.
	 * 
	 * @return The full title.
	 */
	private String getFullTitle() {
		final String fullTitle;

		final Window window = SwingUtilities.getWindowAncestor(this);
		if(window instanceof Frame) {
			fullTitle = ((Frame) window).getTitle();
		}
		else if (window instanceof Dialog) {
			fullTitle = ((Dialog) window).getTitle();
		}
		else { fullTitle = null; }
		return fullTitle;
	}

	/**
	 * Returns the decoration style of the <code>JRootPane</code>.
	 */
	private int getWindowDecorationStyle() {
		return getRootPane().getWindowDecorationStyle();
	}

	/**
	 * Installs the fonts and necessary properties on the MetalTitlePane.
	 */
	private void installDefaults() {
		setFont(UIManager.getFont("InternalFrame.titleFont", getLocale()));
	}

	/**
	 * Installs the necessary listeners.
	 */
	private void installListeners(final Window window) {
		if(null != window) {
			windowListener = new WindowAdapter() {
				public void windowActivated(final WindowEvent ev) {
					setActiveState(true);
				}
				public void windowDeactivated(WindowEvent ev) {
					setActiveState(false);
				}
			};
			window.addWindowListener(windowListener);

			propertyChangeListener = new PropertyChangeListener() {
				public void propertyChange(final PropertyChangeEvent evt) {
					final String propertyName = evt.getPropertyName();
					if("title".equals(propertyName)) {
						repaint();
						setToolTipText((String) evt.getNewValue());
					}
					else if("componentOrientation".equals(propertyName)
							|| "iconImage".equals(propertyName)) {
						revalidate();
						repaint();
					}
				}
			};
			window.addPropertyChangeListener(propertyChangeListener);
		}
	}

	/**
	 * Adds any sub-Components contained in the <code>SubstanceTitlePane</code>.
	 */
	private void installSubcomponents() {
		createButtons();
		add(closeJButton);
	}

	/**
	 * Paint a gradient background on the title pane.
	 * 
	 * @param g
	 *            The graphics context.
	 * @param width
	 *            The title pane width.
	 * @param height
	 *            The title pane height.
	 */
	private void paintGradientBackground(final Graphics2D g, final int width,
			final int height) {
		final Point pt1 = new Point(0, 0);
		final Point pt2 = new Point(0, height);
		final Color color1 = new Color(236, 243, 239, 255);
		final Color color2 = new Color(194, 208, 219, 255);
		final Paint gradientPaint = new GradientPaint(pt1, color1, pt2, color2);
		g.setPaint(gradientPaint);
		g.fillRect(0, 0, width, height);
	}

	/**
	 * Paint the title on the title pane.
	 * 
	 * @param g
	 *            The graphics context.
	 * @param height
	 *            The title pane height.
	 */
	private void paintTitleText(final Graphics2D g, final int height) {
		final double maxTitleWidth = closeJButton.getBounds().getX() - 37;
		final String title = getClippedTitle(g, maxTitleWidth);
		if(null != title) {
			g.setRenderingHint(
					RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			g.setColor(new Color(116, 120, 131, 255));

			final FontMetrics fm = g.getFontMetrics();
			final int yOffset = ((height - fm.getHeight()) / 2) + fm.getAscent();
			g.drawString(title, 19, yOffset + 1);
		}
	}

	/**
	 * Set the active state of the window.
	 * 
	 * @param isWindowActive
	 *            The active state of the window.
	 */
	private void setActiveState(final Boolean isWindowActive) {
		if(getWindowDecorationStyle() == JRootPane.FRAME) {
			closeJButton.putClientProperty("paintActive", isWindowActive);
		}
		getRootPane().repaint();
	}

	/**
	 * Remove the window listener; and the proeprty change listener from the
	 * window.
	 * 
	 * @param window
	 *            The window.
	 */
	private void uninstallListeners(final Window window) {
		if(null != window) {
			window.removeWindowListener(windowListener);
			windowListener = null;

			window.removePropertyChangeListener(propertyChangeListener);
			propertyChangeListener = null;
		}
	}
}
