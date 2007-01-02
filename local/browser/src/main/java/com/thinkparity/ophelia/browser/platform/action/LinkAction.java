/**
 * Created On: Dec 24, 2006 1:34:37 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action;


/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface LinkAction {
        
    /**
     * Get the action.
     * 
     * @return An <code>Action</code>.
     */
    public javax.swing.Action getAction();
    
    /**
     * Get the action name.
     * This is relevant for SHOW_ALWAYS and CLEAR_SHOW_ALWAYS.
     * 
     * @return An action name <code>String</code>.
     */
    public String getActionName();
       
    /**
     * Get the localized intro text.
     * 
     * @return A <code>String</code>.
     */
    public String getIntroText();
    
    /**
     * Get the link type.
     * 
     * @return The <code>LinkType</code>.
     */
    public LinkType getLinkType();
    
    /**
     * Get the localized link text.
     * 
     * @return A <code>String</code>.
     */
    public String getLinkText();
    
    public enum LinkType { CLEAR_SHOW_ALWAYS, SHOW_ALWAYS, SHOW_ONCE }
}
