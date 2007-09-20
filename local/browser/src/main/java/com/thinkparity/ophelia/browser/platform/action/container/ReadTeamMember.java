/*
 * Created On: 27-Jun-2006 2:25:50 PM
 */
package com.thinkparity.ophelia.browser.platform.action.container;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.user.UserModel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity Ophelia UI Container Read Team Member<br>
 * <b>Description:</b>Read the team member and display within an avatar.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadTeamMember extends AbstractBrowserAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create ReadTeamMember.
     * 
     * @param browser
     *            A <code>Browser</code>.
     */
    public ReadTeamMember(final Browser browser) {
        super(ActionId.CONTAINER_READ_TEAM_MEMBER);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final Long userId = (Long) data.get(DataKey.USER_ID);
        
        final ContactModel contactModel = getContactModel();
        final Runnable runnable;
        if (contactModel.doesExist(userId)) {
            final Contact contact = contactModel.read(userId);
            runnable = new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                public void run() {
                    browser.displayContainerTeamMemberInfoAvatar(contact);
                }
            };
        } else {
            final UserModel userModel = getUserModel();
            final User user = userModel.read(userId);
            final Boolean allowInvite = getProfileModel().isInviteAvailable(user);
            runnable = new Runnable() {
                /**
                 * @see java.lang.Runnable#run()
                 *
                 */
                public void run() {
                    browser.displayContainerTeamMemberInfoAvatar(user,
                            allowInvite);
                }
            };
        }
        SwingUtil.ensureDispatchThread(runnable);
    }

    /** <b>Title:</b>Read Team Member Data Key<br> */
    public enum DataKey { USER_ID }
}
