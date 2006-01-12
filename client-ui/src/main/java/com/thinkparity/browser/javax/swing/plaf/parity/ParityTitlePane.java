/*
 * Jan 3, 2006
 */
package com.thinkparity.browser.javax.swing.plaf.parity;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import org.apache.log4j.Logger;
import org.jvnet.substance.SubstanceImageCreator;
import org.jvnet.substance.SubstanceButtonUI.ButtonTitleKind;
import org.jvnet.substance.utils.ButtonBackgroundDelegate;
import org.jvnet.substance.utils.SubstanceCoreUtilities;

import com.thinkparity.browser.javax.swing.BrowserColorUtil;
import com.thinkparity.browser.javax.swing.animation.HideJFrameAnimator;
import com.thinkparity.browser.javax.swing.animation.ShowJFrameAnimator;
import com.thinkparity.browser.javax.swing.plaf.parity.color.iTunesColorScheme;
import com.thinkparity.browser.log4j.BrowserLoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityTitlePane extends JComponent {

	/**
	 * Action used to close the window.
	 * 
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class CloseAction extends AbstractAction {

		/**
		 * @see java.io.Serailizable
		 */
		private static final long serialVersionUID = 1;

		/**
		 * Create a CloseAction.
		 * 
		 */
		public CloseAction() {
			super(UIManager.getString("MetalTitlePane.closeTitle", getLocale()));
		}

		/**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			final Window window = getWindow();
			if(null != window) {
				window.dispatchEvent(new WindowEvent(window,
						WindowEvent.WINDOW_CLOSING));
			}
		}
	}

	/**
	 * The collapse action will call the animation used to hide the main browser
	 * display.
	 * 
	 */
	private class CollapseAction extends AbstractAction {
		private static final long serialVersionUID = 1;
		public void actionPerformed(ActionEvent e) {
			logger.info("Pre-animation \"launch\"");
			new HideJFrameAnimator((JFrame) window).start();
			jFrameState = JFrameState.COLLAPSED;
			logger.info("Post-animation \"launch\"");
		}
	}

	/**
	 * The expand action will call the animation used to show the main browser
	 * display.
	 * 
	 */
	private class ExpandAction extends AbstractAction {
		private static final long serialVersionUID = 1;
		public void actionPerformed(final ActionEvent e) {
			logger.info("Pre-animation \"launch\"");
			new ShowJFrameAnimator((JFrame) window).start();
			jFrameState = JFrameState.EXPANDED;
			logger.info("Post-animation \"launch\"");
		}
	}

	/**
	 * Toggle the state of the jframe.
	 * 
	 */
	private class ToggleAction extends AbstractAction {
		private static final long serialVersionUID = 1;
		public void actionPerformed(final ActionEvent e) {
			if(jFrameState == JFrameState.COLLAPSED) {
				new ExpandAction().actionPerformed(e);
			}
			else if(jFrameState == JFrameState.EXPANDED) {
				new CollapseAction().actionPerformed(e);
			}
		}
	}

	/**
	 * Possible states of the JFrame.
	 * 
	 */
	private enum JFrameState{ COLLAPSED, EXPANDED }

	/**
	 * PropertyChangeListener installed on the Window. Updates the necessary
	 * state as the state of the Window changes.
	 */
	private class PropertyChangeHandler implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent pce) {
			String name = pce.getPropertyName();

			if("title".equals(name)) {
				repaint();
				setToolTipText((String) pce.getNewValue());
			}
			else if("componentOrientation".equals(name) || "iconImage".equals(name)) {
				revalidate();
				repaint();
			}
		}
	}

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
			final int titlePaneWidth = getWidth();

			final int height = toggleJButton.getIcon().getIconHeight();
			final int width = toggleJButton.getIcon().getIconWidth();
			final int x = titlePaneWidth - (4 + width);
			final int y = 3;
			closeJButton.setBounds(x, y, height, width);
			toggleJButton.setBounds(x - (width + 2), y, height, width);
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
		private int computeHeight() {
			// grab the height of the font
			final FontMetrics fm = jRootPane.getFontMetrics(getFont());
			final int fontHeight = fm.getHeight() + 7;

			// grab the height of the icon
			final int iconHeight = ParityConstants.DEFAULT_IMAGE_DIMENSION + 7;

			return Math.max(fontHeight, iconHeight);
		}
	}

	/**
	 * Window adapter used to capture whether or not the window is currently
	 * active.
	 * 
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class WindowHandler extends WindowAdapter {
		public void windowActivated(final WindowEvent ev) { setActive(true); }
		public void windowDeactivated(WindowEvent ev) { setActive(false); }
	}

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger = BrowserLoggerFactory.getLogger(getClass());

	/**
	 * JRootPane rendering for.
	 */
	private final JRootPane jRootPane;

	/**
	 * PropertyChangeListener added to the JRootPane.
	 */
	private PropertyChangeListener propertyChangeListener;

	/**
	 * Toggle button for animating the JFrame.
	 */
	private JButton toggleJButton;

	/**
	 * Close button.
	 */
	private JButton closeJButton;

	/**
	 * Current window; or null if this title pane is not being drawn on top of a
	 * window (not sure that's possible).
	 */
	private Window window;

	/**
	 * Listens for changes in the state of the Window listener to update the
	 * state of the widgets.
	 */
	private WindowListener windowListener;

	/**
	 * Current state of the jframe.
	 */
	private JFrameState jFrameState;

	/**
	 * Simple constructor.
	 * 
	 * @param jRootPane
	 *            Root pane.
	 */
	public ParityTitlePane(final JRootPane jRootPane) {
		this.jRootPane = jRootPane;
		this.jFrameState = JFrameState.COLLAPSED;
		installSubcomponents();
		installDefaults();
		setLayout(createLayout());
		setToolTipText(getFullTitle());
	}

	/**
	 * @see java.awt.Component#addNotify()
	 * 
	 */
	public void addNotify() {
		super.addNotify();

		uninstallListeners();

		window = SwingUtilities.getWindowAncestor(this);
		if(null != window) {
			setActive(window.isActive());
			installListeners();
		}
		setToolTipText(getFullTitle());
	}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 */
	protected void paintComponent(Graphics g) {
		final Graphics2D g2 = (Graphics2D) g.create();

		paintGradientBackground(g2, getWidth(), getHeight());
		paintTitleText(g2, getHeight());

		g2.dispose();
	}

	private void paintTitleText(final Graphics2D g, final int height) {
		// draw the title
		final Double maximumTitleWidth = toggleJButton.getBounds().getX() - 37;
		final String title = getClippedTitle(g, maximumTitleWidth);
		if(null != title) {
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			g.setColor(BrowserColorUtil.getRGBColor(20, 49, 107, 255));

			final FontMetrics fm = jRootPane.getFontMetrics(g.getFont());
			final int yOffset = ((height - fm.getHeight()) / 2) + fm.getAscent();
			g.drawString(title, 6, yOffset + 1);
		}
	}

	/**
	 * @param pt1
	 * @param pt2
	 * @return
	 */
	private void paintGradientBackground(final Graphics2D g, final int width,
			final int height) {
		final Point pt1 = new Point(0, 0);
		final Point pt2 = new Point(0, height - 1);
		final Color color1 = BrowserColorUtil.getRGBColor(247, 247, 248, 255);
		final Color color2 = BrowserColorUtil.getRGBColor(203, 210, 219, 255);
		final Paint gradientPaint = new GradientPaint(pt1, color1, pt2, color2);
		g.setPaint(gradientPaint);
		g.fillRect(0, 0, width - 1, height - 1);
	}

	/**
	 * @see java.awt.Component#removeNotify()
	 * 
	 */
	public void removeNotify() {
		super.removeNotify();

		uninstallListeners();
		window = null;
	}

	/**
	 * Uninstalls the necessary state.
	 */
	public void uninstall() {
		uninstallListeners();
		window = null;
		removeAll();
	}

	/**
	 * Creates the Buttons that will be placed on the TitlePane.
	 */
	private void createButtons() {
		closeJButton = createTitleButton();
		closeJButton.setAction(new CloseAction());
		closeJButton.setText(null);
		closeJButton.putClientProperty("paintActive", Boolean.TRUE);
		closeJButton.setBorder(null);
		closeJButton.setToolTipText("Close");
		closeJButton.setIcon(SubstanceImageCreator.getCloseIcon(new iTunesColorScheme()));
		ButtonBackgroundDelegate.trackTitleButton(
				closeJButton, ButtonTitleKind.CLOSE);

		toggleJButton = createTitleButton();
		toggleJButton.setAction(new ToggleAction());
		toggleJButton.setText(null);
		toggleJButton.putClientProperty("paintActive", Boolean.TRUE);
		toggleJButton.setBorder(null);
		toggleJButton.setToolTipText("Show Browser");
		toggleJButton.setIcon(ParityImageCreator.getExpandIcon(new iTunesColorScheme()));
		ButtonBackgroundDelegate.trackTitleButton(
				toggleJButton, ButtonTitleKind.REGULAR);
	}

	/**
	 * Returns the <code>LayoutManager</code> that should be installed on the
	 * <code>SubstanceTitlePane</code>.
	 */
	private LayoutManager createLayout() {
		return new TitlePaneLayoutManager();
	}

	/**
	 * Returns a <code>JButton</code> appropriate for placement on the
	 * TitlePane.
	 */
	private JButton createTitleButton() {
		JButton button = new JButton();

		button.setFocusPainted(false);
		button.setFocusable(false);
		button.setOpaque(true);
		return button;
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
			final FontMetrics fm = jRootPane.getFontMetrics(graphics2D.getFont());			
			final String clippedTitle =
				SubstanceCoreUtilities.clipString(fm, maximumTitleWidth.intValue(), fullTitle);

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
	 * Returns the <code>Window</code> the <code>JRootPane</code> is
	 * contained in. This will return null if there is no parent ancestor of the
	 * <code>JRootPane</code>.
	 */
	private Window getWindow() { return window; }

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
	private void installListeners() {
		if(null != window) {
			windowListener = new WindowHandler();
			window.addWindowListener(windowListener);

			propertyChangeListener = new PropertyChangeHandler();
			window.addPropertyChangeListener(propertyChangeListener);
		}
	}

	/**
	 * Adds any sub-Components contained in the <code>SubstanceTitlePane</code>.
	 */
	private void installSubcomponents() {
		createButtons();
		add(closeJButton);
		add(toggleJButton);
	}

	/**
	 * Updates state dependant upon the Window's active state.
	 */
	private void setActive(boolean isActive) {
		Boolean activeB = isActive ? Boolean.TRUE : Boolean.FALSE;

		if (getWindowDecorationStyle() == JRootPane.FRAME) {
			closeJButton.putClientProperty("paintActive", activeB);
			toggleJButton.putClientProperty("paintActive", activeB);
		}
		getRootPane().repaint();
	}

	/**
	 * Uninstalls the necessary listeners.
	 */
	private void uninstallListeners() {
		if(null != this.window) {
			window.removeWindowListener(windowListener);
			windowListener = null;
			
			window.removePropertyChangeListener(propertyChangeListener);
			propertyChangeListener = null;
		}
	}
}
