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
     * Obtain the additional text.
     * 
     * @return Additiona text <code>String</code>.
     */
    public String getAdditionalText();

    /**
     * Obtain the icon for the cell.
     * 
     * @return The <code>Icon</code>.
     */
    public Icon getIcon();

    /**
     * Obtain the id.
     * 
     * @return The <code>String</code> id.
     */
    public String getId();

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
     * Determine whether or not an action is available for the cell.
     * 
     * @return True if an action is available.
     */
    public Boolean isActionAvailable();

    /**
     * Determine whether or not a cell should be emphasized (eg bold).
     * 
     * @return True if it should be emphasized (eg. bold).
     */
    public Boolean isEmphasized();

    /**
     * Determine whether or not a cell is enabled.
     * 
     * @return True if it is enabled.
     */
    public Boolean isEnabled();

    /**
     * Determine whether or not a popup is available for the cell.
     * 
     * @return True if a popup is available.
     */
    public Boolean isPopupAvailable();

    /**
     * Determine whether or not additional text is set.
     * 
     * @return True if the additional text is set.
     */
    public Boolean isSetAdditionalText();

    /**
     * Determine whether or not text is set.
     * 
     * @return True if the text is set.
     */
    public Boolean isSetText();

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
