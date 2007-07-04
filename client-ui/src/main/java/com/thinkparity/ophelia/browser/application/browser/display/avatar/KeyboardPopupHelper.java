/**
 * Created On: 4-Jul-07 12:48:22 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserWindow;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class KeyboardPopupHelper {

    /**
     * A client property key <code>String</code> used to store the mouse
     * adapter within the component.
     */
    private static final String LISTENER_CLIENT_PROPERTY_KEY;

    static {
        LISTENER_CLIENT_PROPERTY_KEY = new StringBuffer(KeyboardPopupHelper.class
                .getName()).append("#popupListener").toString();
    }

    /** A <code>Browser</code>. */
    final Browser browser;

    /**
     * Create KeyboardPopupHelper.
     */
    public KeyboardPopupHelper() {
        super();
        this.browser = getBrowser();
    }

    /**
     * Add a popup listener.
     * 
     * @param jComponent
     *            A <code>JComponent</code>.
     * @param popupAction
     *            A <code>Runnable</code> that shows the popup.
     */
    public void addPopupListener(final JComponent jComponent,
            final Runnable popupAction) {
        // property to protect against adding it twice
        PropertyChangeListener listener =
            (PropertyChangeListener) jComponent.getClientProperty(LISTENER_CLIENT_PROPERTY_KEY);
        if (null == listener) {
            listener = new PropertyChangeListener() {
                public void propertyChange(final PropertyChangeEvent e) {
                    if (jComponent.hasFocus() && !MenuFactory.isPopupMenu()) {
                        popupAction.run();
                    }
                }
            };
            getMainWindow().addPropertyChangeListener("showPopup", listener);
            jComponent.putClientProperty(LISTENER_CLIENT_PROPERTY_KEY, listener);
        }
    }

    /**
     * Remove a request popup listener from the component.
     * 
     * @param jComponent
     *            A <code>JComponent</code>.
     */
    void removeListener(final JComponent jComponent) {
        PropertyChangeListener listener =
            (PropertyChangeListener) jComponent.getClientProperty(LISTENER_CLIENT_PROPERTY_KEY);
        if (null != listener) {
            getMainWindow().removePropertyChangeListener(listener);
            jComponent.putClientProperty(LISTENER_CLIENT_PROPERTY_KEY, null);
            listener = null;
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

    /**
     * Get the main window.
     */
    private BrowserWindow getMainWindow() {
        return (BrowserWindow)browser.getMainWindow();
    }
}
