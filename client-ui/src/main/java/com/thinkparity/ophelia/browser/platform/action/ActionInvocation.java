/**
 * Created On: Dec 26, 2006 12:17:16 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface ActionInvocation {
    
    /**
     * Invoke the action.
     * 
     * @param data
     *            The action data.
     */
    public void invokeAction(final Data data);
}
