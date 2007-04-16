/*
 * Created On: 2007-04-14 10:48 -0700
 */
package com.thinkparity.ophelia.browser.util.window;

import java.awt.Window;

/**
 * <b>Title:</b>thinkParity OpheliaUI Window Util<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface WindowUtil {

    /**
     * Apply the window's original rectangle edges.
     * 
     * @param window
     *            A <code>Window</code>.
     */
    public void applyRectangleEdges(final Window window);
    
    /**
     * Apply an elliptical edge to a window.
     * 
     * @param window
     *            A <code>Window</code>.
     * @param ellipseSize
     *            The size (both vertical and horizonal) of the ellipse.
     */
    public void applyRoundedEdges(final Window window,
            final Integer ellipseSize);
}
