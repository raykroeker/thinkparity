/**
 * Created On: 11-Jun-07 9:58:59 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface TabButtonActionDelegate {

    /**
     * Determine if an action is available for a tab button.
     * 
     * @return true if an action is available for a tab button.
     */
    public Boolean isTabButtonActionAvailable();

    /**
     * Invoke an action for a tab button.
     */
    public void invokeForTabButton();
}
