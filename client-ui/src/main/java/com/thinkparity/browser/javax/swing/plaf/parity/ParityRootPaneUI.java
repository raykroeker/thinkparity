/*
 * Jan 2, 2006
 */
package com.thinkparity.browser.javax.swing.plaf.parity;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.WeakHashMap;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;

import org.jvnet.substance.SubstanceTitlePane;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityRootPaneUI extends BasicRootPaneUI {

	/**
	 * A custom layout manager that is responsible for the layout of
	 * layeredPane, glassPane, menuBar and titlePane, if one has been installed.
	 */
	// NOTE: Ideally this would extends JRootPane.RootLayout, but that
	// would force this to be non-static.
	private static class MetalRootLayout implements LayoutManager2 {

		/**
		 * @see java.awt.LayoutManager2#addLayoutComponent(java.awt.Component,
		 *      java.lang.Object)
		 * 
		 */
		public void addLayoutComponent(Component comp, Object constraints) {}

		/**
		 * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
		 *      java.awt.Component)
		 * 
		 */
		public void addLayoutComponent(String name, Component comp) {}

		/**
		 * @see java.awt.LayoutManager2#getLayoutAlignmentX(java.awt.Container)
		 * 
		 */
		public float getLayoutAlignmentX(Container target) { return 0.0f; }

		/**
		 * @see java.awt.LayoutManager2#getLayoutAlignmentY(java.awt.Container)
		 * 
		 */
		public float getLayoutAlignmentY(Container target) { return 0.0f; }

		/**
		 * @see java.awt.LayoutManager2#invalidateLayout(java.awt.Container)
		 * 
		 */
		public void invalidateLayout(Container target) {}

		/**
		 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
		 * 
		 */
		public void layoutContainer(Container parent) {
			final JRootPane jRootPane = (JRootPane) parent;
			final Rectangle b = jRootPane.getBounds();
			final Insets i = jRootPane.getInsets();
			int nextY = 0;
			final int w = b.width - i.right - i.left;
			final int h = b.height - i.top - i.bottom;

			jRootPane.getLayeredPane().setBounds(i.left, i.top, w, h);
			jRootPane.getGlassPane().setBounds(i.left, i.top, w, h);

			// NOTE This is laying out the children of the layeredPane,
			// technically, these are not our children.
			if(jRootPane.getWindowDecorationStyle() != JRootPane.NONE
					&& (jRootPane.getUI() instanceof ParityRootPaneUI)) {
				final JComponent titlePane = ((ParityRootPaneUI) jRootPane.getUI()).getTitlePane();
				if(null != titlePane) {
					final Dimension tpd = titlePane.getPreferredSize();
					titlePane.setBounds(0, 0, w, tpd.height);
					nextY += tpd.height;
				}
			}
			jRootPane.getContentPane().setBounds(0, nextY, w, h < nextY ? 0 : h - nextY);
		}

		/**
		 * @see java.awt.LayoutManager2#maximumLayoutSize(java.awt.Container)
		 * 
		 */
		public Dimension maximumLayoutSize(Container target) {
			final JRootPane jRootPane = (JRootPane) target;

			// content pane
			final Dimension contentPaneMaximumSize =
				jRootPane.getContentPane().getMaximumSize();
			final int contentPaneWidth = contentPaneMaximumSize.width;
			final int contentPaneHeight = contentPaneMaximumSize.height;

			// menu bar
			final int jMenuBarWidth;
			final int jMenuBarHeight;
			if(null != jRootPane.getJMenuBar()) {
				final Dimension mbd = jRootPane.getJMenuBar().getMaximumSize();
				if(null != mbd) {
					jMenuBarWidth = mbd.width;
					jMenuBarHeight = mbd.height;
				}
				else { jMenuBarWidth = jMenuBarHeight = Integer.MAX_VALUE; }
			}
			else { jMenuBarWidth = jMenuBarHeight = Integer.MAX_VALUE; }

			// title pane
			final int titlePaneWidth;
			final int titlePaneHeight;
			if(jRootPane.getWindowDecorationStyle() != JRootPane.NONE
					&& (jRootPane.getUI() instanceof ParityRootPaneUI)) {
				final JComponent titlePane = ((ParityRootPaneUI) jRootPane.getUI()).getTitlePane();
				if(titlePane != null) {
					final Dimension tpd = titlePane.getMaximumSize();
					titlePaneWidth = tpd.width;
					titlePaneHeight = tpd.height;
				}
				else { titlePaneWidth = titlePaneHeight = Integer.MAX_VALUE; }
			}
			else { titlePaneWidth = titlePaneHeight = Integer.MAX_VALUE; }

			int maxHeight = Math.max(Math.max(contentPaneHeight, jMenuBarHeight), titlePaneHeight);
			// Only overflows if 3 real non-MAX_VALUE heights, sum to >
			// MAX_VALUE
			// Only will happen if sums to more than 2 billion units. Not
			// likely.
			if (maxHeight != Integer.MAX_VALUE) {
				final Insets i = target.getInsets();
				maxHeight = contentPaneHeight + jMenuBarHeight + titlePaneHeight + i.top + i.bottom;
			}

			int maxWidth = Math.max(Math.max(contentPaneWidth, jMenuBarWidth), titlePaneWidth);
			// Similar overflow comment as above
			if(maxWidth != Integer.MAX_VALUE) {
				final Insets i = target.getInsets();
				maxWidth += i.left + i.right;
			}

			return new Dimension(maxWidth, maxHeight);
		}

		/**
		 * Returns the minimum amount of space the layout needs.
		 * 
		 * @param the
		 *            Container for which this layout manager is being used
		 * @return a Dimension object containing the layout's minimum size
		 */
		public Dimension minimumLayoutSize(Container parent) {
			Dimension cpd, mbd, tpd;
			int cpWidth = 0;
			int cpHeight = 0;
			int mbWidth = 0;
			int mbHeight = 0;
			int tpWidth = 0;
			int tpHeight = 0;
			Insets i = parent.getInsets();
			JRootPane root = (JRootPane) parent;

			if (root.getContentPane() != null) {
				cpd = root.getContentPane().getMinimumSize();
			} else {
				cpd = root.getSize();
			}
			if (cpd != null) {
				cpWidth = cpd.width;
				cpHeight = cpd.height;
			}

			if (root.getJMenuBar() != null) {
				mbd = root.getJMenuBar().getMinimumSize();
				if (mbd != null) {
					mbWidth = mbd.width;
					mbHeight = mbd.height;
				}
			}
			if (root.getWindowDecorationStyle() != JRootPane.NONE
					&& (root.getUI() instanceof ParityRootPaneUI)) {
				JComponent titlePane = ((ParityRootPaneUI) root.getUI()).getTitlePane();
				if (titlePane != null) {
					tpd = titlePane.getMinimumSize();
					if (tpd != null) {
						tpWidth = tpd.width;
						tpHeight = tpd.height;
					}
				}
			}

			return new Dimension(Math.max(Math.max(cpWidth, mbWidth), tpWidth)
					+ i.left + i.right, cpHeight + mbHeight + tpWidth + i.top
					+ i.bottom);
		}

		/**
		 * Returns the amount of space the layout would like to have.
		 * 
		 * @param the
		 *            Container for which this layout manager is being used
		 * @return a Dimension object containing the layout's preferred size
		 */
		public Dimension preferredLayoutSize(Container parent) {
			Dimension cpd, mbd, tpd;
			int cpWidth = 0;
			int cpHeight = 0;
			int mbWidth = 0;
			int mbHeight = 0;
			int tpWidth = 0;
			int tpHeight = 0;
			Insets i = parent.getInsets();
			JRootPane root = (JRootPane) parent;

			if (root.getContentPane() != null) {
				cpd = root.getContentPane().getPreferredSize();
			} else {
				cpd = root.getSize();
			}
			if (cpd != null) {
				cpWidth = cpd.width;
				cpHeight = cpd.height;
			}

			if (root.getJMenuBar() != null) {
				mbd = root.getJMenuBar().getPreferredSize();
				if (mbd != null) {
					mbWidth = mbd.width;
					mbHeight = mbd.height;
				}
			}

			if (root.getWindowDecorationStyle() != JRootPane.NONE
					&& (root.getUI() instanceof ParityRootPaneUI)) {
				JComponent titlePane = ((ParityRootPaneUI) root.getUI()).getTitlePane();
				if (titlePane != null) {
					tpd = titlePane.getPreferredSize();
					if (tpd != null) {
						tpWidth = tpd.width;
						tpHeight = tpd.height;
					}
				}
			}

			return new Dimension(Math.max(Math.max(cpWidth, mbWidth), tpWidth)
					+ i.left + i.right, cpHeight + mbHeight + tpWidth + i.top
					+ i.bottom);
		}

		public void removeLayoutComponent(Component comp) {
		}
	}

	/**
	 * MouseInputHandler is responsible for handling resize/moving of the
	 * Window. It sets the cursor directly on the Window when then mouse moves
	 * over a hot spot.
	 */
	private class MouseInputHandler implements MouseInputListener {
		/**
		 * Used to determine the corner the resize is occuring from.
		 */
		private int dragCursor;

		/**
		 * Height of the window when the drag started.
		 */
		private int dragHeight;

		/**
		 * X location the mouse went down on for a drag operation.
		 */
		private int dragOffsetX;

		/**
		 * Y location the mouse went down on for a drag operation.
		 */
		private int dragOffsetY;

		/**
		 * Width of the window when the drag started.
		 */
		private int dragWidth;

		/*
		 * PrivilegedExceptionAction needed by mouseDragged method to obtain new
		 * location of window on screen during the drag.
		 */
		private final PrivilegedExceptionAction getLocationAction = new PrivilegedExceptionAction() {
			public Object run() throws HeadlessException {
				return MouseInfo.getPointerInfo().getLocation();
			}
		};

		/**
		 * Set to true if the drag operation is moving the window.
		 */
		private boolean isMovingWindow;

		public void mouseClicked(MouseEvent ev) {
			Window w = (Window) ev.getSource();
			Frame f = null;

			if (w instanceof Frame) {
				f = (Frame) w;
			} else {
				return;
			}

			Point convertedPoint = SwingUtilities.convertPoint(w,
					ev.getPoint(), getTitlePane());

			int state = f.getExtendedState();
			if (getTitlePane() != null
					&& getTitlePane().contains(convertedPoint)) {
				if ((ev.getClickCount() % 2) == 0
						&& ((ev.getModifiers() & InputEvent.BUTTON1_MASK) != 0)) {
					if (f.isResizable()) {
						if ((state & Frame.MAXIMIZED_BOTH) != 0) {
							f.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
						} else {
							f.setExtendedState(state | Frame.MAXIMIZED_BOTH);
						}
						return;
					}
				}
			}
		}

		public void mouseDragged(MouseEvent ev) {
			Window w = (Window) ev.getSource();
			Point pt = ev.getPoint();

			if (isMovingWindow) {
				Point windowPt;
				try {
					windowPt = (Point) AccessController
							.doPrivileged(getLocationAction);
					windowPt.x = windowPt.x - dragOffsetX;
					windowPt.y = windowPt.y - dragOffsetY;
					w.setLocation(windowPt);
				} catch (PrivilegedActionException e) {
				}
			} else if (dragCursor != 0) {
				Rectangle r = w.getBounds();
				Rectangle startBounds = new Rectangle(r);
				Dimension min = w.getMinimumSize();

				switch (dragCursor) {
				case Cursor.E_RESIZE_CURSOR:
					adjust(r, min, 0, 0, pt.x + (dragWidth - dragOffsetX)
							- r.width, 0);
					break;
				case Cursor.S_RESIZE_CURSOR:
					adjust(r, min, 0, 0, 0, pt.y + (dragHeight - dragOffsetY)
							- r.height);
					break;
				case Cursor.N_RESIZE_CURSOR:
					adjust(r, min, 0, pt.y - dragOffsetY, 0,
							-(pt.y - dragOffsetY));
					break;
				case Cursor.W_RESIZE_CURSOR:
					adjust(r, min, pt.x - dragOffsetX, 0,
							-(pt.x - dragOffsetX), 0);
					break;
				case Cursor.NE_RESIZE_CURSOR:
					adjust(r, min, 0, pt.y - dragOffsetY, pt.x
							+ (dragWidth - dragOffsetX) - r.width,
							-(pt.y - dragOffsetY));
					break;
				case Cursor.SE_RESIZE_CURSOR:
					adjust(r, min, 0, 0, pt.x + (dragWidth - dragOffsetX)
							- r.width, pt.y + (dragHeight - dragOffsetY)
							- r.height);
					break;
				case Cursor.NW_RESIZE_CURSOR:
					adjust(r, min, pt.x - dragOffsetX, pt.y - dragOffsetY,
							-(pt.x - dragOffsetX), -(pt.y - dragOffsetY));
					break;
				case Cursor.SW_RESIZE_CURSOR:
					adjust(r, min, pt.x - dragOffsetX, 0,
							-(pt.x - dragOffsetX), pt.y
									+ (dragHeight - dragOffsetY) - r.height);
					break;
				default:
					break;
				}
				if (!r.equals(startBounds)) {
					w.setBounds(r);
					// Defer repaint/validate on mouseReleased unless dynamic
					// layout is active.
					if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
						w.validate();
						getRootPane().repaint();
					}
				}
			}
		}

		public void mouseEntered(MouseEvent ev) {
			Window w = (Window) ev.getSource();
			// fix for defect 107 (would work also for Metal)
			lastCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
			mouseMoved(ev);
		}

		public void mouseExited(MouseEvent ev) {
			Window w = (Window) ev.getSource();
			w.setCursor(lastCursor);
		}

		public void mouseMoved(MouseEvent ev) {
			JRootPane root = getRootPane();

			if (root.getWindowDecorationStyle() == JRootPane.NONE) {
				return;
			}

			Window w = (Window) ev.getSource();

			Frame f = null;
			Dialog d = null;

			if (w instanceof Frame) {
				f = (Frame) w;
			} else if (w instanceof Dialog) {
				d = (Dialog) w;
			}

			// Update the cursor
			int cursor = getCursor(calculateCorner(w, ev.getX(), ev.getY()));

			if (cursor != 0
					&& ((f != null && (f.isResizable() && (f.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0)) || (d != null && d
							.isResizable()))) {
				w.setCursor(Cursor.getPredefinedCursor(cursor));
			} else {
				w.setCursor(lastCursor);
			}
		}

		public void mousePressed(MouseEvent ev) {
			JRootPane rootPane = getRootPane();

			if (rootPane.getWindowDecorationStyle() == JRootPane.NONE) {
				return;
			}
			Point dragWindowOffset = ev.getPoint();
			Window w = (Window) ev.getSource();
			if (w != null) {
				w.toFront();
			}
			Point convertedDragWindowOffset = SwingUtilities.convertPoint(w,
					dragWindowOffset, getTitlePane());

			Frame f = null;
			Dialog d = null;

			if (w instanceof Frame) {
				f = (Frame) w;
			} else if (w instanceof Dialog) {
				d = (Dialog) w;
			}

			int frameState = (f != null) ? f.getExtendedState() : 0;

			if (getTitlePane() != null
					&& getTitlePane().contains(convertedDragWindowOffset)) {
				if ((f != null && ((frameState & Frame.MAXIMIZED_BOTH) == 0) || (d != null))
						&& dragWindowOffset.y >= BORDER_DRAG_THICKNESS
						&& dragWindowOffset.x >= BORDER_DRAG_THICKNESS
						&& dragWindowOffset.x < w.getWidth()
								- BORDER_DRAG_THICKNESS) {
					isMovingWindow = true;
					dragOffsetX = dragWindowOffset.x;
					dragOffsetY = dragWindowOffset.y;
				}
			} else if (f != null && f.isResizable()
					&& ((frameState & Frame.MAXIMIZED_BOTH) == 0)
					|| (d != null && d.isResizable())) {
				dragOffsetX = dragWindowOffset.x;
				dragOffsetY = dragWindowOffset.y;
				dragWidth = w.getWidth();
				dragHeight = w.getHeight();
				dragCursor = getCursor(calculateCorner(w, dragWindowOffset.x,
						dragWindowOffset.y));
			}
		}

		public void mouseReleased(MouseEvent ev) {
			if (dragCursor != 0 && window != null && !window.isValid()) {
				// Some Window systems validate as you resize, others won't,
				// thus the check for validity before repainting.
				window.validate();
				getRootPane().repaint();
			}
			isMovingWindow = false;
			dragCursor = 0;
		}

		private void adjust(Rectangle bounds, Dimension min, int deltaX,
				int deltaY, int deltaWidth, int deltaHeight) {
			bounds.x += deltaX;
			bounds.y += deltaY;
			bounds.width += deltaWidth;
			bounds.height += deltaHeight;
			if (min != null) {
				if (bounds.width < min.width) {
					int correction = min.width - bounds.width;
					if (deltaX != 0) {
						bounds.x -= correction;
					}
					bounds.width = min.width;
				}
				if (bounds.height < min.height) {
					int correction = min.height - bounds.height;
					if (deltaY != 0) {
						bounds.y -= correction;
					}
					bounds.height = min.height;
				}
			}
		}

		/**
		 * Returns the corner that contains the point <code>x</code>,
		 * <code>y</code>, or -1 if the position doesn't match a corner.
		 */
		private int calculateCorner(Window w, int x, int y) {
			Insets insets = w.getInsets();
			int xPosition = calculatePosition(x - insets.left, w.getWidth()
					- insets.left - insets.right);
			int yPosition = calculatePosition(y - insets.top, w.getHeight()
					- insets.top - insets.bottom);

			if (xPosition == -1 || yPosition == -1) {
				return -1;
			}
			return yPosition * 5 + xPosition;
		}

		/**
		 * Returns an integer indicating the position of <code>spot</code> in
		 * <code>width</code>. The return value will be: 0 if <
		 * BORDER_DRAG_THICKNESS 1 if < CORNER_DRAG_WIDTH 2 if >=
		 * CORNER_DRAG_WIDTH && < width - BORDER_DRAG_THICKNESS 3 if >= width -
		 * CORNER_DRAG_WIDTH 4 if >= width - BORDER_DRAG_THICKNESS 5 otherwise
		 */
		private int calculatePosition(int spot, int width) {
			if (spot < BORDER_DRAG_THICKNESS) {
				return 0;
			}
			if (spot < CORNER_DRAG_WIDTH) {
				return 1;
			}
			if (spot >= (width - BORDER_DRAG_THICKNESS)) {
				return 4;
			}
			if (spot >= (width - CORNER_DRAG_WIDTH)) {
				return 3;
			}
			return 2;
		}

		/**
		 * Returns the Cursor to render for the specified corner. This returns 0
		 * if the corner doesn't map to a valid Cursor
		 */
		private int getCursor(int corner) {
			if (corner == -1) {
				return 0;
			}
			return cursorMapping[corner];
		}
	}

	private class TitleMouseInputHandler extends MouseInputAdapter {
		/**
		 * X location the mouse went down on for a drag operation.
		 */
		private int prevX;

		/**
		 * Y location the mouse went down on for a drag operation.
		 */
		private int prevY;

		public void mouseClicked(MouseEvent ev) {
			Window w = titlePanes.get(ev.getSource());
			Frame f = null;

			if (w instanceof Frame) {
				f = (Frame) w;
			} else {
				return;
			}

			Point convertedPoint = SwingUtilities.convertPoint(w,
					ev.getPoint(), getTitlePane());

			int state = f.getExtendedState();
			if (getTitlePane() != null
					&& getTitlePane().contains(convertedPoint)) {
				if ((ev.getClickCount() % 2) == 0
						&& ((ev.getModifiers() & InputEvent.BUTTON1_MASK) != 0)) {
					if (f.isResizable()) {
						if ((state & Frame.MAXIMIZED_BOTH) != 0) {
							f.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
						} else {
							f.setExtendedState(state | Frame.MAXIMIZED_BOTH);
						}
						return;
					}
				}
			}
		}

		public void mouseDragged(MouseEvent ev) {
			Window w = titlePanes.get(ev.getSource());

			Point windowPt;
			windowPt = w.getLocationOnScreen();

			int currX = ev.getPoint().x + w.getLocationOnScreen().x;
			int currY = ev.getPoint().y + w.getLocationOnScreen().y;

			int deltaX = currX - prevX;
			int deltaY = currY - prevY;

			w.setLocation(windowPt.x + deltaX, windowPt.y + deltaY);

			prevX = currX;
			prevY = currY;
		}

		public void mousePressed(MouseEvent ev) {
			JRootPane rootPane = getRootPane();

			if (rootPane.getWindowDecorationStyle() == JRootPane.NONE) {
				return;
			}
			Window w = titlePanes.get(ev.getSource());
			if (w != null) {
				w.toFront();
			}
			prevX = ev.getPoint().x + w.getLocationOnScreen().x;
			prevY = ev.getPoint().y + w.getLocationOnScreen().y;
		}
	}

	/**
	 * Region from edges that dragging is active from.
	 */
	private static final int BORDER_DRAG_THICKNESS = 5;

	/**
	 * The amount of space (in pixels) that the cursor is changed on.
	 */
	private static final int CORNER_DRAG_WIDTH = 16;

	/**
	 * Maps from positions to cursor type. Refer to calculateCorner and
	 * calculatePosition for details of this.
	 */
	private static final int[] cursorMapping = new int[] {
			Cursor.NW_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR,
			Cursor.N_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,
			Cursor.NE_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, 0, 0, 0,
			Cursor.NE_RESIZE_CURSOR, Cursor.W_RESIZE_CURSOR, 0, 0, 0,
			Cursor.E_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, 0, 0, 0,
			Cursor.SE_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR,
			Cursor.SW_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR,
			Cursor.SE_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR };

	private static WeakHashMap<JComponent, Window> titlePanes = new WeakHashMap<JComponent, Window>();

	/**
	 * Creates a UI for a <code>JRootPane</code>.
	 * 
	 * @param c
	 *            the JRootPane the RootPaneUI will be created for
	 * @return the RootPaneUI implementation for the passed in JRootPane
	 */
	public static ComponentUI createUI(JComponent c) {
		return new ParityRootPaneUI();
	}

	/**
	 * <code>Cursor</code> used to track the cursor set by the user. This is
	 * initially <code>Cursor.DEFAULT_CURSOR</code>.
	 */
	private Cursor lastCursor = Cursor
			.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	/**
	 * The <code>LayoutManager</code> that is set on the
	 * <code>JRootPane</code>.
	 */
	private LayoutManager layoutManager;

	/**
	 * <code>MouseInputListener</code> that is added to the parent
	 * <code>Window</code> the <code>JRootPane</code> is contained in.
	 */
	private MouseInputListener mouseInputListener;

	/**
	 * <code>JRootPane</code> providing the look and feel for.
	 */
	private JRootPane root;

	/**
	 * <code>LayoutManager</code> of the <code>JRootPane</code> before we
	 * replaced it.
	 */
	private LayoutManager savedOldLayout;

	private MouseInputListener titleMouseInputListener;

	/**
	 * <code>JComponent</code> providing window decorations. This will be null
	 * if not providing window decorations.
	 */
	private JComponent titlePane;

	/**
	 * Window the <code>JRootPane</code> is in.
	 */
	private Window window;

	/**
	 * Invokes supers implementation of <code>installUI</code> to install the
	 * necessary state onto the passed in <code>JRootPane</code> to render the
	 * metal look and feel implementation of <code>RootPaneUI</code>. If the
	 * <code>windowDecorationStyle</code> property of the
	 * <code>JRootPane</code> is other than <code>JRootPane.NONE</code>,
	 * this will add a custom <code>Component</code> to render the widgets to
	 * <code>JRootPane</code>, as well as installing a custom
	 * <code>Border</code> and <code>LayoutManager</code> on the
	 * <code>JRootPane</code>.
	 * 
	 * @param c
	 *            the JRootPane to install state onto
	 */
	public void installUI(JComponent c) {
		super.installUI(c);
		root = (JRootPane) c;
		int style = root.getWindowDecorationStyle();
		if (style != JRootPane.NONE) {
			installClientDecorations(root);
		}
	}

	/**
	 * Invoked when a property changes. <code>MetalRootPaneUI</code> is
	 * primarily interested in events originating from the
	 * <code>JRootPane</code> it has been installed on identifying the
	 * property <code>windowDecorationStyle</code>. If the
	 * <code>windowDecorationStyle</code> has changed to a value other than
	 * <code>JRootPane.NONE</code>, this will add a <code>Component</code>
	 * to the <code>JRootPane</code> to render the window decorations, as well
	 * as installing a <code>Border</code> on the <code>JRootPane</code>.
	 * On the other hand, if the <code>windowDecorationStyle</code> has
	 * changed to <code>JRootPane.NONE</code>, this will remove the
	 * <code>Component</code> that has been added to the
	 * <code>JRootPane</code> as well resetting the Border to what it was
	 * before <code>installUI</code> was invoked.
	 * 
	 * @param e
	 *            A PropertyChangeEvent object describing the event source and
	 *            the property that has changed.
	 */
	public void propertyChange(PropertyChangeEvent e) {
		super.propertyChange(e);

		String propertyName = e.getPropertyName();
		if (propertyName == null) {
			return;
		}

		if (propertyName.equals("windowDecorationStyle")) {
			JRootPane root = (JRootPane) e.getSource();
			int style = root.getWindowDecorationStyle();

			// This is potentially more than needs to be done,
			// but it rarely happens and makes the install/uninstall process
			// simpler. MetalTitlePane also assumes it will be recreated if
			// the decoration style changes.
			uninstallClientDecorations(root);
			if (style != JRootPane.NONE) {
				installClientDecorations(root);
			}
		} else if (propertyName.equals("ancestor")) {
			uninstallWindowListeners(root);
			if (((JRootPane) e.getSource()).getWindowDecorationStyle() != JRootPane.NONE) {
				installWindowListeners(root, root.getParent());
			}
		}
		return;
	}

	/**
	 * Invokes supers implementation to uninstall any of its state. This will
	 * also reset the <code>LayoutManager</code> of the <code>JRootPane</code>.
	 * If a <code>Component</code> has been added to the
	 * <code>JRootPane</code> to render the window decoration style, this
	 * method will remove it. Similarly, this will revert the Border and
	 * LayoutManager of the <code>JRootPane</code> to what it was before
	 * <code>installUI</code> was invoked.
	 * 
	 * @param c
	 *            the JRootPane to uninstall state from
	 */
	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
		uninstallClientDecorations(root);

		layoutManager = null;
		mouseInputListener = null;
		root = null;
	}

	/**
	 * Install the border on the JRootPane. If the window decoration style is
	 * <code>JRootPane.NONE</code>; the border is removed. Otherwise the
	 * <code>LookAndFeel.installBorder(Component, "RootPane.border")</code>
	 * api is called.
	 * 
	 * @param jRootPane
	 *            The root pane.
	 */
	void installBorder(final JRootPane jRootPane) {
		final int style = jRootPane.getWindowDecorationStyle();
		if(style == JRootPane.NONE) { LookAndFeel.uninstallBorder(jRootPane); }
		else { LookAndFeel.installBorder(jRootPane, "RootPane.border"); }
	}

	/**
	 * Returns a <code>LayoutManager</code> that will be set on the
	 * <code>JRootPane</code>.
	 */
	private LayoutManager createLayoutManager() {
		return new MetalRootLayout();
	}

	/**
	 * Returns a <code>MouseListener</code> that will be added to the
	 * <code>Window</code> containing the <code>JRootPane</code>.
	 */
	private MouseInputListener createWindowMouseInputListener(JRootPane root) {
		return new MouseInputHandler();
	}

	/**
	 * Returns the <code>JRootPane</code> we're providing the look and feel
	 * for.
	 */
	private JRootPane getRootPane() {
		return root;
	}

	/**
	 * Returns the <code>JComponent</code> rendering the title pane. If this
	 * returns null, it implies there is no need to render window decorations.
	 * 
	 * @return the current window title pane, or null
	 * @see #setTitlePane
	 */
	private JComponent getTitlePane() { return titlePane; }

	/**
	 * Installs the necessary state onto the JRootPane to render client
	 * decorations. This is ONLY invoked if the <code>JRootPane</code> has a
	 * decoration style other than <code>JRootPane.NONE</code>.
	 */
	private void installClientDecorations(final JRootPane jPaneRoot) {
		installBorder(jPaneRoot);
		setTitlePane(jPaneRoot, new ParityTitlePane());
		installWindowListeners(jPaneRoot, jPaneRoot.getParent());
		installLayout(jPaneRoot);
		if (window != null) {
			jPaneRoot.revalidate();
			jPaneRoot.repaint();
		}
	}

	/**
	 * Installs the appropriate LayoutManager on the <code>JRootPane</code> to
	 * render the window decorations.
	 */
	private void installLayout(JRootPane root) {
		if (layoutManager == null) {
			layoutManager = createLayoutManager();
		}
		savedOldLayout = root.getLayout();
		root.setLayout(layoutManager);
	}

	/**
	 * Installs the necessary Listeners on the parent <code>Window</code>, if
	 * there is one.
	 * <p>
	 * This takes the parent so that cleanup can be done from
	 * <code>removeNotify</code>, at which point the parent hasn't been reset
	 * yet.
	 * 
	 * @param parent
	 *            The parent of the JRootPane
	 */
	private void installWindowListeners(JRootPane root, Component parent) {
		if (parent instanceof Window) {
			window = (Window) parent;
		} else {
			window = SwingUtilities.getWindowAncestor(parent);
		}
		titlePanes.put(titlePane, window);
		if (window != null) {
			if (mouseInputListener == null) {
				mouseInputListener = createWindowMouseInputListener(root);
			}
			window.addMouseListener(mouseInputListener);
			window.addMouseMotionListener(mouseInputListener);

			if (titlePane != null) {
				if (titleMouseInputListener == null) {
					titleMouseInputListener = new TitleMouseInputHandler();
				}
				titlePane.addMouseMotionListener(titleMouseInputListener);
				titlePane.addMouseListener(titleMouseInputListener);
			}
		}
	}

	/**
	 * Sets the window title pane -- the JComponent used to provide a plaf a way
	 * to override the native operating system's window title pane with one
	 * whose look and feel are controlled by the plaf. The plaf creates and sets
	 * this value; the default is null, implying a native operating system
	 * window title pane.
	 * 
	 * @param content
	 *            the <code>JComponent</code> to use for the window title
	 *            pane.
	 */
	private void setTitlePane(JRootPane root, JComponent titlePane) {
		JLayeredPane layeredPane = root.getLayeredPane();
		JComponent oldTitlePane = getTitlePane();

		if (oldTitlePane != null) {
			// fix for defect 109 - memory leak on theme change
			if (oldTitlePane instanceof SubstanceTitlePane)
				((SubstanceTitlePane)oldTitlePane).uninstall();
//			oldTitlePane.setVisible(false);
			layeredPane.remove(oldTitlePane);
		}
		if (titlePane != null) {
			layeredPane.add(titlePane, JLayeredPane.FRAME_CONTENT_LAYER);
			titlePane.setVisible(true);
		}
		this.titlePane = titlePane;
	}

	/**
	 * Removes any border that may have been installed.
	 */
	private void uninstallBorder(JRootPane root) {
		LookAndFeel.uninstallBorder(root);
	}

	/**
	 * Uninstalls any state that <code>installClientDecorations</code> has
	 * installed.
	 * <p>
	 * NOTE: This may be called if you haven't installed client decorations yet
	 * (ie before <code>installClientDecorations</code> has been invoked).
	 */
	private void uninstallClientDecorations(JRootPane root) {
		uninstallBorder(root);
		uninstallWindowListeners(root);
		setTitlePane(root, null);
		uninstallLayout(root);
		// We have to revalidate/repaint root if the style is JRootPane.NONE
		// only. When we needs to call revalidate/repaint with other styles
		// the installClientDecorations is always called after this method
		// imediatly and it will cause the revalidate/repaint at the proper
		// time.
		int style = root.getWindowDecorationStyle();
		if (style == JRootPane.NONE) {
			root.repaint();
			root.revalidate();
		}
		// Reset the cursor, as we may have changed it to a resize cursor
		if (window != null) {
			window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		window = null;
	}

	/**
	 * Uninstalls the previously installed <code>LayoutManager</code>.
	 */
	private void uninstallLayout(JRootPane root) {
		if (savedOldLayout != null) {
			root.setLayout(savedOldLayout);
			savedOldLayout = null;
		}
	}

	/**
	 * Uninstalls the necessary Listeners on the <code>Window</code> the
	 * Listeners were last installed on.
	 */
	private void uninstallWindowListeners(JRootPane root) {
		if (window != null) {
			window.removeMouseListener(mouseInputListener);
			window.removeMouseMotionListener(mouseInputListener);
		}
		if (titlePane != null) {
			titlePane.removeMouseListener(titleMouseInputListener);
			titlePane.removeMouseMotionListener(titleMouseInputListener);
		}
	}
}
