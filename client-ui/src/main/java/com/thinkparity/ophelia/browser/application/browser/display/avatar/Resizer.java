/**
 * Created On: 30-Aug-06 9:16:21 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;


import com.thinkparity.ophelia.browser.Constants.Resize;
import com.thinkparity.ophelia.browser.application.browser.Browser;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class Resizer {
    
    /** Browser */
    private Browser browser;
    
    /** Component */
    private Component component;

    /** Form location */
    private FormLocation formLocation;
    
    /** Flag to indicate if currently dragging. */
    private static Boolean dragging = Boolean.FALSE;
    
    /** Flag to indicate if initialized. */
    private Boolean initialized = Boolean.FALSE;

    /** Resize mode. */
    private ResizeDirection resizeDirection = ResizeDirection.NONE;
    
    /** The resize offset size in the x direction. */
    private int resizeOffsetX;    
    
    /** The resize offset size in the y direction. */
    private int resizeOffsetY;
      
    /**
     * Create a Resizer (to resize the Browser).
     * 
     * @param browser
     *            The browser.
     * @param component
     *            The component.
     */
    public Resizer(final Browser browser, final Component component) {
        this(browser, component, FormLocation.NO_EDGE);
    }
     
    /**
     * Create a Resizer (to resize the Browser).
     * 
     * @param browser
     *            The browser.
     * @param component
     *            The component.
     * @param formLocation
     *            The form location (eg. top, middle, bottom)
     */
    public Resizer(final Browser browser, final Component component, final FormLocation formLocation) {
        this.browser = browser;
        this.component = component;
        this.formLocation = formLocation;
        setResizeEdges(formLocation);
    }

    /**
     * Set the browser to resize.
     * 
     * @param browser
     *            The browser.
     */
    public void setBrowser(final Browser browser) {
        this.browser = browser;
    }
    
    /**
     * Set the behavior of resize.
     * 
     * @param formLocation
     *            The form location (eg. top, middle, bottom)
     */
    public void setResizeEdges(FormLocation formLocation) {
        this.formLocation = formLocation;
        if (formLocation != FormLocation.NO_EDGE) {
            initComponents();
        }
    }
    
    /**
     * These get and set methods are used by classes that intend to do
     * their own resizing. (For example, the bottom right resize control.)
     */
    public Boolean isResizeDragging() {
        return dragging;
    }
    public void setResizeDragging(Boolean newValue) {
        Resizer.dragging = newValue;
    }
       
    private void initResize(java.awt.event.MouseEvent e) {
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
        
        resizeDirection = filterAllowedDirections(resizeDirection);
        
        adjustCursor(resizeDirection);
    }
    
    private ResizeDirection filterAllowedDirections(ResizeDirection rd) {
        ResizeDirection newDirection = rd;
        
        switch (formLocation) {
        case NO_EDGE:
            resizeDirection = ResizeDirection.NONE;
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
            resizeDirection = ResizeDirection.NONE;
            break;
        }
            
        return newDirection;
    }
    
    private void adjustCursor(ResizeDirection resizeDirection) {
        final Cursor cursor;
        Boolean defaultCursor = Boolean.FALSE;
        
        switch (resizeDirection) {
        case NW:
            cursor = new Cursor(Cursor.NW_RESIZE_CURSOR);
            break;
        case NE:
            cursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
            break;
        case SW:
            cursor = new Cursor(Cursor.SW_RESIZE_CURSOR);
            break;
        case SE:
            cursor = new Cursor(Cursor.SE_RESIZE_CURSOR);
            break;
        case W:
            cursor = new Cursor(Cursor.W_RESIZE_CURSOR);
            break;
        case E:
            cursor = new Cursor(Cursor.E_RESIZE_CURSOR);
            break;
        case N:
            cursor = new Cursor(Cursor.N_RESIZE_CURSOR);
            break;
        case S:
            cursor = new Cursor(Cursor.S_RESIZE_CURSOR);
            break;
        default:
            cursor = new Cursor(Cursor.DEFAULT_CURSOR);
            defaultCursor = Boolean.TRUE;
            break;
        }
        
        if (defaultCursor) {
            component.setCursor(null);
            browser.setCursor(null);
        }
        else {
            component.setCursor(cursor);
            browser.setCursor(cursor);
        }
    }
    
    private void formMouseDragged(java.awt.event.MouseEvent evt) {
        final double oldWidth = component.getSize().getWidth();
        final double oldHeight = component.getSize().getHeight();
        
        switch (resizeDirection) {
        case NW:
            browser.moveAndResizeBrowserWindow(
                    new Point(evt.getPoint().x - resizeOffsetX, evt.getPoint().y - resizeOffsetY),
                    new Dimension(resizeOffsetX - evt.getPoint().x, resizeOffsetY - evt.getPoint().y));
            break;
        case NE:
            browser.moveAndResizeBrowserWindow(
                    new Point(0, evt.getPoint().y - resizeOffsetY),
                    new Dimension(evt.getPoint().x - resizeOffsetX, resizeOffsetY - evt.getPoint().y));
            break;
        case SW:
            browser.moveAndResizeBrowserWindow(
                    new Point(evt.getPoint().x - resizeOffsetX, 0),
                    new Dimension(resizeOffsetX - evt.getPoint().x, evt.getPoint().y - resizeOffsetY));
            break;
        case SE:           
            browser.resizeBrowserWindow(
                    new Dimension(evt.getPoint().x - resizeOffsetX, evt.getPoint().y - resizeOffsetY));
            break;
        case W:
            browser.moveAndResizeBrowserWindow(
                    new Point(evt.getPoint().x - resizeOffsetX, 0),
                    new Dimension(resizeOffsetX - evt.getPoint().x, 0));
            break;            
        case E:
            browser.resizeBrowserWindow(
                    new Dimension(evt.getPoint().x - resizeOffsetX, 0));
            break;
        case N:
            browser.moveAndResizeBrowserWindow(
                    new Point(0, evt.getPoint().y - resizeOffsetY),
                    new Dimension(0, resizeOffsetY - evt.getPoint().y));
            break;
        case S:
            browser.resizeBrowserWindow(
                    new Dimension(0, evt.getPoint().y - resizeOffsetY));
            break;
        default:
            break;
        }
        
        // The width of the control may change when the window is resized.
        if ((resizeDirection == ResizeDirection.NE) ||
            (resizeDirection == ResizeDirection.E) ||
            (resizeDirection == ResizeDirection.SE)) {
            resizeOffsetX += (component.getSize().getWidth() - oldWidth);
        }
        
        // The height of the control may change when the window is resized.
        if ((resizeDirection == ResizeDirection.SW) ||
            (resizeDirection == ResizeDirection.S) ||
            (resizeDirection == ResizeDirection.SE)) {
            resizeOffsetY += (component.getSize().getHeight() - oldHeight);
        }
    }
    
    private void formMouseEntered(java.awt.event.MouseEvent evt) {
        if (!dragging) {
            initResize(evt);
        }
    }
    
    private void formMouseMoved(java.awt.event.MouseEvent evt) {
        if (!dragging) {
            initResize(evt);
        }
    }
    
    private void formMouseExited(java.awt.event.MouseEvent evt) {
        if (!dragging) {
            resizeDirection = ResizeDirection.NONE;
            component.setCursor(null);
            browser.setCursor(null);
        }
    }    
    
    private void formMousePressed(java.awt.event.MouseEvent evt) {
        resizeOffsetX = evt.getPoint().x;
        resizeOffsetY = evt.getPoint().y;
        if (resizeDirection != ResizeDirection.NONE) {
            dragging = Boolean.TRUE;
        }
    }   
       
    private void formMouseReleased(java.awt.event.MouseEvent evt) {
        dragging = Boolean.FALSE;
        
        // Sometimes when the mouse is released it is not over the
        // component anymore, for example, when minimizing beyond
        // the minimum size of the dialog. For this reason it is
        // best to reset the cursor rather than hope for the 
        // mouse exit event.
        resizeDirection = ResizeDirection.NONE;
        component.setCursor(null);
        browser.setCursor(null);
    }
    
    private void initComponents() {
        if (!initialized) {
            component.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                public void mouseDragged(java.awt.event.MouseEvent evt) {
                    formMouseDragged(evt);
                }
                public void mouseMoved(java.awt.event.MouseEvent evt) {
                    formMouseMoved(evt);
                }
            });
            component.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    formMouseEntered(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    formMouseExited(evt);
                }
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    formMousePressed(evt);
                }
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    formMouseReleased(evt);
                }
            });
            initialized = Boolean.TRUE;
        }
    }
      
    private enum ResizeDirection { E, N, NE, NONE, NW, S, SE, SW, W }
    public enum FormLocation { NO_EDGE, TOP, MIDDLE, BOTTOM, LEFT, RIGHT }
}
