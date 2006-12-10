/*
 * Created On:  1-Dec-06 9:09:54 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.panel;

import javax.swing.Icon;

/**
 * <b>Title:</b>thinkParity Tab Panel Cell<br>
 * <b>Description:</b>The interface each cell within a tab panel's multiple
 * lists.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Cell {

    /**
     * Obtain the icon for the cell.
     * 
     * @return The <code>Icon</code>.
     */
    public Icon getIcon();

    /**
     * Obtain the text for the cell.
     * 
     * @return The text <code>String</code>.
     */
    public String getText();

    /**
     * Invoke the default action for the cell.
     * 
     */
    public void invokeAction();

    /**
     * Show a popup menu for the cell.
     * 
     * @param invoker
     *            The invoker <code>Component</code>.
     * @param x
     *            The x coordinate of the menu.
     * @param y
     *            The y coordinate of the menu.
     */
    public void showPopup();
}
