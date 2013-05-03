/**
 * Created On: 4-Jul-2006 3:52:46 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity OpheliaUI Browser Application Create Outgoing User
 * Invitation Action<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CreateOutgoingUserInvitation extends AbstractBrowserAction {

    /** The <code>Browser</code> application. */
    private final Browser browser;

    /**
     * Create CreateOutgoingUserInvitation.
     * 
     * @param browser
     *            The <code>Browser</code> application.
     */
    public CreateOutgoingUserInvitation(final Browser browser) {
        super(ActionId.CONTACT_CREATE_OUTGOING_USER_INVITATION);
        this.browser = browser;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final Long userId = (Long) data.get(DataKey.USER_ID);

        final User user = getUserModel().read(userId);
        if (browser.confirm("ContactOutgoingUserInvitationCreate.ConfirmCreateMessage",
                new Object[] {user.getName()})) {
            getContactModel().createOutgoingUserInvitation(userId);
        }

    }

    /**
     * The key used to set\get the data.
     * 
     * @see Data
     */
    public enum DataKey { USER_ID }
}
