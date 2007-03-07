/**
 * Created On: 4-Jul-2006 3:52:46 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity OpheliaUI Browser Application Create Outgoing EMail
 * Invitation Action<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CreateOutgoingEMailInvitation extends AbstractAction {

    /** The browser application. */
    private final Browser browser;
    
    /**
     * Create CreateOutgoingEMailInvitation.
     * 
     * @param browser
     *            The <code>Browser</code> application.
     */
    public CreateOutgoingEMailInvitation(final Browser browser) {
        super(ActionId.CONTACT_CREATE_OUTGOING_EMAIL_INVITATION);
        this.browser = browser;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final EMail contactEmail = (EMail) data.get(DataKey.CONTACT_EMAIL);
        if (null == contactEmail) {
            browser.displayContactCreateInvitation();
        }
        else {
            getContactModel().createOutgoingEMailInvitation(contactEmail);
        }
    }

    /**
     * The key used to set\get the data.
     * 
     * @see Data
     */
    public enum DataKey { CONTACT_EMAIL }
}
