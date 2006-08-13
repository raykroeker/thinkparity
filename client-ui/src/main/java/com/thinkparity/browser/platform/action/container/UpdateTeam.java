/**
 * Created On: 2-Aug-06 3:08:34 PM
 * $Id$
 */
package com.thinkparity.browser.platform.action.container;

import java.util.List;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class UpdateTeam extends AbstractAction {

    /** The browser application. */
    private final Browser browser;

    /**
     * Create UpdateTeam.
     * 
     * @param browser
     *            The browser application.
     */
    public UpdateTeam(final Browser browser) {
        super(ActionId.CONTAINER_UPDATE_TEAM);
        this.browser = browser;
    }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        final Long containerId = (Long) data.get(DataKey.CONTAINER_ID);
        final List<User> teamMembers = getDataUsers(data, DataKey.TEAM_MEMBERS);
        if (null==teamMembers) {
            // Launch the ManageTeam dialog to get the team members.
            // If the user presses OK, it will call back into this action
            // with the team members provided.
            browser.displayUpdateContainerTeamDialog(containerId);          
        }
        else {
            getContainerModel().updateTeam(containerId, teamMembers);
            browser.fireContainerTeamChanged(containerId, Boolean.FALSE);
        }                
    }

    public enum DataKey { CONTAINER_ID, TEAM_MEMBERS }
}
