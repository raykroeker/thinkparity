/**
 * Created On: Dec 24, 2006 1:34:37 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action;

import javax.swing.Action;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface LinkAction {
    
    /**
     * Get the text.
     * 
     * @return A <code>String</code>.
     */
    public String getText();
    
    /**
     * Get the action.
     * 
     * @return An <code>Action</code>.
     */
    public Action getAction();
}
