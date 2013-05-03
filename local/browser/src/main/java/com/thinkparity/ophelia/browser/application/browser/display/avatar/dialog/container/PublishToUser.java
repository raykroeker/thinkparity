/**
 * Created On: 8-Aug-07 4:56:35 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.dialog.container;

import javax.swing.Action;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface PublishToUser {

    /**
     * Get the action.
     * 
     * @return The <code>Action</code>.
     */
    public Action getAction();

    /**
     * Get the text (appropriate for a popup menu).
     * 
     * @return The text <code>String</code>.
     */
    public String getPopupText();
}
