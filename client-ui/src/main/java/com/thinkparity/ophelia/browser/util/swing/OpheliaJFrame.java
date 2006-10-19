/*
 * Created On: Wed Oct 18, 2006 17:29
 */
package com.thinkparity.ophelia.browser.util.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.thinkparity.codebase.swing.AbstractJFrame;
import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.platform.application.window.WindowBorder;
import com.thinkparity.ophelia.browser.util.l2fprod.NativeSkin;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class OpheliaJFrame extends AbstractJFrame {

    /**
     * Create OpheliaJFrame
     * 
     * @param l18Context
     *            A localization context for the window.
     */
    protected OpheliaJFrame(final String l18Context) {
        super(l18Context);
        getRootPane().setBorder(new WindowBorder());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(com.thinkparity.ophelia.browser.Constants.Images.WINDOW_ICON_IMAGE);
        setUndecorated(true);
    }

    /**
     * Set the visiblitiy of the ophelia custom window. This will apply native
     * round corners on the window; as well as the window location before
     * finally setting the window visibility.
     * 
     * @param b
     *            if <code>true</code>, shows this component; otherwise,
     *            hides this component
     */
    @Override
    public void setVisible(boolean b) {
        if (b) {
            setLocation(calculateLocation());
            new NativeSkin().roundCorners(this);
        }
        super.setVisible(b);
    }

    /**
     * Set the visibility of the ophelia JFrame to true; and block until it is
     * closed.
     * 
     */
    public final void setVisibleAndWait() {
        addWindowListener(new WindowAdapter() {
            public void windowClosed(final WindowEvent e) {
                synchronized(this) {
                    notifyAll();
                }
            }
        });
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    setVisible(true);
                }
            });
        } catch (final Throwable t) {
            throw new BrowserException(
                    "Error opening the thinkParity Ophelia JFrame.", t);
        }
        synchronized (this) {
            try {
                wait();
            } catch (final Throwable t) {
                throw new BrowserException(
                        "Error opening the thinkParity Ophelia JFrame.", t);
            }
        }
    }
}
