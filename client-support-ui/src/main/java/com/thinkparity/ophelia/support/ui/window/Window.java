/*
 * Created On:  Nov 19, 2007 10:12:31 AM
 */
package com.thinkparity.ophelia.support.ui.window;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Window {

    /**
     * Close the window.
     *
     */
    void close();

    /**
     * Obtain the window id.
     * 
     * @return A <code>String</code>.
     */
    String getId();

    /**
     * Set the window's visiblity.
     * 
     * @param visible
     *            A <code>boolean</code>.
     */
    void setVisible(boolean visible);
}
