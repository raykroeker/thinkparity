/**
 * Created On: 30-Aug-06 9:16:21 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;

import com.thinkparity.browser.Constants.Resize;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public abstract class EdgeAvatar extends Avatar {

    /** Form location */
    private FormLocation formLocation = FormLocation.NO_EDGE;
    
    /** Flag to indicate if currently dragging. */
    protected Boolean dragging = Boolean.FALSE;

    /** Resize mode. */
    private ResizeDirection resizeDirection = ResizeDirection.NONE;
    
    /** The resize offset size in the x direction. */
    protected int resizeOffsetX;    
    
    /** The resize offset size in the y direction. */
    protected int resizeOffsetY;
     
    /**
     * Create a EdgeAvatar.
     * 
     * @param formLocation
     *            The form location (eg. top, middle, bottom)
     * @param l18nContext
     *            The localization context.
     */
    protected EdgeAvatar(final FormLocation formLocation, final String l18nContext) {
        super(l18nContext);
        this.formLocation = formLocation;
        initComponents();
    }
    
    private void initResize(java.awt.event.MouseEvent e) {
        final Point p = e.getPoint();
        final Dimension d = getSize();
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
            break;
        }
        
        getController().setCursor(cursor);
    }
    
    private void formMouseDragged(java.awt.event.MouseEvent evt) {
        final double oldWidth = getSize().getWidth();
        final double oldHeight = getSize().getHeight();
        
        switch (resizeDirection) {
        case SW:
            getController().moveAndResizeBrowserWindow(
                    new Point(evt.getPoint().x - resizeOffsetX, 0),
                    new Dimension(resizeOffsetX - evt.getPoint().x, evt.getPoint().y - resizeOffsetY));
            break;
        case SE:           
            getController().resizeBrowserWindow(
                    new Dimension(evt.getPoint().x - resizeOffsetX, evt.getPoint().y - resizeOffsetY));
            break;
        case W:
            getController().moveAndResizeBrowserWindow(
                    new Point(evt.getPoint().x - resizeOffsetX, 0),
                    new Dimension(resizeOffsetX - evt.getPoint().x, 0));
            break;            
        case E:
            getController().resizeBrowserWindow(
                    new Dimension(evt.getPoint().x - resizeOffsetX, 0));
            break;
        case N:
            getController().moveAndResizeBrowserWindow(
                    new Point(0, evt.getPoint().y - resizeOffsetY),
                    new Dimension(0, resizeOffsetY - evt.getPoint().y));
            break;
        case S:
            getController().resizeBrowserWindow(
                    new Dimension(0, evt.getPoint().y - resizeOffsetY));
            break;
        default:
            break;
        }
        
        // The width of the control may change when the window is resized.
        if ((resizeDirection == ResizeDirection.NE) ||
            (resizeDirection == ResizeDirection.E) ||
            (resizeDirection == ResizeDirection.SE)) {
            resizeOffsetX += (getSize().getWidth() - oldWidth);
        }
        
        // The height of the control may change when the window is resized.
        if ((resizeDirection == ResizeDirection.SW) ||
            (resizeDirection == ResizeDirection.S) ||
            (resizeDirection == ResizeDirection.SE)) {
            resizeOffsetY += (getSize().getHeight() - oldHeight);
        }
    }
    
    private void formMouseEntered(java.awt.event.MouseEvent evt) {
        if (!dragging) {
            initResize(evt);
        }
    }
    
    private void formMouseExited(java.awt.event.MouseEvent evt) {
        if (!dragging) {
            resizeDirection = ResizeDirection.NONE;
            getController().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void formMouseMoved(java.awt.event.MouseEvent evt) {
        if (!dragging) {
            initResize(evt);
        }
    }

    private void formMousePressed(java.awt.event.MouseEvent evt) {
        resizeOffsetX = evt.getPoint().x;
        resizeOffsetY = evt.getPoint().y;
        dragging = Boolean.TRUE;
    }
    
    private void formMouseReleased(java.awt.event.MouseEvent evt) {
        dragging = Boolean.FALSE;
    }
    
    private void initComponents() {
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
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
    }
    
    private enum ResizeDirection { E, N, NE, NONE, NW, S, SE, SW, W }
    protected enum FormLocation { NO_EDGE, TOP, MIDDLE, BOTTOM }
}
