/**
 * Created On: Mar 27, 2007 1:16:04 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab;

import javax.swing.Action;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface TabAvatarFilterBy {

    /**
     * Get the action.
     * 
     * @return The <code>Action</code>.
     */
    public Action getAction();

    /**
     * Get the unique name.
     * 
     * @return The unique name <code>String</code>.
     */
    public String getName();

    /**
     * Get the text (appropriate for a menu).
     * 
     * @return The text <code>String</code>.
     */
    public String getText();
}
