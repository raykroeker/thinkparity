/**
 * Created On: 31-Aug-06 9:42:57 AM
 * $Id$
 */
package com.thinkparity.browser.application.browser;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class BrowserResizeListener extends MouseInputAdapter {

    /** The glass pane. */
    private final Component glassPane;
    
    /** The content pane. */
    
    private final Component contentPane;
        
    /**
     * Create a browser resize listener.
     */
    public BrowserResizeListener(Component glassPane, Component contentPane) {
        super();
        this.glassPane = glassPane;
        this.contentPane = contentPane;
    }

    public void mouseMoved(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    public void mouseDragged(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    public void mouseClicked(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    public void mouseEntered(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    public void mouseExited(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    public void mousePressed(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    public void mouseReleased(MouseEvent e) {
        redispatchMouseEvent(e);
    }
    
    private void redispatchMouseEvent(java.awt.event.MouseEvent e) {
        Point glassPanePoint = e.getPoint();
        Point containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, contentPane);
        
        // If containerPoint.y<0 then we're not in the content pane.
        if (containerPoint.y >= 0) {
            // Find out what component the mouse event is over.
            Component component = SwingUtilities.getDeepestComponentAt(
                    contentPane, containerPoint.x, containerPoint.y);
            if (component != null) {
                Point componentPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, component);
                component.dispatchEvent(
                        new MouseEvent(component, e.getID(), e.getWhen(), e.getModifiers(),
                                       componentPoint.x, componentPoint.y, e.getClickCount(),
                                       e.isPopupTrigger(), e.getButton()));
            }
        }
    }
    
    
    

    // The glass pane.
/*    private final Component glassPane;
    private final Component contentPane;
    
    public BrowserResizeListener(final JFrame jFrame) {
        this.glassPane = jFrame.getGlassPane();
        this.contentPane = jFrame.getContentPane();
        initComponents();
    }
    
    private void redispatchMouseEvent(java.awt.event.MouseEvent e) {
        Point glassPanePoint = e.getPoint();
        Point containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, contentPane);*/
        
        // If containerPoint.y<0 then we're not in the content pane.
/*        if (containerPoint.y>=0) {
            // Find out what component the mouse event is over.
            Component component = SwingUtilities.getDeepestComponentAt(
                    contentPane, containerPoint.x, containerPoint.y);
            if (component != null) {
                Point componentPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, component);
                component.dispatchEvent(
                        new MouseEvent(component, e.getID(), e.getWhen(), e.getModifiers(),
                                       componentPoint.x, componentPoint.y, e.getClickCount(),
                                       e.isPopupTrigger(), e.getButton()));
            }
        }*/
        
        
/*        Container container = contentPane;

        if (containerPoint.y < 0) { // we're not in the content pane
        // Could have special code to handle mouse events over
        // the menu bar or non-system window decorations, such as
        // the ones provided by the Java look and feel.
        } else {
            // The mouse event is probably over the content pane.
            // Find out exactly which component it's over.
            Component component = SwingUtilities.getDeepestComponentAt(
                    container, containerPoint.x, containerPoint.y);

            if ((component != null) && (component.equals(liveButton))) {
                // Forward events over the check box.
                Point componentPoint = SwingUtilities.convertPoint(glassPane,
                        glassPanePoint, component);
                component
                        .dispatchEvent(new MouseEvent(component, e.getID(), e
                                .getWhen(), e.getModifiers(), componentPoint.x,
                                componentPoint.y, e.getClickCount(), e
                                        .isPopupTrigger()));
            }
        }*/
//    }

}
