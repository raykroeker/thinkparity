/**
 * Created On: 4-Jul-07 2:24:13 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class FocusHelper {

    /**
     * A client property key <code>String</code> used to store the mouse
     * adapter within the component.
     */
    private static final String LISTENER_CLIENT_PROPERTY_KEY;

    static {
        LISTENER_CLIENT_PROPERTY_KEY = new StringBuffer(FocusHelper.class
                .getName()).append("#mouseListener").toString();
    }

    /** A <code>Browser</code>. */
    final Browser browser;

    /**
     * Create FocusHelper.
     */
    public FocusHelper() {
        super();
        this.browser = getBrowser();
    }

    /**
     * Add a mouse listener to the component that redirects focus appropriately.
     * 
     * @param jComponent
     *            A <code>JComponent</code>.
     */
    void addFocusListener(final JComponent jComponent) {
        // property to protect against adding it twice
        MouseListener mouseListener =
            (MouseListener) jComponent.getClientProperty(LISTENER_CLIENT_PROPERTY_KEY);
        if (null == mouseListener) {
            mouseListener = new MouseAdapter() {
                @Override
                public void mousePressed(final MouseEvent e) {
                    browser.requestFocusInTab();
                }
            };
            jComponent.addMouseListener(mouseListener);
            jComponent.putClientProperty(LISTENER_CLIENT_PROPERTY_KEY, mouseListener);
        }
    }

    /**
     * Remove a request focus listener from the component.
     * 
     * @param jComponent
     *            A <code>JComponent</code>.
     */
    void removeListener(final JComponent jComponent) {
        MouseListener mouseListener =
            (MouseListener) jComponent.getClientProperty(LISTENER_CLIENT_PROPERTY_KEY);
        if (null != mouseListener) {
            jComponent.removeMouseListener(mouseListener);
            jComponent.putClientProperty(LISTENER_CLIENT_PROPERTY_KEY, null);
            mouseListener = null;
        }    
    }

    /**
     * Obtain the browser application.
     * 
     * @return The browser application.
     */
    private Browser getBrowser() {
        return (Browser) new ApplicationRegistry().get(ApplicationId.BROWSER);
    }
}
