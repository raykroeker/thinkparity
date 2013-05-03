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
     * Get the display name associated with the 'common' action.
     * 
     * @return The common action display name <code>String</code>.
     */
    public String getCommonActionDisplayName();

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
     */
    public void invokeAction();

    /**
     * Invoke the 'common' action for the cell.
     */
    public void invokeCommonAction();

    /**
     * Invoke the delete action for the cell.
     */
    public void invokeDeleteAction();

    /**
     * Determine whether or not an action is available for the cell.
     * 
     * @return True if an action is available.
     */
    public Boolean isActionAvailable();

    /**
     * Determine whether or not a delay is required between invokes
     * to the action. This affects whether the action can be called
     * on multiple key presses.
     * 
     * @return True if an action delay is required.
     */
    public Boolean isActionDelayRequired();

    /**
     * Determine if the 'common' action is available for the cell.
     * 
     * @return True if the 'common' action is available for the cell.
     */
    public Boolean isCommonActionAvailable();

    /**
     * Determine if the 'common' action is currently enabled.
     * For example, the action may be disabled when offline.
     * 
     * @return True if the 'common' action is currently enabled.
     */
    public Boolean isCommonActionEnabled();

    /**
     * Determine whether or not a delete action is available for the cell.
     * 
     * @return True if a delete action is available.
     */
    public Boolean isDeleteActionAvailable();

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
     * If showAll is false, commands most relevant for the cell are displayed.
     * If showAll is true, a broader set of commands are displayed.
     * 
     * @param showAll
     *            A <code>Boolean</code>, true if all commands are displayed.
     */
    public void showPopup(final Boolean showAll);
}
