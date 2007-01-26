/**
 * Created On: Jan 25, 2007 11:45:00 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class BrowserPopupHelper {

    /** The browser popup delegate. */
    private final BrowserPopupDelegate browserPopupDelegate;

    /**
     * A client property key <code>String</code> used to store the mouse input
     * adapter within the component.
     */
    private static final String LISTENER_CLIENT_PROPERTY_KEY;

    static {
        LISTENER_CLIENT_PROPERTY_KEY = new StringBuffer(BrowserPopupHelper.class.getName())
            .append("#mouseInputListener").toString();
    }

    /**
     * Create a BrowserPopupHelper.
     */
    public BrowserPopupHelper() {
        super();
        this.browserPopupDelegate = new BrowserPopupDelegate();
    }

    /**
     * Add a popup listener to the component.
     * 
     * @param jComponent
     *            A <code>JComponent</code>.
     */
    public void addPopupListener(final JComponent jComponent) {
        // property to protect against adding it twice
        MouseInputListener mouseInputListener =
            (MouseInputListener) jComponent.getClientProperty(LISTENER_CLIENT_PROPERTY_KEY);
        if (null == mouseInputListener) {
            mouseInputListener = new MouseInputAdapter() {
                @Override
                public void mousePressed(final MouseEvent e) {
                    componentMousePressed(e);
                }
                @Override
                public void mouseReleased(final MouseEvent e) {
                    componentMouseReleased(e);
                }
            };
            jComponent.addMouseListener(mouseInputListener);
            jComponent.putClientProperty(LISTENER_CLIENT_PROPERTY_KEY, mouseInputListener);
        }
    }

    /**
     * Remove a popup listener from the component.
     * 
     * @param jComponent
     *            A <code>JComponent</code>.
     */
    public void removePopupListener(final JComponent jComponent) {
        MouseInputListener mouseInputListener =
            (MouseInputListener) jComponent.getClientProperty(LISTENER_CLIENT_PROPERTY_KEY);
        if (null != mouseInputListener) {
            jComponent.removeMouseListener(mouseInputListener);
            jComponent.putClientProperty(LISTENER_CLIENT_PROPERTY_KEY, null);
        }        
    }

    /**
     * The component's mouse pressed event was fired.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void componentMousePressed(final MouseEvent e) {
        if (e.getClickCount() == 1 && e.isPopupTrigger()) {
            showBrowserPopup(e);           
        }
    }

    /**
     * The component's mouse released event was fired.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void componentMouseReleased(final MouseEvent e) {
        if (e.getClickCount() == 1 && e.isPopupTrigger()) {
            showBrowserPopup(e);           
        }
    }

    /**
     * Show the browser popup.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void showBrowserPopup(final MouseEvent e) {
        browserPopupDelegate.initialize((Component) e.getSource(), e.getX(), e.getY());
        browserPopupDelegate.show();
    }
}
