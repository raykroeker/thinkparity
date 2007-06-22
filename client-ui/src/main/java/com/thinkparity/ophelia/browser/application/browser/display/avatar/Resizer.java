/**
 * Created On: 30-Aug-06 9:16:21 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.Constants;
import com.thinkparity.ophelia.browser.Constants.Resize;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserWindow;
import com.thinkparity.ophelia.browser.util.window.WindowUtil;
import com.thinkparity.ophelia.browser.util.window.WindowUtilProvider;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Resizer {

    /** The original component position when start a move. */
    private static Point moveComponentOrigin;

    /** Flag to indicate if currently movement dragging. */
    private static Boolean moveDragging = Boolean.FALSE;

    /** The original mouse position when start a move. */
    private static Point moveMouseOrigin;

    /** Flag to indicate if currently resize dragging. */
    private static Boolean resizeDragging = Boolean.FALSE;

    /** The resize offset size in the x direction. */
    private static int resizeOffsetX;

    /** The resize offset size in the y direction. */
    private static int resizeOffsetY;

    /** An instance of <code>WindowUtil</code>. */
    private static final WindowUtil WINDOW_UTIL;

    static {
        WINDOW_UTIL = WindowUtilProvider.getInstance().getWindowUtil();
    }

    /** A map of JComponent to AncestorListener. */
    private final Map<JComponent, AncestorListener> ancestorListeners;

    /** Browser */
    private final Browser browser;

    /** Component */
    private final Component component;

    /** Flag to indicate if the resizer is enabled or not. */
    private Boolean enabled = Boolean.TRUE;    

    /** Flag to indicate if initialized. */
    private Boolean initialized = Boolean.FALSE;

    /** A map of components to mouse adapters. */
    private final Map<Component, MouseAdapter> mouseAdapters;

    /** A map of components to mouse motion adapters. */
    private final Map<Component, MouseMotionAdapter> mouseMotionAdapters;

    /** Resize mode. */
    private ResizeDirection resizeDirection = ResizeDirection.NONE;

    /** Resize edges */
    private final ResizeEdges resizeEdges;

    /** Flag to indicate if dragging the centre of the control will cause moving */
    private final Boolean supportMouseMove;

    /** A map of Window to WindowStateListener. */
    private final Map<Window, WindowStateListener> windowStateListeners;

    /**
     * Create a Resizer to resize the ancestor Window of a JComponent.
     * 
     * @param browser
     *            The browser.
     * @param jComponent
     *            The JComponent whose ancestor will be resizable.
     * @param supportMouseMove
     *            Flag to indicate if dragging the centre of the control will cause moving.           
     * @param resizeEdges
     *            The resize edges (eg. top, middle, bottom)
     */
    public Resizer(final Browser browser, final JComponent jComponent,
            final Boolean supportMouseMove, final ResizeEdges resizeEdges) {
        this.browser = browser;
        this.component = jComponent;
        this.supportMouseMove = supportMouseMove;
        this.resizeEdges = resizeEdges;
        this.mouseMotionAdapters = new HashMap<Component, MouseMotionAdapter>();
        this.mouseAdapters = new HashMap<Component, MouseAdapter>();
        this.ancestorListeners = new HashMap<JComponent, AncestorListener>();
        this.windowStateListeners = new HashMap<Window, WindowStateListener>();
        initComponents();
        installAncestorWindowStateListener(jComponent);
        setEnabled(!SwingUtil.isInMaximizedWindow(jComponent));
    }

    /**
     * Create a Resizer for a Window.
     * 
     * @param browser
     *            The browser.
     * @param window
     *            The Window.
     * @param supportMouseMove
     *            Flag to indicate if dragging the centre of the control will cause moving.           
     * @param resizeEdges
     *            The resize edges (eg. top, middle, bottom)
     */
    public Resizer(final Browser browser, final Window window,
            final Boolean supportMouseMove, final ResizeEdges resizeEdges) {
        this.browser = browser;
        this.component = window;
        this.supportMouseMove = supportMouseMove;
        this.resizeEdges = resizeEdges;
        this.mouseMotionAdapters = new HashMap<Component, MouseMotionAdapter>();
        this.mouseAdapters = new HashMap<Component, MouseAdapter>();
        this.ancestorListeners = new HashMap<JComponent, AncestorListener>();
        this.windowStateListeners = new HashMap<Window, WindowStateListener>();
        initComponents();
        installWindowStateListener(window);
        setEnabled(!SwingUtil.isInMaximizedWindow(window));
    }
    /**
     * These get and set methods are used by classes that intend to do
     * their own resizing. (For example, the bottom right resize control.)
     */
    public Boolean isResizeDragging() {
        return resizeDragging;
    }

    /**
     * Remove all listeners for this resizer.
     */
    public void removeAllListeners() {
        for (final Entry<Component, MouseMotionAdapter> entry : mouseMotionAdapters.entrySet()) {
            entry.getKey().removeMouseMotionListener(entry.getValue());
        }
        for (final Entry<Component, MouseAdapter> entry : mouseAdapters.entrySet()) {
            entry.getKey().removeMouseListener(entry.getValue());
        }
        for (final Entry<JComponent, AncestorListener> entry : ancestorListeners.entrySet()) {
            entry.getKey().removeAncestorListener(entry.getValue());
        }
        for (final Entry<Window, WindowStateListener> entry : windowStateListeners.entrySet()) {
            entry.getKey().removeWindowStateListener(entry.getValue());
        }
    }

    public void setResizeDragging(final Boolean resizeDragging) {
        Resizer.resizeDragging = resizeDragging;
    }

    private void adjustCursor(final ResizeDirection resizeDirection, final Component component) {       
        switch (resizeDirection) {
        case NW:
            setCursor(component, Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
            break;
        case NE:
            setCursor(component, Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
            break;
        case SW:
            setCursor(component, Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
            break;
        case SE:
            setCursor(component, Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
            break;
        case W:
            setCursor(component, Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
            break;
        case E:
            setCursor(component, Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
            break;
        case N:
            setCursor(component, Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
            break;
        case S:
            setCursor(component, Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
            break;
        default:
            resetCursor(component);
            break;
        }
    }

    /**
     * Reset the cursor.
     * 
     * @param component
     *            A <code>Component</code>.
     */
    private void resetCursor(final Component component) {
        final int cursorType = component.getCursor().getType();
        if (cursorType == Cursor.NW_RESIZE_CURSOR
                || cursorType == Cursor.NE_RESIZE_CURSOR
                || cursorType == Cursor.SW_RESIZE_CURSOR
                || cursorType == Cursor.SE_RESIZE_CURSOR
                || cursorType == Cursor.W_RESIZE_CURSOR
                || cursorType == Cursor.E_RESIZE_CURSOR
                || cursorType == Cursor.N_RESIZE_CURSOR
                || cursorType == Cursor.S_RESIZE_CURSOR) {
            setCursor(component, Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Set the component cursor.
     * 
     * @param component
     *            A <code>Component</code>.
     * @param cursor
     *            A <code>Cursor</code>.
     */
    private void setCursor(final Component component, final Cursor cursor) {
        SwingUtil.setCursor(component, cursor);
    }

    /**
     * Adjust size to account for minimum size.
     * 
     * @param component
     *          The component to resize.
     * @param size
     *          The size.
     *          
     * @return The adjusted size.
     */
    private Dimension adjustForMinimumSize(final Component component, final Dimension size) {
        Dimension adjustedSize = new Dimension(size);
        Dimension minSize = component.getMinimumSize();
        if (size.getWidth() < minSize.getWidth()) {
            adjustedSize.width = (int)minSize.getWidth();
        }
        if (size.getHeight() < minSize.getHeight()) {
            adjustedSize.height = (int)minSize.getHeight();
        }
        
        return adjustedSize;
    }

    /**
     * Adjust size and move to account for minimum size.
     * 
     * @param component
     *          The component to resize and move.
     * @param sizeAndLocation
     *          The rectangle representing size and location.
     *          
     * @return The adjusted size and location.
     */
    private Rectangle adjustForMinimumSize(final Component component, final Rectangle sizeAndLocation) {
        Rectangle adjustedSizeAndLocation = new Rectangle(sizeAndLocation);
        Dimension minSize = component.getMinimumSize();
        if (sizeAndLocation.getWidth() < minSize.getWidth()) {
            if (sizeAndLocation.x != component.getLocation().x) {
                adjustedSizeAndLocation.x += (sizeAndLocation.getWidth() - minSize.getWidth());
            }
            adjustedSizeAndLocation.width = (int)minSize.getWidth();
        }
        if (sizeAndLocation.getHeight() < minSize.getHeight()) {
            if (sizeAndLocation.y != component.getLocation().y) {
                adjustedSizeAndLocation.y += (sizeAndLocation.getHeight() - minSize.getHeight());
            }
            adjustedSizeAndLocation.height = (int)minSize.getHeight();
        }
        return adjustedSizeAndLocation;
    }

    private ResizeDirection filterAllowedDirections(final ResizeDirection rd) {
        ResizeDirection newDirection = rd;
        
        switch (resizeEdges) {
        case NO_EDGE:
            newDirection = ResizeDirection.NONE;
            break;
        case ALL_EDGES:
            newDirection = rd;
            break;
        case LEFT:
            if (rd==ResizeDirection.SW) { newDirection = ResizeDirection.W; }
            else if (rd==ResizeDirection.NW) { newDirection = ResizeDirection.W; }
            else if (rd!=ResizeDirection.W) { newDirection = ResizeDirection.NONE; }
            break;
        case RIGHT:
            if (rd==ResizeDirection.SE) { newDirection = ResizeDirection.E; }
            else if (rd==ResizeDirection.NE) { newDirection = ResizeDirection.E; }
            else if (rd!=ResizeDirection.E) { newDirection = ResizeDirection.NONE; }
            break;
        case TOP:
            if (rd==ResizeDirection.SW) { newDirection = ResizeDirection.W; }
            else if (rd==ResizeDirection.SE) { newDirection = ResizeDirection.E; }
            else if (rd==ResizeDirection.S) { newDirection = ResizeDirection.NONE; }
            break;
        case BOTTOM:
            if (rd==ResizeDirection.NW) { newDirection = ResizeDirection.W; }
            else if (rd==ResizeDirection.NE) { newDirection = ResizeDirection.E; }
            else if (rd==ResizeDirection.N) { newDirection = ResizeDirection.NONE; }
            break;
        case MIDDLE:
            if (rd==ResizeDirection.SW) { newDirection = ResizeDirection.W; }
            else if (rd==ResizeDirection.SE) { newDirection = ResizeDirection.E; }
            else if (rd==ResizeDirection.S) { newDirection = ResizeDirection.NONE; }
            else if (rd==ResizeDirection.NW) { newDirection = ResizeDirection.W; }
            else if (rd==ResizeDirection.NE) { newDirection = ResizeDirection.E; }
            else if (rd==ResizeDirection.N) { newDirection = ResizeDirection.NONE; }
            break;
        default:
            newDirection = ResizeDirection.NONE;
            break;
        }

        return newDirection;
    }

    private void formMouseDragged(final java.awt.event.MouseEvent evt, final Component component) {
        if (moveDragging) {
            formMouseDraggedMove(evt, component);
        } else if (resizeDragging) {
            formMouseDraggedResize(evt, component);
        }
    }

    private void formMouseDraggedMove(final java.awt.event.MouseEvent evt, final Component component) {
        final Component componentToMove = getComponentAncestor(component);
        final Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        final Point componentLocation = componentToMove.getLocation();

        final Point moveOffset = new Point(
                (mouseLocation.x-moveMouseOrigin.x) - (componentLocation.x-moveComponentOrigin.x),
                (mouseLocation.y-moveMouseOrigin.y) - (componentLocation.y-moveComponentOrigin.y));

        // The following line would almost work, but it creates jittery behavior.
        // final Point moveOffset = new Point(evt.getPoint().x-resizeOffsetX, evt.getPoint().y-resizeOffsetY);

        move(componentToMove, moveOffset);
    }

    private void formMouseDraggedResize(final java.awt.event.MouseEvent evt, final Component component) {
        Dimension resizeOffset = null;
        Point moveOffset = null;
        final Window componentAncestor = getComponentAncestor(component);

        switch (resizeDirection) {
        case NW:
            moveOffset = new Point(evt.getPoint().x - resizeOffsetX, evt.getPoint().y - resizeOffsetY);
            resizeOffset = new Dimension(resizeOffsetX - evt.getPoint().x, resizeOffsetY - evt.getPoint().y);
            break;
        case NE:
            moveOffset = new Point(0, evt.getPoint().y - resizeOffsetY);
            resizeOffset = new Dimension(evt.getPoint().x - resizeOffsetX, resizeOffsetY - evt.getPoint().y);
            break;
        case SW:
            moveOffset = new Point(evt.getPoint().x - resizeOffsetX, 0);
            resizeOffset = new Dimension(resizeOffsetX - evt.getPoint().x, evt.getPoint().y - resizeOffsetY);
            break;
        case SE:           
            resizeOffset = new Dimension(evt.getPoint().x - resizeOffsetX, evt.getPoint().y - resizeOffsetY);
            break;
        case W:
            moveOffset = new Point(evt.getPoint().x - resizeOffsetX, 0);
            resizeOffset = new Dimension(resizeOffsetX - evt.getPoint().x, 0);
            break;            
        case E:
            resizeOffset = new Dimension(evt.getPoint().x - resizeOffsetX, 0);
            break;
        case N:
            moveOffset = new Point(0, evt.getPoint().y - resizeOffsetY);
            resizeOffset = new Dimension(0, resizeOffsetY - evt.getPoint().y);
            break;
        case S:
            resizeOffset = new Dimension(0, evt.getPoint().y - resizeOffsetY);
            break;
        default:
            break;
        }

        // Save the component width (ie. the component that the mouse is dragging in)
        final double oldComponentWidth = component.getSize().getWidth();
        final double oldComponentHeight = component.getSize().getHeight();

        // Resize and maybe also move
        if (null != resizeOffset) {
            if (null != moveOffset) {
                resizeAndMove(componentAncestor, resizeOffset, moveOffset);
            } else {
                resize(componentAncestor, resizeOffset);
            }
        }

        // The width of the control may change when the window is resized.
        if ((resizeDirection == ResizeDirection.NE) ||
            (resizeDirection == ResizeDirection.E) ||
            (resizeDirection == ResizeDirection.SE)) {
            resizeOffsetX += (component.getSize().getWidth() - oldComponentWidth);
        }

        // The height of the control may change when the window is resized.
        if ((resizeDirection == ResizeDirection.SW) ||
            (resizeDirection == ResizeDirection.S) ||
            (resizeDirection == ResizeDirection.SE)) {
            resizeOffsetY += (component.getSize().getHeight() - oldComponentHeight);
        }
    }

    private void formMouseEntered(final java.awt.event.MouseEvent evt, final Component component) {
        if (enabled &&(!resizeDragging) && (!moveDragging) && (resizeEdges!=ResizeEdges.NO_EDGE)) {
            initResize(evt, component);
        }
    }

    private void formMouseExited(final java.awt.event.MouseEvent evt, final Component component) {
        if ((!resizeDragging) && (!moveDragging)) {
            resizeDirection = ResizeDirection.NONE;
            resetCursor(component);
        }
    }

    private void formMouseMoved(final java.awt.event.MouseEvent evt, final Component component) {
        if (enabled && (!resizeDragging) && (!moveDragging) && (resizeEdges!=ResizeEdges.NO_EDGE)) {
            initResize(evt, component);
        }
    }

    private void formMousePressed(final java.awt.event.MouseEvent evt, final Component component) {
        if (enabled) {
            resizeOffsetX = evt.getPoint().x;
            resizeOffsetY = evt.getPoint().y;
            if ((resizeDirection == ResizeDirection.NONE) && (supportMouseMove)) {
                moveDragging = Boolean.TRUE;
                moveMouseOrigin = MouseInfo.getPointerInfo().getLocation();
                moveComponentOrigin = getComponentAncestor(component).getLocation();
            } else if (resizeDirection != ResizeDirection.NONE) {
                resizeDragging = Boolean.TRUE;
            }
        }
    }

    private void formMouseReleased(final java.awt.event.MouseEvent evt, final Component component) {
        resizeDragging = Boolean.FALSE;
        moveDragging = Boolean.FALSE;
        
        // Sometimes when the mouse is released it is not over the
        // component anymore, for example, when minimizing beyond
        // the minimum size of the dialog. For this reason it is
        // best to reset the cursor rather than hope for the 
        // mouse exit event.
        resizeDirection = ResizeDirection.NONE;
        resetCursor(component);
    }

    /**
     * Get the component ancestor.
     * 
     * @param jComponent
     *            The <code>JComponent</code>.
     * @return The ancestor <code>Window</code>.
     */
    private Window getComponentAncestor(final Component component) {
        final Window ancestor;
        if ((component instanceof JDialog) || (component instanceof BrowserWindow)) {
            ancestor = (Window) component;
        } else {
            ancestor = SwingUtilities.getWindowAncestor(component);
        }
        return ancestor;
    }

    private void initComponents() {
        if (!initialized) {
            final MouseMotionAdapter mouseMotionAdapter = new MouseMotionAdapter() {
                public void mouseDragged(java.awt.event.MouseEvent evt) {
                    formMouseDragged(evt, component);
                }
                public void mouseMoved(java.awt.event.MouseEvent evt) {
                    formMouseMoved(evt, component);
                }
            };
            final MouseAdapter mouseAdapter = new MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    formMouseEntered(evt, component);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    formMouseExited(evt, component);
                }
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    formMousePressed(evt, component);
                }
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    formMouseReleased(evt, component);
                }
            };
            component.addMouseMotionListener(mouseMotionAdapter);
            component.addMouseListener(mouseAdapter);

            // Save so they can be removed later.
            mouseMotionAdapters.put(component, mouseMotionAdapter);
            mouseAdapters.put(component, mouseAdapter);

            final Window window = SwingUtilities.getWindowAncestor(component);
            if (null!=window && window instanceof JDialog) {
                final MouseMotionAdapter mouseMotionAdapterWindow = new MouseMotionAdapter() {
                    public void mouseDragged(java.awt.event.MouseEvent evt) {
                        formMouseDragged(evt, window);
                    }
                    public void mouseMoved(java.awt.event.MouseEvent evt) {
                        formMouseMoved(evt, window);
                    }
                };
                final MouseAdapter mouseAdapterWindow = new MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        formMouseEntered(evt, window);
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        formMouseExited(evt, window);
                    }
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        formMousePressed(evt, window);
                    }
                    public void mouseReleased(java.awt.event.MouseEvent evt) {
                        formMouseReleased(evt, window);
                    }
                };
                window.addMouseMotionListener(mouseMotionAdapterWindow);
                window.addMouseListener(mouseAdapterWindow);

                // Save so they can be removed later.
                mouseMotionAdapters.put(window, mouseMotionAdapterWindow);
                mouseAdapters.put(window, mouseAdapterWindow);
            }

            initialized = Boolean.TRUE;
        }
    }

    private void initResize(final java.awt.event.MouseEvent e, final Component component) {
        final Point p = e.getPoint();
        final Dimension d = component.getSize();
        final Boolean lowX = (p.getX() < Resize.EDGE_PIXEL_BUFFER);
        final Boolean highX = (p.getX() >= d.getWidth() - Resize.EDGE_PIXEL_BUFFER);
        final Boolean lowY = (p.getY() < Resize.EDGE_PIXEL_BUFFER);
        final Boolean highY = (p.getY() >= d.getHeight() - Resize.EDGE_PIXEL_BUFFER);
        
        if (lowX && lowY) {
            resizeDirection = ResizeDirection.NW;
        } else if (highX && lowY) {
            resizeDirection = ResizeDirection.NE;
        } else if (lowX && highY) {
            resizeDirection = ResizeDirection.SW;
        } else if (highX && highY) {
            resizeDirection = ResizeDirection.SE;
        } else if (lowX) {
            resizeDirection = ResizeDirection.W;
        } else if (highX) {
            resizeDirection = ResizeDirection.E;
        } else if (lowY) {
            resizeDirection = ResizeDirection.N;
        } else if (highY) {
            resizeDirection = ResizeDirection.S;
        } else {
            resizeDirection = ResizeDirection.NONE;
        }
        
        // Don't allow resize in certain directions for components.
        // The borders should always allow resize in all directions.
        if (!(component instanceof JDialog) && !(component instanceof BrowserWindow)) {
            resizeDirection = filterAllowedDirections(resizeDirection);
        }
        
        adjustCursor(resizeDirection, component);
    }    

    /**
     * Install a window state listener on the ancestor of the component.
     * If the ancestor window is maximized then the resizer will be disabled.
     * 
     * @param jComponent
     *            The <code>JComponent</code>.
     */
    private void installAncestorWindowStateListener(final JComponent jComponent) {
        final Window window = getComponentAncestor(jComponent);
        // Handle the case that the JComponent is not yet added to a Window.
        // In this case do the install when the component is added to a window.
        if (null == window) {
            final AncestorListener ancestorListener = new AncestorListener() {
                public void ancestorAdded(final AncestorEvent event) {
                    final Window window = getComponentAncestor(jComponent);
                    if (null != window) {
                        installWindowStateListener(window);
                        setEnabled(!SwingUtil.isInMaximizedWindow(window));
                    }
                }
                public void ancestorMoved(final AncestorEvent event) {}
                public void ancestorRemoved(final AncestorEvent event) {}
            };
            jComponent.addAncestorListener(ancestorListener);
            ancestorListeners.put(jComponent, ancestorListener);
        } else {
            installWindowStateListener(window);
        }
    }   

    /**
     * Install a window state listener.
     * If the ancestor window is maximized then the resizer will be disabled.
     * 
     * @param window
     *            The <code>Window</code>.
     */
    private void installWindowStateListener(final Window window) {
        final WindowStateListener windowStateListener = new WindowStateListener() {
            public void windowStateChanged(final WindowEvent e) {
                if (e.getID() == WindowEvent.WINDOW_STATE_CHANGED) {
                    setEnabled(!isMaximized(e));
                }
            }
        };
        window.addWindowStateListener(windowStateListener);
        windowStateListeners.put(window, windowStateListener);
    }

    /**
     * Determine if the window event indicates a maximized JFrame window.
     * 
     * @param e
     *            A <code>WindowEvent</code>.
     */
    private Boolean isMaximized(final WindowEvent e) {
        return (e.getNewState() & JFrame.MAXIMIZED_BOTH) > 0;
    }

    /**
     * Move the component.
     * 
     * @param component
     *          The component to resize.
     * @param moveOffset
     *          The amount to move.
     */    
    private void move(final Component component, final Point moveOffset) {
        if ((moveOffset.x != 0 ) || (moveOffset.y != 0 )) {
            final Point location = component.getLocation();
            location.x += moveOffset.x;
            location.y += moveOffset.y;
            component.setLocation(location.x, location.y);
        }
    }

    /**
     * Resize the component.
     * 
     * @param component
     *          The component to resize.
     * @param resizeOffset
     *          The amount to resize.
     */
    private void resize(final Window window, final Dimension resizeOffset) {
        if ((resizeOffset.width != 0) || (resizeOffset.height != 0)) {
            if (window instanceof BrowserWindow) {
                if (null!=browser) {
                    browser.resizeBrowserWindow(resizeOffset);
                }
            } else {
                Dimension size = window.getSize();
                size.width += resizeOffset.width;
                size.height += resizeOffset.height;
                size = adjustForMinimumSize(window, size);
                if (!window.getSize().equals(size)) {
                	window.setSize(size);
                    roundCorners(window);
                    window.validate();
                }
            }
        }
    }

    /**
     * Resize and move the component.
     * 
     * @param component
     *          The component to resize.
     * @param resizeOffset
     *          The amount to resize.
     * @param moveOffset
     *          The amount to move.
     */
    private void resizeAndMove(final Window window,
			final Dimension resizeOffset, final Point moveOffset) {
        if ((resizeOffset.width!=0) || (resizeOffset.height!=0) ||
            (moveOffset.x != 0 ) || (moveOffset.y != 0 )) {        
            if (window instanceof BrowserWindow) {
                if (null!=browser) {
                    browser.moveAndResizeBrowserWindow(moveOffset, resizeOffset);
                }
            } else {
                Rectangle sizeAndLocation = window.getBounds();
                sizeAndLocation.width += resizeOffset.width;
                sizeAndLocation.height += resizeOffset.height;
                sizeAndLocation.x += moveOffset.x;
                sizeAndLocation.y += moveOffset.y;
                sizeAndLocation = adjustForMinimumSize(window, sizeAndLocation);
                if (!window.getBounds().equals(sizeAndLocation)) {
                	window.setBounds(sizeAndLocation);
                    roundCorners(window);
                    window.validate();
                }
            }
        }
    }

    /**
     * Make the corners round.
     */
    private void roundCorners(final Window window) {
    	WINDOW_UTIL.applyRoundedEdges(window, Constants.WindowUtil.DEFAULT_SIZE);
    }

    /**
     * Enable or disable the resizer (affects both moving and resizing)
     * 
     * @param enabled
     *          Flag to enable or disable the resizer.
     */
    private void setEnabled(final Boolean enabled) {
        this.enabled = enabled;
    }

    public enum ResizeEdges { ALL_EDGES, BOTTOM, LEFT, MIDDLE, NO_EDGE, RIGHT, TOP }
    private enum ResizeDirection { E, N, NE, NONE, NW, S, SE, SW, W }
}
