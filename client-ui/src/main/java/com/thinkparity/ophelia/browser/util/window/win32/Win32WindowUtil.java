/*
 * Created On:  Apr 14, 2007 10:55:55 AM
 */
package com.thinkparity.ophelia.browser.util.window.win32;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Window;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * <b>Title:</b>thinkParity OpheliaUI Win32 Window Util<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Win32WindowUtil implements
        com.thinkparity.ophelia.browser.util.window.WindowUtil {

    /** The java awt native library name <code>String</code>. */
    private static final String JAWT_LIBNAME;

    /**
     * A <code>boolean</code> flag indicating whether or not the window util
     * native library was loaded.
     */
    private static boolean loaded;

    /** A <code>Log4JWrapper</code>. */
    private static final Log4JWrapper LOGGER;

    /** The window util native library name <code>String</code>. */
    private static final String WINDOW_UTIL_LIBNAME;

    static {
        LOGGER = new Log4JWrapper();

        /* NOTE we have to load the java awt library first because it is a
         * dependency within the window util library - it is used to grab the
         * window handle */
        JAWT_LIBNAME = "jawt";
        WINDOW_UTIL_LIBNAME = "win32WindowUtil";

        loaded = false;
        try {
            System.loadLibrary(JAWT_LIBNAME);
            System.loadLibrary(WINDOW_UTIL_LIBNAME);
            loaded = true;
        } catch (final Throwable t) {
            LOGGER.logError(t, "Could not load native library.");
        }
    }

    /**
     * Create WindowUtil.
     *
     */
    public Win32WindowUtil() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.util.window.WindowUtil#applyRectangleEdges(java.awt.Window)
     *
     */
    public void applyRectangleEdges(final Window window) {
        // NOTE avoiding extra heap usage by borrowing a rectangle
        final Rectangle rectangle = window.getBounds();
        rectangle.x = 0;
        rectangle.y = 0;
        rectangle.width++;
        rectangle.height++;
        applyRectangleEdges(window, rectangle);
    }

    /**
     * @see com.thinkparity.ophelia.browser.util.window.WindowUtil#applyRoundedEdges(java.awt.Window, java.lang.Integer)
     *
     */
    public void applyRoundedEdges(final Window window, final Integer ellipseSize) {
        // NOTE avoiding extra heap usage by borrowing a rectangle
        final Rectangle rectangle = window.getBounds();
        rectangle.x = 0;
        rectangle.y = 0;
        rectangle.width++;
        rectangle.height++;
        // NOTE avoiding extra heap usage by borrowing a dimension
        final Dimension ellipseDimension = window.getSize();
        ellipseDimension.height = ellipseDimension.width = ellipseSize;
        applyRoundedEdges(window, rectangle, ellipseDimension);
    }

    /**
     * Apply a rectangular region's edges to a window.
     * 
     * @param window
     *            A <code>Window</code>.
     * @param rectangle
     *            A <code>Rectangle</code>.
     */
    private void applyRectangleEdges(final Window window,
            final Rectangle rectangle) {
        if (loaded)
            SetWindowRgn(GetWindowHandle(window), CreateRectRgn(rectangle.x,
                    rectangle.y, rectangle.x + rectangle.width ,
                    rectangle.y + rectangle.height), true);
        else
            LOGGER.logWarning("Win32WindowUtil had not been loaded.");
    }

    /**
     * Apply a rounded ellpise to the edge of the window.
     * 
     * @param window
     *            A <code>Window</code>.
     * @param rectangle
     *            A <code>Rectangle</code>.
     * @param ellipseDimension
     *            An eclipse <code>Dimension</code>.
     */
    private void applyRoundedEdges(final Window window,
            final Rectangle rectangle, final Dimension ellipseDimension) {
        if (loaded)
            SetWindowRgn(GetWindowHandle(window), CreateRoundRectRgn(rectangle.x,
                    rectangle.y, rectangle.x + rectangle.width,
                    rectangle.y + rectangle.height, ellipseDimension.width,
                    ellipseDimension.height), true);
        else
            LOGGER.logWarning("Win32WindowUtil had not been loaded.");
    }

    /**
     * The CreateRectRgn function creates a rectangular region.
     * 
     * @param nLeftRect
     *            The x-coordinate of upper-left corner.
     * @param nTopRect
     *            The y-coordinate of upper-left corner.
     * @param nRightRect
     *            The x-coordinate of lower-right corner.
     * @param nBottomRect
     *            The y-coordinate of lower-right corner.
     * @return If the function succeeds, the return value is the handle to the
     *         region. If the function fails, the return value is NULL.
     */
    private native int CreateRectRgn(final int nLeftRect, final int nTopRect,
            final int nRightRect, final int nBottomRect);

    /**
     * The CreateRoundRectRgn function creates a rectangular region with rounded
     * corners.
     * 
     * @param nLeftRect
     *            The x-coordinate of upper-left corner.
     * @param nTopRect
     *            The y-coordinate of upper-left corner.
     * @param nRightRect
     *            The x-coordinate of lower-right corner.
     * @param nBottomRect
     *            The y-coordinate of lower-right corner.
     * @param nWidthEllipse
     *            The height of ellipse.
     * @param nHeightEllipse
     *            The width of ellipse.
     * @return If the function succeeds, the return value is the handle to the
     *         region. If the function fails, the return value is NULL.
     */
    private native int CreateRoundRectRgn(final int nLeftRect,
            final int nTopRect, final int nRightRect, final int nBottomRect,
            final int nWidthEllipse, final int nHeightEllipse);

    /**
     * Obtain the native window handle.
     * 
     * @param window
     *            A <code>Window</code>.
     * @return The native window handle.
     */
    private native int GetWindowHandle(final Window window);

    /**
     * The SetWindowRgn function sets the window region of a window. The window
     * region determines the area within the window where the system permits
     * drawing. The system does not display any portion of a window that lies
     * outside of the window region
     * 
     * @param hWnd
     *            The handle to window.
     * @param hRgn
     *            The handle to region.
     * @param bRedraw
     *            The window redraw option.
     * @return If the function succeeds, the return value is nonzero. If the
     *         function fails, the return value is zero.
     */
    private native int SetWindowRgn(final int hWnd, final int hRgn,
            final boolean bRedraw);
}
