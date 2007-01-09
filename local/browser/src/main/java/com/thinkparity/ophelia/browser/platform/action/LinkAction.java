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
     * @param displayedFirst
     *            Indicates if this LinkAction is displayed first.
     * @return A <code>String</code>.
     */
    public String getIntroText(final Boolean displayedFirst);
    
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
    
    /**
     * Get the priority.
     * 
     * @return The <code>LinkPriority</code>.
     */
    public LinkPriority getPriority();
    
    public enum LinkType { CLEAR, SHOW_ALWAYS, SHOW_ONCE }
    public enum LinkPriority { HIGH, LOW }
}
